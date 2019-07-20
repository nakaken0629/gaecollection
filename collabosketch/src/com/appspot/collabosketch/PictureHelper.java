package com.appspot.collabosketch;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Text;

public final class PictureHelper {
    private PictureHelper() {}

    /* 最新の絵をデータストアから取得する */
    @SuppressWarnings("unchecked")
    private static Picture getCurrentPicture(PersistenceManager pm) {
        Query query = pm.newQuery(Picture.class);
        query.setFilter("current == true");
        query.setOrdering("createDate DESC");
        query.setRange(0, 1);
        List<Picture> pictures = (List<Picture>) query.execute();
        if (pictures.size() == 0) {
            return null;
        }
        return pictures.get(0);
    }

    /* 絵の作者の1人として登録する */
    public static void connectPicture(String clientId) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            /* 最新の絵を取得する */
            Picture picture = getCurrentPicture(pm);
            if (picture == null) {
                /* 絵がない場合は、新たな絵を登録する */
                picture = new Picture();
            }
            picture.getClientIds().add(clientId);
            pm.makePersistent(picture);
        } finally {
            pm.close();
        }
    }

    /* 最新の絵に、描画情報を追加する */
    public static Picture addStroke(String stroke) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Picture picture = getCurrentPicture(pm);
            if (picture == null) { return null; } /* 絵がない場合は、nullを返す */
            picture.getStrokes().add(new Text(stroke));
            pm.makePersistent(picture);
            return picture;
        } finally {
            pm.close();
        }
    }

    /* 最新の絵を取得する */
    public static Picture getPicture() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            return getCurrentPicture(pm);
        } finally {
            pm.close();
        }
    }

    /* 新しい絵を作成する */
    public static Picture newPicture() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Picture retPicture = null;
        try {
            List<Picture> pictures = getCurrentPictures(pm);
            for (Picture picture : pictures) {
                if (picture.getStrokes().size() == 0) {
                    /* 何も描かれていない絵は削除する */
                    pm.deletePersistent(picture);
                } else {
                    /* 何か描かれている絵は、保存する */
                    picture.setCurrent(false);
                    pm.makePersistent(picture);
                    retPicture = picture;
                }
            }
            return retPicture;
        } finally {
            pm.close();
        }
    }

    /* 最新の絵をデータストアから取得する */
    @SuppressWarnings("unchecked")
    private static List<Picture> getCurrentPictures(PersistenceManager pm) {
        Query query = pm.newQuery(Picture.class);
        query.setFilter("current == true");
        query.setOrdering("createDate ");
        return (List<Picture>) query.execute();
    }

    /* 保存された絵をデータストアから取得する */
    @SuppressWarnings("unchecked")
    private static List<Picture> getHistoryPictures(PersistenceManager pm) {
        Query query = pm.newQuery(Picture.class);
        query.setFilter("current == false");
        query.setOrdering("createDate DESC");
        query.setRange(0, 20);
        List<Picture> pictures = (List<Picture>) query.execute();
        pm.detachCopyAll(pictures);
        return pictures;
    }

    /* 保存された絵を返す */
    public static List<Picture> getHistories() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            return getHistoryPictures(pm);
        } finally {
            pm.close();
        }
    }

    /* 指定された絵を取得する */
    public static Picture getPicture(Long id) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            return pm.getObjectById(Picture.class, id);
        } finally {
            pm.close();
        }
    }
}
