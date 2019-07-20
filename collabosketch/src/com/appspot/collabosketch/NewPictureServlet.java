package com.appspot.collabosketch;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

@SuppressWarnings("serial")
public class NewPictureServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        /* 新しい絵を作成する。前の絵がなければそのまま終わる */
        Picture picture = PictureHelper.newPicture();
        if (picture == null) {
            return;
        }
        /* 前の絵を描いていたクライアントに、再読み込みの指示を送信する */
        Set<String> clientIds = picture.getClientIds();
        ChannelService channelService = ChannelServiceFactory
                .getChannelService();
        Command command = new Command("newpicture", null);
        String message = JSON.encode(command);
        for (String clientId : clientIds) {
            ChannelMessage channelMessage = new ChannelMessage(clientId,
                    message);
            channelService.sendMessage(channelMessage);
        }
    }
}
