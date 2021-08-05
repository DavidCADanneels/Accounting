package be.dafke.Accounting.BasicAccounting.PDFGeneration

import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JPanel
import org.icepdf.ri.common.SwingController
import org.icepdf.ri.common.SwingViewBuilder

import javax.swing.JScrollPane
import java.awt.BorderLayout

class PDFViewer extends JPanel {

    PDFViewer() {
        setLayout(new BorderLayout())
        SwingController controller = new SwingController()
        SwingViewBuilder factory = new SwingViewBuilder(controller)
        JPanel panel = factory.buildViewerPanel()
        JScrollPane scrollPane = new JScrollPane(panel)
        add(scrollPane, BorderLayout.CENTER)

        JButton openButton = new JButton("Open file ...")
        openButton.addActionListener({ e ->
            JFileChooser chooser = new JFileChooser()
            chooser.setMultiSelectionEnabled(false)
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                controller.openDocument(chooser.getSelectedFile().toURL())
            }
        })
        add(openButton, BorderLayout.NORTH)
    }
}
