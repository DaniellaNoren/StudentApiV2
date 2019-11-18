package se.alten.schoolproject.service;

import se.alten.schoolproject.entity.Subject;
import se.alten.schoolproject.entity.Teacher;
import se.alten.schoolproject.exception.IncompleteFormException;
import se.alten.schoolproject.model.TeacherModel;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.Set;

public class TeacherMapper {

    public static Teacher toTeacherEntity(String teacherModel) {

        JsonReader reader = Json.createReader(new StringReader(teacherModel));
        JsonObject jsonObject = reader.readObject();

        Teacher teacher = new Teacher();

        if ( jsonObject.containsKey("lastName")) {
            teacher.setLastName(jsonObject.getString("lastName"));
        } else {
            throw new IncompleteFormException("{\"Name not set\"}");
        }

        if(jsonObject.containsKey("subjects")){
            JsonArray jsonArray = jsonObject.getJsonArray("subjects");
            for ( int i = 0; i < jsonArray.size(); i++ ){
                Subject subject = SubjectMapper.toSubjectEntity(jsonArray.get(i).toString());
                teacher.getSubjects().add(subject);
                subject.setTeacher(teacher);
            }
        }

        return teacher;
    }

    public static TeacherModel toTeacherModel(Teacher teacher){
        TeacherModel teacherModel = new TeacherModel();
        teacherModel.setLastName(teacher.getLastName());
        Set<String> subjectModels = SubjectMapper.toSubjectModelStringSet(teacher.getSubjects());
        teacherModel.setSubjects(subjectModels);
        return teacherModel;
    }
}
