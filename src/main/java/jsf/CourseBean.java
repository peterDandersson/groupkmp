package jsf;

import ejb.CourseService;
import jpa.Course;
import jpa.Day;
import jpa.Student;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static lib.Helpers.dateToString;

@ManagedBean
@RequestScoped
public class CourseBean {

    @EJB
    CourseService courseService;

    private Long id;
    private Long selectedId;
    private String courseName;
    private String description;
    private Date startDate;
    private Date endDate;
    private int maxStudents = 2;

    public String createCourse() {
        if (getId()==null) {
            courseService.createCourse(
                    getCourseName(),
                    getDescription(),
                    getStartDate(),
                    getEndDate(),
                    getMaxStudents()
            );
        }
        else {
            updateCourse(getId());
        }
        setId(null);
        setCourseName("");
        setDescription("");
        setMaxStudents(2);
        setStartDate(new Date());
        setEndDate(new Date());

        return "admin";
    }

    public String removeCourse(Long id) {
        courseService.removeCourse(id);
        return "admin";
    }

    public String updateCourse(Long id) {
        courseService.updateCourse(getId(), getCourseName(), getDescription(), getStartDate(), getEndDate(), getMaxStudents());
        return "admin";
    }

    public String editCourse(Long id) {
        Course course = courseService.getCourse(id);
        setId(id);
        setCourseName(course.getCourseName());
        setDescription(course.getDescription());
        setMaxStudents(course.getMaxStudents());
        setStartDate(course.getStartDate());
        setEndDate(course.getEndDate());
        return "admin";
    }

    public String getSubmitButtonLabel() {
        if (getId()==null)
            return "Add";
        else
            return "Update";
    }

    public List<Course> getAllCourses(){
        List<Course> courses = courseService.getAllCourses();
        if (courses.size() > 0) {
            setSelectedId(courses.get(0).getId());
        }
        return courses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(Long selectedId) {
        this.selectedId = selectedId;
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

    public int getStudentCount() {
        return maxStudents;
    }

    public boolean isFull(Long id) {
        return courseService.isFull(id);
    }

    public boolean getIsCourseCurrent(Long courseId) {
        return courseService.isCourseCurrent(courseId, new Date());
    }

    public boolean getHasCourseStarted(Long courseId) {
        return courseService.isDateAfterStart(courseId, new Date());
    }

    public boolean getHasCourseEnded(Long courseId) {
        return courseService.isDateAfterEnd(courseId, new Date());
    }

    public String getCourseEndDate(Long courseId) {
        Date endDate = courseService.getEndDate(courseId);
        return dateToString(endDate);
    }

/*    public boolean getHasLeavingDate(Long courseId, Long studentId) {

    }*/

    public String getLeavingDate(Long courseId, Long studentId) {
        Date leavingDate = courseService.getLeavingDate(courseId, studentId);
        return leavingDate != null ? dateToString(leavingDate) : null;
    }

    public void leaveCourse(Long courseId, Long studentId) {

    }

    public boolean isStudentRegistered(Long courseId, Long studentId) {
        return courseService.isStudentRegistered(courseId, studentId);
    }

    public String register(Long courseId, Long studentId) {
        System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr "+courseId);
        courseService.registerForCourse(courseId, studentId);
        return "/student" + "?faces-redirect=true";
    }

    public String deregister(Long courseId, Long studentId) {
        System.out.println("Dereg...................................................");
        courseService.leaveOrDeregisterFromCourse(courseId, studentId);
        return "/student" + "?faces-redirect=true";
    }



/*    public String regHandler(Long courseId, Long studentId) {
        System.out.println("Dereg...................................................");
        System.out.println(courseId);
        System.out.println(studentId);
        return "/student" + "?faces-redirect=true";
    }*/

}
