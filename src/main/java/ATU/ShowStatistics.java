/**
 * The data model of StudentStatisticsPage
 * @author
 * @version 1.0
 */
public class ShowStatistics {
    /**
     * idx
     */
    private final String idx;

    /**
     * entry
     */
    private final String entry;

    /**
     * value
     */
    private final String value;

    /**
     * constructor
     * @param idx - idx
     * @param entry - entry
     * @param value - value
     */
    public ShowStatistics(String idx, String entry, String value) {
        this.idx = idx;
        this.entry = entry;
        this.value = value;
    }

    /**
     * get index
     * @return index
     */
    public String getIdx() {
        return idx;
    }

    /**
     * get entry
     * @return - entry
     */
    public String getEntry() {
        return entry;
    }

    /**
     * get value
     * @return value
     */
    public String getValue() {
        return value;
    }
}
