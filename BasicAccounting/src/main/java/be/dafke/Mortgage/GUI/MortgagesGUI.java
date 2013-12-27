package be.dafke.Mortgage.GUI;

import be.dafke.BasicAccounting.GUI.ComponentMap;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.Movement;
import be.dafke.BasicAccounting.Objects.Transaction;
import be.dafke.Mortgage.Objects.Mortgage;
import be.dafke.Mortgage.Objects.Mortgages;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MortgagesGUI extends JPanel implements ListSelectionListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JList<Mortgage> list;
	private final JButton pay;// , newMortgage, details;
	private final DefaultListModel<Mortgage> listModel;

    private Mortgages mortgages;
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

    public void setMortgages(Mortgages mortgages) {
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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Mortgage mortgage = list.getSelectedValue();
		if (mortgage == null) {
			return;
		}
		if (mortgage.isPayedOff()) {
			System.out.println("Payed Off already");
			return;
		}
        Transaction transaction = journal.getCurrentObject();
        Booking booking = new Booking(mortgage);
        booking.setMovement(new Movement(mortgage.getMensuality(),true));
        transaction.addBooking(booking);
        ComponentMap.refreshAllFrames();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (journal!=null && !e.getValueIsAdjusting() && list.getSelectedIndex() != -1) {
			pay.setEnabled(list.getSelectedValue().isBookable());
		} else {
			pay.setEnabled(false);
		}
	}
}
