/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.ChildrenNeedSeparateFile;

import java.util.TreeMap;

/**
 * @author David Danneels
 */
public class Projects extends BusinessCollection<Project> implements ChildrenNeedSeparateFile {

    public static final String PROJECTS = "Projects";
    public static final String PROJECT = "Project";
    private final Accounts accounts;

    public Projects(Accounts accounts) {
        this.accounts = accounts;
        setName(PROJECTS);
    }

    @Override
	public String getChildType() {
		return PROJECT;
	}

	@Override
	public Project createNewChild(TreeMap<String, String> properties) {
        Project project = new Project(accounts);
        project.setName(properties.get(NAME));
        return project;
	}
}
