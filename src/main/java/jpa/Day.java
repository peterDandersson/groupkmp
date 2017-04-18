package jpa;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Day {

    @Id
    @GeneratedValue
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    //private Set<Attendance> attendances;

    public Day() {}

    public Day(Course course, Date date) {
        setCourse(course);
        setDate(date);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

}
