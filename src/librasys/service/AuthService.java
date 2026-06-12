package librasys.service;

import java.util.ArrayList;
import java.util.List;
import librasys.model.User;

/**
 *
 * @author AmmarPasifiky
 */
public class AuthService {

    private List<User> users;

    public AuthService() {
        this.users = new ArrayList<>();
    }

    public AuthService(List<User> users) {
        this.users = new ArrayList<>();
        if (users != null) {
            this.users.addAll(users);
        }
    }

    public User login(String email, String password) {
        if (isBlank(email) || isBlank(password)) {
            throw new IllegalArgumentException("Email and password are required.");
        }

        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)
                    && user.login(email, password)) {
                return user;
            }
        }

        return null;
    }

    public void logout(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        user.logout();
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
