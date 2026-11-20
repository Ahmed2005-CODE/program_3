package university.service;

import university.model.Professor;

import java.util.ArrayList;
import java.util.List;

/**
 * SERVICE - File access for professors.
 */
public class ProfessorService {

    private static final String FILE = "Data/professors.txt";

    public List<Professor> loadProfessors() {
        List<Professor> professors = new ArrayList<>();

        for (String line : FileStorage.readLines(FILE)) {
            String[] parts = line.split(",", -1);
            if (parts.length != 5) {
                continue;
            }

            professors.add(new Professor(
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    parts[4].trim()
            ));
        }

        return professors;
    }

    public void addProfessor(Professor professor) {
        FileStorage.appendLine(FILE, serialize(professor));
    }

    public void updateProfessor(Professor oldProfessor, Professor updatedProfessor) {
        List<Professor> professors = loadProfessors();

        for (int i = 0; i < professors.size(); i++) {
            if (professors.get(i).getId().equalsIgnoreCase(oldProfessor.getId())) {
                professors.set(i, updatedProfessor);
                break;
            }
        }

        saveAll(professors);
    }

    public void deleteProfessor(String id) {
        List<Professor> professors = loadProfessors();
        professors.removeIf(professor -> professor.getId().equalsIgnoreCase(id));
        saveAll(professors);
    }

    public boolean exists(String id) {
        for (Professor professor : loadProfessors()) {
            if (professor.getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    private void saveAll(List<Professor> professors) {
        List<String> lines = new ArrayList<>();
        for (Professor professor : professors) {
            lines.add(serialize(professor));
        }
        FileStorage.writeLines(FILE, lines);
    }

    private String serialize(Professor professor) {
        return FileStorage.join(
                professor.getId(),
                professor.getName(),
                professor.getEmail(),
                professor.getDepartment(),
                professor.getOffice()
        );
    }
}
