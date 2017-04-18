package jpa;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NamedQueries({
        @NamedQuery(name = "getCourse", query="SELECT c FROM Course c WHERE c.id = :id"),
        @NamedQuery(name = "getAllCourses", query="SELECT c FROM Course c ORDER BY c.courseName"),
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


    //@OneToMany(mappedBy = "course", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @OneToMany(mappedBy = "course")
    private Set<StudentCourse> studentCourses = new HashSet<StudentCourse>();

    public Course() {
    }

    public Course(String courseName, String description, Date startDate, Date endDate, int maxStudents) {
        this.courseName = courseName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxStudents = maxStudents;
    }

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

    public Set<StudentCourse> getStudentCourses() {
        return studentCourses;
    }

    public void setStudentCourses(Set<StudentCourse> studentCourses) {
        this.studentCourses = studentCourses;
    }

    public void addStudentCourse(StudentCourse studentCourse) {
        studentCourses.add(studentCourse);
    }

    public void removeStudentCourse(StudentCourse studentCourse) {
        Set updatedStudentCourses = getStudentCourses().
                stream().
                filter(sc -> sc.getId() != studentCourse.getId()).
                collect(Collectors.toSet());
        setStudentCourses(updatedStudentCourses);
    }

    public int getStudentCount() {
        return studentCourses.size();
    }

    public boolean isFull() {
        return getStudentCount() >= getMaxStudents();
    }

    public Set<Student> getStudents() {
        return getStudentCourses().stream().map(sc -> sc.getStudent()).collect(Collectors.toSet());
    }
}
