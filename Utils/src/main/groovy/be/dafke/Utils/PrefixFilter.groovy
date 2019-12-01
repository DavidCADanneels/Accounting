package be.dafke.Utils

import javax.swing.DefaultListModel

class PrefixFilter<K> {
    private final DefaultListModel<K> model
    private final List<K> map

    PrefixFilter(DefaultListModel<K> model, List<K> map){
        this.model = model
        this.map = map
    }

    void filter(String searchString){
        model.removeAllElements()
        for(K o : map) {
            if (o.toString().toLowerCase().startsWith(searchString.toLowerCase())) model.addElement(o)
        }
    }
}
