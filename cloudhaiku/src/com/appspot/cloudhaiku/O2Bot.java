package com.appspot.cloudhaiku;

public class O2Bot extends Bot {
    private final static String[] firstWords = { "気モ早ク", "待チ切レズ", "腹ガヘリ",
            "俳句部デ", "機械ナノニ", };
    private final static String[] middleWords = { "甘酒飲ミシ", "ソウメン流シ", "オ芋恋シヤ",
            "鍋ヲツツキシ", "豪華オセチダ", };
    private final static String[] lastWords = { "すかいつりー", "名古屋城", "西浦和",
            "瀬戸内海", "普賢岳", };

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
        builder.append("（Ｏ２）");
        /* できあがった句を返す */
        return builder.toString();
    }
}
