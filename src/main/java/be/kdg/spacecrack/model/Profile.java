package be.kdg.spacecrack.model;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@Entity
@Table(name = "T_Profile")
public class Profile {
    @Id
    @GeneratedValue
    private int contactId;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column
    private String email;

    @Column
    private Date dayOfBirth;

    @Column
    private String image;

    @Cascade(CascadeType.ALL)
    @OneToOne(mappedBy = "profile")
    @JsonBackReference
    private User user;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "profile")
    @JsonIgnore
    private List<Player> players;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Profile() {
    }

    public Profile(String firstname, String lastname, String email, Date dayOfBirth, String image) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.dayOfBirth = dayOfBirth;
        this.image = image;
        players = new ArrayList<Player>();
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(Date dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return contactId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }


    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        if(players == null)
        {
            players = new ArrayList<Player>();
        }
        players.add(player);
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
