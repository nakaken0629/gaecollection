package com.appspot.hiramekanaito;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Answer answer;
            String pathInfo = req.getPathInfo();
            if (pathInfo == null) {
                /* "/index"の場合は、ランダムにエンティティを取得する */
                answer = getRandomAnswer(pm);
            } else {
                /* "/index/(id)"の場合は、特定のエンティティを取得する */
                Long id = Long.valueOf(pathInfo.split("/")[1]);
                answer = pm.getObjectById(Answer.class, id);
            }
            /* JSPから利用できるよう、Answerエンティティをリクエスト属性に保存する */
            req.setAttribute("answer", answer);
        } finally {
            pm.close();
        }
        /* JSPを呼び出す */
        RequestDispatcher dispatcher = getServletContext()
                .getRequestDispatcher("/WEB-INF/index.jsp");
        dispatcher.forward(req, resp);
    }

    /* 過去2か月間に登録されたAnswerエンティティをランダムに1件返す */
    @SuppressWarnings("unchecked")
    private Answer getRandomAnswer(PersistenceManager pm) {
        /* ランダムな値のすぐ次のoffset値を持つエンティティを探す */
        long offset = new Date().getTime()
                - (long) (Math.random() * Answer.OFFSET_RANGE);
        Query query = pm.newQuery(Answer.class);
        query.setFilter("offset >= :offsetParam");
        query.setOrdering("offset");
        query.setRange(0, 1);
        List<Answer> list = (List<Answer>) query.execute(offset);

        /* エンティティが見つからない場合は、遡ってエンティティを探す */
        if (list.size() == 0) {
            query.setFilter("offset < :offsetParam");
            list = (List<Answer>) query.execute(offset);
        }
        /* エンティティが見つからなかったら、nullを返す */
        if (list.size() == 0) {
            return null;
        }
        /* エンティティが見つかったら、offset値を更新して返す */
        Answer answer = list.get(0);
        answer.setOffset(answer.getRegisterDate().getTime()
                + (long) (Math.random() * Answer.OFFSET_RANGE));
        pm.makePersistent(answer);
        return list.get(0);
    }
}