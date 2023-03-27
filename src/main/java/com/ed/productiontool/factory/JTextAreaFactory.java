package com.ed.productiontool.factory;

import javax.swing.*;
import java.util.Optional;

public class JTextAreaFactory {
    private static JTextArea displayArea;

    private JTextAreaFactory() {
    }

    public static JTextArea getInstance() {
        boolean isPresent = Optional.ofNullable(displayArea).isPresent();
        if (isPresent)
            return displayArea;

        displayArea = new JTextArea();
        displayArea.setVisible(true);

        return displayArea;
    }
}
