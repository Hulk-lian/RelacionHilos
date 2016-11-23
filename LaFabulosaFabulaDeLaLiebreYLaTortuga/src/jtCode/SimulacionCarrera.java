package jtCode;
/*probabilidades: 
Animal		Suceso 		Probabilidad 	 Movimiento
-------------------------------------------------------------
Tortuga	 | Avance rápido	|	50%	|	 +3 
	 |	Resbaló		|	20%	|	 -6 
	 |	Avance lento 	| 	30% 	|	 +1 
Liebre 	 |	Duerme		|	20%	|	  0
	 |	Gran salto	|	20%	|	 +9 
	 |	Resbalón grande	|	10% 	|	 -12 
	 |	Pequeño salto	|	30%	|	 +1 
	 |	Resbalón pequeño|	20%	|	 -2 
		
Si ambos llegan a la vez declare un empate*/
//Autor: Julián Troyano Domínguez
//version 1.0

import java.util.Random;

class Carrera {
	int duraccion;//duracion de la carrera
	int posTortuga;//posicion de la tortuga en la carrera
	int posLiebre;//posicion de la liebre en la carrera
	
	//dos random para que sea aun más aleatorio el movimiento individual
	Random randomTortuga;//random para las probabilidades del movimiento de la tortuga
	Random randomLiebre;//random para las probabilidades del movimiento de la liebre
	boolean hayGanador;// booleano para que al tener un ganador el arbitro deje de actualizar el estado de la carrera

	//las posibles opciones que puede tomar el movimiento
	int rndT=0;//almacen del random de la tortuga
	int rndL=0;//almacen del random de la liebre 
	
	public Carrera(int duraccion) {
		hayGanador=false;
		this.duraccion=duraccion;
		posLiebre=0;
		posTortuga=0;
		randomLiebre= new Random();
		randomTortuga= new Random();
	}
		
	public void movimientoTortuga(){
		
		rndT=(int)(randomTortuga.nextDouble() * 100 + 1);
		int avance=0;
		
		if(rndT<=50){//avance rapido
			avance=3;
		}else if(rndT>50 && rndT<=70){//resbala
			avance=-6;
		}else{//avance normal en una tortuga
			avance=1;
		}
		avanceT(avance);
	}
	
	public void movimientoLiebre(){
		
		rndL=(int)(randomLiebre.nextDouble() * 100 + 1);
		int avance=0;
		if(rndL<=20){//duerme
			avance=0;
		}else if(rndL>20 && rndL<=40){//salto grande
			avance=9;
		}else if(rndL>40 && rndL<=50){//resbalon grande
			avance=-12;
		}else if(rndL>50 && rndL<=80){//salto pequeño
				avance=+1;
		}else {//resbalon pequeño
			avance=-2;
		}
		avanceL(avance);
	}
	
	private void avanceT(int n){
		if(posTortuga+n<0){
			posTortuga=0;
		}else{
			posTortuga+=n;
		}
	}
		
	private void avanceL(int n){
		if(posLiebre+n<0){	
			posLiebre=0;
		}else{
			posLiebre+=n;
		}
	}
	
	public void mostrarEstadoCarrera(){
		System.out.println("Posición liebre: "+posLiebre);
		System.out.println("Posicion tortuga: "+posTortuga);
		for (int i = 0; i < duraccion; i++) {
			if(i==posLiebre && i==posTortuga){
				System.out.print("A");
			}
			else if(i==posTortuga){
				System.out.print("T");
			}
			else if(i==posLiebre){
				System.out.print("L");
			}else{
				System.out.print(" ");
			}
		}
		//separador de las carreras
		System.out.println("\n"+
		"___________________________________________________________________________________________");
		
		
		//en caso de empate
		if (posLiebre>=70 && posTortuga>=70) {
			hayGanador=true;
			System.err.println("Empate, hay que recurrir a la foto finish para asegurar");
			
		}else if(posLiebre>=70){//gana liebre
			hayGanador=true;
			System.err.println("ha ganado la Liebre");
		}
		else if(posTortuga>=70) {//gana tortuga
			hayGanador=true;
			System.err.println("ha ganado la Tortuga");
		}
	}

}
class Arbitro extends Thread{
	Carrera laCarreraDeLaLiebreYlaTortugaTortugosa;
	
	public Arbitro(Carrera carrera) {
		this.laCarreraDeLaLiebreYlaTortugaTortugosa=carrera;
	}
	@Override
	public void run(){
		while(!laCarreraDeLaLiebreYlaTortugaTortugosa.hayGanador){
			try {
				
				sleep(999);//999 para que de el primer avance que es los 2 en la salida
				laCarreraDeLaLiebreYlaTortugaTortugosa.mostrarEstadoCarrera();
				
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
}
class Liebre extends Thread{
	Carrera carrera;
	public Liebre(Carrera carrera) {
		this.carrera=carrera;
	}
	
	@Override
	public void run(){
		try {
			while(!carrera.hayGanador){
				sleep(1000);
				carrera.movimientoLiebre();
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
class Tortuga extends Thread{
	Carrera carrera;
	
	public Tortuga(Carrera carrera) {
		this.carrera=carrera;
	}
	@Override
	public void run(){
		try {
			while(!carrera.hayGanador){
				sleep(1000);
				carrera.movimientoTortuga();
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

public class SimulacionCarrera {
	public static void main(String[] args) {
		int duraccion=70;
		Carrera laCarrera=new Carrera(duraccion);
		new Arbitro(laCarrera).start();
		new Liebre(laCarrera).start();
		new Tortuga(laCarrera).start();
	}
}
