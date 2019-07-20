package com.appspot.guguruchan;

import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class YahooHelper {
    private static final String APPID = "Yahoo!のアプリケーションIDを指定";

    /* 日本語係り受け解析を呼び出す */
    public static Document callDAService(String text) {
        try {
            return DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse("http://jlp.yahooapis.jp/DAService/V1/parse?appid="
                            + APPID + "&sentence="
                            + URLEncoder.encode(text, "utf-8"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
