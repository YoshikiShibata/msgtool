// File: WarningUI.java - last edit:
// Yoshiki Shibata  13-Nov-2004

// Copyright (c) 1997 - 1999, 2004 by Yoshiki Shibata. All rights reserved.

package msgtool.awt;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import msgtool.common.Context;
import msgtool.util.ComponentUtil;
import msgtool.util.StringDefs;

@SuppressWarnings("serial")
public class WarningUI extends Dialog {
	
	public WarningUI(Frame parent, String title, String message) {
		super(parent, title, true);

		Button okButton = new Button(StringDefs.OK);
		okButton.setFont(Context.getFont());
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
			}
		});

		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints gridBagC = new GridBagConstraints();
		setLayout(gridBag);

		TextArea warningMsg = new TextArea(message, 3, 40,
				TextArea.SCROLLBARS_VERTICAL_ONLY);
		warningMsg.setEditable(false);
		warningMsg.setFont(Context.getFont());

		gridBagC.anchor = GridBagConstraints.WEST;
		gridBagC.fill = GridBagConstraints.BOTH;
		gridBagC.gridwidth = GridBagConstraints.REMAINDER;
		gridBagC.weightx = 1.0;
		gridBagC.weighty = 1.0;
		gridBagC.insets.top = 16;
		gridBagC.insets.left = 16;
		gridBagC.insets.bottom = 0;
		gridBagC.insets.right = 16; // (16, 16, 0, 16)
		gridBag.setConstraints(warningMsg, gridBagC);
		add(warningMsg);

		Panel panel = new Panel();
		panel.add(okButton);

		gridBagC.weighty = 0.0;
		gridBagC.anchor = GridBagConstraints.EAST;
		gridBagC.insets.top = 0;
		gridBagC.insets.left = 0;
		gridBagC.insets.right = 0; // (0,0,0,0)
		gridBag.setConstraints(panel, gridBagC);
		add(panel);

		pack();
		/*
		 * place this window on the top of the parent frame.
		 */
		ComponentUtil.centerComponent(this, 
				new Point(0, 0), 
				getToolkit().getScreenSize());
	}
}

// LOG
// 1.80 :  6-Dec-97 Y.Shibata created.
// 1.94 :  5-Jul-98 Y.Shibata modified to use msgtool.util.Component
// 2.22 : 11-Apr-99 Y.Shibata modified not to new Insets
// 2.35 : 24-Oct-99 Y.Shibata WarningDialog -> WarningUI
// 2.52 : 13-Nov-04	Y.Shibata refactored
