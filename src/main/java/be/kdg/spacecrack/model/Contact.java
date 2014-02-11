package be.kdg.spacecrack.model;

import javax.persistence.*;

/**
 * Created by Tim on 11/02/14.
 */
@Entity
@Table(name ="T_contact")
public class Contact {
    @Id
    @GeneratedValue
    private int contactId;
    @OneToOne(mappedBy = "contact")
    private User user;

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
