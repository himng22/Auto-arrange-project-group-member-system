package ATU;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.concurrent.atomic.AtomicReference;

import ATU.UIcomponent.CreateMenuStage;
import ATU.UIcomponent.ReviewMenuStage;
import ATU.UIcomponent.StartMenuStage;
import com.opencsv.CSVParser;
import javafx.application.Application;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import com.opencsv.CSVReader;

/**
 * The JavaFx application that provide UI for user to perform automatic teaming up and reviewing record.
 */
public class Library extends Application {

	private final static ObservableList<Person> person_data = FXCollections.observableArrayList();

	//Task 2: Define a ObservableList for statistics data
	private final static ObservableList<Statistics> stats_data = FXCollections.observableArrayList();

	// the array for generating statistics and run new ATURecord(PROCESS) part
	private static ArrayList<Student> studentArr = new ArrayList<Student>();		// note: ArrayList<> is just like vector<> in C++


	public static ArrayList<Student> getStudentArr() {
		return studentArr;
	}

	/**
	 * 	read the csv file (default: StuPi.CSV) into
	 * 	array of Person(for showing table gui) and array of Student(for ATUEngine Process)
	 * @param csvFile filePath of the target csv, default is src/main/resources/userInput/StuPi.CSV
	 */
	public static void read(String csvFile) {

		System.out.print("\n");
		try {

			FileReader fileReader = new FileReader(csvFile);
			// create a csvReader that will skip the first line (column label)
			CSVReader csvReader = new CSVReader(fileReader, CSVParser.DEFAULT_SEPARATOR,
												CSVParser.DEFAULT_QUOTE_CHARACTER, 1);

			String[] tempArr;
			int count = 0;			// count the number of records/lines, used for row_index of the tableView
			while ((tempArr = csvReader.readNext()) != null) {

				// each line is a student record
				Student tempStu = new Student(tempArr[0], tempArr[1], tempArr[2], tempArr[3], tempArr[4], tempArr[5],
						tempArr[6], tempArr[7], tempArr[8]);
				studentArr.add(tempStu);			// append the student entry into student array

				Person newPerson = new Person(count++,					// count as row_index
						tempStu);

				person_data.add(newPerson);
			}
			// close the file
			csvReader.close();
			fileReader.close();
		} catch (Exception e) {
			System.out.println("Illegal format in the csv !! Please check your csv input file. ");
			e.printStackTrace();
			Platform.exit();		// end the application
		}
	}

	// method invoked by the Main
	public static void run(String[] args) {

		// "src/main/resources/StuPi.CSV" or "build/resources/main/StuPi.CSV" (gradle built folder)

		System.out.println("Hello");
		launch(args);			// launch the Application -> call start()

	}

	/**
	 * 	entry point of the javaFX application - start the stages
	 */
	@Override
	public void start(Stage stage) throws Exception {		// stage is the primaryStage of javaFX application

		// construct the startMenu
		Stage startMenu = new StartMenuStage();
		// this will block the process and wait until startMenu is closed
		int userChosen = ((StartMenuStage) startMenu).showAndReturn();
											// note that showAndWait() is not allowed for primary stage

		ReviewMenuStage reviewStage = new ReviewMenuStage();

		while (userChosen != -1) {		// userChosen == -1 means user no longer want to continue

			// create two new stages(windows) and show Person table and statistics table on them,
			// and run the ATU
			if (userChosen == 1) {
				newATURecord();
				break;				// not allowed to go back to menu for Create activity
			}else{    // if user choose to review previous records i.e. userChose == 0
				reviewATURecord(reviewStage);
				userChosen = ((StartMenuStage) startMenu).showAndReturn();		// back to menu
			}
		}

	}

	/**
	 * This process will run if user choose to review previous ATU record <br>
	 * Show the list of all ATU record found on this local program.
	 * @param reviewStage stage for showing list of ATU record
	 */
	public void reviewATURecord(ReviewMenuStage reviewStage)  {
		reviewStage.showAndWait();			// block the process
	}

