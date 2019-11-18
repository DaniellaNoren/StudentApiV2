package se.alten.schoolproject.service;

import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.entity.Subject;
import se.alten.schoolproject.exception.IncompleteFormException;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.model.SubjectModel;

import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentMapper {

    public StudentModel toModel(Student student) {
        StudentModel studentModel = new StudentModel();

        switch (student.getForename()) {
            case "empty":
                studentModel.setForename("empty");
                return studentModel;
            case "duplicate":
                studentModel.setForename("duplicate");
                return studentModel;
            default:
                studentModel.setForename(student.getForename());
                studentModel.setLastName(student.getLastName());
                studentModel.setEmail(student.getEmail());
                Set<String> subjects = new HashSet<>();
                student.getSubjects().forEach(subject -> {
                    subjects.add(subject.getTitle());
                });
                studentModel.setSubjects(subjects);
                return studentModel;
        }
    }


    public Student toEntity(String studentModel) {
        JsonReader reader = Json.createReader(new StringReader(studentModel));

        JsonObject jsonObject = reader.readObject();

        Student student = new Student();

        if ( jsonObject.containsKey("forename") && jsonObject.containsKey("lastName") && jsonObject.containsKey("email")) {
            student.setForename(jsonObject.getString("forename"));
            student.setLastName(jsonObject.getString("lastName"));
            student.setEmail(jsonObject.getString("email"));
        } else {
            throw new IncompleteFormException("{\"Form not complete\"}");
        }

        if (jsonObject.containsKey("subjects")) {
            JsonArray jsonArray = jsonObject.getJsonArray("subjects");
            for ( int i = 0; i < jsonArray.size(); i++ ){
                Subject subject = this.toSubjectEntity(jsonArray.get(i).toString());
                student.getSubjects().add(subject);
                subject.getStudents().add(student);
            }
        }

        return student;
    }

    public SubjectModel toSubjectModel(Subject subject){
        SubjectModel subjectModel = new SubjectModel();
        subjectModel.setTitle(subject.getTitle());
        Set<StudentModel> students = this.toStudentModelSet(subject.getStudents());
        subjectModel.setStudents(students);
       return subjectModel;
    }

    public Set<StudentModel> toStudentModelSet(Set<Student> students){
        Set<StudentModel> studentModels = new HashSet<>();
        students.forEach(st -> {
            StudentModel studentModel = new StudentModel();
            studentModel.setForename(st.getForename());
            studentModel.setLastName(st.getLastName());
            studentModel.setEmail(st.getEmail());
            studentModel.setSubjects(this.toSubjectModelStringSet(st.getSubjects()));
            studentModels.add(studentModel);
        });
        return studentModels;
    }

    public Set<String> toSubjectModelStringSet(Set<Subject> subjects){
        Set<String> subjectModels = new HashSet<>();
        subjects.forEach(su -> {
            subjectModels.add(su.getTitle());
        });
        return subjectModels;
    }
    public Subject toSubjectEntity(String subjectModel) {

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
                Student student = this.toEntity(jsonArray.get(i).toString());
                subject.getStudents().add(student);
                student.getSubjects().add(subject);
            }
        }

        return subject;
    }
}
