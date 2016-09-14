package com.tibco.businessworks6.sonar.plugin;

import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public abstract class ProcessNode {

	protected String type;
	protected String name;
	protected Node node;
	protected String expression;
	protected String sqlStatement;
	protected boolean jdbcTimeout;
	protected boolean jdbcMaxRows;
	public boolean isJdbcTimeout() {
		return jdbcTimeout;
	}

	public void setJdbcTimeout(boolean jdbcTimeout) {
		this.jdbcTimeout = jdbcTimeout;
	}

	public boolean isJdbcMaxRows() {
		return jdbcMaxRows;
	}

	public void setJdbcMaxRows(boolean jdbcMaxRows) {
		this.jdbcMaxRows = jdbcMaxRows;
	}

	
	
	public String getSqlStatement() {
		return sqlStatement;
	}

	public void setSqlStatement(String sqlStatement) {
		this.sqlStatement = sqlStatement;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public void parseActivityConfiguration(HashMap<String, Transition> transitions, Map<String, String> synonymsGroupMapping) {
		if (node != null) {
			NamedNodeMap attributes = node.getAttributes();
			if (attributes != null) {
				Node nameAttribute = attributes.getNamedItem("name");
				Node type = attributes.getNamedItem("type");
				Node expressionAttribure = attributes.getNamedItem("expression");
				if(type != null)
					setType(type.getTextContent());
				if (nameAttribute != null) 
					setName(nameAttribute.getTextContent());
				if(expressionAttribure != null )
					setExpression(expressionAttribure.getTextContent());
			}
			NodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				if(children.item(i).getNodeName().equals("tibex:config")){
					Node bwactivity_config = children.item(i).getChildNodes().item(1);
					setType(bwactivity_config.getAttributes().getNamedItem("activityTypeID").getTextContent());
					if(bwactivity_config.getAttributes().getNamedItem("activityTypeID").getTextContent().contains("bw.jdbc.")){
						NodeList nodes = bwactivity_config.getChildNodes();
						for (int j = 0; j < nodes.getLength(); j++) {
							if(nodes.item(j) != null && nodes.item(j).getNodeName().equals("activityConfig")){
								NodeList childNodes = nodes.item(j).getChildNodes();
								NodeList propertiesNodes = childNodes.item(1).getChildNodes();
								for (int k = 0; k < propertiesNodes.getLength(); k++) {
									if(propertiesNodes.item(k) != null && propertiesNodes.item(k).getNodeName().equals("value")){
										if(propertiesNodes.item(k).getAttributes().getNamedItem("sqlStatement") != null)
											this.sqlStatement = propertiesNodes.item(k).getAttributes().getNamedItem("sqlStatement").getTextContent();
										if(propertiesNodes.item(k).getAttributes().getNamedItem("maxRows") != null)
											this.jdbcMaxRows = true;
										if(propertiesNodes.item(k).getAttributes().getNamedItem("timeout") != null)
											this.jdbcTimeout = true;
									}
								}
							}
						}
					}
					break;
				}else if(children.item(i).getNodeName().equals("bpws:targets")){
					NodeList transitions_To = children.item(i).getChildNodes();
					Transition transition;
					for (int j = 0; j < transitions_To.getLength(); j++) {
						if(transitions_To.item(j).getNodeName().equals("bpws:target")){
							String grouptransition = transitions_To.item(j).getAttributes().getNamedItem("linkName").getNodeValue();
							String grouptransition2 = synonymsGroupMapping.get(grouptransition);
							if(grouptransition.contains("GroupStart")){
								transition = transitions.get(grouptransition2);
								grouptransition2 = grouptransition2.substring(0, grouptransition2.indexOf("To"));
								transition.setFrom(grouptransition2);
							}else if(grouptransition.contains("GroupEnd")){
								grouptransition = grouptransition.substring(0, grouptransition.indexOf("To"));
								transition = transitions.get(grouptransition2);
								grouptransition2 = grouptransition2.substring(grouptransition2.indexOf("To")+2);
								transition.setTo(grouptransition2);
							}else{
								transition = transitions.get(transitions_To.item(j).getAttributes().getNamedItem("linkName").getTextContent());
								transition.setTo(attributes.getNamedItem("name").getTextContent());
							}
							NodeList expressions = transitions_To.item(j).getChildNodes();
							if(expressions.item(1) != null){
								String expression = expressions.item(1).getChildNodes().item(1).getAttributes().getNamedItem("expression").getTextContent();
								transition.setXpath(expression);
							}
						}
					}
				}else if(children.item(i).getNodeName().equals("bpws:sources")){
					NodeList transitions_To = children.item(i).getChildNodes();
					Transition transition;
					for (int j = 0; j < transitions_To.getLength(); j++) {
						if(transitions_To.item(j).getNodeName().equals("bpws:source")){
							String grouptransition = transitions_To.item(j).getAttributes().getNamedItem("linkName").getNodeValue();
							String grouptransition2 = synonymsGroupMapping.get(grouptransition);
							if(grouptransition.contains("GroupEnd")){
								transition = transitions.get(grouptransition2);
								grouptransition2 = grouptransition2.substring(grouptransition2.indexOf("To")+2);
								transition.setTo(grouptransition2);
							}else if(grouptransition.contains("GroupStart")){
								transition = transitions.get(grouptransition2);
								grouptransition2 = grouptransition2.substring(0, grouptransition2.indexOf("To"));
								transition.setFrom(grouptransition2);
							}else{
								transition = transitions.get(transitions_To.item(j).getAttributes().getNamedItem("linkName").getTextContent());
								transition.setFrom(attributes.getNamedItem("name").getTextContent());
							}
							
							NodeList expressions = transitions_To.item(j).getChildNodes();
							if(expressions.item(1) != null && expressions.item(1).getChildNodes().getLength() != 0){
								String expression = expressions.item(1).getChildNodes().item(1).getAttributes().getNamedItem("expression").getTextContent();
								transition.setXpath(expression);
							}
						}
					}
				}
			}
		}
	}
	
}
