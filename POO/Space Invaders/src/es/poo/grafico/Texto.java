package es.poo.grafico;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import es.poo.math.Vector2D;

/**
 * Clase que sirve para controlar como se imprime el texto del juego.
 */
public class Texto {
	
	//-------------------------------------[ Variables para manejar el texto ] ---------------------------------------------//
	
	// Constante que define la velocidad en la que desaparece el texto.
	private static final float VEL_DESVANECER = 0.01f;

	private float transparente;				// Nivel de transparencia del texto.
	private Vector2D posicion = null;		// Posicion del texto.
	private Color color;					// Color del texto.
	private Font fuente;					// Fuente del texto.
	private String texto = null;			// Mensaje a imprimir.
	
	/**
	 * Constructor de la clase Texto.
	 * 
	 * @param esta			- Referencia a la partida.
	 * @param posicion		- Posicion del texto.
	 * @param color			- Color del texto.
	 * @param desvanecer	- Booleano para saber si el texto desaperece o no.
	 * @param fuente		- Fuente del texto.
	 * @param texto			- Mensaje a imprimir.
	 */
	public Texto(Color color, Font fuente) {
		this.color = color;
		this.fuente = fuente;
		
		transparente = 1;
	}
	
	public Texto(Color color, Font fuente, Vector2D posicion, String texto) {
		this.color = color;
		this.fuente = fuente;
		this.posicion = posicion;
		this.texto = texto;
		
		transparente = 1;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public void setPosicion(Vector2D posicion) {
		this.posicion = posicion;
	}
	
	public double getTransparente() {
		return transparente;
	}
	
	/**
	 * Método para establecer el color del texto.
	 * 
	 * @param color - Color del texto.
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	
	/**
	 * Método que imprime el texto con los parámetro establecidos en el constructor.
	 * @param g - Objeto gráfico.
	 * 
	 */
	public void dibujarTxt(Graphics g) {
		
		g.setColor(color);
		g.setFont(fuente);
		
		FontMetrics metricasFuente = g.getFontMetrics();
		
		g.drawString(texto, (int)(posicion.getX() - metricasFuente.stringWidth(texto)/2), (int)posicion.getY());
	}
	
	
	/**
	 * Método sobrecargado que imprime una cadena de texto y en una posicion específicados
	 * en los parámetros.
	 * 
	 * @param g - 			- Objeto gráfico.
	 * @param posicion		- Posicion en la que se va a imprimir.
	 * @param texto			- Cadena de texto a mostrar.
	 */
	public void dibujarTxt(Graphics g, Vector2D posicion, String texto) {
		
		g.setColor(color);
		g.setFont(fuente);
		
		FontMetrics metricasFuente = g.getFontMetrics();
		
		g.drawString(texto, (int)(posicion.getX() - metricasFuente.stringWidth(texto)/2), (int)posicion.getY());
	}
	
	
	/**
	 * Método que imprime el texto con un efecto de desvanecimiento y elevando su posicion poco
	 * a poco.
	 * 
	 * @param g2d - Objeto gráfico.
	 * 
	 * @throws NullPointerException - Cuando el texto a imprimir o la posicion son nulas o no
	 * han sido instanciadas al crear el objeto Por favor asegurese de usar el constructor correcto
	 * o los setters.
	 */
	public void dibujarTxtDesvanecer(Graphics2D g2d) throws NullPointerException {
		
		comprobarParametros();
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,  transparente));
		
		dibujarTxt(g2d);
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,  1));
	
		posicion = posicion.subY(1);
		transparente -= VEL_DESVANECER;
	}
	
	private void comprobarParametros() throws NullPointerException{
		if(texto == null) {
			throw new NullPointerException("El texto que se desea imprimir es nulo.");
		}
		if(posicion == null) {
			throw new NullPointerException("La posición donde se va a imprimir el texto es nula.");
		}
	}
}
