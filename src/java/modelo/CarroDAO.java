/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;

/**
 * Esta classe tem o padrão DAO tradicional, simplificado para acessar a base de testes;
 * @author elder
 */
@Singleton
public class CarroDAO {
    private List<Carro> listaCarro;
    Gson gson;
    
    public CarroDAO() {
        gson = new Gson();
        //popula uma lista temporária para trabalhar
        listaCarro = new ArrayList<>();
        listaCarro.add( new Carro("Fusca", 7000.0, "coupe", 1) );
        listaCarro.add( new Carro("Chevette", 5000.0, "sedan esportivo", 2) );
        listaCarro.add( new Carro("Gol", 17000.0, "hatch", 3) );
    }
    
   public List<Carro> getAll()
   {
       return listaCarro;
       
   }
   
   public synchronized void add(Carro c)
   {
       listaCarro.add(c);
   }
   
   public Carro get(int id){
       for (Carro c : listaCarro) {
           if(c.getId() == id)
               return c;
       }
       return null;
     
   }
   
   public synchronized void update(Carro c) throws Exception{
       for (Carro car : listaCarro) {
           if(car.getId() == c.getId())
           {
               car = c;
               return;
           }
       }
       throw new Exception("Objeto não existente");
   
   }
   
   public synchronized void delete(int id)
   {
       for (Carro car : listaCarro) {
           if(car.getId() == id)
           {
               listaCarro.remove(car);
           }
       }
   }   
       
}
