package be.dafke.BasicAccounting.Mortgages;

import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BusinessModel.Mortgage;
import be.dafke.BusinessModel.Mortgages;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MortgagesGUI extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final JList<Mortgage> list;
	private final JButton pay;// , newMortgage, details;
	private final DefaultListModel<Mortgage> listModel;
	private Mortgage selectedMortgage;
	private JournalInputGUI journalInputGUI;

	public MortgagesGUI(JournalInputGUI journalInputGUI) {
		this.journalInputGUI = journalInputGUI;
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Mortgages"));
		list = new JList<>();
		listModel = new DefaultListModel<>();
		list.setModel(listModel);
		list.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && list.getSelectedIndex() != -1) {
				selectedMortgage = list.getSelectedValue();
			} else {
				selectedMortgage = null;
			}
			enablePayButton(selectedMortgage);
		});
		pay = new JButton("Pay");
		pay.setMnemonic(KeyEvent.VK_P);
		pay.addActionListener(e -> book());
		pay.setEnabled(false);
		add(list, BorderLayout.CENTER);
		add(pay, BorderLayout.SOUTH);
	}

	public void book() {
		Mortgage mortgage = list.getSelectedValue();
		if (mortgage != null) {
			journalInputGUI.addMortgageTransaction(mortgage);
		}
	}

	public void enablePayButton(Mortgage mortgage) {
		if(mortgage == selectedMortgage) {
			pay.setEnabled(selectedMortgage != null && selectedMortgage.isBookable());
		}
	}

	public void setMortgages(Mortgages mortgages) {
		listModel.clear();
		if (mortgages != null) {
			for (Mortgage mortgage : mortgages.getBusinessObjects()) {
				if (!listModel.contains(mortgage)) {
					listModel.addElement(mortgage);
				}
			}
		}
		list.revalidate();
	}
}
