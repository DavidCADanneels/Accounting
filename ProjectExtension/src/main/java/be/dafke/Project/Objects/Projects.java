/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.dafke.Project.Objects;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;

/**
 * @author David Danneels
 */
public class Projects extends BusinessCollection<BusinessObject> {

	@Override
	public String getChildType() {
		return null;
	}

	@Override
	public BusinessObject createNewChild() {
		return null;
	}
}
