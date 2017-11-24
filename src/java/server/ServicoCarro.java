/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;


import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import modelo.CarroDAO;
import modelo.Carro;

/**
 *
 * @author Elder
 */
@Path("carros")
public class ServicoCarro {
    
    @EJB
    private CarroDAO carros;
    
    public ServicoCarro() {
        carros = new CarroDAO();
        System.out.println("Construtor do serviço");
    }
    
    @GET
    @Path("hello")
    @Produces("text/plain")
    public String hello()
    {
        return "Oi";
    }
    
    @GET
    @Path("{id}")
    @Produces("application/json")
    public Carro getCarro( @PathParam("id") int id)
    {
        System.out.println("entrou");
       
        for (Carro carro : carros.getAll()) {
            if( carro.getId() == id)
                return carro;
        }
        //dispara uma exception que na prática vai gerar um status code 404 para resposta
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }
    
    @GET
    @Path("all")
    @Produces("application/json")
    public List<Carro> getAll()
    {
        return carros.getAll();
    }
    
    @POST
    @Path("")
    @Consumes("application/json")
    public synchronized void add(Carro c)
    {
        
        if( c.getNome() == null || c.getPreco() == null || c.getTipo() == null  ){
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        
        carros.add(c);
    }
    
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public synchronized void update(@PathParam("id") int id, Carro c)
    {
        if( c.getNome() == null || c.getPreco() == null || c.getTipo() == null  ){
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        System.out.println("Update... + "+id+" -"+ c.getNome());
        Carro car = carros.get(id);
        if(car!=null)
            car = c;
    }
    
    @DELETE
    @Path("{id}")
    public synchronized void delete(@PathParam("id")int id)
    {
        System.out.println("Delete... "+ id);
        carros.delete(id);
    }
}
