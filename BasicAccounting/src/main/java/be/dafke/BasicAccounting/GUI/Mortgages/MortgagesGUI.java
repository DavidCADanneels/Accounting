package be.dafke.BasicAccounting.GUI.Mortgages;

import be.dafke.BasicAccounting.Actions.TransactionActions;
import be.dafke.BasicAccounting.GUI.AccountingPanel;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.Mortgage;
import be.dafke.BasicAccounting.Objects.Transaction;
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

    public MortgagesGUI() {
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Mortgages"));
		list = new JList<Mortgage>();
		listModel = new DefaultListModel<Mortgage>();
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
        } else {
            setMortgages(accounting.getMortgages());
            if(accounting.getJournals()!=null){
                setJournal(accounting.getJournals().getCurrentObject());
            }
        }
    }

    public void setMortgages(BusinessCollection<Mortgage> mortgages) {
        this.mortgages = mortgages;
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
			TransactionActions.createMortgageTransaction(mortgage, transaction);
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
