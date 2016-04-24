/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.util.TreeMap;

/**
 * @author David Danneels
 */
public class Projects extends BusinessCollection<BusinessObject> {
    public Projects(Accounting accounting) {
        try {
            accounting.addBusinessObject(this);
        } catch (EmptyNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        accounting.addKey(getBusinessObjectType());
    }

    @Override
	public String getChildType() {
		return null;
	}

	@Override
	public BusinessObject createNewChild(TreeMap<String, String> properties) {
		return null;
	}
}
