package com.ed.productiontool.factory;

import javax.swing.*;
import java.util.Optional;

public class JScrollPaneFactory {
    private static JTextArea jTextArea;
    private static JScrollPane jScrollPane;

    private JScrollPaneFactory() {
    }

    public static JScrollPane getInstance(Bound bound) {
        boolean isPresent = Optional.ofNullable(jScrollPane).isPresent();
        if (isPresent)
            return jScrollPane;
        jTextArea = JTextAreaFactory.getInstance();
        jScrollPane = new JScrollPane(jTextArea);
        jScrollPane.setBounds(bound.getX(), bound.getY(), bound.getWidth(), bound.getHeight());
        jScrollPane.setVisible(true);

        return jScrollPane;
    }

    public static JTextArea getDisplayArea() {
        if (Optional.ofNullable(jTextArea).isEmpty())
            throw new IllegalStateException("DisplayArea尚未初始化");

        return jTextArea;
    }
}
