package ATU.UIcomponent;

import ATU.CourseProject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Customized inheriting JavaFx Stage for building the Menu UI for Review process.
 */
public class ReviewMenuStage extends Stage {

    // maintain a list of courseProject objects
    private static final ArrayList<CourseProject> courseProjectArr = new ArrayList<>();

    /**
     * Constructor for ReviewMenu, scan through the resources/atuOutput/ to get all record's name and path first <br>
     * Then do setting of UI
     * @throws IOException error when reading fxml design file
     */
    public ReviewMenuStage() throws IOException {
        // scan through the atuOutput directory and update CourseProjectArr
        scanRecord();

        // set up the UI from FXML
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui_design/reviewMenu_design.fxml"));
        VBox root = (VBox) loader.load();
        Scene scene = new Scene(root);
        setScene(scene);
        setTitle("Review previous ATU record");

    }

    /**
     * scan the atuOutput directory for all previous ATU record <br>
     * and create corresponding number of CourseProject to the list
     */
    private void scanRecord(){
        final String dirPath = "src/main/resources/atuOutput";
        File dir = new File(dirPath);
        Collection<File> allJsonFiles = FileUtils.listFiles(dir, new String[]{"json"}, false);      // get all .json files

        for (File file : allJsonFiles){
            courseProjectArr.add(new CourseProject(FilenameUtils.removeExtension(
                    file.getName()),
                    file.getAbsolutePath()
                    ));
        }
    }

    // getter of the list
    public static ArrayList<CourseProject> getCourseProjectArr() {
        return courseProjectArr;
    }

}
