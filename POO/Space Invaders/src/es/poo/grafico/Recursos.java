package es.poo.grafico;

import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.sound.sampled.Clip;

/**
 * Clase que actua como contenedor de los distintos elementos multimedia.
 */
public class Recursos {
	
	//--------------------------[ Variables con los recursos que usa el juego ] -------------------------//
	
	public static boolean carga = false;			// Booleano que establece si los recursos han cargado o no.
	
	public static float conteoCarga = 0;			// Numero de recursos cargados.
	
	public static final float NUM_RECURSOS = 100;	// Constante con el numero de recursos a cargar.
	
	// A continuacion están los diferentes recursos que usa el juego(imagenes, sonidos, fuentes...)
	
	public static BufferedImage jugador;
	
	public static BufferedImage[] fuegoNave = new BufferedImage[4];
	
	public static BufferedImage[] ojos = new BufferedImage[8];
	
	public static BufferedImage[] disparo = new BufferedImage[6];
	
	public static BufferedImage[] disparoEvil = new BufferedImage[6];
	
	public static BufferedImage[] disparoCalabaza = new BufferedImage[4];
	
	public static BufferedImage[] disparoFuego = new BufferedImage[4];
	
	public static BufferedImage[] disparoGota = new BufferedImage[4];
	
	public static BufferedImage[] puas1 = new BufferedImage[4];
	public static BufferedImage[] puas2 = new BufferedImage[4];
	public static BufferedImage[] puas3 = new BufferedImage[4];
	public static BufferedImage[] puas4 = new BufferedImage[4];
	public static BufferedImage[] puas5 = new BufferedImage[4];
	public static BufferedImage[] puas6 = new BufferedImage[4];
	
	public static BufferedImage[] fondoMenu = new BufferedImage[12];
	
	public static BufferedImage[] explosion = new BufferedImage[8];
	
	public static BufferedImage[] reflejo = new BufferedImage[7];
	
	public static BufferedImage[] flame = new BufferedImage[4];
	
	public static BufferedImage[] pulpo = new BufferedImage[4];
	
	public static BufferedImage[] fantasma = new BufferedImage[4];
	
	public static BufferedImage[] fantasmaDespierto = new BufferedImage[4];
	
	public static BufferedImage[] fantasmaTransparente = new BufferedImage[4];
	
	public static BufferedImage[] calabaza = new BufferedImage[4];
	
	public static BufferedImage[] calabazaActiva = new BufferedImage[4];
	
	public static BufferedImage[] bigSlime = new BufferedImage[4];
	
	public static BufferedImage[] midSlime = new BufferedImage[4];
	
	public static BufferedImage[] littleSlime = new BufferedImage[4];
	
	public static BufferedImage[] slimeAcorazado = new BufferedImage[8];
	
	public static BufferedImage[] transformacionSlime = new BufferedImage[16];
	
	public static BufferedImage[] numeros = new BufferedImage[10];
	
	public static BufferedImage botonGris;
	
	public static BufferedImage botonBlue;
	
	public static BufferedImage[] advertencia = new BufferedImage[2];
	
	public static BufferedImage logo;
	
	public static BufferedImage[] barraHP = new BufferedImage[6];
	
	public static Font fuenteG, fuenteM, fuenteP;
	
	public static Clip sonidoDisparo;
	
	public static Clip sonidoExplosion;
	
	public static Clip sonidoWin;
	
	public static Clip sonidoLose;
	
	public static Sonido sonidoExpl;

