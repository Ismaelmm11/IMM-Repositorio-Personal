package es.poo.actores;

import java.awt.image.BufferedImage;

import es.poo.math.Vector2D;
import es.poo.ventana.Ventana;

/**
 * Clase que extiende de FiguraMovil y representa los disparos del juego.
 */
public class Disparo extends FiguraMovil{
	
	//-------------------------------------[ Parámetros del Jugador] ----------------------------------//
	
	private FiguraMovil lanzador;		// La figura que ha lanzado el disparo.
	
	private double velocidad;			// Velocidad de desplazamiento.
	
	protected boolean disparoNave;		// Indica si el disaparo es del Jugador. 

	
	/**
	 * Constructor de la clase Disparo.
	 * 
	 * @param posicion	- Posicion inicial del Disparo.
	 * @param velocidad	- Velocidad del desplazamiento del Disparo.
	 * @param imagen	- Imagen que representa al Disparo.
	 * @param frames	- Frames que formarán la animación del disparo.
	 * @param lanzador	- 
	 */
	public Disparo(Vector2D posicion, double velocidad, BufferedImage imagen, BufferedImage[] frames,
			FiguraMovil lanzador) {
		super(posicion, imagen);
		
		this.velocidad = velocidad;
		this.lanzador = lanzador;
		
		setDisparoNave();
		
		setFrames(frames);
		//setFPS(50);
	}
	
	/**
	 * Método totalmente sobreescrito, fija el centro del Disparo en la punta del objeto.
	 */
	protected void setCentro() {
		// Si el disparo es del Jugador la punta estará en la parte superior.
		if(esDisparoNave() == true) {
			centro = new Vector2D(posicion.getX() + ancho/2, posicion.getY() + ancho/2);
			return;
		}
		
		// Si no lo es, la punta estará en la parte inferior.
		centro = new Vector2D(posicion.getX() + ancho/2, posicion.getY() + alto);
	}
	
	/**
	 * Método para establecer la velocidad del disparo.
	 * 
	 * @param velocidad - Velocidad a establecer.
	 */
	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}
	
	/**
	 * Método que indica si el disparo fue realizado por el Jugador.
	 * 
	 * @return	- True si el disparo es del Jugador, False en otro caso.
	 */
	public boolean esDisparoNave() {
		return disparoNave;
	}
	
	/** 
	 * Método que establece si el disparo proviene del Jugador o no.
	 */
	private void setDisparoNave() {
		if(lanzador instanceof Jugador) {
			disparoNave = true;
		}
		else {
			disparoNave = false;
		}
	}

	/**
	 * Método que se encarga de actualizar el estado del disparo, eso incluye su animación
	 * y desplazamiento. Si es un disparo del Jugador se desplazará verticalmente hacia
	 * arriba, en otro caso lo hará hacia abajo.
	 */
	public void actualizar() {
		super.actualizar();
		
		
		// Si es del Jugador se mueve hacia arriba
		if(esDisparoNave()) {
			posicion = posicion.subY(velocidad);
			//Cuando el disparo salga de la ventana hay que eliminarlo, para que deje de consumir recursos.
			if(posicion.getY() <= 0) {
				kill();
			}
		}
		// En otro caso irá hacia abajo.
		else {
			posicion = posicion.addY(velocidad);
			if(posicion.getY() >= Ventana.ALTO - 50) {
				kill();
			}
		}
	}
	
	/**
	 * Método que invierte la trayectoria del disparo.
	 */
	public void reflejarDisparo() {
		if(lanzador instanceof Jugador) {
			disparoNave = false;
		}
		else {
			disparoNave = true;
		}
	}
}
