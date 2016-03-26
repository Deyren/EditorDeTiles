/*
 * @Ruben@
 */
package com.ruben;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ruben
 */
public class PruebaSaltoJuego {

    public boolean saltando;
    public boolean cayendo;
    private float cantidadDeSalto = 20;
    float posicionY = 10;

    private void saltoEnY() {

        float maximo = posicionY + cantidadDeSalto;
        float tipoImpositivo = 1;
        boolean subiendo = true;
        while (true) {

            if (subiendo) {
                if (posicionY < maximo) {
                    posicionY += 2 * tipoImpositivo;
                    tipoImpositivo -= 0.05;
                    if (tipoImpositivo < 0.1) {
                        tipoImpositivo = 0.1f;
                    }
                } else {
                    subiendo = false;
                    cayendo = true;
                }
            } else {

                if (!cayendo) {
                    break;
                }
                posicionY -= 2 * tipoImpositivo;
                tipoImpositivo += 0.05;

                if (tipoImpositivo > 1) {
                    tipoImpositivo = 1f;
                }
            }
            System.out.println(posicionY);
            System.out.println("\t" + subiendo + ":" + cayendo);
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {

            }

        }

    }

    private void cambioSalto() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {

                }
                System.err.println("Cayendo puesto a false");
                cayendo = false;
            }
        }).start();
    }

    public static void main(String[] args) {
        PruebaSaltoJuego ps = new PruebaSaltoJuego();
       
        ps.cambioSalto(); 
        ps.saltoEnY();
    }
}
