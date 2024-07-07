package ATU;

import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Junit test script for most of the testable method in this ATU package
 */
public class ATUtest {

    // relative path of the json file for testing in this repo
    // read only, NO modification to this file !!
    private final String jsonFilePathForTest = "src/main/resources/atuOutput/comp3111_ATU.json";


    @Test
    public void jsonStillExistTrue() {
        String path = "src/main/resources/atuOutput/fake.json";
        assertTrue(ATUEngine.jsonStillExist(path));
    }

    @Test
    public void jsonStillExistTrue2() {
        assertTrue(ATUEngine.jsonStillExist(jsonFilePathForTest));
    }
    @Test
    public void jsonStillExistTrue3() {
        String path = "src/main/resources/atuOutput/nowSuchFile.json";
        assertFalse(ATUEngine.jsonStillExist(path));
    }
    @Test
    public void jsonStillExistTrue4() {
        String path = "src/main/resources/atuOutput/comp3111_apitester.json";
        assertTrue(ATUEngine.jsonStillExist(path));
    }

    @Test
    public void outputJsonTest(){           // testing with a atuEngine for 0 total student -> no output
        ATUEngine atu = new ATUEngine(0);
        assertEquals("[]", atu.outputJson("src/main/resources/atuOutput/junit_test.json"));
    }

    @Test
    public void outputJsonTest2(){           // testing with a atuEngine for 0 total student -> no output
        ATUEngine atu = new ATUEngine(3);

        ArrayList<Student> stuList = new ArrayList<>();
        stuList.add(new Student("20220223", "David", "tset@email.com",
                38, 90, false, true, true, "Good"));
        stuList.add(new Student("20135231", "Kate", "ts2et@email.com",
                38, 92, false, true, false, ""));
        stuList.add(new Student("98739201", "Jason", "t122et@email.com",
                34, 0, false, true, false, "123"));

        atu.run(stuList);           // run the ATU
        String expected_jsonOutput = "[{\"leaderName\":\"David\",\"members\":[{\"name\":\"David\",\"k1_energy\":38,\"id\":\"20220223\",\"k2_energy\":90,\"email\":\"tset@email.com\"},{\"name\":\"Jason\",\"k1_energy\":34,\"id\":\"98739201\",\"k2_energy\":0,\"email\":\"t122et@email.com\"},{\"name\":\"Kate\",\"k1_energy\":38,\"id\":\"20135231\",\"k2_energy\":92,\"email\":\"ts2et@email.com\"}],\"id\":1}]";

        assertEquals(expected_jsonOutput, atu.outputJson("src/main/resources/atuOutput/junit_test.json"));
    }

    @Test
    public void readFromJsonTest(){         // test if the record can be correctly imported from JSON file
        ATUEngine atu = new ATUEngine(0);
        atu.readFromJson(jsonFilePathForTest);

        assertEquals(4, atu.getTeamList().get(0).getSize());
        assertEquals(1, atu.getTeamList().get(0).getId());
        assertEquals("DAFFODIL, Achillobator", atu.getTeamList().get(0).getLeaderName());
        assertEquals(100, atu.getTeamList().get(0).getTeamMembers().get(0).getK1_energy());
        assertEquals(50, atu.getTeamList().get(0).getTeamMembers().get(0).getK2_energy());
        assertEquals(3, atu.getTeamList().get(5).getSize());
    }

    @Test
    public void courseProjectGetter(){      // test with an empty courseProject for Review process
        CourseProject cp = new CourseProject("comp3111_ATU", jsonFilePathForTest);
        assertEquals("", cp.getCourseName());
    }
    @Test
    public void courseProjectGetter2(){      // test with a empty courseProject for Review process
        CourseProject cp = new CourseProject("comp3111_ATU", jsonFilePathForTest);
        assertEquals("", cp.getProjectName());
    }
    @Test
    public void courseProjectToString(){        // test if CourseProject class override the toString() correctly for Review process
        CourseProject cp = new CourseProject("comp3111_ATU", jsonFilePathForTest);
        assertEquals("comp3111_ATU", cp.toString());
    }
    @Test
    public void courseProjectLoadedAlready(){   // file should not be loaded at the creation of CourseProject, it should be loaded in runtime
        CourseProject cp = new CourseProject("comp3111_ATU", jsonFilePathForTest);
        assertEquals(false, cp.getLoadedAlready());
    }

