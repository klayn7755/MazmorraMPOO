/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: González Gaspar Sergio Alexander
 *              Macario Quintero Kevin Alexis 
 *              Trujillo Sedano Eduardo
 * Grupo: 5
 * Semestre: 2027-1
 */
package logica;

import excepciones.MovimientoInvalidoException;
import excepciones.RecursoInsuficienteException;
import java.util.ArrayList;
import modelo.Heroe;
import modelo.Objeto;
import modelo.Tablero;

//Gestiona el desplazamiento del heroe en el tablero, recolección de ítems y uso de inventario
public class GestorExploracion {

    private static final int[] DELTA_FILA = {-1, 1, 0, 0};
    private static final int[] DELTA_COLUMNA = {0, 0, -1, 1};

    private final LectorConsola lector;
    private final ConsolaUI ui;

    public GestorExploracion(LectorConsola lector, ConsolaUI ui) {
        this.lector = lector;
        this.ui = ui;
    }

    public boolean moverHeroe(Heroe heroe, Tablero tablero) {
        int direccion = lector.leerEntero("Direccion (1=Arriba 2=Abajo 3=Izquierda 4=Derecha, 0=Cancelar): ", 0, 4);
        if (direccion == 0) {
            return false;
        }
        try {
            tablero.mover(heroe, heroe.getFila() + DELTA_FILA[direccion - 1],
                    heroe.getColumna() + DELTA_COLUMNA[direccion - 1]);
            recogerDelSuelo(heroe, tablero);
            return true;
        } catch (MovimientoInvalidoException e) {
            System.out.println("[] " + e.getMessage());
            return false;
        }
    }

    private void recogerDelSuelo(Heroe heroe, Tablero tablero) {
        char item = tablero.tomarItem(heroe.getFila(), heroe.getColumna());
        if (item == '$') {
            heroe.sumarPuntos(20);
            System.out.println("Encuentras un tesoro! +20 puntos.");
        } else if (item == '+') {
            try {
                heroe.agregarObjeto(new Objeto("Pocion de vida", 15, 0));
                System.out.println("Recoges una Pocion de vida!");
            } catch (RecursoInsuficienteException e) {
                System.out.println("[] " + e.getMessage() + " La pocion se queda en el suelo.");
                tablero.ponerItem(heroe.getFila(), heroe.getColumna(), '+');
            }
        }
    }

    public boolean usarObjeto(Heroe heroe) {
        ArrayList<Objeto> inventario = heroe.getInventario();
        if (inventario.isEmpty()) {
            System.out.println("[!] Tu inventario esta vacio.");
            return false;
        }
        ui.mostrarInventario(heroe);
        int opcion = lector.leerEntero("Objeto a usar (0=cancelar): ", 0, inventario.size());
        if (opcion == 0) {
            return false;
        }
        try {
            System.out.println(heroe.usarObjeto(opcion - 1));
            return true;
        } catch (RecursoInsuficienteException e) {
            System.out.println("[advertencia] " + e.getMessage());
            return false;
        }
    }
}
