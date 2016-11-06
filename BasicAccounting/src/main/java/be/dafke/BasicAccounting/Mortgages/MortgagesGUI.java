package be.dafke.BasicAccounting.Mortgages;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessActions.AccountingListener;
import be.dafke.BusinessActions.MortgagesListener;
import be.dafke.BusinessActions.TransactionActions;
import be.dafke.BusinessActions.TransactionListener;
import be.dafke.BusinessModel.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;

public class MortgagesGUI extends JPanel implements AccountingListener, MortgagesListener, TransactionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final JList<Mortgage> list;
	private final JButton pay;// , newMortgage, details;
	private final DefaultListModel<Mortgage> listModel;
	private Transaction transaction;

	public MortgagesGUI() {
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Mortgages"));
		list = new JList<>();
		listModel = new DefaultListModel<>();
		list.setModel(listModel);
		list.addListSelectionListener(e -> enablePayButton(e));
		pay = new JButton("Pay");
		pay.addActionListener(e -> book());
		pay.setEnabled(false);
		add(list, BorderLayout.CENTER);
		add(pay, BorderLayout.SOUTH);
	}

	public void book() {
		Mortgage mortgage = list.getSelectedValue();
		if (mortgage != null) {
			TransactionActions.addMortgageTransaction(mortgage, transaction);
			Main.fireTransactionDataChanged();
		}
	}

	public void enablePayButton(ListSelectionEvent e) {
		if (transaction != null && !e.getValueIsAdjusting() && list.getSelectedIndex() != -1) {
			pay.setEnabled(list.getSelectedValue().isBookable());
		} else {
			pay.setEnabled(false);
		}
	}

	@Override
	public void setAccounting(Accounting accounting) {
		setMortgages(accounting==null?null:accounting.getMortgages());
		setJournals(accounting==null?null:accounting.getJournals());
	}

	public void setJournals(Journals journals){
		setJournal(journals==null?null:journals.getCurrentObject());
	}

	public void setJournal(Journal journal){
		setTransaction(journal==null?null:journal.getCurrentObject());
	}

	@Override
	public void setTransaction(Transaction transaction) {
		this.transaction=transaction;
	}

	@Override
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
