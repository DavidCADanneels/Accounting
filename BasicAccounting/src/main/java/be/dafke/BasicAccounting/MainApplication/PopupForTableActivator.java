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

    public PopupForTableActivator(JPopupMenu popup, RefreshableTable tabel){
        this.popup = popup;
        this.tabel = tabel;
    }

    public void mouseClicked(MouseEvent me) {
        if (me.getButton() == 3) {
            Point cell = me.getPoint();
            int col = tabel.columnAtPoint(cell);
            int row = tabel.rowAtPoint(cell);

            ListSelectionModel selectionModel = tabel.getSelectionModel();
            int minSelectionIndex = selectionModel.getMinSelectionIndex();
            int maxSelectionIndex = selectionModel.getMaxSelectionIndex();
            if(minSelectionIndex<=row && row <=maxSelectionIndex){
                // keep selection
            } else {
                selectionModel.setSelectionInterval(row, row);
            }
            //
            // TODO: close all other popups first (popups of any component)
            popup.setLocation(me.getLocationOnScreen());
            popup.setVisible(true);

        } else popup.setVisible(false);
    }
}
