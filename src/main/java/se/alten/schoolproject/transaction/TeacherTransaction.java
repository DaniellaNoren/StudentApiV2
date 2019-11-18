package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Teacher;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
@Default
public class TeacherTransaction implements TeacherTransactionAccess{

    @PersistenceContext(unitName="school")
    private EntityManager entityManager;


    @Override
    public List getAllTeachers() {
        return entityManager.createQuery("SELECT s FROM Teacher s", Teacher.class).getResultList();
    }

    @Override
    public Teacher getTeacherByLastName(String lastName) {
        Query query = entityManager.createQuery("SELECT s FROM Teacher s WHERE s.lastName = :lastName");
        query.setParameter("lastName", lastName);
        List teacher = query.getResultList();
        if(teacher.isEmpty()){
            return null;
        }else{
            return (Teacher)teacher.get(0);
        }
    }

    @Override
    public Teacher removeTeacher(Teacher teacher) {
        teacher.removeTeacherFromSubjects();
        entityManager.remove(teacher);
        entityManager.flush();
        return teacher;
    }

    @Override
    public Teacher addTeacher(Teacher teacherToAdd) {
        entityManager.persist(teacherToAdd);
        entityManager.flush();
        return teacherToAdd;
    }

    @Override
    public Teacher updateTeacherName(String oldName, String newName) {
        Query query = entityManager.createQuery("UPDATE Teacher s SET s.lastName = :newName WHERE s.lastName = :oldName");
        query.setParameter("oldName", oldName).setParameter("newName", newName).executeUpdate();
        return this.getTeacherByLastName(newName);
    }

    @Override
    public Teacher updateTeacher(Teacher teacher) {
        entityManager.merge(teacher);
        entityManager.flush();
        return teacher;
    }
}
