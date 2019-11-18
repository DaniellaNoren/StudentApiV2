package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Subject;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.*;
import java.util.List;

@Stateless
@Default
public class SubjectTransaction implements SubjectTransactionAccess{

    @PersistenceContext(unitName="school")
    private EntityManager entityManager;

    @Override
    public List<Subject> listAllSubjects() {
        return entityManager.createQuery("SELECT s FROM Subject s", Subject.class).getResultList();
    }

    @Override
    public Subject addSubject(Subject subject) {
            entityManager.persist(subject);
            entityManager.flush();
            return subject;
    }

    @Override
    public Subject removeSubject(Subject subject) {
        entityManager.remove(subject);
        entityManager.flush();
        return subject;
    }

    @Override
    public Subject getSubjectByTitle(String title) {
        Query query = entityManager.createQuery("SELECT s FROM Subject s WHERE s.title = :title");
        query.setParameter("title", title);
        List subject = query.getResultList();
        if(subject.isEmpty()){
            return null;
        }else{
            return (Subject)subject.get(0);
        }
    }

    @Override
    public Subject updateSubjectTitle(String oldTitle, String newTitle) {
        Query query = entityManager.createQuery("UPDATE Subject s SET s.title = :newTitle WHERE s.title = :oldTitle");
        query.setParameter("oldTitle", oldTitle).setParameter("newTitle", newTitle).executeUpdate();
        return this.getSubjectByTitle(newTitle);
    }
}
