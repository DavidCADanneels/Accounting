package be.dafke.BasicAccounting.MainApplication;

import be.dafke.ComponentModel.RefreshableTable;
import be.dafke.ObjectModel.BusinessObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by ddanneel on 18/02/2015.
 */
public class PopupForTableActivator extends MouseAdapter {
    private final RefreshableTable<BusinessObject> tabel;
    private final JPopupMenu popup;

    public PopupForTableActivator(JPopupMenu popup, RefreshableTable tabel) {
        this.popup = popup;
        this.tabel = tabel;
    }

    public void mouseClicked(MouseEvent me) {
        if (me.getButton() == 3) {
            Point cell = me.getPoint();
            int col = tabel.columnAtPoint(cell);
            int row = tabel.rowAtPoint(cell);
            tabel.setSelectedRow(row);
            tabel.setSelectedColumn(col);
            //
            Point location = me.getLocationOnScreen();
            popup.show(null, location.x, location.y);
        } else popup.setVisible(false);
    }
}
