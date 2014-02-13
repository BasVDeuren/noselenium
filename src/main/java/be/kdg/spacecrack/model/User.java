package be.kdg.spacecrack.model;

import org.codehaus.jackson.annotate.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@Entity
@Table(name = "T_User")
public class User implements Serializable {

    @Id
    @GeneratedValue
    private int userId;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column
    private String email;

    //@OneToOne(mappedBy = "user")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accesstokenid", nullable = true)
    @JsonManagedReference
    private AccessToken token;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name= "contactId", nullable = true)
    private Contact contact;





    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String pw, String email) {
        this.username = username;
        this.password = pw;
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public AccessToken getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(AccessToken token) {
        this.token = token;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
