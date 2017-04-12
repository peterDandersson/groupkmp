package jsf;

import ejb.UserService;
import jpa.User_;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.xml.ws.Response;
import java.io.IOException;

@ManagedBean
@SessionScoped
public class UserBean {

    @EJB
    UserService userService;

    private String email;
    private String password;
    private String role;

    private User_ user;

    public String logIn(){
        user = userService.logIn(email, password);
        setEmail("");
        setPassword("");
        return user.getRole().toLowerCase() + "?faces-redirect=true";
    }

    public String logOut(){
        user = null;
        email = "";
        password = "";
        return "login";
    }

    public String createUser() {
        Long id = userService.createUser(email, password, role);
        setEmail("");
        setPassword("");
        return "createUser";
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public boolean isStudent() {
        return user != null && user.getRole().equals("STUDENT");
    }

    public boolean isTeacher() {
        return user != null && user.getRole().equals("TEACHER");
    }

    public boolean isAdmin() {
        return user != null && user.getRole().equals("ADMIN");
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

    public String getUserInfo(){
        return user.toString();

    }

    public String userRole() {
        return user.getRole();
    }

}
