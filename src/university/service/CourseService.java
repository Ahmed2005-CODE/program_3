package university.service;

import university.model.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * SERVICE - File access for courses.
 */
public class CourseService {

    private static final String FILE = "Data/courses.txt";

    public List<Course> loadCourses() {
        List<Course> courses = new ArrayList<>();

        for (String line : FileStorage.readLines(FILE)) {
            String[] parts = line.split(",", -1);
            if (parts.length != 4) {
                continue;
            }

            try {
                courses.add(new Course(
                        parts[0].trim(),
                        parts[1].trim(),
                        Integer.parseInt(parts[2].trim()),
                        parts[3].trim()
                ));
            } catch (NumberFormatException e) {
                System.err.println("Skipping invalid course line: " + line);
            }
        }

        return courses;
    }

    public void addCourse(Course course) {
        FileStorage.appendLine(FILE, serialize(course));
    }

    public void updateCourse(Course oldCourse, Course updatedCourse) {
        List<Course> courses = loadCourses();

        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCode().equalsIgnoreCase(oldCourse.getCode())) {
                courses.set(i, updatedCourse);
                break;
            }
        }

        saveAll(courses);
    }

    public void deleteCourse(String code) {
        List<Course> courses = loadCourses();
        courses.removeIf(course -> course.getCode().equalsIgnoreCase(code));
        saveAll(courses);
    }

    /** Called when a professor is deleted: their courses become unassigned. */
    public void clearProfessor(String professorId) {
        List<Course> courses = loadCourses();

        for (Course course : courses) {
            if (course.getProfessorId().equalsIgnoreCase(professorId)) {
                course.setProfessorId("");
            }
        }

        saveAll(courses);
    }

    public boolean exists(String code) {
        for (Course course : loadCourses()) {
            if (course.getCode().equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }

    private void saveAll(List<Course> courses) {
        List<String> lines = new ArrayList<>();
        for (Course course : courses) {
            lines.add(serialize(course));
        }
        FileStorage.writeLines(FILE, lines);
    }

    private String serialize(Course course) {
        return FileStorage.join(
                course.getCode(),
                course.getTitle(),
                String.valueOf(course.getCredits()),
                course.getProfessorId()
        );
    }
}
