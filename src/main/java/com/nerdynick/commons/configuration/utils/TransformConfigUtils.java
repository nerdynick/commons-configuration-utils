package com.nerdynick.commons.configuration.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.Configuration;

public class TransformConfigUtils {
	public static Map<String, Object> toMap(Configuration config){
		final Map<String, Object> mapConfig = new HashMap<>();
		
		final Iterator<String> configKeys = config.getKeys();
		while(configKeys.hasNext()) {
			final String key = configKeys.next();
			mapConfig.put(key, config.getProperty(key));
		}
		
		return mapConfig;
	}
	
	/**
	 * Reads all properties starting with {prefix} from {configToParse}. 
	 * Properties that match prefix will have the prefix removed, key lowercased, and all `_` replaced with `.`
	 * The general idea is to support Docker Containers.
	 * 
	 * @param configToParse
	 * @param prefix
	 * @return
	 */
	public static Configuration envToProp(final Configuration configToParse, final String prefix) {
		final Iterator<String> envIter = configToParse.getKeys();
		final Configuration config = new BaseConfiguration();
		while(envIter.hasNext()) {
			final String key = envIter.next();
			if(key.startsWith(prefix)) {
				final String prop = key.substring(prefix.length()).toLowerCase().replaceAll("_", ".");
				config.addProperty(prop, configToParse.getProperty(key));
			}
		}
		
		return config;
	}
}
