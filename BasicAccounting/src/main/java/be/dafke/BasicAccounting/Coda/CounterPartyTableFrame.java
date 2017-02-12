package be.dafke.BasicAccounting.Coda;

import be.dafke.BasicAccounting.Accounts.AccountSelector;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;
import be.dafke.ObjectModel.BusinessObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.regex.Pattern;

public class CounterPartyTableFrame extends JFrame implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private final Statements statements;
    private Accounts accounts;
    private AccountTypes accountTypes;
    private JTable tabel;
    private CounterPartyDataModel dataModel;
    private static final HashMap<CounterParties, CounterPartyTableFrame> counterpartiesGuis = new HashMap<>();

    private CounterPartyTableFrame(CounterParties counterParties, Statements statements) {
		super("Counterparties");
        this.statements = statements;

        dataModel = new CounterPartyDataModel(counterParties);

        tabel = new JTable(dataModel);
        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
        //tabel.setAutoCreateRowSorter(true);
        tabel.setRowSorter(null);
        JScrollPane scrollPane = new JScrollPane(tabel);
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setContentPane(contentPanel);
        pack();

		// tabel.setAutoCreateRowSorter(true);
		tabel.addMouseListener(this);
	}

    public static CounterPartyTableFrame showStatements(Statements statements, CounterParties counterParties) {
        CounterPartyTableFrame gui = counterpartiesGuis.get(counterParties);
        if(gui == null){
            gui = new CounterPartyTableFrame(counterParties, statements);
            counterpartiesGuis.put(counterParties,gui);
            Main.addFrame(gui);
        }
        return gui;
    }

	public void setAccounting(Accounting accounting){
        accounts = accounting.getAccounts();
        accountTypes = accounting.getAccountTypes();
    }

    public void refresh() {
//		tabel.refresh();
        dataModel.fireTableDataChanged();
    }

	public void mouseClicked(MouseEvent me) {
		Point cell = me.getPoint();
//		Point location = me.getLocationOnScreen();
		if (me.getButton() == 3) {
			int col = tabel.columnAtPoint(cell);
			int row = tabel.rowAtPoint(cell);
			if (col == 0) {
				CounterParty counterParty = (CounterParty) tabel.getValueAt(row, col);
				System.out.println(counterParty.getName());
				for(BankAccount account : counterParty.getBankAccounts().values()) {
					System.out.println(account.getAccountNumber());
					System.out.println(account.getBic());
					System.out.println(account.getCurrency());
				}
                SearchOptions searchOptions = new SearchOptions();
                searchOptions.setCounterParty(counterParty);
                searchOptions.setSearchOnCounterParty(true);
				new GenericStatementTableFrame(searchOptions, statements).setVisible(true);
				// parent.addChildFrame(refreshTable);
            } else if (col == 1){
                String alias = (String) tabel.getValueAt(row, col);
                if(alias != null && !alias.equals("")){
                    String aliases[] = alias.split(Pattern.quote(" | "));
                    int result = JOptionPane.showOptionDialog(this,"Select new name", "Select new name",
                            JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE, null,aliases,aliases[0]);
                    if(result != JOptionPane.CLOSED_OPTION){
                        CounterParty counterParty = (CounterParty) tabel.getValueAt(row, 0);
                        String name = counterParty.getName();
                        counterParty.setName(aliases[result]);
                        counterParty.removeAlias(aliases[result]);
                        // TODO: ask user if old name should be saved as alias
                        counterParty.addAlias(name);
                        ((SelectableTableModel<BusinessObject>)tabel.getModel()).fireTableDataChanged();
                    }
                }
			} else if (col == 5) {
                AccountSelector sel = AccountSelector.getAccountSelector(accounts, accountTypes);
                sel.setVisible(true);
                Account account = sel.getSelection();

                if (account != null) {
                    CounterParty counterParty = (CounterParty) tabel.getValueAt(row, 0);
                    counterParty.setAccount(account);
                    refresh();
                }
			}

		}
	}

    public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

//    @Override
//    public void selectObject(CounterParty counterParty) {
//
//    }
//
//    @Override
//    public CounterParty getSelectedObject() {
//        return null;
//    }
}
