package com.tibco.businessworks6.sonar.plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Process {

	//public static String newline = System.getProperty("line.separator");
	protected String name;
	protected boolean isSubProcess;
	protected List<EventSource> eventSources = new ArrayList<EventSource>();
	protected List<Activity> activities = new ArrayList<Activity>();
	protected List<Group> groups = new ArrayList<Group>();
	protected Deque<Group> groupsstack = new ArrayDeque<Group>();
	protected Map<String, Service> services = new HashMap<String, Service>();
	protected Map<String, Service> processReferenceServices = new HashMap<String, Service>();
	protected Map<String, String> synonymsGroupMapping = new HashMap<String, String>();
	

	protected boolean hasForEachGroup;
	protected Document processXmlDocument;
	protected XPath xPath;
	protected HashMap<String, Transition> transitionMap = new HashMap<String, Transition>();
	protected int groupcount = 0; 
	protected int catchcount = 0;
	protected int eventHandler = 0;
	protected NamedNodeMap namedNodeMap;
	
	public Map<String, String> getSynonymsGroupMapping() {
		return synonymsGroupMapping;
	}
	
	public boolean isHasForEachGroup() {
		return hasForEachGroup;
	}

	public void setHasForEachGroup(boolean hasForEachGroup) {
		this.hasForEachGroup = hasForEachGroup;
	}

	public boolean isSubProcess() {
		return isSubProcess;
	}

	public void setSubProcess(boolean isSubProcess) {
		this.isSubProcess = isSubProcess;
	}

	public Map<String, Service> getProcessReferenceServices() {
		return processReferenceServices;
	}

	public void setProcessReferenceServices(Map<String, Service> onlyReferenceServices) {
		this.processReferenceServices = onlyReferenceServices;
	}

	public Map<String, Service> getServices() {
		return services;
	}

	public void setServices(Map<String, Service> services) {
		this.services = services;
	}

	public int getEventHandler() {
		return eventHandler;
	}

	public void setEventHandler(int eventHandler) {
		this.eventHandler = eventHandler;
	}

	public int getGroupcount() {
		return groupcount;
	}

	public void setGroupcount(int groupcount) {
		this.groupcount = groupcount;
	}

	public int getCatchcount() {
		return catchcount;
	}

	public void setCatchcount(int catchcount) {
		this.catchcount = catchcount;
	}

	public Process() {
		super();
	}

	public String getName() {
		return name;
	}

	public Process setName(String name) {
		this.name = name;
		return this;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public Process setActivities(List<Activity> activities) {
		this.activities = activities;
		return this;
	}

	public Activity getActivityByName(String activityName){
		for(Activity activity : activities){
			if(activity.getName().equals(activityName)){
				return activity;
			}
		}
		for(Group group: groups){
			Activity candidate;
			candidate = group.getActivityByName(activityName);
			if(candidate != null){
				return candidate;
			}
		}
		return null;
	}

	public EventSource getEventSourceByName(String eventsource){
		for(EventSource evSource : eventSources){
			if(evSource.getName().equals(eventsource))
				return evSource;
		}
		return null;
	}

	public Group getGroupByName(String groupName){
		for(Group group : groups){
			if(group.getName().equals(groupName))
				return group;
		}
		return null;
	}

	public List<Activity> getActivitiesByType(String activityType) {
		List<Activity> result = new ArrayList<Activity>();
		for(Activity activity : activities){
			if(activity.getType().equals(activityType)){
				result.add(activity);
			}
		}
		for(Group group: groups){
			List<Activity> candidates = group.getActivitiesByType(activityType);
			result.addAll(candidates);
		}
		return result;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public Process setGroups(List<Group> groups) {
		this.groups = groups;
		return this;
	}

	public HashMap<String, Transition> getTransitions() {
		return transitionMap;
	}

	public Document getProcessXmlDocument() {
		return processXmlDocument;
	}

	public Process setProcessXmlDocument(Document processXmlDocument) {
		this.processXmlDocument = processXmlDocument;
		this.xPath =  XPathFactory.newInstance().newXPath();
		return this;
	}

	public Process setProcessXmlDocument(File processFile) throws IOException {
		try {
			DocumentBuilderFactory factory =
					DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.processXmlDocument = builder.parse(processFile);
			this.xPath = XPathFactory.newInstance().newXPath();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		return this;
	}

	/*public Process setProcessXmlDocument(String processSourceCode) {
		this.processXmlDocument = new SaxParser().parseDocument(
				new ByteArrayInputStream(processSourceCode.getBytes()), true);
		this.xPath =  XPathFactory.newInstance().newXPath();
		return this;
	}*/


	public Process setProcessXmlDocument1(File file) {
		try {
			DocumentBuilderFactory factory =
					DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.processXmlDocument = builder.parse(file);
			xPath =  XPathFactory.newInstance().newXPath();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

	public void startParsing(){
		String process = "/process";
		String transitions = "/process/scope";
		parseProcessName(process);
		try {
			NodeList children = (NodeList) xPath.compile(transitions).evaluate(processXmlDocument, XPathConstants.NODESET);
			parseProcess(children.item(0).getChildNodes());
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String findActualReferenceServiceName(String referenceService){
		String partnerLinks = "/process/partnerLinks/partnerLink";
		try {
			NodeList children = (NodeList) xPath.compile(partnerLinks).evaluate(processXmlDocument, XPathConstants.NODESET);
			if(children != null){
				for (int i = 0; i < children.getLength(); i++) {
					if(children.item(i).getAttributes().getNamedItem("name").getNodeValue().equals(referenceService)){
						NodeList nodeList = children.item(i).getChildNodes();
						for(int j=0; j< nodeList.getLength(); j++){
							if(nodeList.item(j).getNodeName().equals("tibex:ReferenceWire")){
								referenceService = nodeList.item(j).getAttributes().getNamedItem("serviceName").getNodeValue();
								break;
							}
						}
					}
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return referenceService;
	}
	
	public String findReferenceServiceInline(String referenceService){
		String partnerLinks = "/process/partnerLinks/partnerLink";
		try {
			NodeList children = (NodeList) xPath.compile(partnerLinks).evaluate(processXmlDocument, XPathConstants.NODESET);
			if(children != null){
				for (int i = 0; i < children.getLength(); i++) {
					if(children.item(i).getAttributes().getNamedItem("name").getNodeValue().equals(referenceService)){
						NodeList nodeList = children.item(i).getChildNodes();
						for(int j=0; j< nodeList.getLength(); j++){
							if(nodeList.item(j).getNodeName().equals("tibex:ReferenceWire")){
								referenceService = nodeList.item(j).getAttributes().getNamedItem("inline").getNodeValue();
								break;
							}
						}
					}
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return referenceService;
	}
	
	public String findActualReferenceServiceProcessName(String referenceService){
		String partnerLinks = "/process/partnerLinks/partnerLink";
		try {
			NodeList children = (NodeList) xPath.compile(partnerLinks).evaluate(processXmlDocument, XPathConstants.NODESET);
			if(children != null){
				for (int i = 0; i < children.getLength(); i++) {
					if(children.item(i).getAttributes().getNamedItem("name").getNodeValue().equals(referenceService)){
						NodeList nodeList = children.item(i).getChildNodes();
						for(int j=0; j< nodeList.getLength(); j++){
							if(nodeList.item(j).getNodeName().equals("tibex:ReferenceWire")){
								referenceService = nodeList.item(j).getAttributes().getNamedItem("processName").getNodeValue();
								break;
							}
						}
					}
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return referenceService;
	}

	public void parseProcessName(String processName){
		try {
			NodeList children = (NodeList) xPath.compile(processName).evaluate(processXmlDocument, XPathConstants.NODESET);
			setName(children.item(0).getAttributes().getNamedItem("name").getTextContent());
			namedNodeMap = children.item(0).getAttributes();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	public void parseProcess(NodeList transitions){
		for (int i = 0; i < transitions.getLength(); i++) {
			String nodeName = transitions.item(i).getNodeName();
			NamedNodeMap nodeMap = transitions.item(i).getAttributes();
			String whichGroup = null;
			if(nodeMap != null && nodeMap.getNamedItem("tibex:group") != null)
				whichGroup = nodeMap.getNamedItem("tibex:group").getNodeValue();
			if(nodeName.equals("bpws:extensionActivity") ||  nodeName.equals("bpws:reply") || nodeName.equals("bpws:compensate") || nodeName.equals("bpws:exit") || nodeName.equals("bpws:receive") || nodeName.equals("bpws:empty") || nodeName.equals("bpws:throw") || nodeName.equals("bpws:rethrow")){
				parseActivities(transitions.item(i));
			}else if(nodeName.equals("bpws:links")){
				parseTransitions(transitions.item(i), groupsstack);
				if(groupsstack.peek() != null)
					groupsstack.pollLast();
			}else if(nodeName.equals("bpws:scope") || nodeName.equals("bpws:flow") || nodeName.equals("bpws:eventHandlers") || nodeName.equals("bpws:onEvent") || nodeName.equals("bpws:faultHandlers") || nodeName.equals("bpws:repeatUntil") || nodeName.equals("bpws:while") || nodeName.equals("bpws:catchAll") || nodeName.equals("bpws:catch") || nodeName.equals("bpws:forEach") || nodeName.equals("bpws:pick")){
				if(nodeName.equals("bpws:scope") && transitions.item(i).getAttributes().getNamedItem("tibex:group") != null)
					setGroupcount(getGroupcount()+1);
				else if(whichGroup != null && !whichGroup.equalsIgnoreCase("groupStart") && !whichGroup.equalsIgnoreCase("groupEnd") && nodeMap.getNamedItem("tibex:xpdlId") != null)
					setGroupcount(getGroupcount()+1);
				if(nodeName.equals("bpws:catchAll") || nodeName.equals("bpws:catch")){// && (nodeMap != null && nodeMap.getNamedItem("tibex:xpdlId") == null))
					Group dummygroup= new Group();
					groupsstack.add(dummygroup);
					setCatchcount(getCatchcount()+1);
				}
				if(nodeName.equals("bpws:eventHandlers"))
					setEventHandler(getEventHandler()+1);
				if(whichGroup != null && (whichGroup.equalsIgnoreCase("localTX") || whichGroup.equalsIgnoreCase("critical") || whichGroup.equalsIgnoreCase("repeatUntil") || whichGroup.equalsIgnoreCase("while") || whichGroup.equalsIgnoreCase("foreach") || whichGroup.equalsIgnoreCase("iterate") || whichGroup.equalsIgnoreCase("none") || whichGroup.equalsIgnoreCase("repeatOnError"))){
					if(whichGroup.equalsIgnoreCase("foreach"))
						hasForEachGroup = true;
					Group group = new Group();
					group.setName(nodeMap.getNamedItem("name").getNodeValue());
					group.setType(whichGroup);
					groups.add(group);
					groupsstack.add(group);
					parseTranstionFromToGroups(transitions.item(i));
				}
				parseProcess(transitions.item(i).getChildNodes());
			}else if(nodeName.equals("bpws:onMessage")){
				String serviceName =  transitions.item(i).getAttributes().getNamedItem("partnerLink").getNodeValue();
				String operationName = transitions.item(i).getAttributes().getNamedItem("operation").getNodeValue();
				if(services.get(serviceName) == null){
					String namespacePrefix = transitions.item(i).getAttributes().getNamedItem("portType").getNodeValue();
					namespacePrefix = namespacePrefix.substring(0, namespacePrefix.indexOf(":"));
					Service service = new Service(serviceName);
					service.setNamespace(namedNodeMap.getNamedItem("xmlns:"+namespacePrefix).getNodeValue());
					service.setImplementationProcess(getName());
					Operation operation = new Operation(operationName);
					service.getOperations().put(operationName, operation);
					services.put(serviceName, service);
				}else{
					Service service = services.get(serviceName);
					Operation operation = new Operation(operationName);
					service.getOperations().put(operationName, operation);
				}
				parseProcess(transitions.item(i).getChildNodes());
				//parseService(transitions.item(i));
				//do something related to reference and service
				//also create an activity with source and target similar to normal activity
			}else if(nodeName.equals("bpws:invoke")){
				String serviceName = null;
				String referencedServiceName = transitions.item(i).getAttributes().getNamedItem("partnerLink").getTextContent();
				String calledOperation = transitions.item(i).getAttributes().getNamedItem("operation").getTextContent();
				String namespacePrefix = transitions.item(i).getAttributes().getNamedItem("portType").getNodeValue();
				namespacePrefix = namespacePrefix.substring(0, namespacePrefix.indexOf(":"));
				if(transitions.item(i).getParentNode().getParentNode().getParentNode().getAttributes().getNamedItem("partnerLink") != null){
					serviceName = transitions.item(i).getParentNode().getParentNode().getParentNode().getAttributes().getNamedItem("partnerLink").getNodeValue();
					String operationName = transitions.item(i).getParentNode().getParentNode().getParentNode().getAttributes().getNamedItem("operation").getNodeValue();
					Service service = services.get(serviceName);
					HashMap<String, Operation> operations = service.getOperations();
					Operation operation = operations.get(operationName);
					List<Service> operationReferencedService = operation.getOperationReferencedService();
					Service referencedService = new Service(referencedServiceName);
					referencedService.getOperations().put(calledOperation, new Operation(calledOperation));
					operationReferencedService.add(referencedService);
					populateProcessReferencedService(referencedServiceName, calledOperation, namespacePrefix);
					parseActivities(transitions.item(i));
				}else{
					populateProcessReferencedService(referencedServiceName, calledOperation, namespacePrefix);
					parseActivities(transitions.item(i));
				}	

			}
		}
		//nNode.getAttributes().getNamedItem("name").getTextContent();
	}

	public void populateProcessReferencedService(String referencedServiceName, String calledOperation, String namespacePrefix){
		Service referencedService = processReferenceServices.get(referencedServiceName);
		if(referencedService == null){
			String actualReferencedServiceName = findActualReferenceServiceName(referencedServiceName);
			referencedService = new Service(actualReferencedServiceName);
			referencedService.setNamespace(namedNodeMap.getNamedItem("xmlns:"+namespacePrefix).getNodeValue());
			referencedService.setImplementationProcess(findActualReferenceServiceProcessName(referencedServiceName));
			referencedService.setInline(findReferenceServiceInline(referencedServiceName));
			HashMap<String, Operation> operations = referencedService.getOperations();
			operations.put(calledOperation, new Operation(calledOperation));
			processReferenceServices.put(actualReferencedServiceName, referencedService);
		}else{
			HashMap<String, Operation> operations = referencedService.getOperations();
			if(operations.get(calledOperation) == null){
				operations.put(calledOperation, new Operation(calledOperation));
			}
		}
	}

	public void parseTranstionFromToGroups(Node parent){
		NodeList children = parent.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			NodeList transitions = children.item(i).getChildNodes();
			if(children.item(i).getNodeName().equals("bpws:targets")){
				for (int j = 0; j < transitions.getLength(); j++) {
					if(transitions.item(j).getNodeName().equals("bpws:target")){
						String grouptransition = transitions.item(j).getAttributes().getNamedItem("linkName").getNodeValue();
						String grouptransition2 = synonymsGroupMapping.get(grouptransition);
						if(grouptransition.contains("GroupEnd")){
							Transition transition = transitionMap.get(grouptransition2);
							grouptransition2 = grouptransition2.substring(grouptransition2.indexOf("To")+2);
							transition.setTo(grouptransition2);
						}else if(grouptransition.contains("GroupStart")){
							Transition transition = transitionMap.get(grouptransition2);
							grouptransition2 = grouptransition2.substring(0, grouptransition2.indexOf("To"));
							transition.setFrom(grouptransition2);
						}else{
							Transition transition = transitionMap.get(transitions.item(j).getAttributes().getNamedItem("linkName").getNodeValue());
							transition.setTo(parent.getAttributes().getNamedItem("name").getTextContent());
						}
					}
				}
			}else if(children.item(i).getNodeName().equals("bpws:sources")){
				for (int j = 0; j < transitions.getLength(); j++) {
					if(transitions.item(j).getNodeName().equals("bpws:source")){
						String grouptransition = transitions.item(j).getAttributes().getNamedItem("linkName").getNodeValue();
						String grouptransition2 = synonymsGroupMapping.get(grouptransition);
						if(grouptransition.contains("GroupStart")){
							Transition transition = transitionMap.get(grouptransition2);
							grouptransition2 = grouptransition2.substring(0, grouptransition2.indexOf("To"));
							transition.setFrom(grouptransition2);
						}else if(grouptransition.contains("GroupEnd")){
							Transition transition = transitionMap.get(grouptransition2);
							grouptransition2 = grouptransition2.substring(grouptransition2.indexOf("To")+2);
							transition.setTo(grouptransition2);
						}else{
							Transition transition = transitionMap.get(transitions.item(j).getAttributes().getNamedItem("linkName").getNodeValue());
							transition.setFrom(parent.getAttributes().getNamedItem("name").getTextContent());
						}
					}
				}
			}
		}
	}


	public void parseTransitions(Node parent, Deque<Group> groupsstack){
		NodeList children = parent.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			if(children.item(i).getNextSibling() != null){
				if(children.item(i).getNextSibling().getNodeName().equals("bpws:link")){
					Transition transition = new Transition();
					//int lineNumber = SaxParser.getStartLineNumber(children.item(i).getNextSibling());
					//transition.setLineNumber(lineNumber);
					String transitionName = children.item(i).getNextSibling().getAttributes().getNamedItem("name").getTextContent();
					if(transitionName.contains("GroupStart")){
						String synonymsKey = transitionName;
						transitionName = transitionName.substring(transitionName.indexOf("To")+2);
						//String from = parent.getParentNode().getAttributes().getNamedItem("name").getNodeValue();
						String from = groupsstack.peekLast().getName();
						transition.setFrom(from);
						transition.setTo(transitionName);
						transitionName = from +"To"+ transitionName;
						synonymsGroupMapping.put(synonymsKey, transitionName);
					}
					if(transitionName.contains("GroupEnd")){
						String synonymsKey = transitionName;
						transitionName = transitionName.substring(0, transitionName.indexOf("To"));
						String to = groupsstack.peekLast().getName();
						transition.setTo(to);
						transition.setFrom(transitionName);
						transitionName = transitionName +"To"+to ;
						synonymsGroupMapping.put(synonymsKey, transitionName);
					}
					if(children.item(i).getNextSibling().getAttributes().getNamedItem("tibex:label") != null){
						transition.setLabel(children.item(i).getNextSibling().getAttributes().getNamedItem("tibex:label").getTextContent());
					}
					if(children.item(i).getNextSibling().getAttributes().getNamedItem("tibex:linkType") != null)
						transition.setConditionType(children.item(i).getNextSibling().getAttributes().getNamedItem("tibex:linkType").getTextContent());
					transition.setName(transitionName);
					transitionMap.put(transitionName, transition);

				}
			}
		}

	}

	public void parseActivities(Node parent){
		if(parent.getNodeName().equals("bpws:receive")){
			String serviceName =  parent.getAttributes().getNamedItem("partnerLink").getNodeValue();
			String operationName = parent.getAttributes().getNamedItem("operation").getNodeValue();
			if(services.get(serviceName) == null){
				String namespacePrefix = parent.getAttributes().getNamedItem("portType").getNodeValue();
				namespacePrefix = namespacePrefix.substring(0, namespacePrefix.indexOf(":"));
				Service service = new Service(serviceName);
				service.setNamespace(namedNodeMap.getNamedItem("xmlns:"+namespacePrefix).getNodeValue());
				service.setImplementationProcess(getName());
				Operation operation = new Operation(operationName);
				service.getOperations().put(operationName, operation);
				services.put(serviceName, service);
			}else{
				Service service = services.get(serviceName);
				Operation operation = new Operation(operationName);
				service.getOperations().put(operationName, operation);
			}
			Activity activity = new Activity();
			activity.setNode(parent);
			activity.setName(parent.getAttributes().getNamedItem("name").getTextContent());
			activity.parseActivityConfiguration(transitionMap, synonymsGroupMapping);
			activities.add(activity);
		}else{
			if(parent.getChildNodes().item(0) != null){
				Node children = parent.getChildNodes().item(0).getNextSibling();
				if(children.getNodeName().equals("tibex:receiveEvent")){
					parseProcessStarterActivity(children);
				}else if(children.getNodeName().equals("tibex:activityExtension") || children.getNodeName().equals("tibex:extActivity")){
					Activity activity = new Activity();
					activity.setNode(children);
					activity.parseActivityConfiguration(transitionMap, synonymsGroupMapping);
					activities.add(activity);
				}else if(parent.getNodeName().equals("bpws:rethrow") || parent.getNodeName().equals("bpws:compensate") || parent.getNodeName().equals("bpws:throw") || parent.getNodeName().equals("bpws:exit") || parent.getNodeName().equals("bpws:reply") || parent.getNodeName().equals("bpws:invoke") || (parent.getNodeName().equals("bpws:empty") && parent.getAttributes().getNamedItem("tibex:group") == null)){
					Activity activity = new Activity();
					activity.setNode(parent);
					activity.setName(parent.getAttributes().getNamedItem("name").getTextContent());
					activity.parseActivityConfiguration(transitionMap, synonymsGroupMapping);
					activities.add(activity);
				}else if(parent.getNodeName().equals("bpws:empty") && parent.getAttributes().getNamedItem("tibex:group") != null){
					parseTranstionFromToGroups(parent);
				}
			}
		}

	}

	public void parseProcessStarterActivity(Node processStarter){
		EventSource eventSource = new EventSource();
		eventSource.setNode(processStarter);
		eventSource.setName(processStarter.getAttributes().getNamedItem("name").getTextContent());
		for (int i = 0; i < processStarter.getChildNodes().getLength(); i++) {
			if(processStarter.getChildNodes().item(i).getNodeName().equals("bpws:sources")){
				String transitionName = processStarter.getChildNodes().item(i).getChildNodes().item(1).getAttributes().getNamedItem("linkName").getTextContent();
				Transition transition = transitionMap.get(transitionName);
				transition.setFrom(processStarter.getAttributes().getNamedItem("name").getTextContent());
			}
			if(processStarter.getChildNodes().item(i).getNodeName().equals("tibex:eventSource")){
				eventSource.setType(processStarter.getChildNodes().item(i).getChildNodes().item(1).getAttributes().getNamedItem("activityTypeID").getNodeValue());
				eventSources.add(eventSource);
			}
		}
	}


	public int countAllGroups() {	
		int result = 0;
		result += getGroups().size();
		for(Group g:getGroups()){
			result += g.countAllSubGroups();
		}
		return result;
	}

	public int countAllActivities() {
		int result = 0;
		result += getActivities().size();
		return result;
	}

	public int countAllTransitions() {
		int result=0;
		Map<String, Transition> map = getTransitions();
		result += map.size();
		for(Group g:getGroups()){
			result += g.countAllSubTransitions();
		}
		return result;
	}

	public int getEventSourcesCount(){
		return eventSources.size();
	}

	public List<EventSource> getEventSources(){
		return eventSources;
	}
}
