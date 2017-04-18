package ejb;


import domain.CourseDomain;
import jpa.Course;
import jpa.Day;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@Local
@Stateless
public class AttendanceService {
    @PersistenceContext
    EntityManager em;

    @EJB
    CourseService courseService;

    public Day createDay(Course course, Date date) {
        Day day = new Day(course, date);
        course.addDay(day);
        em.persist(day);
        em.merge(course);
        return day;
    }

    public Day findDay(Long course_id, Date date) {
        Course course = courseService.getCourse(course_id);
        Day day = course.getDay(date);
        if (day == null) {
            day = createDay(course, date);
        }
        return day;
    }
}
