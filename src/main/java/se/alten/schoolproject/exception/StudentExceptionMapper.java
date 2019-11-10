package se.alten.schoolproject.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class StudentExceptionMapper implements ExceptionMapper<StudentNotFoundException> {

    @Override
    public Response toResponse(StudentNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND).entity("Student not found").build();
    }
}
