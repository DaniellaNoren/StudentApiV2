package se.alten.schoolproject.exception;

import javax.ejb.ApplicationException;
import javax.ws.rs.WebApplicationException;

@ApplicationException
public class EntityNotFoundException extends WebApplicationException {

    public EntityNotFoundException(String msg){
        super(msg);
    }
}
