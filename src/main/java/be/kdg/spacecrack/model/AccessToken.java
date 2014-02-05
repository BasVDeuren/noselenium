package be.kdg.spacecrack.model;

import javax.persistence.*;

/**
 * Created by Tim on 3/02/14.
 */
@Entity
@Table(name = "T_AccessToken")
public class AccessToken {
    @GeneratedValue
    @Id
    private int accessTokenId;

    @Column
    private String value;

    public AccessToken() {
    }

    public AccessToken(String value) {
        this.value = value;
    }

    public int getAccessTokenId() {
        return accessTokenId;
    }

    public String getValue() {
        return value;
    }

    public void setAccessTokenId(int accessTokenId) {
        this.accessTokenId = accessTokenId;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
