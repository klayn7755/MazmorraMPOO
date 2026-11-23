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

import java.util.ArrayList;
import modelo.Entidad;
import modelo.Heroe;
import modelo.Objeto;

//Se encarga de mostrar menús, estados, barras y pantallas en consola
public class ConsolaUI {

    public void mostrarBanner() {
        System.out.println();
        System.out.println("  ==============================================");
        System.out.println("     M A Z M O R R A S   D E   E L D O R I A");
        System.out.println("        ~ Una aventura de dados y dragones ~");
        System.out.println("  ==============================================");
        System.out.println("        /| ________________");
        System.out.println("  O|===|* >________________>");
        System.out.println("        \\|");
        System.out.println();
    }

    public void mostrarInstrucciones(int limiteTurnos) {
        System.out.println("\n---------------- INSTRUCCIONES ----------------");
        System.out.println("Bajas a una mazmorra llena de monstruos. Tu mision: derrotar");
        System.out.println("al Dragon Ancestral (D) antes de que pasen " + limiteTurnos + " turnos.");
        System.out.println("Pierdes si tu vida llega a 0, abandonas o se acaban los turnos.");
        System.out.println();
        System.out.println("En cada turno haces UNA accion"
                + "n y despues se mueven los monstruos.");
        System.out.println(" - Los ataques usan un dado de 20 caras (d20): un 1 siempre falla");
        System.out.println("   y un 20 es golpe critico (dano doble).");
        System.out.println(" - La habilidad especial gasta energia. Descansar la recupera.");
        System.out.println(" - Pisa '+' para recoger pociones y '$' para ganar puntos.");
        System.out.println(" - El alcance se mide en casillas rectas (sin diagonales).");
        mostrarLeyenda();
    }

    public void mostrarLeyenda() {
        System.out.println("Leyenda: @ tu | g Goblin | T Troll | D Dragon | + pocion | $ tesoro | # muro");
    }

    public void mostrarEstadoHeroe(Heroe heroe, int cantidadEnemigos) {
        System.out.println("\n" + heroe.getNombre() + " el " + heroe.getClase());
        System.out.println("  Vida    " + barra(heroe.getVida(), heroe.getVidaMaxima()));
        System.out.println("  Energia " + barra(heroe.getEnergia(), heroe.getEnergiaMaxima()));
        System.out.println("  Puntos: " + heroe.getPuntos() + " | Objetos: " + heroe.getInventario().size()
                + " | Enemigos: " + cantidadEnemigos);
    }

    public void mostrarInventario(Heroe heroe) {
        ArrayList<Objeto> inventario = heroe.getInventario();
        System.out.println("Inventario:");
        if (inventario.isEmpty()) {
            System.out.println("  (vacio)");
        }
        for (int i = 0; i < inventario.size(); i++) {
            System.out.println("  " + (i + 1) + ") " + inventario.get(i));
        }
    }

    public void mostrarEnemigosEInventario(Heroe heroe, ArrayList<Entidad> enemigos) {
        mostrarInventario(heroe);
        System.out.println("Enemigos:");
        for (Entidad enemigo : enemigos) {
            System.out.println("  - " + enemigo.getNombre() + " " + barra(enemigo.getVida(), enemigo.getVidaMaxima()));
        }
    }

    public void mostrarResumenFinal(Heroe heroe, boolean jefeDerrotado, boolean abandono, int turno, int limiteTurnos) {
        String resultado;
        if (jefeDerrotado) {
            resultado = "*** VICTORIA! Derrotaste al Dragon Ancestral ***";
        } else if (abandono) {
            resultado = "DERROTA: abandonaste la mazmorra.";
        } else if (!heroe.estaVivo()) {
            resultado = "DERROTA: tu heroe ha muerto.";
        } else {
            resultado = "DERROTA: se acabo el tiempo y la mazmorra se derrumbo.";
        }

        int bono = jefeDerrotado ? heroe.getVida() * 2 + (limiteTurnos - turno) : 0;
        int total = heroe.getPuntos() + bono;

        String rango;
        if (total >= 250) {
            rango = "Leyenda de Eldoria";
        } else if (total >= 150) {
            rango = "Aventurero veterano";
        } else if (total >= 60) {
            rango = "Aventurero";
        } else {
            rango = "Novato";
        }

        System.out.println("\n================= RESUMEN FINAL =================");
        System.out.println(resultado);
        System.out.println("Heroe: " + heroe.getNombre() + " (" + heroe.getClase() + ")");
        System.out.println("Turnos jugados: " + turno + " / " + limiteTurnos);
        System.out.println("Enemigos derrotados: " + heroe.getDerrotados());
        System.out.println("Puntos: " + heroe.getPuntos() + " + bono de victoria " + bono);
        System.out.println("PUNTAJE TOTAL: " + total + "  ->  Rango: " + rango);
        System.out.println("=================================================");
    }

    //Barra de progreso en texto
    public String barra(int actual, int maximo) {
        int llenos = (int) Math.round(10.0 * actual / maximo);
        StringBuilder barra = new StringBuilder("[");
        for (int i = 0; i < 10; i++) {
            barra.append(i < llenos ? '#' : '-');
        }
        return barra.append("] ").append(actual).append("/").append(maximo).toString();
    }

}
