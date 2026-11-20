package university.service;

import university.model.Enrollment;

import java.util.ArrayList;
import java.util.List;

/**
 * SERVICE - File access for student/course enrollments and grades.
 */
public class EnrollmentService {

    private static final String FILE = "Data/enrollments.txt";

    public List<Enrollment> loadEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();

        for (String line : FileStorage.readLines(FILE)) {
            String[] parts = line.split(",", -1);
            if (parts.length != 3) {
                continue;
            }

            enrollments.add(new Enrollment(
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim()
            ));
        }

        return enrollments;
    }

    /** Returns false when the student is already enrolled in the course. */
    public boolean addEnrollment(String studentId, String courseCode) {
        if (exists(studentId, courseCode)) {
            return false;
        }

        FileStorage.appendLine(
                FILE,
                FileStorage.join(studentId, courseCode, "")
        );
        return true;
    }

    public void setGrade(String studentId, String courseCode, String grade) {
        List<Enrollment> enrollments = loadEnrollments();

        for (Enrollment enrollment : enrollments) {
            if (matches(enrollment, studentId, courseCode)) {
                enrollment.setGrade(grade);
                break;
            }
        }

        saveAll(enrollments);
    }

    public void deleteEnrollment(String studentId, String courseCode) {
        List<Enrollment> enrollments = loadEnrollments();
        enrollments.removeIf(enrollment ->
                matches(enrollment, studentId, courseCode));
        saveAll(enrollments);
    }

    public void deleteByStudent(String studentId) {
        List<Enrollment> enrollments = loadEnrollments();
        enrollments.removeIf(enrollment ->
                enrollment.getStudentId().equalsIgnoreCase(studentId));
        saveAll(enrollments);
    }

    public void deleteByCourse(String courseCode) {
        List<Enrollment> enrollments = loadEnrollments();
        enrollments.removeIf(enrollment ->
                enrollment.getCourseCode().equalsIgnoreCase(courseCode));
        saveAll(enrollments);
    }

    public boolean exists(String studentId, String courseCode) {
        for (Enrollment enrollment : loadEnrollments()) {
            if (matches(enrollment, studentId, courseCode)) {
                return true;
            }
        }
        return false;
    }

    private boolean matches(Enrollment enrollment, String studentId, String courseCode) {
        return enrollment.getStudentId().equalsIgnoreCase(studentId)
                && enrollment.getCourseCode().equalsIgnoreCase(courseCode);
    }

    private void saveAll(List<Enrollment> enrollments) {
        List<String> lines = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            lines.add(FileStorage.join(
                    enrollment.getStudentId(),
                    enrollment.getCourseCode(),
                    enrollment.getGrade()
            ));
        }
        FileStorage.writeLines(FILE, lines);
    }
}
