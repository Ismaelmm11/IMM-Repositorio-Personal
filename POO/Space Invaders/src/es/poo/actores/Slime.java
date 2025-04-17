package es.poo.actores;



import java.awt.image.BufferedImage;

import es.poo.estado.EstadoJuego;
import es.poo.grafico.Recursos;
import es.poo.math.Cronometro;
import es.poo.math.Vector2D;


/**
 * Clase que extiende a {@link Alien} y representa a los enemigos Slime.<br><br>
 * Los Slimes son enemigos que son inmóviles y tanques, es decir no se mueven ni atacan al Jugador y solo 
 * reciben balas, es por eso que tienen buena cantidad de vida. A parte de eso tienen 2 habilidades:
 * <br><br> 		
 * 	- Coraza : Cuando se transforma a su forma acorazada todos los disparos que impacten con el
 * 	Slime rebotarán hacia el jugador con mucha velocidad.
 * <br><br>
 *  - Multiplicacion: Cuando muere genera 2 Slimes medianos con menos vida y estos cuando mueren
 *  generan otros 2 más pequeños con poca vida y ya estos sí que mueren.
 *  <br><br>
 *  PD: Los Slimes medianos y pequeños no tienen la habilidad Coraza.
 *  
 *  
 */
public class Slime extends Alien{


	// Enum con los posibles tamaños del Slime.
	public enum Tamano {
		BIG, 
		MIDDLE,
		LITTLE
	}

	//-------------------------------------[ Variables de Slime] ----------------------------------//

	// Velocidad con la que vuelve el disparo reflejado:
	private static final int VEL_REFLEJO = 25; 

	// Tiempos entre transformaciones:
														//3500, 8000, 12500, 15000
	private static final int[] TIEMPO_TRANSFORMACION = {2500, 7500, 12500, 15000};

	// Tiempo que dura la animacion de la transformacion:
	private static final int DURACION_TRANSFORMACION = 3500;

	// Distintos conjuntos de imagenes que puede usar el Slime.
	private static final BufferedImage[] littleSlime = Recursos.littleSlime;
	private static final BufferedImage[] midSlime = Recursos.midSlime;
	private static final BufferedImage[] bigSlime = Recursos.bigSlime;
	private static final BufferedImage[] slimeAcorazado = Recursos.slimeAcorazado;

	// Conjunto de imagenes que contiene la animacion de la tranformación del Slime.
	private static final BufferedImage[] slimeTransformado = Recursos.transformacionSlime;


	private Tamano tamano;						// Tamaño del Slime

	private int tiempoTransformacion;

	private Cronometro cronoTransformacion;	// Para controlar las transformaciones del Slime.

	private boolean transformando;				// Indica que el Slime se va a transformar.		
	private boolean acorazado;					// Indica que está en su forma coraza.

	private int indiceTransformacion;			// Indica la transformacion actual.


	//-------------------------------------[ Métodos de Slime] ----------------------------------//


	/**
	 * Constructor de la clase Slime que inicializa las variables.
	 * 
	 * @param posicion	- Posicion inicial del Slime.
	 * @param imagen	- Imagen que representa al Slime.
	 * @param tamano	- Tamaño del Slime.
	 * @param estado	- Referencia al manejador de eventos.
	 * 
	 * @throws IllegalArgumentException	- Si se coloca el Slime en una posicion inválida.
	 */
	public Slime(Vector2D posicion, BufferedImage imagen, int vida, int puntuacion, Tamano tamano) throws IllegalArgumentException {
		super(posicion, imagen, vida, puntuacion);

		this.tamano = tamano;

		setTiempoTransformacion();

		setFrames();

		indiceTransformacion = 0;

		transformando = false;
		acorazado = false;
	}

	/**
	 * Método parcialmente sobreescrito que se encarga de animar, actualizar el
	 * estado y transformar al Slime.
	 */
	@Override
	public void actualizar() {

		/*
		 * Como el Slime es un objeto inmovil no hace falta calcular su Centro ni si está 
		 * colisionando, en caso de que se moviese hay que añadir las siguientes líneas al 
		 * final del método:
		 * setCentro();
		 * colision();
		 */

		/* 
		 * Se me ocurrio la idea de que los Slimes medianos puedan volver a ser grandes
		 * así podemos dejar de subestimarlos.
		 * Solo los Slimes grandes se podrán transformar.
		if(tamano == Tamano.MIDDLE || tamano == Tamano.LITTLE) {
			super.actualizar();
			return;
		}
		 */

		iniciarTransformacion();

		// Mientras no se ejecute la transformacion animamos el objeto como de costumbre.
		if(cronoTransformacion.getActivo() == true && transformando == false) {
			cronoTransformacion.actualizar();
			super.actualizar();
			return;
		}

		transformacion();
	}

	/**
	 * Método que verifica si el cronometro 'cronoTransformacion' ha acabado para dar inicio
	 * a la transformación.
	 */
	private void iniciarTransformacion() {

		if(cronoTransformacion.getActivo() == false && transformando == false) {
			if(indiceTransformacion == 0) {
				indiceTransformacion++;
			}
			else {
				indiceTransformacion--;
			}
			transformando = true;
		}
	}

