package ATU;

import java.io.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Library_skeleton extends Application {
	
	private TableView<Person> person_table = new TableView<Person>();
	//Task 1: Define a TableView for statistics data
	
	
	private final static ObservableList<Person> person_data = FXCollections.observableArrayList();
	//Task 2: Define a ObservableList for statistics data

	public static final String delimiter = ",";
	
	//read csv file
	public static void read(String csvFile) {

		System.out.print("\n");
		try {
			File file = new File(csvFile);
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String line = " ";
			String[] tempArr;
			br.readLine(); // skip the first line
			while ((line = br.readLine()) != null) {
				tempArr = line.split(delimiter);
				person_data.add(new Person(tempArr[0], tempArr[1], tempArr[2], tempArr[3], tempArr[4], tempArr[5],
						tempArr[6], tempArr[7]));
			}
			br.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void run(String[] args) throws Exception {

		String csvFile = "src/main/resources/StuPi.CSV";
		Library_skeleton.read(csvFile);
		System.out.println("Hello");
		launch(args);

	}

	@Override
	public void start(Stage stage_stat) {

		Stage stage_person = new Stage();
		Scene scene_person = new Scene(new Group());
		stage_person.setTitle("Table of statistics data");
		stage_person.setWidth(450);
		stage_person.setHeight(500);

		final Label label_person = new Label("Person");
		label_person.setFont(new Font("Arial", 20));

		person_table.setEditable(true);

		TableColumn studentid_column = new TableColumn("Student_ID");
		studentid_column.setMinWidth(100);
		studentid_column.setCellValueFactory(new PropertyValueFactory<Statistics, String>("studentid"));

		TableColumn studentname_column = new TableColumn("Student_Name");
		studentname_column.setMinWidth(100);
		studentname_column.setCellValueFactory(new PropertyValueFactory<Statistics, String>("studentname"));

		TableColumn k1energy_column = new TableColumn("K1_Energy");
		k1energy_column.setMinWidth(100);
		k1energy_column.setCellValueFactory(new PropertyValueFactory<Statistics, String>("k1energy"));

		TableColumn k2energy_column = new TableColumn("k2_Energy");
		k2energy_column.setMinWidth(100);
		k2energy_column.setCellValueFactory(new PropertyValueFactory<Statistics, String>("k2energy"));

		TableColumn k3trick1_column = new TableColumn("K3_Trick1");
		k3trick1_column.setMinWidth(100);
		k3trick1_column.setCellValueFactory(new PropertyValueFactory<Statistics, String>("k3trick1"));

		TableColumn k3trick2_column = new TableColumn("K3_Trick2");
		k3trick2_column.setMinWidth(100);
		k3trick2_column.setCellValueFactory(new PropertyValueFactory<Statistics, String>("k3trick2"));

		TableColumn mypreference_column = new TableColumn("My_Preference");
		mypreference_column.setMinWidth(100);
		mypreference_column.setCellValueFactory(new PropertyValueFactory<Statistics, String>("mypreference"));

		TableColumn concerns_column = new TableColumn("Concerns");
		concerns_column.setMinWidth(100);
		concerns_column.setCellValueFactory(new PropertyValueFactory<Statistics, String>("concerns"));

		person_table.setItems(person_data);
		person_table.getColumns().addAll(studentid_column, studentname_column, k1energy_column, k2energy_column,
				k3trick1_column, k3trick2_column, mypreference_column, concerns_column);
		person_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		final VBox vbox_person = new VBox();
		vbox_person.setSpacing(5);
		vbox_person.setPadding(new Insets(10, 0, 0, 10));
		vbox_person.getChildren().addAll(label_person, person_table);

		((Group) scene_person.getRoot()).getChildren().addAll(vbox_person);

		stage_person.setScene(scene_person);
		stage_person.show();
		
		//Task3: Define a stage and scene for statistics data, similar to the above code snippet
	}

	public static class Person {

		private final SimpleStringProperty studentid;
		private final SimpleStringProperty studentname;
		private final SimpleStringProperty k1energy;
		private final SimpleStringProperty k2energy;
		private final SimpleStringProperty k3trick1;
		private final SimpleStringProperty k3trick2;
		private final SimpleStringProperty mypreference;
		private final SimpleStringProperty concerns;

		private Person(String student_id, String student_name, String k1_energy, String k2_energy, String k3_trick1,
				String k3_trick2, String my_preference, String concerns) {
			this.studentid = new SimpleStringProperty(student_id);
			this.studentname = new SimpleStringProperty(student_name);
			this.k1energy = new SimpleStringProperty(k1_energy);
			this.k2energy = new SimpleStringProperty(k2_energy);
			this.k3trick1 = new SimpleStringProperty(k3_trick1);
			this.k3trick2 = new SimpleStringProperty(k3_trick2);
			this.mypreference = new SimpleStringProperty(my_preference);
			this.concerns = new SimpleStringProperty(concerns);
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
	public static class Statistics {

	}

}
