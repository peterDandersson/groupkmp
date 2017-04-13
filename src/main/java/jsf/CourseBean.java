package jsf;

import ejb.CourseService;
import jpa.Course;
import jpa.Student;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import java.util.Date;
import java.util.List;

@ManagedBean
@RequestScoped
public class CourseBean {

    @EJB
    CourseService courseService;

    private Long id;
    private String courseName;
    private String description;
    private Date startDate;
    private Date endDate;
    private int maxStudents;

    public String createCourse() {
        if (getId()==null) {
            courseService.createCourse(courseName, description);
        }
        else {
            updateCourse(getId());
        }
        setId(null);
        setCourseName("");
        setDescription("");
        return "admin";
    }

    public String removeCourse(Long id) {
        courseService.removeCourse(id);
        return "admin";
    }

    public String updateCourse(Long id) {
        courseService.updateCourse(getId(), getCourseName(), getDescription());
        return "admin";
    }

    public String editCourse(Long id) {
        Course course = courseService.getCourse(id);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(course.toString());
        System.out.println(course.getId());
        System.out.println(id);
        setId(id);
        System.out.println(getId());

        setCourseName(course.getCourseName());
        setDescription(course.getDescription());
        return "admin";
    }

    public String getSubmitButtonLabel() {
        if (getId()==null)
            return "Add";
        else
            return "Update";
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
