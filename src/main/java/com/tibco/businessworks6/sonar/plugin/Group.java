package com.tibco.businessworks6.sonar.plugin;

import java.util.ArrayList;
import java.util.List;

public class Group extends Activity {

	protected String type;
	protected List<Activity> activities = new ArrayList<Activity>();
	protected List<Group> groups = new ArrayList<Group>();
	protected List<Transition> transitions = new ArrayList<Transition>();	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}

	public int countAllSubGroups() {	
		int result = 0;
		result += getGroups().size();
		for(Group g:getGroups()){
			result += g.countAllSubGroups();
		}
		return result;
	}
	
	public int countAllSubActivities() {
		int result = 0;
		result += getActivities().size();
		for(Group g:getGroups()){
			result += g.countAllSubActivities();
		}
		return result;
	}
	
	public int countAllSubTransitions() {
		int result = 0;
		result += getTransitions().size();
		for(Group g:getGroups()){
			result += g.countAllSubTransitions();
		}
		return result;
	}
	

	public Activity getActivityByName(String activityName) {
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
}
