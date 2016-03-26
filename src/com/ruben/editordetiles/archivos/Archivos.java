/*
 * @Ruben@
 */
package com.ruben.editordetiles.archivos;

import com.ruben.editordetiles.canvas.utiles.Matriz;
import com.ruben.editordetiles.canvas.utiles.Sprite;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Ruben
 */
public class Archivos {

    /**
     * Guarda cada tile de la matriz como una imagen el nombre de la imagen es
     * un numero que va aumentando con cada imagen y crear un archivo java con
     * un array[][] e enteros cada valor del array apuanta a una imagen guardada
     *
     *
     *
     * @param matriz
     * @param carpetaDeSalida
     */
    public static void guardarTodo(Matriz matriz, File carpetaDeSalida) {
        
        Sprite[][] sprites = matriz.getSprites();
        int[][] arraySalida = new int[sprites.length][sprites[0].length];
        StringBuilder sb = new StringBuilder();

        //guarda el archivo con la array////////////////////
        sb.append("// Matriz de ").append(sprites.length).append("x").append(sprites[0].length).append("\n");
        sb.append("int[][] elArray=new int[][]{\n");
        for (int i = sprites.length-1; i >=0; i--) {
            sb.append("\t{");
            for (int j = 0; j < sprites[i].length; j++) {
                //    BufferedImage buff=sprites[i][j].getImagen().getBuffer();
                sb.append(sprites[i][j].getImagen().getNumeroId()).append(",");
            }
            sb.append("},\n");
        }
        sb.append("\n};");

        String nombreArchivo = "LaArray.txt";
        try {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(carpetaDeSalida + File.separator + nombreArchivo))) {
                bw.write(sb.toString());
            }

        } catch (IOException ex) {
            Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println(sb.toString());
//////////////////////////////////Crea los archivos de imagen
        for (int i = 0; i < sprites.length; i++) {
            for (int j = 0; j < sprites[i].length; j++) {
                arraySalida[i][j]=sprites[i][j].getImagen().getNumeroId();
                String n=String.valueOf(arraySalida[i][j])+".jpg";
                String nombre=carpetaDeSalida.getAbsolutePath()+File.separator+n;
                File f=new File(nombre);
                if(!f.exists()){
                    try {
                        ImageIO.write(sprites[i][j].getImagen().getBuffer(), "jpg", f);
                    } catch (IOException ex) {
                        Logger.getLogger(Archivos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
        }

    }

}
