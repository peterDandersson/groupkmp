package ejb;

import domain.StudentComparator;
import jpa.*;
import lib.StatusCode;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static lib.Helpers.truncateDate;
import static lib.StatusCode.*;


@Local
@Stateless
public class CourseService {
    @PersistenceContext
    EntityManager em;

    @EJB
    StudentService studentService;

    public String courseToString() {
        return this.toString();
    }

    public Long createCourse(String courseName, String description){
        Date date = new Date();
        Long id = createCourse(courseName, description, date, date, 2);
        return id;
    }

    public Long createCourse(String courseName, String description, Date startDate, Date endDate, int maxStudents) {
        Course course = new Course(courseName, description, startDate, endDate, maxStudents);
        em.persist(course);
        return course.getId();
    }

    public Course getCourse(Long id) {
        //Course course = (Course) em.createNamedQuery("getCourse").setParameter("id", id).getSingleResult();
        Course course = em.find(Course.class, id);
        return course;
    }

    public List<Course> getAllCourses() {
        return em.createNamedQuery("getAllCourses").getResultList();
    }

    public List<Long> getAllCourseIds() {
        return getAllCourses().stream().map(course -> course.getId()).collect(Collectors.toList());
    }

    public void updateCourse(Long id, String courseName, String description, Date startDate, Date endDate, int maxStudents) {
        Course course = em.find(Course.class, id);
        course.setCourseName(courseName);
        course.setDescription(description);
        course.setStartDate(startDate);
        course.setEndDate(endDate);
        course.setMaxStudents(maxStudents);
        em.merge(course);
    }

    public void removeCourse(Long id) {
        Course course = getCourse(id);
        em.remove(course);
    }

    public Date getEndDate(Long courseId) {
        Course course = getCourse(courseId);
        return course.getEndDate();
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
        students.sort(new StudentComparator());
        return students;
    }

    public boolean isStudentRegistered(Long courseId, Long studentId) {
        Course course = getCourse(courseId);
        return course.isStudentOnCourse(studentId);
    }

    public boolean isDateAfterStart(Long courseId, Date date) {
        Course course = getCourse(courseId);
        return course.isDateAfterStartDate(date);
    }

    public boolean isDateAfterEnd(Long courseId, Date date) {
        Course course = getCourse(courseId);
        return course.isDateAfterEndDate(date);
    }

    public boolean isCourseCurrent(Long courseId, Date date) {
        Course course = getCourse(courseId);
        return course.isCourseCurrentOn(date);
    }

    public Date getLeavingDate(Long courseId, Long studentId) {
        Course course = getCourse(courseId);
        StudentCourse studentCourse = course.getStudentCourse(studentId);
        if (studentCourse == null) {
            //return STUDENT_NOT_ON_COURSE;
            return null;
        }
        else {
            return studentCourse.getEndDate();
        }
    }

    public StatusCode registerForCourse(Long courseId, Long studentId) {
        Course course = getCourse(courseId);
        Student student = studentService.getStudent(studentId);

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

    private void deregisterFromCourse(Course course, Student student) {
        StudentCourse studentCourse = student.getStudentCourse(course);
        Set<Attendance> attendances = studentCourse.getAttendances();
        for (Attendance attendance : attendances) {
            em.remove(attendance);
        }
        em.merge(student);
        em.merge(course);
        StudentCourse sc = em.merge(studentCourse);
        em.remove(sc);
    }

    private void leaveCourse(Course course, Student student) {
        StudentCourse studentCourse = student.getStudentCourse(course);
        studentCourse.setEndDate(truncateDate(new Date()));
        em.merge(studentCourse);
    }

    /**
     * Deregisters the student from the course if the course is yet to start.
     * Student leaves the course if the course is running.
     * @param course_id
     * @param studentId
     * @return
     */
    public StatusCode leaveOrDeregisterFromCourse(Long course_id, Long studentId) {
        Course course = getCourse(course_id);
        Student student = studentService.getStudent(studentId);

        if (student.isRegisteredForCourse(course)) {
            if (course.isCourseCurrentOn(new Date())) {
                leaveCourse(course, student);
                return LEFT_COURSE;
            }
            else if (!course.isDateAfterStartDate(new Date())) { // The course hasn't begun yet.
                deregisterFromCourse(course, student);
                return DEREGISTERED_FROM_COURSE;
            }
            return COURSE_ENDED;
        }
        else {
            return STUDENT_NOT_ON_COURSE;
        }
    }
}
