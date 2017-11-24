/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author elder
 */
//Marcações para mapear a classe para XML
@XmlRootElement
public class Carro {
    
    private String nome;
    private Double preco;
    private String tipo;
    private int id;
    
    public Carro()
    {
    }

    public Carro(String nome, Double preco, String tipo, int id) {
        this.nome = nome;
        this.preco = preco;
        this.tipo = tipo;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    
    
}
