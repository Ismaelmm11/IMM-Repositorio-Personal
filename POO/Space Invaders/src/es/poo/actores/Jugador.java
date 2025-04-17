package es.poo.actores;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import es.poo.entrada.Teclado;
import es.poo.estado.EstadoJuego;
import es.poo.grafico.Recursos;
import es.poo.grafico.Sonido;
import es.poo.math.*;

/**
 * Clase que extiende a FiguraMovil y representa el objeto Jugador.<br><br>
 * 
 * El objeto Jugador representa al jugador de la partida que se enfrenta a los Aliens.
 * Este tiene las capacidades de moverse libremente dentro un amplio rango, disparar rayos 
 * laser y recoger objetos/power ups.
 */
public class Jugador extends FiguraMovil {
	
	//-------------------------------------[ Parámetros del Jugador] ----------------------------------//

	//Parámetros del disparo de la nave:
	private static final int CADENCIA = 750;		// Intervalo entre disparos.
	private static final double VEL_DISP = 7.5;			// Velocidad del disparo.
	
	//Bordes del rango por donde se puede mover el jugador.
	private static final int BORDE_SUP = 480;
	private static final int BORDE_DER = 1140;
	private static final int BORDE_IZQ = 0;
	private static final int BORDE_INF = 610;
	
	// Frames para animar el fuego que propulsa al jugador.
	private static final BufferedImage[] fuegoNave = Recursos.fuegoNave;
	
	// Cronometros:
	private Cronometro temporizadorDisparo = new Cronometro();	// Para la cadencia.
	private Cronometro temporizadorInmortal = new Cronometro();	// Para el estado inmortalidad.
	private Cronometro temporizadorTICKS = new Cronometro();	// Para el efecto parpadeante.
	
	private Sonido sonidoDisparo;
	
	private boolean inmortal;						// Indica si el Jugador es vulnerable.
	private boolean visible;						// Indica si el Jugador es visible.
	
	private int vida;								// Num. de vidas del jugador.
	
	private double velocidad;						// Velocidad de desplazamiento.

	
	//-------------------------------------[ Métodos del Jugador] ----------------------------------//
	
	
	/**
	 * Constructor de la clase Jugador.
	 * 
	 * @param posicion  - Posicion inicial del Jugador.
	 * @param velocidad	- Velocidad de desplazamiento.
	 * @param imagen	- Imagen que representa al Jugador.
	 */
	public Jugador(Vector2D posicion, double velocidad, BufferedImage imagen) {
		super(posicion, imagen);
		
		this.velocidad = velocidad;
		
		setFrames(fuegoNave);
			
		vida = 5;
		inmortal = false;
		visible = true;
		
		sonidoDisparo = new Sonido(Recursos.sonidoDisparo);
		
		temporizadorDisparo.iniciar(CADENCIA);
	}
	
	/**
	 * Método que establece el número de vidas del Jugador.
	 * 
	 * @param vida - Numero de vidas que tendrá el jugador.
	 */
	public void setVidas(int vida) {
		this.vida = vida;
	}
	
	/**
	 * Método que devuelve el numero de vidas del Jugador.
	 * 
	 * @return - Número de vidas del jugador.
	 */
	public int getVida() {
		return vida;
	}
	
	/**
	 * Método que debuelve el estado inmortal del Jugador.
	 * @return - True si el Jugador es inmortal, false en caso contrario.
	 */ 
	public boolean getInmortal() {
		return inmortal;
	}

