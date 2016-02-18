package in.icho.model;

/**
 * Created by sahebjot on 2/13/16.
 */
public class User {
    private String email;
    private String password;
    private String username;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
