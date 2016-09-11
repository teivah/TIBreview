package com.tibco.exchange.tibreview.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

public class Util {
	private static final Logger LOGGER = Logger.getLogger(Util.class);

	public static List<String> listFile(final String path, final String extension) throws IOException {
		final List<String> files = new ArrayList<>();

		Files.walk(Paths.get(path)).filter(Files::isRegularFile)
				.filter((p) -> p.toFile().getAbsolutePath().endsWith(extension)).forEach(p -> files.add(p.toString()));
		
		return files;
	}

//	private static void addToList(final Path file, final List<String> lines) {
//		lines.add
//		
//		try (Stream<String> stream = Files.lines(file, Charset.defaultCharset())) {
//			stream.map(String::trim).filter(s -> !s.isEmpty()).forEach(lines::add);
//		} catch (final IOException e) {
//			LOGGER.error("Unable to add file " + file.getFileName());
//		}
//	}
}
