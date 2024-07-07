package ATU;


import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Each CourseProject object (e.g. comp3511_pa2) will have an associated ATUEngine to run or show output <br>
 * Act as a proxy to create/load each ATU record
 */
public class CourseProject {

    private String courseName;      // Not used for Review process
    private String projectName;     // Not used for Review process

    // the fileName of this courseProject's ATU record (without file extension)
    private String fileName;        // Not used for Create process
    private String filePath;        // Not used for Create process
    private Boolean loadedAlready;  // Not used for Create process


    private final ATUEngine atu;

    @Override
    public String toString() {
        return fileName;
    }

    /**
     * constructor for Create process
     * @param courseName name of the course
     * @param projectName name of the project
     * @param atu ATUEngine associated for performing teaming later
     */
    public CourseProject(String courseName, String projectName, ATUEngine atu) {
        this.courseName = courseName;
        this.projectName = projectName;
        this.fileName = "";         // initialize to empty
        this.filePath = "";
        this.atu = atu;
    }

    /**
     * constructor for Review process
     * @param fileName name combining the course and project e.g. comp3111_ATU
     * @param filePath path of the json record file for loading later
     */
    public CourseProject(String fileName, String filePath) {         // used for Review process
        this.fileName = fileName;
        this.filePath = filePath;
        this.loadedAlready = false;

        // initialize to empty
        this.courseName = "";
        this.projectName = "";

        // atu bounded
        this.atu = new ATUEngine(0);        // Note: ATU with total=0 is not allowed to run() on purpose
    }

    public String getCourseName() {
        return courseName;
    }

    public String getProjectName() {
        return projectName;
    }

    public Boolean getLoadedAlready() {
        return loadedAlready;
    }



    /**
     * Run the ATUEngine of this courseProject with provided Student array <br>
     * Show the result in table and output a json file <br>
     * Used in <i>Create</i> process
     * @param stuArr      array of all Students involved in this courseProject
     * @param outputPath  the path of json output file
     * @param parentStage parent stage of the stage running ATU
     */
    public void runATU(ArrayList<Student> stuArr, String outputPath, Stage parentStage){
        atu.run(stuArr);
        String jsonText = atu.outputJson(outputPath);
        atu.showResultTable(parentStage, new Stage(), courseName, projectName, jsonText);
    }

    /**
     * Load the ATU record from the JSON and show the result in tableView <br>
     * Used in <i>Review</i> process

     * @param parentStage parent stage of the stage showing ATU record
     */
    public void loadAndShow(Stage parentStage) {

        // check if the ATUEngine loaded the json file already,
        // to prevent re-loading same content
        // if the required json file does not exist right before loading, not allowed to load
        if (!ATUEngine.jsonStillExist(filePath) && !loadedAlready)
        {
            System.out.println("Cannot load this record due to missing file.");
            return;
        }
        else if (!loadedAlready) {
            System.out.println("loading... " + fileName + " " + filePath);
            atu.readFromJson(filePath);
            loadedAlready = true;
        }

        atu.showResultTable(parentStage, new Stage(), fileName, "", null);
    }

}
