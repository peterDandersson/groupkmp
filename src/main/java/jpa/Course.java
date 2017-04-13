package jpa;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(name = "getCourse", query="SELECT c FROM Course c WHERE c.id = :id"),
        @NamedQuery(name = "getAllCourses", query="SELECT c FROM Course c"),
        @NamedQuery(name = "countCourses", query="SELECT COUNT(c) FROM Course c")
})
public class Course {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 255)
    private String courseName;
    @Column(length = 3000)
    private String description;
    private Date startDate;
    private Date endDate;
    private int maxStudents;

    @OneToMany
    private Set<StudentCourse> studentCourse;

    public Course() {
    }

    public Course(String courseName, String description, Date startDate, Date endDate, int maxStudents) {
        this.courseName = courseName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxStudents = maxStudents;
    }

    //private Set<Day> days;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Set<StudentCourse> getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(Set<StudentCourse> studentCourse) {
        this.studentCourse = studentCourse;
    }

    /*    public Set<StudentCourse> getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(Set<StudentCourse> studentCourse) {
        this.studentCourse = studentCourse;
    }*/
}
