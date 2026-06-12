package librasys.model;

/**
 *
 * @author AmmarPasifiky
 */
public class Librarian extends User {

    private String employeeId;

    public Librarian(String userId, String name, String email, String password,
            String employeeId) {
        super(userId, name, email, password);
        this.employeeId = employeeId;
    }

    @Override
    public void displayInfo() {
        System.out.println("Librarian ID: " + getUserId());
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Employee ID: " + employeeId);
    }

    @Override
    public String getRole() {
        return "Librarian";
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
