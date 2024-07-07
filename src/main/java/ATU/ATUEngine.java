package ATU;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;



import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Each ATUEngine is associated to one courseProject/request for teaming up, <br>
 * and produce one record for that courseProject <br>
 * The resultant ATU record is stored in teamList of this ATUEngine
 */
public class ATUEngine {

    private int total;          // total number of students
    private int totalTeam;      // total number of teams to be formed
    private final int teamSize = 3;       // default size of a team = 3

    // note that these three list are mutually exclusive
    private ArrayList<Student> k1_list;         // top totalTeam students sorted by k1_energy
    private ArrayList<Student> k2_list;         // bottom totalTeam students (not in k1_list) sorted by k2_energy
    private ArrayList<Student> k3_list;         // totalTeam students that are not in k1_list and not in k2_list
    private ArrayList<Student> remain_list;     // remaining students

    // the resultant array of Team after run() (i.e. process)
    private ArrayList<Team> teamList;           // this is what we want of the whole software


    /**
     * Constructor
     * @param total total number of students assumed to perform teaming
     */
    public ATUEngine(int total){
        this.total = total;
        this.totalTeam = total / teamSize;      // integer divsion
        this.teamList = new ArrayList<>(0);

        // initialize the three lists to be empty
        k1_list = new ArrayList<>(0);
        k2_list = new ArrayList<>(0);
        k3_list = new ArrayList<>(0);
        remain_list = new ArrayList<>(0);
    }


    /**
     * helper function to select top(totalTeam) student from StuPi order by K1_Energy in descending order
     * @param stuArr list of all Students
     */
    private void obtainK1_list(ArrayList<Student> stuArr){

        // sort the stuArr by k1_energy in descending order first
        stuArr.sort((o1, o2) -> {           // pass a lambda function as the comparator
            return Integer.compare(o2.getK1_energy(), o1.getK1_energy());       // o2 at first argument for descending order
        });

        for (int i=0; i < totalTeam; i++){
            k1_list.add(stuArr.get(i));
        }
    }

    /**
     * helper function to select top(totalTeam) student from StuPi order by K2_Energy in ascending order,
     * but given that they are not in k1_list
     * @param stuArr list of all Students
     */
    private void obtainK2_list(ArrayList<Student> stuArr){
        // sort the stuArr by k2_energy in ascending order first
        stuArr.sort((o1, o2) -> {           // pass a lambda function as the comparator
            return Integer.compare(o1.getK2_energy(), o2.getK2_energy());       // o2 at first argument for descending order
        });

        int count = 0;
        int i=0;
        while (i < stuArr.size() && count < totalTeam){
            if (k1_list.contains(stuArr.get(i)))        // if this Student exists in k1_list
                i++;
            else{
                k2_list.add(stuArr.get(i));
                i++;
                count++;
            }
        }

    }

    /**
     * helper function to select totalTeam remaining students (not in K1_list and not in K2_list)
     * @param stuArr list of all Students
     */
    private void obtainK3_list(ArrayList<Student> stuArr){
        int i=0;
        int count=0;
        while (i < stuArr.size() && count < totalTeam){
            // if this Student exists in k1_list or k2_list
            if (k1_list.contains(stuArr.get(i)) || k2_list.contains(stuArr.get(i))) {
            ;}       // skip this iteration
            else{
                k3_list.add(stuArr.get(i));
                count++;
            }
            i++;
        }
    }

    /**
     * helper function to select the remaining students (not in the three lists)
     * @param stuArr list of all Students
     */
    private void obtainRemain_list(ArrayList<Student> stuArr){
        int i=0;
        while (i < stuArr.size()){
            // if this Student exists in k1_list or k2_list
            if (k1_list.contains(stuArr.get(i)) || k2_list.contains(stuArr.get(i)) || k3_list.contains(stuArr.get(i))) {
                ;}       // skip this iteration
            else{
                remain_list.add(stuArr.get(i));
            }
            i++;
        }
    }


