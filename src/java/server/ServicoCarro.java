/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jwt.JWTUtil;
import modelo.CarroDAO;
import modelo.Carro;
import modelo.Credencial;

/**
 *
 * @author Elder
 */
@Path("carros")
public class ServicoCarro {

    @EJB
    private CarroDAO carros;

    private List<Credencial> usuarios;

    public ServicoCarro() {
        carros = new CarroDAO();
        System.out.println("Construtor do serviço");
        usuarios = new ArrayList<>();

        Credencial c = new Credencial("usuario", "senha");
        usuarios.add(c);

    }

    @GET
    @Path("hello")
    @Produces(MediaType.APPLICATION_JSON)
    public Credencial hello() {
        return new Credencial("user", "pass");
    }

    /*Este método recebe uma credencial com usuário e senha e retorna
     o token para ser usado como autenticação nas futuras comunicações.
     */
    @POST
    @Path("/login")
    @Consumes("application/json")
    @Produces("application/json")
    public String login(Credencial credencial) {
        
        Boolean achou = false;
        for (Credencial c : usuarios) {
            if (credencial.getLogin().equals(c.getLogin()) && credencial.getSenha().equals(c.getSenha())) {
                achou = true;
                break;
            }
        }

        if (achou)
            return JWTUtil.create(credencial.getLogin());
        else 
            throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
    }
    
    @POST
    @Path("/session")
    @Consumes("application/json")
    @Produces("application/json")
    public String testaLogin( @HeaderParam("Authorization") String token )
    {
        if(token == null)
        {
            throw new WebApplicationException( Response.Status.FORBIDDEN );
        }
        
        System.out.println("Token recebido: "+ token);
        try{
            Jws<Claims> claims =  JWTUtil.decode(token);
            return "Você está autenticado como usuário " + claims.getBody().getSubject()+ " autenticado por " + claims.getBody().getIssuer();
        
        }catch(ExpiredJwtException ex)
        {
            throw new WebApplicationException("Tempo do token expirado.", Response.Status.FORBIDDEN);
        }catch(Exception e)
        {
            throw new WebApplicationException("Token inválido.", Response.Status.FORBIDDEN);
        }
    }
    
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Carro getCarro(@PathParam("id") int id) {
        System.out.println("entrou");

        for (Carro carro : carros.getAll()) {
            if (carro.getId() == id) {
                return carro;
            }
        }
        //dispara uma exception que na prática vai gerar um status code 404 para resposta
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @GET
    @Path("/all")
    @Produces("application/json")
    public List<Carro> getAll() {
        return carros.getAll();
    }

    @POST
    @Path("/")
    @Consumes("application/json")
    public synchronized void add(Carro c) {

        if (c.getNome() == null || c.getPreco() == null || c.getTipo() == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        carros.add(c);
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    public synchronized void update(@PathParam("id") int id, Carro c) {
        if (c.getNome() == null || c.getPreco() == null || c.getTipo() == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        System.out.println("Update... + " + id + " -" + c.getNome());
        Carro car = carros.get(id);
        if (car != null) {
            car = c;
        }
    }

    @DELETE
    @Path("/{id}")
    public synchronized void delete(@PathParam("id") int id) {
        System.out.println("Delete... " + id);
        carros.delete(id);
    }
    

}
