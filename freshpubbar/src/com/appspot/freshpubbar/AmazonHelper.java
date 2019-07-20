package com.appspot.freshpubbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/* Amazon API用のヘルパークラス */
public final class AmazonHelper {
    /* 検索条件フィールド */
    private String title;
    private String author;
    private String publisher;

    /* コンストラクタ */
    public AmazonHelper(String title, String author, String publisher) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
    }

    /* 新刊情報を返す */
    public List<FreshPub> getFreshPubs() throws Exception {
        /* APIに指定する構成要素や条件を用意する */
        Map<String, String> params = prepareParameters();

        /* 用意したオブジェクトを使って書籍を検索して結果を返す */
        return search(params);
    }

    /* APIにしている構成要素や条件を用意する */
    private Map<String, String> prepareParameters() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2010-11-01");
        params.put("Operation", "ItemSearch");
        params.put("SearchIndex", "Books");
        params.put("Sort", "daterank");
        params.put("ResponseGroup", "ItemAttributes");
        if (title != null && !"".equals(title)) {
            params.put("Title", title);
        }
        if (author != null && !"".equals(author)) {
            params.put("Author", author);
        }
        if (publisher != null && !"".equals(publisher)) {
            params.put("Publisher", publisher);
        }
        return params;
    }

    /* 指定された条件で書籍を検索する */
    private List<FreshPub> search(Map<String, String> params) throws Exception {
        /* 返り値となる検索結果を保存するリスト */
        ArrayList<FreshPub> freshPubs = new ArrayList<FreshPub>();

        /* URLに署名をするヘルパークラスを用意する */
        SignedRequestsHelper helper = new SignedRequestsHelper();

        /* XML解析するためのbuilderオブジェクトを用意する */
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        int totalResults = 40; /* 最大40件 */
        int itemPage = 1;
        /* Amazon APIは一度に10件しか検索できないため、繰り返し検索する */
        while (freshPubs.size() < totalResults) {
            /* 少し待つ */
            waitAccess();

            /* 何件目から検索するかを指定して、APIを呼び出す */
            params.put("ItemPage", Integer.toString(itemPage++));
            String requestUrl = helper.sign(params);
            Document document = builder.parse(requestUrl);
            totalResults = Math.min(
                    totalResults,
                    Integer.parseInt(document
                            .getElementsByTagName("TotalResults").item(0)
                            .getTextContent()));

            /* 新刊情報オブジェクトを検索結果に追加する */
            NodeList items = document.getElementsByTagName("Item");
            if (items.getLength() == 0)
                break;
            for (int i = 0; i < items.getLength(); i++) {
                freshPubs.add(createFreshPub(items, i));
            }
            if (freshPubs.size() >= totalResults) {
                break;
            }
        }
        return freshPubs;
    }

    /* アプリケーション全体を通して、処理が1秒以上の間隔で行なわれるようにする */
    private void waitAccess() {
        MemcacheService service = MemcacheServiceFactory.getMemcacheService();
        while (true) {
            Date lastAccess = ((Date) service.get("lastAccess"));
            if (lastAccess == null) {
                lastAccess = new Date(0);
            }
            Date current = new Date();
            if (lastAccess.getTime() + 1000 <= current.getTime()) {
                service.put("lastAccess", current);
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                /* nop */
            }
        }
    }

    /* 検索結果から「タイトル」「書籍URL」「刊行日」を取得して新刊情報オブジェクトを生成する */
    private FreshPub createFreshPub(NodeList items, int i) {
        Element item = (Element) items.item(i);
        String title = getTextContent(item, "Title");
        String url = getTextContent(item, "DetailPageURL");
        String salesDate = getTextContent(item, "PublicationDate");
        return new FreshPub(title, url, salesDate);
    }

    /* 書籍の情報を項目指定で取得する */
    private String getTextContent(Element item, String name) {
        try {
            return item.getElementsByTagName(name).item(0).getTextContent();
        } catch (Exception e) {
            return "";
        }
    }
}
