/*
 * @Ruben@
 */

package com.ruben.editordetiles.canvas.utiles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Objects;

/**
 *
 * @author Ruben
 */
public class Rectangulo {

    Integer x,y,ancho,alto;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getAncho() {
        return ancho;
    }

    public void setAncho(Integer ancho) {
        this.ancho = ancho;
    }

    public Integer getAlto() {
        return alto;
    }

    public void setAlto(Integer alto) {
        this.alto = alto;
    }
    
    public Rectangulo(Integer x, Integer y, Integer ancho, Integer alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }
    public boolean contiene(Point point){
       return point.x>=x && point.x <=x+ancho &&
               point.y>=y && point.y<=y+alto;
    }
    public boolean esIgual(Rectangulo rect){
        return Objects.equals(rect.x, x) && 
                Objects.equals(rect.y, y) && 
                Objects.equals(rect.ancho, ancho) && 
                Objects.equals(alto, rect.alto);
    }
    
     public void establecer(int x,int y,int ancho,int alto){
        this.x=x;this.y=y;this.ancho=ancho;this.alto=alto;
    }
    public void establecer(Rectangulo rect){
        x=rect.x;y=rect.y;ancho=rect.ancho;alto=rect.alto;
    }
    
    public void dibujarRectangulo(Graphics2D g, Color c) {
      
        g.setColor(c);
        g.drawRect(x, y, ancho-1, alto-1);
       
    }
 public void dibujarRectangulo(Graphics2D g, Color c,int grosor) {
       
     g.setColor(c);
        g.setStroke(new BasicStroke(grosor)); 
        g.drawRect(x, y, ancho-1, alto-1);
        
    }
}
