package es.poo.estado;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//import javax.swing.JOptionPane;

import es.poo.actores.*;
import es.poo.actores.Slime.Tamano;
import es.poo.grafico.*;
import es.poo.math.Cronometro;
import es.poo.math.Vector2D;
import es.poo.tablas.Puntuacion;
import es.poo.ventana.Ventana;

import java.util.Random;

import javax.swing.JOptionPane;


public class EstadoJuego extends Estado {

	/*
	 * Las filas en las que se mueve el jugador son 3 de unos 60 px de alto (Rango: 480 - 660):
	 * 	- 1º Fila: Empieza en los 480px.
	 * 	- 2º Fila: Empieza en los 540px.
	 * 	- 3º Fila: Empieza en los 600px.
	 * 
	 * Si queremos que se accione un evento que afecte al jugador, por ejemplo los ojos que aparecen
	 * a los lados o alguna explosión tenemos que hacerlo entre este rango de px para poder acertarle.
	 */
	private static final int[] FILA_JUG = {480, 540, 600};

	/*
	 * Por la parte de los Aliens estos están distribuidos en 5 filas de unos 75px de alto (Rango: 50 - 350)
	 * 	- 1º Fila: Empieza en los 50px.
	 * 	- 2º Fila: Empieza en los 125px.
	 * 	- 3º Fila: Empieza en los 200px.
	 * 	- 4º Fila: Empieza en los 275px.
	 * 	- 5º Fila: Empieza en los 350px.
	 * 
	 * Si queremos una organización correcta de los Aliens debemos colocarlos dentro de estas filas.
	 */
	private static final int[] FILA_ALIEN = {50, 125, 200, 275, 350};

	private static final int OJO_DER = 1500;
	private static final int OJO_IZQ = -350;

	private static final int RATIO_SPAWN_OJOS = 10000; // 10000

	private static final int PAUSA_INICIO_FIN = 2000;

	private static final int[] VEL_OJOS = {4, 8, 12};

	private static final BufferedImage[] barraHP = Recursos.barraHP;
	
	private static EstadoJuego partida;

	private static ArrayList<FiguraMovil> figurasMoviles = new ArrayList<FiguraMovil>();
	private static ArrayList<Animacion> animaciones = new ArrayList<Animacion>();
	private static ArrayList<Texto> mensajes = new ArrayList<Texto>();
	
	private Jugador jugador;

	private Cronometro cronoOjos = new Cronometro();

	private Cronometro cronoInicioFin = new Cronometro();

	private Random aleatorio = new Random();

	private int numAleatorio;

	private int nivel;

	private boolean finPartida, inicioPartida;

	private boolean pausa;

	private int puntos;

	private static int numAliens;

	private EstadoJuego() {

		pausa = true;
		finPartida = false;
		inicioPartida = false;

		numAliens = 0;
		puntos = 0;

		addJugador();
	}
	
	public static EstadoJuego getEstadoJuego() {
		if(partida == null) {
			partida = new EstadoJuego();
		}
		return partida;
	}

	public void iniciarLvl1() {

		iniciarPartida("NIVEL 1");
		nivel = 1;
		
		Pulpo pulpo = new Pulpo(new Vector2D(100, FILA_ALIEN[0]), Recursos.pulpo[0], 2, 300);
		pulpo.setCaminoDerecha(1000);
		pulpo.setPosicion(pulpo.getPosicion().addX(400));
	
		Flame flame1 = new Flame(new Vector2D(300, FILA_ALIEN[1]), Recursos.flame[0], 2, 500);
		flame1.setCaminoDerecha();

		Flame flame2 = new Flame(new Vector2D(800, FILA_ALIEN[1]), Recursos.flame[0], 2, 500);
		flame2.setCaminoIzquierda();

		addFiguraMovil(pulpo);
		addFiguraMovil(flame1);
		addFiguraMovil(flame2);

		for(int i = 0; i <= 2; i++) {
			Pulpo pulpoAux = new Pulpo(new Vector2D(250+(250*i), FILA_ALIEN[2]), Recursos.pulpo[0], 2, 300);
			pulpoAux.setCaminoDerecha(150);
			addFiguraMovil(pulpoAux);
		}

		for(int i = 0; i <= 2; i++) {
			Slime slime = new Slime(new Vector2D(350+(200*i), FILA_ALIEN[3]), 
					Recursos.bigSlime[0], 4, 500, Tamano.BIG);
			addFiguraMovil(slime);
		}
		
		pausa = false;
	}
	
