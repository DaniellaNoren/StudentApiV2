package se.alten.schoolproject.dao;

import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.model.SubjectModel;
import se.alten.schoolproject.model.TeacherModel;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SchoolAccessLocal {

    List listAllStudents();

    StudentModel addStudent(String student);

    List getStudentsByLastName(String lastName);

    StudentModel getStudentByEmail(String email);

    StudentModel removeStudent(String student);

    StudentModel updateStudent(String forename, String lastName, String email);

    StudentModel updateStudentPartial(String forename, String email);

    List listAllSubjects();

    SubjectModel addSubject(String subjectModel);

    SubjectModel getSubjectByTitle(String title);

    SubjectModel removeSubject(String title);

    StudentModel addStudentToSubject(String email, String title);

    SubjectModel updateSubjectTitle(String oldTitle, String newTitle);

    List listAllTeachers();

    TeacherModel getTeacherByName(String name);

    TeacherModel deleteTeacher(String name);

    TeacherModel addTeacher(String teacherModel);

    TeacherModel addTeacherToSubject(String name, String title);

    TeacherModel updateTeacherName(String oldName, String newName);

}
