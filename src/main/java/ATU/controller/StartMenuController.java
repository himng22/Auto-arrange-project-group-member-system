package ATU.controller;

import ATU.UIcomponent.StartMenuStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


/**
 * Controller and event listener of StartMenu
 */
public class StartMenuController {

    @FXML
    private Label introLabel;

    @FXML
    private Button menuButton_Create;

    @FXML
    private Button menuButton_review;

    @FXML
    void createButtonPressed(ActionEvent event) {

        // get the stage from event
        Node  source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();

        // set the mode bit to 1
        ((StartMenuStage) stage).setMode(1);

        stage.close();          // close this startMenu

    }

    @FXML
    void reviewButtonPressed(ActionEvent event) {

        // get the stage from event
        Node  source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();

        // set the mode bit to 0
        ((StartMenuStage) stage).setMode(0);
        stage.close();          // close this startMenu

    }

}
