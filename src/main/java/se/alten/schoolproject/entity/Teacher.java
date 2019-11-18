package se.alten.schoolproject.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="teacher")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Teacher implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "lastName", unique = true)
    private String lastName;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    Set<Subject> subjects = new HashSet<>();

    @PreRemove
    public void removeTeacherFromSubjects(){
        for(Subject s : subjects){
            s.setTeacher(null);
        }
        subjects.clear();
    }

}
