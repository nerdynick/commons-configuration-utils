package com.nerdynick.commons.configuration.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;

import org.apache.commons.configuration2.Configuration;

public class PrintConfigUtils {
	public static BiConsumer<String, String> print() {
		return printWithPadding(60);
	}
	public static BiConsumer<String, String> printWithFormat(String formatStr){
		return (key,value)->System.out.println(String.format(formatStr, key, value));
	}
	public static BiConsumer<String, String> printWithPadding(int padding){
		return printWithFormat("%-"+padding+"s = %s");
	}
	
	
	public static void printConfiguration(Configuration config) {
		printConfiguration(config, print());
	}
	public static void printConfiguration(Configuration config, BiConsumer<String, String> printFunc) {
		Iterator<String> keys = config.getKeys();
		while(keys.hasNext()) {
			final String key = keys.next();
			try {
				List<Object> value = config.getList(key);
				if(value.size() > 1) {
					printFunc.accept(key, value.toString());
				} else {
					printFunc.accept(key, config.getString(key));
				}
			} catch(Throwable e) {
				printFunc.accept(key, config.getString(key));
			}
		}
	}
	
	public static void printMap(Map<String, ?> config) {
		printMap(config, print());
	}
	public static void printMap(Map<String, ?> config, BiConsumer<String, String> printFunc) {
		for(Map.Entry<String, ?> e: config.entrySet()) {
			if(e.getValue() != null) {
				printFunc.accept(e.getKey(), e.getValue().toString());
			} else {
				printFunc.accept(e.getKey(), "null");
			}
		}
	}
	
	public static void printProperties(Properties config) {
		printProperties(config, print());
	}
	public static void printProperties(Properties config, BiConsumer<String, String> printFunc) {
		for(Map.Entry<?, ?> e: config.entrySet()) {
			if(e.getValue() != null) {
				printFunc.accept(e.getKey().toString(), e.getValue().toString());
			} else {
				printFunc.accept(e.getKey().toString(), "null");
			}
		}
	}
}
