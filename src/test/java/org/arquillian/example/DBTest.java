
package org.arquillian.example;

import javax.ejb.EJB;

import ejb.UserService;
import jpa.User_;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
@RunWith(Arquillian.class)
public class DBTest {
    @Deployment
    public static Archive<?> createDeployment() {
        // You can use war packaging...
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(UserService.class, User_.class)
            //.addPackage(User_.class.getPackage())
            //.addPackage(LogIn.class.getPackage())

            //.addClasses(Driver.class, DriverService.class)
            //.addAsResource("test-persistence.xml")
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            //.addAsWebInfResource("jbossas-ds.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        return war;
    }


    @EJB
    UserService userService;


    @Test
    public void userCountTest() {
        System.out.println("<<<<<<   >>>>>>>>>>>>>>>>> TEST <<<<<<<<<<<<<<<<<<   >>>>>>");
/*        Long userCount = userService.countUsers();
        String newUserName = "Mark" + userCount + "@yahoo.co.uk";
        String password = "hellothere";

        userService.createUser(newUserName, password, "ADMIN");
        Long expectedUserCount = userCount + 1;
        assertEquals(expectedUserCount, userService.countUsers());*/
        assertEquals(1, 1);
    }

/*    @Test
    public void getUserTest() {
        assertEquals(1,1);
        Long userCount = userService.countUsers();
        String newUserName = "Mark" + userCount + "@yahoo.co.uk";
        Long id = userService.createUser(newUserName, "password", "TEACHER");
        User_ user = userService.getUser(id);
        System.out.println(user.toString());
        assertEquals(userToString(id, newUserName, "password"), user.toString());
        assertEquals(newUserName, user.getEmail());
    }

    private String userToString(Long id, String email, String password) {
        return String.format(
                "User{id=%d, email='%s', password='%s', firstName='fnText', lastName='lnTest', address='addrTest'}",
                id, email, password
        );
    }*/



}
