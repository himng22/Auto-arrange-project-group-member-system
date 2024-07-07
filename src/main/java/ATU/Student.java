package ATU;

import java.util.ArrayList;

/**
 * Class for every student entity <br>
 * Each Student has SID, name, email, k1_energy, k2_energy, k3, preference and concerns.
 */
public class Student {

    // attributes/data members
    private final String id;
    private final String name;
    private final String email;
    private final int k1_energy;
    private final int k2_energy;
    private final boolean k3_tick1;
    private final boolean k3_tick2;
    private final boolean my_preference;     // preference to be team leader or not
    private final String concerns;            // any student's concerns

    /**
     * Constructor, all parameters are Strings
     */
    public Student(String id, String name, String email, String k1_energy, String k2_energy, String k3_tick1, String k3_tick2, String my_preference, String concerns) {
        this.id = id.trim();
        this.name = name.trim();
        this.email = email.trim();

        int temp;     // temp variable for validating the k1_energy(should be able to converted to int)
        try {
            temp = Integer.parseInt(k1_energy.trim());
        }
        catch (NumberFormatException e) {
            temp = -1;                   // -1 if exception
            System.out.println("Illegal k1_energy input detected. ATU result may be tampered. ");
        }
        this.k1_energy = temp;          // assign k1_energy

        try {
            temp = Integer.parseInt(k2_energy.trim());
        }
        catch (NumberFormatException e) {
            temp = -1;
            System.out.println("Illegal k2_energy input detected. ATU result may be tampered. ");
        }
        this.k2_energy = temp;          // assign k2_energy

        this.k3_tick1 = strToBool(k3_tick1.trim());           // assign k3_tick1
        this.k3_tick2 = strToBool(k3_tick2.trim());            // assign k3_tick2
        this.my_preference = strToBool(my_preference.trim());  // assign my_preference

        if (concerns.trim().isEmpty())         // if no concern is provided
                this.concerns = "No comment";
        else
            this.concerns = concerns.trim();                       // assign concerns
    }

    /**
     * Typical constructor
     */
    public Student(String id, String name, String email, int k1_energy, int k2_energy, boolean k3_tick1, boolean k3_tick2, boolean my_preference, String concerns) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.k1_energy = k1_energy;
        this.k2_energy = k2_energy;
        this.k3_tick1 = k3_tick1;
        this.k3_tick2 = k3_tick2;
        this.my_preference = my_preference;
        this.concerns = concerns;
    }

    public Student(String id, String name, String email, int k1_energy, int k2_energy, boolean k3_tick1, boolean k3_tick2, boolean my_preference, String concerns) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.k1_energy = k1_energy;
        this.k2_energy = k2_energy;
        this.k3_tick1 = k3_tick1;
        this.k3_tick2 = k3_tick2;
        this.my_preference = my_preference;
        this.concerns = concerns;
    }

    /**
     * helper function to convert from "1" or "0" to boolean
     * @param input a string which is either "1" or "0"
     * @return true if input is "1", false otherwise
     */
    public static boolean strToBool(final String input){
        return input.equals("1");
    }

    /**
     * helper function to convert boolean into "1" or "0"
     * @param input the boolean value to be converted
     * @return "1" if input is true, "0" if input is false, otherwise return "null"
     */
    public static String toNumeralBool(final Boolean input) {
        if (input == null) {
            return "null";
        } else {
            return input ? "1" : "0";
        }
    }

    /**
     * a helper function for calculating the average of k1_energy/k2_energy of an array of Students
     * @param stuArr an array of Student
     * @param energyType 1 for k1_energy, 2 for k2_energy
     * @return the average value of k1_energy/k2_energy, which is in float type
     */
    public static float calEnergy_avg(ArrayList<Student> stuArr, int energyType){
        int sum = 0;
        for (Student stu : stuArr){
            if (energyType == 1)
                sum += stu.getK1_energy();
            else if (energyType == 2)
                sum += stu.getK2_energy();
            else
                return -1;      // -1 indicate error, argument energyType with wrong value
        }
        return (float) sum / stuArr.size();
    }

    /**
     * a helper function for calculating the min of k1_energy/k2_energy of an array of Students
     * @param stuArr an array of Student
     * @param energyType 1 for k1_energy, 2 for k2_energy
     * @return the minimum value of k1_energy/k2_energy, which is in int type
     */
    public static int calEnergy_min(ArrayList<Student> stuArr, int energyType){
        int min;
        if (energyType == 1)  min = stuArr.get(0).getK1_energy();
        else if (energyType == 2) min = stuArr.get(0).getK2_energy();
        else return -1;     // -1 indicate error, argument energyType with wrong value

        for (Student stu : stuArr){
            if (energyType == 1)
                min = Math.min(stu.getK1_energy(), min);
            else
                min = Math.min(stu.getK2_energy(), min);
        }
        return min;
    }

    /**
     * a helper function for calculating the max of k1_energy/k2_energy of an array of Students
     * @param stuArr an array of Student
     * @param energyType 1 for k1_energy, 2 for k2_energy
     * @return the maximum value of k1_energy/k2_energy, which is in int type
     */
    public static int calEnergy_max(ArrayList<Student> stuArr, int energyType){
        int max;
        if (energyType == 1)  max = stuArr.get(0).getK1_energy();
        else if (energyType == 2) max = stuArr.get(0).getK2_energy();
        else return -1;     // -1 indicate error, argument energyType with wrong value

        for (Student stu : stuArr){
            if (energyType == 1)
                max = Math.max(stu.getK1_energy(), max);
            else
                max = Math.max(stu.getK2_energy(), max);
        }
        return max;
    }

    // getter accessor methods
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getK1_energy() {
        return k1_energy;
    }

    public int getK2_energy() {
        return k2_energy;
    }

    public boolean isK3_tick1() {
        return k3_tick1;
    }

    public boolean isK3_tick2() {
        return k3_tick2;
    }

    public boolean isMy_preference() {
        return my_preference;
    }

    public String getConcerns() {
        return concerns;
    }

    // overriding the toString for printing
    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", k1=" + k1_energy +
                ", k2=" + k2_energy +
                '}';
    }
}
