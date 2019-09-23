/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.sql.*;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Ethan
 */
public class SchedulingApp extends Application {
    
    
    @Override
    public void start(Stage stage) throws Exception {

        ResourceBundle bundle = ResourceBundle.getBundle("language_files/rb");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewController/LoginScreen.fxml"));
        loader.setResources(bundle);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException, Exception {
        
        DBConnection.makeConnection();
        launch(args);
        DBConnection.closeConnection();
        
    }
    
}
