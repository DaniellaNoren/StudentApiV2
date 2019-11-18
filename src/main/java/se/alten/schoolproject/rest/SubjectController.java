package se.alten.schoolproject.rest;

import lombok.NoArgsConstructor;
import se.alten.schoolproject.dao.SchoolAccessLocal;
import se.alten.schoolproject.model.SubjectModel;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
@NoArgsConstructor
@Path("/api/subjects")
public class SubjectController {

    @Inject
    private SchoolAccessLocal sal;

    @GET
    @Produces({"application/JSON"})
    public Response listSubjects() {
            List subject = sal.listAllSubjects();
            return Response.ok(subject).build();
    }

    @GET
    @Produces({"application/JSON"})
    @Path("{title}")
    public Response getSubjectByTitle(@PathParam("title") String title){
            SubjectModel subjectmodel = sal.getSubjectByTitle(title);
            return Response.ok(subjectmodel).build();
    }

    @POST
    @Produces({"application/JSON"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addSubject(String subject) {
            SubjectModel subjectModel = sal.addSubject(subject);
            return Response.ok(subjectModel).build();
    }

    @DELETE
    @Produces({"application/JSON"})
    @Path("{title}")
    public Response removeSubject(@PathParam("title")String title){
        SubjectModel subjectModel = sal.removeSubject(title);
        return Response.ok(subjectModel).build();
    }

    @PATCH
    @Produces({"application/JSON"})
    @Path("{title}")
    public Response updateSubjectTitle(@PathParam("title") String title, @QueryParam("newTitle")String newTitle){
        SubjectModel subjectModel = sal.updateSubjectTitle(title, newTitle);
        return Response.ok(subjectModel).build();
    }
}
