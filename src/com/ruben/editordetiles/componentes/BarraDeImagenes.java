/*
 * @Ruben@
 */
package com.ruben.editordetiles.componentes;

import com.ruben.editordetiles.utils.Imagen;
import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author Ruben
 */
public class BarraDeImagenes extends JToolBar {

    private JFrame padre;
    private ButtonGroup grupo;
    private JPanel panelParaImagenes;

    public JPanel getPanelParaImagenes() {
        return panelParaImagenes;
    }

    private BotonDeImagen.ImagenSeleccionadaListener imagenSeleccionadaListener;

    public void setOnImagenSeleccionadaListener(BotonDeImagen.ImagenSeleccionadaListener listener) {
        this.imagenSeleccionadaListener = listener;
    }

    public BarraDeImagenes(JFrame padre) {
        initComponentes(padre);
        setFloatable(false);

    }

    private void initComponentes(JFrame padre) {
        this.padre = padre;
        grupo = new ButtonGroup();
        panelParaImagenes = new JPanel();
        //BoxLayout bl = new BoxLayout(panelParaImagenes, BoxLayout.X_AXIS);
        panelParaImagenes.setLayout(new FlowLayout());
        panelParaImagenes.add(Box.createVerticalStrut(90));
        JScrollPane jspanel = new JScrollPane(panelParaImagenes);
        jspanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jspanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(jspanel);

    }
/**
 * 
 * Se le pasa el file y el bufferedImage
 Añade un nuevo ToggleButon a la barra con la boton
 que referencia el file, y devuelve un BufferImage del archivo
 Devuelve null si hay algun error.
     * @param imagen
 */
    public void crearBotonTile(Imagen imagen) {
        BotonDeImagen boton = BotonDeImagen.getInstance(imagen);
        //BufferedImage salida=null;//Se crea solo para que solo haya un return en el metodo
        if (boton != null) {
            String ni=imagen.getRutaAbsoluta().substring(imagen.getRutaAbsoluta().lastIndexOf("\\")+1);
            boton.setToolTipText(ni);
            boton.setOnImagenSeleccionadaListener(imagenSeleccionadaListener);
            grupo.add(boton);
            panelParaImagenes.add(boton);
           //salida=imagen.getBuffer();
        }
    }
}
