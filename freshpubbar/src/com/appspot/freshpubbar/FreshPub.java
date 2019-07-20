package com.appspot.freshpubbar;

/* 新刊情報を表わすクラス */
public class FreshPub {
    /* フィールド */
    private String title;
    private String url;
    private String salesDate;

    /* コンストラクタ */
    public FreshPub(String title, String url, String salesDate) {
        this.title = title;
        this.url = url;
        this.salesDate = salesDate;
    }

    /* getterとsetter */
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getSalesDate() { return salesDate; }
    public void setSalesDate(String salesDate) { this.salesDate = salesDate; }
}
