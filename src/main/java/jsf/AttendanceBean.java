package jsf;

import ejb.AttendanceService;
import ejb.CourseService;
import javafx.util.Pair;
import jpa.Course;
import jpa.Student;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static lib.Helpers.dateToString;

@ManagedBean
@ViewScoped
public class AttendanceBean implements Serializable {

    @EJB
    AttendanceService attendanceService;

    @EJB
    CourseService courseService;

/*    @ManagedProperty(value="#{courseBean}")
    private CourseBean courseBean;*/

    private Long courseId;
    private Date date = new Date();
    private List<Boolean> attendances;
    private List<Boolean> courseSelection;

    @PostConstruct
    public void init() {
        List<Long> courseIds = courseService.getAllCourseIds();
        if (courseIds.size() > 0) {
            setCourseId(courseIds.get(0));
        }
        setCourseSelection();
    }

    public void setCourseSelection() {
        System.out.println("########################################## SET SELECTIONS #######");
        courseSelection = new ArrayList<>();
        for (Long courseId : courseService.getAllCourseIds()) {
            courseSelection.add(courseId == getCourseId());
        }
    }

    public List<Boolean> getCourseSelection() {
        return courseSelection;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Date getDate() {
        return date;
    }

    public String getDateStr() {
        return dateToString(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Boolean> getAttendances() {
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
        //debugAttendances();
        setCourseSelection();
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
        setDate(getDate()); // Temporary - date will eventually be provided by a calendar.
        return attendanceService.getPrestentList(getCourseId(), getDate());
    }

    public String saveAttendance() {
        debugAttendances();
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
        System.out.println("======= Selection " + getCourseSelection().size());
        for (Boolean selection : getCourseSelection()) {
            System.out.println("-> " + selection);
        }
        System.out.println("======= Selection");
    }

    public void onDateSelect(SelectEvent event) {
        System.out.println("======= date select ====== ");

        //FacesContext facesContext;
        //facesContext = FacesContext.getCurrentInstance();
        Date date = (Date) event.getObject();
        //System.out.println(date);
        setDate(date);
        //System.out.println(" 5555555555555  "+getCourseId());
        takeAttendance();
        //SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        //facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }

    public String getRowClass(int index, String context) {
        //System.out.println("99999999999999999999999999999999999999999999" + index);
        return context=="attendance" && getCourseSelection().get(index) ? "selected-row" :
                (index % 2 == 0) ? "even-row" : "odd-row";
    }

}
