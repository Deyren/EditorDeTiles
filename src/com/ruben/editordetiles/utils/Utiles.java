/*
 * @Ruben@
 */
package com.ruben.editordetiles.utils;

import com.ruben.editordetiles.canvas.utiles.SpriteDeVariasTiles;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import static javax.imageio.ImageIO.read;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Ruben
 */
public class Utiles {

    /**
     * Extension de archivo que crea al recortar imagenes que contiene los datos
     * de esas imagenes
     */
    public static final String EXT_IMGS = "cut";
    public static final String NOMBRE_ARCHIVO_TXT_DE_RECORTES = File.separator + "_." + EXT_IMGS;

    //El signo de dos puntos debe estar, se basa en ese signo para leer el archivo.
    public static String TEXTO_TILES_HOTIZONTAL_EN_TXT = "Columnas: ";
    public static String TEXTO_TILES_VERTICAL_EN_TXT = "Filas: ";
    public static String TEXTO_RUTAS_DE_LOS_ARCHIVOS_EN_TXT = "Rutas de loas archivos: ";
    private static final String[] extensiones = {"jpg", "png", "bmp"};
    public static int TAMANIO_IMAGEN = 32;

    /**
     * Debe contener en todo momento las imagenes que se han cargado en la barra
     * de imagenes
     */
    private static final ArrayList<Imagen> ImagenesQueHay = new ArrayList();
    private static final ArrayList<SpriteDeVariasTiles> ImagenesDeVariosTiles = new ArrayList<>();
    // private static String rutaActual = "C:\\Users\\pedruben\\Desktop\\VARIOS (No mover ni cambiar el nombre)\\imagenes\\imagenes para 3dmax";
    private static String rutaActual = "C:/Users/pedruben/Documents/My Web Sites/Sesiones/imagenes/imagenesDe_r";

    /**
     * Se le pasa un archivo, comprueba si coincide con el primer sprite de
     * algun SpriteDeVariasTiles del array si coincide alguno, devuelve el
     * SpriteDeVariasTiles sino devuelve null
     *
     * @param nombre
     * @return
     */
    public static SpriteDeVariasTiles getSpriteDeVariasTiles(String nombre) {

        for (int i = 0; i < ImagenesDeVariosTiles.size(); i++) {
            SpriteDeVariasTiles sv = ImagenesDeVariosTiles.get(i);
            if (sv.getSprites().get(0).getImagen().getRutaAbsoluta().equals(nombre)) {
                return sv;
            }
        }
        return null;
    }

