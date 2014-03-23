/*
 * File: Deliverer.java - last edit:
 * Yoshiki Shibata 30-Oct-99
 *
 * Copyright (c) 1997, 1999 Yoshiki Shibata. All rights reserved.
 */
package msgtool;

import msgtool.ui.DedicatedUI;
import msgtool.ui.InputArea;

public interface Deliverer {

    void deliver(String toList, InputArea inputArea);

    void deliver(String toList, InputArea inputArea, DedicatedUI originator);

}
// --- 1.46 ---
// 9-Aug-97 Y.Shibata created
// 2.35 : 24-Oct-99 Y.Shibata DedicatedDialog -> DedicatedUI
// toList is redefined as String
