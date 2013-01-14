package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Accounting.Transaction;
import be.dafke.Accounting.Objects.Mortgage.Mortgage;
import be.dafke.Accounting.Objects.RefreshEvent;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class MortgagesGUI extends JPanel implements ListSelectionListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JList list;
	private final JButton pay;// , newMortgage, details;
	private final JournalGUI journalGUI;
	private final DefaultListModel listModel;
	private final Accountings accountings;

	public MortgagesGUI(Accountings accountings, JournalGUI journalGUI) {
		this.journalGUI = journalGUI;
		this.accountings = accountings;
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Mortgages"));
		list = new JList();
		listModel = new DefaultListModel();
		list.setModel(listModel);
		list.addListSelectionListener(this);
		pay = new JButton("Pay");
		pay.addActionListener(this);
		add(list, BorderLayout.CENTER);
		add(pay, BorderLayout.SOUTH);
	}

	public void refresh() {
		Accounting accounting = accountings.getCurrentAccounting();
		if (accounting != null) {
			for(Mortgage mortgage : accounting.getMortgagesTables()) {
				if (!listModel.contains(mortgage)) {
					listModel.addElement(mortgage);
				}
			}
		}
		list.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Mortgage mortgage = (Mortgage) list.getSelectedValue();
		if (mortgage == null) {
			return;
		}
		if (mortgage.isPayedOff()) {
			System.out.println("Payed Off already");
			return;
		}
		mortgage.pay(Transaction.getInstance());
		journalGUI.refresh();
		BigDecimal debettotaal = Transaction.getInstance().getDebetTotaal();
		BigDecimal credittotaal = Transaction.getInstance().getCreditTotaal();
		if (debettotaal.compareTo(credittotaal) == 0) journalGUI.setOK();
		RefreshEvent event = new RefreshEvent(this);
		System.out.println("notifyAll called in " + this.getClass());
		event.notifyAll();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting() && list.getSelectedIndex() != -1) {
			pay.setEnabled(true);
		} else {
			pay.setEnabled(false);
		}
	}
}
