/*
 * @Ruben@
 */
package com.ruben.editordetiles.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

/**
 *Sirve como comunicacion entre los botones con imagen
 *  y los sprites, Amboss requieren un file y un bufferedImage
 * 
 * @author Ruben
 */
public class Imagen {

    private File file;
    private String rutaAbsoluta;
    private BufferedImage buffer;
    private final Integer numeroId;

    public Integer getNumeroId() {
        return numeroId;
    }
    
    private static int autoIncrement;
    public String getRutaAbsoluta() {
        return rutaAbsoluta;
    }

    public void setRutaAbsoluta(String rutaAbsoluta) {
        this.rutaAbsoluta = rutaAbsoluta;
    }

    public File getFile() {
        return file;
    }

    public BufferedImage getBuffer() {
        return buffer;
    }

    /**
     * Devuelve un objeto Imagen, o null si algun parametro es null
     *
     * @param file
     * @param buffer
     * @return
     */
    public static Imagen getInstance(File file, BufferedImage buffer) {
        if (file == null || buffer == null) {
            return null;
        }
        return new Imagen(file, buffer);
    }

    private Imagen(File file, BufferedImage buffer) {
        this.file = file;
        this.buffer = buffer;
        this.rutaAbsoluta=file.getAbsolutePath();
       this.numeroId=autoIncrement++;
    }
    
//
//    public void setImagen(File file, BufferedImage buffer) {
//        this.file = file;
//        this.buffer = buffer;
//    }

    public boolean esMismaImagen(Imagen im){
        return file.getAbsolutePath().equals(im.getFile().getAbsolutePath());
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Imagen && obj.hashCode()==hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.file);
        hash = 53 * hash + Objects.hashCode(this.buffer);
        return hash;
    }

}
