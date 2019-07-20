package com.appspot.cloudhaiku;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.SendResponse.Status;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

@SuppressWarnings("serial")
public class CloudhaikuServlet extends HttpServlet {
    private static final Logger logger = Logger
            .getLogger(CloudhaikuServlet.class.getName());

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        /* メッセージ送受信用のXMPPServiceオブジェクトを作成する */
        XMPPService xmppService = XMPPServiceFactory.getXMPPService();
        /* 受信したメッセージを解析する */
        Message rcvMsg = xmppService.parseMessage(req);

        /* すべての送信元にメッセージを返す */
        JID toJid = rcvMsg.getFromJid();
        for (JID fromJid : rcvMsg.getRecipientJids()) {
            /* 俳句を作成する */
            String fromJidText = fromJid.getId();
            Bot bot = Bot.getBot(fromJidText.split("@")[0]);
            if (bot == null) {
                /* 存在しない部員へのメッセージは無視する */
                if (logger.isLoggable(Level.WARNING)) {
                    logger.warning(fromJidText + "は見つかりません");
                }
                break;
            }
            String haiku = bot.makeHaiku();

            /* すべての送信元にメッセージを返す */
            Message msg = new MessageBuilder().withFromJid(fromJid)
                    .withRecipientJids(toJid).withBody(haiku).build();
            Status status = xmppService.sendMessage(msg).getStatusMap()
                    .get(toJid);
            if (!SendResponse.Status.SUCCESS.equals(status)) {
                /* ログを出力する */
                if (logger.isLoggable(Level.WARNING)) {
                    logger.warning(fromJid.getId() + "から" + toJid.getId()
                            + "への送信に失敗しました");
                }
            }
        }
    }
}
