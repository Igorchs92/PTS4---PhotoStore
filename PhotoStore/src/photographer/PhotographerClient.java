/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photographer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import client.ClientConnector;
import shared.ClientType;
/**
 *
 * @author Igor
 */
public class PhotographerClient {

    private static final Logger LOG = Logger.getLogger(PhotographerClient.class.getName());
    private static PhotographerClientRunnable clientRunnable;


    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // TODO code application logic here
        try {
            ClientConnector clientConnector = new ClientConnector(ClientType.photographer);
            clientRunnable = new PhotographerClientRunnable(clientConnector.getSocket());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }  
    }
    
}
