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
final public class StoreCart {
    static private List<StoreItem> items = new ArrayList();
    
    static public void addToCart(StoreItem item) {
        if(!items.contains(item)) {
            items.add(item);
        }
    }
    
    static public void removeFromCart(StoreItem item) {
        items.remove(item);
    }
    
    static public float getTotal() {
        float total=0;
        for(StoreItem i : items) {
            total += (i.getQuantity()*i.getPrice());
        }
        return total;
    }
    
    static public int getNrOfItems() {
        return items.size();
    }
    
    static public List getList() {
        return items;
    }
    
    static public void clear() {
        items.clear();
    }
}