    /**
     * Forming teams for all students,
     * result of team formation is stored in Team array teamList. <br><br>
     * Student with highest k1_energy in the team is assigned as leader.
     * @param studentsArr the array of students obtained from StuPi.csv
     */
    public void run(ArrayList<Student> studentsArr){

        if (total < teamSize)          // cannot even form 1 team
            return;

        // initialize the teamList with size
        teamList = new ArrayList<Team>(totalTeam);
        for (int i=0; i < totalTeam; i++){
            teamList.add(new Team(i+1, "", teamSize));    // initialize the teamList with empty Team
        }

        // prepare the three lists first
        obtainK1_list(studentsArr);
        obtainK2_list(studentsArr);
        obtainK3_list(studentsArr);
        obtainRemain_list(studentsArr);

        /*for (Student stu : k1_list){
            System.out.println(stu);
        }
        System.out.println(" -------------------- ");
        for (Student stu : k2_list){
            System.out.println(stu);
        }
        System.out.println(" -------------------- ");
        for (Student stu : k3_list){
            System.out.println(stu);
        }
        System.out.println(" -------------------- ");
        for (Student stu : remain_list){
            System.out.println(stu);
        }*/

        for (int i=0; i < totalTeam; i++){
            teamList.get(i).setLeaderName(k1_list.get(i).getName());      // assume leader is the one with highest k1_energy
            teamList.get(i).setTeamMembers(new ArrayList<Student>(
                    Arrays.asList(k1_list.get(i), k2_list.get(i), k3_list.get(i))    // add the three students to the member array in Team
            ));
        }

        // handle the remaining students
        for (int i=0; i < remain_list.size(); i++){
            // add them to any team
            teamList.get(i).addTeamMember(remain_list.get(i));
        }

    }

    // getter of teamList
    public ArrayList<Team> getTeamList() {
        return teamList;
    }

    /**
     * print the team forming result, i.e. print all teams info
     */
    public void printResult(){
        for (Team team : teamList){
            System.out.println(team);
        }
    }

    /**
     * output the result of team formation i.e.teamList to a json format file <br>
     * each json file store an array of teams, where each team has info including team members
     * @param filePath the path of json output file
     * @return the string of json data, null if no team is formed
     */
    public String outputJson(String filePath){
        // array of team's JSONObject
        JSONArray teamArray = new JSONArray();
        String jsonText;

        for (Team team : teamList){
            JSONObject teamObj = new JSONObject();      // one team object
            teamObj.put("id", team.getId());
            teamObj.put("leaderName", team.getLeaderName());

            // array of student's JSONObject
            JSONArray studentArray = new JSONArray();
            for (Student student : team.getTeamMembers()) {
                JSONObject studentObj = new JSONObject();
                studentObj.put("id", student.getId());                      // student's id
                studentObj.put("name", student.getName());                  // student's name
                studentObj.put("email", student.getEmail());                // student's email
                studentObj.put("k1_energy", student.getK1_energy());        // student's k1 energy
                studentObj.put("k2_energy", student.getK2_energy());        // student's k2 energy

                // put the student JSONObject into student JSONArray
                studentArray.put(studentObj);
            }
            // put all the members, i.e. the JSONArray of student JSONObject
            teamObj.put("members", studentArray);

            teamArray.put(teamObj);
        }

        jsonText = teamArray.toString();
        // write to file
        try(FileWriter file = new FileWriter(filePath)){
            file.write(jsonText);
            file.flush();           // flush the buffer
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }

        if (!Objects.equals(jsonText, "[]"))        // if no team is formed, return null for caller to know
            return jsonText;            // return the json data in String
        else
            return null;
    }

