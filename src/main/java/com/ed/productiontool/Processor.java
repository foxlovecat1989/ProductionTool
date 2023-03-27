package com.ed.productiontool;

import com.ed.productiontool.app_config.Config;
import com.ed.productiontool.event.RefreshEvent;
import com.ed.productiontool.factory.Bound;
import com.ed.productiontool.factory.JScrollPaneFactory;
import com.ed.productiontool.tool.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import static com.ed.productiontool.app_config.Config.*;
import static com.ed.productiontool.tool.Logger.logMsg;

public class Processor implements ActionListener {
    private final RefreshEvent refreshEvent;
    private JButton submitButton;
    private JButton cleanLogButton;
    private JTextField branchInput;
    private JTextField commitAInput;
    private JTextField commitBInput;

    public Processor(RefreshEvent refreshEvent) {
        this.refreshEvent = refreshEvent;
        initUi();
    }

    private void initUi() {
        JFrame frame = new JFrame("Production Tool");
        frame.setSize(780, 585);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);
        placeComponents(panel);
        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        // input: Branch
        JLabel branchLabel = new JLabel("Branch:");
        branchLabel.setBounds(10, 10, 120, 25);
        panel.add(branchLabel);
        branchInput = new JTextField(20);
        branchInput.setText(DEFAULT_BRANCH_NAME);
        branchInput.setBounds(130, 10, 460, 25);
        panel.add(branchInput);

        // input: CommitA
        JLabel commitALabel = new JLabel("CommitA:");
        commitALabel.setBounds(10, 40, 120, 25);
        panel.add(commitALabel);
        commitAInput = new JTextField(20);
        commitAInput.setText("HEAD^");
        commitAInput.setBounds(130, 40, 460, 25);
        panel.add(commitAInput);

        // input: CommitB
        JLabel commitBLabel = new JLabel("CommitB:");
        commitBLabel.setBounds(10, 70, 120, 25);
        panel.add(commitBLabel);
        commitBInput = new JTextField(20);
        commitBInput.setText("HEAD");
        commitBInput.setBounds(130, 70, 460, 25);
        panel.add(commitBInput);

        // displayArea & JScrollPane
        panel.add(JScrollPaneFactory.getInstance(new Bound(10, 100, 450, 760)));

        // input: PROCESS
        submitButton = new JButton("PROCESS");
        submitButton.setBounds(590, 10, 100, 25);
        submitButton.addActionListener(this);
        panel.add(submitButton);

        // cleanLogButton
        cleanLogButton = new JButton("Clean Log");
        cleanLogButton.setBounds(680, 10, 100, 25);
        cleanLogButton.addActionListener(this);
        panel.add(cleanLogButton);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if (event.getSource() == submitButton)
                processSubmit();
            if (event.getSource() == cleanLogButton)
                processCleanLog();
        } catch (Exception e) {
            logMsg("ERROR: ", e.getMessage());
        }
    }

    private void processCleanLog() {
        Logger.clean();
    }

    private void processSubmit() {
        displayConfigMessages();
        checkConfigPath();
        produceTargetDir();
        produceChangeFile();
        ProduceTool.produceStatisticalGitDiffFile();
        refreshEvent.stateChanged();
    }

    private void displayConfigMessages() {
        logMsg("作業系統為: " + SystemTool.OS_NAME);
        logMsg("專案名稱: " + PROJECT_NAME);
        logMsg("專案目錄開頭位置: " + PROJECT_DIR_PREFIX);
        logMsg("目的地目錄位置: " + TARGET_PATH);
        logMsg("Shell目錄: " + SHELL_DIR);
        logMsg("Branch: " + branchInput.getText());
        logMsg("CommitA: " + commitAInput.getText());
        logMsg("CommitB: " + commitBInput.getText());
    }

    private void produceTargetDir() {
        FileTool.makeDir(new File(Config.getTargetFolderDir()));
    }

    private void checkConfigPath() {
        SystemTool.checkDirExist(PROJECT_DIR_PREFIX);
        SystemTool.checkDirExist(Config.getProjectDir());
        SystemTool.checkDirExist(TARGET_PATH);
        SystemTool.checkDirExist(SHELL_DIR);
        SystemTool.checkFileExist(SHELL_FULL_PATH_OF_PRODUCE_GIT_DIFF_FILE);
    }

    private void produceChangeFile() {
        List<String> shellMessages =
                ShellTool.executeShell(
                        SHELL_FULL_PATH_OF_PRODUCE_GIT_DIFF_FILE,
                        List.of(branchInput.getText(),
                                commitAInput.getText(),
                                commitBInput.getText(),
                                Config.getGitDiffFileFullPath(),
                                Config.getProjectDir()));
        shellMessages.forEach(Logger::logMsg);
        SystemTool.inspectCreateNewFile(Config.getGitDiffFileFullPath());
    }
}
