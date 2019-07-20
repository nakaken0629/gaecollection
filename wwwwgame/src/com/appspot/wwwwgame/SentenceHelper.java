package com.appspot.wwwwgame;

/* ヘルパークラスは継承が不要なので、final句をつけて継承を抑制する */
public final class SentenceHelper {
    /* ヘルパークラスはすべて静的メソッドなので、なにもしないprivateのコンストラクタを
     * 用意して、外部からのインスタンス化を抑制する */
    private SentenceHelper() { /* なにもしない */ }

    /* ランダムな文章を作るための素材を用意する */
    private static final String[] whenList = { "さっき", "昨日", "あれはもう３年も昔", "紀元前",
            "ジュラ紀" };
    private static final String[] whereList = { "自宅で", "近くの居酒屋で", "世界のまんなかで",
            "追い込み中のプロジェクトで", "木星で" };
    private static final String[] whoList = { "わたしが", "あなたが", "オヤジが",
            "あこがれのアイドルが", "神が" };
    private static final String[] whatList = { "新しいソートアルゴリズムを発見した",
            "イグノーベル賞を受賞した", "悟りを開いた", "こけた", "十円ひろった" };

    /* 新しい文章を作成する */
    public static String makeSentence(String when, String where, String who,
            String what) {
        StringBuilder builder = new StringBuilder();
        builder.append("".equals(when) ? choice(whenList) : when).append(" ");
        builder.append("".equals(where) ? choice(whereList) : where).append(" ");
        builder.append("".equals(who) ? choice(whoList) : who).append(" ");
        builder.append("".equals(what) ? choice(whatList) : what);
        return builder.toString();
    }

    /* 与えられた文字列配列から、ランダムに1つ選択する */
    private static String choice(String[] list) {
        return list[(int) (Math.random() * list.length)];
    }
}