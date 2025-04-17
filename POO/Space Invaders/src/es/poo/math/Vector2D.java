package es.poo.math;

/**
 * Clase que sirve para manejar todo lo relacionado con el movimiento y posicionamiento 
 * de los objetos dentro del canvas.
 */
public class Vector2D {
	
	/**
	* Vector: Posición en el espacio del sistema de coordenadas de 2 dimensiones(X e Y).
	* Dirección: Esta representa por un angulo y su magnitud.
	* Magnitud: Es una cantidad representa la longitud de un vector (distancia entre 2 puntos).
	* 
	* Otra cosa que tenemos que entender es que el sistema de coordenadas en este caso
	* su origen está en la esquina superior izquierda y verticamente está invertido.
	* 
	* Lo que quiere decir esto es que el punto (0, 0) se encuentra en la esquina superior 
	* izquierda. Cuanto más alto sea la coordenada Y más abajo en la ventana estará el punto,
	* por ende cuanto más bajo sea la coordenada Y más arriba estará en la ventana. Esto 
	* quiere decir que para subir en la ventana tendremos que reducir la coordenada Y y
	* para bajar tendremos que aumentarla.
	*/
	
	// Variables que representan las coordenadas(X e Y) del vector dentro del canvas.
	private final double x, y;
	
	
	/**
	 * Método constructor que crea inicializa un vector con las coordenadas que se le proporcionan.
	 * 
	 * @param x - Coordenada X
	 * @param y - Coordenada Y
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	
	/**
	 * Método constructor sobrecargado que crea un vector con sus coordenadas inicializadas al 
	 * origen del plano(0, 0).
	 */
	public Vector2D() {
		x = 0;
		y = 0;
	}
	
	
	/**
	 * Método que devuelve la coordenada X del vector.
	 * 
	 * @return Coordenada X del vector.
	 */
	public double getX() {
		return x;
	}
	
	
	/**
	 * Método que devuelve la coordenada Y del vector.
	 * 
	 * @return Coordenada Y del vector.
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Método que sirve para aumentar la coordenada Y del vector.
	 * Al aumentarla el punto bajará en el plano. 
	 * 
	 * @param y - Valor que se le añade a la coordenada Y.
	 */
	public Vector2D addY(double y) {
		return new Vector2D(x, this.y + y);
	}
	
	
	/**
	 * Método que sirve para disminuir la coordenada Y del vector.
	 * Al disminuirla el punto subirá en el plano. 
	 * 
	 * @param y - Valor que se le quita a la coordenada Y.
	 */
	public Vector2D subY(double y) {
		return new Vector2D(x, this.y - y);
	}
	
	
	/**
	 * Método que sirve para aumentar la coordenada X del vector.
	 * Al aumentarla el punto se deplazará a la derecha en el plano. 
	 * 
	 * @param y - Valor que se le añade a la coordenada X.
	 */
	public Vector2D addX(double x) {
		return new Vector2D(this.x + x, y);
	}
	
	
	/**
	 * Método que sirve para disminuir la coordenada X del vector.
	 * Al disminuirla el punto se desplazará a la izquierda en el plano. 
	 * 
	 * @param y - Valor que se le quita a la coordenada Y.
	 */
	public Vector2D subX(double x) {
		return new Vector2D(this.x - x, y);
	}
	
	
	/**
	 * Método que suma las coordenadas X e Y de otro vector a este vector, para que este 
	 * vector trace una diagonal. Dependiendo de las cordenadas X e Y del vector que 
	 * se pasa como parametro se hará la diagonal en una direccion u otra:<br><br>
	 * 
	 * - X(positivo) e Y(positivo): Diagonal hacia abajo a la derecha.<br>
	 * - X(positivo) e Y(negativo): Diagonal hacia arriba a la derecha.<br>
	 * - X(negativo) e Y(positivo): Diagonal hacia abajo a la izquierda.<br>
	 * - X(negativo) e Y(negativo): Diagonal hacia arriba a la izquierda.
	 *  
	 * @param v - Vector a sumar.
	 * @return	Nuevo vector con la suma de coordenadas.
	 */
	public Vector2D diagonal(Vector2D v) {
		return new Vector2D(x + v.getX(), y + v.getY());
	}
	
	
	/**
	 * Método que multiplica cada coordenada del vector por un determinado valor. Sirve
	 * para que el vector pueda trazar una curva o acelere su movimiento.
	 * 
	 * @param valorX - Valor por el que se multiplica la coordenada X. 
	 * @param valorY - Valor por el que se multiplica la coordenada Y. 
	 * @return  Nuevo vector con las coordenadas multiplicadas.
	 */
	public Vector2D multiplicar(double valorX, double valorY) {
		return new Vector2D(x * valorX, y * valorY);
	}
	
	
	/**
	 * Método para obtener la magnitud de un vector dadas sus coordenadas X e Y.
	 * 
	 * @param x - Coordenada X
	 * @param y - Coordenada Y
	 * @return Magnitud del vector.
	 */
	private double getMagnitud(double x, double y) {
		return Math.sqrt(x*x + y*y);
	}
	
	
	/**
	 * Método que calcula la distancia euclideana entre 2 vectores.
	 * 
	 * @param v	- Vector desde el cual calculamos la distancia.
	 * @return	Distancia euclideana entre 2 vectores.
	 */
	public double getDistancia(Vector2D v) {
		double distX = x - v.getX();
		double distY = y - v.getY();
		
		return getMagnitud(distX, distY);
	}
}
