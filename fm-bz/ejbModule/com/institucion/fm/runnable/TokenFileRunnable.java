package com.institucion.fm.runnable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TokenFileRunnable implements Runnable, TokenizeFile,Consumer,Producer{

	private static Log log2 = LogFactory.getLog(TokenFileRunnable.class);
	
	private AtomicBoolean atomicTokenFileState=new AtomicBoolean(false);;
	private AtomicBoolean atomicReadFileState=new AtomicBoolean(false);
	private BlockingQueue<List<String>> tokenblQueue;
	private BlockingQueue<String> blockingQueueString;
	private ProducerConsumerSynchronizer synchronizer;
	private Integer count=0;
	private CountDownLatch latch;
	private AtomicBoolean die;
	private StringBuffer log;
	

	public TokenFileRunnable(AtomicBoolean atomicTokenFileState, AtomicBoolean atomicReadFileState, BlockingQueue<List<String>> tokenblQueue,
			BlockingQueue<String> blockingQueueString) {
		super();
		this.atomicTokenFileState = atomicTokenFileState;
		this.atomicReadFileState = atomicReadFileState;
		this.tokenblQueue = tokenblQueue;
		this.blockingQueueString = blockingQueueString;
	
	}
	
	
	public TokenFileRunnable(AtomicBoolean atomicTokenFileState, AtomicBoolean atomicReadFileState, BlockingQueue<List<String>> tokenblQueue,
			BlockingQueue<String> blockingQueueString,ProducerConsumerSynchronizer synchronizer) {
		super();
		this.atomicTokenFileState = atomicTokenFileState;
		this.atomicReadFileState = atomicReadFileState;
		this.tokenblQueue = tokenblQueue;
		this.blockingQueueString = blockingQueueString;
		this.synchronizer=synchronizer;

	}
	
	public TokenFileRunnable(BlockingQueue<List<String>> tokenblQueue,
			BlockingQueue<String> blockingQueueString,ProducerConsumerSynchronizer synchronizer,CountDownLatch latch,AtomicBoolean die,StringBuffer log) {
		super();
		this.tokenblQueue = tokenblQueue;
		this.blockingQueueString = blockingQueueString;
		this.synchronizer=synchronizer;
		this.latch=latch;
		this.die=die;
		this.log=log;

	}
	

	@Override
	public void run() {

		tokenblQueueFile();
		
	}
	
	
	public void tokenblQueueFile (){
		String line=null;
		
		try {
			latch.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			log2.error("Mensaje: " + e1.getMessage() + "StackTrace: " + e1.getStackTrace());
		}
		while(!atomicReadFileState.get() || !blockingQueueString.isEmpty()) {
			if(die.get())
				return;
			try {
				line=blockingQueueString.poll();
				if (line!=null){
					tokenblQueue.put(tokenizeFileLine(line));
					count++;
				}
			} catch (InterruptedException e) {
				log.append("No se pudo procesar la linea: " +line);
				log.append("\n");
				log2.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			}
		}
		atomicTokenFileState.set(true);
		synchronizer.acknowldege();
//		System.out.println("hilo tokenizador terminado con  " +count + " lineas tokenizadas");
	}

	@Override
	public List<String> tokenizeFileLine(String line) {
			return Arrays.asList(line.split(";"));
	}

	
	
	@Override
	public void acknowldege() {
		if (atomicReadFileState==null)
			atomicReadFileState= new AtomicBoolean(false);
		atomicReadFileState.set(true);
	}
	
	
	@Override
	public AtomicBoolean isProducerDone() {
		if (atomicTokenFileState!=null && atomicTokenFileState.get())
			return new AtomicBoolean(true);
		else
			return new AtomicBoolean(false);
	}

}
