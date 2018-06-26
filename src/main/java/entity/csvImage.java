/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author taleb
 */
public class csvImage {
    private String path;
    private int width;
    private int lenght;

    public csvImage() {
    }

    public csvImage(String path, int width, int lenght) {
        this.path = path;
        this.width = width;
        this.lenght = lenght;
    }

    public int getLenght() {
        return lenght;
    }

    public String getPath() {
        return path;
    }

    public int getWidth() {
        return width;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    
    
}
