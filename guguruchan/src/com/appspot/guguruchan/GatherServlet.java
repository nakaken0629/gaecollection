package com.appspot.guguruchan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import twitter4j.Status;

@SuppressWarnings("serial")
public class GatherServlet extends HttpServlet {
    private static final Pattern httpPattern = Pattern.compile(
            "(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern retweetPattern = Pattern
            .compile("(^|\\s)[RQ]T\\s");

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            /* タイムライン上のツイート一覧を取得する */
            String screenName = TwitterHelper.getScreenName();
            List<Status> statuses = TwitterHelper.getStatuses();
            /* ツイートを1つずつ解析して、データストアに保存する */
            for (Status status : statuses) {
                /* 自分自身の発言は解析しない */
                if (screenName.equals(status.getUser().getScreenName())) {
                    continue;
                }
                analyzeStatus(pm, status);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            pm.close();
        }
    }

    /* ツイートを解析する */
    private void analyzeStatus(PersistenceManager pm, Status status)
            throws Exception {
        /* "http://～"というリンクは除去する */
        String text = httpPattern.matcher(status.getText()).replaceAll("");

        /* 登録しないツイート（その1） */
        if (text.startsWith("@")) { return; } /* 返信 */
        if (status.isRetweet()) { return; } /* 公式RT */
        if (retweetPattern.matcher(text).find()) { return; } /* 非公式RT/QT */

        /* ツイートのテキストを解析する */
        Document document = YahooHelper.callDAService(text);
        StringBuilder chippedText = new StringBuilder();
        ArrayList<String> chips = new ArrayList<String>();

        /* <Feature>タグごとに品詞を解析する */
        NodeList list = document.getElementsByTagName("Feature");
        for (int index = 0; index < list.getLength(); index++) {
            Element feature = (Element) list.item(index);
            analyzeFeature(pm, feature, chippedText, chips);
        }
        /* 登録しないツイート（その2） */
        if (chips.size() == 0) { return; } /* 名詞のない文章 */

        /* 文書データクラスを保存する */
        Sentence sentence = new Sentence(status.getId(), status.getUser()
                .getScreenName(), text, chippedText.toString(), chips);
        pm.makePersistent(sentence);
    }

    /* 品詞を解析する */
    private void analyzeFeature(PersistenceManager pm, Element feature,
            StringBuilder chippedText, List<String> chips) {
        String[] features = feature.getTextContent().split(",");
        /* 特殊な品詞は除外する */
        if (features.length < 4) {
            return;
        }
        String pos = features[0];
        String posDetail = features[1];
        String surface = features[3];
        if ("名詞".equals(pos)) {
            /* 品詞が名詞ならば、置き換え用の場所を用意して、単語をデータストアに保存する */
            chippedText.append("{" + chips.size() + "}");
            chips.add(posDetail);
            Word word = new Word(surface, posDetail);
            pm.makePersistent(word);
        } else {
            /* 名詞以外であれば、単語をそのまま使う */
            chippedText.append(surface);
        }
    }
}