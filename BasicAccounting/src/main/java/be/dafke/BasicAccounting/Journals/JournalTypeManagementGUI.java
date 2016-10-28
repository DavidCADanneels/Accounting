package be.dafke.BasicAccounting.Journals;

import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.RefreshableFrame;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

public class JournalTypeManagementGUI extends RefreshableFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JList<AccountType> debit, credit, types;
	private final JButton addLeft, addRight, removeLeft, removeRight;
	private final ArrayList<AccountType> debitTypes, creditTypes, allTypes;
	private final DefaultListModel<AccountType> debitModel, creditModel, typesModel;
	private final AccountTypes accountTypes;

	public JournalTypeManagementGUI(AccountTypes accountTypes) {
		super(getBundle("Accounting").getString("JOURNAL_TYPE_MANAGEMENT_TITLE"));
		this.accountTypes = accountTypes;
		debitTypes = new ArrayList<>();
		creditTypes = new ArrayList<>();
		allTypes = new ArrayList<>();
		debitModel = new DefaultListModel<>();
		debit = new JList<>(debitModel);
		creditModel = new DefaultListModel<>();
		credit = new JList<>(creditModel);
        typesModel = new DefaultListModel<>();
		types = new JList<>(typesModel);
		addLeft = new JButton(getBundle("Accounting").getString("ADD_TYPE_TO_DEBITS"));
		addRight = new JButton(getBundle("Accounting").getString("ADD_TYPE_TO_CREDITS"));
		removeLeft = new JButton(getBundle("Accounting").getString("REMOVE_TYPE_FROM_DEBITS"));
		removeRight = new JButton(getBundle("Accounting").getString("REMOVE_TYPE_FROM_CREDITS"));
		addLeft.addActionListener(this);
		addRight.addActionListener(this);
		removeLeft.addActionListener(this);
		removeRight.addActionListener(this);
//		addLeft.setEnabled(false);
//		addRight.setEnabled(false);
//		removeLeft.setEnabled(false);
//		removeRight.setEnabled(false);
		JPanel east = new JPanel();
		JPanel west = new JPanel();
		JPanel center = new JPanel();
		JPanel south = new JPanel(new GridLayout(0, 2));
		east.setLayout(new BorderLayout());
		west.setLayout(new BorderLayout());
		center.setLayout(new BorderLayout());
		west.add(new JLabel(getBundle("Accounting").getString("DEBIT_TYPES")), BorderLayout.NORTH);
		JScrollPane debitScroll = new JScrollPane(debit);
		west.add(debitScroll, BorderLayout.CENTER);
		east.add(new JLabel(getBundle("Accounting").getString("CREDIT_TYPES")), BorderLayout.NORTH);
		JScrollPane creditScroll = new JScrollPane(credit);
		east.add(creditScroll, BorderLayout.CENTER);
		center.add(new JLabel(getBundle("Accounting").getString("ALL_TYPES")), BorderLayout.NORTH);
		JScrollPane typesScroll = new JScrollPane(types);
		center.add(typesScroll, BorderLayout.CENTER);
		south.add(addLeft);
		south.add(addRight);
		south.add(removeLeft);
		south.add(removeRight);
		JPanel middle = new JPanel();
		middle.setLayout(new GridLayout(1, 0));
//		west.setSize(200, 400);
//		center.setSize(200, 400);
//		east.setSize(200, 400);
		middle.add(west);
		middle.add(center);
		middle.add(east);
		JPanel panel = new JPanel(new BorderLayout());
		// panel.add(west, BorderLayout.WEST);
		// panel.add(east, BorderLayout.EAST);
		panel.add(middle, BorderLayout.CENTER);
		panel.add(south, BorderLayout.SOUTH);

		JPanel north = new JPanel();
		// TODO: add components to enter the name and save
		// + show existing types and check for doubles

//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(panel);
		pack();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addLeft) {
			DefaultListModel<AccountType> model = (DefaultListModel<AccountType>) debit.getModel();
			int rows[] = types.getSelectedIndices();
			if (rows.length != 0) {
				for(int i : rows) {
					AccountType type = accountTypes.getBusinessObjects().get(i);
					if (!debitTypes.contains(type)) {
						debitTypes.add(type);
						model.addElement(type);
					}
				}
			}
		} else if (e.getSource() == addRight) {
			DefaultListModel<AccountType> model = (DefaultListModel<AccountType>) credit.getModel();
			int rows[] = types.getSelectedIndices();
			if (rows.length != 0) {
				for(int i : rows) {
                    AccountType type = accountTypes.getBusinessObjects().get(i);
                    if (!creditTypes.contains(type)) {
						creditTypes.add(type);
						model.addElement(type);
					}
				}
			}
		} else if (e.getSource() == removeLeft) {
			DefaultListModel<AccountType> model = (DefaultListModel<AccountType>) debit.getModel();
			List<AccountType> accountTypeList = debit.getSelectedValuesList();
            for(AccountType type : accountTypeList) {
                debitTypes.remove(type);
                model.removeElement(type);
            }
		} else if (e.getSource() == removeRight) {
			DefaultListModel<AccountType> model = (DefaultListModel<AccountType>) credit.getModel();
            List<AccountType> accountTypeList = credit.getSelectedValuesList();
            for(AccountType type : accountTypeList) {
                creditTypes.remove(type);
                model.removeElement(type);
            }
		}
        ComponentMap.refreshAllFrames();
    }

	public void refresh() {
		repaint();
		DefaultListModel<AccountType> model = (DefaultListModel<AccountType>) credit.getModel();
		model.removeAllElements();
		for(AccountType type : creditTypes) {
			model.addElement(type);
		}
		model = (DefaultListModel<AccountType>) debit.getModel();
		model.removeAllElements();
		for(AccountType type : debitTypes) {
			model.addElement(type);
		}
		model = (DefaultListModel<AccountType>) types.getModel();
		model.removeAllElements();
		for(AccountType type : allTypes) {
			model.addElement(type);
		}
	}
}