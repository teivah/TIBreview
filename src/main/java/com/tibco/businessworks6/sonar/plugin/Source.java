package com.tibco.businessworks6.sonar.plugin;

import java.nio.charset.Charset;

public interface Source {
	public boolean parseSource(Charset charset);
}