	/**
	 * This process will run if user choose to create a new ATU record <br>
	 * Handle the gui to show the Person table and Statistics table, <br>
	 * Then do the running of ATU engine and show teaming result table
	 * @throws Exception Any possible Exception during the Create process
	 */
	public void newATURecord() throws Exception {

		CreateMenuStage createStage = new CreateMenuStage();
		createStage.showAndWait();

		// get the fileName from the closed stage
		read(createStage.getFilepath());		// read the csv file

		String atuOutputPath = makeOutputFilePath(createStage);		// the absolute path of result json output after running ATU
		CourseProject newProject = new CourseProject(createStage.getCourseName(),
				createStage.getProjectName(),
				new ATUEngine(studentArr.size())
		);

		showTwoTable(newProject, atuOutputPath);			// show the two required tables, and run ATU when user press button

	}

	/**
	 * construct the fileName and filePath of the json output of ATU engine, <br>
	 * including the .json extension
	 * @param createStage the stage of createMenu
	 * @return an absolute filePath
	 */
	private String makeOutputFilePath(CreateMenuStage createStage){

		// check for already exist file, prepare the new filename e.g. old_1, old_2
		// the supposed json output file name is "courseName_projectName.json" e.g. comp3111_ATU.json
		final String filePath = "src/main/resources/atuOutput/";
		String fileName = createStage.getCourseName() + "_" + createStage.getProjectName();
		File file = new File(filePath + fileName + ".json");

		if (file.exists()){
			int version = 1;
			while (file.exists()){
				version++;
				file = new File(filePath + fileName + String.valueOf(version)  + ".json");        // add the extension for testing now
			}
		}
		return file.getAbsolutePath();
	}


