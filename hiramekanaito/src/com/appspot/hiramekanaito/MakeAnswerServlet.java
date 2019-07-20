package com.appspot.hiramekanaito;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Builder;

@SuppressWarnings("serial")
public class MakeAnswerServlet extends HttpServlet {
    private static final Logger logger = Logger
            .getLogger(MakeAnswerServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            /* Gooキーワードランキングの結果を答えとして登録する */
            for (String text : GooHelper.getWords()) {
                Queue queue = QueueFactory.getQueue("makeanswer-queue");
                queue.add(Builder.withUrl("/task/makeanswer").param("text",
                        text));
            }
        } catch (Exception e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(e.toString());
            }
            throw new ServletException(e);
        }
    }
}
