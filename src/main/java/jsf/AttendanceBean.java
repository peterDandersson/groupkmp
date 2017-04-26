package jsf;

import ejb.AttendanceService;
import ejb.CourseService;
import javafx.util.Pair;
import jpa.Attendance;
import jpa.Course;
import jpa.Student;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static lib.Helpers.dateToString;
import static lib.Helpers.notification;
import static lib.Helpers.truncateDate;

@ManagedBean
@SessionScoped
public class AttendanceBean implements Serializable {

    @EJB
    AttendanceService attendanceService;

    @EJB
    CourseService courseService;

    private Long courseId;
    private Date date = new Date();
    private List<Boolean> attendances;
    private List<Boolean> courseSelectionList;

    private boolean test = false;

    public void setTest(boolean test) {
        this.test = test;
    }

    @PostConstruct
    public void init() {
        List<Course> courses = courseService.getAllCourses();
        if (courses.size() > 0) {
            setCourseId(courses.get(0).getId());
        }
    }

    public void setSelections() {
        courseSelectionList = new ArrayList<>();
        for (Long courseId : courseService.getAllCourseIds()) {
            courseSelectionList.add(courseId.equals(getCourseId()));
        }
    }

    public void setSelections(List<Boolean> selections) {
        this.courseSelectionList = selections;
    }

    public List<Boolean> getSelections() {
        return courseSelectionList;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseService.getCourse(getCourseId()).getCourseName();
    }

    public Date getDate() {
        return date;
    }

    public String getDateStr() {
        return dateToString(date);
    }

    public void setDate(Date date) {
        if (!date.after(new Date()) && getCourseId()!=null
                && courseService.isCourseCurrent(getCourseId(), date)) {
            this.date = date;
        }
    }

    public List<Boolean> getAttendances() {
/*        if (attendances==null) {
            fetchAttendances();
        }*/
        return attendances;
    }

    public void setAttendances(List<Boolean> attendances) {
        this.attendances = new ArrayList<>();
        for (Boolean attendance : attendances) {
            this.attendances.add(attendance);
        }
        //this.attendances = attendances;
    }

    public void takeAttendance(Long courseId) {
        System.out.println("================== Take attendance 1================");
        setCourseId(courseId);
        System.out.println(getCourseId());
        //Day day = attendanceService.getOrCreateDay(course_id, new Date());
        takeAttendance();
        //return "attendance";
    }

    public void takeAttendance() {
        System.out.println("================== Take attendance 2================");
        //Day day = attendanceService.getOrCreateDay(course_id, new Date());
        attendanceService.createAllAttendanceRecords(getCourseId(), getDate());
        setAttendances(fetchAttendances());
        //debugAttendances();
        setSelections();
        //debugSelections();
        //return "attendance";
    }

    public List<Student> getStudents() {
        if (getCourseId() == null) {
            return new ArrayList<>();
        }
        return courseService.getStudents(getCourseId());
    }

    public List<Pair<Student, Integer>> getStudentCounter() {
        if (getCourseId() == null) {
            return new ArrayList<>();
        }
        List<Pair<Student, Integer>> psi = new ArrayList<>();
        Integer counter = 0;
        for (Student student : courseService.getStudents(getCourseId())) {
            Pair<Student, Integer> pair = new Pair<>(student, counter);
            psi.add(pair);
            counter++;
        }
        return psi;
    }

    public List<Boolean> fetchAttendances() {
        return attendanceService.getPrestentList(getCourseId(), getDate());
    }

    public String saveAttendance() {
        System.out.println("saveAttendance");
        if (courseService.isCourseCurrent(getCourseId(), getDate())) {
            attendanceService.setAttendances(getCourseId(), getDate(), getAttendances());
            notification(FacesMessage.SEVERITY_INFO, "Saved", "");
        }
        else {
            notification(FacesMessage.SEVERITY_ERROR, "Error", "Course not running on this date.");
        }
        return "attendance";
    }

    public void onDateSelect(SelectEvent event) {
        System.out.println("======= date select ====== ");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date date = (Date) event.getObject();
        if (truncateDate(date).after(new Date())) {
            notification(FacesMessage.SEVERITY_ERROR, format.format(event.getObject()),
                    "Selected date is in the future." );
        }
        else if(!courseService.isDateAfterStart(getCourseId(), date)) {
            notification(FacesMessage.SEVERITY_ERROR, format.format(event.getObject()),
                    "The course has not begun on the selected date." );
        }
        else if(courseService.isDateAfterEnd(getCourseId(), date)) {
            notification(FacesMessage.SEVERITY_ERROR, format.format(event.getObject()),
                    "The course has ended on the selected date." );
        }
        else {
            setDate(date);
            takeAttendance();
            notification(FacesMessage.SEVERITY_INFO, format.format(event.getObject()), "Taking attendance.");
        }


    }

/*    private void debugAttendances() {
        System.out.println("=======");
        for (Boolean attendance : attendances) {
            System.out.println(attendance);
        }
        System.out.println("=======");
    }

    private void debugSelections() {
        System.out.println("======= Selection " + getSelections().size());
        for (Boolean selection : getSelections()) {
            System.out.println("-> " + selection);
        }
        System.out.println("======= Selection");
    }*/

}
