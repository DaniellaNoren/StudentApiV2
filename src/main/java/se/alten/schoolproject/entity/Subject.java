package se.alten.schoolproject.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Subject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    @ManyToMany(mappedBy = "subjects", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    private Set<Student> students = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Teacher teacher;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(id, subject.id) &&
                Objects.equals(title, subject.title) &&
                Objects.equals(students, subject.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @PreRemove
    public void removeStudents(){
        for(Student s : students){
            s.getSubjects().remove(this);
        }
    }
}
