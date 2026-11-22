/*
 * Proyecto Final - Modelos de Programación Orientada a Objetos
 * Facultad de Ingeniería, UNAM
 * Integrantes: Kev
 * Grupo: 5
 * Semestre: 2027-1
 */
package modelo;

/** Jefe final. Cada 3 turnos exhala fuego que ignora la defensa. Derrotarlo da la victoria. */
public class Dragon extends Enemigo {

    private int turnosActivo;

    public Dragon() {
        super("Dragón Ancestral", 35, 7, 2, 100);
    }

    @Override
    public char getSimbolo() { return 'D'; }

    @Override
    public int calcularDanio() {
        return getAtaqueBase() + tirarDado(10);
    }

    @Override
    public int getAlcance() { return 3; }

    @Override
    public int getRadioDeteccion() { return 5; }

    @Override
    public boolean esJefe() { return true; }

    @Override
    public String actuar(Tablero tablero, Entidad objetivo) {
        turnosActivo++;
        boolean enRango = distanciaA(objetivo) <= getAlcance();
        if (enRango && turnosActivo % 3 == 0) {
            objetivo.recibirDanioDirecto(8 + tirarDado(6));
            return "¡" + getNombre() + " exhala un ALIENTO DE FUEGO y te causa "
                    + objetivo.getUltimoDanioRecibido() + " de daño!";
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
