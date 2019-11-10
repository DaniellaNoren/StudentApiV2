package se.alten.schoolproject.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class IncompleteFormExceptionMapper implements ExceptionMapper<IncompleteFormException> {
    @Override
    public Response toResponse(IncompleteFormException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
}
