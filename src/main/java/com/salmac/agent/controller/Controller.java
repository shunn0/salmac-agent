package com.salmac.agent.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.salmac.agent.service.AgentService;
import com.salmac.agent.service.ProcessBuilderExecutor;
import com.salmac.agent.service.RestClient;
import com.salmac.agent.utils.Utils;
import com.salmac.agent.entity.ScriptDetailsDTO;
import com.salmac.agent.entity.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.salmac.agent.files.FileStorageService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class Controller {

	@Autowired
    ProcessBuilderExecutor executor;

	@Autowired
	AgentService agentService;

	@Autowired
    private FileStorageService fileStorageService;

	@Autowired
	private RestClient restClient;

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
		System.out.println("serverScriptId: "+ serverScriptId);
		if(serverScriptId == null){
			return new ArrayList<String>(){{add("Not a valid Script ID");}};
		}
		ScriptDetailsDTO dto = agentService.getServerFileName(serverScriptId);
		return executor.runScript(dto.getContent(), dto.getScriptType());
	}

	@PostMapping("/runattackscript")
	public List<String> runAttackScript(Long techniqueId) throws IOException {
		if(techniqueId == null){
			return new ArrayList<String>(){{add("Not a valid technique ID");}};
		}
		ScriptDetailsDTO dto = agentService.getServerFileName(techniqueId);
		return executor.runScript(dto.getContent(), dto.getScriptType());
	}

	@GetMapping("/technique")
	public ResponseEntity getServerTechniqueDetail(Long serverId, Long techniqueId){
		return restClient.getTechniqueDetail(serverId, techniqueId);
	}
}
