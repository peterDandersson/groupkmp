package jsf;

import ejb.AttendanceService;
import ejb.CourseService;
import javafx.util.Pair;
import jpa.Attendance;
import jpa.Course;
import jpa.Student;
import jpa.StudentCourse;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static lib.Helpers.*;

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


    @ManagedProperty(value="#{userBean}")
    private UserBean userBean;

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public void init() {
        List<Course> courses;
        if (userBean.isAdmin()) {
            courses = courseService.getAllCourses();
        }
        else {
            courses = userBean.getCourses();
        }
        setDate(new Date());
        if (courses.size() > 0) {
            setCourseId(courses.get(0).getId());
        }
        setCourseSelectionList();
    }

    public String refreshTakeAttendance() {
        init();
        return "/attendance?faces-redirect=true";
    }

    /**
     * courseSelectionList is a list of Booleans that makes the stars '**' follow the course selection on the
     * take attendance page.
     */
    public void setCourseSelectionList() {
        courseSelectionList = new ArrayList<>();

        List<Long> courseIds;

        if (userBean.isAdmin()) {
            courseIds = courseService.getAllCourseIds();
        } else {
            courseIds = userBean.getCourses().stream()
                    .map(course -> course.getId())
                    .collect(Collectors.toList());
        }

        for (Long courseId : courseIds) {
            courseSelectionList.add(courseId.equals(getCourseId()));
        }

    }

    /**
     * courseSelectionList is a list of Booleans that makes the stars '**' follow the course selection on the
     * take attendance page.
     * @param selections
     */
    public void setCourseSelectioLists(List<Boolean> selections) {
        this.courseSelectionList = selections;
    }

    public List<Boolean> getCourseSelectionList() {
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
        return attendances;
    }

    public void setAttendances(List<Boolean> attendances) {
        this.attendances = new ArrayList<>();
        for (Boolean attendance : attendances) {
            this.attendances.add(attendance);
        }
    }

    public void takeAttendance(Long courseId, Date date) {
        setDate(date);
        takeAttendance(courseId);
    }

    public String takeAttendanceForDate(Date date) {
        setDate(date);
        return takeAttendance();
    }

    public void takeAttendance(Long courseId) {
        setCourseId(courseId);
        takeAttendance();
    }

    public String takeAttendance() {
        attendanceService.createAllAttendanceRecords(getCourseId(), getDate());
        setAttendances(fetchAttendances());
        setCourseSelectionList();
        return "/attendance?faces-redirect=true";
    }

    public List<Student> getStudents() {
        if (getCourseId() == null) {
            return new ArrayList<>();
        }
        return courseService.getStudents(getCourseId());
    }

    public List<Pair<Student, Integer>> getStudentCounter() {
        if (getCourseId() == null || !courseService.getCourse(getCourseId()).isCourseCurrentOn(getDate())) {
            return new ArrayList<>();
        }
        List<Pair<Student, Integer>> psi = new ArrayList<>();
        Integer counter = 0;
        for (Student student : courseService.getStudents(getCourseId())) {
            Pair<Student, Integer> pair = new Pair<>(student, counter);
            Date endDate = student.getStudentCourse(getCourseId()).getEndDate();
            if (null == endDate || !endDate.before(getDate())){
                psi.add(pair);
            }
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
        return "attendance?faces-redirect=true";
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
                    "Course not started on this date." );
        }
        else if(courseService.isDateAfterEnd(getCourseId(), date)) {
            notification(FacesMessage.SEVERITY_ERROR, format.format(event.getObject()),
                    "Course ended on this date." );
        }
        else if(isAtWeekend(date)) {
            notification(FacesMessage.SEVERITY_ERROR, format.format(event.getObject()),
                    "Closed during weekends." );
        }
        else {
            setDate(date);
            takeAttendance();
            notification(FacesMessage.SEVERITY_INFO, format.format(event.getObject()), "Taking attendance.");
        }


    }

    public List<List<Date>> getCourseWeeks() {
        List<List<Date>> days = new ArrayList<>();
        List<Calendar> weeks = courseService.getCourseWeeks(courseId);
        for (Calendar cal : weeks) {
            days.add(daysInWeek(cal));
        }
        return days;
    }

    public String courseAttendanceStats(Long courseId) {
        setCourseId(courseId);
        return "/course-attendance?faces-redirect=true";
    }

    public List<Date> getWeekDays(Calendar courseWeek) {
        return daysInWeek(courseWeek);
    }

    public Long getExpectedAttendance() {
        return attendanceService.getExpectedAttendanceForDate(getCourseId(), date);
    }

    public Long getActualAttendance() {
        return attendanceService.getAttendanceForDate(getCourseId(), date);
    }

    public String getCourseAttendanceStr(Date date) {
        if (!courseService.isCourseCurrent(getCourseId(), date) || isAtWeekend(date)) return "-";
        if (date.after(new Date())) return "Future Date";
        setDate(date);
        Long actual   = attendanceService.getAttendanceForDate(getCourseId(), date);
        if (actual==null) return "Not Taken";
        return String.format("%d/%d", actual, getExpectedAttendance());
    }

    public String studentAttendance(Date date) {
        setDate(date);
        return "course-attendance-for-day?faces-redirect=true";
    }

    public List<Student> getStudentsForCourseDate() {
        return courseService.getStudents(getCourseId(), getDate());
    }

    public boolean isStudentPresent(Student student) {
        return attendanceService.isStudentPresent(getCourseId(), getDate(), student.getId());
    }

    public String viewStudents(Long courseId) {
        setCourseId(courseId);
        return "/students-on-course?faces-redirect=true";
    }

    public String getStudentStatus(Student student) {
        StudentCourse studentCourse = student.getStudentCourse(getCourseId());
        Course course = courseService.getCourse(getCourseId());
        Date leavingDate = studentCourse.getEndDate();
        if (leavingDate != null) {
            return "Leaving Date: " + dateToString(leavingDate);
        }
        else if (course.isDateAfterEndDate(new Date())) {
            return "Completed Course";
        }
        else {
            return "Studying Course";
        }
    }

    /**
     * Total a students registration attendances for a course.
     * @param student
     * @return
     */
    public String getStudentAttendance(Student student) {
        Course course = courseService.getCourse(getCourseId());
        Date date = course.getStartDate();
        Date endDate = course.getEndDate();
        Date leavingDate = student.getStudentCourse(getCourseId()).getEndDate();
        Date now = new Date();
        Date stopDate = leavingDate == null ? course.getEndDate()
                : leavingDate.before(endDate) ? leavingDate : endDate;
        stopDate = now.before(stopDate) ? now : stopDate;

        int dayCount = 0, presentCount = 0;
        while (!date.after(stopDate)) {
            Attendance attendance = attendanceService.getAttendance(courseId, date, student.getId());
            // Only include attendances that have been taken.
            if (attendances != null) {
                dayCount++;
                presentCount += attendanceService.isStudentPresent(getCourseId(), date, student.getId()) ? 1 : 0;
            }
            date = addDays(date, 1);
            while (isAtWeekend(date)) date = addDays(date, 1);
        }

        String percentStr = dayCount != 0 ? String.format(" (%.1f)", presentCount*100.0/dayCount) : "";
        return String.format("%d/%d", presentCount, dayCount);
    }

}
