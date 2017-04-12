package jsf;

import ejb.CourseService;
import jpa.Course;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.Date;
import java.util.List;

@ManagedBean
@RequestScoped
public class CoursesBean {

    @EJB
    CourseService courseService;

    private Long id;
    private String courseName;
    private String description;
    private Date startDate;
    private Date endDate;
    private int maxStudents;

    public String createCourse() {
        Long id = courseService.createCourse(courseName, description);
        setCourseName("");
        setDescription("");
        return "admin";
    }

    public String removeCourse(Long id){
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        courseService.removeCourse(id);
        return "admin";
    }

    public List<Course> getAllCourses(){
        return courseService.getAllCourses();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }


}
