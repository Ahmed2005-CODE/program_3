package university.model;

/**
 * MODEL - A course. professorId refers to Professor.id (empty = unassigned).
 */
public class Course {

    private String code;
    private String title;
    private int credits;
    private String professorId;

    public Course(String code, String title, int credits, String professorId) {
        this.code = code;
        this.title = title;
        this.credits = credits;
        this.professorId = professorId == null ? "" : professorId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId == null ? "" : professorId;
    }

    @Override
    public String toString() {
        return code + " - " + title;
    }
}
