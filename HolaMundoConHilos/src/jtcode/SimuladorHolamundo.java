package jtcode;

class Saludador implements Runnable{

	Thread hilohijo;
	int numerohilo;
	
	public Saludador(int n) {
		this.hilohijo= new Thread(this,"hilo numero "+n );
		this.numerohilo=n;
		hilohijo.start();
	
	}
	private void dormir(){
		try {
			Thread.sleep(numerohilo*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void esperar(){
		try {
			hilohijo.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		System.out.println(hilohijo.getName()+" espera "+numerohilo);
		dormir();
		System.out.println("El "+hilohijo.getName()+" te saluda \n Hola caracola");
	}
}
class Saludador2 extends Thread{
	String nombre;
	int numerohilo;
	
	public Saludador2(int n) {
		nombre="hilo "+n;
		this.numerohilo=n;
	}
	private void dormir(){
		try {
			Thread.sleep(numerohilo*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		System.out.println(nombre+" espera "+numerohilo);
		dormir();
		System.out.println("El "+nombre+" te saluda \n Hola caracola");
	}
}

public class SimuladorHolamundo {
	public static void main(String[] args) throws InterruptedException {
		
		double inicio=0,fin=0;
		Saludador[] saludosRun= new Saludador[6];
		System.out.println("Implementando Runnable");
		
		inicio=System.currentTimeMillis();
		for(int i=0;i<6;i++){
			saludosRun[i]= new Saludador(i+1);
			saludosRun[i].esperar();
		}
		fin= System.currentTimeMillis();
		System.out.println(fin-inicio);
		
	System.out.println("############################################################################");
		
	System.out.println("Heredando de Thread");
	
		inicio=System.currentTimeMillis();
		Saludador2[] salud= new Saludador2[6];
		for(int i=0;i<6;i++){
			salud[i]=new Saludador2(i+1);
			salud[i].start();
			salud[i].join();
		}
		fin= System.currentTimeMillis();
		System.out.println(fin-inicio);
	}
}
