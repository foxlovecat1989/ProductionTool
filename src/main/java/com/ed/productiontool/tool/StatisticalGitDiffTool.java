package com.ed.productiontool.tool;

import com.ed.productiontool.enums.GitStatusEnum;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static com.ed.productiontool.enums.GitStatusEnum.*;
import static com.ed.productiontool.tool.Logger.logMsg;

@Component
public class StatisticalGitDiffTool {
    public static final List<String> ignoreKeyWords = List.of(".gitignore");

    public static Map<GitStatusEnum, List<String>> statisticGitDiff(String fileFullPath) {
        List<String> addPaths = new LinkedList<>();
        List<String> modifiedPaths = new LinkedList<>();
        List<String> deletePaths = new LinkedList<>();
        List<String> errorPaths = new LinkedList<>();

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     new FileInputStream(fileFullPath)))) {
            String oneLine;
            while ((oneLine = reader.readLine()) != null) {
                boolean isIgnorePath = isIgnorePath(oneLine);
                if (isIgnorePath)
                    continue;

                GitStatusEnum gitStatusEnum = GitStatusEnum.getEnum(String.valueOf(oneLine.charAt(0)));
                switch (gitStatusEnum) {
                    case MODIFIED -> modifiedPaths.add(replacePath(oneLine));
                    case ADD -> addPaths.add(replacePath(oneLine));
                    case DELETE -> deletePaths.add(replacePath(oneLine));
                    case ERROR, default -> errorPaths.add(replacePath(oneLine));
                }
            }

            Map<GitStatusEnum, List<String>> datas = new HashMap<>();
            datas.put(ADD, addPaths);
            datas.put(MODIFIED, modifiedPaths);
            datas.put(DELETE, deletePaths);
            datas.put(ERROR, errorPaths);

            return datas;
        } catch (IOException e) {
            logMsg(e.getMessage());
            throw new IllegalStateException(e.getMessage());
        }
    }

    private static String replacePath(String oneLine) {
        int indexOfSrc = oneLine.indexOf("src");
        boolean isStartBySrc = oneLine.contains("src") && indexOfSrc < 5;

        return !isStartBySrc ?
                oneLine.substring(oneLine.lastIndexOf("\t") + 1) :
                oneLine.substring(indexOfSrc);
    }

    private static boolean isIgnorePath(String oneLine) {
        return ignoreKeyWords.stream()
                .anyMatch(oneLine::equalsIgnoreCase);
    }
}

