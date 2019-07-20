package com.appspot.cloudhaiku;

public class EriBot extends Bot {
    private final static String[] firstWords = { "淡雪や", "向日葵や", "名月や", "クリスマス",
            "初夢や", };
    private final static String[] middleWords = { "あなたのそばで", "なじみのカフェで",
            "ブランコに乗り", "１０年後にも", "映画のように", };
    private final static String[] lastWords = { "見つめたい", "手に取りたい", "歌いたい",
            "送りたい", "眠りたい", };

    @Override
    public String makeHaiku() {
        StringBuilder builder = new StringBuilder();
        /* 初めの五字 */
        int firstIndex = (int) (Math.random() * firstWords.length);
        builder.append(firstWords[firstIndex]);
        builder.append(" ");
        /* 真ん中の七字 */
        int middleIndex = (int) (Math.random() * middleWords.length);
        builder.append(middleWords[middleIndex]);
        builder.append(" ");
        /* 最後の五字 */
        int lastIndex = (int) (Math.random() * lastWords.length);
        builder.append(lastWords[lastIndex]);
        builder.append(" ");
        /* 読み人 */
        builder.append("（えり）");
        /* できあがった句を返す */
        return builder.toString();
    }
}
