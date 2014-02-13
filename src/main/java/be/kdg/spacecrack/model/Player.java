package be.kdg.spacecrack.model;

/**
 * Created by Tim on 13/02/14.
 */
public class Player {
    private Contact contact;

    public Player(Contact contact) {

        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
