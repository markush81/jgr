package org.rosuda.JGR.browser;

import org.rosuda.JGR.JGR;
import org.rosuda.JGR.JGRDataFileSaveDialog;
import org.rosuda.JGR.RController;
import org.rosuda.JGR.robjects.RObject;
import org.rosuda.JGR.toolkit.DataTable;
import org.rosuda.JGR.toolkit.JGRPrefs;

import javax.swing.*;
import java.awt.event.ActionListener;

public class DataFrameNode extends DefaultBrowserNode {
    public DataFrameNode() {
    }

    public DataFrameNode(BrowserNode parent, String rName, String rClass) {
        super(parent, rName, rClass);
    }

    public BrowserNode generate(BrowserNode parent, String rName, String rClass) {
        return new DataFrameNode(parent, rName, rClass);
    }

    public JPopupMenu getPopupMenu() {
        JPopupMenu menu = new JPopupMenu();
        final DataFrameNode pThis = this;
        ActionListener lis = new PopupListener() {
            public void runCmd(String cmd) {
                super.runCmd(cmd);
                if (cmd.equals("Save")) {
                    new JGRDataFileSaveDialog(null, pThis.getRName(), JGRPrefs.workingDirectory);
                }
            }
        };
        JMenuItem item = new JMenuItem("Edit");
        item.addActionListener(lis);
        menu.add(item);
        item = new JMenuItem("Save");
        item.addActionListener(lis);
        menu.add(item);
        menu.add(new JSeparator());
        item = new JMenuItem("Print");
        item.addActionListener(lis);
        menu.add(item);
        item = new JMenuItem("Summary");
        item.addActionListener(lis);
        menu.add(item);
        item = new JMenuItem("Plot");
        item.addActionListener(lis);
        menu.add(item);
        menu.add(new JSeparator());
        item = new JMenuItem("Remove");
        item.addActionListener(lis);
        menu.add(item);

        return menu;
    }

    public void editObject() {
        new Thread(new Runnable() {
            public void run() {
                final RObject o = new RObject(getExecuteableRObjectName(), cls, true);
                final org.rosuda.ibase.SVarSet vs = RController.newSet(o);
                if (vs != null && vs.count() != 0) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            new DataTable(vs, o.getType(), o.isEditable());
                        }

                    });
                }
            }
        }).start();
    }

    public void plotObject() {
        JGR.MAINRCONSOLE.execute("plot(" + getExecuteableRObjectName() + ")");
    }
}
