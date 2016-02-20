/*
** Name: William Funk
** Course: CNT 4714 Spring 2016
** Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking
** Due Date: February 14, 2016
*/
package SQL_Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Controller implements Initializable
{
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost:3310/project3";
    Connection connection = null; // manages connection
    Statement statement = null; // query statement
    private boolean isDatabaseConnected = false;

    @FXML
    private Button btn_execute;
    @FXML
    private Button btn_clearCommand;
    @FXML
    private Button btn_clearResults;
    @FXML
    private Button btn_connect;
    @FXML
    private TextField text_username;
    @FXML
    private TextField text_password;
    @FXML
    private TextArea text_query;
    @FXML
    private TextArea text_results;
    @FXML
    private ChoiceBox choice_driver;
    @FXML
    private Label label_connected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert btn_execute != null : "fx:id=\"btn_execute\" was not injected: check your FXML file 'balanceSheet.fxml'.";
        assert btn_clearCommand != null : "fx:id=\"btn_clearCommand\" was not injected: check your FXML file 'balanceSheet.fxml'.";
        assert btn_clearResults != null : "fx:id=\"btn_clearResults\" was not injected: check your FXML file 'balanceSheet.fxml'.";
        assert btn_connect != null : "fx:id=\"btn_connect\" was not injected: check your FXML file 'balanceSheet.fxml'.";
        assert text_username != null : "fx:id=\"text_username\" was not injected: check your FXML file 'balanceSheet.fxml'.";
        text_username.setText("root");
        assert text_password != null : "fx:id=\"text_password\" was not injected: check your FXML file 'balanceSheet.fxml'.";
        text_password.setText("root");
        assert text_query != null : "fx:id=\"text_query\" was not injected: check your FXML file 'balanceSheet.fxml'.";
        assert text_results != null : "fx:id=\"text_results\" was not injected: check your FXML file 'balanceSheet.fxml'.";
        assert choice_driver != null : "fx:id=\"choice_driver\" was not injected: check your FXML file 'balanceSheet.fxml'.";
        assert label_connected != null : "fx:id=\"label_connected\" was not injected: check your FXML file 'balanceSheet.fxml'.";
    }

    @FXML
    private void executeCommand()
    {
        String query = text_query.getText();
        if(query.equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Query empty");
            alert.setHeaderText("You must enter a query to be executed.");
            alert.getDialogPane().setStyle(" -fx-max-width:500px; -fx-pref-width: 500px;");

            alert.showAndWait();
        }
        else
        {
            if(query.length() >= 6 && query.substring(0,6).toUpperCase().equals("SELECT"))
            {
                try
                {
                    statement = connection.createStatement();
                }
                catch ( SQLException sqlException )
                {
                    sqlException.printStackTrace();
                    System.exit( 1 );
                } // end catch
                catch (Exception e)
                {
                    System.out.println("Other exception: " + e);
                }
            }
            else if(query.length() >= 6 && query.substring(0,6).toUpperCase().equals("DELETE"))
            {

            }
            else if(query.length() >= 6 && query.substring(0,6).toUpperCase().equals("INSERT"))
            {

            }
            else if(query.length() >= 6 && query.substring(0,6).toUpperCase().equals("UPDATE"))
            {

            }
            else if(query.length() >= 4 && query.substring(0,4).toUpperCase().equals("DROP"))
            {

            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Query");
                alert.setHeaderText("You've entered a query that's incompatible with this interface.");
                alert.setHeaderText("Try using one of the following:\nSELECT\nINSERT\nUPDATE\nDELETE\nDROP\n");
                alert.getDialogPane().setStyle(" -fx-max-width:500px; -fx-pref-width: 500px;");

                alert.showAndWait();
            }
        }
    }
    @FXML
    private void clearCommand()
    {
        text_query.setText("");
    }
    @FXML
    private void clearResults()
    {
        text_results.setText("");
    }
    @FXML
    private void connectDatabase()
    {
        if(isDatabaseConnected)
        {
            try
            {
                System.out.println("Disconnecting from database.");
                connection.close();

                btn_connect.setText("Connect to Database");
                btn_connect.setStyle("-fx-background-color:#692DAC;-fx-text-fill: #34ACAF;-fx-font-weight: bold");
                label_connected.setText("No Connection Now");
                isDatabaseConnected = false;
            } // end try
            catch ( Exception exception )
            {
                exception.printStackTrace();
                System.exit( 1 );
            } // end catch
        }
        else
        {
            // Connect to database
            try
            {
                System.out.println("Connecting to the database.");
                Class.forName(JDBC_DRIVER); // load database driver class

                // establish connection to database
                connection = DriverManager.getConnection(DATABASE_URL, text_username.getText(), text_password.getText());

                btn_connect.setText("Disconnect from Database");
                btn_connect.setStyle("-fx-background-color:red;-fx-text-fill: #34ACAF;-fx-font-weight: bold");
                label_connected.setText("Connected to " + DATABASE_URL);
                isDatabaseConnected = true;
            }
            catch ( SQLException sqlException )
            {
                sqlException.printStackTrace();
                System.exit( 1 );
            } // end catch
            catch ( ClassNotFoundException classNotFound )
            {
                classNotFound.printStackTrace();
                System.exit( 1 );
            } // end catch
        }
    }
}
