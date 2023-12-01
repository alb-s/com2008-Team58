package CustomerGUI;

public class Session {
    private static Session instance = null;

    private String userId;
    private String userEmail;
    private String userRole;
    private int houseNumber; // New field for house number
    private String postcode; // New field for postcode

    private Session() { }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    // Updated setUserDetails method to include houseNumber and postcode
    public void setUserDetails(String userId, String userEmail, String userRole, int houseNumber, String postcode) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userRole = userRole;
        this.houseNumber = houseNumber; // Set house number
        this.postcode = postcode; // Set postcode
    }

    public String getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserRole() {
        return userRole;
    }

    // Getter for house number
    public int getHouseNumber() {
        return houseNumber;
    }

    // Getter for postcode
    public String getPostcode() {
        return postcode;
    }
}
