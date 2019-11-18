package se.alten.schoolproject.rest;

import lombok.NoArgsConstructor;
import org.jboss.resteasy.annotations.Body;
import se.alten.schoolproject.dao.SchoolAccessLocal;
import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.service.StudentMapper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
@NoArgsConstructor
@Path("/api/students")
public class StudentController {

    @Inject
    private SchoolAccessLocal sal;

    @Inject
    private StudentMapper mapper;

    @GET
    @Produces({"application/JSON"})
    public Response showStudents(@QueryParam("lastName")String lastName) {
            List students;
            if(lastName != null){
                students = sal.getStudentsByLastName(lastName);
            }else {
                students = sal.listAllStudents();
            }

            return Response.ok(students).build();

    }

    @GET
    @Path("{email}")
    @Produces({"application/JSON"})
    public Response getStudentByEmail(@PathParam("email")String email){
       StudentModel student = sal.getStudentByEmail(email);
       return Response.ok(student).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({"application/JSON"})
    public Response addStudent(String student) {
            StudentModel answer = sal.addStudent(student);
            return Response.ok(answer).build();
    }

    @DELETE
    @Path("{email}")
    @Produces({"application/JSON"})
    public Response deleteUser(@PathParam("email") String email) {
            StudentModel removedStudent = sal.removeStudent(email);
            return Response.ok(removedStudent).build();
    }

    @PUT
    @Path("{email}")
    @Produces({"application/JSON"})
    public Response updateStudent(@PathParam("email")String email,
                                  @QueryParam("forename") String forename,
                                  @QueryParam("lastName") String lastName) {
            StudentModel updatedStudent = sal.updateStudent(forename, lastName, email);
            return Response.ok(updatedStudent).build();
    }

    @PATCH
    @Path("{email}")
    @Produces({"application/JSON"})
    public Response updatePartialAStudent(@QueryParam("forename") String forename,
                                          @PathParam("email") String email) {
            StudentModel updatedStudent =  sal.updateStudentPartial(forename, email);
            return Response.ok(updatedStudent).build();
    }

    @PATCH
    @Path("{email}/{title}")
    @Produces({"application/JSON"})
    public Response updateSubjectList(@PathParam("title") String title,
                                          @PathParam("email") String email) {
        StudentModel updatedStudent =  sal.addStudentToSubject(email, title);
        return Response.ok(updatedStudent).build();
    }



}
