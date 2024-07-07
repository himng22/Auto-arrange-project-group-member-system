package ATU.controller;

import ATU.CourseProject;
import ATU.UIcomponent.ReviewMenuStage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller and event listener of ReviewMenu
 */
public class ReviewMenuController implements Initializable {

    @FXML
    private Label numLabel;

    @FXML
    private ListView<CourseProject> recordList;

    private static final ObservableList<CourseProject> projectList = FXCollections.observableArrayList();

    /**
     * when user double-click on any cell in the list, pop the corresponding ATU record table
     * @param event the MouseEvent triggered
     */
    @FXML
    void selectRecord(MouseEvent event) {

        // check for double click
        if (event.getButton().equals(MouseButton.PRIMARY) &&
            event.getClickCount() == 2)
        {

            // current selection on the GUI listView
            CourseProject selectedProject = recordList.getSelectionModel().getSelectedItem();

            // show the ATU result record of that courseProject
            // reference the stage
            Node source = (Node)  event.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();

            selectedProject.loadAndShow(stage);     // pass the ReviewStage as parent stage

        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // add all CourseProject object into the observablesList
        projectList.addAll(ReviewMenuStage.getCourseProjectArr());
        recordList.setItems(projectList);           // show the courseProject on listView

        numLabel.setText(String.valueOf(projectList.size()));       // update the numLabel
    }
}
