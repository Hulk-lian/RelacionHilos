import java.util.concurrent.atomic.AtomicInteger; 

class ElementoCompartido{
	private int contadornormal;
	private int contadorSinc;
	private AtomicInteger contadorAtomico;
	private int contadorSincRunnable;
	
	public ElementoCompartido() {
		this.contadornormal=0;
		this.contadorSinc=0;
		this.contadorSincRunnable=0;
		this.contadorAtomico= new AtomicInteger();
	}
	
	public void incrementar(){
		contadornormal++;
	}
	public synchronized void incrementarSinc(){
		contadorSinc++;
	}
	public synchronized void incrementarSincRun(){
		contadorSincRunnable++;
	}
	public void incrementarAtomico(){
		contadorAtomico.incrementAndGet();
	}
	public int getContadorN(){
		return contadornormal;
	}
	public int getContadorS(){
		return contadorSinc;
	}
	public int getContadorA(){
		return contadorAtomico.get();
	}
	public int getContadorRunS(){
		return contadorSincRunnable;
	}
}

class ThreadHiloContador extends Thread{
	ElementoCompartido elElemento;
	int cont=0;
	public ThreadHiloContador(ElementoCompartido ec) {
		this.elElemento= ec;
		
	}
	@Override
	public void run(){
		while(5000>cont++){
			elElemento.incrementar();
		}
		
	}
}
class ThreadHiloContadorSincRunnable implements Runnable{
	
	ElementoCompartido elElemento;
	Thread hilohijo;
	int cont=0;
	public ThreadHiloContadorSincRunnable(ElementoCompartido ec) {
		this.hilohijo=new Thread(this,"h1");
		this.elElemento= ec;
		hilohijo.start();
	}
	public void esperar(){
		try {
			this.hilohijo.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		while(5000>cont++){
			elElemento.incrementarSincRun();
		}

	}
}

class ThreadHiloContadorAtomico extends Thread{
	
	ElementoCompartido elElemento;
	int cont=0;
	public ThreadHiloContadorAtomico(ElementoCompartido ec) {
		this.elElemento= ec;
		
	}
	
	@Override
	public void run(){
		while(5000>cont++){
			elElemento.incrementarAtomico();
		}
		
	}
}

class ThreadHiloContadorSincronizado extends Thread{
	ElementoCompartido elElemento;
	int cont=0;
	public ThreadHiloContadorSincronizado(ElementoCompartido ec) {
		this.elElemento= ec;
	}
	
	@Override
	public void run(){
		while(5000>cont++){
			elElemento.incrementarSinc();
		}
		
	}
}


public class Simulador {

	public static void main(String[] args) {
		ElementoCompartido elElemento= new ElementoCompartido();
		double inicio=0;
		double res=0;

		//hilos normales sin trato especial
		ThreadHiloContador[] hilosContadores=new ThreadHiloContador[4];
		inicio=System.currentTimeMillis();
		for(int i=0;i<hilosContadores.length;i++){
			hilosContadores[i]= new ThreadHiloContador(elElemento);
			hilosContadores[i].start();
		}
		
		for(int i=0;i<hilosContadores.length;i++){
			try {
				hilosContadores[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		res=System.currentTimeMillis()-inicio;
		System.out.println("El resultado extendiendo de Thread sin sincronismo es: "+elElemento.getContadorN()+ "\nEn: "+res);
		
		//hilos con el syncronismo
		ThreadHiloContadorSincronizado[] hilosContadoresS=new ThreadHiloContadorSincronizado[4];
		for(int i=0;i<hilosContadoresS.length;i++){
			hilosContadoresS[i]= new ThreadHiloContadorSincronizado(elElemento);
			hilosContadoresS[i].start();
		}
		
		for(int i=0;i<hilosContadoresS.length;i++){
			try {
				hilosContadoresS[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("El resultado extendiendo de Thread y sincronizado es: "+elElemento.getContadorS()+ "\nEn: "+res);
		
		//hilo con runnable sincronizado
		ThreadHiloContadorSincRunnable[] hilosContadoresRun=new ThreadHiloContadorSincRunnable[4];
		inicio=System.currentTimeMillis();
		for(int i=0;i<hilosContadoresRun.length;i++){
			hilosContadoresRun[i]= new ThreadHiloContadorSincRunnable(elElemento);
		}
		
		for(int i=0;i<hilosContadoresRun.length;i++){
				hilosContadoresRun[i].esperar();
		}
		res=System.currentTimeMillis()-inicio;
		System.out.println("El resultado implementando Runnable y sincronizado es: "+elElemento.getContadorRunS()+ "\nEn: "+res);
		
		
		//hilos con el atomico
		ThreadHiloContadorAtomico[] hilosContadoresA=new ThreadHiloContadorAtomico[4];
		for(int i=0;i<hilosContadoresA.length;i++){
			hilosContadoresA[i]= new ThreadHiloContadorAtomico(elElemento);
			hilosContadoresA[i].start();
		}
		
		for(int i=0;i<hilosContadoresA.length;i++){
			try {
				hilosContadoresA[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("El resultado extendiendo de thread y usando Atomic es: "+elElemento.getContadorA()+ "\nEn: "+res);
		
	}

}
