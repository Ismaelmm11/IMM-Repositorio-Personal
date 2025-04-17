package es.poo.estado;

import java.awt.Graphics;

public abstract class Estado {
	
	private static Estado estadoActual = null;

	public static Estado getEstadoActual() {
		return estadoActual;
	} 
	
	public static void cambiarEstado(Estado nuevoEstado) {
		estadoActual = nuevoEstado;
	}
	
	public abstract void actualizar();
	public abstract void dibujar(Graphics g);
	
}
