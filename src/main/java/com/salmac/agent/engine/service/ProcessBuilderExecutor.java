package com.salmac.agent.engine.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystemNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.salmac.agent.engine.utils.Utils;
import com.salmac.agent.entity.OSType;
import com.salmac.agent.entity.ScriptType;
import org.springframework.stereotype.Component;

import com.salmac.agent.engine.utils.OSNAME;

import static com.salmac.agent.engine.service.ProcessBuilderExecutorHelper.*;

@Component
public class ProcessBuilderExecutor {




    public List<String> run(String cmd) {
    	if(OSNAME.isUnix()) {
    		return runForUnix(cmd);
    	} else if(OSNAME.isWindows()) {
    		return runForWindows(cmd);
    	} else if(OSNAME.isMac()) {
    		return runForMac(cmd);
    	} else if(OSNAME.isSolaris()) {
    		return runForSolaris(cmd);
    	} else {
    		List<String> responseList = new ArrayList<String>();
    		responseList.add("Undefined OS detected");
    		return responseList;
    	}        
    }
    
    public List<String> runMultipleCmd(String cmds) {
    	return run(cmds);
    }

    public List<String> editAndRunCmd(String cmd){
        if (!Utils.isEmptyString(cmd)) {
            cmd = prepareCMD(cmd);
            //System.out.println(cmds.trim());
            if(cmd.startsWith("cd ") || cmd.startsWith("CD ")){
                List<String> resps = new ArrayList<>();
                resps.add(handleCD(cmd));
                return resps;
            }
            return runMultipleCmd(cmd);
        } else {
            return new ArrayList<>();
        }
    }

    public List<String> runScript(String fileName, ScriptType scriptType){
        String cmd = "";
        try {
            if (OSNAME.isUnix()) {
                cmd = getScriptRunCommand(OSType.Linux, scriptType) + fileName;
                return runForUnix(cmd);
            } else if (OSNAME.isWindows()) {
                cmd = getScriptRunCommand(OSType.Windows, scriptType) + fileName;
                return runForWindows(cmd);
            } else if (OSNAME.isMac()) {
                cmd = getScriptRunCommand(OSType.Linux, scriptType) + fileName;
                return runForMac(cmd);
            } else if (OSNAME.isSolaris()) {
                cmd = getScriptRunCommand(OSType.Linux, scriptType) + fileName;
                return runForSolaris(cmd);
            } else {
                List<String> responseList = new ArrayList<String>();
                responseList.add("Undefined OS detected");
                return responseList;
            }
        } catch (FileSystemNotFoundException e){
            List<String> responseList = new ArrayList<String>();
            responseList.add("Undefined OS detected");
            return responseList;
        }
    }
    
    private List<String> runForUnix(String cmd){
        //System.out.println("Inside Linux runner");
    	ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(LINUX_DIRECTORY));
        //System.out.println("1");
    	processBuilder.command("bash", "-c", cmd);
        //System.out.println("2");
        List<String> responseList = new ArrayList<String>();
        //System.out.println("3");
        try {
            Process process = processBuilder.start();
            //Process process = Runtime.getRuntime().exec(cmd);
            //System.out.println("4");
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            //System.out.println("5");
            while ((line = reader.readLine()) != null) {
            	responseList.add(line);
            }
            //System.out.println("6");
            int exitCode = process.waitFor();
            //responseList.add("\nExited with error code : " + exitCode);
            responseList.add("##"+exitCode+"##");
            process.destroy();
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
            responseList.add("500:Internal server error : " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
            e.printStackTrace();
            responseList.add("500:Internal server error : " + e.getMessage());
        }
        return responseList;
    }

    private List<String> runForWindows(String cmd){
    	ProcessBuilder processBuilder = new ProcessBuilder();
        System.out.println("#############"+cmd+"#############");
        processBuilder.command("cmd.exe", "/c", cmd);
        
        List<String> responseList = new ArrayList<String>();

        try {
            Process process = processBuilder.start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
            	responseList.add(line);
            }

            int exitCode = process.waitFor();
            responseList.add("\nExited with error code : " + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
            responseList.add("500:Internal server error : " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            responseList.add("500:Internal server error : " + e.getMessage());
        }
        return responseList;
    }
    
    private List<String> runForMac(String cmd){
    	ProcessBuilder processBuilder = new ProcessBuilder();
    	processBuilder.command("bash", "-c", cmd);
        
        List<String> responseList = new ArrayList<String>();

        try {
            Process process = processBuilder.start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
            	responseList.add(line);
            }

            int exitCode = process.waitFor();
            responseList.add("\nExited with error code : " + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
            responseList.add("500:Internal server error : " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            responseList.add("500:Internal server error : " + e.getMessage());
        }
        return responseList;
    }
    
    private List<String> runForSolaris(String cmd){
    	ProcessBuilder processBuilder = new ProcessBuilder();
    	processBuilder.command("bash", "-c", cmd);
        
        List<String> responseList = new ArrayList<String>();

        try {
            Process process = processBuilder.start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
            	responseList.add(line);
            }

            int exitCode = process.waitFor();
            responseList.add("\nExited with error code : " + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
            responseList.add("500:Internal server error : " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            responseList.add("500:Internal server error : " + e.getMessage());
        }
        return responseList;
    }

    public String prepareCMD(String str) {
        if(str.startsWith("cmds")){
            str = str.substring(5);
        }
        if(str.endsWith("=")){
            str = str.substring(0, str.length() - 1);
        }

        String s = str.trim()
                .replaceAll("___", " ")
                .replaceAll("%0A", " && ")
                .replaceAll("\\+"," ");

        return s;
    }



}