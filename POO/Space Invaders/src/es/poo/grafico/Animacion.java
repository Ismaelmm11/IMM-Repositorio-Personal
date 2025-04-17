package es.poo.grafico;

import java.awt.image.BufferedImage;

import es.poo.math.Cronometro;
import es.poo.math.Vector2D;

/**
 * Clase encargada de animaciones y efectos especiales que puedan ocurrir en el juego(explosiones, rayos...)
 */
public class Animacion {

	private BufferedImage[] frames;
	
	private int velocidad;
	private int indice;
	
	Cronometro tasaRefresco; 
	
	private Vector2D posicion;
	
	private int numRepeticiones;
	
	private boolean animado;
	
	private boolean bucle;
	
	public Animacion(BufferedImage[] frames, int velocidad, Vector2D posicion, boolean bucle) {
		this.frames = frames;
		this.velocidad = velocidad;
		this.posicion = posicion;
		
		if(bucle == true) {
			numRepeticiones = 999;
		}
		
		this.bucle = bucle;
		
		indice = 0;
		
		
		tasaRefresco = new Cronometro();
		tasaRefresco.iniciar(this.velocidad);
		
		animado = true;
	}
	
	public void actualizar() {
		
		if(tasaRefresco.getActivo() == false) {
			tasaRefresco.iniciar(velocidad);
			indice++;
			if(indice == frames.length && bucle == false) {
				animado = false;
			}
			else if(indice == frames.length && bucle == true) {
				if(numRepeticiones == 0) {
					animado = false;
					return;
				}
				numRepeticiones--;
				indice = 0;
			}
		}
		
		tasaRefresco.actualizar();
	}
	
	public void setNumRepeticiones(int numRepeticiones) {
		if(bucle == false) {
			throw new IllegalStateException("Para establecer el número de veces que una animacion se "
					+ "repite, el booleano bucle del objeto debe ser 'True'");
		}
		this.numRepeticiones = numRepeticiones;
	}
	
	public boolean getAnimado() {
		return animado;
	}
	
	public Vector2D getPosicion() {
		return posicion;
	}
	
	public BufferedImage getCurrentFrame() {
		if(animado == true) {
			return frames[indice];
		}
		return null;
	}
}
