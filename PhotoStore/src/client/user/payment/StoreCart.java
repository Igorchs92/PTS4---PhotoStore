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
public final class StoreCart {
    private static List<StoreItem> items = new ArrayList<>();    
    
    public static void addToCart(StoreItem item) {
        if(!items.contains(item)) {
            items.add(item);
        }
    }
    
    public static void removeFromCart(StoreItem item) {
        items.remove(item);
    }
    
    public static void clearCart() {
        items.clear();
    }
    
    public static float getTotal() {
        float total=0;
        for(StoreItem i : items) {
            total += (i.getQuantity()*i.getPrice());
        }
        return total;
    }
    
    public static int getNrOfItems() {
        return items.size();
    }
    
    public static List getList() {
        return items;
    }
}
