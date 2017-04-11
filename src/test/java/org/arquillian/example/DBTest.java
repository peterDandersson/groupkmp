package org.arquillian.example;

import javax.ejb.EJB;

import ejb.UserService;
import jpa.*;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import testpack.TestEJB;

import static org.junit.Assert.assertEquals;


@RunWith(Arquillian.class)
public class DBTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClass(UserService.class)
                .addClass(User_.class)
                .addClass(Admin.class)
                .addClass(Teacher.class)
                .addClass(Student.class)
                .addClass(StudentCourse.class)
                //.addClass(Course.class)
/*            .addClass(Admin.class)
            .addClass(Teacher.class)
                .addClass(Student.class)
                .addClass(Attendance.class)
                .addClass(Course.class)
                .addClass(Day.class)
                .addClass(StudentCourse.class)
                .addClass(TestEJB.class)*/
            //.addPackage(User_.class.getPackage())
            //.addPackage(LogIn.class.getPackage())

            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            //.addAsWebInfResource("jbossas-ds.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                //.addAsWebInfResource("beans.xml")
        ;
    }


    @EJB
    private UserService userService;


    @Test
    public void userCountTest() {
        System.out.println("<<<<<<   >>>>>>>>>>>>>>>>> TEST <<<<<<<<<<<<<<<<<<   >>>>>>");
        //userService.createUser("admin", "p", "ADMIN");
        Long userCount = userService.countUsers();
        String newUserName = "admin" + userCount;
        userService.createUser(newUserName, "p", "ADMIN");
        System.out.println(userCount);
        Long expectedCount = userCount + 1;
        assertEquals(expectedCount, userService.countUsers());
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
