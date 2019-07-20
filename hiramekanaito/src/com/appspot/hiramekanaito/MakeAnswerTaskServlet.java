package com.appspot.hiramekanaito;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class MakeAnswerTaskServlet extends HttpServlet {
    private static final Logger logger = Logger
            .getLogger(MakeAnswerTaskServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String text = req.getParameter("text");
            register(text);
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(e.toString());
            }
        }
    }

    /* Answerエンティティの登録 */
    @SuppressWarnings("unchecked")
    private void register(String text) throws Exception {
        List<String> hints = YahooHelper.getHints(text);
        /* ヒントの数が16に足りないものは登録しない */
        if (hints.size() < 16) {
            return;
        }
        Date registerDate = new Date();
        long offset = registerDate.getTime()
                + (long) (Math.random() * Answer.OFFSET_RANGE);

        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Query query = pm.newQuery(Answer.class);
            query.setFilter("text == :textParam");
            List<Answer> answers = (List<Answer>) query.execute(text);
            Answer answer;
            if (answers.size() > 0) {
                /* すでに登録されている答えは、最新のヒントで上書きする */
                answer = answers.get(0);
                answer.setHints(hints);
                answer.setRegisterDate(registerDate);
                answer.setOffset(offset);
            } else {
                /* 新規の答えを追加する */
                String furigana = YahooHelper.getFurigana(text);
                answer = new Answer(text, furigana, hints, registerDate, offset);
            }
            pm.makePersistent(answer);
        } finally {
            pm.close();
        }
    }
}