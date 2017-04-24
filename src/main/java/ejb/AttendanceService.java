package ejb;


import domain.AttendanceComparator;
import jpa.Attendance;
import jpa.Course;
import jpa.Day;
import jpa.StudentCourse;
import org.eclipse.persistence.annotations.TimeOfDay;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static lib.Helpers.truncateDate;

@Local
@Stateless
public class AttendanceService {
    @PersistenceContext
    EntityManager em;

    @EJB
    CourseService courseService;

    private Day createDay(Course course, Date date) {
        Day day = new Day(course, date);
        course.addDay(day);
        em.persist(day);
        em.merge(course);
        return day;
    }

    private Attendance createAttendance(StudentCourse studentCourse, Day day) {
        Attendance attendance = new Attendance(studentCourse, day);
        studentCourse.addAttendance(attendance);
        day.addAttendance(attendance);
        em.persist(attendance);
        em.merge(studentCourse);
        em.merge(day);
        return attendance;
    }

    public Day getOrCreateDay(Long courseId, Date date) {
        Course course = courseService.getCourse(courseId);
        return getOrCreateDay(course, date);
    }

    private Day getOrCreateDay(Course course, Date date) {
        Day day = course.getDay(date);
        if (day == null) {
            day = createDay(course, date);
        }
        return day;
    }

    public void createAllAttendanceRecords(Long courseId, Date date) {
        Day day = getOrCreateDay(courseId, date);
        Course course = courseService.getCourse(courseId);
        for(StudentCourse studentCourse : course.getStudentCourses()) {
            getOrCreateAttendance(studentCourse, day);
        }
    }

    public Attendance getOrCreateAttendance(Long courseId, Long studentId, Date date) {
        Course course = courseService.getCourse(courseId);
        StudentCourse studentCourse = course.getStudentCourse(studentId);
        Attendance attendance = studentCourse.getAttendance(date);
        if (attendance == null) {
            Day day = getOrCreateDay(course, date);
            attendance = createAttendance(studentCourse, day);
        }
        return attendance;
    }

    private Attendance getOrCreateAttendance(StudentCourse studentCourse, Day day) {
        Attendance attendance = studentCourse.getAttendance(day.getDate());
        if (attendance == null) {
            attendance = createAttendance(studentCourse, day);
        }
        return attendance;
    }

    public List<Attendance> getAttendances(Long courseId, Date date) {
        Course course = courseService.getCourse(courseId);
        Day day = getOrCreateDay(course, date);

        // TODO: Use namedquery instead of stream.
        List<Attendance> attendances = day.getAttendances()
                .stream()
                .filter(d -> d.getStudentCourse().getCourse().getId().equals(courseId))
                .collect(Collectors.toList());

        attendances.sort(new AttendanceComparator());
        return attendances;
    }

    public List<Boolean> getPrestentList(Long courseId, Date date) {
        return getAttendances(courseId, date)
                .stream()
                .map(a -> a.isPresent())
                .collect(Collectors.toList());
    }



    public void setAttendances(Long courseId, Date date, List<Boolean> presentList) {
        List<Attendance> attendances = getAttendances(courseId, date);
        for (int i=0; i<attendances.size(); i++) {
            Attendance attendance = attendances.get(i);
            attendance.setPresent(presentList.get(i));
            em.merge(attendance);
        }
    }

}
