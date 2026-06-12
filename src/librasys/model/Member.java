package librasys.model;

/**
 *
 * @author AmmarPasifiky
 */
public class Member extends User {

    private String memberNumber;
    private boolean active;

    public Member(String userId, String name, String email, String password,
            String memberNumber, boolean active) {
        super(userId, name, email, password);
        this.memberNumber = memberNumber;
        this.active = active;
    }

    @Override
    public void displayInfo() {
        System.out.println("User ID: " + getUserId());
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Member Number: " + memberNumber);
        System.out.println("Active: " + active);
    }

    @Override
    public String getRole() {
        return "Member";
    }

    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
