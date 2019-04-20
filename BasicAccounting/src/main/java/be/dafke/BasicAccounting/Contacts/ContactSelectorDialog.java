package be.dafke.BasicAccounting.Contacts;

import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.RefreshableDialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ContactSelectorDialog extends RefreshableDialog {
	private final JButton create, ok;
	private Contact contact;
	private Contact.ContactType contactType;
	private final JComboBox<Contact> combo;
    private final DefaultComboBoxModel<Contact> model;
	private Contacts contacts;
	private static ContactSelectorDialog contactSelectorDialog = null;

	private ContactSelectorDialog(Contacts contacts, Contact.ContactType contactType) {
		super("Select Contact");
		this.contactType = contactType;
		model = new DefaultComboBoxModel<>();
		combo = new JComboBox<>(model);
		combo.addActionListener(e -> contact = (Contact) combo.getSelectedItem());
		create = new JButton("Add contact(s) ...");
		create.addActionListener(e -> new NewContactDialog(contacts).setVisible(true));
		ok = new JButton("Ok (Close popup)");
		ok.addActionListener(e -> dispose());
		JPanel innerPanel = new JPanel(new BorderLayout());
		JPanel panel = new JPanel();
		panel.add(combo);
		panel.add(create);
		innerPanel.add(panel, BorderLayout.CENTER);
		innerPanel.add(ok, BorderLayout.SOUTH);
		setContentPane(innerPanel);
//		setContacts(contacts);
		pack();
	}

	public static ContactSelectorDialog getContactSelector(Contacts contacts, Contact.ContactType contactType){
		if(contactSelectorDialog==null){
			contactSelectorDialog = new ContactSelectorDialog(contacts, contactType);
		}
		contactSelectorDialog.setContacts(contacts);
		return contactSelectorDialog;
	}

	public Contact getSelection() {
		return contact;
	}

    public void setContacts(Contacts contacts) {
		this.contacts = contacts;
		fireContactDataChanged();
    }

	public static void fireContactDataChangedForAll() {
		if(contactSelectorDialog !=null){
			contactSelectorDialog.fireContactDataChanged();
		}
	}

	public void fireContactDataChanged() {
		model.removeAllElements();
		List<Contact> list = null;
		if(contactType == Contact.ContactType.ALL){
			list = contacts.getBusinessObjects();
		} else if (contactType == Contact.ContactType.CUSTOMERS){
			list = contacts.getBusinessObjects(Contact::isCustomer);
		} else if (contactType == Contact.ContactType.SUPPLIERS){
			list = contacts.getBusinessObjects(Contact::isSupplier);
		}
		if (list!=null) {
			for (Contact contact : list) {
				model.addElement(contact);
			}
		}
		invalidate();
		combo.invalidate();
	}
}
