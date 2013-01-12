package be.dafke.Accounting;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import be.dafke.ParentFrame;
import be.dafke.RefreshableFrame;
import be.dafke.Accounting.Objects.Account.AccountType;

public class NewJournalTypeGUI extends RefreshableFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JList debit, credit, types;
	private final JButton addLeft, addRight, removeLeft, removeRight;
	private final ArrayList<AccountType> debitTypes, creditTypes;
	private final DefaultListModel debitModel, creditModel;

	private static NewJournalTypeGUI newJournalTypeGUI = null;

	public static NewJournalTypeGUI getInstance(ParentFrame parent) {
		if (newJournalTypeGUI == null) {
			newJournalTypeGUI = new NewJournalTypeGUI(parent);
		}
		parent.addChildFrame(newJournalTypeGUI);
		return newJournalTypeGUI;
	}

	private NewJournalTypeGUI(ParentFrame parent) {
		super("Create and modify journal types", parent);
		debitTypes = new ArrayList<AccountType>();
		creditTypes = new ArrayList<AccountType>();
		debitModel = new DefaultListModel();
		debit = new JList(debitModel);
		creditModel = new DefaultListModel();
		credit = new JList(creditModel);
		types = new JList(AccountType.values());
		addLeft = new JButton("Add type to Debit types");
		addRight = new JButton("Add type to Credit types");
		removeLeft = new JButton("Remove type from Debit types");
		removeRight = new JButton("Remove type from Credit types");
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
		west.add(new JLabel("Debit Types"), BorderLayout.NORTH);
		JScrollPane debitScroll = new JScrollPane(debit);
		west.add(debitScroll, BorderLayout.CENTER);
		east.add(new JLabel("Credit Types"), BorderLayout.NORTH);
		JScrollPane creditScroll = new JScrollPane(credit);
		east.add(creditScroll, BorderLayout.CENTER);
		center.add(new JLabel("All Types"), BorderLayout.NORTH);
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

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(panel);
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addLeft) {
			DefaultListModel model = (DefaultListModel) debit.getModel();
			int rows[] = types.getSelectedIndices();
			if (rows.length != 0) {
				for(int i : rows) {
					AccountType type = AccountType.values()[i];
					if (!debitTypes.contains(type)) {
						debitTypes.add(type);
						model.addElement(type);
					}
				}
			}
		} else if (e.getSource() == addRight) {
			DefaultListModel model = (DefaultListModel) credit.getModel();
			int rows[] = types.getSelectedIndices();
			if (rows.length != 0) {
				for(int i : rows) {
					AccountType type = AccountType.values()[i];
					if (!creditTypes.contains(type)) {
						creditTypes.add(type);
						model.addElement(type);
					}
				}
			}
		} else if (e.getSource() == removeLeft) {
			DefaultListModel model = (DefaultListModel) debit.getModel();
			Object[] accountTypes = debit.getSelectedValues();
			if (accountTypes.length != 0) {
				for(Object type : accountTypes) {
					debitTypes.remove(type);
					model.removeElement(type);
				}
			}
		} else if (e.getSource() == removeRight) {
			DefaultListModel model = (DefaultListModel) credit.getModel();
			Object[] accountTypes = credit.getSelectedValues();
			if (accountTypes.length != 0) {
				for(Object type : accountTypes) {
					creditTypes.remove(type);
					model.removeElement(type);
				}
			}
		}
		refresh();
	}

	@Override
	public void refresh() {
		repaint();
		DefaultListModel model = (DefaultListModel) credit.getModel();
		model.removeAllElements();
		for(AccountType type : creditTypes) {
			model.addElement(type);
		}
		model = (DefaultListModel) debit.getModel();
		model.removeAllElements();
		for(AccountType type : debitTypes) {
			model.addElement(type);
		}
	}
}