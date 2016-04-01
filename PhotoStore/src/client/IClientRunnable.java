/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import shared.ClientType;

/**
 *
 * @author Igor
 */
public interface IClientRunnable {

    public boolean login(String username, String password);
}
