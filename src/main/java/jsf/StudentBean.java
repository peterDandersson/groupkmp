package jsf;

import ejb.AdminService;
import ejb.StudentService;
import ejb.UserService;
import jpa.Student;
import jpa.User_;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import java.util.List;

@ManagedBean
@RequestScoped
public class StudentBean {

    //@EJB
    //UserService userService;

    @EJB
    StudentService studentService;

    private Long id;
    private String email;
    private String password;
    private String role;

    // private User_ user;


    public String createStudent() {
        try {
            Long id = studentService.createStudent(email, password);
        } catch (EJBException e) {
            // Do nothing
            // Pop-up message - email already exists.
        }
        setEmail("");
        setPassword("");
        return "admin/students";
    }

    public String removeUser(Long id) {
        studentService.removeStudent(id);
        return "admin/students";
    }
/*
    public String updateStudent(Long id) {
        userService.updateUser(getId(), getEmail(), getPassword());
        return "admin/students";
    }*/

    public String editStudent(Long id) {
        Student student = studentService.getStudent(id);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
/*        System.out.println(course.toString());
        System.out.println(course.getId());
        System.out.println(id);*/
        setId(id);
        //System.out.println(getId());

        setEmail(student.getEmail());
        setPassword(student.getPassword());
        return "admin";
    }

    public List<Student> getAllStudents(){
        return studentService.getStudents();
    }

    public String getSubmitButtonLabel() {
        if (getId()==null)
            return "Add";
        else
            return "Update";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
