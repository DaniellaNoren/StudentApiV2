package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Student;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.*;
import java.util.List;

@Stateless
@Default
public class StudentTransaction implements StudentTransactionAccess{

    @PersistenceContext(unitName="school")
    private EntityManager entityManager;

    @Override
    public List<Student> listAllStudents() {
        Query query = entityManager.createQuery("SELECT s from Student s", Student.class);
        return query.getResultList();
    }

    @Override
    public Student addStudent(Student studentToAdd) {
        entityManager.persist(studentToAdd);
        entityManager.flush();
        return studentToAdd;
    }

    @Override
    public Student getStudentByEmail(String email) {
        Query query = entityManager.createQuery("SELECT s FROM Student s WHERE s.email = :email");
        query.setParameter("email", email);
        List student = query.getResultList();
        if(student.isEmpty()){
            return null;
        }else{
            return (Student)student.get(0);
        }


    }

    @Override
    public List getStudentsByLastName(String lastName) {
        Query query = entityManager.createQuery("SELECT s FROM Student s WHERE s.lastName = :lastname ORDER BY s.lastName");
        query.setParameter("lastname", lastName);
        return query.getResultList();
    }

    @Override
    public Student removeStudent(Student student) {
       student.removeSubject();
        entityManager.remove(student);
        entityManager.flush();

       return student;
    }

    @Override
    public Student updateStudent(Student student) {
        entityManager.merge(student);
        entityManager.flush();
        return student;

    }

    @Override
    public Student updateStudentPartial(String forename, String email) {
        Query query = entityManager.createQuery("UPDATE Student SET forename = :forename WHERE email = :email");

        query.setParameter("forename", forename)
                .setParameter("email", email)
                .executeUpdate();

            Student student = this.getStudentByEmail(email);
            return student;

    }

}
