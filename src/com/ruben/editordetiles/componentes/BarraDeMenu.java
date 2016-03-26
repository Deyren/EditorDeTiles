/*
 * @Ruben@
 */
package com.ruben.editordetiles.componentes;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

/**
 *
 * @author Ruben
 */
public class BarraDeMenu extends JMenuBar {

    private final ArrayList<BarraDeMenuListener> listeners;

    public void addMenuListener(BarraDeMenuListener listener) {
        listeners.add(listener);
    }

    private static enum Nombres {

        Archivo, Editar,Herramientas, Ayuda
    };
    
    private static final int MENU_ARCHIVO=0;
    public static final int NUEVO =MENU_ARCHIVO;
    public static final int ABRIR = MENU_ARCHIVO+1;
    public static final int EXPORTAR = MENU_ARCHIVO+2;
     public static final int GUARDAR_IMAGEN = MENU_ARCHIVO+3;
     public static final int GUARDAR_IMAGEN_COMO = MENU_ARCHIVO+4;
    public static final int SALIR =MENU_ARCHIVO+ 5;

    
     private static final int MENU_HERRAMIENTAS=1000;
     public static final int RECORTAR_IMAGEN = MENU_HERRAMIENTAS;
     public static final int CREAR_TILE=MENU_HERRAMIENTAS+1;
     
    private static final String[] menuDeArchivos = {"Nuevo", "Abrir","Exportar...","Guardar imagen...","Guardar imagen como...", "Salir"};
    
    private static final String[] menuDeHerramientas = {"Recortar imagen...","Crear tile"};

    private final JMenu[] menus;

    public BarraDeMenu() {
        listeners = new ArrayList<>();
        menus = new JMenu[Nombres.values().length];
        for (int i = 0; i < menus.length; i++) {
            menus[i] = new JMenu(Nombres.values()[i].name());
            menus[i].setName(Nombres.values()[i].name());
            add(menus[i]);
        }

        setRequestFocusEnabled(true);
        
        
        for (JMenu menu : menus) {
          
            if (menu.getName().equals(Nombres.Archivo.name())) {
                for (int i = 0; i < menuDeArchivos.length; i++) {
                    if(i==SALIR){//Si es la opcion salir
                        menu.add(new JSeparator(JSeparator.HORIZONTAL));
                    }
                    menu.add(crearItemsDelMenu(menuDeArchivos[i], i+MENU_ARCHIVO));
                }
            }else if (menu.getName().equals(Nombres.Herramientas.name())) {
                for (int i = 0; i < menuDeHerramientas.length; i++) {
                    menu.add(crearItemsDelMenu(menuDeHerramientas[i], i+MENU_HERRAMIENTAS));
                }
            }
        }
    }

    private Item crearItemsDelMenu(String nombre, int id) {
        Item jItem = new Item(nombre, id);
        jItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Item j = (Item) e.getSource();
                listeners.stream().forEach((listener) -> {
                    listener.itemPulsado(j);
                });
            }

        });
        return jItem;
    }

 
    
    public class Item extends JMenuItem {

        private final int id;

        public int getId() {
            return id;
        }

        public Item(String text, int id) {
            super(text);
            this.id = id;
        }

    }

    //En cosntruccionn

    public static interface BarraDeMenuListener {

        public void itemPulsado(Item item);
    }

}
