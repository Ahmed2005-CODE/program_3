package university.model;

/**
 * MODEL - A student record. Plain Java object: no JavaFX here.
 */
public class Student {

    private String id;
    private String name;
    private String email;
    private String major;
    private int year;

    public Student(String id, String name, String email, String major, int year) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.major = major;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}
