package se.alten.schoolproject.exception;

import javax.ejb.ApplicationException;
import javax.ws.rs.WebApplicationException;

@ApplicationException
public class EntityNotUniqueException extends WebApplicationException {

    public EntityNotUniqueException(String msg){
        super(msg);
    }
}
