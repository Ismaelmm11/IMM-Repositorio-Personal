package es.poo.tablas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Puntuacion {
	
	private static final String RUTA = System.getProperty("user.dir") + File.separator + "JSON" + File.separator + "data.json";

	
	private int puntos;
	private String nombre;
	private String fecha;
	
	private static ArrayList<Puntuacion> tablaPuntuacionFacil = new ArrayList<Puntuacion>();
	private static ArrayList<Puntuacion> tablaPuntuacionMedio = new ArrayList<Puntuacion>();
	private static ArrayList<Puntuacion> tablaPuntuacionDificil = new ArrayList<Puntuacion>();
	
	public Puntuacion(int puntos, String nombre) throws IllegalArgumentException{
		
		this.puntos = puntos;
		this.nombre = nombre;
		
		checkParametros();
		
		Date fechaHoy = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		fecha = sdf.format(fechaHoy);
	}
	
	/**
	 * Constructor vacío para crear Objetos Puntuacion desde el archivo JSON.
	 */
	public Puntuacion() {};
	
	public String getFecha() {
		return fecha;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public int getPuntos() {
		return puntos;
	}
	
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}
	
	public static ArrayList<Puntuacion> getTablaPuntuacionFacil(){
		return tablaPuntuacionFacil;
	}
	
	public static ArrayList<Puntuacion> getTablaPuntuacionMedio(){
		return tablaPuntuacionMedio;
	}
	
	public static ArrayList<Puntuacion> getTablaPuntuacionDificil(){
		return tablaPuntuacionDificil;
	}
	
	public static void addPuntuacionFacil(Puntuacion puntuacion){
		addPuntuacion(puntuacion, tablaPuntuacionFacil);
	}
	
	public static void addPuntuacionMedio(Puntuacion puntuacion){
		addPuntuacion(puntuacion, tablaPuntuacionMedio);
	}
	
	public static void addPuntuacionDificil(Puntuacion puntuacion){
		addPuntuacion(puntuacion, tablaPuntuacionDificil);
	}
	
	private static void addPuntuacion(Puntuacion puntuacion, ArrayList<Puntuacion> tabla) {
		if(tabla.size() < 10) {
			tabla.add(puntuacion);
			try {
				guardar();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ordenarTablaPuntuacion(tabla);
		}
		else {
			ordenarTablaPuntuacion(tabla);
			Puntuacion puntuacionAux = tabla.get(0);
			if(puntuacion.getPuntos() > puntuacionAux.getPuntos()) {
				tabla.remove(0);
				tabla.add(puntuacion);
				try {
					guardar();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void ordenarTablaPuntuacion(ArrayList<Puntuacion> tabla){
		if(tabla.isEmpty() || tabla.size() == 1) {
			return;
		}
		QuickSort(tabla, 0, tabla.size() - 1);
	}
	
	private static void QuickSort(ArrayList<Puntuacion> puntuaciones, int izq, int der) {
		
		int i, d, p, pivote;
		
		p = seleccionarPivote(puntuaciones, izq, der);
		
		pivote = puntuaciones.get(p).getPuntos();
		i = izq;
		d = der;
		
		while(i <= d) {
			while(puntuaciones.get(i).getPuntos() < pivote) {
				i++;
			}
			while(puntuaciones.get(d).getPuntos() > pivote) {
				d--;
			}
			
			if(i <= d) {
				intercambio(puntuaciones, i, d);
				i++;
				d--;
			}
		}
		
		if(izq < d) {
			QuickSort(puntuaciones, izq, d);
		}
		if(i < der) {
			QuickSort(puntuaciones, i, der);
		}
	}
	
	private static int seleccionarPivote(ArrayList<Puntuacion> puntuaciones, int izq, int der) {
		int centro = (izq+der)/2;
		
		int elemIzq = puntuaciones.get(izq).getPuntos();
		int elemCen = puntuaciones.get(centro).getPuntos();
		int elemDer = puntuaciones.get(der).getPuntos();
		
		if((elemIzq < elemDer && elemIzq > elemCen) || (elemIzq < elemCen && elemIzq > elemDer)) {
			return izq;
		}
		if((elemCen < elemDer && elemCen > elemIzq) || (elemCen < elemIzq && elemCen > elemDer)) {
			return centro;
		}
		if((elemDer < elemIzq && elemIzq > elemCen) || (elemDer < elemCen && elemDer > elemIzq)) {
			return der;
		}

		return izq;	//En caso de empate se devuelve siempre izquierda.
	}
	
	private static void intercambio(ArrayList<Puntuacion> puntuaciones, int x, int y) {
		
		Puntuacion puntuacionAux = puntuaciones.get(x);
		puntuaciones.set(x, puntuaciones.get(y));
		puntuaciones.set(y, puntuacionAux);
	}
	
	/**
	 * Método que verifica que los parámetros de la puntuación son válidos.<br><br>
	 * <pre>
	 * - Nombre: Máximo 20 caracteres de longitud.
	 *           No debe ser null ni vacío ni solo con espacios blancos.
	 * 
	 * - Puntos: La puntuación máxima es de 999.999.999 puntos. 			
	 * </pre>
	 * 
	 * @throws IllegalArgumentException - Cuando no se cumplen los parámetros de la puntuacion.
	 */
	private void checkParametros() throws IllegalArgumentException {
		checkNombre();
		checkPuntuacion();
	}
	
	/**
	 * Método que checkea que el nombre introducido para la puntuacion sea correcto. Los criterios
	 * para saber si es correcto son que: el String no sea nulo, no sea una cadena vacía(solo espacios
	 * blancos o sin nada) o que la longitud de la cadena supere los 20 caracteres.
	 * 
	 * @throws IllegalArgumentException - En caso de que la cadena Nombre no sea correcta.
	 */
	private void checkNombre() throws IllegalArgumentException {
		if(nombre == null || nombre.isBlank())  {
			throw new IllegalArgumentException("Nombre incorrecto insertelo de nuevo por favor.");
		}
		if(nombre.length() > 20) {
			throw new IllegalArgumentException("Los nombres/apodos de las puntuaciones como mucho pueden tener"
					+ " 20 carácteres de longitud.");
		}
	}
	
	/**
	 * Método que checkea si la puntuacion es válida. El único criterio para que la puntuación sea válida
	 * es que no supere los 999.999.999 puntos, que es el máximo establecido.
	 * 
	 * @throws IllegalArgumentException - Si la puntuacion no es válida.
	 */
	private void checkPuntuacion() throws IllegalArgumentException {
		if(puntos > 999999999) {
			throw new IllegalArgumentException("La puntuacion máxima posible es 999.999.999 puntos.");
		}
	}
	
	public static void leer() throws FileNotFoundException{
		
		tablaPuntuacionFacil.clear();
		tablaPuntuacionMedio.clear();
		tablaPuntuacionDificil.clear();
		
		File archiu = new File(RUTA);
		
		if(!archiu.exists() || archiu.length() == 0) {
			return;
		}
		
		JSONObject listaJSON = new JSONObject(new JSONTokener(new FileInputStream(archiu)));

	    leerPuntuaciones(listaJSON, "lista1", tablaPuntuacionFacil);
	    leerPuntuaciones(listaJSON, "lista2", tablaPuntuacionMedio);
	    leerPuntuaciones(listaJSON, "lista3", tablaPuntuacionDificil);
		
	}
	
	private static void leerPuntuaciones(JSONObject listaJSON, String clave, ArrayList<Puntuacion> tabla) {
	    JSONArray lista = listaJSON.getJSONArray(clave);
	    for (int i = 0; i < lista.length(); i++) {
	    	JSONObject objeto = (JSONObject) lista.get(i);
			Puntuacion puntuacion = new Puntuacion();
			puntuacion.setPuntos(objeto.getInt("puntos"));
			puntuacion.setNombre(objeto.getString("nombre"));
			puntuacion.setFecha(objeto.getString("fecha"));
			addPuntuacion(puntuacion, tabla);
	    }
	}
	
	
	private static void guardar() throws IOException {
		File archiu = new File(RUTA);
		
		archiu.getParentFile().mkdirs();
		archiu.createNewFile();
		
		JSONObject listaJSON = new JSONObject();
		
		listaJSON.put("lista1", convertirListaAJSON(tablaPuntuacionFacil));
	    listaJSON.put("lista2", convertirListaAJSON(tablaPuntuacionMedio));
	    listaJSON.put("lista3", convertirListaAJSON(tablaPuntuacionDificil));
		
		BufferedWriter writer = Files.newBufferedWriter(Paths.get(archiu.toURI()));
		listaJSON.write(writer);
		writer.close();
	}
	
	private static JSONArray convertirListaAJSON(ArrayList<Puntuacion> lista) {
	    JSONArray jsonArray = new JSONArray();
	    for (Puntuacion puntuacion : lista) {
	        JSONObject objeto = new JSONObject();
	        objeto.put("puntos", puntuacion.getPuntos());
	        objeto.put("nombre", puntuacion.getNombre());
	        objeto.put("fecha", puntuacion.getFecha());
	        jsonArray.put(objeto);
	    }
	    return jsonArray;
	}
}
