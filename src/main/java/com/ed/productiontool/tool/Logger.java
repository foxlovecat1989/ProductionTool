package com.ed.productiontool.tool;

import com.ed.productiontool.factory.JScrollPaneFactory;

import javax.swing.*;

public class Logger {
    private static JTextArea displayArea = JScrollPaneFactory.getDisplayArea();

    public static void logMsg(String... msgs) {
        String msg = String.join(" ", msgs);
        displayArea.append(msg + "\n");
    }

    public static void clean() {
        displayArea.setText("");
    }
}
