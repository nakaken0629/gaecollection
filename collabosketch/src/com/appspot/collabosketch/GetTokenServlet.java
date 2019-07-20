package com.appspot.collabosketch;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

@SuppressWarnings("serial")
public class GetTokenServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        /* クライアントIDを取得する */
        String clientId = req.getParameter("clientId");
        PictureHelper.connectPicture(clientId);

        /* トークンを生成する */
        ChannelService ChannelService = ChannelServiceFactory
                .getChannelService();
        String token = ChannelService.createChannel(clientId);
        resp.setContentType("text/plain");
        resp.getWriter().write(token);
    }
}
