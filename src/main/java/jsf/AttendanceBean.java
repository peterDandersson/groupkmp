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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static lib.Helpers.dateToString;
import static lib.Helpers.truncateDate;

@ManagedBean
@ViewScoped
public class AttendanceBean implements Serializable {

    @EJB
    AttendanceService attendanceService;

    @EJB
    CourseService courseService;

    private Long courseId;
    private Date date = new Date();
    private List<Boolean> attendances;
    private List<Boolean> selections;

    private boolean test = false;

    public void setTest(boolean test) {
        this.test = test;
    }

    @PostConstruct
    public void init() {
        //getCourseBean().getAllCourses();
        List<Course> courses = courseService.getAllCourses();
        if (courses.size() > 0) {
            setCourseId(courses.get(0).getId());
        }
    }

    public void setSelections() {
        System.out.println("########################################## SET SELECTIONS #######");
        selections = new ArrayList<>();
        for (Long courseId : courseService.getAllCourseIds()) {
            selections.add(courseId.equals(getCourseId()));
        }
    }

    public List<Boolean> getSelections() {
        return selections;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        System.out.println("ttttttttttttttttttttttttttttttttttttttttt setting course id ffffffffffffffffffffffffffff " + courseId);
        this.courseId = courseId;
    }

    public Date getDate() {
        return date;
    }

    public String getDateStr() {
        return dateToString(date);
    }

    public void setDate(Date date) {
        System.out.println("test");
        if (!date.after(new Date())) {
            this.date = date;
        }
    }

    public List<Boolean> getAttendances() {
        if (attendances==null) {
            fetchAttendances();
        }
        return attendances;
    }

    public void setAttendances(List<Boolean> attendances) {
        this.attendances = attendances;
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
        debugAttendances();
        setSelections();
        debugSelections();
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
        //setDate(getDate()); // Temporary - date will eventually be provided by a calendar.
        return attendanceService.getPrestentList(getCourseId(), getDate());
    }

    public String saveAttendance() {
        //debugAttendances();
        attendanceService.setAttendances(getCourseId(), getDate(), getAttendances());
        return "attendance";
    }

    private void debugAttendances() {
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
    }

    public void onDateSelect(SelectEvent event) {
        System.out.println("======= date select ====== ");

        FacesContext facesContext;
        facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date date = (Date) event.getObject();
        if (!truncateDate(date).after(new Date())) {
            setDate(date);
            takeAttendance();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, format.format(event.getObject()), "Taking attendance."));
        }
        else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not allowed", format.format(event.getObject()) + " is in the future." ));
        }

    }

}
