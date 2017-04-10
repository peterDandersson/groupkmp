package jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class StudentCourse {
    @Id
    @GeneratedValue
    private Long id;

    //private Student student;
    //private Course course;
    //private Set<Attendance> attendance;
    private boolean isActive;
}
