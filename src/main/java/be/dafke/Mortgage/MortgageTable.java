package be.dafke.Mortgage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import be.dafke.ParentFrame;
import be.dafke.RefreshableFrame;
import be.dafke.Accounting.Objects.Accountings;

public class MortgageTable extends RefreshableFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton save;
//	private final AccountingGUIFrame parent;
	private final MortgageDataModel model;
	private final JTable tabel;

	public MortgageTable(ParentFrame parent, Mortgage mortgage) {
		super("Aflossingstabel", parent);
//		this.parent = parent;
		model = new MortgageDataModel(mortgage);
		tabel = new JTable(model);
		tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
		JScrollPane scrollPane = new JScrollPane(tabel);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(scrollPane, BorderLayout.CENTER);
		save = new JButton("Save table");
		save.addActionListener(this);
		panel.add(save, BorderLayout.SOUTH);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
		String name = JOptionPane.showInputDialog(parent, "Enter a name for the table.");
		while (Accountings.getCurrentAccounting().containsMortgageName(name)) {
			name = JOptionPane.showInputDialog(parent, "This name is already used. Enter another name.");
		}
		if (name != null) {
			Mortgage m = new Mortgage(name, model.getData());
			Accountings.getCurrentAccounting().addMortgageTable(name, m);
			parent.repaintAllFrames();
			dispose();
		}
	}
}
