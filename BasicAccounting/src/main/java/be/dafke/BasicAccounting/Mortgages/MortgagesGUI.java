package be.dafke.BasicAccounting.Mortgages;

import be.dafke.BusinessActions.*;
import be.dafke.BusinessModel.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MortgagesGUI extends JPanel implements ListSelectionListener, ActionListener, AccountingListener, JournalsListener, MortgagesListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final JList<Mortgage> list;
	private final JButton pay;// , newMortgage, details;
	private final DefaultListModel<Mortgage> listModel;
	private JournalDataChangedListener journalDataChangedListener;
	private Journal journal;

	public MortgagesGUI() {
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Mortgages"));
		list = new JList<>();
		listModel = new DefaultListModel<>();
		list.setModel(listModel);
		list.addListSelectionListener(this);
		pay = new JButton("Pay");
		pay.addActionListener(this);
		pay.setEnabled(false);
		add(list, BorderLayout.CENTER);
		add(pay, BorderLayout.SOUTH);
	}

	public void setJournalDataChangedListener(JournalDataChangedListener journalDataChangedListener) {
		this.journalDataChangedListener = journalDataChangedListener;
	}

	public void actionPerformed(ActionEvent arg0) {
		Mortgage mortgage = list.getSelectedValue();
//		Transaction transaction = journalDataChangedListener.getTransaction();
		Transaction transaction = journal.getCurrentObject();
		if (mortgage != null) {
			TransactionActions.createMortgageTransaction(mortgage, transaction);
			journalDataChangedListener.fireJournalDataChanged();
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		if (journal.getCurrentObject() != null && !e.getValueIsAdjusting() && list.getSelectedIndex() != -1) {
			pay.setEnabled(list.getSelectedValue().isBookable());
		} else {
			pay.setEnabled(false);
		}
	}

	@Override
	public void setAccounting(Accounting accounting) {
		setMortgages(accounting == null ? null : accounting.getMortgages());
		setJournals(accounting == null ? null : accounting.getJournals());
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

	public void setJournals(Journals journals) {
		setJournal(journals == null ? null : journals.getCurrentObject());
	}

	@Override
	public void setJournal(Journal journal) {
		this.journal = journal;
	}
}
