package com.appspot.guguruchan;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.TwitterException;

@SuppressWarnings("serial")
public class UnfollowServlet extends HttpServlet {
    /* フォロー外しをする */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            TwitterHelper.unfollow();
        } catch (TwitterException e) {
            throw new ServletException(e);
        }
    }
}