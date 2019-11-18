package se.alten.schoolproject.model;

import lombok.*;
import se.alten.schoolproject.entity.Subject;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubjectModel implements Serializable{

    private String title;
    private Set<StudentModel> students;
    private String teacherName;
}
