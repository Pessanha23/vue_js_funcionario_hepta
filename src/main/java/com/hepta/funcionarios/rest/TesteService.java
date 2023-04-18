package com.hepta.funcionarios.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/teste")
public class TesteService {

    @Produces(MediaType.TEXT_PLAIN)
    @GET
    public String testeJersey() {
        return "Testando Jersey.";
    }
}
