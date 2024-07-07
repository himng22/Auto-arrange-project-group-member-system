package ATU.controller;

import ATU.UIcomponent.CreateMenuStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;


/**
 * Controller and event listener of CreateMenu
 */
public class CreateMenuController {

    @FXML
    private TextField courseNameField;

    @FXML
    private Button createSubmitButton;

    @FXML
    private TextField projectNameField;

    @FXML
    private Label selectFileLabel;

    @FXML
    private Label warningLabel;


    @FXML
    void createSubmitButtonPressed(ActionEvent event) {

        // validate the input?

        // set the courseName and projectName as the text typed in
        // kind of like "return the filename back to main"
        Node root = (Node) event.getSource();
        CreateMenuStage stage = (CreateMenuStage) root.getScene().getWindow();        // reference the stage

        if (!courseNameField.getText().trim().equals(""))          // if the field is not empty
            stage.setCourseName(courseNameField.getText());
        if (!projectNameField.getText().trim().equals(""))
            stage.setProjectName(projectNameField.getText());

        stage.close();
    }

    @FXML
    void selectFileLabelPressed(MouseEvent event)  {
        FileChooser fc = new FileChooser();

        fc.setInitialDirectory(new File("src/main/resources/userInput"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("csv files", "*.csv"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("csv files", "*.CSV"));


        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            selectFileLabel.setText(selectedFile.getName());

            // kind of like "return the filename back to main"
            Node root = (Node) event.getSource();
            CreateMenuStage stage = (CreateMenuStage) root.getScene().getWindow();        // reference the stage
            stage.setFilepath(selectedFile.getAbsolutePath());
        }
        else
            System.out.println("Wrong file selected");
    }


}
