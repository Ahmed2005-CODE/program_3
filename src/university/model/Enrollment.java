package university.model;

/**
 * MODEL - Links a student to a course, with an optional grade.
 */
public class Enrollment {

    private String studentId;
    private String courseCode;
    private String grade;

    public Enrollment(String studentId, String courseCode, String grade) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.grade = grade == null ? "" : grade;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade == null ? "" : grade;
    }
}
