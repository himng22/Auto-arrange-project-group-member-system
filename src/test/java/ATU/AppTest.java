package ATU;

import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;


import static org.junit.Assert.assertEquals;

// using the TestFX library, for testing with running the application
public class AppTest extends ApplicationTest {

    @Before
    public void setUpClass() throws Exception{
        ApplicationTest.launch(Library.class);
    }

    @Test
    public void libraryReadCSV(){               // test if CSV file is read correctly
        assertEquals("SAFFRON, Corgipoo", Library.getStudentArr().get(0).getName());
        assertEquals("20023331", Library.getStudentArr().get(1).getId());
        assertEquals("BeetleLEE@connect.ust.hk", Library.getStudentArr().get(2).getEmail());
        assertEquals("No comment", Library.getStudentArr().get(3).getConcerns());
    }

}
