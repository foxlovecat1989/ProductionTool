package com.ed.productiontool.tool;

import com.ed.productiontool.app_config.Config;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.ed.productiontool.tool.Logger.logMsg;


@Component
public class ShellTool {

    public static List<String> executeShell(String shellFullPath, List<String> args) {
        List<String> messages = new ArrayList<>();
        String msg = String.format("執行 Shell(%s)...", shellFullPath);
        messages.add(msg);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(getShellCmds(shellFullPath, args));
        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            String successMsg = String.format("執行 Shell - Success <%s>", shellFullPath);
            String failMsg = String.format("Exited with error code : %s", exitCode);
            boolean isSuccess = exitCode == 0;
            String message = isSuccess ? successMsg : failMsg;
            messages = getShellMessages(process);
            messages.add(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            String errorMsg = String.format("Interrupted in executeShell() - e.getMessage(): %s", e.getMessage());
            logMsg(errorMsg);
            throw new IllegalStateException(errorMsg);
        } catch (IOException e) {
            String errorMsg = String.format("IOException Error in executeShell()- e.getMessage(): %s", e.getMessage());
            logMsg(errorMsg);
            throw new IllegalStateException(errorMsg);
        }

        return messages;
    }

    private static List<String> getShellMessages(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        List<String> messages = new ArrayList<>();
        while ((line = reader.readLine()) != null)
            messages.add(line);

        return messages;
    }

    private static List<String> getShellCmds(String shellFullPath, List<String> args) {
        List<String> cmds = new LinkedList<>();
        cmds.add(Config.getCommand());
        cmds.add(shellFullPath);
        cmds.addAll(args);

        return cmds;
    }
}
