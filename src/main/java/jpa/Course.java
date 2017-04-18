package jpa;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Optional;
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
    private java.util.Date startDate;
    private java.util.Date endDate;
    private int maxStudents;


    //@OneToMany(mappedBy = "course", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @OneToMany(mappedBy = "course")
    private Set<StudentCourse> studentCourses = new HashSet<StudentCourse>();

    @OneToMany(mappedBy = "course")
    private Set<Day> days = new HashSet<Day>();

    public Course() {
    }

    public Course(String courseName, String description, java.util.Date startDate, java.util.Date endDate, int maxStudents) {
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

    public java.util.Date getStartDate() {
        return startDate;
    }

    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }

    public java.util.Date getEndDate() {
        return endDate;
    }

    public void setEndDate(java.util.Date endDate) {
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
        Set updatedStudentCourses = getStudentCourses()
                .stream()
                .filter(sc -> sc.getId() != studentCourse.getId())
                .collect(Collectors.toSet());
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

    public Set<Day> getDays() {
        return days;
    }

    public Day getDay(java.util.Date date) {
        long millis = date.toInstant().toEpochMilli();
        java.sql.Date sqlDate = new java.sql.Date(millis);
        //java.util.Date d  = new SimpleDateFormat();
        SimpleDateFormat dd  = new SimpleDateFormat("y M d");
        java.util.Date truncated;
        try {
            truncated = dd.parse(dd.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //d.applyLocalizedPattern("");
        //date.
        Optional<Day> day = days
                .stream()
                .filter(d -> d.getDate().equals(sqlDate))
                .findFirst();
        return day.isPresent() ? day.get() : null;
    }

    public void setDays(Set<Day> days) {
        this.days = days;
    }

    public void addDay(Day day) {
        days.add(day);
    }
}
