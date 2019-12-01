package be.dafke.Utils

import javax.swing.DefaultListModel

class AlphabeticListModel<K> extends DefaultListModel<K> {
    /**
     * Constructor
     */
    AlphabeticListModel() {
        super()
    }

    /**
     * voegt een element toe aan de verzameling en sorteert de lijst automatisch in alfabetische volgorde
     * <ul>
     * <li><i>(in dit geval staat p voor een Account) --> Accounting programma</i></li>
     * <li><i>sorteert mbv binaire zoek (<sup>O(lg n)</sup>) operatie</i></li>
     * </ul>
     * @param p het toe te voegen Object (<i>hier: de <a href="Accounting/Objects/Account.html">Account</a></i>)
     * @see javax.swing.DefaultListModel#addElement DefaultListModel.addElement
     */
    @Override
    void addElement(K p) {
        if (size() == capacity()) ensureCapacity(size())
        if (isEmpty()) super.addElement(p)
        else {
            Object p1 = elementAt(0)
            Object p2 = elementAt(size() - 1)
            if (p.toString().compareTo(p1.toString()) <= 0) {
                insertElementAt(p, 0)
            } else if (p.toString().compareTo(p2.toString()) >= 0) {
                insertElementAt(p, size())
            } else {
                insert(p, 0, size() - 1)
            }
        }
    }

    /**
     * zoekt de toe te voegen persoon/object in interval [ links , rechts ] (grenzen inbegrepen) indien persoon/object
     * gevonden >> "duplicaat" indien niet vind men de plaats waar de persoon / het object moet worden toegevoegd
     * @see javax.swing.DefaultListModel#add(int index, java.lang.Object element)
     */
    void insert(K nieuw, int links, int rechts) {
        K m
        while (rechts - links > 1) {
            int midden = (links + rechts) / 2
            m = elementAt(midden)
            if (nieuw.toString().compareTo(m.toString()) <= 0) {
                rechts = midden
            } else {
                links = midden
            }
        }
        add(rechts, nieuw)
    }
}
