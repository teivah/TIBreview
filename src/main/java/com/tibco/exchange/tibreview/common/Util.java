package com.tibco.exchange.tibreview.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class Util {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(Util.class);

	public static List<String> listFile(final String path, final String extension) throws IOException {
		if(!new File(path).isDirectory()) {
			throw new IllegalArgumentException("Path " + path + " is not a directory");
		}
		
		final List<String> files = new ArrayList<>();

		Files.walk(Paths.get(path)).filter(Files::isRegularFile)
				.filter((p) -> p.toFile().getAbsolutePath().endsWith(extension)).forEach(p -> files.add(p.toString()));

		return files;
	}
	
	public static String mapToString(Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("[");
		
		if(map != null || map.size() > 0) {
			int i = 0;
			for (Map.Entry<String, String> entry : map.entrySet()) {
				if(i + 1 == map.size()) {
					sb.append(entry.getKey()).append("=").append(entry.getValue());
				} else {
					sb.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
				}
				i++;
			}
		}
		
		sb.append("]");
		
		return sb.toString();
	}
}
