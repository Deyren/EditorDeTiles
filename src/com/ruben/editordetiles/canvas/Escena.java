/*
 * @Ruben@
 */
package com.ruben.editordetiles.canvas;

import com.ruben.editordetiles.Ventana;
import com.ruben.editordetiles.canvas.utiles.Matriz;
import com.ruben.editordetiles.canvas.utiles.Sprite;
import com.ruben.editordetiles.canvas.utiles.SpriteDeVariasTiles;
import com.ruben.editordetiles.componentes.BarraDeHerramientas;
import com.ruben.editordetiles.utils.Imagen;
import com.ruben.editordetiles.utils.Utiles;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

/**
 * Clase que dibuja en el canvas
 *
 * @author Ruben
 */
public class Escena{

    private final Ventana padre;
   // private final Rectangulo puntoDelRaton;
   // private final ArrayList<SpriteDeVariasTiles> spritesVariasTiles;
    private boolean controlPulsado;
    private boolean mayusPulsado;
    //private Sprite spriteActual;
    private final Matriz matriz;

    public Matriz getMatriz() {
        return matriz;
    }
    
    
    
    private final Canvas canvas;
    private BarraDeHerramientas.ModoDeDibujo modo=BarraDeHerramientas.ModoDeDibujo.PintaTexturas;

    public void setModo(BarraDeHerramientas.ModoDeDibujo modo) {
        this.modo = modo;
    }
    
    public Escena(Canvas canvas, Imagen imagenInicial, Ventana padre) {
        this.padre = padre;
        matriz = new Matriz(canvas, imagenInicial,60,60);
      //  puntoDelRaton = new Rectangulo(0, 0, 0, 0);
        canvas.addDibujable(matriz);
        this.canvas = canvas;
      //  spritesVariasTiles = new ArrayList<>();
       
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                pintarTile(e.getPoint());
                canvas.dibujar();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
//             matriz.dr(e.getPoint());
//             canvas.dibujar(); 
            }

        });
        canvas.addMouseWheelListener((MouseWheelEvent e) -> {
            // System.out.println("RUeda: " + e.getUnitsToScroll());
            final int cantidadMov=90;
            if (controlPulsado) {
                if (e.getUnitsToScroll() >= 0) {
                    Matriz.ZOOM -= cantidadMov;
                    matriz.actualizarTamanio();
                } else {
                    Matriz.ZOOM += cantidadMov;
                    matriz.actualizarTamanio();
                }
                
            } else if (mayusPulsado) {
                
                if (e.getUnitsToScroll() >= 0) {
                    matriz.moverEnHorizontal(-cantidadMov);
                    matriz.actualizarTamanio();
                } else {
                    matriz.moverEnHorizontal(cantidadMov);
                    matriz.actualizarTamanio();
                }
            } else {
                
                if (e.getUnitsToScroll() >= 0) {
                    matriz.moverEnVertical(-cantidadMov);
                    matriz.actualizarTamanio();
                } else {
                    matriz.moverEnVertical(cantidadMov);
                    matriz.actualizarTamanio();
                }
                
            }
            //  System.out.println(Matriz.ZOOM);
        });
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                pintarTile(e.getPoint());
                Sprite spp=matriz.choca(e.getPoint());
                if(spp!=null){
                    spp.setDibujaElRectangulo(false);
                }
                
                canvas.dibujar();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                canvas.requestFocus();
            }

        });
        canvas.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_CONTROL:
                        controlPulsado = true;
                        break;
                    case KeyEvent.VK_SHIFT:
                        mayusPulsado = true;
                        break;
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_CONTROL:
                        controlPulsado = false;
                        break;
                    case KeyEvent.VK_SHIFT:
                        mayusPulsado = false;
                        break;
                }

            }

        });

    }
/**
 * Reemplaza todos los tiles que contengan a 'actual'
 * por 'nuevo'
 * @param nuevo
 * @param actual 
 */
    public void reemplazarImagenDeTile(Imagen nuevo,Imagen actual){
        matriz.reemplazarImagenDeTile(nuevo, actual);
    }
    
    public void actualizarTamanioDeLaMatriz() {
        matriz.actualizarTamanio();
    }

    public void rellenarMatriz(Imagen imagen) {
        matriz.rellenarMatriz(imagen);
    }

     /**
     * Cambia la imagen de un tile. <br>
     * Se usa cuando se hace click en el canvas, <br>
     * -Obtiene el objeto Sprite en el que se ha clickeado. <br>
     * -Busca el archivo del boton seleccionado de BarraDeImagenes para obtener
     * el objeto File. <br>
     * -Obtiene el Sprite asociado a ese File del array de Utiles. <br>
     * -Establece el file y el buffer del array hacia el tile. <br>
     *
     * @param p
     */
    private void cambiarImagenDeTile(Point p){
        
        Sprite aux = matriz.choca(p);//Obtiene el Sprite que esta en la posicion del raton y que esta dentro de la matriz
        Imagen imagenSeleccionada = padre.getImagenSeleccionada();//Obtiene el archivo seleccionado en le panel o null si no hay seleccion

        //Si el raton esta en un sprite de la matriz y hay algun boton de imagen seleccionado...
        if (imagenSeleccionada != null && aux != null) {
            //Si el nombre del archivo obtenido es distinto al del archivo seleccionado...
            if (!aux.getImagen().equals(imagenSeleccionada)) {
                Imagen result = Utiles.buscarArchivoEnElArray(imagenSeleccionada.getFile());
                if (result != null) {

                    //Si es un sprite de varias tiles
                    if (imagenSeleccionada.getRutaAbsoluta().contains(SpriteDeVariasTiles.SPRITE_ARRAY)) {
                        SpriteDeVariasTiles svt = Utiles.getSpriteDeVariasTiles(imagenSeleccionada.getRutaAbsoluta());
                        if (svt != null){
                            //aux.setImagen(result);
                            Sprite sig = aux;
                            int fila = 1, columna = 0;
                            
                            for (int i = 0; i < svt.getSprites().size(); i++) {
                                Imagen immm = svt.getSprite(i).getImagen();    
                                if (columna < svt.getTilesEnAncho()) {
                                   if(sig!=null){
                                         sig.setImagen(immm);
                                   }
                                      
                                    if (columna != svt.getTilesEnAncho() - 1) {
                                        sig = matriz.getSiguienteHorizontal(sig);
                                             columna++;
                                    }else{//Si es la ultma columna...
                                        sig = matriz.getSiguienteVertical(aux, fila);
                                        fila++;
                                        columna = 0;
                                    }
                                  
                                } 
                             //   System.out.println("Clase Escena -> fila: " + fila + " columnas: " + columna + " imagen: " + immm.getFile().getName());

                            }
                        }
                    } else {
                        aux.setImagen(result);
                    }

                }

            }
        }

    }
    
    private void pintarLineas(Point p){
         Sprite sp = matriz.choca(p);
         BufferedImage aux=sp.getImagen().getBuffer();
         aux.setRGB(p.x-sp.getX(), p.y-sp.getY(), Color.WHITE.getRGB()); 
    }
   
    private void pintarTile(Point p) {
                switch(modo){
                    case PintaTexturas:cambiarImagenDeTile(p);
                    break;
                    case PintaLineas:pintarLineas(p);
                }
    }

    public void mostrarMatriz(boolean siONo) {
        matriz.setDibujarMatriz(siONo);

    }


}
