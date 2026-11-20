package university.model;

/**
 * MODEL - Read-only display row for the enrollments table.
 * It resolves the IDs stored in Enrollment into readable names.
 */
public class EnrollmentRow {

    private final String studentId;
    private final String courseCode;
    private final String studentName;
    private final String courseTitle;
    private String grade;

    public EnrollmentRow(String studentId, String courseCode,
                         String studentName, String courseTitle, String grade) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.studentName = studentName;
        this.courseTitle = courseTitle;
        this.grade = grade == null ? "" : grade;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade == null ? "" : grade;
    }
}
