package be.dafke.BasicAccounting.Meals;

import be.dafke.Accounting.BusinessModel.Allergene;
import be.dafke.Accounting.BusinessModel.Allergenes;
import be.dafke.ComponentModel.SelectableTableModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AllergenesNoDetailsDataTableModel extends AllergenesDataTableModel {

	public int getColumnCount() {
		return 2;
	}
}