package com.salmac.agent.engine.service;

import com.salmac.agent.engine.utils.OSNAME;
import com.salmac.agent.entity.OSType;
import com.salmac.agent.entity.ScriptType;

import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ProcessBuilderExecutorHelper {

    public static String LINUX_DIRECTORY = System.getProperty("user.home");
    public static String WINDOWS_BANE_DIRECTORY = "C:\\";
    public static String MAC_DIRECTORY = "/home";

    public static String getScriptRunCommand(OSType osType, ScriptType scriptType) throws FileSystemNotFoundException {
        if (ScriptType.Shell.equals(scriptType)) {
            if (OSType.Linux.equals(osType)) {
                return "sh ";
            }
        } else if (ScriptType.Python.equals(scriptType)) {
            if (OSType.Linux.equals(osType)) {
                return "phython3 ";
            }
        } else if (ScriptType.Batch.equals(scriptType)) {
            if (OSType.Windows.equals(osType)) {
                return "";
            }
        }
        throw new FileSystemNotFoundException();
    }

    public static String handleCD(String cmd) {
        cmd = cmd.substring(3).trim();
        if (OSNAME.isUnix()) {
            if (cmd.equals("~")) {
                initLinuxDir();
            } else if(cmd.equals("..")) {
                Path path = Paths.get(LINUX_DIRECTORY);
                path = path.getParent();
                if(Files.exists(path)) {
                    LINUX_DIRECTORY = path.toString();
                }
            }else {
                String nextPath = LINUX_DIRECTORY.trim() + "/" +cmd;
                if(Files.exists(Paths.get(nextPath))){
                    LINUX_DIRECTORY = nextPath;
                } else {
                    return "No such file or directory";
                }
            }
        } else if (OSNAME.isWindows()) {
            //return runForWindows(cmd);
        } else if (OSNAME.isMac()) {
            //return runForMac(cmd);
        } else if (OSNAME.isSolaris()) {
            //return runForSolaris(cmd);
        } else {
            return "Undefined OS detected";
        }

        return "";
    }

    private static void initLinuxDir(){
        LINUX_DIRECTORY = System.getProperty("user.home");
    }
}
