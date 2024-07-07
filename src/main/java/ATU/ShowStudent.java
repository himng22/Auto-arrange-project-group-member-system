/**
 * The data model of StudentPersonalPage
 * @author
 * @version 1.0
 */
public class ShowStudent {
    /**
     * index
     */
    private final String idx;

    /**
     * id
     */
    private final String id;

    /**
     * name
     */
    private final String name;

    /**
     * email
     */
    private final String email;

    /**
     * k1Energy
     */
    private final String k1Energy;

    /**
     * k2Energy
     */
    private final String k2Energy;

    /**
     * k3Tick1
     */
    private final String k3Tick1;

    /**
     * k3Tick2
     */
    private final String k3Tick2;

    /**
     * myPreference
     */
    private final String myPreference;

    /**
     * concerns
     */
    private final String concerns;


    /**
     * constructor
     * @param idx - index
     * @param student - student
     */
    public ShowStudent(int idx, Student student) {
        this.idx = "" + idx;
        this.id ="" + student.getId();
        this.name = student.getName();
        this.email = student.getEmail();
        this.k1Energy = "" + student.getK1Energy();
        this.k2Energy = "" + student.getK2Energy();
        this.k3Tick1 = student.isK3Tick1() ? "1" :"0";
        this.k3Tick2 = student.isK3Tick2() ? "1" :"0";
        this.myPreference = student.isMyPreference() ? "1" :"0";
        this.concerns = student.getConcerns();
    }

    /**
     * index
     * @return index
     */
    public String getIdx() {
        return idx;
    }

    /**
     * id
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * k1Energy
     * @return k1Energy
     */
    public String getK1Energy() {
        return k1Energy;
    }

    /**
     * k2Energy
     * @return k2Energy
     */
    public String getK2Energy() {
        return k2Energy;
    }

    /**
     * k3Tick1
     * @return k3Tick1
     */
    public String getK3Tick1() {
        return k3Tick1;
    }

    /**
     * k3Tick2
     * @return k3Tick2
     */
    public String getK3Tick2() {
        return k3Tick2;
    }

    /**
     * myPreference
     * @return myPreference
     */
    public String getMyPreference() {
        return myPreference;
    }

    /**
     * Concerns
     * @return Concerns
     */
    public String getConcerns() {
        return concerns;
    }
}
