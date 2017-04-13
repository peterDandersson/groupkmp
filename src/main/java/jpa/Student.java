package jpa;

import javax.persistence.*;
import java.util.Set;

@Entity()
@NamedQueries({
        @NamedQuery(name = "getStudent", query = "SELECT s FROM Student s WHERE s.id = :id"),
        @NamedQuery(name = "getStudents", query = "SELECT s FROM Student s")
})
@DiscriminatorValue("STUDENT")
public class Student extends User_ {

    private boolean naughty;

    @OneToMany(cascade = CascadeType.PERSIST)
    private Set<StudentCourse> studentCourses;

    public Student(String email, String password, String firstName, String lastName, String address) {
        super(email, password, firstName, lastName, address);
        naughty = false;
    }

    public Student() {
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + getId() +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", naughty='" + naughty + '\'' +
                ", role=" + getDecriminatorValue() +
                '}';
    }

    public Set<StudentCourse> getStudentCourses() {
        return studentCourses;
    }

    public void setStudentCourses(Set<StudentCourse> studentCourses) {
        this.studentCourses = studentCourses;
    }
}
