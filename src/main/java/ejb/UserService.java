package ejb;

import jpa.Student;
import jpa.Teacher;
import jpa.User_;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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

    public Long createUser(String email, String password){
        // String email, String password, String firstName, String lastName, String address, Date birthDate
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++ create user");
        //Teacher user = new Teacher(email, password, "fnText", "lnTest", "addrTest");
        //Student user = new Student(email, password, "fnText", "lnTest", "addrTest");
        User_ user = new User_(email, password, "fnText", "lnTest", "addrTest");
        em.persist(user);
        System.out.println(user.getDecriminatorValue());
        return user.getId();
    }

    public Long createUser(String email, String password, String firstName, String lastName, String address){
        User_ user = new User_(email,password,firstName,lastName,address);
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
}
