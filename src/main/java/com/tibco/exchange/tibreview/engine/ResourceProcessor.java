package com.tibco.exchange.tibreview.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tibco.exchange.tibreview.common.PMDManager;
import com.tibco.exchange.tibreview.common.TIBResource;
import com.tibco.exchange.tibreview.common.Util;
import com.tibco.exchange.tibreview.exception.EngineException;
import com.tibco.exchange.tibreview.exception.ProcessorException;
import com.tibco.exchange.tibreview.model.rules.Configuration;
import com.tibco.exchange.tibreview.model.rules.Resourcerule;
import com.tibco.exchange.tibreview.model.rules.Tibrules;
import com.tibco.exchange.tibreview.processor.resourcerule.ConfigurationProcessor;
import com.tibco.exchange.tibreview.processor.resourcerule.RRProcessable;

public class ResourceProcessor implements AssetProcessable {
	private static final String RESOURCE_EXTENSION = "Resource";
	private static final Logger LOGGER = Logger.getLogger(ResourceProcessor.class);

	@Override
	public void process(Context context, Tibrules tibrules, PMDManager manager) throws EngineException {
		// Check if type is project or specific process
		if (!Engine.INPUT_PROCESS.equals(context.getInputType())
				&& !Engine.INPUT_PROJECT.equals(context.getInputType())) {
			return;
		}

		// List processes
		final List<String> resources;

		if (Engine.INPUT_PROJECT.equals(context.getInputType())) {
			try {
				resources = listResources(context.getSource());
			} catch(Exception e) {
				LOGGER.warn("Error while listing resources");
				return;
			}
		} else if (Engine.INPUT_RESOURCE.equals(context.getInputType())) {
			resources = new ArrayList<>();
			resources.add(context.getSource());
		} else {
			return;
		}

		LOGGER.debug("resources=" + resources);

		processResources(context, tibrules, manager, resources);
	}

	private void processResources(Context context, Tibrules tibrules, PMDManager manager, List<String> resources) {
		LOGGER.info("Process TIBCO resources");
		for (String resource : resources) {
			try {
				processUnitResource(context, tibrules, manager, resource);
			} catch (Exception e) {
				LOGGER.error("Unable to process resource " + resource + ": " + e);
			}
		}
	}

	private void processUnitResource(Context context, Tibrules tibrules, PMDManager manager, String resource) {
		LOGGER.debug("Testing unit TIBCO resource: " + resource);
		
		List<Resourcerule> rules = tibrules.getResource().getRule();

		for (Resourcerule rule : rules) {
			try {
				if (!context.getDisabledRules().containsKey(rule.getName())) {
					LOGGER.debug("Applying rule: " + rule.getName());

					Configuration configuration = rule.getConfiguration();
					RRProcessable processor = new ConfigurationProcessor();
					
					try {
						manager.addViolations(resource,
								processor.process(context, new TIBResource(resource), rule, configuration));
					} catch (ProcessorException e) {
						LOGGER.error("Processing exception: " + e.getMessage());
					}
					
				} else {
					LOGGER.debug("Rule " + rule.getName() + " is disabled");
				}
			} catch (Exception e) {
				LOGGER.error("Processing exception: " + e.getMessage());
			}

		}
	}

	private List<String> listResources(String project) throws IOException {
		return Util.listFile(project, RESOURCE_EXTENSION);
	}

}
