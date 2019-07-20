package com.appspot.collabosketch;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Picture {
    /* データストアが内部で管理する主キー */
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @PrimaryKey private Long id;

    /* データストアに保存するフィールドに、Persistentアノテーションを付与 */
    @Persistent private List<Text> strokes;
    @Persistent private Set<String> clientIds;
    @Persistent private boolean current;
    @Persistent private Date createDate;

    /* コンストラクタ */
    public Picture() {
        this.strokes = new ArrayList<Text>();
        this.clientIds = new HashSet<String>();
        this.current = true;
        this.createDate = new Date();
    }

    /* getterとsetter */
    public Long getId() { return this.id; }
    public List<Text> getStrokes() { return strokes; }
    public void setStrokes(List<Text> strokes) { this.strokes = strokes; }
    public Set<String> getClientIds() { return clientIds; }
    public void setClientIds(Set<String> clientIds) { this.clientIds = clientIds; }
    public boolean getCurrent() { return current; }
    public void setCurrent(boolean current) { this.current = current; }
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
}
