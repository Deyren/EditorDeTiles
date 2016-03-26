/*
 * @Ruben@
 */
package com.ruben.editordetiles.componentes;

import com.ruben.editordetiles.utils.Imagen;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.Raster;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

/**
 * Un JToggleButton con una imagen Se usa getInstance para crear un objeto
 *
 * @author Ruben
 */
public class BotonDeImagen extends JToggleButton {

    private static final int ANCHO_DE_BORDE = 8;

    private static enum TonoDeColor {

        Normal, Oscuro
    };
    private Raster rst;
    //private BufferedImage buffer;
    private ImagenSeleccionadaListener seleccionListener;

    private JPopupMenu jpm;
    private final Imagen imagen;

    public Imagen getImagen() {
        return imagen;
    }

    public void setOnImagenSeleccionadaListener(ImagenSeleccionadaListener ima) {
        this.seleccionListener = ima;
    }

    /**
     * Obtiene un objeto BotonDeImagen o JToggleButton con la imagen Si no puede
     * cargar la imagen devuelve null.
     *
     * @param imagen
     * @param esMultiple
     * @return
     */
    public static BotonDeImagen getInstance(Imagen imagen, boolean esMultiple) {
        //BufferedImage bim = Imagenes.cambiarTamanio(imagen, TAMANIO_IMAGEN, TAMANIO_IMAGEN);
        if (imagen == null) {
            return null;
        }
        BotonDeImagen ima = new BotonDeImagen(imagen);

      //  ima.buffer = buff;
        // ima.rst = ima.buffer.getData();
        ima.rst = imagen.getBuffer().getData();
        ima.setIcon(new ImageIcon(ima.imagen.getBuffer()));
        return ima;
    }

    public static BotonDeImagen getInstance(Imagen imagen) {
        return BotonDeImagen.getInstance(imagen, false);
    }

    private BotonDeImagen(Imagen imagen) {
        this.imagen = imagen;
        init();
        jpm=new JPopupMenu();
    }

    private void init() {
        setBorder(new EmptyBorder(ANCHO_DE_BORDE, ANCHO_DE_BORDE, ANCHO_DE_BORDE, ANCHO_DE_BORDE));
        addItemListener((ItemEvent e) -> { // ItemListener
            if (e.getStateChange() == ItemEvent.SELECTED) {
                tipoDeColor(TonoDeColor.Normal);
                setBorder(BorderFactory.createLineBorder(Color.GREEN, ANCHO_DE_BORDE));

                //Llama al metodo de la interface ImagenSeleccionadaListener
                seleccionListener.seleccionada(BotonDeImagen.this);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                tipoDeColor(TonoDeColor.Normal);
                setBorder(new EmptyBorder(ANCHO_DE_BORDE, ANCHO_DE_BORDE, ANCHO_DE_BORDE, ANCHO_DE_BORDE));

            }
        });
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {//Si es el boton derecho...
                   jpm=new JPopupMenu();
                    jpm.setLocation(e.getLocationOnScreen());

                    JMenuItem jedit = new JMenuItem("Editar imagen");
                    
                    
                    
                     JMenuItem jeliminar = new JMenuItem("Eliminar");
                    jeliminar.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            BotonDeImagen.this.seleccionListener.botonEliminarPulsado(BotonDeImagen.this);
                            jpm.setVisible(false);   
                            jpm=null;  
                        }
                    });

                
                 
                    jpm.add(jedit);
                    jpm.add(jeliminar);
                    jpm.setVisible(true);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
              //  LineBorder lb=(LineBorder)BorderFactory.createLineBorder(Color.GREEN);

                setBorder(BorderFactory.createLineBorder(Color.YELLOW, ANCHO_DE_BORDE));
                if (!isSelected()) {
                    tipoDeColor(TonoDeColor.Oscuro);
                }
                seleccionListener.ratonEntraOSale();
            }

            @Override
            public void mouseExited(MouseEvent e) {

                if (!isSelected()) {
                    tipoDeColor(TonoDeColor.Normal);
                    setBorder(new EmptyBorder(ANCHO_DE_BORDE, ANCHO_DE_BORDE, ANCHO_DE_BORDE, ANCHO_DE_BORDE));

                } else {
                    setBorder(BorderFactory.createLineBorder(Color.GREEN, ANCHO_DE_BORDE));

                }
                seleccionListener.ratonEntraOSale();
            }
        });

    }

//    private BotonDeImagen(File imagen) {
//        this.archivo = imagen;
//        init();
//    }
    /**
     * Establece la luminosidad de la imagen del boton
     *
     * @param luminosidad
     */
    private void tipoDeColor(TonoDeColor luminosidad) {
        switch (luminosidad) {
            case Normal:
                imagen.getBuffer().setData(rst);
                break;
            case Oscuro:
                for (int i = 0; i < getWidth(); i++) {
                    for (int j = 0; j < getHeight(); j++) {
                        try {
                            Color c = new Color(imagen.getBuffer().getRGB(j, i));
                            c = c.darker().darker().darker().darker();
                            imagen.getBuffer().setRGB(j, i, c.getRGB());
                        } catch (Exception e) {
                        }

                    }
                }
        }
    }

    public static interface ImagenSeleccionadaListener {

        public void seleccionada(BotonDeImagen itile);
        public void botonEliminarPulsado(BotonDeImagen itile);
        public void ratonEntraOSale();
    }
}
