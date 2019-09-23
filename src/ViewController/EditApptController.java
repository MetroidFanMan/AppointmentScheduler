package ViewController;

import static Main.DBConnection.conn;
import Model.Appointment;
import static ViewController.LoginScreenController.user;
import static ViewController.MainScreenController.appointments;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditApptController implements Initializable {

    @FXML
    private Button saveBtn;

    @FXML
    private TextField urlTxt;

    @FXML
    private ChoiceBox<String> titleCb;

    @FXML
    private ChoiceBox<String> descCb;

    @FXML
    private ChoiceBox<String> locCb;

    @FXML
    private ChoiceBox<String> contCb;

    @FXML
    private ChoiceBox<String> typeCb;

    @FXML
    private ChoiceBox<String> startHourCb;

    @FXML
    private ChoiceBox<String> startMinCb;

    @FXML
    private ChoiceBox<String> lengthCb;

    @FXML
    private DatePicker startDateDp;
    Appointment appt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleCb.getItems().addAll("Consulting", "Meeting");
        descCb.getItems().addAll("First Consultation", "First Meeting", "Follow-up");
        locCb.getItems().addAll("Phoenix, Arizona", "Online", "New York, New York", "London, England");
        contCb.getItems().addAll("wflick", "user1", "user2", "test");
        typeCb.getItems().addAll("Group", "1on1");
        for (int i = 9; i <= 16; i++){
            startHourCb.getItems().addAll(Integer.toString(i));
        }
        startMinCb.getItems().addAll("00", "15", "30", "45");
        lengthCb.getItems().addAll("15", "30", "45", "60");
        lengthCb.setValue("30");
    }

    @FXML
    void saveBtn(ActionEvent event) throws SQLException {
        
        int id = appt.getApptId();
        String length = lengthCb.getValue();
        String title = titleCb.getValue();
        String desc = descCb.getValue();
        String loc = locCb.getValue();
        String cont = contCb.getValue();
        String url = urlTxt.getText();
        String type = typeCb.getValue();
        String startHour = startHourCb.getValue();
        String startMin = startMinCb.getValue();
        LocalDate date = startDateDp.getValue();
        String apptConflicts = null;
        boolean conflict = false;
        
        DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("UTC"));
        LocalTime startTime = LocalTime.of(Integer.parseInt(startHour), Integer.parseInt(startMin));
        LocalTime endTime;
        
        switch (length) {
            case "15":
                endTime = startTime.plusMinutes(15);
                break;
            case "30":
                endTime = startTime.plusMinutes(30);
                break;
            case "45":
                endTime = startTime.plusMinutes(45);
                break;
            default:
                endTime = startTime.plusMinutes(60);
                break;
        }        

        LocalDateTime start = LocalDateTime.of(date, startTime);
        LocalDateTime end = LocalDateTime.of(date, endTime);

        ZonedDateTime zdtStart = start.atZone(ZoneId.systemDefault());
        ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.of("UTC"));

        ZonedDateTime zdtEnd = end.atZone(ZoneId.systemDefault());
        ZonedDateTime utcEnd = zdtEnd.withZoneSameInstant(ZoneId.of("UTC"));

        String nowZoned = dtFormat.format(utcNow);
        String startZoned = dtFormat.format(utcStart);
        String endZoned = dtFormat.format(utcEnd);

        for (Appointment a : appointments){
            if (a.getContact().equals(cont)){
                if (a.getStartDate().equals(date)){
                    if (a.getApptId() == appt.getApptId()){
                        //If appointment is the same as the one being edited do nothing
                        
                    } else if ((startTime.isAfter(a.getStartTime()) && startTime.isBefore(a.getEndTime()))){
                        //If start time is between start and end of a scheduled appointment
                        apptConflicts = a.getTitle()+" @ "+a.getStartTime()+" with "+a.getContact()+"\n";
                        conflict = true;
                    } else if (endTime.isAfter(a.getStartTime()) && endTime.isBefore(a.getEndTime())){
                        //If end time is between start and end of a scheduled appointment
                        apptConflicts = a.getTitle()+" @ "+a.getStartTime()+" with "+a.getContact()+"\n";
                        conflict = true;
                    } else if (startTime.equals(a.getEndTime())){
                        //If appointment is starting the same time one is ending
                        apptConflicts = a.getTitle()+" @ "+a.getStartTime()+" with "+a.getContact()+"\n";
                        conflict = true;
                    }
                }
            }
        }

        if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Date");
            alert.setHeaderText("Selected date is not during business hours.");
            alert.setContentText("Please select a day during the week.");
            alert.show();
        } else if (date.isBefore(LocalDate.now())){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Date");
            alert.setHeaderText("Selected date is in the past");
            alert.setContentText("Please select a different day.");
            alert.show();
        } else if (conflict){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Conflicting Appointments");
            alert.setHeaderText("This appointment conflicts with the following:");
            alert.setContentText(apptConflicts);
            alert.show();
        } else {
            try {
                String sql = "Update appointment Set title='"+title+"', description='"+desc+"', location='"+loc+"', contact='"+cont+"', url='"+url+"',"
                        + " type='"+type+"', start='"+startZoned+"', end='"+endZoned+"', lastUpdate='"+nowZoned+"', lastUpdateBy='"+user+"' Where appointmentId="+id;

                Statement stmt = conn.createStatement();
                int rs = stmt.executeUpdate(sql);

                Stage stage = (Stage) saveBtn.getScene().getWindow();
                stage.close();
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText(e.getLocalizedMessage().substring(0, 1).toUpperCase() + e.getLocalizedMessage().substring(1) +" cannot be empty.");
                alert.setContentText("Please select a "+e.getLocalizedMessage()+".");
                alert.show();
            }
            catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText(e.getLocalizedMessage().substring(8, 9).toUpperCase()+e.getLocalizedMessage().substring(9).replace("'", "").replaceAll("null", "empty"));
                alert.setContentText("Please select a "+e.getLocalizedMessage().substring(e.getLocalizedMessage().indexOf("'")+1, e.getLocalizedMessage().lastIndexOf("'"))+".");
                alert.show();
            }
        }
    }

    public void setAppt (Appointment a){
        appt = a;

        String hour = a.getStartTime().toString().substring(0, 2).replaceFirst("^0*", "");
        String min = a.getStartTime().toString().substring(3);

        titleCb.getSelectionModel().select(a.getTitle());
        descCb.getSelectionModel().select(a.getDesc());
        locCb.getSelectionModel().select(a.getLocation());
        urlTxt.setText(a.getUrl());
        contCb.getSelectionModel().select(a.getContact());
        typeCb.getSelectionModel().select(a.getType());
        startHourCb.getSelectionModel().select(hour);
        startMinCb.getSelectionModel().select(min);
        startDateDp.setValue(a.getStartDate());

    }

}
