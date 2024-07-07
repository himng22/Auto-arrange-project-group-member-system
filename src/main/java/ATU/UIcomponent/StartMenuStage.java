package ATU.UIcomponent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Customized inheriting JavaFx Stage for building the Menu UI for Start Menu.
 */
public class StartMenuStage extends Stage {

    /**
     * Constructor of StartMenu, set all the UI
     * @throws IOException error when reading fxml design file
     */
    public StartMenuStage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui_design/startMenu_design.fxml"));
        VBox root = (VBox) loader.load();
        Scene scene = new Scene(root);
        setScene(scene);
        setTitle("Start Menu");
        setAlwaysOnTop(true);

    }

    // adding new attributes
    /**
     * mode is the activity chosen by user, <br>
     * mode = 0 means review previous ATU result <br>
     * mode = 1 means create a new ATU record <br>
     * mode = -1 means the user want to quit the app, i.e. no longer want to continue
     */
    private int mode = -1;       // default is equal to -1

    /**
     * show the stage until it is close (this is a synchronized event)
     * @return mode decided by the user
     */
    public int showAndReturn() {
        setMode(-1);                // reset the mode to -1
        super.showAndWait();        // call the showAndWait() of Stage
                                    // block the whole process until the stage is closed
        return mode;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

}
