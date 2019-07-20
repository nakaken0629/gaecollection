package com.appspot.avatartravel;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Blog {
    /* データストアが内部で管理する主キー（Google App Engineが提供する主キーの自動作成機能） */
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @PrimaryKey() private Long id;

    /* データストアに保存するフィールドに、Persistentアノテーションを付与 */
    @Persistent private String avatarId;
    @Persistent private String destination;
    @Persistent private Date departureDate;
    @Persistent private Date nextPostDate;

    /* コンストラクタ */
    public Blog(String avatarId, String destination, Date departureDate,
            Date nextPostDate) {
        this.avatarId = avatarId;
        this.destination = destination;
        this.departureDate = departureDate;
        this.nextPostDate = nextPostDate;
    }

    /* getterとsetter */
    public Long getId() { return id; }
    public String getAvatarId() { return avatarId; }
    public void setAvatarId(String avatarId) { this.avatarId = avatarId; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) {
                                        this.destination = destination; }
    public Date getDepartureDate() { return this.departureDate; }
    public void setDepartureDate(Date departureDate) {
                                        this.departureDate = departureDate; }
    public Date getNextPostDate() { return nextPostDate; }
    public void setNextPostDate(Date nextPostDate) {
                                        this.nextPostDate = nextPostDate; }

    /* ブログに登録されている記事のリスト */
    @Persistent(mappedBy = "blog", defaultFetchGroup = "true")
    private List<Article> articles;
    public List<Article> getArticles() { return articles; }

    /* アバタークラスを返すヘルパーメソッド */
    public Avatar getAvatar() {
        return Avatar.getAvatar(this.avatarId);
    }
}
