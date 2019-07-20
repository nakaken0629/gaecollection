package com.appspot.avatartravel;

import java.io.IOException;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import javax.jdo.Query;

@SuppressWarnings("serial")
public class IndexServlet extends HttpServlet {
    /* ブログ登録画面を表示する */
    @Override @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        /* アバター一覧をリクエスト属性に保存する */
        req.setAttribute("avatars", Avatar.getAvatars());

        /* 最新のブログの一覧をリクエスト属性に保存する */
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Query query = pm.newQuery(Blog.class);
            query.setOrdering("departureDate desc");
            query.setRange(0, 10);
            List<Blog> blogs = (List<Blog>) query.execute();
            pm.detachCopyAll(blogs);
            req.setAttribute("blogs", blogs);
        } finally {
            pm.close();
        }

        RequestDispatcher rd = getServletContext().getRequestDispatcher(
                "/WEB-INF/index.jsp");
        rd.forward(req, resp);
    }

    /* ブログ登録完了画面を表示する */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        /* ブログを登録する */
        String avatarId = req.getParameter("avatarId");
        String destination = req.getParameter("destination");
        Date departureDate = new Date();
        Date nextPostDate = new Date();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Blog blog = new Blog(avatarId, destination, departureDate,
                    nextPostDate);
            pm.makePersistent(blog);
            /* Blogエンティティをリクエスト属性に保存 */
            req.setAttribute("blog", blog);
        } finally {
            pm.close();
        }
        /* 画面を表示する */
        RequestDispatcher requestDispatcher = getServletContext()
                .getRequestDispatcher("/WEB-INF/index_post.jsp");
        requestDispatcher.forward(req, resp);
    }
}
