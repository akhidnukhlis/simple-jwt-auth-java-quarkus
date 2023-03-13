package akhid.development.controller;

import akhid.development.model.postgres.Cake;
import akhid.development.service.CakeService;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.ValidationException;
import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.jboss.resteasy.plugins.providers.jsonb.i18n.LogMessages.LOGGER;

@SecurityScheme(
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT"
)
@Path("/api/v1/product")
public class CakeController {
    @Inject
    CakeService cakeService;

    @GET
    @PermitAll
    @Path("/cakes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        LOGGER.info("Cake find all");

        Map<String, Object> result = new HashMap<>();
        try {
            result.put("statusCode", 200);
            result.put("data", Cake.findAll().list());
            return Response.ok(result).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            result.put("statusCode", 500);
            result.put("message", "INTERNAL_SERVER_ERROR");
            return Response.serverError()
                    .entity(result)
                    .build();
        }
    }

    @POST
    @RolesAllowed("writer")
    @Path("/cake/submit")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response submit (Cake cake) {
        LOGGER.info("Cake submit");

        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> payload = cakeService.submit(cake);

            result.put("statusCode", 201);
            result.put("data", payload);
            return Response.status(Response.Status.CREATED)
                    .entity(result)
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();
            result.put("statusCode", 500);
            result.put("message", "INTERNAL_SERVER_ERROR");
            return Response.serverError()
                    .entity(result)
                    .build();
        }
    }

    @DELETE
    @RolesAllowed("admin")
    @Path("/cake/delete/id/{id}")
    public Response deleteById (
            @PathParam("id")
            @Pattern(regexp = "[\\w]{8}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{12}", message = "Pattern not match")
            String uuid) {
        LOGGER.info("Cake delete by id");

        Map<String, Object> result = new HashMap<>();
        try {
            result.put("statusCode", 200);
            result.put("data", cakeService.deleteById(uuid));
            return Response.ok(result).build();
        } catch (ValidationException ex) {
            result.put("message", ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        } catch (NotFoundException nfe) {
            result.put("message", nfe.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(result).build();
        }  catch (Exception ex) {
            ex.printStackTrace();
            result.put("statusCode", 500);
            result.put("message", "INTERNAL_SERVER_ERROR");
            return Response.serverError()
                    .entity(result)
                    .build();
        }
    }
}
