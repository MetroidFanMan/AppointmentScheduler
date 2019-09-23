package ViewController;

import static Main.DBConnection.conn;
import Model.Customer;
import static ViewController.LoginScreenController.user;
import java.io.IOException;
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

public class EditCustController {

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
    Customer cust;

    @FXML
    void saveBtn(ActionEvent event) throws SQLException, IOException {
        String name = nameTxt.getText();
        String addr = addrTxt.getText();
        String addr2 = addr2Txt.getText();
        String city = cityTxt.getText();
        String country = countryTxt.getText();
        String zip = zipTxt.getText();
        String phone = phoneTxt.getText();
        
        if (name.isEmpty()){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Name Not Valid");
            alert.setContentText("Please enter a name.");
            alert.show();
        } else if (addr.isEmpty()){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Address Not Valid");
            alert.setContentText("Please enter an address.");
            alert.show();
        } else if (zip.isEmpty()){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Zipcode Not Valid");
            alert.setContentText("Please enter a zipcode.");
            alert.show();
        } else if (phone.isEmpty()){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Phone Number Not Valid");
            alert.setContentText("Please enter a phone number.");
            alert.show();
        } else if (city.isEmpty()){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("City Not Valid");
            alert.setContentText("Please enter a city.");
            alert.show();
        } else if (country.isEmpty()){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Country Not Valid");
            alert.setContentText("Please enter a country.");
            alert.show();
        } else {
            DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("UTC"));
            String nowZoned = dtFormat.format(utcNow);
            
            Statement stmt = conn.createStatement();
            int rs = stmt.executeUpdate("Update customer, address, city, country Set customer.customerName = '"+name+"', customer.lastUpdateBy = '"+user+"',"
                    + " customer.lastUpdate = '"+nowZoned+"', address.address = '"+addr+"', address.lastUpdateBy = '"+user+"', address.lastUpdate = '"+nowZoned+"',"
                    + " address.address2 = '"+addr2+"', address.phone = '"+phone+"', address.postalCode = '"+zip+"', city.city = '"+city+"', city.lastUpdateBy = '"+user+"',"
                    + " city.lastUpdate = '"+nowZoned+"', country.country = '"+country+"', country.lastUpdateBy = '"+user+"', country.lastUpdate = '"+nowZoned+"'"
                    + " Where country.countryId = city.countryId And address.cityId = city.cityId And customer.addressId = address.addressId And customer.customerId = "+cust.getcId());

            Stage stage = (Stage) saveBtn.getScene().getWindow();
            stage.close();
        }
    }

    public void setCust(Customer c){
        cust = c;

        nameTxt.setText(cust.getName());
        addrTxt.setText(cust.getAddress());
        addr2Txt.setText(cust.getAddress2());
        cityTxt.setText(cust.getCity());
        countryTxt.setText(cust.getCountry());
        zipTxt.setText(cust.getZipcode());
        phoneTxt.setText(cust.getPhone());

    }



}
