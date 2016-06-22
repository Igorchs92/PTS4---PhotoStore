/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.user.payment;

/**
 *
 * @author Robin
 */
public class Item implements StoreItem{

    private String name;
    private float price;
    private int quantity;
    
    public Item(String name, float price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public float getPrice() {
        return price;
    }
    
    @Override
    public String toString() {
        return name + "\t\t" + quantity + "x €" + price;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity=quantity;
    }
    
}
