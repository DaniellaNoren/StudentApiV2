package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.exception.StudentNotFoundException;

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
    public List listAllStudents() {
        Query query = entityManager.createQuery("SELECT s from Student s");
        return query.getResultList();
    }

    @Override
    public Student addStudent(Student studentToAdd) {
        try {
            entityManager.persist(studentToAdd);
            entityManager.flush();
            return studentToAdd;
        } catch ( PersistenceException pe ) {
            studentToAdd.setForename("duplicate");
            return studentToAdd;
        }
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
    public Student removeStudent(String email) {
       Student student = this.getStudentByEmail(email);

       Query query = entityManager.createQuery("DELETE FROM Student s WHERE s.email = :email");
       query.setParameter("email", email).executeUpdate();

       return student;
    }

    @Override
    public Student updateStudent(String forename, String lastName, String email) {

        Query updateQuery = entityManager.createQuery("UPDATE Student s SET s.forename = :forename, s.lastName = :lastName WHERE s.email = :email");
         updateQuery.setParameter("forename", forename)
                .setParameter("lastName", lastName)
                .setParameter("email", email)
                .executeUpdate();


            Student student = this.getStudentByEmail(email);
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
