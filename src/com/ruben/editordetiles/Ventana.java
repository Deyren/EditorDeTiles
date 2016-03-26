/*
 * @Ruben@
 */
package com.ruben.editordetiles;

import com.ruben.editordetiles.archivos.Archivos;
import com.ruben.editordetiles.componentes.BarraDeHerramientas;
import com.ruben.editordetiles.componentes.BarraDeImagenes;
import com.ruben.editordetiles.canvas.Canvas;
import com.ruben.editordetiles.canvas.Escena;
import com.ruben.editordetiles.componentes.BarraDeMenu;
import com.ruben.editordetiles.componentes.BotonDeImagen;
import com.ruben.editordetiles.componentes.BotonDeImagen.ImagenSeleccionadaListener;
import com.ruben.editordetiles.componentes.DialogoCrearTiles;
import com.ruben.editordetiles.componentes.DialogoGuardarImagen;
import com.ruben.editordetiles.componentes.DialogoRecortarImagenes;
import com.ruben.editordetiles.componentes.DialogoVarios;
import com.ruben.editordetiles.utils.Imagen;
import com.ruben.editordetiles.utils.Utiles;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author Ruben
 */
public class Ventana extends JFrame implements BarraDeHerramientas.BarraDeHerramientasListener, BarraDeMenu.BarraDeMenuListener, ImagenSeleccionadaListener {

    private Canvas canvas;
    private Escena escena;
    private BarraDeImagenes barraImagenes;
    private JPanel panelDeFondo;
    BarraDeHerramientas barraDeHerramientas;
    private Imagen imagenSeleccionada = null;
    private BarraDeMenu barraDeMenu;
    private Imagen imagenDeTilesPorDefecto;

    public Imagen getImagenSeleccionada() {
        return imagenSeleccionada;
    }

    public Ventana() {
        initComponents();
        setTitle("Editor de tiles");
    }

    private void initComponents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setResizable(true);
       
        setMinimumSize(new Dimension(200, 300));
        barraDeMenu = new BarraDeMenu();
        barraDeMenu.addMenuListener(this);
        setJMenuBar(barraDeMenu);

        barraImagenes = new BarraDeImagenes(this);
        barraImagenes.setOnImagenSeleccionadaListener(this);

        barraDeHerramientas = new BarraDeHerramientas();
        add(barraDeHerramientas, BorderLayout.NORTH);
        panelDeFondo = new JPanel(new BorderLayout(2, 2));

