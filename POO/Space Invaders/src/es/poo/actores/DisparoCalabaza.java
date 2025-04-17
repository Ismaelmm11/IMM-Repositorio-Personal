package es.poo.actores;

import java.awt.image.BufferedImage;

import es.poo.estado.EstadoJuego;
import es.poo.math.Vector2D;

/**
 * Clase que extiende a Disparo y representa al objeto llamado "Disparo Calabaza".<br><br>
 * 
 * Este es un disparo especial que es lanzado por el Alien Calabaza, tiene la propiedad de que 
 * antes de llegar al final explota y genera 6 puas que se expanden en forma de Estrella.
 */
public class DisparoCalabaza extends Disparo {

	/**
	 * Constructor del objeto DisparoCalabaza
	 * 
	 * @param posicion	- Posicion inicial del objeto.
	 * @param velocidad	- Velocidad de desplazamiento.
	 * @param imagen	- Imagen que representa al objeto.
	 * @param frames	- Frames que formarán la animación del disparo.
	 * @param lanzador	- Figura que lanza el disparo.
	 */
	public DisparoCalabaza(Vector2D posicion, double velocidad, BufferedImage imagen,
			BufferedImage[] frames, FiguraMovil lanzador) {
		super(posicion, velocidad, imagen, frames, lanzador);
		setFPS(125);
	}
	
	
	/**
	 * Método parcialmente sobreescrito que actualiza el estado del DisparoCalabaza. 
	 * Dependiendo si es lanzado por el jugador o no explotará en un punto del canvas.
	 */
	public void actualizar() {
		super.actualizar();
		
		if(disparoNave == false && centro.getY() >= 567.5) {
			kill();
		}
		
		if(disparoNave == true && (centro.getY() >= 99 && centro.getY() <= 106)) {
			kill();
		}
	}
	
	/**
	 * Método que marca el objeto para eliminarlo y además genera en su centro una 
	 * explosion de puas.
	 */
	protected void kill() {
		super.kill();
		EstadoJuego.addExplosion(getCentro());
		Puas.estrellaPuas(getCentro());
	}
}
