package ejb;

import jpa.*;
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

    public void updateStudent(Long id, String email, String password) {
        Student student = em.find(Student.class, id);
        student.setEmail(email);
        student.setPassword(password);
        em.merge(student);
    }

    public void removeStudent(Long id) {
        userService.removeUser(id);
    }

    public void registerForCourse(Student student, Course course) {
        // check capacity of course not exceeded;


/*        StudentCourse studentCourse = new StudentCourse(student, course);
        student.getStudentCourses().add(studentCourse);
        em.merge(student);*/
        for(StudentCourse sc : student.getStudentCourses()) {
            System.out.println(sc.getCourse().getCourseName());
        }
        //System.out.println(student.getStudentCourses());
    }
}
