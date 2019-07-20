package com.appspot.imakoko;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        /* 現在地登録画面を表示する */
        RequestDispatcher dispatcher = getServletContext()
                .getRequestDispatcher("/WEB-INF/index.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        /* 送信された情報を取得する */
        String nickname = req.getParameter("nickname");
        String tag = req.getParameter("tag");
        String message = req.getParameter("message");
        double lat = Double.parseDouble(req.getParameter("lat"));
        double lng = Double.parseDouble(req.getParameter("lng"));

        /* nicknameが入力されていることをチェックする */
        boolean hasErrors = false;
        if (nickname == null || "".equals(nickname)) {
            req.setAttribute("error_nickname", true);
            hasErrors = true;
        }
        if (hasErrors) {
            req.setAttribute("nickname", nickname);
            req.setAttribute("tag", tag);
            req.setAttribute("message", message);
            RequestDispatcher rd = getServletContext().getRequestDispatcher(
                    "/WEB-INF/index.jsp");
            rd.forward(req, resp);
            return;
        }

        /* 位置情報をデータストアに保存する */
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Place place = new Place(nickname, tag, message, lat, lng);
            pm.makePersistent(place);
        } finally {
            pm.close();
        }
        /* nicknameとtagを保存して画面を再表示する */
        req.setAttribute("nickname", nickname);
        req.setAttribute("tag", tag);
        RequestDispatcher dispatcher = getServletContext()
                .getRequestDispatcher("/WEB-INF/index.jsp");
        dispatcher.forward(req, resp);
    }
}
