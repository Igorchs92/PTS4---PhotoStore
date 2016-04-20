/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.files;

import java.awt.Image;
import java.io.File;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.util.Date;

/**
 *
 * @author Igor
 */
public class Picture implements Serializable {

    private int id;
    private String location;
    private String extension;
    private String name;
    private double price;
    private Date created;

    public Picture(String location, String name, double price, Date created) {
        this.location = location;
        this.extension = getFileExtension(new File(location));
        this.name = name;
        this.price = price;
        this.created = created;
    }    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getCreated() {
        return created;
    }

    public File getFile() {
        return new File(location);
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    @Override
    public String toString() {
        return  id + "." + extension;
    }

}
