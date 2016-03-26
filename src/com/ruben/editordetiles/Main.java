/*
 * @Ruben@
 */
package com.ruben.editordetiles;

import com.ruben.editordetiles.utils.Imagenes;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *Cosas por hacer. <br>
 *  Arreglar el zoom
 * Determinar la carpeta donde se guarda cuando se recorta una imagen de una URL
 * Mover la matriz con Ctrl+arrastrar raton
 * Poner iconos a los botones.
 * Panel para editar brillo y color de la imagen.
 * Opcion de cambiar el color de la matriz.
 * Guardar el canvas como una imagen.
 * Cuando se elimina una imagen multiple, los buffers de la imagen siguen estando
 *      por que los contiene el array de imagenDeVariasTiles.!!! Se deben eliminar ¡¡¡
 * 
 * 
 * @author Ruben
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       initApp();
     // recortarImagen();
          
    }
     private static void initApp() {
        try {
            //UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new Ventana().setVisible(true);
        });
    }
     
     private static void recortarImagen(){
          String ruta="C:\\Users\\pedruben\\Documents\\NetBeansProjects\\EditorDeTiles\\public_html\\images\\prueba\\229.png";
        Imagenes.recortarImagen(new File(ruta), 40, 40);
     }
     
     
     
     
     
     
}
