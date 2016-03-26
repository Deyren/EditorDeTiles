/*
 * @Ruben@
 */
package com.ruben.editordetiles.canvas.utiles;

import com.ruben.editordetiles.canvas.Canvas;
import com.ruben.editordetiles.utils.Imagen;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import javafx.util.Pair;

/**
 *
 * @author Ruben
 */
public class Matriz implements Dibujable {

    private final int cantidadHorizontal, cantidadVertical;
    private final Sprite[][] sprites;
    private final Canvas canvas;
    private boolean dibujarMatriz = false;
    private int posicionX, posicionY;
    private final Color colorMatriz = Color.PINK;
    private static Integer anchoDeCadaRectangulo, altoDeCadaRectangulo;
    public static Integer ZOOM = 0;

    public boolean isDibujarMatriz() {
        return dibujarMatriz;
    }

    public void setDibujarMatriz(boolean dibujarMatriz) {
        this.dibujarMatriz = dibujarMatriz;
        canvas.dibujar();
    }

    public Matriz(Canvas canvas, Imagen imagenInicial, int cantidadVertical, int cantidadHorizontal) {
        this.canvas = canvas;
        sprites = new Sprite[cantidadHorizontal][cantidadVertical];
        this.cantidadHorizontal = cantidadHorizontal;
        this.cantidadVertical = cantidadVertical;
        anchoDeCadaRectangulo = (canvas.getSize().width) / cantidadHorizontal;
        altoDeCadaRectangulo = (canvas.getSize().height) / cantidadVertical;

        this.posicionX = 0;
        this.posicionY = 0;
        // imagenInicial = new File("C:\\Users\\pedruben\\Desktop\\VARIOS (No mover ni cambiar el nombre)\\imagenes\\negro.jpg");
        iniciarMatriz(imagenInicial);
    }

    public Sprite[][] getSprites(){
        return sprites;
    }
    
    /**
     * Se le pasa una posicion y devuelve el objeto Sprite de la matriz que esta
     * en esa posicion. <br>
     * Devuelve null si no encuentra nada
     *
     * @param punto
     * @return
     */
    public Sprite choca(Point punto) {
        for (Sprite[] sprite : sprites) {
            for (Sprite sp : sprite) {
                if (sp.contiene(punto)) {
                    return sp;
                }
            }
        }
        return null;
    }

    /**
     * pone en dibujarRectangulo el sprite que este en el punto pasado es usado
     * por objeto Escena, pero esta comentado porque ralentiza
     *
     * @param punto
     */
    public void dr(Point punto) {
        if (!dibujarMatriz) {
            for (Sprite[] sprite : sprites) {
                for (Sprite sp : sprite) {
                    if (sp.contiene(punto)) {
                        sp.setDibujaElRectangulo(true);
                    } else {
                        sp.setDibujaElRectangulo(false);
                    }
                }
            }
        }
    }

    public Pair<Integer, Integer> getPosicionEnElArray(Sprite sp) {
        Pair<Integer, Integer> salida = null;
        for (int i = 0; i < sprites.length; i++) {
            for (int j = 0; j < sprites[i].length; j++) {
                if (sprites[i][j].equals(sp)) {
                    salida = new Pair(i, j);
                    return salida;
                }
            }
        }
        return salida;
    }

    public Sprite getSiguienteHorizontal(Sprite sp) {
        Pair<Integer, Integer> p = getPosicionEnElArray(sp);
        if (p != null) {
            if (p.getValue() + 1 >= sprites[0].length) {
                return null;
            }
            return sprites[p.getKey()][p.getValue() + 1];
        }
        return null;
    }

    public Sprite getSiguienteVertical(Sprite sp) {
        Pair<Integer, Integer> p = getPosicionEnElArray(sp);
        if (p != null) {
            if (p.getKey() + 1 >= sprites.length) {
                return null;
            }
            return sprites[p.getKey() + 1][p.getValue()];
        }
        return null;
    }

