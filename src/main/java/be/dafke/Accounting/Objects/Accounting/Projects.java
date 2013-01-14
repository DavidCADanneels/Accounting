/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.dafke.Accounting.Objects.Accounting;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author David Danneels
 */
public class Projects extends java.util.HashMap<String, Project> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Projects() {
		super();
	}

	public ArrayList<Project> getProjects() {
		return new ArrayList<Project>(values());
	}
}
