package se.alten.schoolproject.dao;

import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.exception.IncompleteFormException;
import se.alten.schoolproject.exception.StudentNotFoundException;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.service.StudentMapper;
import se.alten.schoolproject.transaction.StudentTransactionAccess;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Stream;

@Stateless
public class SchoolDataAccess implements SchoolAccessLocal, SchoolAccessRemote {


    @Inject
    private StudentMapper studentMapper;

    @Inject
    StudentTransactionAccess studentTransactionAccess;

    @Override
    public List listAllStudents(){
        return studentTransactionAccess.listAllStudents();
    }

    @Override
    public StudentModel addStudent(String newStudent) {
        Student studentToAdd = studentMapper.toEntity(newStudent);
        boolean checkForEmptyVariables = Stream.of(studentToAdd.getForename(), studentToAdd.getLastName(), studentToAdd.getEmail()).anyMatch(String::isEmpty);

        if (checkForEmptyVariables) {
            studentToAdd.setForename("empty");
        }else {
            try{
                studentToAdd = studentTransactionAccess.addStudent(studentToAdd);
            }catch(Exception e){
                studentToAdd.setForename("duplicate");
            }
        }

        return studentMapper.toModel(studentToAdd);
    }

    @Override
    public List getStudentsByLastName(String lastName) {
        return studentTransactionAccess.getStudentsByLastName(lastName);
    }

    @Override
    public StudentModel getStudentByEmail(String email) {
        Student student = studentTransactionAccess.getStudentByEmail(email);
        if(student != null)
            return studentMapper.toModel(student);
        else
            throw new StudentNotFoundException("{\"Student with that email does not exist\"}");

    }

    @Override
    public StudentModel removeStudent(String studentEmail) {
        Student student = studentTransactionAccess.removeStudent(studentEmail);
        if(student != null)
            return studentMapper.toModel(student);
        else
            throw new StudentNotFoundException("{\"Student with that email does not exist\"}");
    }

    @Override
    public StudentModel updateStudent(String forename, String lastName, String email) {
        if(forename == null || lastName == null)
            throw new IncompleteFormException("{\"Please fill in both forename and last name\"}");

        Student student = studentTransactionAccess.updateStudent(forename, lastName, email);

        if(student == null)
            throw new StudentNotFoundException("{\"Student with that email does not exist\"}");

         return studentMapper.toModel(student);
    }

    @Override
    public StudentModel updateStudentPartial(String forename, String email) {
        if(forename == null)
            throw new IncompleteFormException("{\"Please fill in forename\"}");

        Student student = studentTransactionAccess.updateStudentPartial(forename, email);

        if(student == null)
            throw new StudentNotFoundException("{\"Student with that email does not exist\"}");

        return studentMapper.toModel(student);
    }
}
