package jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Attendance {

    @Id
    @GeneratedValue
    private Long id;

/*    @OneToOne
    private StudentCourse student;*/
  
    //private Day day;

    private boolean present;
}
