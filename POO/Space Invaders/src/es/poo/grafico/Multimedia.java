package es.poo.grafico;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Clase con el único proposito de cargar recursos multimedia.
 */
public class Multimedia {
	
	
	/**
     * Carga una imagen desde un archivo especificado por la ruta.
     * 
     * @param ruta La ruta del archivo de imagen.
     * @return La imagen cargada como BufferedImage, o null si ocurre un error.
     */
	public static BufferedImage CargarImagen(String ruta) {
		
		try {
			return ImageIO.read(Multimedia.class.getResourceAsStream(ruta));
		} catch (IOException e) {
			e.printStackTrace();	//Imprimir el error en caso de que surja.
		}
		
		return null;	
	}
	
	
	/**
     * Carga una fuente desde un archivo especificado por la ruta y ajusta su tamaño.
     * 
     * @param ruta La ruta del archivo de fuente.
     * @param tamano El tamaño deseado para la fuente.
     * @return La fuente cargada como Font, o null si ocurre un error.
     */
	public static Font CargarFuente(String ruta, int tamano) {
		
		try {
			return Font.createFont(Font.TRUETYPE_FONT, Multimedia.class.getResourceAsStream(ruta)).deriveFont(Font.PLAIN, tamano);
		}
		catch(FontFormatException | IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
     * Carga un sonido desde un archivo especificado por la ruta.
     * 
     * @param ruta La ruta del archivo de sonido.
     * @return El sonido cargado como Clip, o null si ocurre un error.
     */
	public static Clip CargarSonido(String ruta) {
				
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(Multimedia.class.getResource(ruta)));
			
			return clip;
		}
		catch(LineUnavailableException e) {
			e.printStackTrace();
		}
		catch(UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
}


