package be.dafke.BasicAccounting.Projects;

import be.dafke.BusinessModel.Project;
import be.dafke.ComponentModel.RefreshableFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 2/11/2016.
 */
public class ProjectGUI extends RefreshableFrame implements ActionListener {
    private final JButton manage;
    private final JComboBox<Project> combo;
    private Project project;

    public ProjectGUI() {
        super(getBundle("Projects").getString("PROJECTMANAGER"));
        setLayout(new BorderLayout());

        JPanel noord = new JPanel();
        combo = new JComboBox<>();
        combo.addActionListener(this);

        manage = new JButton(getBundle("Projects").getString(
                "PROJECTMANAGER"));
        manage.addActionListener(this);

        noord.add(combo);
        noord.add(manage);
        add(noord, BorderLayout.NORTH);
        pack();
        refresh();
    }

    @Override
    public void refresh() {

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == combo) {
            project = (Project) combo.getSelectedItem();

        }
    }
}
