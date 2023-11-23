package Functions;

import java.sql.*;
import javax.swing.JTextField;

public class CardReader{

    enum CardType {
        AMERICANEXPRESS,
        MASTERCARD,
        VISA,
    }       
    
	public void checkCard(JTextField cardField){

        try  {
            String cardReader = cardField.getText();

            if (isValidCardNumber(cardReader)){
                CardType cardType = identifyCardType(cardReader);

                if (cardType != null){
                    System.out.println("Card Type: " + cardType);
                    insertCardNumberIntoDatabase(cardReader);
                } 
            }
            else{
                System.out.println("error");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static boolean isValidCardNumber(String cardNumber){
        int sum = 0;
        boolean alternate = false;
        
        for (int i = cardNumber.length() - 1; i >= 0; i--){
            int digit = Character.getNumericValue(cardNumber.charAt(i));
        
            if (alternate){
                digit *= 2;
                if (digit > 9) {
                    digit = digit % 10 + 1;
                }
            }
        
            sum += digit;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    // Identify the card type based on the first digit(s)
    private static CardType identifyCardType(String cardNumber) {

        String firstChar = cardNumber.substring(0, 1);
        if (firstChar.equals("4")){
            return CardType.VISA;
        } 
        else if (firstChar.equals("5")){
            return CardType.MASTERCARD;
        } 
        else if (firstChar.equals("3")){
            // Note: American Express typically starts with 34 or 37
            if (cardNumber.startsWith("34") || cardNumber.startsWith("37")) {
                return CardType.AMERICANEXPRESS;
            }
        }
        return null; // Unable to identify card type
    }

    // Insert the card number and its type into the database
    private void insertCardNumberIntoDatabase(String cardNumber) {
        String url = "jdbc:mysql://stusql.dcs.shef.ac.uk/team058";
        String username = "team058";
        String password = "eel7Ahsi0";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "INSERT INTO CardNumber (CardNumber) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, cardNumber);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Card number inserted into the database.");
                } 
                else{
                    System.out.println("Failed to insert card number into the database.");
                }
            }
        } 
        catch (SQLException e){
            e.printStackTrace();
        }
    }

}