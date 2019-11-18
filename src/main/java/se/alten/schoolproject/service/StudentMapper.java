package se.alten.schoolproject.service;

import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.entity.Subject;
import se.alten.schoolproject.exception.IncompleteFormException;
import se.alten.schoolproject.model.StudentModel;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

public class StudentMapper {

    public static StudentModel toModel(Student student) {
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


    public static Student toEntity(String studentModel) {
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
                Subject subject = SubjectMapper.toSubjectEntity(jsonArray.get(i).toString());
                student.getSubjects().add(subject);
                subject.getStudents().add(student);
            }
        }

        return student;
    }

    public static Set<StudentModel> toStudentModelSet(Set<Student> students){
        Set<StudentModel> studentModels = new HashSet<>();
        students.forEach(st -> {
            StudentModel studentModel = new StudentModel();
            studentModel.setForename(st.getForename());
            studentModel.setLastName(st.getLastName());
            studentModel.setEmail(st.getEmail());
            studentModel.setSubjects(SubjectMapper.toSubjectModelStringSet(st.getSubjects()));
            studentModels.add(studentModel);
        });
        return studentModels;
    }



}
