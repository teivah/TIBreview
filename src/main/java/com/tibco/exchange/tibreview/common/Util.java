package com.tibco.exchange.tibreview.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
}
