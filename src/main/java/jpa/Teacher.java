package jpa;

import javax.persistence.*;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(name = "getTeacher", query = "SELECT t FROM Teacher t WHERE t.id = :id")
})
@DiscriminatorValue("TEACHER")
public class Teacher extends User_ {

    @OneToMany
    private Set<Course> courses;

    public Teacher(String email, String password, String firstName, String lastName, String address) {
        super(email, password, firstName, lastName, address);
    }

    public Teacher() {
    }

    @Override
    public String toString() {
        return "Teacher {" +
                "id=" + getId() +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", role=" + getDecriminatorValue() +
                '}';
    }

}
