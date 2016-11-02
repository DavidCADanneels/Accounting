package be.dafke.BasicAccounting.Coda;

import be.dafke.BusinessModel.SearchOptions;
import be.dafke.BusinessModel.Statement;
import be.dafke.BusinessModel.Statements;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.*;

public class GenericStatementTableFrame extends RefreshableFrame {

	private static final long serialVersionUID = 1L;
    private RefreshableTable<Statement> tabel;
    private GenericStatementDataModel dataModel;

	public GenericStatementTableFrame(SearchOptions searchOptions,
                                      Statements statements) {
		super("Statements where"+
                (searchOptions.isSearchOnCounterParty()?" [counterParty = "+searchOptions.getCounterParty()+"]":"")+
                (searchOptions.isSearchOnTransactionCode()? " [transactioncode = "+searchOptions.getTransactionCode()+"]":"")+
                (searchOptions.isSearchOnCommunication()? " [communication = "+searchOptions.getCommunication()+"]":""));
        dataModel = new GenericStatementDataModel(searchOptions,statements);

        tabel = new RefreshableTable<>(dataModel);
        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
        //tabel.setAutoCreateRowSorter(true);
        tabel.setRowSorter(null);
        JScrollPane scrollPane = new JScrollPane(tabel);
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setContentPane(contentPanel);
        pack();
	}

    public void refresh() {
//		tabel.refresh();
        dataModel.fireTableDataChanged();
    }
}
