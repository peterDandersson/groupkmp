package jsf;

import ejb.AttendanceService;
import ejb.CourseService;
import jpa.Attendance;
import jpa.Course;
import jpa.Student;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


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

        int attending = 0;
        for (Attendance attendance : attendanceList){
            attending = (attendance.isPresent()) ? ++attending : attending;
        }

        int students = 0;
        for(Student student : courseService.getStudents(id)){
            Date studentDate = courseService.getLeavingDate(id, student.getId());
            if(null != studentDate){
                students += getDate().after(studentDate) ? 0 : 1;
            }else {
                students += 1;
            }

        }

        Map<String, Integer> map = new HashMap<>();
        map.put("STUDENTS", students);
        map.put("ATTENDANCE", attending);

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

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        c.add(Calendar.DATE, dateOffset);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        DateFormat df = new SimpleDateFormat("EEE dd/MM/yyyy");

        System.out.println("day: " + dayOfWeek + df.format(c.getTime()));

        int o = 0;
        if((dayOfWeek == 7 || dayOfWeek == 1 ) && newOffset > 0){
            o = dayOfWeek == 7 ? 2 : 1;
            c.add(Calendar.DATE, o);
        } else if(dayOfWeek == 7 || dayOfWeek == 1){
            o = dayOfWeek == 1 ? -2 : -1;
            c.add(Calendar.DATE, o);
        }
        dateOffset += o;
        setDate(c.getTime());
        return page;
    }

    public String formatDate(){
        Calendar c = Calendar.getInstance();
        c.setTime(getDate());
        DateFormat df = new SimpleDateFormat("EEE dd/MM/yyyy");
        return df.format(c.getTime());
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