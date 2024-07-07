package ATU.UIcomponent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Customized inheriting JavaFx Stage for building the Menu UI for Create process.
 */
public class CreateMenuStage extends Stage {

    // we have a default csv file
    private String filepath = "src/main/resources/userInput/StuPi.CSV";        // filepath of the .csv to read

    // default values
    private String courseName = "comp3111";                 // used for directory name

    private String projectName = "ATU";                // use for result json file name


    /**
     * Constructor of the CreateMenu, set all the UI design
     * @throws IOException error when reading fxml design file
     */
    public CreateMenuStage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui_design/createInput_design.fxml"));
        VBox root = (VBox) loader.load();
        Scene scene = new Scene(root);
        setScene(scene);
        setTitle("Create a new ATU record");
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}
