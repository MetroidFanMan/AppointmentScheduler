package ViewController;

import Model.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CustDetailsController {

    @FXML
    private Label nameLbl;

    @FXML
    private Label addrLbl;

    @FXML
    private Label addr2Lbl;

    @FXML
    private Label cityLbl;

    @FXML
    private Label countryLbl;

    @FXML
    private Label zipLbl;

    @FXML
    private Label phoneLbl;
    Customer c;

    
    public void setCust(Customer c){
        this.c = c;
        
        nameLbl.setText(c.getName());
        addrLbl.setText(c.getAddress());
        addr2Lbl.setText(c.getAddress2());
        cityLbl.setText(c.getCity());
        countryLbl.setText(c.getCountry());
        zipLbl.setText(c.getZipcode());
        phoneLbl.setText(c.getPhone());
        
    }
    
}
