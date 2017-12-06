/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;


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

    
    private Credencial c;

    public ServicoCarro() {
        carros = new CarroDAO();
        System.out.println("Construtor do serviço");
        
        c = new Credencial();
        c.setLogin("ele");
        c.setSenha("aaa");

    }

    @GET
    @Path("hello")
    @Produces(MediaType.APPLICATION_JSON)
    public Credencial hello() {
        
        return c;
    }

    /*Este método recebe uma credencial com usuário e senha e retorna
     o token para ser usado como autenticação nas futuras comunicações.
     */
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String login(Credencial credencial) {
        
        System.out.println("Login: "+ credencial.getLogin()+"; senha:  " + credencial.getSenha());
        Boolean achou = false;
        
            if (credencial.getLogin().equals(c.getLogin()) && credencial.getSenha().equals(c.getSenha())) {
                achou = true;
               
            }
        

        if (achou)
            return JWTUtil.create(credencial.getLogin());
        else 
            throw new WebApplicationException(Response.Status.NOT_FOUND);
    }
    
    /*
    GET
    /session
    "Autorization": "token"
    
    corpo...
    */
    
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
        System.out.println("entrou no get carro");

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
    public synchronized void add(Carro c, @HeaderParam("Authorization")String token) {

        //verificar a autenticação
        if(token == null)
        {
            throw new WebApplicationException( Response.Status.FORBIDDEN );
        }
        
        System.out.println("Token recebido: "+ token);
        try{
            Jws<Claims> claims =  JWTUtil.decode(token);
             if (c.getNome() == null || c.getPreco() == null || c.getTipo() == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        carros.add(c);
         
        }catch(ExpiredJwtException ex)
        {
            throw new WebApplicationException("Tempo do token expirado.", Response.Status.FORBIDDEN);
        }catch(Exception e)
        {
            throw new WebApplicationException("Token inválido.", Response.Status.FORBIDDEN);
        }
       
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    public synchronized void update(@PathParam("id") int id, Carro c, @HeaderParam("Authorization") String token ) {
         
        String user = testaToken(token);
        System.out.println("Usuario " + user + " com token: " +token);
        
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
    
private String testaToken( String token )
{
     if(token == null)
        {
            throw new WebApplicationException( Response.Status.FORBIDDEN );
        }
        
        System.out.println("Token recebido: "+ token);
        try{
            Jws<Claims> claims =  JWTUtil.decode(token);
            return  claims.getBody().getSubject();
        
        }catch(ExpiredJwtException ex)
        {
            throw new WebApplicationException("Tempo do token expirado.", Response.Status.FORBIDDEN);
        }catch(Exception e)
        {
            throw new WebApplicationException("Token inválido.", Response.Status.FORBIDDEN);
        }
}
    
    
}
