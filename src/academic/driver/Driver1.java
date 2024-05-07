package academic.driver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import academic.model.Course;
import academic.model.CourseOpening;
import academic.model.Enrollment;
import academic.model.Lecturer;
import academic.model.Student;

/**
 * @author 12S22022 Grace Arintya Siahaan
 * @author NIM Nama
 */
public class Driver1 {

    final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] _args) {
        List<Course> courses = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        List<Enrollment> enrollments = new ArrayList<>();
        List<String> inputList = new ArrayList<>();
        List<Lecturer> lecturers = new ArrayList<>();
        List<CourseOpening> courseOpenings = new ArrayList<>();
        Map<String, String> lecturerEmails = new HashMap<>();

        

        do {
            String input = scanner.nextLine();
            if (input.equals("---")) {
                break;
            }
            inputList.add(input);
        } while (true);

        for (String input : inputList) {
            String[] command = input.split("#");

            switch (command[0]) {
                case "lecturer-add":
                    if (command.length == 6) {
                        Lecturer lecturer = new Lecturer(command[1], command[2], command[3], command[4], command[5]);
                        if (!isDuplicateLecturer(lecturer, lecturers)) {
                            lecturers.add(lecturer);
                            // Simpan email pengajar
                            lecturerEmails.put(command[1], command[5]);
                        }
                    }
                    break;
                case "course-add":
                    if (command.length >= 5) {
                        Course course = new Course(command[1], command[2], Integer.parseInt(command[3]), command[4]);
                        if (!isDuplicateCourse(course, courses)) {
                            courses.add(course);
                        }
                    }
                    break;
                
                case "student-add":
                    if (command.length == 5) {
                        Student student = new Student(command[1], command[2], command[3], command[4]);
                        if (!isDuplicateStudent(student, students)) {
                            students.add(student);
                        }
                    }
                    break;
                case "enrollment-add":
                    if (command.length == 5) {
                        Enrollment enrollment = new Enrollment(command[1], command[2], command[3], command[4]);
                        if (!enrollments.contains(enrollment)) {
                            enrollments.add(enrollment);
                        }
                    }
                    break;
                case "enrollment-grade":
                    if (command.length == 6) {
                        String Code = command[1];
                        String studentId = command[2];
                        String academicYear = command[3];
                        String semester = command[4];
                        String grade = command[5];
                        
                        // Cari Enrollment yang sesuai
                        Enrollment targetEnrollment = findEnrollment(Code, studentId, academicYear, semester, enrollments);
                        
                        // Jika ditemukan, set nilai grade
                        if (targetEnrollment != null) {
                            targetEnrollment.setGrade(grade);
                        }
                    }
                    break;
                case "student-details":
                    for (Student student : students) {
                        double gpa = calculateGPA(student.getId(), enrollments, courses); 
                        int totalCredits = calculateTotalCredits(student.getId(), enrollments, courses);
                        if (student.getId().equals(command[1])) {
                            System.out.println(student.getId() + "|" + student.getName() + "|" + student.getYear() + "|" + student.getStudyProgram() + "|" + String.format("%.2f", gpa) + "|" + totalCredits);
                        }
                    }
                    break;
                    case "course-open":
                    String courseCode = command[1];
                    String academicYear = command[2];
                    String semester = command[3];
                    String lecturerInitial = command[4];
                    List<Lecturer> lecturerList = new ArrayList<>(); // Mengubah tipe list menjadi List<Lecturer>
                
                    // Menambahkan dosen yang sesuai ke dalam list
                    for (Lecturer lecturer : lecturers) {
                        if (lecturer.getInitial().equals(lecturerInitial)) {
                            lecturerList.add(lecturer);
                        }
                    }
                
                    // Membuat objek CourseOpening dan menambahkannya ke dalam list
                    CourseOpening courseOpening = new CourseOpening(courseCode, academicYear, semester, lecturerList, lecturerInitial); // Menggunakan daftar lecturerList yang sudah sesuai tipe
                    courseOpenings.add(courseOpening);
                    break;
                


                    case "course-history":
                    String targetCourseCode = command[1];
                    List<CourseOpening> filteredOpenings = courseOpenings.stream()
                            .filter(opening -> opening.getCourseCode().equals(targetCourseCode))
                            .sorted(Comparator.comparing((CourseOpening opening) -> opening.getSemester().equalsIgnoreCase("odd") ? 0 : 1)
                            .thenComparing(CourseOpening::getAcademicYear))
                            .collect(Collectors.toList());
                    
                    for (CourseOpening opening : filteredOpenings) {
                        System.out.print(opening.getCourseCode() + "|" + courses.stream()
                                .filter(c -> c.getCode().equals(opening.getCourseCode()))
                                .findFirst().get().getName() + "|" + courses.stream()
                                .filter(c -> c.getCode().equals(opening.getCourseCode()))
                                .findFirst().get().getCredit() + "|" +
                                courses.stream().filter(c -> c.getCode().equals(opening.getCourseCode()))
                                        .findFirst().get().getPassingGrade() + "|" + opening.getAcademicYear() + "|" + opening.getSemester() + "|");
                        
                        List<Lecturer> courseLecturers = opening.getLecturers(); // Mengambil daftar pengajar dari CourseOpening
                        boolean firstLecturerPrinted = false;
                        for (Lecturer lecturer : courseLecturers) {
                            if (!firstLecturerPrinted) {
                                System.out.print(lecturer.getInitial() + " (" + lecturer.getEmail() + ")");
                                firstLecturerPrinted = true;
                            }
                        }
                        
                        if (!firstLecturerPrinted) {
                            System.out.print("No lecturer assigned");
                        }
                        
                        System.out.println();
                        
                        for (Enrollment enr : enrollments) {
                            if (enr.getCode().equals(opening.getCourseCode()) && enr.getAcademicYear().equals(opening.getAcademicYear()) && enr.getSemester().equals(opening.getSemester())) {
                                if (enr.getPreviousGrade().equals("") || enr.getPreviousGrade().equals("null")) {
                                    System.out.println(enr.getCode() + "|" + enr.getId() + "|" + enr.getAcademicYear() + "|" + enr.getSemester() + "|" + enr.getGrade());
                                } else if (!enr.getPreviousGrade().equals("") && !enr.getPreviousGrade().equals("null")) {
                                    System.out.println(enr.getCode() + "|" + enr.getId() + "|" + enr.getAcademicYear() + "|" + enr.getSemester() + "|" + enr.getGrade() + "(" + enr.getPreviousGrade() + ")");
                                }
                            }
                        }
                    }
                    break;
                
                
                
            case "enrollment-remedial":
                for (Enrollment enrollment : enrollments) {
                    if (enrollment.getCode().equals(command[1]) && 
                        enrollment.getId().equals(command[2]) &&
                        enrollment.getAcademicYear().equals(command[3]) &&
                        enrollment.getSemester().equals(command[4])) {
                        if (enrollment.getGrade().equals("None")) {
                            break;
                        } else {
                            if (enrollment.getTotal() == 0) {
                                enrollment.setPreviousGrade(command[5]);
                                enrollment.swapGrade();
                                enrollment.setTotal();
                            } else {
                                String previousGrade = enrollment.getPreviousGrade();
                                enrollment.setRemedialGrade(previousGrade + "(" + command[5] + ")");
                            }
                        }
                    }
                }
        }
    }

        
                
        for (Lecturer lecturer : lecturers) {
            System.out.println(lecturer);
        }
        
        for (Course course : courses) {
            System.out.println(course.getCode() + "|" + course.getName() + "|" + course.getCredit() + "|" + course.getPassingGrade());
        }

        for (Student student : students) {
            System.out.println(student);
        }                  

        for (Enrollment enrollment : enrollments) {
            if(enrollment.getPreviousGrade().equals("")){
                System.out.println(enrollment.getCode() + "|" + enrollment.getId() + "|" + enrollment.getAcademicYear() + "|" + enrollment.getSemester() + "|" + enrollment.getGrade());
            } else {
                System.out.println(enrollment.getCode() + "|" + enrollment.getId() + "|" + enrollment.getAcademicYear() + "|" + enrollment.getSemester() + "|" + enrollment.getGrade()+"("+enrollment.getPreviousGrade()+")");
            }
        } 
    }

    private static boolean isDuplicateCourse(Course course, List<Course> courses) {
        return courses.stream().anyMatch(c -> c.getCode().equals(course.getCode()));
    }

    private static boolean isDuplicateStudent(Student student, List<Student> students) {
        return students.stream().anyMatch(s -> s.getId().equals(student.getId()));
    }

    private static boolean isDuplicateLecturer(Lecturer lecturer, List<Lecturer> lecturers) {
        return lecturers.stream().anyMatch(e -> e.getName().equals(lecturer.getName()));
    }

    private static Enrollment findEnrollment(String courseCode, String studentId, String academicYear, String semester, List<Enrollment> enrollments) {
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getCode().equals(courseCode) && 
                enrollment.getId().equals(studentId) &&
                enrollment.getAcademicYear().equals(academicYear) &&
                enrollment.getSemester().equals(semester)) {
                return enrollment;
            }
        }
        return null;
    }

