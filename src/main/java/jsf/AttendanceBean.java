package jsf;

import ejb.AttendanceService;
import ejb.CourseService;
import jpa.Day;
import jpa.Student;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean
@RequestScoped
public class AttendanceBean {

    @EJB
    AttendanceService attendanceService;

    @EJB
    CourseService courseService;

    Long courseId;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String takeAttendance(Long course_id) {
        setCourseId(course_id);
        Day day = attendanceService.findDay(course_id, new Date());
        return "attendance";
    }

    public List<Student> getStudents() {
        if (getCourseId() == null) {
            return new ArrayList<>();
        }
        return courseService.getStudents(getCourseId());
    }
}
