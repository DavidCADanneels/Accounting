package be.dafke.BasicAccounting.Coda;

import be.dafke.BusinessModel.CounterParty;
import be.dafke.BusinessModel.TmpCounterParty;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ColorRenderer extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (value instanceof TmpCounterParty) {
			cell.setForeground(Color.GREEN);
		} else if (value instanceof CounterParty) {
			cell.setForeground(Color.RED);
		} else {
			cell.setForeground(Color.BLACK);
		}
		return cell;
	}
}
