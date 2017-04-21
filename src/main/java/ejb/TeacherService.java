package ejb;


import jpa.Teacher;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Local
@Stateless
public class TeacherService {

    @PersistenceContext
    EntityManager em;

    public List<Teacher> getAllTeachers(){
        return em.createNamedQuery("selectAll", Teacher.class).getResultList();
    }

    public Teacher getTeacher(Long id) {
        TypedQuery<Teacher> query = em.createNamedQuery("getTeacher", Teacher.class);
        query.setParameter("id", id);

        return query.getSingleResult();
    }

    public void removeTeacher(Long id){
        Teacher teacher = em.find(Teacher.class, id);
        em.remove(teacher);
    }

    public void saveTeacher(Teacher teacher){
        if(teacher.getId() == null){
            em.persist(teacher);
        } else {
            em.merge(teacher);
        }
    }


}
