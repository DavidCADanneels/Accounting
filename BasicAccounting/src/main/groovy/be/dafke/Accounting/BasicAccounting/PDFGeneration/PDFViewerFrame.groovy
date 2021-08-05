package be.dafke.Accounting.BasicAccounting.PDFGeneration

import be.dafke.Accounting.BasicAccounting.MainApplication.Main

import javax.swing.JFrame

import static java.util.ResourceBundle.getBundle

class PDFViewerFrame extends JFrame {

    PDFViewerFrame(String filePath, String fileName) {
        super("${getBundle("Accounting").getString("INVOICE")}: ${fileName}")

        def viewerPanel = new PDFViewerPanel()
        viewerPanel.openFile(filePath)
        setContentPane(viewerPanel)
        pack()
    }

    static PDFViewerFrame showInvoice(String filePath, String fileName) {
        PDFViewerFrame gui = new PDFViewerFrame(filePath, fileName)
        Main.addFrame(gui)
        gui
    }
}
