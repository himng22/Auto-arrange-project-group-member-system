package ATU;

/**
 * Main engine of the system - where the system start
 */
public class Main {

    /**
     * entry point of the whole system
     * @throws Exception the exception thrown by javaFX application
     */
    public static void main(String[] args) throws Exception {

        // launch the javaFX application
        Library.run(args);                 // <--- if you want to run another javaFx application, just change the class

        System.out.println("Application finished successfully");
    }
}
