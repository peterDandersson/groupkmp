package jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Course {
    @Id
    @GeneratedValue
    private Long id;

    private Date startDate;
    private Date endDate;


    //private Set<Day> days;
}
