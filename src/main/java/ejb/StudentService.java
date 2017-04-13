package ejb;

import jpa.Admin;
import jpa.Student;
import jpa.Teacher;
import jpa.Student;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Local
@Stateless
public class StudentService {

    @PersistenceContext
    EntityManager em;

    @EJB
    UserService userService;

    public Long createStudent(String email, String password) {
        return userService.createUser(email, password, "STUDENT");
    }

    public Student getStudent(Long id) {
        return (Student) userService.getUser(id);
    }

    public Student getFirstStudent() {
        TypedQuery<Student> query = em.createNamedQuery("getStudents", Student.class);
        Student student = query.getResultList().get(0);
        return student;
    }

    public List<Student> getStudents() {
        TypedQuery<Student> query = em.createNamedQuery("getStudents", Student.class);
        List<Student> students = query.getResultList();
        return students;
    }


    public void removeStudent(Long id) {
        userService.removeUser(id);
    }
}
