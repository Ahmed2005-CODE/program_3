# University Management System (JavaFX + MVC)

A JavaFX desktop application built in the **MVC** style of the
`Programming_3/Week3/src/sample` pharmacy example. Data is persisted in
plain comma-separated **text files** under `Data/`.

## Run

```sh
./run.sh          # compiles if needed, then starts the app
./compile.sh      # compile only (output goes to out/)
```

Requirements: JDK 17+ (built and tested with JDK 27). The needed JavaFX 27
jars for macOS `aarch64` are already in `lib/`, so no Maven/Gradle is needed.

If you are on Intel macOS or another JDK/OS, replace the jars in `lib/` with
the matching `mac` / `linux` / `win` classifiers from Maven Central
(`org.openjfx:javafx-{base,graphics,controls}:27.0.1`).

## Project structure

```
src/university/
  Main.java                  Application entry point (builds the Scene)
  controller/                Controllers: events, validation, orchestration
    MainController.java      Composes all modules + dashboard refresh
    StudentController.java
    CourseController.java
    ProfessorController.java
    EnrollmentController.java
  model/                     Plain Java data classes (no JavaFX)
    Student.java  Course.java  Professor.java
    Enrollment.java  EnrollmentRow.java
  service/                   All text-file reading/writing
    FileStorage.java         Shared UTF-8 line reader/writer
    StudentService.java  CourseService.java
    ProfessorService.java  EnrollmentService.java
  view/                      JavaFX UI construction (controls + getters)
    MainView.java  StudentView.java  CourseView.java
    ProfessorView.java  EnrollmentView.java
    StudentFormView.java  CourseFormView.java  ProfessorFormView.java
    Ui.java                  Shared styling/alert helpers

Data/
  students.txt             id,name,email,major,year
  professors.txt           id,name,email,department,office
  courses.txt              code,title,credits,professorId
  enrollments.txt          studentId,courseCode,grade
```

## MVC flow (same idea as the sample project)

- **Model** – simple POJOs with fields, getters and setters.
- **Service** – the only layer that touches the text files (`load`, `add`,
  `update`, `delete`). Lines starting with `#` are comments.
- **View** – builds controls in Java code and exposes them with getters;
  contains no business logic.
- **Controller** – creates the view and service, loads records into an
  `ObservableList`, wires button/table/search events, validates input and
  shows alerts. `Main` gets `controller.getView().getRoot()` for the Scene.

## Features

- Add / edit / delete / search students, courses and professors.
- Delete a student or course → their enrollments are removed too.
- Delete a professor → their courses become unassigned.
- Enroll students in courses, assign grades, remove enrollments, search.
- Dashboard counts and a global **Refresh** button.
- Input validation (required fields, number parsing, duplicate IDs,
  comma-in-field check because comma is the file delimiter).

## Opening in an IDE

Create a plain Java project with `src` as the source root, add the three jars
from `lib/` to the classpath/module path, and run `university.Main`.
Add `--module-path lib --add-modules javafx.controls,javafx.graphics,javafx.base`
to the VM options if the IDE does not do it automatically.
