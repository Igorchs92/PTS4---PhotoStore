/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.user.payment;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robin
 */
public class StoreCart {
    private List<StoreItem> items;
    public StoreCart() {
        items = new ArrayList<>();
    }
    
    public void addToCart(StoreItem item) {
        if(!items.contains(item)) {
            items.add(item);
        }
    }
    
    public void removeFromCart(StoreItem item) {
        items.remove(item);
    }
    
    public float getTotal() {
        float total=0;
        for(StoreItem i : items) {
            total += (i.getQuantity()*i.getPrice());
        }
        return total;
    }
    
    public int getNrOfItems() {
        return items.size();
    }
    
    public List getList() {
        return items;
    }
}
