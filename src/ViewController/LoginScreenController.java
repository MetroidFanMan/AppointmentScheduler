package ViewController;

import Main.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginScreenController implements Initializable{

    @FXML
    private TextField nameTxt;

    @FXML
    private PasswordField pwTxt;

    @FXML
    private Button loginBtn;

    @FXML
    private Label titleLbl;

    @FXML
    private Label nameLbl;

    @FXML
    private Label pwLbl;

    ResourceBundle bundle;
    public static String user = "";

    
    public void initialize(URL url, ResourceBundle rb){

        this.bundle = rb;
        System.out.println(Locale.getDefault());
        
        user = nameTxt.getText();

        nameLbl.setText(bundle.getString("name"));
        pwLbl.setText(bundle.getString("pw"));
        titleLbl.setText(bundle.getString("title"));
        loginBtn.setText(bundle.getString("button"));

    }

    @FXML
    void loginBtnAction(ActionEvent event) throws SQLException, IOException {

        String name = nameTxt.getText().trim();
        String pw = pwTxt.getText().trim();
        boolean result = false;
        ResultSet rs;
        Statement stmt;

        stmt = DBConnection.conn.createStatement();
        rs = stmt.executeQuery("Select userName, password From user");

        while (rs.next()){
            if (rs.getString("userName").equals(name)) {
                if (rs.getString("password").equals(pw)) {
                    result = true;
                }
            }
        }
        
        //Logs log in attempts before checking validity
        Logger log = Logger.getLogger("log.txt");

                FileHandler fh = new FileHandler("log.txt", true);
                SimpleFormatter sf = new SimpleFormatter();
                fh.setFormatter(sf);
                log.addHandler(fh);
                log.setLevel(Level.INFO);
                
        
        
        if(result){
            try {
                log.log(Level.WARNING, "Login attempt succeeded");
                FXMLLoader mainLoad = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
                user = nameTxt.getText();
                Parent root = mainLoad.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("Scheduling");
                stage.setScene(scene);
                stage.show();
                Stage login = (Stage) loginBtn.getScene().getWindow();
                fh.close();
                login.close();
            }
            catch (IOException e) {
                System.out.print(Arrays.toString(e.getStackTrace()));
            }
        }
        else {
            log.log(Level.WARNING, "Login attempt failed");
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle(bundle.getString("alertTitle"));
            a.setHeaderText(bundle.getString("alertHeader"));
            a.setContentText(bundle.getString("alertContent"));
            a.showAndWait();
        }
    }
    
}