package com.hepta.funcionarios.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.hepta.funcionarios.entity.Funcionario;
import com.hepta.funcionarios.persistence.FuncionarioDAO;

@Path("/funcionarios")
public class FuncionarioService {

    private FuncionarioDAO dao;

    public FuncionarioService() {
        dao = new FuncionarioDAO();
    }

    /**
     * Adiciona novo Funcionario
     *
     * @param Funcionario: Novo Funcionario
     * @return response 200 (OK) - Conseguiu adicionar
     */
    @Path("/salvar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response funcionarioCreate(Funcionario funcionario) {
        try {
            dao.save(funcionario);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Response.status(Status.OK).build();
    }

    /**
     * Lista todos os Funcionarios
     *
     * @return response 200 (OK) - Conseguiu listar
     */
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response funcionarioRead() {
        List<Funcionario> funcionarios = new ArrayList<>();
        try {
            funcionarios = dao.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Erro ao buscar Funcionarios").build();
        }

        GenericEntity<List<Funcionario>> entity = new GenericEntity<List<Funcionario>>(funcionarios) {
        };
        return Response.status(Status.OK).entity(entity).build();
    }

    /**
     * Atualiza um Funcionario
     *
     * @param id:          id do Funcionario
     * @param Funcionario: Funcionario atualizado
     * @return response 200 (OK) - Conseguiu atualizar
     */
    @Path("/atualizar/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    public Response funcionarioUpdate(@PathParam("id") Integer id, Funcionario funcionario) throws Exception {

        if (id == null || id == 0) {
            throw new BadRequestException("ID do funcionário inválido");
        }

        Funcionario funcionarioExistente = dao.find(id);

        if (funcionarioExistente == null) {
            throw new NotFoundException("Funcionário não encontrado");
        }

        funcionarioExistente.setEmail(funcionario.getEmail());
        funcionarioExistente.setIdade(funcionario.getIdade());
        funcionarioExistente.setNome(funcionario.getNome());
        funcionarioExistente.setSalario(funcionario.getSalario());
        funcionarioExistente.setSetor(funcionario.getSetor());

        dao.update(funcionarioExistente);

        return Response.status(Status.OK).build();
    }

    /**
     * Remove um Funcionario
     *
     * @param id: id do funcionario
     * @return response 200 (OK) - Conseguiu remover
     */
    @Path("/deletar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @DELETE
    public Response FuncionarioDelete(@PathParam("id") Integer id) throws Exception {
        Funcionario funcionario = dao.find(id);
        if (funcionario == null) {
            throw new NotFoundException("Funcionário não encontrado");
        }
        dao.delete(id);
        return Response.ok().build();
    }

    /**
     * Métodos simples apenas para testar o REST
     *
     * @return
     */
    @Path("/teste")
    @Produces(MediaType.TEXT_PLAIN)
    @GET
    public String testeJersey() {
        return "Funcionando.";
    }

}
