/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

/** Jefe final: cada 3 turnos exhala fuego. Si lo derrotas, ganas la partida. */
public class Dragon extends Enemigo {

    private int turnosActivo;

    public Dragon() {
        super("Dragón Ancestral", 35, 7, 2, 100);
    }

    @Override
    public char getSimbolo() { return 'D'; }

    @Override
    public int calcularDanio() {
        return getAtaque() + tirarDado(10);
    }

    @Override
    public int getAlcance() { return 3; }

    @Override
    public boolean esJefe() { return true; }

    @Override
    public String actuar(Tablero tablero, Entidad objetivo) {
        turnosActivo++;
        if (turnosActivo % 3 == 0 && distanciaA(objetivo) <= getAlcance()) {
            int real = objetivo.recibirDanio(8 + tirarDado(6));
            return "¡" + getNombre() + " exhala un ALIENTO DE FUEGO! Recibes " + real + " de daño.";
        }
        return super.actuar(tablero, objetivo);
    }

    public int getTurnosActivo() { return turnosActivo; }

    public void setTurnosActivo(int turnosActivo) {
        if (turnosActivo < 0) {
            throw new IllegalArgumentException("Los turnos no pueden ser negativos.");
        }
        this.turnosActivo = turnosActivo;
    }
}
