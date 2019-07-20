package com.appspot.freshpubbar;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;

@SuppressWarnings("serial")
public class FreshpubbarServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        /* レスポンスの形式をJSONに変更する */
        resp.setContentType("application/json");

        /* 新刊情報を取得する */
        String title = req.getParameter("title");
        String author = req.getParameter("author");
        String publisher = req.getParameter("publisher");
        if ((title == null || "".equals(title))
                && (author == null || "".equals(author))
                && (publisher == null || "".equals(publisher))) {
            /* 条件が指定されていなければ、Javaの書籍を検索する */
            title = "Java";
        }
        AmazonHelper helper = new AmazonHelper(title, author, publisher);
        List<FreshPub> freshPubs = null;
        try {
            freshPubs = helper.getFreshPubs();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        /* 新刊情報をJSONで返す */
        JSON.encode(freshPubs, resp.getOutputStream());
    }
}
