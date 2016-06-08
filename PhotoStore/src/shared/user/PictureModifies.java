/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.user;

import java.io.Serializable;

/**
 *
 * @author martijn
 */
public class PictureModifies implements Serializable{
    
    public int photoId;
    public double x1;
    public double y1;
    public double x2;
    public double y2;
    public ModifyColors color;
    public PhotoItem item;
    
    public PictureModifies(int photoId, double x1, double y1, double x2, double y2, ModifyColors color, PhotoItem item){
        this.photoId = photoId;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.item = item;
    }
}
