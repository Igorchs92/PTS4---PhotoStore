/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author Igor
 */
public interface IClientRunnable {

    public void registerUser(String username, String password);

    public void loginUser(String username, String password);
}
