package com.appspot.hiramekanaito;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Answer {
    /* 2か月をミリ秒に変換したもの */
    public static final long OFFSET_RANGE = 5184000000L;

    /* データストアが内部で管理する主キー */
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @PrimaryKey private Long id;

    /* データストアに保存するフィールドに、Persistentアノテーションを付与 */
    @Persistent private String text;
    @Persistent private String furigana;
    @Persistent private List<String> hints;
    @Persistent private Date registerDate;
    @Persistent private long offset;

    /* コンストラクタ */
    public Answer(String text, String furigana, List<String> hints,
            Date registerDate, long offset) {
        this.text = text;
        this.furigana = furigana;
        this.hints = hints;
        this.registerDate = registerDate;
        this.offset = offset;
    }

    /* getterとsetter */
    public Long getId() { return id; }
    public String getText() { return text; }
    public String getFurigana() { return furigana; }
    public void setFurigana(String furigana) { this.furigana = furigana; }
    public List<String> getHints() { return hints; }
    public void setHints(List<String> hints) { this.hints = hints; }
    public void setRegisterDate(Date registerDate) { this.registerDate = registerDate; }
    public Date getRegisterDate() { return registerDate; }
    public void setOffset(long offset) { this.offset = offset; }
    public long getOffset() { return offset; }
}
