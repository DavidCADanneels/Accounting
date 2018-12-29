package be.dafke.BasicAccounting.Journals;

import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.RefreshableDialog;

import javax.swing.*;
import java.awt.*;

/**
 * @author David Danneels
 */
public class JournalSelectorDialog extends RefreshableDialog {
	private JButton ok;
	private Journal journal;
	private JComboBox<Journal> combo;

	public JournalSelectorDialog(Journals journals){
		super("Select Journal:");
		combo = new JComboBox<>();
		for (Journal journal : journals.getBusinessObjects()) {
			combo.addItem(journal);
		}
		combo.addActionListener(e -> journal = (Journal) combo.getSelectedItem());
		ok = new JButton("Ok (Close popup)");
		ok.addActionListener(e -> dispose());

		JPanel innerPanel = new JPanel(new BorderLayout());
		innerPanel.setLayout(new BorderLayout());
		innerPanel.add(combo, BorderLayout.CENTER);
		innerPanel.add(ok, BorderLayout.SOUTH);
		setContentPane(innerPanel);
		pack();
	}

	public void setSelectedJournal(Journal journal){
		combo.setSelectedItem(journal);
	}

	public Journal getSelection(){
		return journal;
	}
}