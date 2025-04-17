package es.poo.ventana;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import es.poo.entrada.*;
import es.poo.estado.*;
import es.poo.grafico.Recursos;

@SuppressWarnings("serial")
public class Ventana extends JFrame implements Runnable{
	
	//---------------------------------[ Variables para manejar el flujo y la ventana ] -----------------------------------------//
	
	// Constantes que definen el ancho y alto de la ventana.
	public static final int ANCHO = 1200;
	public static final int ALTO = 700;
	
	// Constantes que definen el número de frames por segundo (FPS) y el tiempo de refresco (en nanosegundos).
	private static final int FPS = 60;						
	private static final double REFRESH = 1000000000/FPS; 	
	
	// Objeto Canvas que nos servirá para dibujar por pantalla diferentes elementos.
	private Canvas canvas;		
	
	// Hilo con el cual vamos a controlar la ejecución del juego.
	private Thread hilo;
	
	private boolean ejecucion = false;	//Bool para ver si el juego está en ejecución.
	
	// Objetos BufferStrategy y Graphics para manejar el renderizado de los objetos.
	private BufferStrategy buffer;		
	private Graphics g;		
	
	// Variables para controlar el tiempo.
	private double clk = 0;				
	private int avgFPS = FPS; 			
	
	private Teclado tecla;
	private Raton raton;
	
	
	/**
	 * Método constructor de la clase Ventana, con el cuál inicializamos la ventana donde se ejecutará el juego.
	 */
	public Ventana() {
		
   //-------------------------------Crear Ventana----------------------------------------------------//
		
		// Dar título a la ventana.
		setTitle("Space Invaders");		
		
		// Establecer el tamaño de la ventana.
		setSize(ANCHO, ALTO);	
		
		//.Hacemos que el tamaño no sea modificable.
		setResizable(false);		
		
		// La ventana se inicia en el centro.
		setLocationRelativeTo(null);	
		
		// Parar la ejecucion del programa al cerrarse la ventana
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		canvas = new Canvas();
		
		// Fijar las dimensiones del Canvas:
		canvas.setPreferredSize(new Dimension(ANCHO, ALTO));
		canvas.setMaximumSize(new Dimension(ANCHO, ALTO));
		canvas.setMinimumSize(new Dimension(ANCHO, ALTO));
		
		// Hacemos que el canvas sea el que recibe los eventos de entrada del usuario.
		canvas.setFocusable(true);		
		
		tecla = new Teclado();
		raton = new Raton();
		
		// Añadimos listeners para que el canvas detecte:
		
		canvas.addMouseListener(raton);				// Clicks del ratón.
		canvas.addMouseMotionListener(raton);		// Movimiento del ratón.
		canvas.addKeyListener(tecla);				// Uso de las teclas.

		
		// Añadimos el canvas a la ventana y que comience la magia.
		add(canvas);
	}
	
	
	/**
	 * Método para actualizar los elementos que hay dentro del canvas.
	 */
	private void actualizar() {
		// Con esta línea sabemos que tecla se pulsa en cada momento.
		tecla.actualizar();
		
		// Con esta línea actualizamos todos los elementos de la pantalla-
		Estado.getEstadoActual().actualizar();
	}
	
	
	/**
	 * Método con el cual dibujamos los elementos en el canvas.
	 */
	private void dibujar() {
		
		buffer = canvas.getBufferStrategy();
		
		/* - Al inicio no hay estrategia de buffer, por le asignamos la estrategia del triple buffer:
		 * - Un buffer está listo para ser mostrado por pantalla.
		 * - Otro buffer está a la espera para ser mostrado.
		 * - El último buffer está dibujando la siguiente imagen. */
		if(buffer == null) {
			canvas.createBufferStrategy(3);
			return;
		}
		
		// Obtenemos un Objeto Graphics con el cual vamos a dibujar.
		g = buffer.getDrawGraphics();	
		
		//----------------Comienzo Dibujo-----------------------------//

		g.fillRect(0,  0,  ANCHO, ALTO);		// Rellenamos el fondo del canvas de negro.
		g.setColor(Color.yellow);				// Asignamos el color amarillo.
		g.drawString(""+avgFPS, 0, 10);			// Dibujar los fps promedios en la esquina superior izquierda.
		
		Estado.getEstadoActual().dibujar(g);	// Dibujamos los elementos que hay dentro del canvas.
		
		//--------------------Fin Dibujo------------------------------//
		
		// Liberamos el Objeto Graphics
		g.dispose();
		
		// Mostramos la imagen que tiene el buffer.
		buffer.show();		
	}
	
	
	/**
	 * Método con el que inicializamos todos los recursos del juego.
	 * Mientras cargan los recursos del juego mostramos una barra de carga.
	 */
	private void inicializar() {
		
		// Usamos un hilo para cargar los objetos.
		Thread hiloCarga = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Recursos.inicializar();
			}
		});

		Estado.cambiarEstado(new EstadoCarga(hiloCarga));
	}
	
	
	/**
	 * Método implementado por la interfaz Runnable, básicamente es un método donde se ha de describir lo que debe
	 * realizar el hilo.
	 * En nuestro caso controla el bucle principal del juego para actualizar y renderizar los elementos.
	 */
	public void run() {
		
		long ahora = 0;						// Variable para registrar el tiempo actual.
		long lastTime = System.nanoTime(); 	// Variable para marcar el último tiempo de la máquina en ns.
		
		int frames = 0;						// Variable que cuenta los frames.
		long tiempo = 0;					// Variable para contar ns.
		
		
		inicializar();
		
		/*Haciendo uso de las variables de tiempo marcamos la ejecución a 60 FPS, esto se debe a que solo 
		 *actualizamos cuando CLK supera 1, es decir cuando se cumple la tasa de refresco.
		 */
		while(ejecucion) {
			
			ahora = System.nanoTime();
			clk += (ahora - lastTime)/REFRESH;
			
			tiempo += (ahora - lastTime);
			
			lastTime = ahora;
			
			// Cuando clk supera 1 actualizamos y renderizamos 1 frame.
			if(clk >= 1) {
				actualizar();
				dibujar();
				clk = 0;	
				frames++;
			}
			
			// Cuanto la tiempo supera 1 segundo actualizamos la media de fps.
			if(tiempo >= 1000000000) {
				avgFPS = frames;
				
				frames = 0;
				tiempo = 0;
			}
			
		}
		
		
		//fin();
	}
	
	
	/**
	 * Método con el que se inicializa el juego, por ende se inicia el hilo principal de ejecucion.
	 */
	public void iniciar() {
		
		/* 
		 * Se le pasa este objeto como parámetro para que sepa que el método 'run' 
		 * que debe realizar es el que hay en esta clase. 
		*/
		hilo = new Thread(this);		
		hilo.start();
		ejecucion = true;
		
	}
	
	/*
	 * Usaba este método para finalizar el juego, pero soy bobo y me acabo de dar cuenta que esto nunca se va a ejecutar.
	 * 
	//Con esto finalizo el programa.
	private void fin() {
		try {
			hilo.join();
			ejecucion = false;
		}
		catch(InterruptedException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	 */
}
