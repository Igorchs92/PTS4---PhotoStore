/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import client.ClientConnector;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ClientType;

/**
 *
 * @author Igor
 */
public class UserClient {

    private static final Logger LOG = Logger.getLogger(UserClient.class.getName());
    private static photographer.PhotographerClientRunnable clientRunnable;


    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // TODO code application logic here
        try {
            ClientConnector clientConnector = new ClientConnector(ClientType.user);
            clientRunnable = new photographer.PhotographerClientRunnable(clientConnector.getSocket());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }  
    }
    
}
