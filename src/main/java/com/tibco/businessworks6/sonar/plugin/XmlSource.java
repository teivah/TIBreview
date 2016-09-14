package com.tibco.businessworks6.sonar.plugin;

import java.io.File;
import java.nio.charset.Charset;

/*
 * SonarQube XML Plugin
 * Copyright (C) 2010 SonarSource
 * dev@sonar.codehaus.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.w3c.dom.Document;

import com.tibco.exchange.tibreview.model.pmd.Violation;
import com.tibco.exchange.tibreview.model.rules.Rule;

/**
 * A object representing a XML Source code document
 * on which validation rules ({@link Rule}) can be executed and 
 * violations ({@link Violation}) can be reported.
 * 
 * It uses SAX parser to load the XML {@link Document} in memory
 * based on a {@link File} or a simple {@link String}.
 * 
 * It provide a set of method to control validity of XML source
 * code fragments.
 * 
 * @author Kapil Shivarkar
 */
public class XmlSource extends AbstractSource {
	private XmlFile xmlFile;
	
	private Document documentNamespaceAware = null;
	private Document documentNamespaceUnaware = null;

	/**
	 * @param file
	 */
	public XmlSource(File file) {
		super();
		this.xmlFile = new XmlFile(file);
	}
	
	/**
	 * @param xmlFile
	 */
	public XmlSource(XmlFile xmlFile) {
		super();
		this.xmlFile = xmlFile;
	}
	
	@Override
	public boolean parseSource(Charset charset) {
		xmlFile.checkForCharactersBeforeProlog(charset);

		documentNamespaceUnaware = parseFile(false);
		if (documentNamespaceUnaware != null) {
			documentNamespaceAware = parseFile(true);
		}
		return documentNamespaceUnaware != null
				|| documentNamespaceAware != null;
	}

	/**
	 * @param namespaceAware
	 * @return
	 */
	private Document parseFile(boolean namespaceAware) {
		//return new SaxParser().parseDocument(createInputStream(), namespaceAware);
		return new SaxParser().parseDocument(xmlFile.getIOFile(), namespaceAware);
	}
}
