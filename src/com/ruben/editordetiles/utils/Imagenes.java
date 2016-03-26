/*
 * @Ruben@
 */
package com.ruben.editordetiles.utils;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Ruben
 */
public class Imagenes {

    private static int ANCHO_DE_RECORTE, ALTO_DE_RECORTE;

    /**
     * Se le pasa un File con la imagen y la cantidad de tiles en que se
     * recortará. El tamaño de la imagen debe ser multiplo del valor del ancho y
     * el alto., si no devuelve un error. Crea una carpeta en la carpeta donde
     * se encuentre la imagen que se le pase con las imagenes resultantes.
     *
     * @param archivoDeEntrada
     * @param ancho
     * @param alto
     */
    public static void recortarImagen(File archivoDeEntrada, int ancho, int alto) {
        if (!archivoDeEntrada.exists()) {
            System.err.println("El archivo no existe: " + archivoDeEntrada.getAbsolutePath());
            return;
        }
        ANCHO_DE_RECORTE = ancho;
        ALTO_DE_RECORTE = alto;
        String extension = archivoDeEntrada.getName();
        extension = extension.substring(extension.lastIndexOf(".") + 1);
        BufferedImage bim = leerArchivo(archivoDeEntrada);

        int anchoImagenEntrada = bim.getWidth();
        int altoImagenEntrada = bim.getHeight();
        // System.out.println("Ancho: " + anchoImagenEntrada);

        if (!tamañoCorrecto(anchoImagenEntrada, altoImagenEntrada)) {
            return;
        }

        File carpetaSalida = crearDirectorio(archivoDeEntrada);
        
        
        
        int sub = 1;
        ArrayList<String> nombres=new ArrayList<>();
        for (int i = 0; i < altoImagenEntrada; i += ALTO_DE_RECORTE) {
            for (int j = 0; j < anchoImagenEntrada; j += ANCHO_DE_RECORTE) {
                String nombreDelArchivo = archivoDeEntrada.getName();
                nombreDelArchivo = nombreDelArchivo.substring(0, nombreDelArchivo.indexOf("."));
                String archivoSalida = carpetaSalida.getAbsolutePath() + "/" + (sub++) + nombreDelArchivo + "." + extension;
                File fs=new File(archivoSalida);
              
                guardarImagen(bim, fs, new Rectangle(j, i, ANCHO_DE_RECORTE, ALTO_DE_RECORTE), extension);
                nombres.add(fs.getAbsolutePath());

            }
        }
        int imagenesEnAncho = anchoImagenEntrada / ANCHO_DE_RECORTE;
        int imagenesEnAlto = altoImagenEntrada / ALTO_DE_RECORTE;

        try {
            try (BufferedWriter bw=new BufferedWriter(new FileWriter(new File(carpetaSalida.getAbsolutePath() +File.separatorChar+Utiles.NOMBRE_ARCHIVO_TXT_DE_RECORTES)))) {
                bw.write("Cantidad de imagenes:" + imagenesEnAncho * imagenesEnAlto);bw.newLine();;
                bw.write(Utiles.TEXTO_TILES_HOTIZONTAL_EN_TXT + imagenesEnAncho);bw.newLine();
                bw.write(Utiles.TEXTO_TILES_VERTICAL_EN_TXT+ imagenesEnAlto);bw.newLine();
                bw.write(Utiles.TEXTO_RUTAS_DE_LOS_ARCHIVOS_EN_TXT);bw.newLine();
                for (String nombre : nombres) {
                     bw.write(nombre);bw.newLine();
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Imagenes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static BufferedImage cortarImagen(BufferedImage src, Rectangle rect) {
        BufferedImage dest = src.getSubimage(rect.x, rect.y, rect.width, rect.height);
        return dest;
    }

    
    private static File crearDirectorio(File f) {
        String name = f.getName();
        name = name.substring(0, name.indexOf("."));
        File dir = new File(f.getParent() + "/" + name + "_recortado");
        if (!dir.exists()) {
            dir.mkdir();
        } 
        
        File[] files=dir.listFiles();
        for (File file : files) {
            if(!file.isDirectory()){
                String[] exts={"jpg","png","bmp"};
                for (String ext : exts) {
                    if (file.getName().endsWith(ext)) {
                        file.delete();
                        break;
                    }
                }
            }
        }
        
        
        return dir;
    }

    private static BufferedImage leerArchivo(File archivo) {
        BufferedImage bim = null;
        try {
            bim = ImageIO.read(new FileInputStream(archivo));

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace(System.err);
        }
        return bim;
    }

    private static void guardarImagen(BufferedImage bim, File archivoSalida,
            Rectangle rect, String extension) {
        if (bim != null) {
            bim = cortarImagen(bim, rect);
            File salida = archivoSalida;
            try {
                ImageIO.write(bim, extension, salida);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace(System.err);
            }
        }
    }

    /**
     * Comprueba si el ancho y el alto es correcto
     *
     * @param ancho
     * @param alto
     */
    private static boolean tamañoCorrecto(int ancho, int alto) {
        if (ancho % ANCHO_DE_RECORTE != 0) {
            System.err.println("El ancho de la imagen debe ser multiplo de " + ANCHO_DE_RECORTE);
            return false;
        }
        if (alto % ALTO_DE_RECORTE != 0) {
            System.err.println("El alto de la imagen debe ser multiplo de " + ALTO_DE_RECORTE);
            return false;
        }
        return true;
    }

}
