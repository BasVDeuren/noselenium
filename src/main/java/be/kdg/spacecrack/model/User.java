package be.kdg.spacecrack.model;

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
    private int id;

    @Column
    private String name;

    @Column
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accessTokenId")
    private AccessToken token;

    public User() {
        System.out.println("lol");
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
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