        try {
            canvas = Canvas.getInstance(this);
        } catch (IOException ex) {
            Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "No se ha podido cargar la vista.", "Error al cargar la vista", JOptionPane.ERROR_MESSAGE);
        }
        
        panelDeFondo.add(canvas, BorderLayout.CENTER);
        panelDeFondo.add(barraImagenes, BorderLayout.SOUTH);

        ////////////CREA LA IMAGEN DE RELLENO INICIAL Y LA AGREGA A LA ESCENA///////////////////
        File f = new File("C:\\Users\\pedruben\\Desktop\\VARIOS (No mover ni cambiar el nombre)\\imagenes\\negro.jpg");
        imagenDeTilesPorDefecto = Utiles.addImagen(f);
        if (imagenDeTilesPorDefecto != null) {
            barraImagenes.crearBotonTile(imagenDeTilesPorDefecto);
        }
        escena = new Escena(canvas, imagenDeTilesPorDefecto, this);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        getContentPane().add(panelDeFondo);

        barraDeHerramientas.setOnBarraDeHerramientasListener(this);

        addComponentListener(new ComponentAdapter() {
            //cuando cambia el tamaño de la ventana, actualiza la matriz
            @Override
            public void componentResized(ComponentEvent e) {
                escena.actualizarTamanioDeLaMatriz();
            }
        });

        setVisible(true);
    }

    /**
     * Lo usa botonBuscarImagenes por si algun archivo no coincide, no es una
     * imagen.
     *
     * @param ar
     */
    private void mostrarArchivosNoCargados(ArrayList<String> ar) {
        StringBuilder sb = new StringBuilder();
        ar.stream().forEach((a) -> {
            sb.append(a).append("\n");
        });
        JOptionPane.showMessageDialog(this, sb.toString(), "Archivos no cargados", JOptionPane.INFORMATION_MESSAGE);

    }

    private boolean agregarNuevaImagen(File file) {
        Imagen bi = Utiles.addImagen(file);
        if (bi != null) {
            barraImagenes.crearBotonTile(bi);
            return true;
        }
        return false;
    }

    /*             EVENTOS DE  LA BARRA DE HERRAMIENTAS          */
    @Override
    public void botonBuscarImagenes() {
        File[] files = Utiles.seleccionarArchivos(Ventana.this);
        if (files != null) {
            ArrayList<String> filesNoValidos = new ArrayList<>();
            for (File file : files) {
                if (!agregarNuevaImagen(file)) {
                    filesNoValidos.add(file.getAbsolutePath());
                }
            }
            if (!filesNoValidos.isEmpty()) {
                mostrarArchivosNoCargados(filesNoValidos);
            }
            barraImagenes.updateUI();
        }
    }

    @Override
    public void botonBuscarArchivosParaTile() {
        File[] files = Utiles.seleccionarArchivos(Ventana.this);
        if (files != null) {
            if (files.length >= 2) {

                int[] horVer = Utiles.buscarArchivoTxt(new File(files[0].getParent()));
                DialogoVarios vacvt;

                if (horVer == null || horVer[0] == -1 || horVer[1] == -1) {
                    vacvt = new DialogoVarios(this, true, files);

                } else {
                    vacvt = new DialogoVarios(this, true, files, horVer[0], horVer[1]);
                }

                vacvt.addListener((int tilesEnAncho, int tilesEnAlto) -> {
                    ArrayList<Imagen> imas = Utiles.addImagenes(files, tilesEnAncho, tilesEnAlto);
                   // System.out.println(imas.get(0).getFile());

                    barraImagenes.crearBotonTile(imas.get(0));
                    barraImagenes.updateUI();
                } //Cuando se da a aceptar despues de elegir la cantidad de tiles
                );
                vacvt.setVisible(true);

            } else if (files.length == 1) {//Si solo hay uno, sebe ser el archivo de los detaos de recortes de una imagen
                File f = files[0];
                
                String ext = f.getName();
                ext = ext.substring(ext.lastIndexOf(".") + 1);
                //System.out.println(ext);
              //  System.out.println(Utiles.EXT_IMGS);
                if (ext.equals(Utiles.EXT_IMGS)) {
                    
                    int[] horVer = Utiles.buscarArchivoTxt(new File(files[0].getParent()));
                    DialogoVarios vacvt;
                    // System.out.println("horizontal: "+horVer[0]);
                    //  System.out.println("vertical: "+horVer[1]);
                    if (horVer == null || horVer[0] == -1 || horVer[1] == -1) {
                        vacvt = new DialogoVarios(this, true, files);

                    } else {
                        vacvt = new DialogoVarios(this, true, Utiles.obtenerFilesDeUnArchivoDeDatos(f), horVer[0], horVer[1]);
                    }

                    vacvt.addListener((int tilesEnAncho, int tilesEnAlto) -> {
                     ArrayList<Imagen> imas = Utiles.addImagenesDesdeArchivoDeDatos(f, tilesEnAncho, tilesEnAlto);
                    
                   // System.out.println(imas.get(0).getFile());

                        barraImagenes.crearBotonTile(imas.get(0));
                        barraImagenes.updateUI();
                    } //Cuando se da a aceptar despues de elegir la cantidad de tiles
                    );
                    vacvt.setVisible(true);

                   
                }
            }

        }
    }

    //Rellena la matriz con la imagen seleccionada
    @Override
    public void botonPintarTodo() {
        if (imagenSeleccionada != null) {
            Imagen buff = Utiles.getImagenFromFile(imagenSeleccionada.getFile());
            if (buff != null) {
                escena.rellenarMatriz(imagenSeleccionada);
            }
        }

    }

    @Override
    public void botonMostrarMatriz(JToggleButton boton) {
        escena.mostrarMatriz(boton.isSelected());

    }

    @Override
    public void modoDeDibujoSeleccionado(BarraDeHerramientas.ModoDeDibujo modo) {
        escena.setModo(modo);
    }

    @Override
    public void botonGuardarImagen() {
        DialogoGuardarImagen diag = new DialogoGuardarImagen(this, true);
        diag.setVisible(true);
    }

    @Override
    public void botonGuardarImagenComo() {
        File f = Utiles.guardarArchivo(this);
        if (f != null) {
            String ext = f.getName();
            ext = ext.substring(ext.lastIndexOf(".") + 1);
            canvas.guardarComoImagen(f, ext);
        }
    }

    /*                  EVENTO DE LA BARRA DE IMAGENES        */
    @Override
    public void seleccionada(BotonDeImagen itile) {
        imagenSeleccionada = itile.getImagen();
        barraDeHerramientas.activarModo(BarraDeHerramientas.ModoDeDibujo.PintaTexturas);
        escena.setModo(BarraDeHerramientas.ModoDeDibujo.PintaTexturas);
    }

    //Cuando el raton sale o entra en un boton de la barra de imagenes
    @Override
    public void ratonEntraOSale() {
        canvas.dibujar();
    }

    @Override
    public void botonEliminarPulsado(BotonDeImagen itile) {
        Imagen imagen = itile.getImagen();
        escena.reemplazarImagenDeTile(imagenDeTilesPorDefecto, imagen);
        barraImagenes.getPanelParaImagenes().remove(itile);
        barraImagenes.updateUI();
        Utiles.borrarImagen(itile.getImagen());
    }

    /*                   EVENTO DE LA BARRA DE MENU               */
    @Override
    public void itemPulsado(BarraDeMenu.Item item) {
        switch (item.getId()) {
            case BarraDeMenu.EXPORTAR:
                File salida = Utiles.seleccionarCarpeta(this);
                if (salida != null) {
                    Archivos.guardarTodo(escena.getMatriz(), salida);
                }
                break;
            case BarraDeMenu.GUARDAR_IMAGEN:
                DialogoGuardarImagen diag = new DialogoGuardarImagen(this, true);
                diag.setVisible(true);
                break;
            case BarraDeMenu.GUARDAR_IMAGEN_COMO:
                File f = Utiles.guardarArchivo(this);
                if (f != null) {
                    String ext = f.getName();
                    ext = ext.substring(ext.lastIndexOf(".") + 1);
                    canvas.guardarComoImagen(f, ext);
                }
                break;
            case BarraDeMenu.SALIR:
                this.setVisible(false);
                System.exit(0);
                break;
            case BarraDeMenu.RECORTAR_IMAGEN:
                DialogoRecortarImagenes dri = new DialogoRecortarImagenes(this, true);
                dri.setVisible(true);
                break;
            case BarraDeMenu.CREAR_TILE:
                DialogoCrearTiles digti = new DialogoCrearTiles(this, true);
                digti.setVisible(true);
                break;
        }

    }

}
