/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.files;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Igor
 */
public class PersonalPicture implements Serializable {

    @Override
    public String toString() {
        return Integer.toString(id);
    }

    private int id;
    private List<Picture> pictures;
    
    
    public PersonalPicture(int idnumber){
        this.id = idnumber;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }
    
    public void removePicture(Picture picture){
        this.pictures.remove(picture);
    }
    
    public void addPicture(Picture picture){
        this.pictures.add(picture);
    }
}
