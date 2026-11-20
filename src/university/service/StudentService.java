package university.service;

import university.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * SERVICE - All file access for students lives here.
 * The controller never touches the text file directly.
 */
public class StudentService {

    private static final String FILE = "Data/students.txt";

    public List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();

        for (String line : FileStorage.readLines(FILE)) {
            String[] parts = line.split(",", -1);
            if (parts.length != 5) {
                continue;
            }

            try {
                students.add(new Student(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        Integer.parseInt(parts[4].trim())
                ));
            } catch (NumberFormatException e) {
                System.err.println("Skipping invalid student line: " + line);
            }
        }

        return students;
    }

    public void addStudent(Student student) {
        FileStorage.appendLine(FILE, serialize(student));
    }

    public void updateStudent(Student oldStudent, Student updatedStudent) {
        List<Student> students = loadStudents();

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equalsIgnoreCase(oldStudent.getId())) {
                students.set(i, updatedStudent);
                break;
            }
        }

        saveAll(students);
    }

    public void deleteStudent(String id) {
        List<Student> students = loadStudents();
        students.removeIf(student -> student.getId().equalsIgnoreCase(id));
        saveAll(students);
    }

    public boolean exists(String id) {
        for (Student student : loadStudents()) {
            if (student.getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    private void saveAll(List<Student> students) {
        List<String> lines = new ArrayList<>();
        for (Student student : students) {
            lines.add(serialize(student));
        }
        FileStorage.writeLines(FILE, lines);
    }

    private String serialize(Student student) {
        return FileStorage.join(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getMajor(),
                String.valueOf(student.getYear())
        );
    }
}
