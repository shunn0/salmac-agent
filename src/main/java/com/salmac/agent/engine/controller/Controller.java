package com.salmac.agent.engine.controller;

import java.util.ArrayList;
import java.util.List;

import com.salmac.agent.engine.ProcessBuilderExecutor;
import com.salmac.agent.engine.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.salmac.agent.engine.files.FileStorageService;

@CrossOrigin(origins = "http://hostapp:8080")
@RestController
public class Controller {

	@Autowired
    ProcessBuilderExecutor executor;

	@Autowired
    private FileStorageService fileStorageService;

	//@CrossOrigin(origins = "http://10.0.2.15:3000")
	@GetMapping("/runcmd")
	public String runcmdGet() {
		return "Hello";
	}

	//@CrossOrigin(origins = "http://10.0.2.15:3000")
	@PostMapping("/runcmd")
	public List<String> runcmd(@RequestBody String cmd) {
		if (!Utils.isEmptyString(cmd)) {
			cmd = prepareCMD(cmd);
			//System.out.println(cmds.trim());
			return executor.runMultipleCmd(cmd);
		} else {
			return new ArrayList<>();
		}
	}

	@CrossOrigin(origins = "http://hostapp:8080")
	@PostMapping("/runmulticmd")
	public List<String> runmulticmd(@RequestBody String cmds) {
		System.out.println("##############Command received:"+cmds);
		if (!Utils.isEmptyString(cmds)) {
			cmds = prepareCMD(cmds);
			System.out.println("##############Command after parse:"+cmds);
			return executor.runMultipleCmd(cmds);
		} else {
			return new ArrayList<>();
		}
	}

	@PostMapping("/runscript")
    public List<String> uploadFile(@RequestParam MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        return executor.runScript(fileName);
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
