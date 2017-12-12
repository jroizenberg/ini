package com.institucion.fm.runnable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReadFileRunnable implements Runnable, ReadFile,Producer {

	private static Log log2 = LogFactory.getLog(ReadFileRunnable.class);
	
	private File file;
	private AtomicBoolean atomicReadFileState; 
	private BlockingQueue<String>lines;
	private ProducerConsumerSynchronizer synchronizer;
	private CountDownLatch latch;
	private AtomicBoolean die;
	private StringBuffer log;

	public ReadFileRunnable(File file) {
		super();
		this.file = file;
	}


	
	public ReadFileRunnable(File file, BlockingQueue<String>lines,ProducerConsumerSynchronizer syncrhonizer,CountDownLatch latch,AtomicBoolean die,StringBuffer log) {
		super();
		this.file = file;
		this.lines=lines;
		this.synchronizer=syncrhonizer;
		this.latch=latch;
		this.die=die;
		this.log=log;
	}
	

//	public ReadFileRunnable(File file,AtomicBoolean atomicReadFileState, BlockingQueue<String>lines) {
//		super();
//		this.file = file;
//		this.atomicReadFileState = atomicReadFileState;
//		this.lines=lines;
//	}
//
//	
//	public ReadFileRunnable(File file,AtomicBoolean atomicReadFileState, BlockingQueue<String>lines,ProducerConsumerSynchronizer syncrhonizer) {
//		super();
//		this.file = file;
//		this.atomicReadFileState = atomicReadFileState;
//		this.lines=lines;
//		this.synchronizer=syncrhonizer;
//	}

	
	@Override
	public void run() {
		atomicReadFileState=new AtomicBoolean(false);
			this.readFile();
		
		
	}

	@Override
	public void readFile() {
		try {
			latch.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			log2.error("Mensaje: " + e1.getMessage() + "StackTrace: " + e1.getStackTrace());
		}
		Integer i=0;
		try {
			BufferedReader bufReader = new BufferedReader(new FileReader(file));
			String lineInFile;
			bufReader.readLine();
			lineInFile=bufReader.readLine();
			while (lineInFile!= null){
				try {
					i++;
					lines.put(lineInFile.replace("\"", ""));
					lineInFile=bufReader.readLine();	
				} catch (InterruptedException e) {
					e.printStackTrace();
					log2.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
				}	
			}
			atomicReadFileState.set(true); 
			synchronizer.acknowldege();
//			System.out.println("hilo lector terminado");
			bufReader.close();
			log.append("se leyeron: "+ i +" lineas del archivo de prescripciones");
		} catch (FileNotFoundException e) {
			log.append("No se pudo encontrar el archivo de prescripciones. El proceso abortara");
			log.append("\n");
			log2.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			die.set(true);
		} catch (IOException e) {
			log.append("No se puede procesar el archivo de prescripciones. El proceso abortara");
			log2.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			die.set(true);
		}

	}
	
	
	@Override
	public AtomicBoolean isProducerDone() {
		if(atomicReadFileState.get())
			return new AtomicBoolean(true);
		else
			return new AtomicBoolean(false);
	}

}
