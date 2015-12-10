package be.dafke.BasicAccounting.Actions;

import be.dafke.ObjectModel.BusinessObject;

import javax.swing.JList;
import javax.swing.JPopupMenu;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by ddanneel on 18/02/2015.
 */
public class PopupForListActivator extends MouseAdapter {
    private final JList<? extends BusinessObject> list;
    private final JPopupMenu popup;
    private ActionListener defaultAction;

    public PopupForListActivator(JPopupMenu popup, JList<? extends BusinessObject> list) {
        this(popup, list, null);
    }

    public PopupForListActivator(JPopupMenu popup, JList<? extends BusinessObject> list, ActionListener defaultAction) {
        this.popup = popup;
        this.list = list;
        this.defaultAction = defaultAction;
    }


    public void mouseClicked(MouseEvent me) {
        // TODO: if double-clicked: execute default action
        if(defaultAction!=null && me.getClickCount()==2){
            defaultAction.actionPerformed(null);
        }else {
            Point location = me.getLocationOnScreen();
            if (me.getButton() == 3 && list.getSelectedIndex() != -1) {
                popup.show(null, location.x, location.y);
            } else {
                popup.setVisible(false);
            }
        }
    }
}