    @Test
    public void studentTest(){          // empty Student object test
        Student stu = new  Student("", "", "",
                "", "", "", "", "", "");
        assertEquals("", stu.getId());
        assertEquals("", stu.getEmail());
        assertEquals("No comment", stu.getConcerns());
        assertEquals("", stu.getName());
        assertEquals(-1, stu.getK1_energy());
        assertEquals(-1, stu.getK2_energy());
    }
    @Test
    public void studentTest2(){          // non-empty Student object construction test
        Student stu = new  Student("123", "HI", "hi@email.com",
                "35", "42", "0", "1", "0", "ok");
        assertEquals("123", stu.getId());
        assertEquals("hi@email.com", stu.getEmail());
        assertEquals("ok", stu.getConcerns());
        assertEquals("HI", stu.getName());
        assertEquals(35, stu.getK1_energy());
        assertEquals(42, stu.getK2_energy());
    }

    @Test
    public void libraryCalculateStats(){
        ArrayList<Student> stuList = new ArrayList<Student>(){};
        stuList.add(new Student("20220223", "David", "tset@email.com",
                        38, 90, false, true, true, "Good"));
        stuList.add(new Student("20135231", "Kate", "ts2et@email.com",
                38, 92, false, true, false, ""));

        Library.Statistics[] statResult = Library.calculateStats(stuList);          // test

        Library.Statistics[] expectResult = new Library.Statistics[6];
        expectResult[0] = new Library.Statistics(0, "Total number of Students", String.valueOf(2));
        expectResult[1] = new Library.Statistics(1, "K1_Energy(Average, Min, Max)", "(" + String.format("%.02f",38.00) + ", " + String.valueOf(38) + ", " + String.valueOf(38) + ")");
        expectResult[2] = new Library.Statistics(2, "K2_Energy(Average, Min, Max)",
                "(" + String.format("%.02f", (float)91) + ", " + String.valueOf(90) + ", " + String.valueOf(92) + ")");
        expectResult[3] = new Library.Statistics(3, "K3_Tick1 = 1", String.valueOf(0));
        expectResult[4] = new Library.Statistics(4, "K3_Tick2 = 1", String.valueOf(2));
        expectResult[5] = new Library.Statistics(5, "My_Preference = 1", String.valueOf(1));

        for (int i=0; i < 5; i++) {
            assertEquals(expectResult[i].getEntry(), statResult[i].getEntry());
            assertEquals(expectResult[i].getValue(), statResult[i].getValue());
        }
    }

    @Test
    public void libraryCalculateStats2(){           // all empty fields in student record
        ArrayList<Student> stuList = new ArrayList<Student>(){};
        stuList.add(new Student("", "", "",
                "", "", "", "", "", ""));
        stuList.add(new Student("", "", "",
                "", "", "", "", "", ""));

        Library.Statistics[] statResult = Library.calculateStats(stuList);          // test

        Library.Statistics[] expectResult = new Library.Statistics[6];
        expectResult[0] = new Library.Statistics(0, "Total number of Students", String.valueOf(2));
        expectResult[1] = new Library.Statistics(1, "K1_Energy(Average, Min, Max)", "(" + String.format("%.02f", -1.00) + ", " + String.valueOf(-1) + ", " + String.valueOf(-1) + ")");
        expectResult[2] = new Library.Statistics(2, "K2_Energy(Average, Min, Max)",
                "(" + String.format("%.02f", (float)-1) + ", " + String.valueOf(-1) + ", " + String.valueOf(-1) + ")");
        expectResult[3] = new Library.Statistics(3, "K3_Tick1 = 1", String.valueOf(0));
        expectResult[4] = new Library.Statistics(4, "K3_Tick2 = 1", String.valueOf(0));
        expectResult[5] = new Library.Statistics(5, "My_Preference = 1", String.valueOf(0));

        for (int i=0; i < 5; i++) {
            assertEquals(expectResult[i].getEntry(), statResult[i].getEntry());
            assertEquals(expectResult[i].getValue(), statResult[i].getValue());
        }
    }




}