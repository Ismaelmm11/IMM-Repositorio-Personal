package es.poo.grafico;

import javax.sound.sampled.Clip;

/**
 * Clase que sirve para controlar el sonido del juego.
 */
public class Sonido {

	// Haremos uso de un objeto Clip, el cual tiene métodos para reproducir archivos de sonido.
	// Solo funciona con archivos ".wav".
	private Clip clip;

	/**
	 * Método constructor de la clase Sonido.
	 * @param clip	- Objeto para reproducir sonido.
	 */
	public Sonido(Clip clip) {
		this.clip = clip;
	}


	/**
	 * Método que reproduce un sonido. 
	 */
	public void reproducir() {
		clip.setFramePosition(0);
		clip.start();
	}


	/**
	 * Método para detener la reproducción de un sonido.
	 */
	public void parar() {
		clip.stop();
	}
}