	public static void inicializar(){

		jugador = CargarImagen("/naves/space_ship.png");

		botonGris = CargarImagen("/interfaz/BTN_GRIS.png");
		botonBlue = CargarImagen("/interfaz/BTN_BLUE.png");
		logo = CargarImagen("/interfaz/LOGO_NEON.png");
		
		advertencia[0] = CargarImagen("/efectos/ADVERTENCIA.png");
		advertencia[1] = null;

		for(int i = 0; i < barraHP.length; i++) {
			barraHP[i] = CargarImagen("/vida/BARRAHP_000"+(i + 1)+".png");
		}

		for(int i = 0; i < ojos.length; i++) {
			ojos[i] = CargarImagen("/meteoros/METEORO_000" + (i + 1) + ".png");
		}

		for(int i = 0; i < disparo.length; i++) {
			disparo[i] = CargarImagen("/disparos/DISPARO_LASER_000"+ (i + 1) +".png");
			disparoEvil[i] = CargarImagen("/disparos/DISPARO_EVIL_000"+ (i + 1) +".png");
		}

		for(int i = 0; i < flame.length; i++) {
			
			fuegoNave[i] = CargarImagen("/efectos/FUEGO_NAVE_000" + (i + 1) + ".png");
			
			flame[i] = CargarImagen("/aliens/FIRE_000" + (i + 1) + ".png");
			fantasma[i] = CargarImagen("/aliens/FANTASMA_000" + (i + 1) + ".png");
			fantasmaDespierto[i] = CargarImagen("/aliens/FANTASMA_DESPIERTO_000" + (i + 1) + ".png");
			fantasmaTransparente[i] = CargarImagen("/aliens/FANTASMA_TRANSPARENTE_000" + (i + 1) + ".png");
			pulpo[i] = CargarImagen("/aliens/PULPO_000" + (i + 1) + ".png");
			
			disparoCalabaza[i] = CargarImagen("/disparos/DISPARO_CALABAZA_000" + (i + 1) +".png");
			disparoFuego[i] = CargarImagen("/disparos/DISPARO_FUEGO_000" + (i + 1) +".png");
			disparoGota[i] = CargarImagen("/disparos/DISPARO_GOTA_000" + (i + 1) +".png");
			
			
			puas1[i] = CargarImagen("/puas/PUAS_1_000" + (i + 1) +".png");
			puas2[i] = CargarImagen("/puas/PUAS_2_000" + (i + 1) +".png");
			puas3[i] = CargarImagen("/puas/PUAS_3_000" + (i + 1) +".png");
			puas4[i] = CargarImagen("/puas/PUAS_4_000" + (i + 1) +".png");
			puas5[i] = CargarImagen("/puas/PUAS_5_000" + (i + 1) +".png");
			puas6[i] = CargarImagen("/puas/PUAS_6_000" + (i + 1) +".png");
			
			calabaza[i] = CargarImagen("/aliens/CALABAZA_000" + (i + 1) + ".png");
			calabazaActiva[i] = CargarImagen("/aliens/CALABAZA_ACTIVADA_000" + (i + 1) + ".png");
			
			bigSlime[i] = CargarImagen("/aliens/BIG_SLIME_000" + (i + 1) + ".png");
			midSlime[i] = CargarImagen("/aliens/MID_SLIME_000" + (i + 1) + ".png");
			littleSlime[i] = CargarImagen("/aliens/LITTLE_SLIME_000" + (i + 1) + ".png");
		}
		
		for(int i = 0; i < reflejo.length; i++) {
			reflejo[i] = CargarImagen("/animacion/REFLEJO_000" + (i + 1) + ".png");
		}
		

		for(int i = 0; i < explosion.length; i++) {
			explosion[i] = CargarImagen("/explosiones/EXPLOSION_000"+ (i + 1) +".png");
			
			slimeAcorazado[i] = CargarImagen("/aliens/SLIME_ACORAZADO_000" + (i + 1) + ".png");
		}

		for(int i = 0; i < numeros.length; i++) {
			numeros[i] = CargarImagen("/numeros/" + i +".png"); 
		}
		
		for(int i = 0; i < transformacionSlime.length; i++) {
			if(i < 9) {
				transformacionSlime[i] = CargarImagen("/animacion/SLIME_TRANSICION_000" + (i + 1) +".png");
			}
			else {
				transformacionSlime[i] = CargarImagen("/animacion/SLIME_TRANSICION_00" + (i + 1) +".png");
			}
		}
		
		for(int i = 0; i < fondoMenu.length; i++) {
			if(i < 9) {
				fondoMenu[i] = CargarImagen("/interfaz/FONDO_MENU_000" + (i + 1) +".gif");
			}
			else {
				fondoMenu[i] = CargarImagen("/interfaz/FONDO_MENU_00" + (i + 1) +".gif");
			}
		}

		fuenteG = CargarFuente("/fuente/Pixel NES.otf", 62);
		fuenteM = CargarFuente("/fuente/Pixel NES.otf", 20);
		fuenteP = CargarFuente("/fuente/Pixel NES.otf", 18);

		sonidoDisparo = CargarSonido("/sonido/DISPARO_LASER.wav");
		sonidoExplosion = CargarSonido("/sonido/EXPLOSION.wav");

		sonidoWin = CargarSonido("/sonido/YOU_WIN.wav");
		sonidoLose = CargarSonido("/sonido/YOU_LOSE.wav");		


		sonidoExpl = new Sonido(sonidoExplosion);


		carga = true;
	}
	
	
	/**
	 * Método que carga una imagen a partir de una ruta.
	 * 
	 * @param ruta - Es el path a la imagen.
	 * @return Imagen que representará un objeto del juego. 
	 */
	private static BufferedImage CargarImagen(String ruta) {
		conteoCarga++;
		return Multimedia.CargarImagen(ruta);
	}
	
	
	/**
	 * Método que carga una fuente con un tamaño determinado a partir de una ruta.
	 * 
	 * @param ruta		- Es el path a la fuente.
	 * @param tamano	- Tamaño de la fuente.
	 * @return Fuente que se usará para imprimir texto.
	 */
	private static Font CargarFuente(String ruta, int tamano) {
		conteoCarga++;
		return Multimedia.CargarFuente(ruta, tamano);
	}
	
	
	/**
	 * Método que carga un sonido a partir de una ruta.
	 * 
	 * @param ruta - Es el path al sonido.
	 * @return	Sonido que se podrá hacer escuchar en el juego.
	 */
	private static Clip CargarSonido(String ruta) {
		conteoCarga++;
		return Multimedia.CargarSonido(ruta);
	}
	
}