	/**
	 * Método parcialmente sobreescrito que actualiza el estado del Jugador. Se encarga del
	 * desplazamiento, de disparar, de animar, del estado de invulnerabilidad y de los cronometros.
	 */
	public void actualizar() {
		
		super.actualizar();
		
		// Dependiendo de la tecla pulsada el jugador se desplazará a una direccion.
		if(Teclado.DER == true) {
			posicion = posicion.addX(velocidad);
		}
		if(Teclado.IZQ == true) {
			posicion = posicion.subX(velocidad);
		}
		if(Teclado.UP == true) {
			posicion = posicion.subY(velocidad);
		}
		if(Teclado.DOWN == true) { 
			posicion = posicion.addY(velocidad);
		}
		
		manejarInmortalidad();
		
		// En estado de inmortalidad no permitimos que dispare.
		if(Teclado.SHOOT == true && temporizadorDisparo.getActivo() == false && inmortal == false) {
			disparar();
		}
		
		limitarMovimiento();
		
		actualizarCronometros();
	}

	/**
	 * Método que se encarga de dibujar al jugador por pantalla.
	 * 
	 * @param g - Objeto gráfico.
	 */
	public void dibujar(Graphics g) {
		
		// Si el objeto es invisible no dibujamos nada.
		if(visible == false) {
			return;
		}

		g.drawImage(imagen, (int)posicion.getX(), (int)posicion.getY(), null);
		g.drawImage(fuegoNave[indice], (int)posicion.getX() + (int)(ancho/3), (int)posicion.getY() + (int)(alto/1.3), null);
		
		// DEBUG:
		g.setColor(Color.YELLOW);
		g.drawString(".", (int)getCentro().getX(), (int)getCentro().getY());
		
		g.drawRect((int)posicion.getX(), (int)posicion.getY(), ancho, alto);
	}
	
	/**
	 * Método que gestiona la "muerte" del jugador. Reduce en una unidad la vida del jugador y
	 * activa la invulnerabilidad por un periodo de tiempo.<br><br>
	 * 
	 * En caso de que el Jugador tenga 0 vidas, este morirá.
	 */
	public void kill() {
		if(inmortal == true) {
			return;
		}
		vida--;
		inmortal = true;
		temporizadorInmortal.iniciar(3000);
		if(vida == 0) {
			super.kill();
		}
	}
	
	
	/**
	 * Método que no permite que el Jugador salga de los límites establecidos. 
	 * Siempre que su posicion sobrepase el límite será redirigido de vuelta al interior 
	 * del rango.
	 */
	private void limitarMovimiento() {
		if(posicion.getX() >= BORDE_DER) {
			posicion = new Vector2D(BORDE_DER, posicion.getY());
		}
		if(posicion.getX() <= BORDE_IZQ) {
			posicion = new Vector2D(BORDE_IZQ, posicion.getY());
		}
		if(posicion.getY() >= BORDE_INF) {
			posicion = new Vector2D(posicion.getX(), BORDE_INF);
		}
		if(posicion.getY() <= BORDE_SUP) {
			posicion = new Vector2D(posicion.getX(), BORDE_SUP);
		}
	}
	
	/**
	 * Método que genera un disparo del jugador.
	 */
	private void disparar() {
		Disparo disparo = new Disparo(
				new Vector2D(posicion.getX() + ancho/4, posicion.getY()),
				VEL_DISP,
				Recursos.disparo[0],
				Recursos.disparo,
				this
				);
		
		EstadoJuego.addFiguraMovil(disparo);
		sonidoDisparo.reproducir();
		temporizadorDisparo.iniciar(CADENCIA);
	}
	
	/**
	 * Método que se encargar de manejar la inmortalidad, haciendo que el Jugador
	 * tenga un efecto parpadeante mientras es inmortal.
	 */
	private void manejarInmortalidad() {
		if(temporizadorInmortal.getActivo() == false) {
			inmortal = false;
			visible = true; 
		}
			
		if(inmortal == true) {
				
			if(temporizadorTICKS.getActivo() == false) {
				temporizadorTICKS.iniciar(200);
				visible = !visible;
			}
		}
	}
	
	/**
	 * Método que se encarga de actualizar los Cronometros.
	 */
	private void actualizarCronometros() {
		temporizadorDisparo.actualizar();
		temporizadorInmortal.actualizar();
		temporizadorTICKS.actualizar();	
	}
}
