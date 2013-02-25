package be.dafke.Accounting.GUI.MortgageManagement;

import be.dafke.Accounting.GUI.ComponentMap;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Mortgage.Mortgage;
import be.dafke.RefreshableFrame;

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
	private final Accountings accountings;

    private static int nr = 1;

	public MortgageTable(Mortgage mortgage, BigDecimal startCapital, Accountings accountings) {
		super("Aflossingstabel "+ nr++);
		this.accountings = accountings;
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
		Accounting accounting = accountings.getCurrentAccounting();
		String name = JOptionPane.showInputDialog(this, "Enter a name for the table.");
		while (accounting.containsMortgageName(name)) {
			name = JOptionPane.showInputDialog(this, "This name is already used. Enter another name.");
		}
		if (name != null) {
			Mortgage mortgage = new Mortgage(name, startCapital);
//			mortgage.setAccounting(accounting);
			mortgage.setTable(model.getData());
			accounting.addMortgageTable(name, mortgage);
            ComponentMap.refreshAllFrames();
            dispose();
		}
	}
}
