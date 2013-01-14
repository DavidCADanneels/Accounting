package be.dafke.Accounting.GUI.CodaManagement;

import be.dafke.Accounting.Objects.Coda.CounterParty;
import be.dafke.Accounting.Objects.Coda.TmpCounterParty;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ColorRenderer extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (value instanceof TmpCounterParty) {
			cell.setForeground(Color.RED);
		} else if (value instanceof CounterParty) {
			cell.setForeground(Color.GREEN);
		} else {
			cell.setForeground(Color.BLACK);
		}
		return cell;
	}
}
