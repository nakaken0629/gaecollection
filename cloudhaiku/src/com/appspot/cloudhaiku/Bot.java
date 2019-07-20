package com.appspot.cloudhaiku;

import java.util.HashMap;
import java.util.Map;

public abstract class Bot {
    private static final Map<String, Bot> bots;
    static {
        /* 部員ボットの登録 */
        bots = new HashMap<String, Bot>();
        bots.put("eri", new EriBot());
        bots.put("soun", new SounBot());
        bots.put("o2", new O2Bot());
    }

    /* id（@の前の文字）から部員ボットを取得する */
    public static Bot getBot(String id) {
        return bots.get(id);
    }

    /* 部員ボットが実装するmakeHaikuメソッドの定義 */
    public abstract String makeHaiku();
}