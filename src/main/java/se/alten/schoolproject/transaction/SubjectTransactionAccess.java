package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Subject;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SubjectTransactionAccess {
    List listAllSubjects();
    Subject addSubject(Subject subject);
    Subject removeSubject(Subject subject);
    Subject getSubjectByTitle(String title);
    Subject updateSubjectTitle(String oldTitle, String newTitle);
}