	/**
	 * Método se encarga de transformar al Slime del estado Base al Acorazado y viceversa.
	 */
	private void transformacion() {

		if(cronoTransformacion.getActivo() == false && transformando == true) {

			setFPS(200);
			setFrames(slimeTransformado);
			temporizador.iniciar(FPS);

			cronoTransformacion.iniciar(DURACION_TRANSFORMACION);

			if(indiceTransformacion == 1) {
				acorazado = true;
				indice = 0;
			}
			else {
				acorazado = false;
				indice = 13;
			}
		}

		animarTransformacion();
	}

	/**
	 * Método que se encarga de animar la transformación y preparar los frames y los FPS para 
	 * cuando acabe la transformacion.
	 */
	private void animarTransformacion() {

		if(temporizador.getActivo() == false) {
			if(indiceTransformacion == 1) {
				indice++;
				if(indice >= slimeTransformado.length) {
					transformando = false;
					transformarCoraza();
				}
				else {

				}
			}
			else {
				indice--;
				if(indice <= 0) {
					transformando = false;
					transformarBase();
				}
			}

			temporizador.iniciar(FPS);
		}

		temporizador.actualizar();
	}

	/**
	 * Método que asigna los frames, FPS y tiempo de transformacion para el 
	 * slime en su estado acorazado.
	 */
	private void transformarCoraza() {
		if(tamano == Tamano.MIDDLE) {
			tamano = Tamano.BIG;
		}
		setFrames(slimeAcorazado);
		indice = 0;
		setFPS(250);
		setTiempoTransformacion();
		cronoTransformacion.iniciar(tiempoTransformacion);
	}

	/**
	 * Método que asigna los frames, FPS y tiempo de transformacion para el 
	 * slime en su estado base.
	 */
	private void transformarBase() {
		setFrames(bigSlime);
		indice = 0;
		setFPS(125);
		//acorazado = false;
		setTiempoTransformacion();
		cronoTransformacion.iniciar(tiempoTransformacion);
	}

	/**
	 * Método que devuelve el booleando acorazado.
	 * 
	 * @return - True si el Slime está acorazado, False en otro caso.
	 */
	public boolean getAcorazado() {
		return acorazado;
	}

	/**
	 * Método parcialmente sobreescrito, que además de matar el objeto: si el Slime muerto era 
	 * grande genera a sus lados 2 Slimes medianos, si el Slime muerto era mediano genera 2 nuevos
	 * Slimes pequeños a sus lados y si el Slime muerto era pequeño simplemente muere.
	 */
	public void kill() {

		super.kill();
		if(getVida() == 0) {
			super.kill();
			if(tamano == Tamano.BIG) {
				Slime slime = new Slime(new Vector2D(
						posicion.getX() - 48, posicion.getY()),
						Recursos.midSlime[0],
						2,
						100,
						Tamano.MIDDLE);
				
				slime.iniciarCronometro();
				EstadoJuego.addFiguraMovil(slime);

				slime = new Slime(new Vector2D(
						posicion.getX() + 48, posicion.getY()),
						Recursos.midSlime[0],
						2,
						100,
						Tamano.MIDDLE);
				
				slime.iniciarCronometro();
				EstadoJuego.addFiguraMovil(slime);
			}
		}
		/*
			else if(tamano == Tamano.MIDDLE) {

				EstadoJuego.addFiguraMovil(new Slime(new Vector2D(
						posicion.getX() + 16, posicion.getY()),
						Recursos.littleSlime[0],
						1,
						Tamano.LITTLE));
				EstadoJuego.addFiguraMovil(new Slime(new Vector2D(
						posicion.getX() - 16, posicion.getY()),
						Recursos.littleSlime[0],
						1,
						Tamano.LITTLE));
			}
		 */
	}

	/**
	 * Método que establece los frames de animación según el tamaño del Slime. Básicamente
	 * el rango del tamaño es (Grande=64px, Mediano=48px, Pequeño=32px).
	 */
	private void setFrames() {
		if(tamano == Tamano.BIG) {
			super.setFrames(bigSlime);
		}
		else if(tamano == Tamano.MIDDLE) {
			super.setFrames(midSlime);
		}
		/*
		else {
			super.setFrames(littleSlime);
		}
		 */
	}

	/**
	 * Método de clase que devuelve la velocidad del disparo reflejado al estar en modo coraza.
	 * 
	 * @return - Velocidad del disparo reflejado.
	 */
	public static int getVelocidadReflejo() {
		return VEL_REFLEJO;
	}

	private void setTiempoTransformacion() {
		tiempoTransformacion = TIEMPO_TRANSFORMACION[random.nextInt(4)];
	}

	@Override
	public void iniciarCronometro() {
		cronoTransformacion = new Cronometro();

		if(tamano == Tamano.MIDDLE) {
			cronoTransformacion.iniciar(TIEMPO_TRANSFORMACION[3]);
		}
		else {
			cronoTransformacion.iniciar(tiempoTransformacion);
		}
	}
}
