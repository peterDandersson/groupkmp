package jpa;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name = "getStudentAttendance", query="SELECT a FROM Attendance a " +
                "WHERE a.day.date = :date " +
                "AND a.studentCourse.course.id = :course_id " +
                "AND a.studentCourse.student.id = :student_id")
})
public class Attendance {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne //(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "student_course_id")
    private StudentCourse studentCourse;

    @ManyToOne //(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "day_id")
    private Day day;

    private boolean present;

    public Attendance() {}

    public Attendance(StudentCourse studentCourse, Day day) {
        setStudentCourse(studentCourse);
        setDay(day);
        setPresent(false);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentCourse getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(StudentCourse studentCourse) {
        this.studentCourse = studentCourse;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
