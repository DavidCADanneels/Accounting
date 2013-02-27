package be.dafke.Accounting.GUI.Balances;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Account.AccountType;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.RefreshableTable;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RefreshableBalance extends RefreshableTable implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String name;
	private final AccountType left, right;
	private final JButton button;
	private final Accounting accounting;

	public RefreshableBalance(String title, AbstractTableModel m, AccountType left, AccountType right,
			Accounting accounting) {
		super(title, m);
		this.accounting = accounting;
		name = title;
		this.left = left;
		this.right = right;
		button = new JButton("Print"); //$NON-NLS-1$
		button.addActionListener(this);
		getContentPane().add(button, BorderLayout.SOUTH);
	}

	public void toXML() {
		if (accounting.getBalances().getBalanceLocationXml() == null) { //$NON-NLS-1$
			JFileChooser dialoog = new JFileChooser();
			dialoog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			ResourceBundle bundle = ResourceBundle.getBundle("Accounting"); //$NON-NLS-1$
			dialoog.setDialogTitle(bundle.getString("OPEN_BALANCES-MAP")); //$NON-NLS-1$
			int result = dialoog.showSaveDialog(null);
			while (result != JFileChooser.APPROVE_OPTION)
				result = dialoog.showSaveDialog(null);
			accounting.getBalances().setBalanceLocationXml(dialoog.getSelectedFile());
		}
		String xml = accounting.getBalances().getBalanceLocationXml() + "//" + name + ".xml"; //$NON-NLS-1$ //$NON-NLS-2$
		String xsl = accounting.getLocationXSL().getAbsolutePath();
		try {
			Writer writer = new FileWriter(xml);
			writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" //$NON-NLS-1$
					+ "<?xml-stylesheet type=\"text/xsl\" href=\"" + xsl + "Balance.xsl" + "\"?>\r\n" + "<balance>\r\n  <name>" + name //$NON-NLS-1$ //$NON-NLS-2$
					+ "</name>\r\n  <left>" + left + "</left>\r\n  <right>" + right + "</right>\r\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			ArrayList<Account> leftAccounts = accounting.getAccounts().getAccountsNotEmpty(left);
			ArrayList<Account> rightAccounts = accounting.getAccounts().getAccountsNotEmpty(right);

			int nrLeft = leftAccounts.size();
			int nrRight = rightAccounts.size();
			int max;
			if (nrLeft > nrRight) {
				max = nrLeft;
			} else {
				max = nrRight;
			}
			for(int i = 0; i < max; i++) {
				writer.write("  <line>\r\n"); //$NON-NLS-1$
				if (i < nrLeft) {
					writer.write("    <name1>" + leftAccounts.get(i).toString() + "</name1>\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
					writer.write("    <amount1>" + leftAccounts.get(i).saldo() + "</amount1>\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					writer.write("    <name1></name1>\r\n"); //$NON-NLS-1$
					writer.write("    <amount1></amount1>\r\n"); //$NON-NLS-1$
				}
				if (i < nrRight) {
					writer.write("    <amount2>" + BigDecimal.ZERO.subtract(rightAccounts.get(i).saldo()) //$NON-NLS-1$
							+ "</amount2>\r\n"); //$NON-NLS-1$
					writer.write("    <name2>" + rightAccounts.get(i).toString() + "</name2>\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					writer.write("    <amount2></amount2>\r\n"); //$NON-NLS-1$
					writer.write("    <name2></name2>\r\n"); //$NON-NLS-1$
				}
				writer.write("  </line>\r\n"); //$NON-NLS-1$
			}
			writer.write("</balance>"); //$NON-NLS-1$
			writer.flush();
			writer.close();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		toXML();
	}
}