package com.appspot.wwwwgame;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class IndexServlet extends HttpServlet {
    /* ブラウザからのGETリクエストを処理する */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        /* /WEB-INF/index.jspを呼び出す */
        RequestDispatcher rd = getServletContext().getRequestDispatcher(
                "/WEB-INF/index.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        /* 入力項目を取得する */
        String when = req.getParameter("when");
        String where = req.getParameter("where");
        String who = req.getParameter("who");
        String what = req.getParameter("what");
        /* 新しい文章を作成してリクエスト属性に保存する */
        String sentence = SentenceHelper.makeSentence(when, where, who, what);
        req.setAttribute("sentence", sentence);
        /* 文章を保存して、JSPを呼び出す */
        RequestDispatcher rd = getServletContext().getRequestDispatcher(
                "/WEB-INF/index_post.jsp");
        rd.forward(req, resp);
    }
}