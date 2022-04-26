package com.example.studentregistrationui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import java.awt.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class Controller extends Component implements Initializable {
    @FXML
    private TextField name;
    @FXML
    private TextField number;
    @FXML
    private TextField course;
    @FXML
    private TextField id;
    @FXML
    private TableView<TableRecords> recordsTable;
    @FXML
    private TableColumn<TableRecords, String> col_ID;
    @FXML
    private TableColumn<TableRecords, String> col_Name;
    @FXML
    private TableColumn<TableRecords, String> col_Number;
    @FXML
    private TableColumn<TableRecords, String> col_Course;

    ObservableList<TableRecords>oblist = FXCollections.observableArrayList();

    public static final String DB_NAME = "studentRegistration.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:D:\\JAVA SQL LITE DATABASES\\" + DB_NAME;

    public static final String TABLE_RECORDS = "records";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "student_name";
    public static final String COLUMN_MOBILE = "mobile";
    public static final String COLUMN_COURSE = "course";

    public static final String INSERT_NEW_RECORD = "INSERT INTO " + TABLE_RECORDS + " (" + COLUMN_NAME + ", " + COLUMN_MOBILE + ", " + COLUMN_COURSE + ")" + " VALUES(?, ?, ?)";
    public static final String DISPLAY_RECORDS_ON_TABLE = "SELECT * FROM " + TABLE_RECORDS;

    private PreparedStatement insertStudentRecords;
    private PreparedStatement viewRecordsInTable;
    private Connection connection;

    // ADDING A NEW STUDENT INTO THE DATABASE
    @FXML
    private void addStudent() {
        String studentName = name.getText();
        String studentNumber = number.getText();
        String studentCourse = course.getText();
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
            insertStudentRecords = connection.prepareStatement(INSERT_NEW_RECORD);
            insertStudentRecords.setString(1, studentName);
            insertStudentRecords.setString(2, studentNumber);
            insertStudentRecords.setString(3, studentCourse);
            insertStudentRecords.executeUpdate();
            name.setText("");
            number.setText("");
            course.setText("");
            name.requestFocus();
            updateTable();
            insertStudentRecords.close();
            connection.close();
        } catch (SQLException err) {
            System.out.println("QUERY FAILED : " + err.getMessage());
        }
    }

    // UPDATE TABLE FUNCTION
    public void updateTable(){
        try{
            connection = DriverManager.getConnection(CONNECTION_STRING);
            viewRecordsInTable = connection.prepareStatement(DISPLAY_RECORDS_ON_TABLE);
            oblist.clear(); // Clearing the lst of data and adding to prevent data repetition
            ResultSet resultSet = connection.createStatement().executeQuery(DISPLAY_RECORDS_ON_TABLE);
            while (resultSet.next()){
                oblist.add(new TableRecords(
                        resultSet.getString("_id"),
                        resultSet.getString("student_name"),
                        resultSet.getString("mobile"),
                        resultSet.getString("course")));
            }
            viewRecordsInTable.close();
            connection.close();
        } catch (SQLException err) {
            System.out.println("QUERY FAILED : "+err.getMessage());
        }
        col_ID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        col_Name.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        col_Number.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        col_Course.setCellValueFactory(new PropertyValueFactory<>("studentCourse"));
        recordsTable.setItems(oblist);
    }

    // UPDATING A RECORD ON THE TABLE VIEW
    public void changeStudentNameCellEvent(TableColumn.CellEditEvent editedCell){ // cellEditEvent allows user to double click to select a record cell
        TableRecords tableRecords = recordsTable.getSelectionModel().getSelectedItem(); // selecting the row the user clicks on
        tableRecords.setStudentName((String) editedCell.getNewValue()); // cast to string
    }

    public void changeStudentNumberCellEvent(TableColumn.CellEditEvent editedCell){ // cellEditEvent allows user to double click to select a record cell
        TableRecords tableRecords = recordsTable.getSelectionModel().getSelectedItem(); // selecting the row the user clicks on
        tableRecords.setStudentNumber((String) editedCell.getNewValue()); // cast to string
    }

    public void changeStudentCourseCellEvent(TableColumn.CellEditEvent editedCell){ // cellEditEvent allows user to double click to select a record cell
        TableRecords tableRecords = recordsTable.getSelectionModel().getSelectedItem(); // selecting the row the user clicks on
        tableRecords.setStudentCourse((String) editedCell.getNewValue()); // cast to string
    }

    // DELETING A RECORD FROM THE TABLE VIEW
    public void deleteRowFromTable() {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
            int selectedID = recordsTable.getSelectionModel().getSelectedIndex(); // extracting the row index of the selected column
            recordsTable.getItems().remove(selectedID);
            recordsTable.refresh();
            connection.close();
        } catch (SQLException err){
            System.out.println("QUERY FAILED : "+err.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTable();
        // Making the columns editable for the Update Function (CRUD)
        recordsTable.setEditable(true); // Allowing the tableView to be editable
        col_Name.setCellFactory(TextFieldTableCell.forTableColumn()); // Makes a text field when the user double clicks on the cell
        col_Number.setCellFactory(TextFieldTableCell.forTableColumn()); // Cell factory -> How the cell is going to behave
        col_Course.setCellFactory(TextFieldTableCell.forTableColumn());
    }
}
