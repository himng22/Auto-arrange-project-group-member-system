import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentPersonalController implements Initializable {

    private ObservableList<ShowStudent> showStudentList;

    @FXML
    private TableView<ShowStudent> studentsTableView;

    private TableColumn<ShowStudent, String> rowIdxColumn;

    private TableColumn<ShowStudent, String> idColumn;

    private TableColumn<ShowStudent, String> nameColumn;

    private TableColumn<ShowStudent, String> emailColumn;

    private TableColumn<ShowStudent, String> k1Column;

    private TableColumn<ShowStudent, String> k2Column;

    private TableColumn<ShowStudent, String> k31Column;

    private TableColumn<ShowStudent, String> k32Column;

    private TableColumn<ShowStudent, String> preferenceColumn;

    private TableColumn<ShowStudent, String> concernColumn;


    private Main app;

    public StudentPersonalController() {
    }


    public void setStudentList(List<Student> studentList) {
        showStudentList.clear();
        int i = 0;
        for (Student student : studentList) {
           showStudentList.add(new ShowStudent(i++, student));
        }
        studentsTableView.setItems(showStudentList);
    }

    public void setModel(Main app) {
        this.app = app;
    }

    @FXML
    void OpenStatisticsHandler(MouseEvent event) {
        app.showStatisticsPage();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showStudentList = FXCollections.observableArrayList();
        studentsTableView.setItems(showStudentList);

        rowIdxColumn = new TableColumn<>("Row_Index");
        rowIdxColumn.setMinWidth(75);
        rowIdxColumn.setCellValueFactory(new PropertyValueFactory<>("idx"));
        rowIdxColumn.setCellFactory(TextFieldTableCell.<ShowStudent>forTableColumn());


        idColumn = new TableColumn<>("Student_ID");
        idColumn.setMinWidth(90);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setCellFactory(TextFieldTableCell.<ShowStudent>forTableColumn());



        nameColumn = new TableColumn<>("Student_Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.<ShowStudent>forTableColumn());


        emailColumn = new TableColumn<>("Student_Email");
        emailColumn.setMinWidth(220);
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setCellFactory(TextFieldTableCell.<ShowStudent>forTableColumn());


        k1Column = new TableColumn<>("K1_Energy");
        k1Column.setMinWidth(75);
        k1Column.setCellValueFactory(new PropertyValueFactory<>("k1Energy"));
        k1Column.setCellFactory(TextFieldTableCell.<ShowStudent>forTableColumn());

        k2Column = new TableColumn<>("K2_Energy");
        k2Column.setMinWidth(75);
        k2Column.setCellValueFactory(new PropertyValueFactory<>("k2Energy"));
        k2Column.setCellFactory(TextFieldTableCell.<ShowStudent>forTableColumn());


        k31Column = new TableColumn<>("K3_Trick1");
        k31Column.setMinWidth(75);
        k31Column.setCellValueFactory(new PropertyValueFactory<>("k3Tick1"));
        k31Column.setCellFactory(TextFieldTableCell.<ShowStudent>forTableColumn());


        k32Column = new TableColumn<>("K3_Trick2");
        k32Column.setMinWidth(75);
        k32Column.setCellValueFactory(new PropertyValueFactory<>("k3Tick2"));
        k32Column.setCellFactory(TextFieldTableCell.<ShowStudent>forTableColumn());


        preferenceColumn = new TableColumn<>("My_Preference");
        preferenceColumn.setMinWidth(105);
        preferenceColumn.setCellValueFactory(new PropertyValueFactory<>("myPreference"));
        preferenceColumn.setCellFactory(TextFieldTableCell.<ShowStudent>forTableColumn());


        concernColumn = new TableColumn<>("Concerns");
        concernColumn.setMinWidth(160);
        concernColumn.setCellValueFactory(new PropertyValueFactory<>("concerns"));
        concernColumn.setCellFactory(TextFieldTableCell.<ShowStudent>forTableColumn());

        studentsTableView.getColumns().setAll(rowIdxColumn,idColumn,nameColumn,emailColumn,k1Column,k2Column,k31Column,k32Column,preferenceColumn,concernColumn);
    }
}
