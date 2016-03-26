/*
 * @Ruben@
 */
package com.ruben.editordetiles.componentes;

import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Ventana que aparece la ir a crear una imagen de varias tiles. <br>
 * aqui se elige la cantidad de tiles de ancho y de alto
 *
 * @author Ruben
 */
public class DialogoVarios extends javax.swing.JDialog {

    private File[] files;
    private DialogoVarios.Listener listener = null;
    private int tilesEnAncho = 0;
    private int tilesEnAlto = 0;

    public void addListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * Creates new form VentanaCargarParaImagenDeVariasTIles
     *
     * @param parent
     * @param modal
     * @param files
     */
    public DialogoVarios(java.awt.Frame parent, boolean modal, File[] files) {
         this(parent, modal, files, files.length/2, files.length);
    }
    
    public DialogoVarios(java.awt.Frame parent, boolean modal, File[] files,int horizontalPorDefecto,int verticalPorDefecto) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        this.files = files;
        labelCantidadDeImagenes.setText(labelCantidadDeImagenes.getText() + String.valueOf(files.length));
        labelVertical.setText(String.valueOf(verticalPorDefecto));
        int cantidad = files.length;
       //if(cantidad>1)cantidad-=1;
        //System.out.println(horizontalPorDefecto+"   "+(cantidad-1));
        SpinnerNumberModel model1 = new SpinnerNumberModel(horizontalPorDefecto, 1, cantidad, 1);
        spinerHorizontal.setModel(model1);
        spinerHorizontal.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                actualizarLabel();

            }
        });
        actualizarLabel();
        
        //setVisible(true);

    }
    
    private void actualizarLabel(){
                SpinnerNumberModel mh = (SpinnerNumberModel) spinerHorizontal.getModel();
                int i = (int) mh.getNumber();
                i=files.length/i;
                //i = Math.abs(DialogoVarios.this.files.length - i);
                labelVertical.setText(String.valueOf(i));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        labelCantidadDeImagenes = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        labelVertical = new javax.swing.JLabel();
        spinerHorizontal = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        botonAceptar = new javax.swing.JButton();
        botonCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        labelCantidadDeImagenes.setText("Cantidad de imagenes:");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel2.setText("Filas:");

        labelVertical.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelVertical.setToolTipText("");

        jLabel1.setText("Columnas:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spinerHorizontal, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelVertical, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 3, Short.MAX_VALUE))
                    .addComponent(labelVertical, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(spinerHorizontal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        botonAceptar.setText("Aceptar");
        botonAceptar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonAceptarMouseClicked(evt);
            }
        });

        botonCancelar.setText("Cancelar");
        botonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelCantidadDeImagenes)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(botonAceptar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botonCancelar))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(labelCantidadDeImagenes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonAceptar)
                    .addComponent(botonCancelar))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonAceptarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonAceptarMouseClicked

        if (listener != null) {
            tilesEnAncho = (int) spinerHorizontal.getValue();
            tilesEnAlto = Integer.parseInt(labelVertical.getText());
            listener.DialogoVariosAceptar(tilesEnAncho, tilesEnAlto);
            setVisible(false);
            //  System.out.println(tilesEnAncho + " " + tilesEnAlto);
        }

      
    }//GEN-LAST:event_botonAceptarMouseClicked

    private void botonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarActionPerformed

        this.dispose();
     
    }//GEN-LAST:event_botonCancelarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                String ruta = "C:\\Users\\pedruben\\Documents\\NetBeansProjects\\EditorDeTiles\\public_html\\images";
                File[] files = new File[4];

                files[0] = new File(ruta + "\\1.jpg");
                files[1] = new File(ruta + "\\2.jpg");
                files[2] = new File(ruta + "\\3.jpg");
                files[3] = new File(ruta + "\\4.jpg");

                DialogoVarios dialog = new DialogoVarios(new javax.swing.JFrame(), true, files);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonAceptar;
    private javax.swing.JButton botonCancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel labelCantidadDeImagenes;
    private javax.swing.JLabel labelVertical;
    private javax.swing.JSpinner spinerHorizontal;
    // End of variables declaration//GEN-END:variables

    public static interface Listener {

        public void DialogoVariosAceptar(int tilesEnAncho, int tilesEnAlto);
    }

}