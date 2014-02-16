package be.kdg.spacecrack.jsonviewmodels;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class UserWrapper {
    private String username;
    private String password;
    private String passwordRepeated;
    private String email;

    public UserWrapper() {
    }

    public UserWrapper(String username, String password, String passwordRepeated, String email) {
        this.username = username;
        this.password = password;
        this.passwordRepeated = passwordRepeated;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeated() {
        return passwordRepeated;
    }

    public void setPasswordRepeated(String passwordRepeated) {
        this.passwordRepeated = passwordRepeated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
