package com.ed.productiontool.tool;

import com.ed.productiontool.app_config.Config;
import com.ed.productiontool.enums.GitStatusEnum;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.ed.productiontool.app_config.Config.getGitDiffFileFullPath;
import static com.ed.productiontool.enums.GitStatusEnum.*;
import static com.ed.productiontool.tool.Logger.logMsg;

@Component
public class ProduceTool {
    public static final String DIR_OF_JAVA_FILES = "java-files" + Config.getPathSlash();
    public static final String NEXT_LINE = "\n";
    public static final String HORIZONTAL_LINE = "---------------------------------------------------------------------------------------";

    private ProduceTool() {
    }

    public static void produceStatisticalGitDiffFile() {
        Map<GitStatusEnum, List<String>> datas =
                StatisticalGitDiffTool.statisticGitDiff(getGitDiffFileFullPath());
        List<String> pendingPaths = getDiffFilePaths(datas);
        // 1. 產生檔案至java-files目錄下
        pendingPaths.forEach(ProduceTool::copyDiffJavaFiles);
        logMsg("1. 產生檔案至java-files目錄下: 成功");
        // 2. 建立異動目錄
        pendingPaths.forEach(ProduceTool::createDiffFileFullPath);
        logMsg("2. 建立異動目錄: 成功");
        // 3. 異動清單
        String targetPath = String.format("%s%s", Config.getTargetFolderDir(), Config.CHANGE_STATISTICAL_FILENAME);
        createChangeStatisticalFile(datas, targetPath);
        SystemTool.inspectCreateNewFile(targetPath);
        logMsg("3. 建立異動清單: 成功");
        // 4. copy .jar
        String jarTargetPath = copyJar();
        SystemTool.inspectCreateNewFile(jarTargetPath);
        logMsg("4. copy .jar: 成功");
    }

    private static List<String> getDiffFilePaths(Map<GitStatusEnum, List<String>> statisticalDatas) {
        List<String> addPaths = statisticalDatas.get(ADD);
        List<String> modifiedPaths = statisticalDatas.get(MODIFIED);
        List<String> deletePaths = statisticalDatas.get(DELETE);
        List<String> errorPaths = statisticalDatas.get(ERROR);
        List<String> pendingPaths = new ArrayList<>();
        pendingPaths.addAll(addPaths);
        pendingPaths.addAll(modifiedPaths);
        pendingPaths.addAll(deletePaths);
        pendingPaths.addAll(errorPaths);

        return pendingPaths;
    }

    private static void createChangeStatisticalFile(Map<GitStatusEnum, List<String>> datas, String targetPath) {
        List<String> addPaths = datas.get(ADD);
        List<String> modifiedPaths = datas.get(MODIFIED);
        List<String> deletePaths = datas.get(DELETE);
        List<String> errorPaths = datas.get(ERROR);

        String addFileContent = String.join(NEXT_LINE, addPaths);
        String modifiedFileContent = String.join(NEXT_LINE, modifiedPaths);
        String deleteFileContent = String.join(NEXT_LINE, deletePaths);
        String errorFileContent = String.join(NEXT_LINE, errorPaths);
        int total = addPaths.size() + modifiedPaths.size() + deletePaths.size() + errorPaths.size();

        String content = new StringBuilder()
                .append("==異動清單統計===========================================================================").append(NEXT_LINE)
                .append("異動總數量: ").append(total).append(NEXT_LINE)
                .append("新增數量: ").append(addPaths.size()).append(NEXT_LINE)
                .append("修改數量: ").append(modifiedPaths.size()).append(NEXT_LINE)
                .append("刪除數量: ").append(deletePaths.size()).append(NEXT_LINE)
                .append(HORIZONTAL_LINE).append(NEXT_LINE)
                .append("新增檔案列表: ").append(NEXT_LINE)
                .append(addFileContent).append(NEXT_LINE)
                .append(HORIZONTAL_LINE).append(NEXT_LINE)
                .append("修改檔案列表: ").append(NEXT_LINE)
                .append(modifiedFileContent).append(NEXT_LINE)
                .append(HORIZONTAL_LINE).append(NEXT_LINE)
                .append("刪除檔案列表: ").append(NEXT_LINE)
                .append(deleteFileContent).append(NEXT_LINE)
                .append(HORIZONTAL_LINE).append(NEXT_LINE)
                .append("錯誤狀態檔案列表: ").append(NEXT_LINE)
                .append(errorFileContent).append(NEXT_LINE)
                .append(HORIZONTAL_LINE).append(NEXT_LINE)
                .toString();

        FileTool.writeFile(content, targetPath);
    }


    private static String copyJar() {
        String jarPath = String.format("target%s%s", Config.getPathSlash(), Config.JAR_NAME);
        String sourcePath = Config.getProjectDir() + jarPath;
        String targetPath = String.format("%s%s", Config.getTargetFolderDir(), Config.JAR_NAME);

        if (!new File(sourcePath).exists())
            throw new IllegalStateException(String.format("File not exist: <%s>", sourcePath));

        try {
            FileTool.copyFile(sourcePath, targetPath);
        } catch (IOException e) {
            String errorMsg = String.format("copy .jar時發生錯誤: <%s>", sourcePath);
            logMsg(errorMsg);
            throw new IllegalStateException(errorMsg);
        }

        new File(targetPath).setLastModified(new Date().getTime());

        return targetPath;
    }

    private static void createDiffFileFullPath(String pendingPath) {
        String sourceFullPath = Config.getProjectDir() + pendingPath;
        String targetFullPath = Config.getTargetFolderDir() + Config.PROJECT_NAME + Config.getPathSlash() + pendingPath;
        try {
            FileTool.copyFile(sourceFullPath, targetFullPath);
        } catch (IOException e) {
            String errorMsg = String.format("建立異動目錄時發生錯誤: <%s>", targetFullPath);
            logMsg(errorMsg);
            throw new IllegalStateException(errorMsg);
        }
    }

    private static void copyDiffJavaFiles(String pendingPath) {
        String pendingFilename = pendingPath.substring(pendingPath.lastIndexOf(Config.getPathSlash()) + 1);
        String sourceFullPath = Config.getProjectDir() + pendingPath;
        String targetFullPath = Config.getTargetFolderDir() + DIR_OF_JAVA_FILES + pendingFilename;
        try {
            FileTool.copyFile(sourceFullPath, targetFullPath);
        } catch (IOException e) {
            String errorMsg = String.format("產生檔案至java-files目錄下時發生錯誤: <%s>", targetFullPath);
            logMsg(errorMsg);
            throw new IllegalStateException(errorMsg);
        }
        
    }
}

