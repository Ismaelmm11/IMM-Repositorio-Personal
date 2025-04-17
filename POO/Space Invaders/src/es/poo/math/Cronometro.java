package es.poo.math;

/**
 * Clase que nos va a servir para manejar los tiempos del juego, por ejemplo cada cuanto deben
 * ocurrir ciertos eventos.
 * 
 * PD: Esta clase tiene un fallo pequeño, es que al calcularse con ms y con el tiempo de la máquina,
 * pasado un tiempo de ejecución se puede ver como el movimiento de los enemigos se desincroniza.
 */
public class Cronometro {
	
	//-------------------------------------[ Variables para manejar el cronometro ] ---------------------------------------------//
	
	private long lastTime;		// Almacena el último tiempo marcado por la máquina.
	
	private long tiempo;		// Acumula el tiempo que transcurre.
	
	private int alarma;			// Es la marca de tiempo en la que el cronometro se detiene.
	
	private boolean activo;		// Booleano para saber si el cronometro está siendo usado.
	
	
	/**
	 * Método constructor que inicializa las variables que va a usar el cronometro.
	 */
	public Cronometro() {
		
		tiempo = 0;
		lastTime = 0;
		activo = false;
	}
	
	
	/**
	 * Método que establece un tiempo de alarma e inicia el cronometro.
	 * 
	 * @param alarma - Tiempo de alarma(ms).
	 */
	public void iniciar(int alarma) {
		
		this.alarma = alarma;
		
		activo = true;
		lastTime = System.currentTimeMillis();
	}
	
	
	/**
	 * Método que actualiza el estado del cronometro, haciendo que avance el tiempo y
	 * verificando si se ha alcanzado la alarma o no.
	 */
	public void actualizar() {
		
		// Si está activo vamos contando el tiempo que transcurre.
		if(activo == true) {
			tiempo += System.currentTimeMillis() - lastTime;
		}
		
		// Una vez salta la alarma desactivamos el cronometro.
		if(tiempo >= alarma) {
			activo = false;
			tiempo = 0;
		}
		
		lastTime = System.currentTimeMillis();
	}
	
	
	/**
	 * Método que devuelve el estado del cronometro.
	 * 
	 * @return True si está activado o False si está apagado.
	 */
	public boolean getActivo() {
		return activo;
	}

}
