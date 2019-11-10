package se.alten.schoolproject.exception;

import javax.ejb.ApplicationException;
import javax.ws.rs.WebApplicationException;

@ApplicationException(rollback = true)
public class StudentNotFoundException extends WebApplicationException {

    public StudentNotFoundException(String msg){
        super(msg);
    }
}
