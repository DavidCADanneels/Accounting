package be.dafke.Mortgage.GUI;

import be.dafke.BasicAccounting.GUI.AccountingComponentMap;
import be.dafke.BasicAccounting.Objects.AccountType;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.Mortgage.Objects.Mortgage;
import be.dafke.Mortgage.Objects.Mortgages;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class MortgageTable extends RefreshableFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton save;
	private final MortgageDataModel model;
	private final JTable tabel;
	private final BigDecimal startCapital;
	private final Accounting accounting;

    private static int counter = 1;
    protected final int nr;
    private Mortgages mortgages;


    public MortgageTable(Mortgage mortgage, BigDecimal startCapital, Accounting accounting, Mortgages mortgages) {
		super("Aflossingstabel (" + accounting.toString() + " )");
        nr = counter++;
        this.mortgages = mortgages;
		this.accounting = accounting;
		this.startCapital = startCapital;
		model = new MortgageDataModel(mortgage);
		tabel = new JTable(model);
		tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
		JScrollPane scrollPane = new JScrollPane(tabel);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(scrollPane, BorderLayout.CENTER);
		save = new JButton("Save table");
		save.addActionListener(this);
		panel.add(save, BorderLayout.SOUTH);
		setContentPane(panel);
		pack();
		setVisible(true);
	}

	@Override
	public void refresh() {
		model.fireTableDataChanged();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String name = JOptionPane.showInputDialog(this, "Enter a name for the table.");
        Mortgage mortgage = new Mortgage();
        mortgage.setName(name);
        mortgage.setStartCapital(startCapital);
        mortgage.setTable(model.getData());
        AccountType type = accounting.getAccountTypes().getBusinessObject("Mortgage");
        mortgage.setType(type);
        try {
//            accounting.getAccounts().addBusinessObject(mortgage);    // this is implicitely done in the next step
            mortgages.addBusinessObject(mortgage);
            AccountingComponentMap.refreshAllFrames();
            dispose();
        } catch (DuplicateNameException e) {
            JOptionPane.showMessageDialog(this, "There is already a mortgage table with the name \""+name.trim()+"\".\r\n"+
                    "Please provide a new name.");
        } catch (EmptyNameException e) {
            JOptionPane.showMessageDialog(this, "Mortgage name cannot be empty.\r\nPlease provide a new name and/or abbreviation.");
        }
	}
}
