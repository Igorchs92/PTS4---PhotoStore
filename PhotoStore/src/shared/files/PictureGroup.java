/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.files;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Igor
 */
public class PictureGroup implements Serializable {

    private int id;
    private String name;
    private String description;
    private List<Picture> pictures;
    private List<PersonalPicture> personalPictures;


    public PictureGroup(int id) {
        this.id = id;
        pictures = new ArrayList<>();
        personalPictures = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public void addPicture(Picture picture) {
        this.pictures.add(picture);
    }
    
    public void removePicture(Picture picture) {
        this.pictures.remove(picture);
    }

    public List<PersonalPicture> getPersonalPictures() {
        return personalPictures;
    }

    public void setPersonalPictures(List<PersonalPicture> persons) {
        this.personalPictures = persons;
    }
    
    public void addPersonalPicture(PersonalPicture personalPicture){
        this.personalPictures.add(personalPicture);
    }
    
    public void removePersonalPicture(PersonalPicture personalPicture){
        this.personalPictures.remove(personalPicture);
    }
    
    
    @Override
    public String toString(){
        return name + "aseygoaa";
    }
}