    /**
     * Upload the json file into a RESTful Json api as a sever by http POST request
     * @param jsonText a String of our data (in the json file) to be uploaded
     * @param path file name of the json file (without .json extension) e.g. comp3111_ATU
     */
    private static void postUpload(String jsonText, String path) throws IOException {

        System.out.println("hi we are going to send data to sever");

        final String severURL = "https://jsonbin.org/me/";

        /*  !! Security concern
            In this project, our team choose to use a RESTful api called jsonbin.org for storing ATU record online as a database sever.
            A reasonable practice:
            Instructor as the user of this program can register to this api and replace the key below,
            with making little change on the html of web page (changing the identifier in URL).
            Below is my own api key of the RESTful api online which should be kept secret
            PLEASE DO NOT EXPLOIT !!
         */
        final String myAPIKEY = "token 0cefd463-20ae-45c2-b0ab-fbe3f574384e";

        URL url = new URL (severURL + path);                    // e.g. https://jsonbin.org/me/comp3111_ATU
        // POST request for sending data
        HttpURLConnection con_POST = null;
        try {
            con_POST = (HttpURLConnection) url.openConnection();
        } catch (IOException ioe){
            System.out.println("Cannot create HttpURLConnection");
            return;
        }
        con_POST.setRequestMethod("POST");
        // set all the request headerss
        con_POST.setRequestProperty("Content-Type", "application/json");
        //con_POST.setRequestProperty("Accept", "application/json");           // for reading response in json
        con_POST.setRequestProperty("authorization", myAPIKEY);
        con_POST.setDoOutput(true);
        // initialize a TCP connection
        try {
            con_POST.connect();
        } catch (IOException ioe){
            System.out.println("Cannot create establish TCP connection");
            return;
        }

        // our request body is the jsonText
        // create the request body
        try (OutputStream os = con_POST.getOutputStream()) {
            os.write(jsonText.getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();
        } catch (IOException ioe){
            System.out.println("Error sending data to the online API ");
        }
        // not doing anything for reading response here
        StringBuilder response = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con_POST.getInputStream()));
            String line = null;

            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            reader.close();
        } catch (IOException ioe){
            // do nothing - pretend to handle exception
        }


        // PUT request for setting the json data on api is public (for our web page to fetch)
        url = new URL(severURL + path + "/_perms");                // URL for setting public permission
        HttpURLConnection con_PUT = (HttpURLConnection)url.openConnection();
        con_PUT.setRequestMethod("PUT");
        con_PUT.setRequestProperty("authorization", myAPIKEY);
        int responseCode = con_PUT.getResponseCode();           // send the request and geting response status code
        // status 200 means OK
        if (responseCode != 200) {
            System.out.println("Oops something wrong when setting public permission on online API, response status = " + responseCode);
        }
        con_PUT.disconnect();
    }

    /**
     * Unit function for checking if the required json file still exists before loading <br>
     * User may accidentally delete the required file during program runtime
     * @param filePath path of the json record
     * @return true if file exists, otherwise false
     */
    public static boolean jsonStillExist(String filePath){
        File f = new File(filePath);
        if (f.exists() && !f.isDirectory())
            return true;
        return false;
    }

    /**
     * read previous ATU record from parsing a .json to this ATUEngine object <br>
     * i.e. to set the teamList result array
     * @param filePath the path of the json file to be parsed
     */
    public void readFromJson(String filePath){
        // Decode the JSON file to array of Team
        File file = new File(filePath);
        try {
            String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
            JSONArray teamObjArr = new JSONArray(content);

            teamList = new ArrayList<>(teamObjArr.length());

            for (int i=0; i< teamObjArr.length(); i++){
                teamList.add(i, new Team(teamObjArr.getJSONObject(i)));
            }

            // we need to sort the teamList by team id since JSON

        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
     * read previous ATU record from parsing a .json to this ATUEngine object <br>
     * i.e. to set the teamList result array
     * @param filePath the path of the json file to be parsed
     */
    public void readFromJson(String filePath){
        // Decode the JSON file to array of Team
        File file = new File(filePath);
        try {
            String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
            JSONArray teamObjArr = new JSONArray(content);

            teamList = new ArrayList<>(teamObjArr.length());

            for (int i=0; i< teamObjArr.length(); i++){
                teamList.add(i, new Team(teamObjArr.getJSONObject(i)));
            }

            // we need to sort the teamList by team id since JSON

        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
     * show the result as javaFX TableView in the new stage
     *
     * @param parentStage parent stage of this upcoming stage showing ATU result
     * @param stage       the javaFX stage for showing the table onto
     * @param courseName  name of course that the project belongs to
     * @param projectName name of project this ATU perform on
     * @param jsonText    string of json data to be uploaded online, null is passed for Review process
     */
    public void showResultTable(Stage parentStage, Stage stage, String courseName, String projectName, String jsonText) {
        // settings of stage
        stage.setTitle("ATU Engine result table ( " + courseName + " " + projectName + " )");
        stage.setWidth(720);
        stage.setHeight(512);
        final Label tableLabel = new Label("ATU result of " + courseName + " " + projectName);           // a textLabel
        tableLabel.setFont(new Font("Arial", 20));

        // create TableView
        TableView<ResultTeam> table = new TableView<>();
        // create columns
        TableColumn<ResultTeam, Integer> teamId_column = new TableColumn<>("Team_ID");
        teamId_column.setMinWidth(50);
        teamId_column.setCellValueFactory(new PropertyValueFactory<>("team_id"));
        TableColumn<ResultTeam, String> studentColumn = new TableColumn<>("Student");
        studentColumn.setMinWidth(70 + 165 + 53 + 50 + 50);

        TableColumn<ResultTeam, String> stuID_column = new TableColumn<>("SID");
        stuID_column.setMinWidth(70);
        stuID_column.setCellValueFactory(new PropertyValueFactory<>("student_id"));
        TableColumn<ResultTeam, String> stuName_column = new TableColumn<>("Name");
        stuName_column.setMinWidth(165);
        stuName_column.setCellValueFactory(new PropertyValueFactory<>("student_name"));
        TableColumn<ResultTeam, String> leader_column = new TableColumn<>("is_Leader?");
        leader_column.setMinWidth(53);
        leader_column.setCellValueFactory(new PropertyValueFactory<>("is_leader"));
        TableColumn<ResultTeam, Integer> k1_column = new TableColumn<>("K1_energy");
        k1_column.setMinWidth(50);
        k1_column.setCellValueFactory(new PropertyValueFactory<>("k1_energy"));
        TableColumn<ResultTeam, Integer> k2_column = new TableColumn<>("K2_energy");
        k2_column.setMinWidth(50);
        k2_column.setCellValueFactory(new PropertyValueFactory<>("k2_energy"));

        // nested columns in Student column
        studentColumn.getColumns().addAll(stuID_column, stuName_column, leader_column, k1_column, k2_column);

        // construct observableList<ResultTeam>
        ObservableList<ResultTeam> resultData = FXCollections.observableArrayList();
        for (Team team : teamList) {
            // construct an array and initialization
            ResultTeam[] recordArr = new ResultTeam[team.getSize()];         // array of record coz each team has 3 to 4 member records
            for (int i = 0; i < team.getSize(); i++)
                recordArr[i] = new ResultTeam(team.getId());

            for (int i = 0; i < team.getSize(); i++) {
                recordArr[i].setStudent_id(team.getTeamMembers().get(i).getId());
                recordArr[i].setStudent_name(team.getTeamMembers().get(i).getName());
                recordArr[i].setIs_leader(team.getLeaderName().equals(team.getTeamMembers().get(i).getName()) ? "Yes" : "No");
                recordArr[i].setK1_energy(team.getTeamMembers().get(i).getK1_energy());
                recordArr[i].setK2_energy(team.getTeamMembers().get(i).getK2_energy());
            }
            resultData.addAll(recordArr);
        }
        // adding columns and data
        table.getColumns().addAll(teamId_column, studentColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setItems(resultData);

        // create Vbox and scene object
        final VBox vbox = new VBox();
        Scene resultScene = new Scene(new Group());
        vbox.setSpacing(22);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(tableLabel, table);

        if (jsonText != null) {          // if in Create process, make a button for uploading data to sever
            // create a button for uploading data to online API
            Button uploadButton = new Button();
            uploadButton.setText("Upload to sever");
            uploadButton.setFont(new Font("Arial", 14));
            uploadButton.setLayoutX(410);
            uploadButton.setLayoutY(0);
            uploadButton.setPrefHeight(48);
            uploadButton.setPrefWidth(125);

            // add eventHandler for the button
            uploadButton.addEventHandler(ActionEvent.ACTION, actionEvent -> {
                try {
                    postUpload(jsonText, courseName + "_" + projectName);
                } catch (IOException e) {
                    System.out.println("Fail to upload to sever");
                }
                uploadButton.setDisable(true);        // only allow pressing once
            });

            // also add the uploadButton the stage
            ((Group) resultScene.getRoot()).getChildren().addAll(vbox, uploadButton);
        } else {
            ((Group) resultScene.getRoot()).getChildren().addAll(vbox);
        }

        //((Group) resultScene.getRoot()).getChildren().addAll(vbox);

        stage.setX(200);
        stage.setY(200);
        stage.setScene(resultScene);
        stage.initOwner(parentStage);       // set the parent stage
        stage.show();                       // display the stage

    }

    /**
     * class dedicated for showResultTable()
     */
    public static class ResultTeam {

        private Integer team_id;

        private String student_id;
        private String student_name;

        private String is_leader;

        private Integer k1_energy;
        private Integer k2_energy;

        public ResultTeam(Integer team_id, String student_id, String student_name, String is_leader) {
            this.team_id = team_id;
            this.student_id = student_id;
            this.student_name = student_name;
            this.is_leader = is_leader;
        }

        public ResultTeam(Integer team_id) {
            this.team_id = team_id;
        }

        public Integer getTeam_id() {
            return team_id;
        }

        public String getStudent_id() {
            return student_id;
        }

        public String getStudent_name() {
            return student_name;
        }

        public String getIs_leader() {
            return is_leader;
        }

        public Integer getK1_energy() {
            return k1_energy;
        }

        public Integer getK2_energy() {
            return k2_energy;
        }

        public void setStudent_id(String student_id) {
            this.student_id = student_id;
        }

        public void setStudent_name(String student_name) {
            this.student_name = student_name;
        }

        public void setIs_leader(String is_leader) {
            this.is_leader = is_leader;
        }

        public void setK1_energy(Integer k1_energy) {
            this.k1_energy = k1_energy;
        }

        public void setK2_energy(Integer k2_energy) {
            this.k2_energy = k2_energy;
        }

    }

}
