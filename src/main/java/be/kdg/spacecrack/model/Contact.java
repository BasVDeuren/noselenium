package be.kdg.spacecrack.model;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.Date;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@Entity
@Table(name = "T_Contact")
public class Contact {
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
    @OneToOne(mappedBy = "contact")
    @JsonBackReference
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Contact() {
    }

    public Contact(String firstname, String lastname, String email, Date dayOfBirth, String image) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.dayOfBirth = dayOfBirth;
        this.image = image;
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


}