	public void iniciarLvl2() {

		iniciarPartida("NIVEL 2");
		nivel = 2;
		
		Calabaza calabaza = new Calabaza(new Vector2D(200, FILA_ALIEN[0]), Recursos.calabaza[0], 2, 750);
		calabaza.setCaminoDerecha();
		addFiguraMovil(calabaza);
		
		calabaza = new Calabaza(new Vector2D(866, FILA_ALIEN[0]), Recursos.calabaza[0], 2, 750);
		calabaza.setCaminoIzquierda();
		addFiguraMovil(calabaza);
		
		Fantasma fantoche = new Fantasma(new Vector2D(450, FILA_ALIEN[1]), Recursos.fantasma[0], 3, 1250);
		addFiguraMovil(fantoche);
		
		for(int i = 0; i <= 2; i++) {
			Pulpo pulpoAux = new Pulpo(new Vector2D(250+(250*i), FILA_ALIEN[2]), Recursos.pulpo[0], 2, 300);
			pulpoAux.setCaminoDerecha(150);
			addFiguraMovil(pulpoAux);
		}
		
		for(int i = 0; i <= 2; i++) {
			Slime slime = new Slime(new Vector2D(450+(100*i), FILA_ALIEN[3]), 
					Recursos.bigSlime[0], 4, 500, Tamano.BIG);
			addFiguraMovil(slime);
		}
		
		
		pausa = false;
	}
	
	public void iniciarLvl3() {

		iniciarPartida("NIVEL 3");
		nivel = 3;
		
		Calabaza calabaza = new Calabaza(new Vector2D(110, FILA_ALIEN[0]), Recursos.calabaza[0], 2, 750);
		calabaza.setCaminoDerecha();
		addFiguraMovil(calabaza);
		
		calabaza = new Calabaza(new Vector2D(441, FILA_ALIEN[0]), Recursos.calabaza[0], 2, 750);
		calabaza.setCaminoDerecha();
		calabaza.setPosicion(new Vector2D(510,FILA_ALIEN[0]));
		addFiguraMovil(calabaza);
		
		calabaza = new Calabaza(new Vector2D(956, FILA_ALIEN[0]), Recursos.calabaza[0], 2, 750);
		calabaza.setCaminoIzquierda();
		addFiguraMovil(calabaza);
		
		for(int i = 0; i <= 1; i++) {
			Pulpo pulpoAux = new Pulpo(new Vector2D(200+(500*i), FILA_ALIEN[1]), Recursos.pulpo[0], 2, 300);
			pulpoAux.setCaminoDerecha(150);
			addFiguraMovil(pulpoAux);
		}
		
		Fantasma fantoche = new Fantasma(new Vector2D(450, FILA_ALIEN[1]), Recursos.fantasma[0], 3, 1250);
		addFiguraMovil(fantoche);
		fantoche = new Fantasma(new Vector2D(250, FILA_ALIEN[2]), Recursos.fantasma[0], 3, 1250);
		addFiguraMovil(fantoche);
		fantoche = new Fantasma(new Vector2D(700, FILA_ALIEN[2]), Recursos.fantasma[0], 3, 1250);
		addFiguraMovil(fantoche);
		
		Flame flame = new Flame(new Vector2D(425, FILA_ALIEN[2]), Recursos.flame[0], 2, 500);
		flame.setCaminoDerecha();

		addFiguraMovil(flame);
		
		Slime slime = new Slime(new Vector2D(400, FILA_ALIEN[1]), 
				Recursos.bigSlime[0], 4, 500, Tamano.BIG);
		addFiguraMovil(slime);
		slime = new Slime(new Vector2D(650, FILA_ALIEN[1]), 
				Recursos.bigSlime[0], 4, 500, Tamano.BIG);
		addFiguraMovil(slime);
		
		for(int i = 0; i <= 2; i++) {
			slime = new Slime(new Vector2D(300+(225*i), FILA_ALIEN[3]), 
					Recursos.bigSlime[0], 4, 500, Tamano.BIG);
			addFiguraMovil(slime);
		}
		
		
		pausa = false;
	}

