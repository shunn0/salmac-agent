package com.salmac.agent.engine.controller;

import java.io.IOException;
import java.util.List;

import com.salmac.agent.engine.service.AgentService;
import com.salmac.agent.engine.service.ProcessBuilderExecutor;
import com.salmac.agent.engine.utils.Utils;
import com.salmac.agent.entity.ScriptDetailsDTO;
import com.salmac.agent.entity.ScriptType;
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
	AgentService agentService;

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
		return executor.editAndRunCmd(cmd);
	}

	@CrossOrigin(origins = "http://hostapp:8080")
	@PostMapping("/runmulticmd")
	public List<String> runmulticmd(@RequestBody String cmds) {
		return executor.editAndRunCmd(cmds);
	}

	@PostMapping("/runscript")
    public List<String> uploadAndRunScript(@RequestParam MultipartFile file, ScriptType scriptType) {
        String fileName = fileStorageService.storeFile(file);
		if(scriptType == null){
			scriptType = Utils.assumeScriptType(fileName.trim());
		}
        return executor.runScript(fileName, scriptType);
    }

	@PostMapping("/runserverscript")
	public List<String> runServerScript(Long serverScriptId) throws IOException {
		ScriptDetailsDTO dto = agentService.getServerFileName(serverScriptId);
		return executor.runScript(dto.getContent(), dto.getScriptType());
	}
}
