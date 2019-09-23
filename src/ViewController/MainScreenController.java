package ViewController;

import static Main.DBConnection.conn;
import Model.Appointment;
import Model.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class MainScreenController implements Initializable {

    @FXML
    private TableView<Customer> custTbl;

    @FXML
    private TableColumn<Customer, String> nameCol;

    @FXML
    private TableColumn<Customer, String> addrCol;

    @FXML
    private TableColumn<Customer, String> phoneCol;

    @FXML
    private TableView<Appointment> apptTbl;

    @FXML
    private TableColumn<Appointment, String> typeCol;

    @FXML
    private TableColumn<Appointment, String> contactCol;

    @FXML
    private TableColumn<Appointment, LocalTime> startTimeCol;

    @FXML
    private TableColumn<Appointment, LocalDate> startDateCol;
    public static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    public static ObservableList<Customer> customers = FXCollections.observableArrayList();
    StringBuilder list = new StringBuilder();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            customers = getCust();
            appointments = getAppts();
            list = getNearAppt();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addrCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        custTbl.setItems(customers);

        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        apptTbl.setItems(appointments);
        
            if (!list.toString().isEmpty()){
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Appointments");
                alert.setHeaderText("The following appointments are in the next 15 minutes:");
                alert.setContentText(list.toString());
                alert.showAndWait();
            }
            
    }
    
    @FXML
    void clientSchedule(ActionEvent event) {
        
        Dialog dialog = new Dialog();
        dialog.setTitle("Client Schedule");
        dialog.setHeaderText("Choose a client to see their schedule.");
        
        GridPane gp = new GridPane();
        gp.setVgap(15);
        
        ChoiceBox<String> cb = new ChoiceBox();
        for (Customer c : customers){
            cb.getItems().add(c.getName());
        }
        
        
        TextArea ta = new TextArea();
        ta.setEditable(false);
        ta.setWrapText(true);
        ta.autosize();
        
        gp.add(cb, 0, 0);
        gp.add(ta, 0, 1);
        
        cb.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            StringBuilder appts = new StringBuilder();
            int count = 0;
            for (Customer c : customers){
                int cId = 0;
                if (c.getName().equals(newValue)){
                    cId = c.getcId();
                }
                for (Appointment a : appointments){
                    if (a.getCustId() == cId){
                        appts.append(newValue);
                        appts.append(" has an appointment on ");
                        appts.append(a.getStartDate());
                        appts.append(" at ");
                        appts.append(a.getStartTime());
                        appts.append(" with ");
                        appts.append(a.getContact());
                        appts.append(".\n");
                        count++;
                    } 
                }
            }
            
            if (count == 0){
                    appts.append("This consultant has no scheduled appointments.");
            }
            ta.setText(appts.toString());
        });
        
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.getDialogPane().setContent(gp);
        dialog.show();
    }

    @FXML
    void consultantSchedule(ActionEvent event) {
        
        Dialog dialog = new Dialog();
        dialog.setTitle("Consult Schedule");
        dialog.setHeaderText("Choose a consultant to see their schedule");
        
        GridPane gp = new GridPane();
        gp.setVgap(15);
        
        ChoiceBox<String> cb = new ChoiceBox();
        cb.getItems().addAll("wflick", "user1", "user2", "test");
        
        TextArea ta = new TextArea();
        ta.setEditable(false);
        ta.setWrapText(true);
        ta.autosize();
        
        gp.add(cb, 0, 0);
        gp.add(ta, 0, 1);
        
        cb.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
//            StringBuilder appts = new StringBuilder();
            String text = "";
            int count = 0;
            for (Appointment a : appointments){
                if (a.getContact().equals(newValue)){
                    count++;
                }
            }
            String[] time = new String[count];
            for (int i = 0; i < count; i++){
                if (appointments.get(i).getContact().equals(newValue)){
                    StringBuilder appts = new StringBuilder();
//                    appts.append(a.getContact());
//                    appts.append(" has an appointment on ");
                    appts.append(appointments.get(i).getStartDate());
//                    appts.append(" at ");
                    appts.append(" @ ");
                    appts.append(appointments.get(i).getStartTime());
                    appts.append("\n");
                    time[i] = appts.toString();
                }
                text = appointments.get(i).getContact() + " has " + count + " appointment(s) on: \n";
                
                if (count == 0){
//              StringBuilder appts = new StringBuilder();
//              appts.append("This consultant has no scheduled appointments.");
                text = appointments.get(i).getContact() + " has no appointments.";
                }
            }
            
            ta.setText(text);
//            appts.toString()
        });
        
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.getDialogPane().setContent(gp);
        dialog.show();
        
        
    }

    @FXML
    void apptTypes(ActionEvent event) {
        
        Dialog dialog = new Dialog();
        dialog.setTitle("Appointment Types Scheduled");
        dialog.setHeaderText("Choose a month to see the number of appointment types.");
        
        GridPane gp = new GridPane();
        gp.setVgap(15);
        
        ChoiceBox<String> cb = new ChoiceBox();
        cb.getItems().add("January");
        cb.getItems().add("February");
        cb.getItems().add("March");
        cb.getItems().add("April");
        cb.getItems().add("May");
        cb.getItems().add("June");
        cb.getItems().add("July");
        cb.getItems().add("August");
        cb.getItems().add("September");
        cb.getItems().add("October");
        cb.getItems().add("November");
        cb.getItems().add("December");
        
        TextArea ta = new TextArea();
        ta.setEditable(false);
        ta.setWrapText(true);
        ta.autosize();

        
        gp.add(cb, 0, 0);
        gp.add(ta, 0, 1);
        
        cb.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            StringBuilder types = new StringBuilder();
            int groupCount = 0;
            int oneOnOneCount = 0;
            String month = newValue;
            
            for (Appointment a : appointments){
                switch (month){
                    case "January":
                        if (a.getStartDate().getMonth().equals(Month.JANUARY)){
                            if (a.getType().equals("Group")){
                                groupCount++;
                            } else if (a.getType().equals("1on1")){
                                oneOnOneCount++;
                            }
                        }
                        break;
                    case "February":
                        if (a.getStartDate().getMonth().equals(Month.FEBRUARY)){
                            if (a.getType().equals("Group")){
                                groupCount++;
                            } else if (a.getType().equals("1on1")){
                                oneOnOneCount++;
                            }
                        }
                        break;
                    case "March":
                        if (a.getStartDate().getMonth().equals(Month.MARCH)){
                            if (a.getType().equals("Group")){
                                groupCount++;
                            } else if (a.getType().equals("1on1")){
                                oneOnOneCount++;
                            }
                        }
                        break;
                    case "April":
                        if (a.getStartDate().getMonth().equals(Month.APRIL)){
                            if (a.getType().equals("Group")){
                                groupCount++;
                            } else if (a.getType().equals("1on1")){
                                oneOnOneCount++;
                            }
                        }
                        break;
                    case "May":
                        if (a.getStartDate().getMonth().equals(Month.MAY)){
                            if (a.getType().equals("Group")){
                                groupCount++;
                            } else if (a.getType().equals("1on1")){
                                oneOnOneCount++;
                            }
                        }
                        break;
                    case "June":
                        if (a.getStartDate().getMonth().equals(Month.JUNE)){
                            if (a.getType().equals("Group")){
                                groupCount++;
                            } else if (a.getType().equals("1on1")){
                                oneOnOneCount++;
                            }
                        }
                        break;
                    case "July":
                        if (a.getStartDate().getMonth().equals(Month.JULY)){
                            if (a.getType().equals("Group")){
                                groupCount++;
                            } else if (a.getType().equals("1on1")){
                                oneOnOneCount++;
                            }
                        }
                        break;
                    case "August":
                        if (a.getStartDate().getMonth().equals(Month.AUGUST)){
                            if (a.getType().equals("Group")){
                                groupCount++;
                            } else if (a.getType().equals("1on1")){
                                oneOnOneCount++;
                            }
                        }
                        break;
                    case "September":
                        if (a.getStartDate().getMonth().equals(Month.SEPTEMBER)){
                            if (a.getType().equals("Group")){
                                groupCount++;
                            } else if (a.getType().equals("1on1")){
                                oneOnOneCount++;
                            }
                        }
                        break;
                    case "October":
                        if (a.getStartDate().getMonth().equals(Month.OCTOBER)){
                            if (a.getType().equals("Group")){
                                groupCount++;
                            } else if (a.getType().equals("1on1")){
                                oneOnOneCount++;
                            }
                        }
                        break;
                    case "November":
                        if (a.getStartDate().getMonth().equals(Month.NOVEMBER)){
                            if (a.getType().equals("Group")){
                                groupCount++;
                            } else if (a.getType().equals("1on1")){
                                oneOnOneCount++;
                            }
                        }
                        break;
                    case "December":
                        if (a.getStartDate().getMonth().equals(Month.DECEMBER)){
                            if (a.getType().equals("Group")){
                                groupCount++;
                            } else if (a.getType().equals("1on1")){
                                oneOnOneCount++;
                            }
                        }
                        break;
                }
        }
            types.append("There are ");
            types.append(groupCount);
            types.append(" Group and ");
            types.append(oneOnOneCount);
            types.append(" 1on1 appointment types scheduled for ");
            types.append(month);
            types.append(".");
            ta.setText(types.toString());
        });
        
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.getDialogPane().setContent(gp);
        dialog.show();
    }


    @FXML
    void allRbAction(ActionEvent event){
        apptTbl.setItems(appointments);
    }
    
    
    @FXML
    void monthRbAction(ActionEvent event) throws SQLException{
        ObservableList<Appointment> monthAppt = FXCollections.observableArrayList();
        Calendar currCal = Calendar.getInstance();
        int currMonth = currCal.get(Calendar.MONTH);
        ObservableList<Appointment> appt = getAppts();
        
        //Cycles through array list to check for the month of appointment
        appt.forEach((a) -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String ldt = a.getStartDate()+" "+a.getStartTime();
                Calendar cal = Calendar.getInstance();
                Date date = sdf.parse(ldt);
                cal.setTime(date);
                int apptMonth = cal.get(Calendar.MONTH);
                if (currMonth == apptMonth) {
                    monthAppt.add(a);
                }
            }
            catch (ParseException ex) {
                ex.printStackTrace();
            }
        });
        apptTbl.setItems(monthAppt);
    }


    @FXML
    void weekRbAction(ActionEvent event) throws SQLException{
        ObservableList<Appointment> weekAppt = FXCollections.observableArrayList();
        Calendar currCal = Calendar.getInstance();
        int currWeek = currCal.get(Calendar.WEEK_OF_MONTH);
        int currMonth = currCal.get(Calendar.MONTH);
        ObservableList<Appointment> appt = getAppts();
        
        //Cycles through array list to check for matching weeks
        appt.forEach((a) -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String ldt = a.getStartDate()+" "+a.getStartTime();
                Calendar cal = Calendar.getInstance();
                Date date = sdf.parse(ldt);
                cal.setTime(date);
                int apptMonth = cal.get(Calendar.MONTH);
                if (currMonth == apptMonth) {
                    int apptWeek = cal.get(Calendar.WEEK_OF_MONTH);
                    if (currWeek == apptWeek){
                        weekAppt.add(a);
                    }
                }
            }
            catch (ParseException ex) {
                ex.printStackTrace();
            }
        });

        apptTbl.setItems(weekAppt);
    }


    @FXML
    void createAppt(ActionEvent event) throws IOException, SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateAppt.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Create Appointment");
            Scene scene = new Scene(root);
            CreateApptController controller = loader.getController();
            Customer c = custTbl.getSelectionModel().getSelectedItem();
            controller.setCust(c);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            appointments.clear();
            appointments = getAppts();
            apptTbl.setItems(appointments);
            custTbl.getSelectionModel().clearSelection();
            apptTbl.getSelectionModel().clearSelection();
        }
        catch (NullPointerException e){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Customer Selection Error");
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("Please select a customer");
            alert.show();
        }
    }


    @FXML
    void createCustomer(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateCust.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Create Customer");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        customers.clear();
        customers = getCust();
        custTbl.setItems(customers);
        custTbl.getSelectionModel().clearSelection();
        apptTbl.getSelectionModel().clearSelection();
    }


    @FXML
    void deleteAppt(ActionEvent event) throws SQLException {
        try {
            Appointment selected = apptTbl.getSelectionModel().getSelectedItem();
            int id = selected.getApptId();
            String sql = "DELETE FROM appointment WHERE appointmentId = " + id;

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Delete Appointment");
            alert.setHeaderText("Delete Appointment Record Permanently");
            alert.setContentText("Do you wish to continue?");

            Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){
                    Statement stmt = conn.createStatement();
                    int rs = stmt.executeUpdate(sql);
                    
                    appointments.clear();
                    appointments = getAppts();
                    apptTbl.setItems(appointments);
                    custTbl.getSelectionModel().clearSelection();
                    apptTbl.getSelectionModel().clearSelection();
                }
                else {
                    alert.close();
                }
        } catch (NullPointerException e){
            Alert alert1 = new Alert(AlertType.ERROR);
            alert1.setTitle("Appointment Selection Error");
            alert1.setHeaderText("No Appointment Selected");
            alert1.setContentText("Please select an appointment");
            alert1.show();
        }
    }


    @FXML
    void deleteCustomer(ActionEvent event) throws SQLException {
        try {
            Customer selected = custTbl.getSelectionModel().getSelectedItem();
            int cId = selected.getcId();
            int aId = selected.getaId();
            String deleteCust = "DELETE FROM customer WHERE customerId = " + cId;
            String deleteAppt = "DELETE FROM appointment WHERE customerId = " + cId;
            String deleteAddr = "Delete FROM address WHERE addressId = " + aId;

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Delete Customer");
            alert.setHeaderText("Delete Customer Record Permanently");
            alert.setContentText("Deleting a customer will delete any associated appointment. \nDo you wish to continue?");

            Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){
                    Statement stmt = conn.createStatement();
                    int rsCust = stmt.executeUpdate(deleteCust);
                    int rsAppt = stmt.executeUpdate(deleteAppt);
                    int rsAddr = stmt.executeUpdate(deleteAddr);
                    
                    customers.clear();
                    customers = getCust();
                    custTbl.setItems(customers);
                    appointments.clear();
                    appointments = getAppts();
                    apptTbl.setItems(appointments);
                    custTbl.getSelectionModel().clearSelection();
                    apptTbl.getSelectionModel().clearSelection();
                } else {
                    alert.close();
                }
        } catch (NullPointerException e) {
            Alert alert1 = new Alert(AlertType.ERROR);
            alert1.setTitle("Customer Selection Error");
            alert1.setHeaderText("No Customer Selected");
            alert1.setContentText("Please select a customer");
            alert1.show();
        }
    }


    @FXML
    void editAppt(ActionEvent event) throws IOException, SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditAppt.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Edit Appointment");
            Scene scene = new Scene(root);
            EditApptController controller = loader.getController();
            Appointment a = apptTbl.getSelectionModel().getSelectedItem();
            controller.setAppt(a);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            appointments.clear();
            appointments = getAppts();
            apptTbl.setItems(appointments);
            custTbl.getSelectionModel().clearSelection();
            apptTbl.getSelectionModel().clearSelection();
        }
        catch (NullPointerException e){
            Alert alert1 = new Alert(AlertType.ERROR);
            alert1.setTitle("Appointment Selection Error");
            alert1.setHeaderText("No Appointment Selected");
            alert1.setContentText("Please select an appointment");
            alert1.show();
        }
    }


    @FXML
    void editCustomer(ActionEvent event) throws IOException, SQLException{
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditCust.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Edit Customer");
            Scene scene = new Scene(root);
            EditCustController controller = loader.getController();
            Customer c = custTbl.getSelectionModel().getSelectedItem();
            controller.setCust(c);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            customers.clear();
            customers = getCust();
            custTbl.setItems(customers);
            custTbl.getSelectionModel().clearSelection();
            apptTbl.getSelectionModel().clearSelection();
        }
        catch (NullPointerException e){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Customer Selection Error");
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("Please select a customer");
            alert.show();
        }
    }

    //Closes program
    @FXML
    void exitApp(ActionEvent event) {
        System.exit(0);
    }

    //Creates a new screen that shows detailed customer info
    //This screen is uneditable. For viewing purposes only.
    @FXML
    void showCustDetails(ActionEvent event) throws IOException, SQLException {
        Customer c = custTbl.getSelectionModel().getSelectedItem();
        Appointment a = apptTbl.getSelectionModel().getSelectedItem();        
        
        if (a == null && c == null){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Selection Made");
            alert.setContentText("Please select a customer or appointment!");
            alert.show();
        }
        
        
        if (c != null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CustDetails.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Customer Details");
            Scene scene = new Scene(root);
            CustDetailsController controller = loader.getController();
            controller.setCust(c);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            custTbl.getSelectionModel().clearSelection();
        }
        else if (a != null){
            int cId = a.getCustId();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CustDetails.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Customer Details");
            Scene scene = new Scene(root);
            CustDetailsController controller = loader.getController();
            for (Customer customer: customers){
                if (customer.getcId()==cId){
                    controller.setCust(customer);
                }
            }
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            apptTbl.getSelectionModel().clearSelection();
        }

    }

    //SQL query to get a result set of customers
    public ResultSet getCustList() throws SQLException {
        Statement stmt;
        ResultSet rs = null;

        try {
            String sql = "Select customerId, customerName, customer.addressId, address, address2, city, country, postalCode, phone "
                    + "From customer, address, city, country Where customer.addressId = address.addressId And address.cityId = city.cityId "
                    + "And city.countryId = country.countryId";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    //SQL query to get a result set of appointments
    public ResultSet getApptList() throws SQLException {
        Statement stmt;
        ResultSet rs = null;

        try {
            String sql = "Select appointmentId, customer.customerId, type, title, location, description, url, contact, start, end From appointment, customer Where customer.customerId = appointment.customerId;";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    //Uses sql query above to create a new appointment object.
    public ObservableList<Appointment> getAppts() throws SQLException{
        ObservableList<Appointment> appt = FXCollections.observableArrayList();
        ResultSet rs = getApptList();

        while (rs.next()){
            int aId = rs.getInt("appointmentId");
            int cId = rs.getInt("customerId");
            String type = rs.getString("type");
            String contact = rs.getString("contact");
            String desc = rs.getString("description");
            String title = rs.getString("title");
            String location = rs.getString("location");
            String url = rs.getString("url");
            Timestamp start = rs.getTimestamp("start");
            Timestamp end = rs.getTimestamp("end");
            LocalDateTime ldtStart = start.toLocalDateTime();
            LocalDateTime ldtEnd = end.toLocalDateTime();
            LocalTime localStart = ldtStart.toLocalTime();
            LocalTime localEnd = ldtEnd.toLocalTime();
            LocalDate localDate = ldtStart.toLocalDate();


            Appointment appointment = new Appointment();
            appointment.setApptId(aId);
            appointment.setCustId(cId);
            appointment.setType(type);
            appointment.setContact(contact);
            appointment.setTitle(title);
            appointment.setLocation(location);
            appointment.setUrl(url);
            appointment.setDesc(desc);
            appointment.setStartTime(localStart);
            appointment.setEndTime(localEnd);
            appointment.setStartDate(localDate);
            appt.addAll(appointment);
        }

        return appt;
    }

    //Uses sql query above to create a new customer object.
    public ObservableList<Customer> getCust() throws SQLException{
        ObservableList<Customer> cust = FXCollections.observableArrayList();
        ResultSet rs = getCustList();

        while (rs.next()){
            int cId = rs.getInt("customerId");
            String name = rs.getString("customerName");
            int aId = rs.getInt("addressId");
            String addr = rs.getString("address");
            String addr2 = rs.getString("address2");
            String city = rs.getString("city");
            String country = rs.getString("country");
            String zip = rs.getString("postalCode");
            String phone = rs.getString("phone");

            Customer customer = new Customer();
            customer.setcId(cId);
            customer.setName(name);
            customer.setaId(aId);
            customer.setAddress(addr);
            customer.setAddress2(addr2);
            customer.setCity(city);
            customer.setCountry(country);
            customer.setZipcode(zip);
            customer.setPhone(phone);
            cust.addAll(customer);
        }
        return cust;
    }
    
    //Shows appointments for customer in appointment table
    public void showCustAppointments(){
        Customer c = custTbl.getSelectionModel().getSelectedItem();
        int cId = c.getcId();
        ObservableList<Appointment> appts = FXCollections.observableArrayList();
        
        for (Appointment a : appointments){
            if (cId == a.getCustId()){
                appts.add(a);
            }
        }
        
        apptTbl.setItems(appts);
        
    }
    
    public void refresh(){
        custTbl.getSelectionModel().clearSelection();
        apptTbl.getSelectionModel().clearSelection();
        custTbl.setItems(customers);
        apptTbl.setItems(appointments);
    }
    
    //Creates string to be used in the near appointments alert
    public StringBuilder getNearAppt() throws SQLException {
        DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime zdtNow = ZonedDateTime.now(ZoneId.of("UTC"));
        StringBuilder sAppt = new StringBuilder();
        String sql = "Select * From appointment";
        
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while (rs.next()){
            String startAppt = rs.getString("start");
            LocalDateTime ldtStart = LocalDateTime.parse(startAppt, dtFormat);
            ZonedDateTime zdtStart = ZonedDateTime.of(ldtStart, ZoneId.of("UTC"));
            
            if (zdtNow.plusMinutes(15).isAfter(zdtStart) && zdtStart.isAfter(zdtNow)){
                int id = rs.getInt("appointmentId");
                for (Appointment a : appointments){
                    if (id == a.getApptId()){
                        sAppt.append(a.getTitle());
                        sAppt.append(" @ ");
                        sAppt.append(a.getStartTime());
                        sAppt.append(" with ");
                        sAppt.append(a.getContact());
                        sAppt.append("\n");
                    }
                }
            }
        }
        
        return sAppt;
        
    }
    
}
