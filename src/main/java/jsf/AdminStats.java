package jsf;

import ejb.AttendanceService;
import ejb.CourseService;
import jpa.Attendance;
import jpa.Course;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO check if course is active


@ManagedBean
@SessionScoped
public class AdminStats {
    private Date date;
    private String page = "admin/admin-stats";

    @EJB
    AttendanceService attendanceService;
    @EJB
    CourseService courseService;


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

    public String theDayBefore(){
        this.date.setDate(date.getDate() - 1);
        return page;
    }

    public String theDayAfter(){
        this.date.setDate(date.getDate() + 1);
        return page;
    }

    public Date getDate() {
        return (date != null) ? date : (date = new Date());
    }

    public void setDate(Date date) {
        this.date = date;
    }
}