package com.appspot.collabosketch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/* JSON形式に変換される履歴クラス */
public class History {
    private Long id;
    private String createDate;

    public History(Long id, Date createDate) {
        this.id = id;
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分ごろの絵");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        this.createDate = format.format(createDate);
    }

    public String getCreateDate() { return createDate; }
    public Long getId() { return id; }
}