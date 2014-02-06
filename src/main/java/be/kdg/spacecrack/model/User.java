package be.kdg.spacecrack.model;

import org.codehaus.jackson.annotate.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Tim on 3/02/14.
 */
@Entity
@Table(name="T_User")
public class User implements Serializable{
    @Id
    @GeneratedValue
    private int userId;

    @Column
    private String name;

    @Column
    private String password;

    //@OneToOne(mappedBy = "user")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="accesstokenid", nullable = true)
    @JsonManagedReference
    private AccessToken token;

    public User() {
        System.out.println("lol");
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public AccessToken getToken() {
        return token;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(AccessToken token) {
        this.token = token;
    }
}
