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
public class AddStrokeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String clientId = req.getParameter("clientId");
        String stroke = req.getParameter("stroke");

        /* 描画情報を登録する */
        Picture picture = PictureHelper.addStroke(stroke);

        /* 描画情報をチャネルサービスに送信する */
        Set<String> clientIds = picture.getClientIds();
        ChannelService channelService = ChannelServiceFactory
                .getChannelService();
        Command command = new Command("addstroke", stroke);
        String message = JSON.encode(command);
        /* 接続中のクライアントに1つずつ送信する */
        for (String targetId : clientIds) {
            /* 自分自身は除く */
            if (targetId.equals(clientId)) { continue; }
            ChannelMessage channelMessage = new ChannelMessage(targetId,
                    message);
            channelService.sendMessage(channelMessage);
        }
    }
}
