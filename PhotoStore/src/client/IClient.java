/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author IGOR
 */
public interface IClient {
    public boolean login(String username, String password);
    
    public void setSceneRegister();

    public void setSceneMain();
    
    public void loggedIn();
}
