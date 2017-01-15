package be.dafke.BasicAccounting.Contacts;

import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.RefreshableDialog;

import javax.swing.*;
import java.awt.*;

public class ContactSelector extends RefreshableDialog {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final JButton create, ok;
	private Contact contact;
	private final JComboBox<Contact> combo;
    private final DefaultComboBoxModel<Contact> model;
	private Contacts contacts;
	private static ContactSelector contactSelector = null;

	private ContactSelector(Contacts contacts) {
		super("Select Contact");
		model = new DefaultComboBoxModel<>();
		combo = new JComboBox<>(model);
		combo.addActionListener(e -> contact = (Contact) combo.getSelectedItem());
		create = new JButton("Add contact(s) ...");
		create.addActionListener(e -> new NewContactGUI(contacts).setVisible(true));
		ok = new JButton("Ok (Close popup)");
		ok.addActionListener(e -> dispose());
		JPanel innerPanel = new JPanel(new BorderLayout());
		JPanel panel = new JPanel();
		panel.add(combo);
		panel.add(create);
		innerPanel.add(panel, BorderLayout.CENTER);
		innerPanel.add(ok, BorderLayout.SOUTH);
		setContentPane(innerPanel);
		setContacts(contacts);
		pack();
	}

	public static ContactSelector getContactSelector(Contacts contacts){
		if(contactSelector ==null){
			contactSelector = new ContactSelector(contacts);
		}
		return contactSelector;
	}

	public Contact getSelection() {
		return contact;
	}

    public void setContacts(Contacts contacts) {
		this.contacts = contacts;
		fireContactDataChanged();
    }

	public static void fireContactDataChangedForAll() {
		if(contactSelector !=null){
			contactSelector.fireContactDataChanged();
		}
	}

	public void fireContactDataChanged() {
		model.removeAllElements();
		for (Contact contact:contacts.getBusinessObjects()) {
			model.addElement(contact);
		}
		invalidate();
		combo.invalidate();
	}
}
