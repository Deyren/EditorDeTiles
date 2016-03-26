package  com.ruben.editordetiles.canvas.utiles;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

public class Recta implements Serializable{

    private  final Point punto1;
    private  final Point punto2;
   
   
   
    public Point getPunto1() {
        return punto1;
    }

    public Point getPunto2() {
        return punto2;
    }
    
    private  float pendiente;
    private  final int ancho=100;
    public Recta(Point punto1, Point punto2) {
        this.punto1 = punto1;
        this.punto2 = punto2;
     
    }

    public Recta(int x1, int y1, int x2, int y2) {
        this.punto1 = new Point(x1, y1);
        this.punto2 = new Point(x2, y2);
       
    }

    private float equacionRectaDevuelveY(int posicionX) {

        /*
         y=mx+n
                                         y2-y1
         m=pendiente=    --------
                                        x2-x1        
         n= ordenada en el origen
         
         y=m*posicionX+n
        se pone un Y de uno de los puntos para sacar la ordenada
         */
        pendiente = (float)(punto2.y - punto1.y) / (punto2.x - punto1.x);
       // System.out.println("Pendiente: "+pendiente);
        float ordenada = punto1.y - (pendiente * punto1.x);
        float Y = (int) ((pendiente * posicionX) + ordenada);
       
        return Y;
    }


    public void dibujar(Graphics2D g) {
        g.setStroke(new BasicStroke(ancho));
        g.drawLine(punto1.x, punto1.y, punto2.x, punto2.y);       
    }
    
   
    public void dibujar(Graphics2D g,BufferedImage imagen) {
          
        g.setStroke(new BasicStroke(ancho));
        g.setPaint(new TexturePaint(imagen, new Rectangle(punto1.x, punto1.y, 64, 64)));
        g.drawLine(punto1.x, punto1.y, punto2.x, punto2.y);       
    }

}
