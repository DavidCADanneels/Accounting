package be.dafke;

import javax.swing.*;
import java.util.List;

/**
 * User: Dafke
 * Date: 14/01/13
 * Time: 17:48
 */
public class PrefixFilter<K> {
    private final DefaultListModel<K> model;
    private final List<K> map;

    public PrefixFilter(DefaultListModel<K> model, List<K> map){
        this.model = model;
        this.map = map;
    }

    public void filter(String searchString){
        model.removeAllElements();
        for(K o : map) {
            if (o.toString().startsWith(searchString)) model.addElement(o);
        }
    }
}
