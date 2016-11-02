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
    private final Accounting accounting;

    public Projects(Accounting accounting) {
        this.accounting = accounting;
        setName(PROJECTS);
    }

    @Override
	public String getChildType() {
		return PROJECT;
	}

	@Override
	public Project createNewChild(TreeMap<String, String> properties) {
        String name = properties.get(NAME);
        return new Project(name, accounting);
	}
}
