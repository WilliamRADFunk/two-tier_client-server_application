/*
** Name: William Funk
** Course: CNT 4714 Spring 2016
** Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking
** Due Date: February 14, 2016
*/
package SQL_Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;

import java.net.URL;
import java.sql.*;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost:3310/project3";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
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
    private TableView table_results;
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
        assert table_results != null : "fx:id=\"table_results\" was not injected: check your FXML file 'balanceSheet.fxml'.";
        assert choice_driver != null : "fx:id=\"choice_driver\" was not injected: check your FXML file 'balanceSheet.fxml'.";
        assert label_connected != null : "fx:id=\"label_connected\" was not injected: check your FXML file 'balanceSheet.fxml'.";
    }

    @FXML
    private void executeCommand()
    {
        if(!isDatabaseConnected)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not connected!");
            alert.setHeaderText("You need to be connected to the database to make queries.");
            alert.getDialogPane().setStyle(" -fx-max-width:500px; -fx-pref-width: 500px;");

            alert.showAndWait();
            return;
        }
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
            // Prepares statement
            try
            {
                preparedStatement = connection.prepareStatement(query);
            }
            catch ( SQLException sqlException )
            {
                sqlException.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid query!");
                alert.setHeaderText(sqlException.getMessage());
                alert.getDialogPane().setStyle(" -fx-max-width:500px; -fx-pref-width: 500px;");

                alert.showAndWait();
                return;
            }
            catch (Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Unexpected Error!");
                alert.setHeaderText(e.getMessage());
                alert.getDialogPane().setStyle(" -fx-max-width:500px; -fx-pref-width: 500px;");

                alert.showAndWait();
                return;
            }
            // If user started query with SELECT, an attempt to select the data and print to results section is made.
            if(query.length() >= 6 && query.substring(0,6).toUpperCase().equals("SELECT"))
            {
                try
                {
                    preparedStatement = connection.prepareStatement(query);
                    ResultSet rset = preparedStatement.executeQuery();
                    // obtain meta data for ResultSet
                    ResultSetMetaData metaData = rset.getMetaData();

                    for(int i = 1; i < metaData.getColumnCount(); i++)
                    {
                        TableColumn tc = new TableColumn(metaData.getColumnName(i));
                        tc.setMinWidth(200);
                        table_results.getColumns().add(tc);
                    }
                    ObservableList<TableRow> data = FXCollections.observableArrayList();
                    while(rset.next())
                    {
                        TableRow row;
                        row = new TableRow();
                        for(int j = 1; j <= metaData.getColumnCount(); j++)
                        {
                            TableCell cell = new TableCell();
                            row.setItem(rset.getObject(j));
                            break;
                        }
                        data.add(row);
                    }
                    table_results.setItems(data);
                }
                catch ( SQLException sqlException )
                {
                    sqlException.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Invalid SELECT query!");
                    alert.setHeaderText(sqlException.getMessage());
                    alert.getDialogPane().setStyle(" -fx-max-width:500px; -fx-pref-width: 500px;");

                    alert.showAndWait();
                }
                catch (Exception e)
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Unexpected Error!");
                    alert.setHeaderText(e.getMessage());
                    alert.getDialogPane().setStyle(" -fx-max-width:500px; -fx-pref-width: 500px;");

                    alert.showAndWait();
                }
            }
            // If user started query with DELETE, an attempt to select the data and print to results section is made.
            else if(query.length() >= 6)
            {
                try
                {
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.executeUpdate();

                    String queryType = "";
                    if(query.substring(0,6).toUpperCase().equals("INSERT")) queryType = "INSERT";
                    else if(query.substring(0,6).toUpperCase().equals("DELETE")) queryType = "DELETE";
                    else if(query.substring(0,6).toUpperCase().equals("UPDATE")) queryType = "UPDATE";
                    else if(query.substring(0,4).toUpperCase().equals("DROP")) queryType = "DROP";

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success!");
                    alert.setHeaderText("Your " + queryType + " query was successful.");
                    alert.getDialogPane().setStyle(" -fx-max-width:500px; -fx-pref-width: 500px;");

                    alert.showAndWait();
                }
                catch ( SQLException sqlException )
                {
                    sqlException.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Invalid query!");
                    alert.setHeaderText(sqlException.getMessage());
                    alert.getDialogPane().setStyle(" -fx-max-width:500px; -fx-pref-width: 500px;");

                    alert.showAndWait();
                }
                catch (Exception e)
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Unexpected Error!");
                    alert.setHeaderText(e.getMessage());
                    alert.getDialogPane().setStyle(" -fx-max-width:500px; -fx-pref-width: 500px;");

                    alert.showAndWait();
                }
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Query");
                alert.setHeaderText("You've entered a query that's incompatible with this interface.");
                alert.setContentText("Try using one of the following:\nSELECT\nINSERT\nUPDATE\nDELETE\nDROP\n");
                alert.getDialogPane().setStyle(" -fx-max-width:500px; -fx-pref-width: 500px;");

                alert.showAndWait();
            }
        }
    }
    // Clears the query textArea
    @FXML
    private void clearCommand()
    {
        text_query.setText("");
    }
    // Clears the window where all selects are output.
    @FXML
    private void clearResults()
    {
        //table_results.set
    }
    // Attempts to make a connection/disconnection with the user-defined database
    @FXML
    private void connectDatabase()
    {
        // Disconnect with the database
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
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Failed to disconnect to the database!");
                alert.setHeaderText(e.getMessage());
                alert.getDialogPane().setStyle(" -fx-max-width:500px; -fx-pref-width: 500px;");

                alert.showAndWait();
            }
        }
        // Connect with the database
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
            catch ( Exception e )
            {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Failed to connect to the database!");
                alert.setHeaderText(e.getMessage());
                alert.getDialogPane().setStyle(" -fx-max-width:500px; -fx-pref-width: 500px;");

                alert.showAndWait();
            }
        }
    }
}
