import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.NClob;

import javax.swing.plaf.ActionMapUIResource;

class Registro{
	
	private int npalabras;
	private int nlineas;
	private int nCaracteres;
	
	public Registro() {
		this.nCaracteres=0;
		this.nlineas=0;
		this.npalabras=0;
	}
	
	public synchronized void actualizarregistro(int np, int nl, int nc){
		npalabras+=np;
		nlineas+=nl;
		nCaracteres+=nc;
	}	
	
	public String obtenerDatos(){
		return System.lineSeparator()+"Palabras: "+npalabras+System.lineSeparator()+"Letras: "+nCaracteres+System.lineSeparator()+"Lineas"+nlineas;
	}
}

class HiloLector extends Thread{
	
	BufferedReader lector;
	File fichero;
	Registro elRegistro;
	
	public HiloLector(String ficheroRuta,Registro elRegistro) {
		fichero = new File(ficheroRuta);
		this.elRegistro=elRegistro;
		
		try {
			lector= new BufferedReader(new FileReader(fichero));
			
		} catch (FileNotFoundException e) {e.printStackTrace();}
	}
	
	private void leer() throws IOException{
		int npalabras=0;
		int nletras=0;
		int nlineas=0;
		String lineaLeida="";	
		
		while((lineaLeida=lector.readLine())!=null){
			String[] listaPalabras=lineaLeida.split(" ");
			for(String s: listaPalabras){
				if(!s.trim().isEmpty()){
					npalabras++;
					nletras=s.length();
				}
			}
			
			nlineas++;
		}
		elRegistro.actualizarregistro(npalabras, nlineas, nletras);
	}
	
	@Override
	public void run(){
		try {
			
			leer();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}


public class SimuladorWC {

	public static void main(String[] args) {
		
		long i=0,res=0;
		if(args.length!=0){
			System.out.println("Con hilos asincronos");
			
			i=System.currentTimeMillis();
			
			try {
				
				lanzarHilos(args);
				
			} catch (InterruptedException e) {e.printStackTrace();}
			
			res=System.currentTimeMillis()-i;
			System.out.println("Tiempo "+res);
		}
		else{
			System.out.println("No hay ficheros a los que acceder");
		}

	}
	
	private static void lanzarHilos(String[] rutas) throws InterruptedException {
		
		Registro elRegistro= new Registro();
		HiloLector[] hilos= new HiloLector[rutas.length];
		
		for(int i=0; i<rutas.length;i++){
				hilos[i]= new HiloLector(rutas[i], elRegistro);
				hilos[i].start();
		}
		for(int i=0; i<rutas.length;i++){
			hilos[i].join();
		}
		System.out.println("se termino de leer el fichero"+elRegistro.obtenerDatos());	
		
	}

}
