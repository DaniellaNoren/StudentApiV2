package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Student;

import javax.ejb.Local;
import java.util.List;

@Local
public interface StudentTransactionAccess {
    List listAllStudents();
    Student addStudent(Student studentToAdd);
    Student getStudentByEmail(String email);
    List getStudentsByLastName(String lastName);
    Student removeStudent(String email);
    Student updateStudent(String forename, String lastName, String email);
    Student updateStudentPartial(String forename, String email);
}
