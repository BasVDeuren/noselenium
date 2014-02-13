package be.kdg.spacecrack.model;

/**
 * Created by Tim on 13/02/14.
 */
public class Player {
    private Contact opponent;

    public Player(Contact opponent) {

        this.opponent = opponent;
    }

    public Contact getContact() {
        return opponent;
    }

    public void setContact(Contact contact) {
        this.opponent = contact;
    }
}
