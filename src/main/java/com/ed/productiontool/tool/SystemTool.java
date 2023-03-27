package com.ed.productiontool.tool;

import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.util.Date;

import static com.ed.productiontool.tool.Logger.logMsg;

@Component
public class SystemTool {
    public static final String WINDOWS = "windows";
    public static final String OS_NAME = "os.name";

    private SystemTool() {
    }

    public static boolean isWindows() {
        return getOsName().toLowerCase().startsWith(WINDOWS);
    }

    public static String getOsName() {
        return System.getProperty(OS_NAME);
    }


    public static void checkDirExist(String dirFullPath) {
        boolean isExist = Paths.get(dirFullPath).toFile().isDirectory();
        if (isExist)
            return;

        String errorMsg = String.format("目錄不存在or不是目錄: %s", dirFullPath);
        logMsg(errorMsg);
        throw new IllegalStateException(errorMsg);
    }

    public static void checkFileExist(String fileFullPath) {
        boolean isExist = Paths.get(fileFullPath).toFile().isFile();
        if (isExist)
            return;

        String errorMsg = String.format("檔案不存在: %s", fileFullPath);
        logMsg(errorMsg);
        throw new IllegalStateException(errorMsg);
    }

    public static void inspectCreateNewFile(String fileFullPath) {
        File file = new File(fileFullPath);
        boolean isSuccessToProduceNewFile =
                file.exists() && Math.abs(file.lastModified() - new Date().getTime()) < 10000;
        String msg =
                String.format(
                        "建立檔案結果: %s <%s>",
                        isSuccessToProduceNewFile ? "成功" : "失敗",
                        file.getAbsolutePath());
        logMsg(msg);
        if (!isSuccessToProduceNewFile)
            throw new IllegalStateException(msg);
    }
}
