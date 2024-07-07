import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentStatisticsController implements Initializable {


    @FXML
    private TableView<ShowStatistics> statisticsTableView;

    private ObservableList<ShowStatistics> showStatisticsList;

    private TableColumn<ShowStatistics, String> rowIdxColumn;

    private TableColumn<ShowStatistics, String> entryColumn;

    private TableColumn<ShowStatistics, String> valueColumn;

    public void setStudentList(List<Student> studentList) {
        int k1Total = 0;
        int k1Min = 200;
        int k1Max = 0;
        int k2Total = 0;
        int k2Min = 200;
        int k2Max = 0;
        int k31 = 0 ;
        int k32 = 0;
        int pre = 0;

        for (Student student:studentList) {
            if (student.isK3Tick1()) {
                k31++;
            }
            if (student.isK3Tick2()) {
                k32++;
            }
            if (student.isMyPreference()) {
                pre++;
            }

            if (student.getK1Energy() < k1Min) {
                k1Min = student.getK1Energy();
            }
            if (student.getK1Energy() > k1Max) {
                k1Max = student.getK1Energy();
            }
            k1Total += student.getK1Energy();

            if (student.getK2Energy() < k2Min) {
                k2Min = student.getK2Energy();
            }
            if (student.getK2Energy() > k2Max) {
                k2Max = student.getK2Energy();
            }
            k2Total += student.getK2Energy();
        }

        showStatisticsList.clear();
        showStatisticsList.add(new ShowStatistics("0", "Total Number of Students", "" + studentList.size()));
        showStatisticsList.add(new ShowStatistics("1", "K1_Energy(Average, Min, Max)", "(" + String.format("%.1f", k1Total *1.0/studentList.size()) + ", " + k1Min + ", " + k1Max + ")"));
        showStatisticsList.add(new ShowStatistics("2", "K2_Energy(Average, Min, Max)", "(" + String.format("%.1f", k2Total *1.0/studentList.size()) + ", " + k2Min + ", " + k2Max + ")"));
        showStatisticsList.add(new ShowStatistics("3", "K3_Tick1 = 1", "" + k31));
        showStatisticsList.add(new ShowStatistics("4", "K3_Tick2 = 1", "" + k32));
        showStatisticsList.add(new ShowStatistics("5", "My_Preference = 1", "" + pre));

        statisticsTableView.setItems(showStatisticsList);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showStatisticsList = FXCollections.observableArrayList();
        statisticsTableView.setItems(showStatisticsList);

        rowIdxColumn = new TableColumn<>("Row_Index");
        rowIdxColumn.setMinWidth(80);
        rowIdxColumn.setCellValueFactory(new PropertyValueFactory<>("idx"));
        rowIdxColumn.setCellFactory(TextFieldTableCell.<ShowStatistics>forTableColumn());


        entryColumn = new TableColumn<>("Entry");
        entryColumn.setMinWidth(210);
        entryColumn.setCellValueFactory(new PropertyValueFactory<>("entry"));
        entryColumn.setCellFactory(TextFieldTableCell.<ShowStatistics>forTableColumn());



        valueColumn = new TableColumn<>("Value");
        valueColumn.setMinWidth(120);
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueColumn.setCellFactory(TextFieldTableCell.<ShowStatistics>forTableColumn());

        statisticsTableView.getColumns().setAll(rowIdxColumn,entryColumn,valueColumn);
    }
}
