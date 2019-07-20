package com.appspot.hiramekanaito;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class GooHelper {
    private GooHelper() { /* nop */ }

    /* 問題の答えを生成して返す */
    public static List<String> getWords() throws Exception {
        List<String> words = new ArrayList<String>();

        /* GooキーワードランキングをURLフェッチサービスで取得する */
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(
            "http://ranking.goo.ne.jp/rss/keyword/keyrank_all1/index.rdf");

        /* <title>タグの内容を答えとして抽出して返す */
        NodeList items = document.getElementsByTagName("item");
        for (int i = 0; i < items.getLength(); i++) {
            Element item = (Element) items.item(i);
            String text = item.getElementsByTagName("title").item(0)
                    .getTextContent();
            /* スペースが含まれるキーワードは除外する */
            if (text.indexOf(" ") >= 0) { continue; }
            words.add(text);
        }
        return words;
    }
}
