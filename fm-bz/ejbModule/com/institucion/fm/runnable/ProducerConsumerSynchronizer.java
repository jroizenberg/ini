package com.institucion.fm.runnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
/**
 * Esta clase se encarga de sincronizar a productores y consumidores.<br>
 * Ejemplo:<br><br>
 * 
 * Tomemos la tarea tokenizar un linea desde un String<br><br>
 * 
 * 1;2;3;4;5<br>
 * hdsajk;dshajkd;jdsah<br> <br>
 * 
 * Un hilo productor toma la linea  y la desarma en sus tokens, dejando a estos en una cola. <br>
 * Un hilo consumidor toma estos tokens de la cola y realiza alguna tarea con ellos.<br>
 * 
 * Los hilos consumidores deben seguir trabajando siempre y cuando haya al menos un hilo
 * productor trabajando o haya algun elemento en la cola.<br>
 * 
 * Esta clase se encarga de avisarles a los consumidores cuando todos lo productores hayan terminado su trabajo.<br>
 * Cada vez que un productor termina de trabajar, le avisa al sincronizador. Al ser notificado el sincronizador, este le pregunta
 * al resto de los productores si ya terminaron. Si todos los productores terminaron el sincronizador les avisa a todos los consumidores
 * que los productores ya terminaron de trabajar.
 * @author Diego Krulewietki
 *
 */
public class ProducerConsumerSynchronizer {
	
	
	

	private AtomicBoolean areProducersDone=new AtomicBoolean(true);
	private List<Producer>producers=new ArrayList<Producer>();
	private List<Consumer>consumers=new ArrayList<Consumer>();
	
	/**
	 *  Cuando se agrega un consumidor puede darse el siguiente caso:<rb>
	 *  Todos los productores pueden haber terminado su tarea antes de que el se registre con <br>
	 *  el sincronizador. En este caso los consumidores no se enteraran de que los productores terminaron su<br>
	 *  tarea y caeran en un loop infinito.<br>
	 *  Para evitar este caso la variable areProducersDone se declara como variable de instancia.<br>
	 *  Si al registrarse un consumidor se da el caso de que todos los productores hayan terminado su trabajo<br>
	 *  el mismo sera inmediatamente notificado a traves del metodo <code>{@link Consumer#acknowldege()}</code>
	 *  
	 * @param consumer El consumidor
	 */
	public void addConsumer(Consumer consumer){
		if (!consumers.contains(consumer)){
			consumers.add(consumer);
//			if(areProducersDone!=null && areProducersDone.get())
//				consumer.acknowldege();
		}
	}
	
	
	public void removeConsumer(Consumer consumer){
		if (consumers.contains(consumer))
			consumers.remove(consumer);
	}
	
	
	
	public void addProducer(Producer producer){
		if (!producers.contains(producer))
			producers.add(producer);
	}
	
	public void removeProducer(Producer producer){
		if (producers.contains(producer))
			producers.remove(producer);
	}
	
	/**
	 * Cada vez que un productor, registrado con este sincronizador,
	 * termina de trabajar invoca este metodo avisandole al sincronizador
	 * que termina con su trabajo.<br>
	 * El sincronizador le pregunta al resto de los productores si ya terminaron.<br>
	 * Si todos lo productores terminaron de trabajar les avisa a todos los consumidores<br>
	 * registrados con este sincronizador.
	 */
	public void acknowldege(){
		areProducersDone.set(true);
		for (Producer p:producers){
//			if(areProducersDone==null)
//				areProducersDone=new AtomicBoolean(true);
			areProducersDone.set(areProducersDone.get() && p.isProducerDone().get());
		}
		
	
		if(areProducersDone.get()){
			for (Consumer c:consumers){
				c.acknowldege();
			}
		}
	}

}
