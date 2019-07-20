package com.appspot.cloudhaiku;

public class SounBot extends Bot {
    private final static String[] firstWords = { "簡単に", "楽しげに", "悲しげに",
            "おおまかに", "盛大に", };
    private final static String[] middleWords = { "旅先決める", "小物を買うや", "家路につきし",
            "予定を立てる", "友を祝いし", };
    private final static String[] lastWords = { "蜃気楼", "五月晴れ", "秋の空", "除夜の鐘",
            "年賀状", };

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
        builder.append("（早雲）");
        /* できあがった句を返す */
        return builder.toString();
    }
}
