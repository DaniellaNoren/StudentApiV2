package se.alten.schoolproject.dao;

import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.entity.Subject;
import se.alten.schoolproject.entity.Teacher;
import se.alten.schoolproject.exception.EntityNotUniqueException;
import se.alten.schoolproject.exception.IncompleteFormException;
import se.alten.schoolproject.exception.EntityNotFoundException;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.model.SubjectModel;
import se.alten.schoolproject.model.TeacherModel;
import se.alten.schoolproject.service.SubjectMapper;
import se.alten.schoolproject.service.TeacherMapper;
import se.alten.schoolproject.transaction.SubjectTransactionAccess;
import se.alten.schoolproject.service.StudentMapper;
import se.alten.schoolproject.transaction.StudentTransactionAccess;
import se.alten.schoolproject.transaction.TeacherTransactionAccess;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class SchoolDataAccess implements SchoolAccessLocal, SchoolAccessRemote {

    @Inject
    SubjectTransactionAccess subjectTransactionAccess;

    @Inject
    StudentTransactionAccess studentTransactionAccess;

    @Inject
    TeacherTransactionAccess teacherTransactionAccess;

    @Override
    public List<StudentModel> listAllStudents(){
        List<Student> student = studentTransactionAccess.listAllStudents();
        List<StudentModel> studentModels = student.stream().map(st -> StudentMapper.toModel(st)).collect(Collectors.toList());
        return studentModels;
    }

    @Override
    public StudentModel addStudent(String newStudent) {
        Student studentToAdd = StudentMapper.toEntity(newStudent);

        if(this.isEmailRegistered(studentToAdd.getEmail())){
                throw new EntityNotUniqueException("{\"That email is already registered\"}");
        }

        studentToAdd.getSubjects().forEach(sub -> {
            if (this.isSubjectRegistered(sub.getTitle())) {
                throw new EntityNotUniqueException("\"A subject with that title already exists\"}");
        }});

        studentToAdd = studentTransactionAccess.addStudent(studentToAdd);

        return StudentMapper.toModel(studentToAdd);
    }

    public boolean isEmailRegistered(String email){
        return studentTransactionAccess.getStudentByEmail(email) != null;
    }
    public boolean isSubjectRegistered(String title){
        return subjectTransactionAccess.getSubjectByTitle(title) != null;
    }
    public boolean isTeacherRegistered(String name){
        return teacherTransactionAccess.getTeacherByLastName(name) != null;
    }

    @Override
    public List getStudentsByLastName(String lastName) {
        List<Student> student = studentTransactionAccess.getStudentsByLastName(lastName);
        List<StudentModel> studentModels = student.stream().map(st -> StudentMapper.toModel(st)).collect(Collectors.toList());
        return studentModels;
    }

    @Override
    public StudentModel getStudentByEmail(String email) {
        Student student = studentTransactionAccess.getStudentByEmail(email);
        if(student != null)
            return StudentMapper.toModel(student);
        else
            throw new EntityNotFoundException("{\"Student with that email does not exist\"}");

    }

    @Override
    public StudentModel removeStudent(String studentEmail) {
        if(this.isEmailRegistered(studentEmail)){
            Student student = studentTransactionAccess.getStudentByEmail(studentEmail);
            studentTransactionAccess.removeStudent(student);
            return StudentMapper.toModel(student);
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
            return StudentMapper.toModel(student);
        }else
            throw new EntityNotFoundException("{\"Student with that email does not exist\"}");


    }

    @Override
    public StudentModel updateStudentPartial(String forename, String email) {
        if(forename == null)
            throw new IncompleteFormException("{\"Please fill in forename\"}");
        if(isEmailRegistered(email)) {
            Student student = studentTransactionAccess.updateStudentPartial(forename, email);
            return StudentMapper.toModel(student);
        }else
            throw new EntityNotFoundException("{\"Student with that email does not exist\"}");
    }


    @Override
    public List listAllSubjects() {
        List<Subject> subjects = subjectTransactionAccess.listAllSubjects();
        List<SubjectModel> subjectModels = subjects.stream()
                                            .map(sub -> SubjectMapper.toSubjectModel(sub))
                                            .collect(Collectors.toList());
        return subjectModels;
    }

    @Override
    public SubjectModel addSubject(String newSubject) {

            Subject subjectToAdd = SubjectMapper.toSubjectEntity(newSubject);
            if(subjectTransactionAccess.getSubjectByTitle(subjectToAdd.getTitle()) != null){
                throw new EntityNotUniqueException("{\"That subject is already registered\"}");
            }

            subjectToAdd.getStudents().forEach(st -> {
                if(this.isEmailRegistered(st.getEmail()))
                    throw new EntityNotUniqueException("{\"That email is already registered\"}");
            });

            if(this.isTeacherRegistered(subjectToAdd.getTeacher().getLastName()))
                throw new EntityNotUniqueException("{\"That teacher is already registered\"}");

            subjectTransactionAccess.addSubject(subjectToAdd);
            return SubjectMapper.toSubjectModel(subjectToAdd);


    }

    @Override
    public SubjectModel getSubjectByTitle(String title) {
        Subject subject = subjectTransactionAccess.getSubjectByTitle(title);
        if(subject != null)
            return SubjectMapper.toSubjectModel(subject);
        else
            throw new EntityNotFoundException("{\"Subject with that title does not exist\"}");
    }

    @Override
    public SubjectModel removeSubject(String title){
        if(this.isSubjectRegistered(title)){
            Subject subject = subjectTransactionAccess.getSubjectByTitle(title);
            subjectTransactionAccess.removeSubject(subject);
            return SubjectMapper.toSubjectModel(subject);
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
            return StudentMapper.toModel(student);
        }else
            throw new EntityNotFoundException("{\"Entity does not exist\"}");
    }

    @Override
    public SubjectModel updateSubjectTitle(String oldTitle, String newTitle) {
        if(isSubjectRegistered(oldTitle)){
            if(newTitle != null){
                return SubjectMapper.toSubjectModel(subjectTransactionAccess.updateSubjectTitle(oldTitle, newTitle));
            }else
                throw new IncompleteFormException("{\"Missing new title\"}");
        }else
            throw new EntityNotFoundException("{\"Subject does not exist\"}");
    }

    @Override
    public List listAllTeachers() {
        List<Teacher> teachers = teacherTransactionAccess.getAllTeachers();
        List<TeacherModel> teacherModels = teachers.stream()
                .map(teacher -> TeacherMapper.toTeacherModel(teacher))
                .collect(Collectors.toList());
        return teacherModels;
    }

    @Override
    public TeacherModel getTeacherByName(String name) {
        Teacher teacher = teacherTransactionAccess.getTeacherByLastName(name);
        if(teacher != null)
            return TeacherMapper.toTeacherModel(teacher);
        else
            throw new EntityNotFoundException("{\"Teacher with that name does not exist\"}");
    }

    @Override
    public TeacherModel deleteTeacher(String name) {
        if(this.isTeacherRegistered(name)){
            Teacher teacher = teacherTransactionAccess.getTeacherByLastName(name);
            teacherTransactionAccess.removeTeacher(teacher);
            return TeacherMapper.toTeacherModel(teacher);
        }else
            throw new EntityNotFoundException("{\"Teacher with that name does not exist\"}");
    }

    @Override
    public TeacherModel addTeacher(String teacherModel) {
        Teacher teacherToAdd = TeacherMapper.toTeacherEntity(teacherModel);
        if(teacherTransactionAccess.getTeacherByLastName(teacherToAdd.getLastName()) != null){
            throw new EntityNotUniqueException("{\"That teacher is already registered\"}");
        }

        teacherToAdd.getSubjects().forEach(st -> {
            if(this.isSubjectRegistered(st.getTitle()))
                throw new EntityNotUniqueException("{\"That subject is already registered\"}");
        });

        teacherTransactionAccess.addTeacher(teacherToAdd);
        return TeacherMapper.toTeacherModel(teacherToAdd);
    }

    @Override
    public TeacherModel addTeacherToSubject(String name, String title) {
        if(isTeacherRegistered(name) && isSubjectRegistered(title)){
            Teacher teacher = teacherTransactionAccess.getTeacherByLastName(name);
            Subject subject = subjectTransactionAccess.getSubjectByTitle(title);
            teacher.getSubjects().add(subject);
            subject.setTeacher(teacher);
            teacherTransactionAccess.updateTeacher(teacher);
            return TeacherMapper.toTeacherModel(teacher);
        }else
            throw new EntityNotFoundException("{\"Entity does not exist\"}");
    }

    @Override
    public TeacherModel updateTeacherName(String oldName, String newName) {
        if(isTeacherRegistered(oldName)){
            if(newName != null){
                return TeacherMapper.toTeacherModel(teacherTransactionAccess.updateTeacherName(oldName, newName));
            }else
                throw new IncompleteFormException("{\"Missing new title\"}");
        }else
            throw new EntityNotFoundException("{\"Subject does not exist\"}");
    }


}
