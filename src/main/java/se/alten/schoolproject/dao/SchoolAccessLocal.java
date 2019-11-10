package se.alten.schoolproject.dao;

import se.alten.schoolproject.model.StudentModel;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SchoolAccessLocal {

    List listAllStudents();

    StudentModel addStudent(String studentModel);

    List getStudentsByLastName(String lastName);

    StudentModel getStudentByEmail(String email);

    StudentModel removeStudent(String student);

    StudentModel updateStudent(String forename, String lastName, String email);

    StudentModel updateStudentPartial(String forename, String email);
}
