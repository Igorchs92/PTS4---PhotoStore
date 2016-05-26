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
    private boolean uploaded;

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
        if (name.isEmpty()) {
            this.name = "Untitled";
        } else {
            this.name = name;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description.isEmpty()) {
            this.description = "No description is available.";
        } else {
            this.description = description;
        }
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

    public void addPersonalPicture(PersonalPicture personalPicture) {
        this.personalPictures.add(personalPicture);
    }

    public void removePersonalPicture(PersonalPicture personalPicture) {
        this.personalPictures.remove(personalPicture);
    }
    
    public boolean getUploaded(){
        return uploaded;
    }
    
    public void setUploaded(){
        uploaded = true;
    }

    @Override
    public String toString() {
        return id + ": " + name + "; Pictures: " + pictures.size() + ", UID's: " + personalPictures.size();
    }

}
