package be.dafke.Utils

import javax.swing.DefaultListModel
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import java.awt.BorderLayout
import java.awt.Graphics
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent

import static java.util.ResourceBundle.getBundle

class PrefixFilterPanel<K> extends JPanel {
    private JScrollPane scrol
    protected JTextField zoekveld
    private PrefixFilter<K> filter
    private DefaultListModel<K> model

    /**
     * Constructor with model, panel and map
     * @param model het model van de list
     * @param list the list
     * @param map map met de gegevens uit de list
     */
    PrefixFilterPanel(DefaultListModel<K> model, JList<K> list, List<K> map) {
        this.model = model
        filter = new PrefixFilter<K>(model, map)
        scrol = new JScrollPane(list)
        zoekveld = new JTextField(20)
        zoekveld.getDocument().addDocumentListener(new DocumentListener() {
            void changedUpdate(DocumentEvent e) {
                repaint()
            }
            void insertUpdate(DocumentEvent e) {
                repaint()
            }
            void removeUpdate(DocumentEvent e) {
                repaint()
            }
        })

        zoekveld.addFocusListener(new FocusAdapter() {
            @Override
            void focusGained(FocusEvent e) {
                zoekveld.selectAll()
            }
        })

        JPanel zoekpaneel = new JPanel()
        zoekpaneel.add(new JLabel(getBundle("Utils").getString("SEARCH")))
        zoekpaneel.add(zoekveld)
        setLayout(new BorderLayout())
        add(scrol, BorderLayout.CENTER)
        add(zoekpaneel, BorderLayout.NORTH)
    }

    /**
     * Vervangt de datamap en hertekent het JPanel
     * @param newMap de nieuwe map
     * @since 01/10/2010
     */
    void resetMap(List<K> newMap) {
        filter = new PrefixFilter<K>(model,newMap)
        repaint()
    }

    /**
     * verwijdert alle objecten uit de view en voegt enkel de objecten toe, waarvan de String-voorstelling begint met de
     * letters in het tekstveld
     */
    @Override
    void paintComponent(Graphics g) {
        filter.filter(zoekveld.getText())
        super.paintComponent(g)
    }
}
