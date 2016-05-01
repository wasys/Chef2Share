package com.android.utils.lib.utils;

import android.util.Log;

public class Memoria {

	private static final String TAG = "memoria";
	private static long free;
	private static long time;

	public static void start() {
		time = System.currentTimeMillis();
		free = Runtime.getRuntime().freeMemory();
		Log.i(TAG, "Memoria.start - free: " + free);
	}

	public static void start(String desc) {
		time = System.currentTimeMillis();
		free = Runtime.getRuntime().freeMemory();
		Log.i(TAG, "Memoria.start["+ desc + "] - free: " + free);
	}

	public static void end() {
		long t2 = System.currentTimeMillis();
		long mem2 = Runtime.getRuntime().freeMemory();
		long tempo = t2 - time;
		long memUsada = mem2 - free;
		Log.i(TAG, "Memoria.end ("+tempo+" ms) - free: " + free + ", usada: " + memUsada);
	}

	public static void end(String desc) {
		long t2 = System.currentTimeMillis();
		long mem2 = Runtime.getRuntime().freeMemory();
		long tempo = t2 - time;
		long memUsada = mem2 - free;
		Log.i(TAG, "Memoria.end["+ desc + "] / ("+tempo+" ms) - free: "+ free + ", usada: " + memUsada);
	}
}