	private void iniciarPartida(String cadena) {
		cronoInicioFin.iniciar(PAUSA_INICIO_FIN);
		cronoOjos.iniciar(RATIO_SPAWN_OJOS);
		imprimirTxtGrande(cadena);
		inicioPartida = true;
		finPartida = false;
	}

	private void finalizarPartida() {
		cronoInicioFin.iniciar(PAUSA_INICIO_FIN);
		puntuarVida();
		if(numAliens == 0) {
			imprimirTxtGrande("YOU WIN");
		}
		else {
			imprimirTxtGrande("YOU LOSE");
		}
		finPartida = true;
	}
	
	private void reiniciarValores() {
		figurasMoviles.clear();
		animaciones.clear();
		mensajes.clear();
		
		puntos = 0;
		numAliens = 0;
		
		addJugador();
	}
	
	private void addJugador() {
		jugador = new Jugador(new Vector2D(550, 550), 4, Recursos.jugador);	
		figurasMoviles.add(jugador);
	}

	/**
	 * Método que devuelve la coordenada Y donde se encuentra una de las filas del Jugador. Si el numFila es
	 * 1 devolverá la primera fila, si es 2 la segunda fila y si es 3 devolverá la tercera fila.
	 * 
	 * @param numFila - Es el número que indica que fila se quiere obtener.
	 * 
	 * @return - Coordenada Y de una de las filas del Jugador.
	 * 
	 * @throws IllegalArgumentException - Si el parámetro numFila no es ni 1, ni 2 ni 3.
	 */
	public static int getFilaJug(int numFila) throws IllegalArgumentException {
		if(numFila == 1) {
			return FILA_JUG[0];
		}
		else if(numFila == 2) {
			return FILA_JUG[1];
		}
		else if(numFila == 3) {
			return FILA_JUG[2];
		}
		else {
			throw new IllegalArgumentException("El parámetro numFila solo puede tomar los valores 1, 2 o 3");
		}
	}

	private void puntuar(int valor, Vector2D posicion) {
		puntos += valor;
		mensajes.add(new Texto(Color.WHITE, Recursos.fuenteM, posicion,"+"+valor+" PUNTOS"));
	}

	private void puntuarVida() {
		if(jugador.getVida() == 5) {
			puntos += 7500;
			mensajes.add(new Texto(Color.WHITE, Recursos.fuenteM, jugador.getCentro(),
					"5 VIDAS = "+5000+" PUNTOS"));
		}
		else if(jugador.getVida() == 4) {
			puntos += 3000;
			mensajes.add(new Texto(Color.WHITE, Recursos.fuenteM, jugador.getCentro(),
					"4 VIDAS = "+2500+" PUNTOS"));
		}
		else if(jugador.getVida() == 3) {
			puntos += 1000;
			mensajes.add(new Texto(Color.WHITE, Recursos.fuenteM, jugador.getCentro(),
					"3 VIDAS = "+1000+" PUNTOS"));
		}
		else if(jugador.getVida() == 2) {
			puntos += 500;
			mensajes.add(new Texto(Color.WHITE, Recursos.fuenteM, jugador.getCentro(),
					"2 VIDAS = "+500+" PUNTOS"));
		}
	}	

	private void oleadaFacil() {
		double x, xAdv, y;

		int sentido;

		numAleatorio = aleatorio.nextInt(3);

		if(numAleatorio == 1) {
			y = FILA_JUG[0];
		}
		else if(numAleatorio == 2) {
			y = FILA_JUG[1];
		}
		else {
			y = FILA_JUG[2];
		}

		numAleatorio = aleatorio.nextInt(2);

		if(numAleatorio == 1) {
			sentido = 1;
			x = OJO_DER;
			xAdv = OJO_DER - 375;
		}
		else {
			sentido = 0;
			x = OJO_IZQ;
			xAdv = OJO_IZQ + 375;
		}

		Ojos ojo = new Ojos(
				new Vector2D(x, y),
				Recursos.ojos[0],
				1, 
				750,
				VEL_OJOS[0],
				sentido);
		
		addAdvertencia(new Vector2D(xAdv, y));

		figurasMoviles.add(ojo);
	}