	/**
	 * Wrap the code of creating two stages and showing two tables into a method <br>
	 * Person table and Statistics table <br>
	 * This function will block the process until all stages are closed
	 * @param newProject the project to be performed ATU on
	 * @param atuOutputFilePath an absolute path of output file of ATU
	 */
	private void showTwoTable(CourseProject newProject,String atuOutputFilePath) {

		// Two TableView (i.e. table) for showing student info and statistics
		TableView<Person> person_table = new TableView<Person>();

		//Task 1: Define a TableView for statistics data
		TableView<Statistics> stats_table = new TableView<Statistics>();

		/* table UI */
		Stage stage_person = new Stage();
		Scene scene_person = new Scene(new Group());
		stage_person.setTitle("Table of students (" + newProject.getCourseName() + " " + newProject.getProjectName() + ")");
		stage_person.setWidth(1050);
		stage_person.setHeight(512);

		final Label label_person = new Label("People in " + newProject.getCourseName() + " " + newProject.getProjectName());
		label_person.setFont(new Font("Arial", 26));

		person_table.setEditable(true);

		// Create all rows
		TableColumn<Person, Integer> person_index_column = new TableColumn<Person, Integer>("Row_Index");
		person_index_column.setMinWidth(55);
		person_index_column.setCellValueFactory(new PropertyValueFactory<Person, Integer>("rowindex"));

		TableColumn<Person, String> studentid_column = new TableColumn<Person, String>("Student_ID");
		studentid_column.setMinWidth(100);
		studentid_column.setCellValueFactory(new PropertyValueFactory<Person, String>("studentid"));		// the factory will use getStudentid() in Person to set the cell

		TableColumn<Person, String> studentname_column = new TableColumn<Person, String>("Student_Name");
		studentname_column.setMinWidth(175);				// <-- width of student name is set larger because name is long
		studentname_column.setCellValueFactory(new PropertyValueFactory<Person, String>("studentname"));

		TableColumn<Person, String> k1energy_column = new TableColumn<Person, String>("K1_Energy");
		k1energy_column.setMinWidth(50);
		k1energy_column.setCellValueFactory(new PropertyValueFactory<Person, String>("k1energy"));

		TableColumn<Person, String> k2energy_column = new TableColumn<Person, String>("k2_Energy");
		k2energy_column.setMinWidth(50);
		k2energy_column.setCellValueFactory(new PropertyValueFactory<Person, String>("k2energy"));

		TableColumn<Person, String> k3trick1_column = new TableColumn<Person, String>("K3_Trick1");
		k3trick1_column.setMinWidth(50);
		k3trick1_column.setCellValueFactory(new PropertyValueFactory<Person, String>("k3trick1"));

		TableColumn<Person, String> k3trick2_column = new TableColumn<Person, String>("K3_Trick2");
		k3trick2_column.setMinWidth(50);
		k3trick2_column.setCellValueFactory(new PropertyValueFactory<Person, String>("k3trick2"));

		TableColumn<Person, String> mypreference_column = new TableColumn<Person, String>("My_Preference");
		mypreference_column.setMinWidth(50);
		mypreference_column.setCellValueFactory(new PropertyValueFactory<Person, String>("mypreference"));

		TableColumn<Person, String> concerns_column = new TableColumn<Person, String>("Concerns");
		concerns_column.setMinWidth(175);
		concerns_column.setCellValueFactory(new PropertyValueFactory<Person, String>("concerns"));

		// add all the created columns to the tableView person_table
		person_table.getColumns().addAll(person_index_column, studentid_column, studentname_column, k1energy_column, k2energy_column,
				k3trick1_column, k3trick2_column, mypreference_column, concerns_column);
		person_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		// set items of this tableView to ObservableList: person_data
		person_table.setItems(person_data);
		final VBox vbox_person = new VBox();
		vbox_person.setSpacing(15);
		vbox_person.setPadding(new Insets(10, 0, 0, 10));
		vbox_person.getChildren().addAll(label_person, person_table);			// add the person tableView and the textLabel into the VBox

		// create a button to this scene for running ATU
		Button runButton = new Button();
		runButton.setText("Run ATU");
		runButton.setFont(new Font("Arial", 15));
		runButton.setLayoutX(500);
		runButton.setLayoutY(3);
		runButton.setPrefHeight(45);
		runButton.setPrefWidth(98);
		final boolean[] atuExecuted = {false};
		// pressHandler for runButton
		runButton.addEventHandler(ActionEvent.ACTION, actionEvent -> {
			if (!atuExecuted[0]){
				newProject.runATU(studentArr, atuOutputFilePath, stage_person);		// run the ATU bound with the courseProject object
				atuExecuted[0] = true;
				runButton.setDisable(true);		// only allow pressing once
			}
		}
		);

		// add the VBox and button
		((Group) scene_person.getRoot()).getChildren().addAll(vbox_person, runButton);

		stage_person.setScene(scene_person);			// set the scene to the stage


		//Task3: Define a stage and scene for statistics data, similar to the above code snippet
		Stage stage_stats = new Stage();
		Scene scene_stats = new Scene(new Group());
		stage_stats.setTitle("Table of statistics (" + newProject.getCourseName() + " " + newProject.getProjectName() + ")");
		stage_stats.setWidth(512);
		stage_stats.setHeight(512);

		final Label label_stats = new Label("Statistics");
		label_stats.setFont(new Font("Arial", 26));			// a textLabel

		// set Editable (probably need?

		// create columns
		TableColumn<Statistics, Integer> stats_index_column = new TableColumn<Statistics, Integer>("Row_Index");
		stats_index_column.setMinWidth(37);
		stats_index_column.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("rowindex"));

