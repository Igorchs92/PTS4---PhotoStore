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
    public double x;
    public double y;
    public double width;
    public double height;
    public ModifyColors color;
    public PhotoItem item;
    
    public PictureModifies(int photoId, double x, double y, double width, double height, ModifyColors color, PhotoItem item){
        this.photoId = photoId;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.item = item;
    }
}
