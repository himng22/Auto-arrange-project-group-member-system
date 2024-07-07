package ATU;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Class for every team entity formed after ATU. <br>
 * Each team has id, leaderName and an array of Student objects representing the team members.
 */
public class Team {

    private final int id;       // teamID which should be const
    private String leaderName;  // leader's name

    private int size;           // size of this team

    // array of Students in this Team
    private ArrayList<Student> teamMembers;

    // constructor
    public Team(int id, String leaderName, int size){
        this.id = id;
        this.leaderName = leaderName;
        this.size = size;
    }

    // conversion constructor from JSONObject(team object)
    // called by readFromJSON in ATUEngine
    public Team(JSONObject teamobj){
        this.id = teamobj.getInt("id");
        this.leaderName = teamobj.getString("leaderName");

        this.teamMembers = new ArrayList<>();
        JSONArray members = teamobj.getJSONArray("members");
        for (int i=0; i < members.length(); i++){
            teamMembers.add(new Student(
                    members.getJSONObject(i).getString("id"),           // note: SID is String
                    members.getJSONObject(i).getString("name"),
                    members.getJSONObject(i).getString("email"),
                    members.getJSONObject(i).getInt("k1_energy"),
                    members.getJSONObject(i).getInt("k2_energy"),
                    false,
                    false,
                    false,
                    ""
                    ));                     // filling in dummy values to some attributes because they are not used in Review process
        }

        this.size = this.teamMembers.size();
    }

    // adding one team member
    public void addTeamMember(Student newMember){
        teamMembers.add(newMember);
        size = teamMembers.size();          // update the size
    }

    // setter of teamMembers
    public void setTeamMembers(ArrayList<Student> teamMembers) {
        this.teamMembers = teamMembers;
        size = teamMembers.size();          // update the size
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public void setSize(int size) {
        this.size = size;
    }

    // accessor methods
    public ArrayList<Student> getTeamMembers() {
        return teamMembers;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public String getLeaderName() {
        return leaderName;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", leaderName='" + leaderName + '\'' +
                ", size=" + size +
                ", teamMembers=" + teamMembers +
                '}';
    }
}
