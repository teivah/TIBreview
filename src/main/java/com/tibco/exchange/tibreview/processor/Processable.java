package com.tibco.exchange.tibreview.processor;

public interface Processable {
	public boolean process(String file, Object impl);
}
