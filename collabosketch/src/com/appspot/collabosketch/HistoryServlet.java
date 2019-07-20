package com.appspot.collabosketch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;

@SuppressWarnings("serial")
public class HistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        /* 過去の絵の履歴を取得してHistoryクラスとして保存する */
        ArrayList<History> histories = new ArrayList<History>();
        for (Picture picture : PictureHelper.getHistories()) {
            Long id = picture.getId();
            Date createDate = picture.getCreateDate();
            histories.add(new History(id, createDate));
        }
        resp.setContentType("application/json; charset=UTF-8");
        JSON.encode(histories, resp.getOutputStream());
    }
}
