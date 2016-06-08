/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.producer;

import client.IClientRunnable;
import client.photographer.PhotographerClientRunnable;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import shared.SocketConnection;
import shared.producer.ProducerCall;

/**
 *
 * @author Igor
 */
public class ProducerClientRunnable implements IClientRunnable {

    private SocketConnection socket;
    public static ProducerClientRunnable clientRunnable;
    private HashMap<String, String> stats;
    private List<Pair<String, Double>> income24h;
    private List<Pair<String, Integer>> pictures7d;
    private List<Pair<String, Double>> photographers30d;

    public ProducerClientRunnable(SocketConnection socket) throws IOException, ClassNotFoundException {
        clientRunnable = this;
        this.socket = socket;
        testConnection();
    }

    public void testConnection() throws IOException, ClassNotFoundException {
        socket.writeObject(ProducerCall.test);
        boolean send = false;
        socket.writeObject(send);
        Logger.getAnonymousLogger().log(Level.INFO, "Message sent: {0}", send);
        boolean receive = (boolean) socket.readObject();
        Logger.getAnonymousLogger().log(Level.INFO, "Message received: {0}", receive);
    }

    public boolean registerUser(String email, String password, String name) {
        try {
            socket.writeObject(ProducerCall.register);
            socket.writeObject(new String[]{email, password, name});
            return (boolean) socket.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ProducerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean registerPhotographer(String email, String password, String name, String address, String zipcode, String city, String country, String phone, String kvk) {
        try {
            socket.writeObject(ProducerCall.new_photographer);
            socket.writeObject(new String[]{email, password, name, phone, address, zipcode, city, country, kvk});
            return (boolean) socket.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(PhotographerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean login(String email, String password) {
        try {
            socket.writeObject(ProducerCall.login);
            socket.writeObject(new String[]{email, password});
            return (boolean) socket.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ProducerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void reloadStats() {
        try {
            socket.writeObject(ProducerCall.getstats);
            stats = (HashMap<String, String>) socket.readObject();
            income24h = (List<Pair<String, Double>>) socket.readObject();
            pictures7d = (List<Pair<String, Integer>>) socket.readObject();
            photographers30d = (List<Pair<String, Double>>) socket.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ProducerClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap<String, String> getStats() {
        return stats;
    }

    public List<Pair<String, Double>> getIncome24h() {
        return income24h;
    }

    public List<Pair<String, Integer>> getPictures7d() {
        return pictures7d;
    }

    public List<Pair<String, Double>> getPhotographers30d() {
        return photographers30d;
    }
}
