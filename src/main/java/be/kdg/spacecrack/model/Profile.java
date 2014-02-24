package be.kdg.spacecrack.model;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
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
    private int profileId;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column
    private Date dayOfBirth;

    @Column
    @Type(type="text")
    private String image;


    @Cascade(value = CascadeType.ALL)
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

    public Profile(String firstname, String lastname,Date dayOfBirth, String image) {

        this.firstname = firstname;
        this.lastname = lastname;
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



    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
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
