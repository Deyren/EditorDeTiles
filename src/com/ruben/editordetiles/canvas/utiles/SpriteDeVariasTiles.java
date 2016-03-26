/*
 * @Ruben@
 */
package com.ruben.editordetiles.canvas.utiles;

import com.ruben.editordetiles.utils.Imagen;
import java.util.ArrayList;

/**
 *
 * @author Ruben
 */
public class SpriteDeVariasTiles extends Sprite {

    public static final String SPRITE_ARRAY = "Imagen de varios tiles";
    private final ArrayList<Sprite> sprites;

    public ArrayList<Sprite> getSprites() {
        return sprites;
    }
    
    private final int tilesEnAncho;
    private final int tilesEnAlto;

    public int getTilesEnAncho() {
        return tilesEnAncho;
    }

    public int getTilesEnAlto() {
        return tilesEnAlto;
    }
    

    
    public SpriteDeVariasTiles(ArrayList<Imagen> imagenes, int cantidadDeTilesX, int cantidadDeTilesY) {
        super(imagenes.get(0), 0, 0); 
        this.sprites=new ArrayList<>();
        imagenes.stream().forEach((imagene) -> {
            sprites.add(new Sprite(imagene, 0, 0));
            // sprites[i]=new Sprite(imagenes.get(i), 0, 0);
        });
        super.imagen.setRutaAbsoluta(SPRITE_ARRAY+" "+imagenes.get(0).getFile().getName());
        this.tilesEnAncho=cantidadDeTilesX;
        this.tilesEnAlto=cantidadDeTilesY;
                
    }
    public Sprite getSprite(int index){
        if(index>=0 && index<sprites.size()){
            return sprites.get(index);
        }
        return null;
    }

}
