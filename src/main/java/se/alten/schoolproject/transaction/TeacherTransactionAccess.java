package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Teacher;

import java.util.List;

public interface TeacherTransactionAccess {

    List getAllTeachers();
    Teacher getTeacherByLastName(String lastName);

    Teacher removeTeacher(Teacher teacher);

    Teacher addTeacher(Teacher teacherToAdd);

    Teacher updateTeacherName(String oldName, String newName);

    Teacher updateTeacher(Teacher teacher);
}
