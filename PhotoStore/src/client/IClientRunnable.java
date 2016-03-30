/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import shared.user.Account;

/**
 *
 * @author Igor
 */
public interface IClientRunnable {

    public Account registerUser(String username, String password);

    public Account loginUser(String username, String password);
}
