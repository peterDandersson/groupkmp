package jsf;

import ejb.AttendanceService;
import ejb.CourseService;
import jpa.Attendance;
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
    Date date;
    List<Attendance> attendances;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        System.out.println("########################################## SET ATTENDANCE #######");
        this.attendances = attendances;
    }

    public String takeAttendance(Long course_id) {
        setCourseId(course_id);
        //Day day = attendanceService.getOrCreateDay(course_id, new Date());
        attendanceService.createAllAttendanceRecords(course_id, new Date());
        setAttendances(fetchAttendances());
        return "attendance";
    }

    public List<Student> getStudents() {
        if (getCourseId() == null) {
            return new ArrayList<>();
        }
        return courseService.getStudents(getCourseId());
    }

    public List<Attendance> fetchAttendances() {
        setDate(new Date()); // Temporary - date will eventually be provided by a calendar.
        return attendanceService.getAttendances(getCourseId(), getDate());
    }
}
