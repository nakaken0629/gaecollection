package com.appspot.collabosketch;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;

import com.google.appengine.api.datastore.Text;

@SuppressWarnings("serial")
public class InitStrokesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Picture picture;
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            /* 最新の絵の情報を取得する */
            picture = PictureHelper.getPicture();
        } else {
            /* 指定された履歴の情報を取得する */
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            picture = PictureHelper.getPicture(id);
        }
        ArrayList<String> strokes = new ArrayList<String>();
        for (Text text : picture.getStrokes()) {
            strokes.add(text.getValue());
        }
        resp.setContentType("application/json; charset=UTF-8");
        JSON.encode(strokes, resp.getOutputStream());
    }
}
