/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.ChildrenNeedSeparateFile;
import be.dafke.ObjectModel.MustBeRead;

import java.util.TreeMap;

/**
 * @author David Danneels
 */
public class Projects extends BusinessCollection<Project> implements ChildrenNeedSeparateFile, MustBeRead {

    public static final String PROJECTS = "Projects";
    public static final String PROJECT = "Project";
    private final Accounts accounts;
    private final AccountTypes accountTypes;

    public Projects(Accounts accounts, AccountTypes accountTypes) {
        this.accounts = accounts;
        this.accountTypes = accountTypes;
        setName(PROJECTS);
    }

    @Override
	public String getChildType() {
		return PROJECT;
	}

	@Override
	public Project createNewChild(TreeMap<String, String> properties) {
        String name = properties.get(NAME);
        return new Project(name, accounts, accountTypes);
	}
}
