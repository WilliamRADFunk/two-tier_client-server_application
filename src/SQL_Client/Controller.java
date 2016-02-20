/*
** Name: William Funk
** Course: CNT 4714 Spring 2016
** Assignment title: Project 2 – Synchronized, Cooperating Threads Under Locking
** Due Date: February 14, 2016
*/
package SQL_Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert btn_execute != null : "fx:id=\"btn_execute\" was not injected: check your FXML file 'balanceSheet.fxml'.";
        btn_execute.setDisable(true);
        assert btn_clearCommand != null : "fx:id=\"btn_clearCommand\" was not injected: check your FXML file 'balanceSheet.fxml'.";
        btn_clearCommand.setDisable(true);
        assert btn_clearResults != null : "fx:id=\"btn_clearResults\" was not injected: check your FXML file 'balanceSheet.fxml'.";
        btn_clearResults.setDisable(true);
        assert btn_connect != null : "fx:id=\"btn_connect\" was not injected: check your FXML file 'balanceSheet.fxml'.";
        assert text_username != null : "fx:id=\"text_username\" was not injected: check your FXML file 'balanceSheet.fxml'.";
        assert text_password != null : "fx:id=\"text_password\" was not injected: check your FXML file 'balanceSheet.fxml'.";
        assert text_query != null : "fx:id=\"text_query\" was not injected: check your FXML file 'balanceSheet.fxml'.";
        assert text_results != null : "fx:id=\"text_results\" was not injected: check your FXML file 'balanceSheet.fxml'.";
        assert choice_driver != null : "fx:id=\"choice_driver\" was not injected: check your FXML file 'balanceSheet.fxml'.";
    }

    @FXML
    private void executeCommand()
    {

    }
    @FXML
    private void clearCommand()
    {
        btn_clearCommand.setDisable(true);
        text_query.setText("");
    }
    @FXML
    private void clearResults()
    {
        btn_clearResults.setDisable(true);
        text_results.setText("");
    }
    @FXML
    private void connectDatabase()
    {
        btn_connect.setText("Disconnect from Database");
        btn_connect.setStyle("-fx-background-color:red");
    }
    @FXML
    private void SQLentered()
    {
        btn_clearCommand.setDisable(false);
    }
}
