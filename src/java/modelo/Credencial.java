/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author elder
 */
public class Credencial {
    
    private String login, senha;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Credencial() {
    }

    public Credencial(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }
    
    
    
}