	private void oleadaMedio() {
		double x, xAdv, y;

		double yJug = jugador.getPosicion().getY();
		double xJug = jugador.getPosicion().getX();

		int sentido, velocidad;

		velocidad = getVelocidadRandom();

		if(yJug >= FILA_JUG[0] && yJug <= FILA_JUG[1]) {
			y = FILA_JUG[0];
		}
		else if(yJug > FILA_JUG[1] && yJug <= FILA_JUG[2]) {
			y = FILA_JUG[1];
		}
		else {
			y = FILA_JUG[2];
		}



		if(xJug <= 600) {
			sentido = 0;
			x = OJO_IZQ;
			xAdv = OJO_IZQ + 375;
		}
		else {
			sentido = 1;
			x = OJO_DER;
			xAdv = OJO_DER - 375;
		}

		Ojos ojo = new Ojos(
				new Vector2D(x, y),
				Recursos.ojos[0],
				1, 
				750,
				velocidad,
				sentido);
		
		addAdvertencia(new Vector2D(xAdv, y));

		figurasMoviles.add(ojo);
	}

	private void oleadaZigZag() {

		Ojos ojo;
		int aux = 0;
		int x, xAdv, sentido;
		
		numAleatorio = aleatorio.nextInt(2);
		
		if(numAleatorio == 1) {
			x = OJO_DER;
			xAdv = OJO_DER - 375;
			sentido = 1;
		}
		else {
			x = OJO_IZQ - 320;
			xAdv = OJO_IZQ + 375;
			sentido = 0;
		}
		

		for(int i = 0; i < 3; i++) {
			ojo = new Ojos(new Vector2D(x + (Recursos.ojos[0].getWidth()*i), FILA_JUG[aux]),
					Recursos.ojos[0],
					(i+1),
					750,
					VEL_OJOS[0],
					sentido);
			figurasMoviles.add(ojo);

			if(i%2 == 0) {
				aux++;
			}
			else {
				aux--;
			}
		}

		aux = 2;

		for(int i = 3; i < 6; i++) {
			ojo = new Ojos(new Vector2D(x + (Recursos.ojos[0].getWidth()*i), FILA_JUG[aux]),
					Recursos.ojos[0],
					1, 
					750,
					VEL_OJOS[0],
					sentido);
			figurasMoviles.add(ojo);

			if(i%2 == 0) {
				aux++;
			}
			else {
				aux--;
			}
		}
		
		for(int i = 0; i <= FILA_JUG.length - 1; i++) {
			addAdvertencia(new Vector2D(xAdv, FILA_JUG[i]));
		}
	}
	
	private void oleadaEncerrada() {
		
		Ojos ojo;
		
		for(int i = 0; i <= 2; i++) {
			if(i % 2 == 0) {
				ojo = new Ojos(new Vector2D(OJO_DER + Recursos.ojos[0].getWidth(), FILA_JUG[i]),
						Recursos.ojos[0],
						1, 
						750,
						VEL_OJOS[0],
						1);
			}
			else {
				ojo = new Ojos(new Vector2D(OJO_DER, FILA_JUG[i]),
						Recursos.ojos[0],
						1, 
						750,
						VEL_OJOS[0],
						1);
			}
			addFiguraMovil(ojo);
		}
		
		for(int i = 0; i <= FILA_JUG.length - 1; i++) {
			addAdvertencia(new Vector2D(OJO_DER - 375, FILA_JUG[i]));
		}
	}


	public static void addExplosion(Vector2D posicion) {
		animaciones.add(new Animacion(
				Recursos.explosion,
				75,
				posicion.diagonal(new Vector2D(-(Recursos.explosion[0].getWidth()/2), -(Recursos.explosion[0].getHeight()/2))),
				false));
	}

	public static void addReflejo(Vector2D posicion) {
		animaciones.add(new Animacion(
				Recursos.reflejo,
				50,
				posicion,
				false));
	}
	
