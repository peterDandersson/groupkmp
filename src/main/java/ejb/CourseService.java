package ejb;

import domain.StudentComparator;
import jpa.*;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        Long id = createCourse(courseName, description, date, date, 2);
        return id;
    }

    public Long createCourse(String courseName, String description, Date startDate, Date endDate, int maxStudents){
        Course course = new Course(courseName, description, startDate, endDate, maxStudents);
        em.persist(course);
        return course.getId();
    }


    public Course getCourse(Long id) {
        //Course course = (Course) em.createNamedQuery("getCourse").setParameter("id", id).getSingleResult();
        Course course = em.find(Course.class, id);
        return course;
    }

/*    public Course1Domain getCourseDomain(Long id) {
        Course course = (Course) em.createNamedQuery("getCourse").setParameter("id", id).getSingleResult();
        return new Course1Domain(course);
    }*/

    public List<Course> getAllCourses() {
        return em.createNamedQuery("getAllCourses").getResultList();
    }

    public Set<Long> getAllCourseIds() {
        return getAllCourses().stream().map(course -> course.getId()).collect(Collectors.toSet());
    }

    public void updateCourse(Long id, String courseName, String description) {
        Course course = em.find(Course.class, id);
        course.setCourseName(courseName);
        course.setDescription(description);
        em.merge(course);
    }

    public void removeCourse(Long id) {
        Course course = getCourse(id);
        em.remove(course);
    }

    public boolean isFull(Long id) {
        Course course = getCourse(id);
        return course.isFull();
    }

    public Long countCourses() {
        List<Long> c = em.createNamedQuery("countCourses").getResultList();
        Long i = c.get(0);
        return i;
    }

    public List<Student> getStudents(Long course_id) {
        Course course = getCourse(course_id);
        List<Student> students = new ArrayList<>(course.getStudents());
        students.stream().sorted(new StudentComparator());
        return students;
    }

}
