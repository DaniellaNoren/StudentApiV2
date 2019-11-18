package se.alten.schoolproject.rest;

import lombok.NoArgsConstructor;
import se.alten.schoolproject.dao.SchoolAccessLocal;
import se.alten.schoolproject.model.TeacherModel;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
@NoArgsConstructor
@Path("/api/teachers")
public class TeacherController {

    @Inject
    private SchoolAccessLocal sal;

    @GET
    @Produces({"application/JSON"})
    public Response listTeachers() {
        List teachers = sal.listAllTeachers();
        return Response.ok(teachers).build();
    }

    @GET
    @Produces({"application/JSON"})
    @Path("{name}")
    public Response getTeacherByName(@PathParam("name")String name) {
        TeacherModel teacherModel = sal.getTeacherByName(name);
        return Response.ok(teacherModel).build();
    }

    @PATCH
    @Produces({"application/JSON"})
    @Path("{name}")
    public Response updateTeacherName(@PathParam("name")String name, @QueryParam("newName")String newName) {
        TeacherModel teacherModel = sal.updateTeacherName(name, newName);
        return Response.ok(teacherModel).build();
    }

    @DELETE
    @Produces({"application/JSON"})
    @Path("{name}")
    public Response removeTeacher(@PathParam("name") String name){
        TeacherModel teacherModel = sal.deleteTeacher(name);
        return Response.ok(teacherModel).build();
    }

    @POST
    @Produces({"application/JSON"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTeacher(String teacherModel){
        TeacherModel teacher = sal.addTeacher(teacherModel);
        return Response.status(Response.Status.CREATED).entity(teacher).build();
    }

    @PATCH
    @Path("{name}/{title}")
    @Produces({"application/JSON"})
    public Response addSubject(@PathParam("name") String name, @PathParam("title")String title){
        TeacherModel teacherModel = sal.addTeacherToSubject(name, title);
        return Response.ok(teacherModel).build();
    }
}