		TableColumn<Statistics, Integer> entry_column = new TableColumn<Statistics, Integer>("Entry");
		entry_column.setMinWidth(180);
		entry_column.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("entry"));

		TableColumn<Statistics, Integer> value_column = new TableColumn<Statistics, Integer>("Value");
		value_column.setMinWidth(120);
		value_column.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("value"));

		// add all the created columns to the tableView stats_table
		stats_table.getColumns().addAll(stats_index_column, entry_column, value_column);
		// set column resize
		stats_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// generate all the statistics report before displaying on table
		Statistics[] allStats = calculateStats(studentArr);
		stats_data.addAll(Arrays.asList(allStats));			// add the statistics array into ObservableList:stats_data

		// set items of this tableView to ObservableList: stats_data
		stats_table.setItems(stats_data);
		final VBox vbox_stats = new VBox();
		vbox_stats.setSpacing(5);
		vbox_stats.setPadding(new Insets(10, 0, 0, 10));
		vbox_stats.getChildren().addAll(label_stats, stats_table);

		((Group) scene_stats.getRoot()).getChildren().addAll(vbox_stats);		// add the VBox to the scene

		stage_stats.setScene(scene_stats);			// set the scene to the stage
		stage_stats.show();							// show the stage
		stage_person.show();					// show the stage and block the process

	}

	/**
	 * generate the statistics report of all student information
	 * @param studentList the array of students obtained from StuPi.csv
	 * @return a Statistics array that contain some statistics of StuPi.csv, e.g. totalCount, k1_summary, k3_count...
	 */
	public static Statistics[] calculateStats(ArrayList<Student> studentList){

		// declare the array os Statistics
		Statistics[] statsArr = new Statistics[6];		// we do 6 statistics

		// prepare some initial variables
		int k3_tick1_count = 0, k3_tick2_count = 0;
		int mypreference_count = 0;

		for (Student stu : studentList){
			if (stu.isK3_tick1()) k3_tick1_count++;
			if (stu.isK3_tick2()) k3_tick2_count++;
			if (stu.isMy_preference()) mypreference_count++;
		}
		float k1_avg = Student.calEnergy_avg(studentList, 1);
		float k2_avg = Student.calEnergy_avg(studentList, 2);
		int k1_min = Student.calEnergy_min(studentList, 1);
		int k2_min = Student.calEnergy_min(studentList, 2);
		int k1_max = Student.calEnergy_max(studentList, 1);
		int k2_max = Student.calEnergy_max(studentList, 2);

		// 1. Total Count
		statsArr[0] = new Statistics(0, "Total number of Students", String.valueOf(studentList.size()));

		// 2. K1_Energy(Average, Min, Max)
		statsArr[1] = new Statistics(1, "K1_Energy(Average, Min, Max)",
				"(" + String.valueOf(String.format("%.02f", k1_avg)) + ", " + String.valueOf(k1_min) + ", " + String.valueOf(k1_max) + ")");

		// 3. K2_Energy(Average, Min, Max)
		statsArr[2] = new Statistics(2, "K2_Energy(Average, Min, Max)",
				"(" + String.valueOf(String.format("%.02f",k2_avg)) + ", " + String.valueOf(k2_min) + ", " + String.valueOf(k2_max) + ")");

		// 4. K3_Tick1 = 1, count of K3_tick1
		statsArr[3] = new Statistics(3, "K3_Tick1 = 1", String.valueOf(k3_tick1_count));

		// 5. K3_Tick2 = 1, count of k3_tick2
		statsArr[4] = new Statistics(4, "K3_Tick2 = 1", String.valueOf(k3_tick2_count));

		// 6. My_Preference = 1
		statsArr[5] = new Statistics(5, "My_Preference = 1", String.valueOf(mypreference_count));

		return statsArr;
	}

	/**
	 * Person class for displaying table of students in the GUI
	 */
	public static class Person {

		// Notes: there is no "email" attribute in Person class !!!
		private final SimpleIntegerProperty rowindex;		// for displaying Row_Index in the tableView
		private final SimpleStringProperty studentid;
		private final SimpleStringProperty studentname;
		private final SimpleStringProperty k1energy;
		private final SimpleStringProperty k2energy;
		private final SimpleStringProperty k3trick1;
		private final SimpleStringProperty k3trick2;
		private final SimpleStringProperty mypreference;
		private final SimpleStringProperty concerns;

		private Person(int row_index, String student_id, String student_name, String k1_energy, String k2_energy, String k3_trick1,
				String k3_trick2, String my_preference, String concerns) {
			this.rowindex = new SimpleIntegerProperty(row_index);
			this.studentid = new SimpleStringProperty(student_id);
			this.studentname = new SimpleStringProperty(student_name);
			this.k1energy = new SimpleStringProperty(k1_energy);
			this.k2energy = new SimpleStringProperty(k2_energy);
			this.k3trick1 = new SimpleStringProperty(k3_trick1);
			this.k3trick2 = new SimpleStringProperty(k3_trick2);
			this.mypreference = new SimpleStringProperty(my_preference);
			this.concerns = new SimpleStringProperty(concerns);
		}

		/**
		 * conversion constructor from class-type Student to Person
		 * @param student a Student class type object
		 */
		private Person(int row_index, Student student)
		{
			this.rowindex = new SimpleIntegerProperty(row_index);
			this.studentid = new SimpleStringProperty(student.getId());
			this.studentname = new SimpleStringProperty(student.getName());
			this.k1energy = new SimpleStringProperty(Integer.toString(student.getK1_energy()));
			this.k2energy = new SimpleStringProperty(Integer.toString(student.getK2_energy()));
			this.k3trick1 = new SimpleStringProperty(Student.toNumeralBool(student.isK3_tick1()));
			this.k3trick2 = new SimpleStringProperty(Student.toNumeralBool(student.isK3_tick2()));
			this.mypreference = new SimpleStringProperty(Student.toNumeralBool(student.isMy_preference()));
			this.concerns = new SimpleStringProperty(student.getConcerns());
		}

		public Integer getRowindex(){
			return rowindex.get();
		}
		public String getStudentid() {
			return studentid.get();
		}

		public void setStudentid(String val) {
			studentid.set(val);
		}

		public String getStudentname() {
			return studentname.get();
		}

		public void setStudentname(String val) {
			studentname.set(val);
		}

		public String getK1energy() {
			return k1energy.get();
		}

		public void setK1energy(String val) {
			k1energy.set(val);
		}

		public String getK2energy() {
			return k2energy.get();
		}

		public void setK2energy(String val) {
			k2energy.set(val);
		}

		public String getK3trick1() {
			return k3trick1.get();
		}

		public void setK3trick1(String val) {
			k3trick1.set(val);
		}

		public String getK3trick2() {
			return k3trick2.get();
		}

		public void setK3trick2(String val) {
			k3trick2.set(val);
		}

		public String getMypreference() {
			return mypreference.get();
		}

		public void setMypreference(String val) {
			mypreference.set(val);
		}

		public String getConcerns() {
			return concerns.get();
		}

		public void setConcerns(String val) {
			concerns.set(val);
		}
	}
	
	//Task 3: Define a class for statistics data, similar to the class "Person"
	/**
	 * Statistics class for displaying Statistics table in the GUI
	 */
	public static class Statistics {

		// attributes
		private final SimpleIntegerProperty rowindex;
		private final SimpleStringProperty entry;
		private final SimpleStringProperty value;

		// constructor
		public Statistics(int rowindex, String entry, String value) {
			this.rowindex = new SimpleIntegerProperty(rowindex);
			this.entry = new SimpleStringProperty(entry);
			this.value = new SimpleStringProperty(value);
		}

		// getter accessor method for the PropertyValueFactory
		public int getRowindex() {
			return rowindex.get();
		}

		public String getEntry() {
			return entry.get();
		}

		public String getValue() {
			return value.get();
		}

	}


}
