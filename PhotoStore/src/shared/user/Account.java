/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.user;

/**
 *
 * @author Robin
 */
public abstract class Account {
    private String username;
    private String name;
    private String adress;
    private String phoneNumber;
    
    public Account(String username, String password, String adress, String phonenumber) {
        this.username = username;
        this.name = name;
        this.adress = adress;
        this.phoneNumber = phonenumber;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getName() {
        return name;
    }
    
    public String getAdress() {
        return adress;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
}
