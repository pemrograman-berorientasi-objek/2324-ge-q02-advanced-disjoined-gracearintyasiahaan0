package academic.model;

/**
 * @author 12S22022 Grace Arintya Siahaan
 * @author NIM Nama
 */
public class Enrollment {

    private String code;  
    private String id;
    private String academicYear;
    private String semester;
    private String grade = "None"; 
    private String remedialGrade;
    private String previousGrade;
    private int total ;
    

    //construction  
    public Enrollment(String _code, String _id, String _academicYear, String _semester) {
        code = _code;
        id = _id;
        academicYear = _academicYear; 
        semester = _semester;
        previousGrade = "";
        remedialGrade = null;
        total = 0;

    }

    // get code
    public String getCode() {
        return code;
    }

    // get id
    public String getId() {
        return id;
    }

    // get academic year
    public String getAcademicYear() {
        return academicYear;
    }

    // get semester
    public String getSemester() {
        return semester;
    }

    // get grade
    public String getGrade() {
        return grade;
    }

    // set grade
    public void setGrade(String _grade) {
        grade = _grade;
    }

    public String getRemedialGrade() {
        return remedialGrade;
    }

    public void setRemedialGrade(String remedialGrade) {
        this.remedialGrade = remedialGrade;
    }

    public int getTotal(){
        return total;
    }

    public void  setTotal(){
        this.total += 1;
    }

    // buat fungsi untuk menukar grade dengan remedial grade
    public void swapGrade() {
        String temp = grade;
        grade = previousGrade;
        previousGrade = temp;
    }

    public String getPreviousGrade() {
        return previousGrade;
    }

    public void setPreviousGrade(String previousGrade) {
        this.previousGrade = previousGrade;
    }

    @Override
    public String toString() {
        return code + "|" + id + "|" + academicYear + "|" + semester + "|" + grade + "|" + remedialGrade + "|" + previousGrade ;
    }

    public String remedialToString() {
        return code + "|" + id + "|" + academicYear + "|" + semester + "|" + remedialGrade + "|" + grade + "|" + previousGrade + "|" + "(" + total  + ")";
    }
}