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

    private int id;
    private PictureGroup group;
    private List<Picture> personalPictures;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PictureGroup getGroup() {
        return group;
    }

    public void setGroup(PictureGroup group) {
        this.group = group;
    }

    public List<Picture> getPersonalPictures() {
        return personalPictures;
    }

    public void setPersonalPictures(List<Picture> personalPictures) {
        this.personalPictures = personalPictures;
    }

}
