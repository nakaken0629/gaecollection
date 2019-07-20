package com.appspot.hiramekanaito;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class YahooHelper {
    private static final String APPID = "ここにアプリケーションIDを書く";

    private YahooHelper() { /* nop */}

    /* ふりがなを取得する */
    public static String getFurigana(String word) throws Exception {
        /* ルビ振りサービスを呼び出す */
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse("http://jlp.yahooapis.jp/FuriganaService/V1/furigana?appid="
                        + APPID
                        + "&sentence="
                        + URLEncoder.encode(word, "utf-8"));

        /* 取得した単語を1つずつ処理する */
        NodeList items = document.getElementsByTagName("Word");
        StringBuilder furigana = new StringBuilder();
        for (int i = 0; i < items.getLength(); i++) {
            Element element = (Element) items.item(i);
            NodeList furiganaList = element.getElementsByTagName("Furigana");
            if (furiganaList.getLength() > 0) {
                /* ふりがながあれば利用する */
                furigana.append(furiganaList.item(0).getTextContent());
            } else {
                /* ふりがながない（英数字など）場合は、単語をそのまま利用する */
                NodeList surfaceList = element.getElementsByTagName("Surface");
                furigana.append(surfaceList.item(0).getTextContent());
            }
        }
        return furigana.toString();
    }

    /* 検索関連ワードを最大50件取得する */
    public static List<String> getHints(String word) throws Exception {
        /* 関連検索ワードサービスを呼び出す */
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse("http://search.yahooapis.jp/AssistSearchService/V1/webunitSearch?appid="
                        + APPID
                        + "&query="
                        + URLEncoder.encode(word, "utf-8")
                        + "&results=50");

        /* 関連検索ワードを1つずつ処理する */
        NodeList results = document.getElementsByTagName("Result");
        List<String> hints = new ArrayList<String>();
        for (int i = 0; i < results.getLength(); i++) {
            Element element = (Element) results.item(i);
            String hint = getGoodHint(word, element);
            if (hint != null && !hints.contains(hint)) {
                /* 新しい関連検索ワードであれば、返り値候補に追加する */
                hints.add(hint);
                if (hints.size() >= 16) {
                    /* 16個ヒントを用意したら結果を返す */
                    return hints;
                }
            }
        }
        return hints;
    }

    /* resultに含まれる複数のワードから適切なヒントを返す */
    private static String getGoodHint(String word, Element result) {
        String[] hintWords = result.getTextContent().split(" ");
        if (hintWords.length <= 1) {
            /* 短すぎるヒント */
            return null;
        }
        for (int i = 0; i < hintWords.length; i++) {
            if (!hintWords[i].matches(word)) {
                return hintWords[i];
            }
        }
        return null;
    }
}
