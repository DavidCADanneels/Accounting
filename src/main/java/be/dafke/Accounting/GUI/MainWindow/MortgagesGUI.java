package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.GUI.ComponentMap;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Booking;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Mortgage;
import be.dafke.Accounting.Objects.Accounting.Mortgages;
import be.dafke.Accounting.Objects.Accounting.Movement;
import be.dafke.Accounting.Objects.Accounting.Transaction;

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
                setJournal(accounting.getJournals().getCurrentJournal());
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
        Transaction transaction = journal.getCurrentTransaction();
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
