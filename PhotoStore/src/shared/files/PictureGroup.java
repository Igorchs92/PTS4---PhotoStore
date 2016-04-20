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
    private List<Picture> groupPictures;

    public PictureGroup(int id) {
        this.id = id;
        groupPictures = new ArrayList<>();
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

    public List<Picture> getGroupPictures() {
        return groupPictures;
    }

    public void setGroupPictures(List<Picture> groupPictures) {
        this.groupPictures = groupPictures;
    }

    public void addGroupPicture(Picture picture) {
        groupPictures.add(picture);
    }

}
