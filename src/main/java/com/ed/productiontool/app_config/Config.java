package com.ed.productiontool.app_config;

import com.ed.productiontool.factory.FolderCreateFactory;
import com.ed.productiontool.tool.SystemTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {
    public static String LINUX_BACKWARD_SLASH;
    public static String WINDOWS_FORWARD_SLASH;
    public static String WINDOWS_CMD;
    public static String LINUX_SH;
    public static String PROJECT_DIR_PREFIX;
    public static String TARGET_PATH;
    public static String PROJECT_NAME;
    public static String SHELL_DIR;
    public static String DEFAULT_BRANCH_NAME;
    public static String DIFF_FILENAME;
    public static String GIT_DIFF_SHELL_FILENAME;
    public static String SHELL_FULL_PATH_OF_PRODUCE_GIT_DIFF_FILE;
    public static String CHANGE_STATISTICAL_FILENAME;
    public static String JAR_NAME;

    @Value("${com.ed.application.system.config.linuxBackwardSlash}")
    public void setLinuxBackwardSlash(String value) {
        LINUX_BACKWARD_SLASH = value;
    }

    @Value("${com.ed.application.system.config.windowsForwardSlash}")
    public void setWindowsForwardSlash(String value) {
        WINDOWS_FORWARD_SLASH = value;
    }

    @Value("${com.ed.application.system.config.windowsCmd}")
    public void setWindowsCmd(String value) {
        WINDOWS_CMD = value;
    }

    @Value("${com.ed.application.system.config.linuxSh}")
    public void setLinuxSh(String value) {
        LINUX_SH = value;
    }

    @Value("${com.ed.application.system.config.projectDirPrefix}")
    public void setProjectDirPrefix(String value) {
        PROJECT_DIR_PREFIX = value;
    }

    @Value("${com.ed.application.system.config.targetPath}")
    public void setTargetPath(String value) {
        TARGET_PATH = value;
    }

    @Value("${com.ed.application.system.config.projectName}")
    public void setProjectName(String value) {
        PROJECT_NAME = value;
    }

    @Value("${com.ed.application.system.config.shellDir}")
    public void setShellDir(String value) {
        SHELL_DIR = value;
    }

    @Value("${com.ed.application.system.config.defaultBranchName}")
    public void setDefaultBranchName(String value) {
        DEFAULT_BRANCH_NAME = value;
    }

    @Value("${com.ed.application.system.config.diffFilename}")
    public void setDiffFilename(String value) {
        DIFF_FILENAME = value;
    }

    @Value("${com.ed.application.system.config.gitDiffShellFileName}")
    public void getGitDiffShellFilename(String value) {
        GIT_DIFF_SHELL_FILENAME = value;
    }

    @Value("${com.ed.application.system.config.shellFullPathOfProduceGitDiffFile}")
    public void setShellFullPathOfProduceGitDiffFile(String value) {
        SHELL_FULL_PATH_OF_PRODUCE_GIT_DIFF_FILE = value;
    }

    @Value("${com.ed.application.system.config.changeStatisticalFilename}")
    public void setChangeStatisticalFilename(String value) {
        CHANGE_STATISTICAL_FILENAME = value;
    }

    @Value("${com.ed.application.system.config.jarName}")
    public void setJarName(String value) {
        JAR_NAME = value;
    }

    public static String getProjectDir() {
        return String.format("%s%s%s", PROJECT_DIR_PREFIX, PROJECT_NAME, getPathSlash());
    }

    public static String getPathSlash() {
        return SystemTool.isWindows() ? WINDOWS_FORWARD_SLASH : LINUX_BACKWARD_SLASH;
    }

    public static String getTargetFolderDir() {
        return String.format("%s%s%s", TARGET_PATH, FolderCreateFactory.getFolderName(), getPathSlash());
    }

    public static String getGitDiffFileFullPath() {
        return String.format("%s%s%s%s", TARGET_PATH, FolderCreateFactory.getFolderName(), getPathSlash(), DIFF_FILENAME);
    }

    public static String getCommand() {
        return Boolean.TRUE.equals(SystemTool.isWindows()) ? WINDOWS_CMD : LINUX_SH;
    }
}

