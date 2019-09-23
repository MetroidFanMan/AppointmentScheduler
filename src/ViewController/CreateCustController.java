package ViewController;

import static Main.DBConnection.conn;
import static ViewController.LoginScreenController.user;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateCustController {

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField addrTxt;
    
    @FXML
    private TextField addr2Txt;

    @FXML
    private TextField cityTxt;

    @FXML
    private TextField countryTxt;

    @FXML
    private TextField zipTxt;

    @FXML
    private TextField phoneTxt;
    
    @FXML
    private Button saveBtn;

    @FXML
    void saveBtn(ActionEvent event) throws SQLException {
        
        String name = nameTxt.getText();
        String addr = addrTxt.getText();
        String addr2 = addr2Txt.getText();
        String city = cityTxt.getText();
        String country = countryTxt.getText();
        String zip = zipTxt.getText();
        String phone = phoneTxt.getText();
        int cityId = 0;
        int addressId = 0;
        int countryId = 0;
        boolean cityFound = false;
        boolean countryFound = false;
        
        if (name.equals("")){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Name Not Valid");
            alert.setContentText("Please enter a name.");
            alert.show();
        } else if (addr.equals("")){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Address Not Valid");
            alert.setContentText("Please enter an address.");
            alert.show();
        } else if (city.equals("")){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("City Not Valid");
            alert.setContentText("Please enter a city.");
            alert.show();
        } else if (country.equals("")){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Country Not Valid");
            alert.setContentText("Please enter a country.");
            alert.show();
        } else if (zip.equals("")){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Zip Not Valid");
            alert.setContentText("Please enter a zipcode.");
            alert.show();
        } else if (phone.equals("")){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Phone Not Valid");
            alert.setContentText("Please enter a phone number.");
            alert.show();
        } else {
        
            try {
                
                DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("UTC"));
                String nowZoned = dtFormat.format(utcNow);

                String sqlMatches = "Select cityId, city, country.countryId, country From city, country";
                String sqlCountry = "Insert Into country (country, createDate, createdBy, lastUpdate, lastUpdateBy) Values (?, ?, ?, ?, ?)";
                String sqlCity = "Insert Into city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) Values (?, ?, ?, ?, ?, ?)";
                String sqlAddress = "Insert Into address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
                    + "Values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                String sqlCustomer = "Insert Into customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) "
                        + "Values (?, ?, ?, ?, ?, ?, ?)";

                Statement stmtMatches = conn.createStatement();
                ResultSet rsMatches = stmtMatches.executeQuery(sqlMatches);
                rsMatches.beforeFirst();
                while (rsMatches.next()){
                    String dbCountry = rsMatches.getString("country");
                    String dbCity = rsMatches.getString("city");
                    if (country.equalsIgnoreCase(dbCountry)){
                        countryFound = true;
                        countryId = rsMatches.getInt("countryId");
                    } 
                    if (city.equalsIgnoreCase(dbCity)){
                        cityFound = true;
                        cityId = rsMatches.getInt("cityId");
                    }
                }

                if (!countryFound){
                    PreparedStatement ps = conn.prepareStatement(sqlCountry);
                    ps.setString(1, country);
                    ps.setString(2, nowZoned);
                    ps.setString(3, user);
                    ps.setString(4, nowZoned);
                    ps.setString(5, user);

                    int result = ps.executeUpdate();

                    String getSql = "Select last_insert_id()";
                    PreparedStatement getPs = conn.prepareStatement(getSql);
                    ResultSet getRs = getPs.executeQuery(getSql);
                    getRs.beforeFirst();
                    getRs.next();
                    countryId = getRs.getInt("last_insert_id()");
                } 

                if (!cityFound){
                    PreparedStatement ps = conn.prepareStatement(sqlCity);
                    ps.setString(1, city);
                    ps.setInt(2, countryId);
                    ps.setString(3, nowZoned);
                    ps.setString(4, user);
                    ps.setString(5, nowZoned);
                    ps.setString(6, user);

                    int result = ps.executeUpdate();

                    String getSql = "Select last_insert_id()";
                    PreparedStatement getPs = conn.prepareStatement(getSql);
                    ResultSet getRs = getPs.executeQuery(getSql);
                    getRs.beforeFirst();
                    getRs.next();
                    cityId = getRs.getInt("last_insert_id()");
                }

                PreparedStatement ps = conn.prepareStatement(sqlAddress);
                ps.setString(1, addr);
                ps.setString(2, addr2);
                ps.setInt(3, cityId);
                ps.setString(4, zip);
                ps.setString(5, phone);
                ps.setString(6, nowZoned);
                ps.setString(7, user);
                ps.setString(8, nowZoned);
                ps.setString(9, user);

                int result = ps.executeUpdate();

                String getSql = "Select last_insert_id()";
                PreparedStatement getPs = conn.prepareStatement(getSql);
                ResultSet getRs = getPs.executeQuery(getSql);
                getRs.beforeFirst();
                getRs.next();
                addressId = getRs.getInt("last_insert_id()");

                PreparedStatement psCustomer = conn.prepareStatement(sqlCustomer);
                psCustomer.setString(1, name);
                psCustomer.setInt(2, addressId);
                psCustomer.setInt(3, 1);
                psCustomer.setString(4, nowZoned);
                psCustomer.setString(5, user);
                psCustomer.setString(6, nowZoned);
                psCustomer.setString(7, user);

                int rs = psCustomer.executeUpdate();

                Stage stage = (Stage) saveBtn.getScene().getWindow();
                stage.close();
                    
            } catch(SQLException e){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText(e.getLocalizedMessage().substring(8, 9).toUpperCase()+e.getLocalizedMessage().substring(9).replace("'", "").replaceAll("null", "empty"));
                alert.setContentText("Please select a "+e.getLocalizedMessage().substring(e.getLocalizedMessage().indexOf("'")+1, e.getLocalizedMessage().lastIndexOf("'"))+".");
                alert.show();
            }
        }
    }
}