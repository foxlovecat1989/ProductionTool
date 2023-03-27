package com.ed.productiontool.tool;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;

import static com.ed.productiontool.tool.Logger.logMsg;


@Component
public class FileTool {

    public static void copyFile(String sourceFullPath, String targetFullPath) throws IOException {
        File destinationFile = new File(targetFullPath);
        if (!destinationFile.exists())
            makeDir(destinationFile);
        Files.copy(Path.of(sourceFullPath), Path.of(targetFullPath), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void makeDir(File destinationFile) {
        boolean isSuccess = destinationFile.mkdirs();
        if (!isSuccess) {
            String errorMsg = "無法建立該資料夾" + destinationFile;
            throw new IllegalStateException(errorMsg);
        }
    }

    public static List<String> readFile(String path) {
        List<String> datas = new LinkedList<>();
        try (BufferedReader br = getBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null)
                datas.add(line);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }

        return datas;
    }

    public static void writeFile(String content, String targetPath) {
        File file = new File(targetPath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes(Charset.defaultCharset()));
        } catch (IOException e) {
            String errorMsg = String.format("寫入檔案時發生錯誤: <%s>", targetPath);
            logMsg(errorMsg);
            throw new IllegalStateException(errorMsg);
        }
    }

    private static BufferedReader getBufferedReader(String path) throws FileNotFoundException {
        return new BufferedReader(new FileReader(path));
    }
}
