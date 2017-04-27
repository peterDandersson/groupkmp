package jsf;

import ejb.AttendanceService;
import ejb.CourseService;
import jpa.Attendance;
import jpa.Course;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ManagedBean
@SessionScoped
public class AdminStats implements Serializable {
    private Date date;
    private int dateOffset = 0;
    private String page = "admin/admin-stats";

    @EJB
    AttendanceService attendanceService;
    @EJB
    CourseService courseService;

    @ManagedProperty(value="#{attendanceBean}")
    private AttendanceBean attendanceBean;

    //must povide the setter method
    public void setAttendanceBean(AttendanceBean attendanceBean) {
        this.attendanceBean = attendanceBean;
    }
    public AttendanceBean getAttendanceBean() {
        return attendanceBean;
    }

    public String courseAttendance(Long id){
        Map map = courseAttendanceMap(id);

        if(!courseService.getCourse(id).isCourseCurrentOn(date)){
            return "-";
        }

        return "" + map.get("ATTENDANCE") + "/" + map.get("STUDENTS");
    }


    public Map courseAttendanceMap(Long id){

        List<Attendance> attendanceList = attendanceService.getAttendances(id, getDate());

        int students = 0;
        for (Attendance attendance : attendanceList){
            students = (attendance.isPresent()) ? ++students : students;
        }

        Map<String, Integer> map = new HashMap<>();
        map.put("STUDENTS", courseService.getStudents(id).size());
        map.put("ATTENDANCE", students);

        return map;
    }

    public String courseAttendanceTotal(){
        List<Course> courseList = courseService.getAllCourses();
        int students = 0;
        int attendance = 0;

        for (Course course : courseList){
            if(course.isCourseCurrentOn(date)){
                Map map = courseAttendanceMap(course.getId());
                students += (int) map.get("STUDENTS");
                attendance += (int) map.get("ATTENDANCE");
            }
        }

        return "Attendance total: " + attendance + "/" + students;
    }


    public List<Course> getAllCourses(){
        return courseService.getAllCourses();
    }

    public String changeDay(int newOffset){
        dateOffset += newOffset;
        getDate().setDate(new Date().getDate() + dateOffset);
        return page;
    }

    public Date getDate() {
        return (date != null) ? date : (date = new Date());
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDateOffset() {
        return dateOffset;
    }

    public void setDateOffset(int dateOffset) {
        this.dateOffset = dateOffset;
    }

    public String goToCourseStats(Long id){
        System.out.println("ID: " + id);
        attendanceBean.setCourseId(id);
        return "/course-attendance";
    }
}