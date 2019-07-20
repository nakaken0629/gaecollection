package com.appspot.guguruchan;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.TwitterException;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.text.MessageFormat;

@SuppressWarnings("serial")
public class TweetServlet extends HttpServlet {
    /* ツイートを投稿する */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            /* 文章を取得する */
            Sentence sentence = getRandomEntity(Sentence.class, pm, null);
            if (sentence == null) {
                return;
            }

            /* 置換する単語を取得する */
            int chipCount = sentence.getChips().size();
            Object[] arguments = new Object[chipCount];
            for (int index = 0; index < chipCount; index++) {
                String chip = sentence.getChips().get(index);
                Word word = getRandomEntity(Word.class, pm, chip);
                arguments[index] = (word != null ? word.getSurface() : "ぬる");
            }

            /* 文章の名詞が入る場所に名詞を当て込む */
            String chippedText = sentence.getChippedText();
            String text = MessageFormat.format(chippedText, arguments);

            /* ツイートを投稿する */
            TwitterHelper.tweet(text);
        } catch (TwitterException e) {
            throw new RuntimeException(e);
        } finally {
            pm.close();
        }
    }

    /* randフィールドを持つデータクラスのエンティティをランダムに1件返す */
    /* posDetailは、Wordの検索時に品詞を細かく指定するためのオプション */
    @SuppressWarnings("unchecked")
    private static <T extends Selectable> T getRandomEntity(Class<T> clazz,
            PersistenceManager pm, String posDetail) {
        /* ランダムな値のすぐ次のrand値を持つエンティティを探す */
        Query query = pm.newQuery(clazz);
        query.setFilter("rand >= :randParam"
                + (posDetail == null ? "" : " && posDetail == :posDetail"));
        query.setOrdering("rand");
        query.setRange(0, 1);
        List<T> list = (List<T>) query.execute(Math.random(), posDetail);

        /* ランダムな値のすぐ次のエンティティが見つからない場合は、先頭のエンティティを探す */
        if (list.size() == 0) {
            query.setFilter(posDetail == null ? null
                    : "posDetail == :posDetail");
            list = (List<T>) query.execute(posDetail);
        }
        /* エンティティが見つかったら、rand値を更新して返す */
        if (list.size() > 0) {
            T entity = list.get(0);
            entity.setRand(Math.random());
            pm.makePersistent(entity);
            return entity;
        }
        /* エンティティが見つからなかったら、nullを返す */
        return null;
    }
}
