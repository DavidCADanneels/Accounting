package be.dafke.Accounting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import be.dafke.Accounting.Objects.Accountings;
import be.dafke.Accounting.Objects.Transaction;
import be.dafke.Mortgage.Mortgage;

public class MortgagesGUI extends JPanel implements ListSelectionListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JList list;
	private final JButton pay;// , newMortgage, details;
	private final JournalGUI journalGUI;
	private final AccountingGUIFrame parent;
	private final DefaultListModel listModel;

	public MortgagesGUI(JournalGUI journalGUI, AccountingGUIFrame parent) {
		this.parent = parent;
		this.journalGUI = journalGUI;
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
		if (Accountings.getCurrentAccounting() != null) {
			for(Mortgage mortgage : Accountings.getCurrentAccounting().getMortgagesTables()) {
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
		parent.repaintAllFrames();
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