	public static void addAdvertencia(Vector2D posicion) {
		Animacion advertencia = new Animacion(
				Recursos.advertencia,
				200,
				posicion,
				true);
		advertencia.setNumRepeticiones(2);
		animaciones.add(advertencia);
	}

	private void addPuntuacion(Puntuacion puntuacion) {
		if(nivel == 1) {
			Puntuacion.addPuntuacionFacil(puntuacion);
		}
		else if(nivel == 2) {
			Puntuacion.addPuntuacionMedio(puntuacion);
		}
		else if(nivel == 3){
			Puntuacion.addPuntuacionDificil(puntuacion);
		}
	}


	public void actualizar() {
		
		System.out.println(numAliens);
		
		if(pausa == true) {
			return;
		}
		
		for(int i = animaciones.size() - 1; i >= 0; i--) {
			Animacion animacion = animaciones.get(i);
			animacion.actualizar();
			if(animacion.getAnimado() == false) {
				animaciones.remove(i);
				i--;
			}
		}
		
		if((numAliens == 0 || jugador.getVida() == 0) && finPartida == false) {
			finalizarPartida();	
		}

		if(cronoInicioFin.getActivo() == true && (inicioPartida == true || finPartida == true)) {
			cronoInicioFin.actualizar();
			return;
		}

		if(inicioPartida == true && cronoInicioFin.getActivo() == false) {
			inicioPartida = false;
			iniciarCronometros();
		}


		if((numAliens == 0 || jugador.getVida() == 0) && cronoInicioFin.getActivo() == false) {

			if(numAliens == 0) {
				boolean nombreValido = false;
				String nombreJugador;

				while(nombreValido == false) {

					try {
						nombreJugador = (String) JOptionPane.showInputDialog(null, "Campeón, introduce tu alias:", "FELICIDADES POR PASARTE EL JUEGO!!!", JOptionPane.QUESTION_MESSAGE);
						addPuntuacion(new Puntuacion(puntos, nombreJugador));
						nombreValido = true;
					}
					catch(IllegalArgumentException e) {
						JOptionPane.showMessageDialog(null, e.getMessage(), "MENSAJE DE ERROR", JOptionPane.ERROR_MESSAGE);
					}				
				}
			}
			Estado.cambiarEstado(new EstadoMenu());
			reiniciarValores();
		}

		for(int i = 0; i < figurasMoviles.size(); i++) {
			FiguraMovil figura = figurasMoviles.get(i);
			figura.actualizar();
			if(figura.getMuerto() == true){
				if(figura instanceof Alien) {
					Alien alien = (Alien) figura;
					puntuar(alien.getPuntuacion(), alien.getCentro());
					if(!(alien instanceof Ojos)) {
						numAliens--;
					}
				}
				figurasMoviles.remove(i);
				i--;
			}
		}

		if(cronoOjos.getActivo() == false) {
			spawnOjos();
		}

		cronoOjos.actualizar();
	}
	
	private void spawnOjos() {
		numAleatorio = aleatorio.nextInt(10); 
		if(nivel == 1) {
			if(numAleatorio <= 4) {
				oleadaFacil();
			}
			else {
				oleadaMedio();
			}
		}
		else if(nivel == 2) {
			if(numAleatorio <= 6) {
				oleadaMedio();
			}
			else if(numAleatorio <= 8) {
				oleadaEncerrada();
			}
			else {
				oleadaZigZag();
			}
		}
		else if(nivel == 3) {
			if(numAleatorio == 0) {
				oleadaFacil();
			}
			else if(numAleatorio <= 3) {
				oleadaMedio();
			}
			else if(numAleatorio <= 6) {
				oleadaEncerrada();
			}
			else {
				oleadaZigZag();
			}
		}
		cronoOjos.iniciar(RATIO_SPAWN_OJOS);
	}

