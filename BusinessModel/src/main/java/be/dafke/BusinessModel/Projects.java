/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

import java.util.TreeMap;

/**
 * @author David Danneels
 */
public class Projects extends BusinessCollection<Project> {

    public static final String PROJECTS = "Projects";
    public static final String PROJECT = "Project";

    public Projects(/*Accounting accounting*/) {
//        this.accounting = accounting;
        setName(PROJECTS);
    }

    @Override
	public String getChildType() {
		return PROJECT;
	}

	@Override
	public Project createNewChild(TreeMap<String, String> properties) {
        Project project = new Project();
        project.setName(properties.get(NAME));
        return project;
	}
}
