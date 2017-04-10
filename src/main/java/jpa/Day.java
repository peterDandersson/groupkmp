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
    //private Set<Attendance> attendances;
}