    public Sprite getSiguienteHorizontal(Sprite sp, int cantidad) {
        Pair<Integer, Integer> p = getPosicionEnElArray(sp);
        if (p != null) {
            if (p.getValue() + cantidad >= sprites[0].length) {
                return null;
            }
            return sprites[p.getKey()][p.getValue() + cantidad];
        }
        return null;
    }

    public Sprite getSiguienteVertical(Sprite sp, int cantidad) {
        Pair<Integer, Integer> p = getPosicionEnElArray(sp);
        if (p != null) {
            if (p.getKey() + cantidad >= sprites.length) {
                return null;
            }
            return sprites[p.getKey() + cantidad][p.getValue()];
        }
        return null;
    }

    public Sprite getSprite(int fila, int columna) {
        return sprites[fila][columna];
    }

    public void reemplazarImagenDeTile(Imagen nuevo, Imagen actual) {
        for (Sprite[] sprite1 : sprites) {
            for (Sprite sprite : sprite1) {
                if(sprite.getImagen().getFile().getAbsolutePath().equals(actual.getFile().getAbsolutePath())){
                    sprite.setImagen(nuevo);
                } 
            }
        }
        canvas.dibujar();
    }

    public void rellenarMatriz(Imagen imagen) {
        new Thread(() -> {
            for (Sprite[] sprite : sprites) {
                for (Sprite sprite1 : sprite) {
                    sprite1.setImagen(imagen);
                }
            }
            canvas.dibujar();
        }).start();

    }

    public void moverEnHorizontal(int cantidad) {
        this.posicionX += cantidad;
    }

    public void moverEnVertical(int cantidad) {
        this.posicionY += cantidad;
    }

    public void actualizarTamanio() {
        if (ZOOM < -350) {
            ZOOM = -350;
        }
        new Thread(() -> {
            anchoDeCadaRectangulo = (canvas.getSize().width + ZOOM) / cantidadHorizontal;
            altoDeCadaRectangulo = (canvas.getSize().height + ZOOM) / cantidadVertical;
            Integer posX = this.posicionX - (ZOOM / 2);
            Integer posY = this.posicionY - (ZOOM / 2);
            for (Sprite[] sprite : sprites) {
                for (Sprite sp : sprite) {
                    sp.x = posX;
                    sp.y = posY;
                    sp.ancho = anchoDeCadaRectangulo;
                    sp.alto = altoDeCadaRectangulo;
                    posX += anchoDeCadaRectangulo;
                }
                posY += altoDeCadaRectangulo;
                posX = this.posicionX - (ZOOM / 2);
            }
            canvas.dibujar();
        }).start();

    }

    private void iniciarMatriz(Imagen imagenInicial) {
        for (Sprite[] sprite : sprites) {
            for (int j = 0; j < sprite.length; j++) {
                sprite[j] = new Sprite(imagenInicial, 0, 0);
            }
        }
        actualizarTamanio();
    }

    @Override
    public void dibujar(Graphics2D g) {

//System.err.println(sprites[0][0].getFileName());
        for (Sprite[] sprite1 : sprites) {
            for (Sprite sprite : sprite1) {
                sprite.dibujar(g);
                g.setColor(Color.WHITE);
               // g.drawString(String.valueOf( sprite.getImagen().getNumeroId()),sprite.x+ sprite.ancho/3,sprite.y+ sprite.alto/3); 
                if (dibujarMatriz) {
                    // sprite.setDibujaElRectangulo(true);
                    // sprite.dibujar(g);
                    sprite.dibujarRectangulo(g, colorMatriz);
                } else {
                    if (sprite.isDibujaElRectangulo()) {
                        sprite.dibujarRectangulo(g, Color.YELLOW.brighter());
                    }
                }

                //System.out.println("Dibujando matriz");
            }
        }

    }

    @Override
    public void setTamanio() {
        actualizarTamanio();
    }

}
