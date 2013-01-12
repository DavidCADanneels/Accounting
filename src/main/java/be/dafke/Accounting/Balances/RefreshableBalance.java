package be.dafke.Accounting.Balances;

import java.awt.BorderLayout;
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

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.table.AbstractTableModel;

import be.dafke.ParentFrame;
import be.dafke.RefreshableTable;
import be.dafke.Accounting.Objects.Account;
import be.dafke.Accounting.Objects.Account.AccountType;
import be.dafke.Accounting.Objects.Accountings;

public class RefreshableBalance extends RefreshableTable implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String name;
	private final AccountType left, right;
	private final JButton button;

//	private final AccountingGUIFrame parent;

	public RefreshableBalance(String title, AbstractTableModel m, ParentFrame parent, AccountType left,
			AccountType right) {
		super(title, m, parent);
//		this.parent = parent;
		name = title;
		this.left = left;
		this.right = right;
		button = new JButton("Print");
		button.addActionListener(this);
		getContentPane().add(button, BorderLayout.SOUTH);
	}

	public void toXML() {
		if (Accountings.getCurrentAccounting().getBalanceLocation().equals("")) {
			JFileChooser dialoog = new JFileChooser();
			dialoog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			ResourceBundle bundle = ResourceBundle.getBundle("be/dafke/Accounting/Bundle");
			dialoog.setDialogTitle(bundle.getString("OPEN_BALANCES-MAP"));
			int result = dialoog.showSaveDialog(null);
			while (result != JFileChooser.APPROVE_OPTION)
				result = dialoog.showSaveDialog(null);
			Accountings.getCurrentAccounting().setBalanceLocation(dialoog.getSelectedFile());
		}
		String path = Accountings.getCurrentAccounting().getBalanceLocation() + "//" + name + ".xml";
		try {
			Writer writer = new FileWriter(path);
			writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n"
					+ "<?xml-stylesheet type=\"text/xsl\" href=\"Balance.xsl\"?>\r\n" + "<balance name=\"" + name
					+ "\" left=\"" + left + "\" right=\"" + right + "\">\r\n");
			ArrayList<Account> leftAccounts = Accountings.getCurrentAccounting().getAccounts().getAccountsNotEmpty(left);
			ArrayList<Account> rightAccounts = Accountings.getCurrentAccounting().getAccounts().getAccountsNotEmpty(
					right);

			int nrLeft = leftAccounts.size();
			int nrRight = rightAccounts.size();
			int max;
			if (nrLeft > nrRight) {
				max = nrLeft;
			} else {
				max = nrRight;
			}
			for(int i = 0; i < max; i++) {
				writer.write("  <line>\r\n");
				if (i < nrLeft) {
					writer.write("    <name1>" + leftAccounts.get(i).toString() + "</name1>");
					writer.write("    <amount1>" + leftAccounts.get(i).saldo() + "</amount1>");
				} else {
					writer.write("    <name1></name1>");
					writer.write("    <amount1></amount1>");
				}
				if (i < nrRight) {
					writer.write("    <amount2>" + BigDecimal.ZERO.subtract(rightAccounts.get(i).saldo())
							+ "</amount2>");
					writer.write("    <name2>" + rightAccounts.get(i).toString() + "</name2>");
				} else {
					writer.write("    <amount2></amount2>");
					writer.write("    <name2></name2>");
				}
				writer.write("  </line>\r\n");
			}
			writer.write("</balance>");
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