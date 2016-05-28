/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.files;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.text.NumberFormat;

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
    private boolean uploaded;
    private boolean updated;

    public Picture(String location, String name, double price) {
        this.location = location;
        this.extension = getFileExtension(new File(location));
        if (!name.isEmpty()) {
            this.name = name;
        } else {
            this.name = "Untitled";
        }
        this.price = price;
        this.created = new Date();
        this.uploaded = false;
        this.updated = true;
    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExtension() {
        return extension;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || this.name == name) {
            return;
        }
        this.name = name;
        this.updated = false;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (this.price == price) {
            return;
        }
        this.price = price;
        this.updated = false;
    }

    public Date getCreated() {
        return created;
    }

    public String getCreatedString() {
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm");
        df.setTimeZone(TimeZone.getDefault());
        return df.format(created);
    }

    public void setFile(String location) {
        this.location = location;
        this.extension = getFileExtension(new File(location));
    }

    public void setFile(File file) {
        this.location = file.toString();
        this.extension = getFileExtension(file);
    }

    public File getFile() {
        return new File(location);
    }

    public String getRelativePath() {
        return id + "." + extension.toLowerCase();
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded() {
        this.uploaded = true;
        setUpdated();
    }

    public boolean isUpdated() {
        if (id == 0) {
            return true;
        }
        return updated;
    }

    public void setUpdated() {
        this.updated = true;
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
        } else {
            return "";
        }
    }

    @Override
    public String toString() {
        String t = "";
        String s = name + " - " + NumberFormat.getCurrencyInstance().format(price) + "";
        if (id != 0) {
            if (uploaded && !updated) {
                t = "[C] ";
            }
            return t + id + ": " + s;
        } else {
            return "[U] " + s;
        }
    }
}
