package ejb;

import domain.CourseComparator;
import jpa.*;
import jpa.Student;
import lib.StatusCode;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static lib.StatusCode.*;

@Local
@Stateless
public class StudentService {
/*    public static final enum TRANSACTION_CODE = {
        "COURSE_FULL", "DUP_STUDENT", "REGISTERED"
    };*/

    @PersistenceContext
    EntityManager em;

    @EJB
    UserService userService;

    @EJB
    CourseService courseService;

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
        Student student = getStudent(id); // em.find(Student.class, id);
        student.setEmail(email);
        student.setPassword(password);
        em.merge(student);
    }

    public void removeStudent(Long id) {
        userService.removeUser(id);
    }

    public List<Course> getCourses(Student student) {
        //Student student = getStudent(id);
        //Set<Long> courses = student.getCourseIds();
        Set<Course> courses = student.getCourses();
        return courses.stream().sorted(new CourseComparator()).collect(Collectors.toList());
        //return student.getCourses();
    }

    public List<Course> getOtherCourses(Student student) {
        Set<Long> registeredCourseIds = student.getCourseIds();
        Set<Long> allCourseIds = courseService.getAllCourseIds();
        allCourseIds.removeAll(registeredCourseIds);
        //allCourses.stream().sorted(new CourseComparator());
        return allCourseIds.stream()
                //.map(id -> new CourseDomain(courseService.getCourse(id)))
                .map(id -> courseService.getCourse(id))
                .filter(course -> !course.isFull())
                .sorted(new CourseComparator())
                .collect(Collectors.toList());
    }

    public StatusCode registerForCourse(Student student, Long course_id) {
        // check capacity of course not exceeded;
        Course course = courseService.getCourse(course_id);

        if (student.isRegisteredForCourse(course)) {
            return ALREADY_REGISTERED;
        }
        else if (course.isFull()) {
            return COURSE_FULL;
        }
        else {
            StudentCourse studentCourse = new StudentCourse(student, course);
            student.addStudentCourse(studentCourse);
            course.addStudentCourse(studentCourse);
            em.persist(studentCourse);
            em.merge(student);
            em.merge(course);
            return SUCCESSFULLY_REGISTERED;
        }

    }

    public StatusCode deregisterFromCourse(Student student, Long course_id) {
        Course course = courseService.getCourse(course_id);

        if (student.isRegisteredForCourse(course)) {
            StudentCourse studentCourse = student.getStudentCourse(course);
            student.removeStudentCourse(studentCourse);
            course.removeStudentCourse(studentCourse);
            em.merge(student);
            em.merge(course);
            StudentCourse sc = em.merge(studentCourse);
            em.remove(sc);
            return DEREGISTERED;
        }
        else {
            return STUDENT_NOT_ON_COURSE;
        }

    }
}
