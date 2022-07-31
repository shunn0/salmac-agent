package com.salmac.agent.engine.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.salmac.agent.engine.utils.Utils;
import org.springframework.stereotype.Component;

import com.salmac.agent.engine.utils.OSNAME;

@Component
public class ProcessBuilderExecutor {

    private String LINUX_DIRECTORY = "/home";
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

    public List<String> runScript(String fileName){
    	return run(fileName);
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

    private String handleCD(String cmd) {
        List<String> respList = runMultipleCmd(cmd);
        if (respList.size() == 1 && respList.get(0).equals("##2##")){
            return "bash: cd: srcs: No such file or directory";
        } else {
            cmd = cmd.substring(3).trim();
            if(OSNAME.isUnix()) {
                if(cmd.equals("~")){
                    LINUX_DIRECTORY = "/home";
                } else {
                    LINUX_DIRECTORY = LINUX_DIRECTORY.trim() + "/" + cmd;
                }
            } else if(OSNAME.isWindows()) {
                //return runForWindows(cmd);
            } else if(OSNAME.isMac()) {
                //return runForMac(cmd);
            } else if(OSNAME.isSolaris()) {
                //return runForSolaris(cmd);
            } else {
                return "Undefined OS detected";
            }
        }
        return "";
    }

}