package es.poo.interfaz;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import es.poo.entrada.Raton;
import es.poo.grafico.Recursos;
import es.poo.grafico.Texto;
import es.poo.math.Cronometro;
import es.poo.math.Vector2D;

/**
 * Clase que representa los botones que aparecerán en el menú.
 */
public class Boton {
	
	//-------------------------------------[ Variables para definir el botón ] ---------------------------------------------//

	private BufferedImage fotoHover;		// Imagen que tendrá el botón cuando el ratón esté encima.
	private BufferedImage fotoBoton;		// Imagen que tendrá por defecto el botón.
	
	private Texto texto;					// Texto que contiene el botón.
	
	private Rectangle cuadroColision;		// Es el rango donde se le puede dar click al botón.
	
	private boolean hover;					// Booleano para saber si el ratón está encima.
	
	private Vector2D posicion;
	
	private String cadenaTxt;
	
	private boolean visible;
	
	private Accion accion;					// Es la acción a realizar si se pulsa el botón.
	
	private static Cronometro delay = new Cronometro();;
	
	/**
	 * Método constructor de la clase botón.
	 * 
	 * @param fotoHover		- Foto del hover del botón.
	 * @param fotoBoton		- Foto por defecto del ratón.
	 * @param x				- Coordenada X
	 * @param y				- Coordenada Y
	 * @param cadenaTexto	- Texto que va a tener el botón.
	 * @param accion		- Accion que se va realizar al pulsar el botón.
	 */
	public Boton(BufferedImage fotoHover, BufferedImage fotoBoton, Vector2D posicion, String cadenaTxt, boolean visible,Accion accion) {

		this.fotoHover = fotoHover;
		this.fotoBoton = fotoBoton; 
		this.visible = visible;
		this.accion = accion;
		this.cadenaTxt =  cadenaTxt;
		this.posicion = posicion;
		
		iniciarDelay();
		
		// Creamos el rectangulo que va a detectar la colision.
		cuadroColision = new Rectangle((int)posicion.getX(), (int)posicion.getY(), fotoBoton.getWidth(), fotoBoton.getHeight());
		
		inicializarTxt();		
	}
	
	
	/**
	 * Método con el que se actualiza el estado del botón, verficando si el ratón está
	 * encima o si se hace click.
	 */
	public void actualizar() {
		
		if(delay.getActivo() == true) {
			delay.actualizar();
			return;
		}
		
		// Modificar hover según si el ratón está encima o no.
		if(cuadroColision.contains(Raton.x, Raton.y)) {
			hover = true;
		}
		else {
			hover = false;
		}
		
		// En caso de que se haga click en el botón se realiza la acción.
		if(hover == true && Raton.CIR == true  && visible == true && delay.getActivo() == false) {
			iniciarDelay();
			accion.hacerAccion();
		}
	}
	
	
	/**
	 * Método que dibuja el botón en la pantalla.
	 * 
	 * @param g - Objeto gráfico.
	 */
	public void dibujar(Graphics g) {
		
		if(visible == false) {
			return;
		}
		
		// Según si el ratón está encima se desplegará una imagen u otra.
		if(hover == true) {
			g.drawImage(fotoHover, cuadroColision.x, cuadroColision.y, null);
		}
		else{
			g.drawImage(fotoBoton, cuadroColision.x, cuadroColision.y, null);
		}
		
		texto.dibujarTxt(g);
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	private void iniciarDelay() {
		delay.iniciar(300);
	}
	
	private void inicializarTxt() {
		texto = new Texto(Color.BLACK, Recursos.fuenteM);
		texto.setPosicion(new Vector2D((posicion.getX() + fotoHover.getWidth()/2), (posicion.getY()+ fotoHover.getHeight()/1.5)));
		texto.setTexto(cadenaTxt);
	}
}
