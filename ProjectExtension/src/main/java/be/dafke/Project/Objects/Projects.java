/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.dafke.Project.Objects;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author David Danneels
 */
public class Projects extends HashMap<String, Project> {

	public Projects() {
		super();
	}

	public ArrayList<Project> getProjects() {
		return new ArrayList<Project>(values());
	}
}