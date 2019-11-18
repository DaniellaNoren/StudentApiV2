package se.alten.schoolproject.model;

import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeacherModel implements Serializable {

    private String lastName;
    private Set<String> subjects;

}
