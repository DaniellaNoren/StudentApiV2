package se.alten.schoolproject.service;

import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.entity.Subject;
import se.alten.schoolproject.entity.Teacher;
import se.alten.schoolproject.exception.IncompleteFormException;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.model.SubjectModel;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

public class SubjectMapper {

    public static Subject toSubjectEntity(String subjectModel) {

        JsonReader reader = Json.createReader(new StringReader(subjectModel));
        JsonObject jsonObject = reader.readObject();

        Subject subject = new Subject();

        if ( jsonObject.containsKey("title")) {
            subject.setTitle(jsonObject.getString("title"));
        } else {
            throw new IncompleteFormException("{\"Title not set\"}");
        }

        if(jsonObject.containsKey("students")){
            JsonArray jsonArray = jsonObject.getJsonArray("students");
            for ( int i = 0; i < jsonArray.size(); i++ ){
                Student student = StudentMapper.toEntity(jsonArray.get(i).toString());
                subject.getStudents().add(student);
                student.getSubjects().add(subject);
            }
        }

        if(jsonObject.containsKey("teacher")){
            Teacher teacher = TeacherMapper.toTeacherEntity(jsonObject.getJsonObject("teacher").toString());
            subject.setTeacher(teacher);
            teacher.getSubjects().add(subject);
        }else
            throw new IncompleteFormException("{\"Teacher not set\"}");

        return subject;
    }

    public static Set<String> toSubjectModelStringSet(Set<Subject> subjects){
        Set<String> subjectModels = new HashSet<>();
        subjects.forEach(su -> {
            subjectModels.add(su.getTitle());
        });
        return subjectModels;
    }

    public static SubjectModel toSubjectModel(Subject subject){
        SubjectModel subjectModel = new SubjectModel();
        subjectModel.setTitle(subject.getTitle());
        if(subject.getTeacher() != null){
            subjectModel.setTeacherName(subject.getTeacher().getLastName());
        }else
            subjectModel.setTeacherName("");

        Set<StudentModel> students = StudentMapper.toStudentModelSet(subject.getStudents());
        subjectModel.setStudents(students);
        return subjectModel;
    }
}
