package be.dafke.BasicAccounting.Actions;

import be.dafke.ComponentModel.RefreshableTable;
import be.dafke.ObjectModel.BusinessObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by ddanneel on 18/02/2015.
 */
public class PopupActivator extends MouseAdapter {
    private final RefreshableTable<BusinessObject> tabel;
    private final JPopupMenu popup;
    private ArrayList<Integer> activeColumns;

    public PopupActivator(JPopupMenu popup, RefreshableTable tabel, int ... activeColumns) {
        this.popup = popup;
        this.tabel = tabel;
        this.activeColumns = new ArrayList<Integer>();
        for(int i : activeColumns){
            this.activeColumns.add(Integer.valueOf(i));
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        Point cell = me.getPoint();//
        Point location = me.getLocationOnScreen();
        int col = tabel.columnAtPoint(cell);
        boolean columnOk = activeColumns.isEmpty() || activeColumns.contains(col);
        if (columnOk && me.getButton() == 3) {
            int row = tabel.rowAtPoint(cell);
            tabel.setSelectedRow(row);
            tabel.setSelectedColumn(col);
            popup.show(null, location.x, location.y);
        } else popup.setVisible(false);
    }
}
