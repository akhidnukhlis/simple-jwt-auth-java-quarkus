package akhid.development.controller;

import akhid.development.service.JwtService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/jwt")
@ApplicationScoped
public class JwtController {
    @Inject
    JwtService service;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getJwt() {
        String jwt = service.generateJwt();
        return Response.ok(jwt).build();
    }

    @GET
    @Path("/admin")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getJwtAdmin() {
        String jwt = service.generateJwtAdmin();
        return Response.ok(jwt).build();
    }
}
