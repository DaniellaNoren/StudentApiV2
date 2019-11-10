package se.alten.schoolproject.exception;

import javax.ejb.ApplicationException;
import javax.ws.rs.WebApplicationException;

@ApplicationException(rollback = true)
public class IncompleteFormException extends WebApplicationException {

    public IncompleteFormException(String msg){
        super(msg);
    }
}