	public void dibujar(Graphics g) {
		/*
		g.setColor(Color.yellow);

		g.drawLine(OJO_DER, FILA_JUG[0], OJO_IZQ, FILA_JUG[0]);
		g.drawLine(OJO_DER, FILA_JUG[1], OJO_IZQ, FILA_JUG[1]);
		g.drawLine(OJO_DER, FILA_JUG[2], OJO_IZQ, FILA_JUG[2]);

		g.setColor(Color.pink);
		g.drawLine(OJO_DER, FILA_ALIEN[0], OJO_IZQ, FILA_ALIEN[0]);
		g.drawLine(OJO_DER, FILA_ALIEN[1], OJO_IZQ, FILA_ALIEN[1]);
		g.drawLine(OJO_DER, FILA_ALIEN[2], OJO_IZQ, FILA_ALIEN[2]);
		g.drawLine(OJO_DER, FILA_ALIEN[3], OJO_IZQ, FILA_ALIEN[3]);
		g.drawLine(OJO_DER, FILA_ALIEN[4], OJO_IZQ, FILA_ALIEN[4]);

		g.drawString(".", 1000, 100);
*/	
		for(int i = 0; i < figurasMoviles.size(); i++) {
			FiguraMovil figura = figurasMoviles.get(i);
			figura.dibujar(g);
		}

		for(int i = 0; i < animaciones.size(); i++) {
			Animacion animacion = animaciones.get(i);
			if(animacion.getCurrentFrame() != null) {
				g.drawImage(animacion.getCurrentFrame(), (int)animacion.getPosicion().getX(), (int)animacion.getPosicion().getY(), null);
			}
			
		}

		dibujarPuntuacion(g);

		dibujarBarraHP(g);

		if(inicioPartida == true || finPartida == true) {
			g.setColor(new Color(0, 0, 0, 100));

			g.fillRect(0, 0, 1200, 700);
		}

		for(int i = 0; i < mensajes.size(); i++) {
			Texto txt = mensajes.get(i);
			if(txt.getTransparente() <= 0.01) {
				mensajes.remove(i);
				i--;
			}
			txt.dibujarTxtDesvanecer((Graphics2D)g);
		}
	}

	private void dibujarBarraHP(Graphics g) {
		g.drawImage(barraHP[jugador.getVida()], -5, -15, null);
	}

	private void dibujarPuntuacion(Graphics g) {

		int x = 1050;
		int y = 10;

		String cadenaPuntuacion = Integer.toString(puntos);

		for(int i = 0; i < cadenaPuntuacion.length(); i++) {

			g.drawImage(Recursos.numeros[Integer.parseInt(cadenaPuntuacion.substring(i, i+1))], x, y, null);
			x += 20; 
		}

	}

	/**
	 * Método que imprime una cadena de texto grande centrado con efecto desvanescente.
	 * 
	 * @param cadena - Cadena de texto a imprimir.
	 */
	private void imprimirTxtGrande(String cadena) {
		mensajes.add(new Texto(Color.WHITE, 
				Recursos.fuenteG, 
				new Vector2D(Ventana.ANCHO/2, Ventana.ALTO/2), 
				cadena));
	}

	public static void addFiguraMovil(FiguraMovil figura){
		figurasMoviles.add(figura);
		if(figura instanceof Alien) {
			if(figura instanceof Ojos) {
				return;
			}
			numAliens++;
		}
	}

	public static ArrayList<FiguraMovil> getFigurasMoviles(){
		return figurasMoviles;
	}
	
	private static void iniciarCronometros() {
		for(FiguraMovil figura : figurasMoviles) {
			if(figura instanceof Alien) {
				Alien alien = (Alien) figura;
				alien.iniciarCronometro();
			}
		}
	}

	private int getVelocidadRandom() {

		numAleatorio = aleatorio.nextInt(5);
		
		if(numAleatorio < 2) {
			return VEL_OJOS[0];
		}
		else if(numAleatorio < 4) {
			return VEL_OJOS[1];
		}
		return VEL_OJOS[2];
	}
}


