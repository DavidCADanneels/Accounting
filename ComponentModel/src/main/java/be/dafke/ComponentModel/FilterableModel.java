package be.dafke.ComponentModel;

import java.util.List;

/**
 * Created by ddanneels on 7/05/2017.
 */
public interface FilterableModel<O> {
    void setCollection(List<O> collection);
//    for list:
//    model.removeAllElements();
//        for (O o : collection) {
//        model.addElement(o);
//    }
}
