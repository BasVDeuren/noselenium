package be.kdg.spacecrack.model;

import javax.persistence.*;

/**
 * Created by Arno on 13/02/14.
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
    private String dayOfBirth;
    @Column
    private String image;

    @OneToOne(mappedBy = "contact")
    public User user;

    public Contact() {
    }

    public Contact(String firstname, String lastname, String email, String dayOfBirth, String image) {

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

    public String getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(String dayOfBirth) {
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
