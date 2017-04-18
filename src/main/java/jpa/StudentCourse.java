package jpa;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@NamedQueries({
/*        @NamedQuery(
                name = "getStudentCourse",
                query = "SELECT sc FROM StudentCourse sc " +
                        "WHERE sc.student_id = :student_id " +
                        "AND sc.course_id = :course_id"
        ),*/
/*        @NamedQuery(
                name = "getStudentCourse2",
                query = "SELECT * FROM StudentCourse sc" +
                        "WHERE sc.student_id = :student_id " +
                        "AND sc.course_id = :course_id"
        ),*/
})
public class StudentCourse {
    @Id
    @GeneratedValue
    private Long id;
/*
    //@ManyToOne(cascade = CascadeType.PERSIST)
    @ManyToOne
    private Student student;

    //@ManyToOne(cascade = CascadeType.PERSIST)

    @ManyToOne
    private Course course;

    //private Long courses_id;
    //private Long student_id;

    private boolean isActive;*/

    @ManyToOne //(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne //(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "active")
    private boolean active;

    // The date a student ends the course if it is before the course's end date.
    private Date endDate;

    @OneToMany(mappedBy = "studentCourse")
    private Set<Attendance> attendances = new HashSet<>();

    //private Set<Attendance> attendance;

    public StudentCourse() {
    }

    public StudentCourse(Student student, Course course) {
        setStudent(student);
        setCourse(course);
        setActive(true);
    }

/*    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }*/

/*    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }*/

/*    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Set<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(Set<Attendance> attendances) {
        this.attendances = attendances;
    }
}
