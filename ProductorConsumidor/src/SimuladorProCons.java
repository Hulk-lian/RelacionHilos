import java.util.Random;

class BufferCircular{
	private int[] capacidad;
	private int PosEs;
	private int PosLe;
	private int posLLenas;
	
	public BufferCircular() {
		capacidad= new int[] {0,0,0,0};
		posLLenas=0;
	}
	
	public synchronized void escribir(int dato){
		
		while(posLLenas==capacidad.length){
			//mensaje de que se espere y blablabla
			try {
				System.out.println("Intenta escribir pero no puede esperando huecos libres");
				wait();
				
			} catch (InterruptedException e) {}
		}
		//notify y todo eso
		//incremento de la pos de llenado
		//escribir en la pos que toca el dato que toca
		capacidad[PosEs]=dato;
		PosEs=(PosEs+1)%capacidad.length;
		posLLenas++;
		notify();
	}
	public synchronized int leer(){
		int dato=0;
		while(posLLenas==0){
			try{
			System.out.println("Tratando de leer pero no se puede esperando huecos con datos");
			wait();
			}
			catch(InterruptedException e){}
		}
		dato=capacidad[PosLe];
		PosLe=(PosLe+1)%capacidad.length;
		posLLenas--;
		notify();
		
		return dato;
	}
	
}
class Productor extends Thread{
	private final int MAXVAL=30;
	private BufferCircular elBuffer;
		
	public Productor(BufferCircular buf) {
		this.elBuffer=buf;
	}
	
	@Override
	public void run(){
		try {
			for(int i=0; i<MAXVAL; i++){
					sleep((int)(Math.random()*2001));
					elBuffer.escribir(i);
			}
			
			System.out.println("Termino el productor de producir datos");
			
		} catch (InterruptedException e) {}
		
	}
}
class Consumidor extends Thread {
	private final int MAXVAL=30;
	private int posLec;
	private BufferCircular elBuffer;
	
	public Consumidor(BufferCircular elb) {
		this.elBuffer=elb;
	}
	
	@Override
	public void run(){
		
		try
		{			
			for(int i=0; i<MAXVAL; i++){
				
				sleep((int)(Math.random()*2001));
				System.out.println(elBuffer.leer());
			}
		System.out.println("Termino el lector de leer datos");		
		} catch (InterruptedException e) {}
	}
}

public class SimuladorProCons {
	public static void main(String[] args) {
		BufferCircular elBuffer=new BufferCircular();
		Productor prod=new Productor(elBuffer);
		Consumidor consu= new Consumidor(elBuffer);
		
		prod.start();
		consu.start();
		try {
			prod.join();
			consu.join();
				System.out.println("po se acabóh");
		} catch (InterruptedException e) {}				
	}
}
