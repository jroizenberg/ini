package com.institucion.fm.utils;

public class MemoryUtil {

	private static final long  MEGABYTE = 1024L * 1024L;
	static	Runtime rt = Runtime.getRuntime();
	static 	int 	toKb = 1024;
	static	long 	memoriaAntes = 0; 
	static	long 	memoriaDespues = 0; 
	static	long 	memoriaConsumida = 0;

	public static void memoriaAntes() {
		System.gc();
		memoriaAntes = rt.freeMemory();
//		System.out.println("Memoria Antes: " + memoriaAntes / MEGABYTE + "Mb");
	}
	
	public static void memoriaDespues() {
		memoriaDespues = rt.freeMemory();
//		System.out.println("Memoria Despues: " + memoriaDespues / MEGABYTE + "Mb");
		memoriaConsumida = memoriaAntes - memoriaDespues;
//		System.out.println("Memoria Consumida: " + memoriaConsumida / MEGABYTE + "Mb");

	}

}
