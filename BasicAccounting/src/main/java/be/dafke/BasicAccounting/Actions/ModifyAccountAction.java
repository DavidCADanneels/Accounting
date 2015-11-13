package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.Objects.Account;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * Created by ddanneel on 15/02/2015.
 */
public abstract class ModifyAccountAction implements Action{
    protected ArrayList<Account> accountList;

    public Object getValue(String key) {
        return null;
    }

    public void putValue(String key, Object value) {
        accountList = (ArrayList<Account>)value;
    }

    public void setEnabled(boolean b) {

    }

    public boolean isEnabled() {
        return true;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {

    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {

    }
}
