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
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    public ModifyColors color;
    public PhotoItem item;
    
    public PictureModifies(int photoId, int x1, int y1, int x2, int y2, ModifyColors color, PhotoItem item){
        this.photoId = photoId;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.item = item;
    }
}
