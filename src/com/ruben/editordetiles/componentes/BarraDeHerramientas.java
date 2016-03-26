/*
 * @Ruben@
 */
package com.ruben.editordetiles.componentes;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;

/**
 *
 * @author Ruben
 */
public class BarraDeHerramientas extends JToolBar implements ActionListener {

    public static enum ModoDeDibujo {

        PintaTexturas, PintaLineas
    }
    private final JButton botonVerArchivos;
    private final JButton botonSeleccionarArchivosParaTile;
    private final JButton botonPintarTodo;
    
    private final JToggleButton botonMostrarMatriz;

    private final JToggleButton botonPintarTexturas;
    private final JToggleButton botonPintarLineas;
    private final ButtonGroup gupoDeModoDePintar;
    
    private final JButton botonGuardarImagen;
    private final JButton botonGuardarImagenComo;
    
 

    public void activarModo(ModoDeDibujo modo) {
        switch (modo) {
            case PintaTexturas:
                botonPintarTexturas.setSelected(true);
                break;
            case PintaLineas:
                botonPintarLineas.setSelected(true);
                break;
        }
    }

    private BarraDeHerramientasListener listener;

    //private BarraConLabelYTexto anchoDeMatriz,altoDeMatriz;
    public void setOnBarraDeHerramientasListener(BarraDeHerramientasListener listener) {
        this.listener = listener;
    }

    public BarraDeHerramientas() {

        botonVerArchivos = crearBoton("Cargar");
        botonSeleccionarArchivosParaTile = crearBoton("CargarB");
        botonPintarTodo = crearBoton("PTodo");
        botonGuardarImagen=crearBoton("Guargar imagen...");
        botonGuardarImagenComo=crearBoton("Guargar imagen como...");
        botonMostrarMatriz = new JToggleButton("Matriz");
        botonMostrarMatriz.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                listener.botonMostrarMatriz(botonMostrarMatriz);
            }

        });

        this.botonPintarTexturas = new JToggleButton("Texturas");
        this.botonPintarTexturas.setSelected(true);
        this.botonPintarTexturas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BarraDeHerramientas.this.listener.modoDeDibujoSeleccionado(ModoDeDibujo.PintaTexturas);
            }
        });

        this.botonPintarLineas = new JToggleButton(" Lineas");
        this.botonPintarLineas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BarraDeHerramientas.this.listener.modoDeDibujoSeleccionado(ModoDeDibujo.PintaLineas);
            }
        });
        this.botonPintarLineas.setEnabled(false);
        this.botonPintarLineas.setToolTipText("De momento solo pinta cada tile, no sobre todo el canvas");
        this.gupoDeModoDePintar = new ButtonGroup();
        gupoDeModoDePintar.add(botonPintarTexturas);
        gupoDeModoDePintar.add(botonPintarLineas);

        
       
        
        
        
        setLayout(new FlowLayout(FlowLayout.LEFT));

        // setBorder(new LineBorder(Color.GRAY)); 
        add(botonVerArchivos);
        add(botonSeleccionarArchivosParaTile);
        add(new Separator());
        add(this.botonPintarTexturas);
        add(this.botonPintarLineas);
        add(new Separator());
        add(botonPintarTodo);
        add(botonMostrarMatriz);
        add(new Separator());
        add(botonGuardarImagen);
        add(botonGuardarImagenComo);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        //  setLayout(new FlowLayout(FlowLayout.LEFT));

    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.addActionListener(this);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (listener != null) {
            if (e.getSource().equals(botonVerArchivos)) {
                listener.botonBuscarImagenes();
            } else if (e.getSource().equals(botonPintarTodo)) {
                listener.botonPintarTodo();
            } else if (e.getSource().equals(botonSeleccionarArchivosParaTile)) {
                listener.botonBuscarArchivosParaTile();
            }else if (e.getSource().equals(botonGuardarImagen)) {
                listener.botonGuardarImagen();
            }else if (e.getSource().equals(botonGuardarImagenComo)) {
                listener.botonGuardarImagenComo();
            }
        }

    }

    public static interface BarraDeHerramientasListener {

        public void botonBuscarImagenes();

        public void botonPintarTodo();

        public void botonMostrarMatriz(JToggleButton boton);

        public void botonBuscarArchivosParaTile();

        public void modoDeDibujoSeleccionado(ModoDeDibujo modo);
        
        public void botonGuardarImagen();
        
        public void botonGuardarImagenComo();
    }
}
