package CustomerGUI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DatabaseOperations {


    private DatabaseConnectionHandler connectionHandler;

    public DatabaseOperations(DatabaseConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    public void insertCardNumberIntoDatabase(String cardNumber, String userEmail) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connectionHandler.getConnection();
            String query = "UPDATE Users SET CardNumber = ? WHERE email = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cardNumber);
            preparedStatement.setString(2, userEmail);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Card number inserted into the database.");
            } 
            else {
                System.out.println("Failed to insert card number into the database.");
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } 
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    


}