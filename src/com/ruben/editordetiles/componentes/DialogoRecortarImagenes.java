/*
 * @Ruben@
 */
package com.ruben.editordetiles.componentes;

import com.ruben.editordetiles.canvas.utiles.Rectangulo;
import com.ruben.editordetiles.utils.Imagen;
import com.ruben.editordetiles.utils.Imagenes;
import com.ruben.editordetiles.utils.Utiles;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Ruben
 */
public class DialogoRecortarImagenes extends javax.swing.JDialog {

    PanelConImagen pan;

    int[] divisoresDeAncho;
    int[] divisoresDeAlto;

    int d = 0;

    /**
     * Creates new form DialogoRecortarImagenes
     *
     * @param parent
     * @param modal
     */
    public DialogoRecortarImagenes(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Recortar imagen");

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(radioArchivoLocal);
        grupo.add(radioURL);

        radioArchivoLocal.setSelected(true);
        textoRutaDeImagen.setEnabled(true);
        botonBuscarImagen.setEnabled(true);
        textoURL.setEnabled(false);
        botonIrUrl.setEnabled(false);

        radioArchivoLocal.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (radioArchivoLocal.isSelected()) {
                    textoRutaDeImagen.setEnabled(true);
                    botonBuscarImagen.setEnabled(true);
                    textoURL.setEnabled(false);
                    botonIrUrl.setEnabled(false);
                } else {
                    textoRutaDeImagen.setEnabled(false);
                    botonBuscarImagen.setEnabled(false);
                    textoURL.setEnabled(true);
                    botonIrUrl.setEnabled(true);
                }
            }
        });

        labelTamanioCadaTile.setText("Tamaño de cada tile: ");
        comboAltosPosibles.removeAllItems();
        comboAnchosPosibles.removeAllItems();
        textoRutaDeImagen.setEditable(false);
        textoCarpetaSalida.setEditable(false);
        pan = new PanelConImagen();
        //pan.setBounds(0, 0,  panelParaCanvas.getWidth(),  panelParaCanvas.getHeight());
        panelParaCanvas.setLayout(new BorderLayout());
        panelParaCanvas.add(pan, BorderLayout.CENTER);
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                DialogoRecortarImagenes.this.actualizaLaMatriz();
            }

        });

        enableElements(false);
        comboAnchosPosibles.addItemListener((ItemEvent e) -> {
            DialogoRecortarImagenes.this.actualizaLaMatriz();
        });
        comboAltosPosibles.addItemListener((ItemEvent e) -> {
            DialogoRecortarImagenes.this.actualizaLaMatriz();
        });

    }

    /**
     * Se le pasa el valor, lo busca en el array que se le pasa y devuelve el
     * valor de la posicion en la que lo encuentra mas el se le pasa a pos. Si
     * no encuentra el valor, o la posicion que se le pasa esta fuera de la
     * cantidad del array, devuelve -1
     *
     * @param divisores Array donde buscar 'valor'
     * @param valor Valor a buscar
     * @param pos valor para la posicion que devuelve el array
     * @return divisores[posicion del valor que coincide con valor + pos]
     */
    private int getSiguienteDivisor(int[] divisores, int valor, int pos) {
        int result = -1;
        for (int i = 0; i < divisores.length; i++) {
            if (divisores[i] == valor) {
                if (i + pos >= 0 && i + pos < divisores.length) {
                    result = divisores[i + pos];
                }
                break;
            }
        }
        return result;
    }

    private void enableElements(boolean yn) {
//         spinnerTilesX.setEnabled(yn);
//        spinnerTilesY.setEnabled(yn);
        labelColumnas.setEnabled(yn);
        labelFilas.setEnabled(yn);
        labelTamanio.setEnabled(yn);
        labelCantidadDeImagenes.setEnabled(yn);
        botonRecortar.setEnabled(yn);
        labelTamanioCadaTile.setEnabled(yn);
        comboAnchosPosibles.setEnabled(yn);
        comboAltosPosibles.setEnabled(yn);
        if (yn) {
            panelConOpciones.setBorder(new LineBorder(Color.black));
        } else {
            panelConOpciones.setBorder(new LineBorder(Color.GRAY));
        }

    }

    private void actualizaLaMatriz() {
        int valorX = 1;
        int valorY = 1;

        if (comboAnchosPosibles.isEnabled()
                && comboAltosPosibles.isEnabled()) {
            Object i1 = comboAnchosPosibles.getSelectedItem();
            Object i2 = comboAltosPosibles.getSelectedItem();
            if (i1 == null || i2 == null) {
                return;
            }
            valorX = (int) i1;
            valorY = (int) i2;

        }

        BufferedImage ima = pan.getSuImagen();
        if (ima != null) {
            int w = pan.getSuImagen().getWidth() / valorX;
            int h = pan.getSuImagen().getHeight() / valorY;

            labelTamanioCadaTile.setText("Tamaño de cada tile: " + String.valueOf(w) + "x" + String.valueOf(h));
            labelCantidadDeImagenes.setText("Cantidad de imagenes: " + ((pan.getSuImagen().getWidth() / w) * (pan.getSuImagen().getHeight() / h)));

        }

        pan.actualizarMatriz(valorY, valorX);
        pan.repaint();
    }

    private void recortar() {
        // 32
        // 4
        // 

        int columnas = (int) comboAnchosPosibles.getSelectedItem();
        int filas = (int) comboAltosPosibles.getSelectedItem();

        int w = pan.getSuImagen().getWidth() / columnas;
        int h = pan.getSuImagen().getHeight() / filas;

        labelTamanioCadaTile.setText("Tamaño de cada tile: " + String.valueOf(w) + "x" + String.valueOf(h));

        File fi = new File(textoCarpetaSalida.getText());
        if (fi.exists()) {
            Imagenes.recortarImagen(new File(textoCarpetaSalida.getText()), w, h);
            labelErrores.setText("");
        } else {
            labelErrores.setText("Selecciona una carpeta de salida.");
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        panelConOpciones = new javax.swing.JPanel();
        labelFilas = new javax.swing.JLabel();
        labelColumnas = new javax.swing.JLabel();
        botonRecortar = new javax.swing.JButton();
        labelTamanioCadaTile = new javax.swing.JLabel();
        comboAnchosPosibles = new javax.swing.JComboBox();
        comboAltosPosibles = new javax.swing.JComboBox();
        labelCantidadDeImagenes = new javax.swing.JLabel();
        labelErrores = new javax.swing.JLabel();
        btnCambiarTamanioImagen = new javax.swing.JButton();
        textoRutaDeImagen = new javax.swing.JTextField();
        botonBuscarImagen = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        panelParaCanvas = new javax.swing.JPanel();
        labelTamanio = new javax.swing.JLabel();
        radioArchivoLocal = new javax.swing.JRadioButton();
        radioURL = new javax.swing.JRadioButton();
        textoURL = new javax.swing.JTextField();
        botonIrUrl = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        textoCarpetaSalida = new javax.swing.JTextField();
        botonCarpetaSalida = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);

        panelConOpciones.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        labelFilas.setText("Filas:");

        labelColumnas.setText("Columnas:");

        botonRecortar.setText("Recortar");
        botonRecortar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRecortarActionPerformed(evt);
            }
        });

        labelTamanioCadaTile.setText("jLabel3");

        comboAnchosPosibles.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        comboAltosPosibles.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        labelCantidadDeImagenes.setText("Cantidad de imagenes:");

        labelErrores.setForeground(new java.awt.Color(255, 51, 0));

        btnCambiarTamanioImagen.setText("Cambiar tamaño...");
        btnCambiarTamanioImagen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCambiarTamanioImagenMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelConOpcionesLayout = new javax.swing.GroupLayout(panelConOpciones);
        panelConOpciones.setLayout(panelConOpcionesLayout);
        panelConOpcionesLayout.setHorizontalGroup(
            panelConOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelConOpcionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelConOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelColumnas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelFilas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelConOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(comboAltosPosibles, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboAnchosPosibles, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelConOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelConOpcionesLayout.createSequentialGroup()
                        .addComponent(labelCantidadDeImagenes)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(labelTamanioCadaTile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelConOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelConOpcionesLayout.createSequentialGroup()
                        .addComponent(labelErrores, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCambiarTamanioImagen))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelConOpcionesLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(botonRecortar)))
                .addContainerGap())
        );
        panelConOpcionesLayout.setVerticalGroup(
            panelConOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConOpcionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelConOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelConOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(labelFilas, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboAltosPosibles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelTamanioCadaTile))
                    .addComponent(botonRecortar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelConOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelErrores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelConOpcionesLayout.createSequentialGroup()
                        .addGroup(panelConOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelConOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(comboAnchosPosibles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelColumnas)
                                .addComponent(labelCantidadDeImagenes))
                            .addComponent(btnCambiarTamanioImagen))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        textoRutaDeImagen.setText(" ");

        botonBuscarImagen.setText("...");
        botonBuscarImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBuscarImagenActionPerformed(evt);
            }
        });

        panelParaCanvas.setBackground(new java.awt.Color(153, 153, 153));

        javax.swing.GroupLayout panelParaCanvasLayout = new javax.swing.GroupLayout(panelParaCanvas);
        panelParaCanvas.setLayout(panelParaCanvasLayout);
        panelParaCanvasLayout.setHorizontalGroup(
            panelParaCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelParaCanvasLayout.setVerticalGroup(
            panelParaCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 342, Short.MAX_VALUE)
        );

        labelTamanio.setText("Tamaño de imagen:");

        radioArchivoLocal.setText("Archivo local");

        radioURL.setText("URL");

        textoURL.setText("https://manga7ine.files.wordpress.com/2013/10/bola-1.jpg");

        botonIrUrl.setText("Ir");
        botonIrUrl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonIrUrlMouseClicked(evt);
            }
        });

        jLabel1.setText("Carpeta de salida:");

        botonCarpetaSalida.setText("...");
        botonCarpetaSalida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCarpetaSalidaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelParaCanvas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(panelConOpciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelTamanio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(radioArchivoLocal)
                            .addComponent(radioURL, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textoRutaDeImagen)
                            .addComponent(textoURL)
                            .addComponent(textoCarpetaSalida))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(botonBuscarImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(botonIrUrl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(botonCarpetaSalida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textoRutaDeImagen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonBuscarImagen)
                    .addComponent(radioArchivoLocal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioURL)
                    .addComponent(textoURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonIrUrl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(textoCarpetaSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonCarpetaSalida))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelConOpciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelTamanio, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelParaCanvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonBuscarImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBuscarImagenActionPerformed

        leerArchivo();

    }//GEN-LAST:event_botonBuscarImagenActionPerformed

    private void botonRecortarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRecortarActionPerformed
        // TODO add your handling code here:
        recortar();

    }//GEN-LAST:event_botonRecortarActionPerformed

    private void botonIrUrlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonIrUrlMouseClicked
        obtenerDeURL();
    }//GEN-LAST:event_botonIrUrlMouseClicked

    private void botonCarpetaSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCarpetaSalidaActionPerformed
        // TODO add your handling code here:
        File f = Utiles.seleccionarCarpeta(this);
        if (f != null) {
            textoCarpetaSalida.setText(f.getAbsolutePath());
        }

    }//GEN-LAST:event_botonCarpetaSalidaActionPerformed

    private void btnCambiarTamanioImagenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCambiarTamanioImagenMouseClicked
        // TODO add your handling code her
        DialogoCmbiarTamanio dd = new DialogoCmbiarTamanio(null, true, pan,this);
        dd.setVisible(true);
        actualizarImagen();

    }//GEN-LAST:event_btnCambiarTamanioImagenMouseClicked

    private void obtenerDeURL() {
        Imagen ima = Utiles.seleccionarArchivoDesdeURL(textoURL.getText());

        BufferedImage buff = ima.getBuffer();
        pan.setSuImagen(buff);
        pan.setRutaImagen(ima.getFile());
        pan.updateUI();

        int ancho = buff.getWidth();
        int alto = buff.getHeight();

        divisoresDeAncho = divisoresDeUnNUmero(ancho);
        divisoresDeAlto = divisoresDeUnNUmero(alto);

        comboAnchosPosibles.removeAllItems();
        comboAltosPosibles.removeAllItems();
        for (int i = 0; i < divisoresDeAncho.length; i++) {
            comboAnchosPosibles.addItem(divisoresDeAncho[i]);
        }
        for (int i = 0; i < divisoresDeAlto.length; i++) {
            comboAltosPosibles.addItem(divisoresDeAlto[i]);
        }

        labelTamanio.setText("Tamaño: " + ancho + "x" + alto);

//                    spinnerTilesX.setModel(new SpinnerNumberModel(1, 1, ancho, 1));
//                    spinnerTilesY.setModel(new SpinnerNumberModel(1, 1, ancho, 1));
        enableElements(true);

        textoRutaDeImagen.setText(ima.getFile().getAbsolutePath());

    }

    private void actualizarImagen() {
        int ancho = pan.suImagen.getWidth();
        int alto = pan.suImagen.getHeight();

        divisoresDeAncho = divisoresDeUnNUmero(ancho);
        divisoresDeAlto = divisoresDeUnNUmero(alto);

        comboAnchosPosibles.removeAllItems();
        comboAltosPosibles.removeAllItems();
        for (int i = 0; i < divisoresDeAncho.length; i++) {
            comboAnchosPosibles.addItem(divisoresDeAncho[i]);
        }
        for (int i = 0; i < divisoresDeAlto.length; i++) {
            comboAltosPosibles.addItem(divisoresDeAlto[i]);
        }

        labelTamanio.setText("Tamaño: " + ancho + "x" + alto);

    }

    /**
     * Abre el cuadro de elegir imagen.
     */
    private void leerArchivo() {
        File[] files = Utiles.seleccionarArchivos(this);
        if (files != null) {
            if (files[0] != null) {
                try {
                    BufferedImage buff = ImageIO.read(files[0]);
                    pan.setSuImagen(buff);
                    pan.setRutaImagen(files[0]);
                    pan.updateUI();

                    int ancho = buff.getWidth();
                    int alto = buff.getHeight();

                    divisoresDeAncho = divisoresDeUnNUmero(ancho);
                    divisoresDeAlto = divisoresDeUnNUmero(alto);

                    comboAnchosPosibles.removeAllItems();
                    comboAltosPosibles.removeAllItems();
                    for (int i = 0; i < divisoresDeAncho.length; i++) {
                        comboAnchosPosibles.addItem(divisoresDeAncho[i]);
                    }
                    for (int i = 0; i < divisoresDeAlto.length; i++) {
                        comboAltosPosibles.addItem(divisoresDeAlto[i]);
                    }

                    labelTamanio.setText("Tamaño: " + ancho + "x" + alto);

                    enableElements(true);

                    textoRutaDeImagen.setText(files[0].getAbsolutePath());
                    textoCarpetaSalida.setText(files[0].getAbsolutePath());
                } catch (IOException ex) {
                    Logger.getLogger(DialogoRecortarImagenes.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private int[] divisoresDeUnNUmero(int numero) {
        ArrayList<Integer> numeros = new ArrayList<>();

        for (int i = 1; i <= numero; i++) {
            if (numero % i == 0) {
                numeros.add(i);
            }
        }
        int[] salida = new int[numeros.size()];
        for (int i = 0; i < numeros.size(); i++) {
            salida[i] = numeros.get(i);
        }
        return salida;
    }

    public static class PanelConImagen extends JPanel {

        BufferedImage suImagen;
        File rutaImagen;

        public File getRutaImagen() {
            return rutaImagen;
        }

        public void setRutaImagen(File rutaImagen) {
            this.rutaImagen = rutaImagen;
        }

        private Rectangulo[][] rectangulos = null;
        private int anchoDeTile = 1, altoDeTile = 1;

        void setSuImagen(BufferedImage suImagen) {
            this.suImagen = suImagen;
        }

        public BufferedImage getSuImagen() {
            return suImagen;
        }

        public void actualizarMatriz(int filas, int columnas) {
            rectangulos = null;
            rectangulos = new Rectangulo[filas][columnas];
            int x = 0, y = 0;
            anchoDeTile = getWidth() / columnas;
            altoDeTile = getHeight() / filas;

            for (Rectangulo[] rectangulo : rectangulos) {
                for (int j = 0; j < rectangulo.length; j++) {
                    rectangulo[j] = new Rectangulo(x, y, anchoDeTile, altoDeTile);
                    x += anchoDeTile;
                }
                y += altoDeTile;
                x = 0;
            }
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            if (suImagen != null) {
                g.drawImage(suImagen, 0, 0, getWidth(), getHeight(), null);
            }

            if (rectangulos != null) {
                for (Rectangulo[] rectangulo : rectangulos) {
                    for (Rectangulo rect : rectangulo) {
                        if (rect != null) {
                            rect.dibujarRectangulo((Graphics2D) g, Color.GREEN);
                        }
                    }
                }
            }

        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogoRecortarImagenes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            DialogoRecortarImagenes dialog = new DialogoRecortarImagenes(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonBuscarImagen;
    private javax.swing.JButton botonCarpetaSalida;
    private javax.swing.JButton botonIrUrl;
    private javax.swing.JButton botonRecortar;
    private javax.swing.JButton btnCambiarTamanioImagen;
    private javax.swing.JComboBox comboAltosPosibles;
    private javax.swing.JComboBox comboAnchosPosibles;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labelCantidadDeImagenes;
    private javax.swing.JLabel labelColumnas;
    private javax.swing.JLabel labelErrores;
    private javax.swing.JLabel labelFilas;
    private javax.swing.JLabel labelTamanio;
    private javax.swing.JLabel labelTamanioCadaTile;
    private javax.swing.JPanel panelConOpciones;
    private javax.swing.JPanel panelParaCanvas;
    private javax.swing.JRadioButton radioArchivoLocal;
    private javax.swing.JRadioButton radioURL;
    private javax.swing.JTextField textoCarpetaSalida;
    private javax.swing.JTextField textoRutaDeImagen;
    private javax.swing.JTextField textoURL;
    // End of variables declaration//GEN-END:variables

    public JTextField getTextoCarpetaSalida() {
        return textoCarpetaSalida;
    }



}
