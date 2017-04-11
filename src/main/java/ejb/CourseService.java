package ejb;

import jpa.*;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Local
@Stateless
public class CourseService {
    @PersistenceContext
    EntityManager em;

    public String courseToString() {
        return this.toString();
    }

    public Long createCourse(String courseName, String description){
        Date date = new Date();
        Long id = createCourse(courseName, description, date, date, 20);
        return id;
    }

    public Long createCourse(String courseName, String description, Date startDate, Date endDate, int maxStudents){
        Course course = new Course(courseName, description, startDate, endDate, maxStudents);
        em.persist(course);
        return course.getId();
    }

    public Long countCourses() {
        List<Long> c = em.createNamedQuery("countCourses").getResultList();
        Long i = c.get(0);
        return i;
    }

    public Course getCourse(Long id) {
        Course course = (Course) em.createNamedQuery("getCourse").setParameter("id", id).getSingleResult();
        return course;
    }

    public List<Course> getAllCourses() {
        return em.createNamedQuery("getAllCourses").getResultList();
    }

}
