package ejb;

import jpa.*;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Local
@Stateless
public class CourseService {
    @PersistenceContext
    EntityManager em;

    public String courseToString() {
        return this.toString();
    }

    public Long createCourse(){

        Long id = createCourse("test");
        return id;
    }

    public Long createCourse(String name){
        Course course = null;
        em.persist(course);
        return course.getId();
    }

    public Long countCourses() {
        List<Long> c = em.createNamedQuery("countCourses").getResultList();
        Long i = c.get(0);
        return i;
    }

    public Course getCourse(Long id) {
        Course user = (Course) em.createNamedQuery("getCourse").setParameter("id", id).getSingleResult();
        return user;
    }

}
