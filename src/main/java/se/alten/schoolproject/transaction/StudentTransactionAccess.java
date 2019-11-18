package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.entity.Subject;

import javax.ejb.Local;
import java.util.List;

@Local
public interface StudentTransactionAccess {
    List listAllStudents();
    Student addStudent(Student studentToAdd);
    Student getStudentByEmail(String email);
    List getStudentsByLastName(String lastName);
    Student removeStudent(Student student);
    Student updateStudent(Student student);
    Student updateStudentPartial(String forename, String email);

}
