/*
 Juego de plataformas tipo Grizor...
 */
package com.ruben.editordetiles.canvas.utiles;

import com.ruben.editordetiles.utils.Imagen;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Sprite extends Rectangulo {
    
    
    Imagen imagen;
    BufferedImage otraCapa=null;
    public Imagen getImagen() {
        return imagen;
    }
    private boolean dibujaElRectangulo=false;

    public boolean isDibujaElRectangulo() {
        return dibujaElRectangulo;
    }

    public void setDibujaElRectangulo(boolean dibujandoRectangulo) {
        this.dibujaElRectangulo = dibujandoRectangulo;
    }

    /**
     * Se le pasa el objeto Imagen ya creado,
     * @param imagen 
     * @param ancho
     * @param alto
     */
    public Sprite(Imagen imagen, int ancho, int alto) {
        super(0, 0, ancho, alto);
       this.imagen=imagen;

    }

    /**
     * Establece su objeto bufferedImage y el nombre del archivo
     *
     * @param ima
     *
     */
    public void setImagen(Imagen ima) {
           otraCapa=this.imagen.getBuffer();
          this.imagen=ima;
       
       

    }

    
    
    public void dibujar(Graphics2D g) {
       
         if(otraCapa!=null){
            g.drawImage(otraCapa, this.x,
                this.y,
                this.ancho,
                this.alto, null);
        }
        g.drawImage(imagen.getBuffer(), this.x,
                this.y,
                this.ancho,
                this.alto, null);
       
        if(dibujaElRectangulo){
            dibujarRectangulo(g, Color.RED,3);
        }
    }

}