    /**
     * Busca un archivo llamado 'NOMBRE_ARCHIVO_TXT_DE_RECORTES' en la ruta del
     * file que se le pase, file debe ser un directorio. <br>
     * , si los encuentra lo lee y devuelde el numero de tiles horizontal y
     * vertical que contiene el archivo, si no existe o no lo puede leer
     * devuelve null
     *
     * @param file
     * @return
     */
    public static int[] buscarArchivoTxt(File file) {
        // System.out.println(file.getAbsolutePath());
        if (file.isDirectory()) {
            int[] result = new int[2];
            result[0] = -1;
            result[1] = -1;
            File[] files = file.listFiles();
            for (File file1 : files) {

                if (file1.getName().equals(NOMBRE_ARCHIVO_TXT_DE_RECORTES.substring(
                        NOMBRE_ARCHIVO_TXT_DE_RECORTES.lastIndexOf(File.separator) + 1))) {
                    //System.out.println(file1.getName());
                    try {
                        try (BufferedReader br = new BufferedReader(new FileReader(file1))) {
                            String line = br.readLine();
                            while (line != null) {
                                if (line.contains(TEXTO_TILES_HOTIZONTAL_EN_TXT)) {
                                    int hori = Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
                                    result[0] = hori;
                                } else if (line.contains(TEXTO_TILES_VERTICAL_EN_TXT)) {
                                    int hori = Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
                                    result[1] = hori;
                                }
                                line = br.readLine();
                            }
                            //  System.out.println(result[0]+" "+result[1]);
                            if (result[0] != -1 && result[1] != -1) {
                                return result;
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Utiles.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }
        return null;
    }

    public static void borrarImagen(Imagen imagen) {
        int i = 0;
        int s = -1;
        for (Imagen im : ImagenesQueHay) {
            if (imagen.getFile().getAbsolutePath().equals(im.getFile().getAbsolutePath())) {
                s = 0;
                break;
            }
            i++;
        }
        if (s != -1) {
            ImagenesQueHay.remove(i);
        }
    }

    /**
     * Crea un objeto Imagen, lo guarda en el array y lo devuelve. <br>
     *
     *
     * @param file
     * @return
     */
    public static final Imagen addImagen(File file) {
        BufferedImage bim = GetImagenConTamanioDado(file, TAMANIO_IMAGEN, TAMANIO_IMAGEN);
        Imagen im = null;
        if (bim != null) {
            im = Imagen.getInstance(file, bim);
            if (!imagenExiste(im)) {
                ImagenesQueHay.add(im);
            } else {
                im = null;
            }
        }
        // System.out.println("Imagenes en el array: "+ImagenesQueHay.size());
        return im;
    }

    /**
     * Agrega varios objetos Imagen al array y los devuelve Si algun archivo ya
     * lo contieneno lo agrega pero si devuelve el que tiene que coincide agrega
     * los sprites de varias tiles
     *
     * @param files
     * @param cantidadTilesEnAncho
     * @param cantidadTilesEnAlto
     * @return
     */
    public static final ArrayList<Imagen> addImagenes(File[] files, int cantidadTilesEnAncho, int cantidadTilesEnAlto) {
        ArrayList<Imagen> imagenes = new ArrayList<>();
        for (File file : files) {
            BufferedImage bim = GetImagenConTamanioDado(file, TAMANIO_IMAGEN, TAMANIO_IMAGEN);
            Imagen im;
            if (bim != null) {
                im = Imagen.getInstance(file, bim);
                //  imagenes= new ArrayList<>();
                if (!imagenExiste(im)) {
                    ImagenesQueHay.add(im);
                    imagenes.add(im);
                } else {
                    imagenes.add(buscarArchivoEnElArray(file));
                    //System.out.println("Imagen que ya existe");
                }
            }

        }
        if (!imagenes.isEmpty()) {
            SpriteDeVariasTiles svt = new SpriteDeVariasTiles(imagenes, cantidadTilesEnAncho, cantidadTilesEnAlto);
            ImagenesDeVariosTiles.add(svt);
            //System.out.println("caNTIDAD en array de imagen-de-varios-tiles: " +ImagenesDeVariosTiles.size());
        }
        return imagenes;
    }

    public static final ArrayList<Imagen> addImagenesDesdeArchivoDeDatos(File archivoDeDatos, int cantidadTilesEnAncho, int cantidadTilesEnAlto) {
        ArrayList<File> filesEnElArchivo = new ArrayList<>();
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(archivoDeDatos))) {
                boolean lineaEncontrada = false;
                String line = br.readLine();
                while (line != null) {
                    if (lineaEncontrada) {
                        if (!line.isEmpty()) {
                            filesEnElArchivo.add(new File(line));
                        }
                    }
                    if (line.startsWith(TEXTO_RUTAS_DE_LOS_ARCHIVOS_EN_TXT)) {
                        lineaEncontrada = true;
                    }
                    line = br.readLine();
                }
                //  System.out.println(result[0]+" "+result[1]);

            }
        } catch (IOException ex) {
            Logger.getLogger(Utiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        // File[] files=(File[])filesEnElArchivo.toArray();
        File[] files = new File[filesEnElArchivo.size()];
        int i = 0;
        for (File filesEnElArchivo1 : filesEnElArchivo) {
            //System.out.println(filesEnElArchivo1.getAbsolutePath());
            files[i++] = filesEnElArchivo1;
        }

        ArrayList<Imagen> imagenes = new ArrayList<>();
        for (File file : files) {
            BufferedImage bim = GetImagenConTamanioDado(file, TAMANIO_IMAGEN, TAMANIO_IMAGEN);
            Imagen im;
            if (bim != null) {
                im = Imagen.getInstance(file, bim);
                //  imagenes= new ArrayList<>();
                if (!imagenExiste(im)) {
                    ImagenesQueHay.add(im);
                    imagenes.add(im);
                } else {
                    imagenes.add(buscarArchivoEnElArray(file));
                    //System.out.println("Imagen que ya existe");
                }
            }

        }
        if (!imagenes.isEmpty()) {
            SpriteDeVariasTiles svt = new SpriteDeVariasTiles(imagenes, cantidadTilesEnAncho, cantidadTilesEnAlto);
            ImagenesDeVariosTiles.add(svt);
            //System.out.println("caNTIDAD en array de imagen-de-varios-tiles: " +ImagenesDeVariosTiles.size());
        }
        return imagenes;
    }

    public static File[] obtenerFilesDeUnArchivoDeDatos(File archivoDeDatos) {
        ArrayList<File> filesEnElArchivo = new ArrayList<>();
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(archivoDeDatos))) {
                boolean lineaEncontrada = false;
                String line = br.readLine();
                while (line != null) {
                    if (lineaEncontrada) {
                        if (!line.isEmpty()) {
                            filesEnElArchivo.add(new File(line));
                        }
                    }
                    if (line.startsWith(TEXTO_RUTAS_DE_LOS_ARCHIVOS_EN_TXT)) {
                        lineaEncontrada = true;
                    }
                    line = br.readLine();
                }
                //  System.out.println(result[0]+" "+result[1]);

            }
        } catch (IOException ex) {
            Logger.getLogger(Utiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("FF " + filesEnElArchivo.size());
        File[] files = new File[filesEnElArchivo.size()];
        int i = 0;
        for (File filesEnElArchivo1 : filesEnElArchivo) {
            //System.out.println(filesEnElArchivo1.getAbsolutePath());
            files[i++] = filesEnElArchivo1;
        }
        //File[] files=(File[])filesEnElArchivo.toArray();
        return files;
    }

    /**
     * Comprueba si existe cierto file en el array, usando la ruta absoluta para
     * la comparacion. <br>
     *
     * @param file
     * @return
     */
    private static boolean imagenExiste(Imagen ima) {
        for (Imagen par : ImagenesQueHay) {
            if (ima.esMismaImagen(par)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Busca en el array un archivo que coindica con el que se le pasa y
     * devuelve el bufferedImage del Sprite de ese archivo o null si ningun
     * Sprite del array contiene la misma ruta del archivo que se le pasa.
     *
     * @param file
     * @return
     */
    public static Imagen getImagenFromFile(File file) {
        for (Imagen sp : ImagenesQueHay) {
            if (sp.getFile().equals(file)) {
                return sp;
            }
        }
        return null;
    }

    /**
     * Se le pasa un archivo y devuelve el Sprite asociado al archivo que sea
     * igual al que se le pasa. <br>
     * El BufferedImage lo busca en el ArrayList (Pair(File), (BufferedImage))
     * que hay en esta clase y que contiene todas las imagenes que se han ido
     * agregando al panel. Si ningun archivo coincide devuelde null.
     *
     * @param file
     * @return
     */
    public static Imagen buscarArchivoEnElArray(File file) {
        if (file != null) {
            Imagen result;
            for (Imagen par : ImagenesQueHay) {
                if (file.equals(par.getFile())) {
                    result = par;
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * Abre un dialogo de seleccionar archivo de buff y de vuelve un array con
     * los archivos elegidos
     *
     * @param c
     * @return
     */
    public static File[] seleccionarArchivos(Component c) {
        File[] Fsalida = null;
        JFileChooser jfc = new JFileChooser();
        File rutaActualFile = new File(rutaActual);
        if (rutaActualFile.exists()) {
            jfc.setCurrentDirectory(rutaActualFile);
        }

        jfc.setMultiSelectionEnabled(true);

        jfc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                boolean salida = false;
                for (String extension : extensiones) {
                    String ext = f.getName();
                    ext = ext.substring(ext.lastIndexOf(".") + 1);
                    if (extension.compareToIgnoreCase(ext) == 0
                            || f.isDirectory()) {
                        salida = true;
                        break;
                    }
                }
                return salida;
            }

            @Override
            public String getDescription() {
                StringBuilder sb = new StringBuilder();
                sb.append("Imagenes: ");
                for (String extensione : extensiones) {
                    sb.append(".").append(extensione).append(" ");
                }
                return sb.toString();
            }
        });

        int r = jfc.showOpenDialog(c);
        if (r == JFileChooser.APPROVE_OPTION) {
            rutaActual = jfc.getCurrentDirectory().getAbsolutePath();
            Fsalida = jfc.getSelectedFiles();
            // Este bucle ordena los archivos por nombre. Burbuja
            for (int i = 0; i < Fsalida.length; i++) {
                for (int j = 0; j < Fsalida.length - 1; j++) {
                    File este = Fsalida[j];
                    File siguiente = Fsalida[j + 1];
                    //  int s=este.getName().compareTo(siguiente.getName());
                    //  if(s==1){
                    if (este.getName().length() > siguiente.getName().length()) {
                        File aux = este;
                        este = siguiente;
                        siguiente = aux;
                        Fsalida[j] = este;
                        Fsalida[j + 1] = siguiente;
                    }

                    //   }
                }

            }
//            for (File Fsalida1 : Fsalida) {
//                System.out.println(Fsalida1.getName());
//            }
        }
        return Fsalida;
    }

    public static File seleccionarCarpeta(Component c) {
        File Fsalida = null;
        JFileChooser jfc = new JFileChooser();
        File rutaActualFile = new File(rutaActual);
        if (rutaActualFile.exists()) {
            jfc.setCurrentDirectory(rutaActualFile);
        }

        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int r = jfc.showSaveDialog(c);
        if (r == JFileChooser.APPROVE_OPTION) {
            Fsalida = jfc.getSelectedFile();
        }

        return Fsalida;
    }

    /**
     * Obtiene lo que devuelve la url. <br>
     * lo guarda en archivos temporales. <br>
     * crea un objeto Imagen del archivo guardado <br>
     * borra el archivo guardado. <br>
     * No guarda el objeto imagen en el array, solo lo crea y lo devuelve.
     * devuelve null si algo falla
     *
     * @param surl
     * @return
     */
    public static Imagen seleccionarArchivoDesdeURL(String surl) {
        Imagen salida = null;

        try {
            URL url = new URL(surl);
            URLConnection comm = url.openConnection();
            BufferedImage buff = ImageIO.read(comm.getInputStream());

            String rutaArchivosTemporales = System.getenv("tmp");

            File fS = new File(rutaArchivosTemporales + "\\edit_tiles" + surl.substring(surl.lastIndexOf("/") + 1));

            if (ImageIO.write(buff, surl.substring(surl.lastIndexOf(".") + 1), fS)) {

                salida = Imagen.getInstance(fS, buff);
            }

            fS.delete();

        } catch (MalformedURLException ex) {
            Logger.getLogger(Utiles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Utiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return salida;
    }

    /**
     * Cambia el tamaño de la imagen y la cambia al ancho y alto que se le pase.
     * <br>
     * Devuelve un BufferedImage con la imagen escalada. o null si no es una
     * imagen o no existe el archivo
     *
     * @param file Archivo de imagen
     * @param ancho
     * @param alto
     * @return BufferedImage
     */
    public static BufferedImage GetImagenConTamanioDado(File file, int ancho, int alto) {
        BufferedImage bi = null;
        if (file.exists()) {
            String nombreFile = file.getName();
            String extension = nombreFile.substring(nombreFile.indexOf("."));

            if ((extension.endsWith("jpg") || extension.endsWith("JPG"))
                    || (extension.endsWith("png") || extension.endsWith("PNG"))
                    || (extension.endsWith("bmp") || extension.endsWith("BMP"))) {
                Image img = new ImageIcon(file.getAbsolutePath()).getImage();
                int anchoOriginal = img.getWidth(null);
                int altoOriginal = img.getHeight(null);

                double anchoResultado = 1f * ancho / anchoOriginal;
                double altoResultado = 1f * alto / altoOriginal;

                int tipo = extension.compareToIgnoreCase(".jpg") == 0
                        || extension.compareToIgnoreCase(".bmp") == 0
                                ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;

                bi = new BufferedImage((int) (anchoOriginal * anchoResultado), (int) (altoOriginal * altoResultado), tipo);

                Graphics2D grph = (Graphics2D) bi.getGraphics();
                grph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                grph.scale(anchoResultado, altoResultado);
                grph.drawImage(img, 0, 0, null);
                grph.dispose();
            }
        }

        return bi;

    }

    /**
     *
     * Abre un dialogo para seleccionar archivos y devuelve un array de File con
     * los archivos seleccionados. Este metodo se puede usar para cualquier
     * cosa, no depende de la clase.
     *
     *
     * @param c
     * @return
     */
    public static File[] seleccionDeArchivos(Component c) {
        File[] Fsalida = null;
        JFileChooser jfc = new JFileChooser();
        File ruta = new File(rutaActual);
        if (ruta.exists()) {
            jfc.setCurrentDirectory(ruta);
        }

        jfc.setMultiSelectionEnabled(true);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                boolean salida = false;
                for (String extension : extensiones) {
                    String ext = f.getName();
                    ext = ext.substring(ext.lastIndexOf(".") + 1);
                    if (extension.compareToIgnoreCase(ext) == 0
                            || f.isDirectory()) {
                        salida = true;
                        break;
                    }
                }
                return salida;
            }

            @Override
            public String getDescription() {
                StringBuilder sb = new StringBuilder();
                sb.append("Imagenes: ");
                for (String extensione : extensiones) {
                    sb.append(".").append(extensione).append(" ");
                }
                return sb.toString();
            }
        });

        int r = jfc.showOpenDialog(c);
        if (r == JFileChooser.APPROVE_OPTION) {
            rutaActual = jfc.getCurrentDirectory().getAbsolutePath();
            Fsalida = jfc.getSelectedFiles();
            // Este bucle ordena los archivos por nombre. Burbuja
            for (int i = 0; i < Fsalida.length; i++) {
                for (int j = 0; j < Fsalida.length - 1; j++) {
                    File este = Fsalida[j];
                    File siguiente = Fsalida[j + 1];
                    //  int s=este.getName().compareTo(siguiente.getName());
                    //  if(s==1){
                    if (este.getName().length() > siguiente.getName().length()) {
                        File aux = este;
                        este = siguiente;
                        siguiente = aux;
                        Fsalida[j] = este;
                        Fsalida[j + 1] = siguiente;
                    }

                    //   }
                }

            }
//            for (File Fsalida1 : Fsalida) {
//                System.out.println(Fsalida1.getName());
//            }
        }
        return Fsalida;
    }

    /**
     * Abre un dialogo para guardar archivo, y devuelve el File elegido. <br>
     * Este metodo se puede usar para cualquier cosa, no depende de la clase.
     *
     * @param c
     * @return
     */
    public static File guardarArchivo(Component c) {
        File Fsalida = null;
        JFileChooser jfc = new JFileChooser();
        File ruta = new File(rutaActual);
        if (ruta.exists()) {
            jfc.setCurrentDirectory(ruta);
        }
        jfc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                boolean salida = false;
                for (String extension : extensiones) {
                    String ext = f.getName();
                    ext = ext.substring(ext.lastIndexOf(".") + 1);
                    if (extension.compareToIgnoreCase(ext) == 0
                            || f.isDirectory()) {
                        salida = true;
                        break;
                    }
                }
                return salida;
            }

            @Override
            public String getDescription() {
                StringBuilder sb = new StringBuilder();
                sb.append("Imagenes: ");
                for (String extensione : extensiones) {
                    sb.append(".").append(extensione).append(" ");
                }
                return sb.toString();
            }
        });

        int r = jfc.showSaveDialog(c);
        if (r == JFileChooser.APPROVE_OPTION) {
            Fsalida = jfc.getSelectedFile();
            System.out.println(Fsalida.getAbsoluteFile());
        }

        return Fsalida;
    }

}
