package com.institucion.fm.runnable;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractCreateObject <T> implements GenericCreateObject<T>, Runnable,Producer,Consumer  {
	
	private static Log log = LogFactory.getLog(AbstractCreateObject.class);
	
	protected AtomicBoolean doneTokenizing=new AtomicBoolean(false);;
	protected AtomicBoolean doneCreatingObjects=new AtomicBoolean(false);;
	private BlockingQueue<List<String>> tokens;
	private BlockingQueue<T>createdObjects;
	private ProducerConsumerSynchronizer synchronizer;
	private CountDownLatch latch;
	private AtomicBoolean die;

	public AbstractCreateObject(AtomicBoolean doneTokenizing,
			AtomicBoolean doneCreatingObjects,
			BlockingQueue<List<String>> token,
			BlockingQueue<T> createdObjects) {
		super();
		this.doneTokenizing= doneTokenizing;
		this.doneCreatingObjects = doneCreatingObjects;
		this.tokens = token;
		this.createdObjects=createdObjects;
	}
	
	
	public AbstractCreateObject(
			BlockingQueue<List<String>> token,
			BlockingQueue<T> createdObjects,
			ProducerConsumerSynchronizer synchronizer,CountDownLatch latch,AtomicBoolean die) {
		super();
		this.tokens = token;
		this.createdObjects=createdObjects;
		this.synchronizer=synchronizer;
		this.latch=latch;
		this.die=die;
	}
	
	
	
	@Override
	public void run() {

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}
		this.getElementFromQueueToCreateObject(tokens);
		
	}
	/**
	 * toma un elmento de la bloQueueListString que se cargo en ToknFileRunnable*/
	public void  getElementFromQueueToCreateObject(BlockingQueue<List<String>> tokens){
		Integer count=0;
		while (!doneTokenizing.get() || !tokens.isEmpty()){
			if(die.get())
				return;
			List<String> tokenLineList;
			try {
				tokenLineList = tokens.poll();
				if (tokenLineList!=null){
					createdObjects.put(createObject(tokenLineList));
					count++;
					}
			} catch (Exception e) {
				log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			}
		}
		doneCreatingObjects.set(true);
		synchronizer.acknowldege();
//		System.out.println("fin hilo creador de objetos, se crearon: " +count +" objetos");
		
	}
	
	@Override
	public abstract T createObject(List<String> stringList);

}
