package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class ArticlesGUI extends JFrame {
    final ArticlesPanel articlesPanel

    static ArticlesGUI gui = null

    ArticlesGUI() {
        super(getBundle("Accounting").getString("ARTICLES"))
        articlesPanel = new ArticlesPanel()
        setContentPane(articlesPanel)
        pack()
    }

    static ArticlesGUI showArticles() {
        if (gui == null) {
            gui = new ArticlesGUI()
            Main.addFrame(gui)
        }
        gui
    }
}
