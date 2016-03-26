/*
 * @Ruben@
 */
package com.ruben.editordetiles.canvas;

import com.ruben.editordetiles.canvas.utiles.Dibujable;
import com.sun.javafx.iio.ImageStorage;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import java.util.TimerTask;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


/**
 *Quien contenga al canvas llama a su metodo dibujar()
 *A su vez el canvas llama al metodo dibujar de quien implemente Dibujable
 *  que se le pasa con el metodo addDibujable(Dibujable);
 * @author Ruben
 */
public class Canvas extends java.awt.Canvas {

    //private Tiempo t;
    private VolatileImage volatileImg;
   
    
   
    private Dibujable dibujable;
    public void addDibujable(Dibujable d){
       this.dibujable=d; 
    }
    
//    private DibujadorListener dibujador;
//   public void setOnDibujadorListener(DibujadorListener listener){
//       this.dibujador=listener;
//   }
   
    private Canvas(Component padre) throws IOException {
       // escena = new Escena();
        setBounds(padre.getBounds());
        setBackground(Color.black);
   
     
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        
        
    }

    public static Canvas getInstance(Component padre) throws IOException {
        Canvas can = new Canvas(padre);
        //can.t = Tiempo.getInstance(can);
        
        return can;
    }
    
    public void dibujar(){
      
        repaint();
    }
    //Se sobreescribe update porque
//el metodo original borra la pantalla y eso crea un parpadeo
    @Override
    public void update(Graphics g) {
        paint(g);
    }

   // int coloor=0;
    @Override
    public void paint(Graphics g) {
         //dibujable.dibujar((Graphics2D)g);
        
        createBackBuffer();
        do {
            
            GraphicsConfiguration gc = this.getGraphicsConfiguration();
            int valCode = volatileImg.validate(gc);

            // This means the device doesn't match up to this hardware accelerated image.
            if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
                createBackBuffer(); // recreate the hardware accelerated image.
            }
            Graphics2D offscreenGraphics = (Graphics2D)volatileImg.getGraphics();
            
            offscreenGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);     
            offscreenGraphics.setColor(new Color(125, 85, 125));//Color de fondo
            offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());          
            
          //  dibujador.dibujar(offscreenGraphics);//dibuja todo
         dibujable.dibujar(offscreenGraphics);
            g.drawImage(volatileImg, 0, 0, this);
        } while (volatileImg.contentsLost());

    }

    
    public void guardarComoImagen(File archivo,String formato){
        BufferedImage bim;
        if(formato.equals("png")){
              bim=new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        }else{
            bim=new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        }
        Graphics g = bim.getGraphics();
        this.paint(g);
        
        try {
            ImageIO.write(bim, formato, archivo);
        } catch (IOException ex) {
            Logger.getLogger(Canvas.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       }
    
     // This method produces a new volatile image.

    private void createBackBuffer() {
        GraphicsConfiguration gc = getGraphicsConfiguration();
        volatileImg = gc.createCompatibleVolatileImage(getWidth(), getHeight());
    }
    

}

final class Tiempo extends TimerTask {

    private final Timer timer;
    private final Canvas canvas;

    public static Tiempo getInstance(Canvas canvas) {
        Tiempo tie = new Tiempo(canvas);
        tie.initTimer();
        return tie;

    }

    private Tiempo(Canvas canvas) {
        timer = new Timer();
        this.canvas = canvas;

    }

    private void initTimer() {
        timer.schedule(this, 0, 90);
        run();
    }

    @Override
    public void run() {
        canvas.repaint();
    }

    public final void empezar() {
        this.run();
    }
}
