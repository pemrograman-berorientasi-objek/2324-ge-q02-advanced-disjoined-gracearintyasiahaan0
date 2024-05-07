package academic.model;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String code;
    private String name;
    private int credit;
    private String passingGrade;
    private List<Lecturer> lecturers; // Tambahkan atribut untuk menyimpan dosen pengajar

    // Constructor
    public Course(String code, String name, int credit, String passingGrade) {
        this.code = code;
        this.name = name;
        this.credit = credit;
        this.passingGrade = passingGrade;
        this.lecturers = new ArrayList<>(); // Inisialisasi list dosen pengajar
    }//membuat setter getter nya
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getCredit() {
        return credit;
    }
    

    public String getPassingGrade() {
        return passingGrade;
    }
    public void addLecturer(Lecturer lecturer) {
        this.lecturers.add(lecturer);
    }

    public List<Lecturer> getLecturers() {
        return lecturers;
    }

    @Override
    public String toString() {
        return code + "|" + name + "|" + credit + "|" + passingGrade;
    }
}
