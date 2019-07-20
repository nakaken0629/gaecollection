package com.appspot.guguruchan;

import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Sentence implements Selectable {
    /* データストアが内部で管理する主キー */
    @PrimaryKey() private Long id;

    /* データストアに保存するフィールドに、Persistentアノテーションを付与 */
    @Persistent private String screenName;
    @Persistent private String text;
    @Persistent private String chippedText;
    @Persistent private List<String> chips;
    @Persistent private double rand;

    /* コンストラクタ */
    public Sentence(Long id, String screenName, String text,
            String chippedText, List<String> chips) {
        this.id = id;
        this.screenName = screenName;
        this.text = text;
        this.chippedText = chippedText;
        this.chips = chips;
        this.rand = Math.random();
    }

    /* getterとsetter */
    public Long getId() { return id; }
    public String getScreenName() { return screenName; }
    public void setScreenName(String screenName) { this.screenName = screenName; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getChippedText() { return chippedText; }
    public void setChippedText(String chippedText) { this.chippedText = chippedText; }
    public List<String> getChips() { return chips; }
    public void setChips(List<String> chips) { this.chips = chips; }
    public double getRand() { return rand; }
    public void setRand(double rand) { this.rand = rand; }
}
