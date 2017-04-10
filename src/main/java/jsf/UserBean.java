package jsf;

import ejb.UserService;
import jpa.User_;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class UserBean {

    @EJB
    UserService userService;

    private String email;
    private String password;

    private User_ user;

    public String logIn(){
        user = userService.logIn(email, password);
        return user.getDecriminatorValue().toLowerCase();
    }

    public void createStudent() {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++ createStudent");
        Long id = userService.createUser(email,password);

    }

    private boolean isStudent() {
        return user.getRole().equals("STUDENT");
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

    public String getUserInfo(){
        return user.toString();

    }
}