private static double calculateGPA(String studentId, List<Enrollment> enrollments, List<Course> courses) {
    Map<String, String> latestGrades = new HashMap<>();

    // Mengumpulkan nilai terakhir untuk setiap mata kuliah yang diulang
    for (Enrollment enrollment : enrollments) {
        if (enrollment.getId().equals(studentId) && enrollment.getGrade() != null && !enrollment.getGrade().equals("None")) {
            latestGrades.put(enrollment.getCode(), enrollment.getGrade());
        }
    }

    double totalScore = 0;
    int totalCredits = 0;

    // Menghitung total skor dan total kredit menggunakan nilai terakhir untuk setiap mata kuliah
    for (String courseCode : latestGrades.keySet()) {
        String latestGrade = latestGrades.get(courseCode);
        for (Course course : courses) {
            if (course.getCode().equals(courseCode)) {
                totalScore += convertGradeToScore(latestGrade) * course.getCredit();
                totalCredits += course.getCredit();
                break;
            }
        }
    }

    if (totalCredits == 0) {
        return 0;
    }

    return totalScore / totalCredits;
}
        
      
private static int calculateTotalCredits(String studentId, List<Enrollment> enrollments, List<Course> courses) {
    Set<String> enrolledCourses = new HashSet<>();
    int totalCredits = 0;

    // Mengumpulkan semua mata kuliah yang diambil oleh mahasiswa
    for (Enrollment enrollment : enrollments) {
        if (enrollment.getId().equals(studentId)) {
            enrolledCourses.add(enrollment.getCode());
        }
    }

    // Menghitung total kredit hanya untuk setiap mata kuliah yang diambil, tidak termasuk kursus yang diulang
    for (String courseCode : enrolledCourses) {
        for (Course course : courses) {
            if (course.getCode().equals(courseCode)) {
                totalCredits += course.getCredit();
                break;
            }
        }
    }

    return totalCredits;
}
       
        private static double convertGradeToScore(String grade) {
        switch (grade) {
            case "A":
                return 4.0;
            case "AB":
                return 3.5;
            case "B":
                return 3.0;
            case "BC":
                return 2.5;
            case "C":
                return 2.0;
            case "D":
                return 1.0;
            case "E":
                return 0.0;
            default:
                return 0.0;
        }
    }
}

 