/**
 * Armonia Absoluta:
 * alien1 = new Alien(new Vector2D(100, 50), new Vector2D(1, 0), 2, Recursos.flame[0], Recursos.flame, new Vector2D(100, 50), new Vector2D(1000, 50),  this);
		alien2 = new Alien(new Vector2D(100, 125), new Vector2D(1, 0), 3, Recursos.flame[0], Recursos.flame, new Vector2D(100, 125), new Vector2D(1000, 125),  this);
		alien3 =new Alien(new Vector2D(100, 200), new Vector2D(1, 0), 4, Recursos.flame[0], Recursos.flame, new Vector2D(100, 200), new Vector2D(1000, 200),  this);
		alien4 = new Alien(new Vector2D(100, 275), new Vector2D(1, 0), 5, Recursos.flame[0], Recursos.flame, new Vector2D(100, 275), new Vector2D(1000, 275),  this);
		alien5 = new Alien(new Vector2D(100, 370), new Vector2D(1, 0), 6, Recursos.flame[0], Recursos.flame, new Vector2D(100, 370), new Vector2D(1000, 370),  this);
		figurasMoviles.add(jugador);
		figurasMoviles.add(alien1);
		figurasMoviles.add(alien2);
		figurasMoviles.add(alien3);
		figurasMoviles.add(alien4);
		figurasMoviles.add(alien5);
 */

/**
 * Oleada Zig-Zag:
 * private void oleada3() {
		Ojos ojo1 = new Ojos(
				new Vector2D(OJO_DER, OJO_ARR),
				new Vector2D(1,1),
				4,
				Recursos.ojos[0],
				Recursos.ojos,
				this,
				1);

		Ojos ojo2 = new Ojos(
				new Vector2D(OJO_DER+Recursos.ojos[0].getWidth(), OJO_MEDIO),
				new Vector2D(1,1),
				4,
				Recursos.ojos[0],
				Recursos.ojos,
				this,
				1);

		Ojos ojo3 = new Ojos(
				new Vector2D(OJO_DER+(Recursos.ojos[0].getWidth()*2), OJO_ARR),
				new Vector2D(1,1),
				4,
				Recursos.ojos[0],
				Recursos.ojos,
				this,
				1);

		Ojos ojo4 = new Ojos(
				new Vector2D(OJO_DER+(Recursos.ojos[0].getWidth()*3), OJO_ABA),
				new Vector2D(1,1),
				4,
				Recursos.ojos[0],
				Recursos.ojos,
				this,
				1);

		Ojos ojo5 = new Ojos(
				new Vector2D(OJO_DER+(Recursos.ojos[0].getWidth()*4), OJO_MEDIO),
				new Vector2D(1,1),
				4,
				Recursos.ojos[0],
				Recursos.ojos,
				this,
				1);

		Ojos ojo6 = new Ojos(
				new Vector2D(OJO_DER+(Recursos.ojos[0].getWidth()*5), OJO_ABA),
				new Vector2D(1,1),
				4,
				Recursos.ojos[0],
				Recursos.ojos,
				this,
				1);

		figurasMoviles.add(ojo1);
		figurasMoviles.add(ojo2);
		figurasMoviles.add(ojo3);
		figurasMoviles.add(ojo4);
		figurasMoviles.add(ojo5);
		figurasMoviles.add(ojo6);
	}
 */

/**
 * Oleada con encerrona
 * private void oleada3() {
		Ojos ojo1 = new Ojos(
				new Vector2D(OJO_DER, OJO_ARR),
				new Vector2D(1,1),
				4,
				Recursos.ojos[0],
				Recursos.ojos,
				this,
				1);

		Ojos ojo2 = new Ojos(
				new Vector2D(OJO_DER+Recursos.ojos[0].getWidth(), OJO_MEDIO),
				new Vector2D(1,1),
				4,
				Recursos.ojos[0],
				Recursos.ojos,
				this,
				1);

		Ojos ojo3 = new Ojos(
				new Vector2D(OJO_DER+(Recursos.ojos[0].getWidth()*2), OJO_ABA),
				new Vector2D(1,1),
				4,
				Recursos.ojos[0],
				Recursos.ojos,
				this,
				1);

		Ojos ojo4 = new Ojos(
				new Vector2D(OJO_DER+(Recursos.ojos[0].getWidth()*2), OJO_ARR),
				new Vector2D(1,1),
				4,
				Recursos.ojos[0],
				Recursos.ojos,
				this,
				1);

		figurasMoviles.add(ojo1);
		figurasMoviles.add(ojo2);
		figurasMoviles.add(ojo3);
		figurasMoviles.add(ojo4);
	} 
 */

/**
 *  
 */
