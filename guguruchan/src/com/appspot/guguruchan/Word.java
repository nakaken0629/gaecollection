package com.appspot.guguruchan;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Word implements Selectable {
    /* データストアが内部で管理する主キー */
    @PrimaryKey private String surface;

    /* データストアに保存するフィールドに、Persistentアノテーションを付与 */
    @Persistent private String posDetail;
    @Persistent private double rand;

    /* コンストラクタ */
    public Word(String surface, String posDetail) {
        this.surface = surface;
        this.posDetail = posDetail;
        this.rand = Math.random();
    }

    /* getterとsetter */
    public String getSurface() { return surface; }
    public String getPosDetail() { return posDetail; }
    public void setPosDetail(String posDetail) { this.posDetail = posDetail; }
    public double getRand() { return rand; }
    public void setRand(double rand) { this.rand = rand; }
}
