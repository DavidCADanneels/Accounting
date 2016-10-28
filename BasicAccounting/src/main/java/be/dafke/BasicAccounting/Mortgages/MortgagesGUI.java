package be.dafke.BasicAccounting.Mortgages;

import be.dafke.BusinessActions.TransactionActions;
import be.dafke.BasicAccounting.MainApplication.AccountingPanel;
import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.BusinessCollection;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MortgagesGUI extends AccountingPanel implements ListSelectionListener, ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final JList<Mortgage> list;
	private final JButton pay;// , newMortgage, details;
	private final DefaultListModel<Mortgage> listModel;

    private BusinessCollection<Mortgage> mortgages;
    private Journal journal;
	private Accounts accounts;

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

    public void setAccounting(Accounting accounting) {
        if (accounting == null) {
            setMortgages(null);
            setJournal(null);
			setAccounts(null);
        } else {
            setMortgages(accounting.getMortgages());
			setAccounts(accounting.getAccounts());
            if(accounting.getJournals()!=null){
                setJournal(accounting.getJournals().getCurrentObject());
            }
        }
    }

    public void setMortgages(BusinessCollection<Mortgage> mortgages) {
        this.mortgages = mortgages;
    }

	public void setAccounts(Accounts accounts){
		this.accounts = accounts;
	}

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

	public void refresh() {
        listModel.clear();
        if (mortgages != null) {
            for(Mortgage mortgage : mortgages.getBusinessObjects()) {
                if (!listModel.contains(mortgage)) {
                    listModel.addElement(mortgage);
                }
            }
        }
		list.revalidate();
	}

	public void actionPerformed(ActionEvent arg0) {
		Mortgage mortgage = (Mortgage)list.getSelectedValue();
		Transaction transaction = journal.getCurrentObject();
		if (mortgage != null) {
			TransactionActions.createMortgageTransaction(accounts, mortgage, transaction);
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		if (journal!=null && !e.getValueIsAdjusting() && list.getSelectedIndex() != -1) {
			pay.setEnabled(list.getSelectedValue().isBookable());
		} else {
			pay.setEnabled(false);
		}
	}
}
