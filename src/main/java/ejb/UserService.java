package ejb;

import jpa.Admin;
import jpa.Student;
import jpa.Teacher;
import jpa.User_;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Local
@Stateless
public class UserService {

    @PersistenceContext
    EntityManager em;

    public String userToString() {
        return this.toString();
    }

    public Long createUser(String email, String password, String role){

        Long id = createUser(role, email, password, "fnTest", "lnTest", "addrTest");
        return id;
    }

    public Long createUser(String role, String email, String password, String firstName, String lastName, String address){
        User_ user = null;
        if (role.equals("TEACHER")) {
            user = new Teacher(email, password, firstName, lastName, address);
        }
        else if(role.equals("STUDENT")) {
            user = new Student(email, password, firstName, lastName, address);
        }
        else if(role.equals("ADMIN")) {
            user = new Admin(email, password, firstName, lastName, address);
        }
        em.persist(user);
        return user.getId();
    }

    public Long countUsers() {
        List<Long> c = em.createNamedQuery("countUsers").getResultList();
        Long i = c.get(0);
        return i;
    }

    public User_ getUser(Long id) {
        User_ user = (User_) em.createNamedQuery("getUser").setParameter("id", id).getSingleResult();
        return user;
    }

    public User_ logIn(String email, String password) {

        TypedQuery<User_> query = em.createNamedQuery("LogIn", User_.class);
        query.setParameter("email", email);
        query.setParameter("password", password);

        User_ user = query.getSingleResult();

        return user;
    }

    public User_ getAdmin() {
        TypedQuery<User_> query = em.createQuery("SELECT u FROM User_ u", User_.class);
        List<User_> users = query.getResultList();
        for (User_ user: users) {
            String role = user.getRole();
            if (role.equals("ADMIN")) {
                return user;
            }
        }
        return null;
    }
}
