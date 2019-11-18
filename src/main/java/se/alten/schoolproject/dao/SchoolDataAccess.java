package se.alten.schoolproject.dao;

import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.entity.Subject;
import se.alten.schoolproject.exception.EntityNotUniqueException;
import se.alten.schoolproject.exception.IncompleteFormException;
import se.alten.schoolproject.exception.EntityNotFoundException;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.model.SubjectModel;
import se.alten.schoolproject.transaction.SubjectTransactionAccess;
import se.alten.schoolproject.service.StudentMapper;
import se.alten.schoolproject.transaction.StudentTransactionAccess;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class SchoolDataAccess implements SchoolAccessLocal, SchoolAccessRemote {


    @Inject
    private StudentMapper studentMapper;

    private Subject subject = new Subject();
    private SubjectModel subjectModel = new SubjectModel();

    @Inject
    SubjectTransactionAccess subjectTransactionAccess;

    @Inject
    StudentTransactionAccess studentTransactionAccess;

    @Override
    public List<StudentModel> listAllStudents(){
        List<Student> student = studentTransactionAccess.listAllStudents();
        List<StudentModel> studentModels = student.stream().map(st -> studentMapper.toModel(st)).collect(Collectors.toList());
        return studentModels;
    }

    @Override
    public StudentModel addStudent(String newStudent) {
        Student studentToAdd = studentMapper.toEntity(newStudent);

        if(this.isEmailRegistered(studentToAdd.getEmail())){
                throw new EntityNotUniqueException("{\"That email is already registered\"}");
        }

        studentToAdd.getSubjects().forEach(sub -> {
            if (this.isSubjectRegistered(sub.getTitle())) {
                throw new EntityNotUniqueException("\"A subject with that title already exists\"}");
        }});

        studentToAdd = studentTransactionAccess.addStudent(studentToAdd);

        return studentMapper.toModel(studentToAdd);
    }

    public boolean isEmailRegistered(String email){
        return studentTransactionAccess.getStudentByEmail(email) != null;
    }
    public boolean isSubjectRegistered(String title){
        return subjectTransactionAccess.getSubjectByTitle(title) != null;
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
            throw new EntityNotFoundException("{\"Student with that email does not exist\"}");

    }

    @Override
    public StudentModel removeStudent(String studentEmail) {
        if(this.isEmailRegistered(studentEmail)){
            Student student = studentTransactionAccess.getStudentByEmail(studentEmail);
            studentTransactionAccess.removeStudent(student);
            return studentMapper.toModel(student);
        }else
            throw new EntityNotFoundException("{\"Student with that email does not exist\"}");
    }

    @Override
    public StudentModel updateStudent(String forename, String lastName, String email) {
        if(forename == null || lastName == null)
            throw new IncompleteFormException("{\"Please fill in both forename and last name\"}");

        if(isEmailRegistered(email)){
            Student student = studentTransactionAccess.getStudentByEmail(email);
            student.setLastName(lastName);
            student.setForename(forename);
            studentTransactionAccess.updateStudent(student);
            return studentMapper.toModel(student);
        }else
            throw new EntityNotFoundException("{\"Student with that email does not exist\"}");


    }

    @Override
    public StudentModel updateStudentPartial(String forename, String email) {
        if(forename == null)
            throw new IncompleteFormException("{\"Please fill in forename\"}");
        if(isEmailRegistered(email)) {
            Student student = studentTransactionAccess.updateStudentPartial(forename, email);
            return studentMapper.toModel(student);
        }else
            throw new EntityNotFoundException("{\"Student with that email does not exist\"}");
    }


    @Override
    public List listAllSubjects() {
        List<Subject> subjects = subjectTransactionAccess.listAllSubjects();
        List<SubjectModel> subjectModels = subjects.stream()
                                            .map(sub -> studentMapper.toSubjectModel(sub))
                                            .collect(Collectors.toList());
        return subjectModels;
    }

    @Override
    public SubjectModel addSubject(String newSubject) {

            Subject subjectToAdd = studentMapper.toSubjectEntity(newSubject);
            if(subjectTransactionAccess.getSubjectByTitle(subjectToAdd.getTitle()) != null){
                throw new EntityNotUniqueException("{\"That subject is already registered\"}");
            }

            subjectToAdd.getStudents().forEach(st -> {
                if(this.isEmailRegistered(st.getEmail()))
                    throw new EntityNotUniqueException("{\"That email is already registered\"}");
            });

            subjectTransactionAccess.addSubject(subjectToAdd);
            return studentMapper.toSubjectModel(subjectToAdd);


    }

    @Override
    public SubjectModel getSubjectByTitle(String title) {
        Subject subject = subjectTransactionAccess.getSubjectByTitle(title);
        if(subject != null)
            return studentMapper.toSubjectModel(subject);
        else
            throw new EntityNotFoundException("{\"Subject with that title does not exist\"}");
    }

    @Override
    public SubjectModel removeSubject(String title){
        if(this.isSubjectRegistered(title)){
            Subject subject = subjectTransactionAccess.getSubjectByTitle(title);
            subjectTransactionAccess.removeSubject(subject);
            return studentMapper.toSubjectModel(subject);
        }else
            throw new EntityNotFoundException("{\"Subject with that title does not exist\"}");
    }

    @Override
    public StudentModel addStudentToSubject(String email, String title) {
        if(isEmailRegistered(email) && isSubjectRegistered(title)){
            Student student = studentTransactionAccess.getStudentByEmail(email);
            Subject subject = subjectTransactionAccess.getSubjectByTitle(title);
            student.addSubject(subject);
            studentTransactionAccess.updateStudent(student);
            return studentMapper.toModel(student);
        }else
            throw new EntityNotFoundException("{\"Entity does not exist\"}");
    }

    @Override
    public SubjectModel updateSubjectTitle(String oldTitle, String newTitle) {
        if(isSubjectRegistered(oldTitle)){
            if(newTitle != null){
                return studentMapper.toSubjectModel(subjectTransactionAccess.updateSubjectTitle(oldTitle, newTitle));
            }else
                throw new IncompleteFormException("{\"Missing new title\"}");
        }else
            throw new EntityNotFoundException("{\"Subject does not exist\"}");
    }
}
