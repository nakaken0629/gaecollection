package com.appspot.avatartravel;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public final class FlickrHelper {
    private static final Logger logger = Logger.getLogger(FlickrHelper.class
            .getName());
    private static final String KEY = "Flickr APIのキー情報を指定";
    private HashMap<String, String> params = new HashMap<String, String>();
    private FlickrHelper() {}

    public static FlickrHelper newHelper(String keywords) {
        try {
            XMLStreamReader reader = null;
            try {
                /* URLフェッチ（URLクラスのopenStreamメソッド）でFlickr APIを呼び出す */
                reader = XMLInputFactory.newInstance().createXMLStreamReader(
                        new URL(buildSearchUrl(keywords)).openStream());
                return createHelperFromReader(reader);
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (Exception e) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.severe(e.toString());
            }
            return null;
        }
    }/*
      * Flickr APIの画像検索用のURL文字列を作成する
      * http://www.flickr.com/services/rest?method=flickr.photos.search
      * &format=rest&api_key=取得したキー&per_page=1&license=1,2,3,4,5,6
      * &sort=date-posted-desc&text=検索文字列
      */

    private static String buildSearchUrl(String keywords)
            throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        builder.append("http://www.flickr.com/services/rest")
                .append("?method=flickr.photos.search").append("&format=rest")
                .append("&api_key=").append(KEY).append("&per_page=1")
                .append("&license=1,2,3,4,5,6")
                .append("&sort=date-posted-desc").append("&text=")
                .append(URLEncoder.encode(keywords, "utf-8"));
        String url = builder.toString();
        return url;
    }

    /* Flickr APIの呼び出し結果のXMLを解析して、ヘルパークラスを返す */
    private static FlickrHelper createHelperFromReader(XMLStreamReader reader)
            throws XMLStreamException {
        FlickrHelper helper = new FlickrHelper();
        moveToPhotoElement(reader);
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            helper.params.put(reader.getAttributeLocalName(i),
                    reader.getAttributeValue(i));
        }
        return helper;
    }

    /* 最初の<photo>タグに移動する */
    private static void moveToPhotoElement(XMLStreamReader reader)
            throws XMLStreamException {
        while (!reader.isStartElement()
                || !"photo".equals(reader.getLocalName())) {
            reader.nextTag();
        }
    }

    /*
     * Flickr画像のURLを取得する
     * http://farm[farm].static.flickr.com/［server］/［id］_［secret］_m.jpg
     */
    public String getImageUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append("http://farm").append(params.get("farm"))
                .append(".static.flickr.com").append("/")
                .append(params.get("server")).append("/")
                .append(params.get("id")).append("_")
                .append(params.get("secret")).append("_m.jpg");
        return builder.toString();
    }

    /*
     * Flickr画像の元のページのURLを取得する http://www.flickr.com/［owner］/［id］
     */
    public String getPageUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append("http://www.flickr.com").append("/")
                .append(params.get("owner")).append("/")
                .append(params.get("id"));
        return builder.toString();
    }
}