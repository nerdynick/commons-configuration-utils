package com.nerdynick.commons.configuration.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.JSONConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class FileConfigUtils {
	public static final Map<String, Class<? extends FileBasedConfiguration>> ExtToFileConfig;
	
	static {
		final Map<String, Class<? extends FileBasedConfiguration>> _extToFileConfig = new HashMap<>();
		_extToFileConfig.put(".yaml", YAMLConfiguration.class);
		_extToFileConfig.put(".yml", YAMLConfiguration.class);
		_extToFileConfig.put(".json", JSONConfiguration.class);
		_extToFileConfig.put(".ini", INIConfiguration.class);
		_extToFileConfig.put(".properties", PropertiesConfiguration.class);
		_extToFileConfig.put(".prop", PropertiesConfiguration.class);
		_extToFileConfig.put(".props", PropertiesConfiguration.class);
		
		ExtToFileConfig = Collections.unmodifiableMap(_extToFileConfig);
	}
	
	public static CompositeConfiguration newFileConfig(String... files) throws IOException {
		final CompositeConfiguration configs = new CompositeConfiguration();
		for(String file: files) {
			configs.addConfiguration(FileConfigUtils.newFileConfig(file));
		}
		return configs;
	}
	
	public static FileBasedConfiguration newFileConfig(String file) throws IOException {
		final File filePath = Paths.get(file).toFile();
		if(filePath.isFile()) {
			return _loadFile(filePath);
		} else {
			try {
				final URL fileUrl = FileConfigUtils.class.getClassLoader().getResource(file);
				return _loadFile(Paths.get(fileUrl.toURI()).toFile());
			} catch(Exception e) {
				throw new IOException("Provided config is not a file or can't be found: "+ filePath, e);
			}
		}
	}
	
	private static FileBasedConfiguration _loadFile(File file) throws IOException {
		for(String ext: ExtToFileConfig.keySet()) {
			if(file.getName().endsWith(ext)) {
				try {
					return new Configurations()
							.fileBased(ExtToFileConfig.get(ext), file);
				} catch (ConfigurationException e) {
					throw new IOException("Failed to load provided config file: "+ file, e);
				}
			}
		}
		throw new IOException("No Config handler could be located for provided config file: "+ file);
	}
}
