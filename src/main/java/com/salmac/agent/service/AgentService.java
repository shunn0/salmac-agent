package com.salmac.agent.service;

import com.salmac.agent.files.FileStorageService;
import com.salmac.agent.entity.ScriptDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class AgentService {

    @Value("${salmac.host}")
    private String salmacHost;

    @Autowired
    private FileStorageService fileStorageService;

    public ScriptDetailsDTO getServerFileName(Long scriptId) throws IOException {
        ScriptDetailsDTO script = getScript(scriptId);
        String fileLocalPath = fileStorageService.getFilePath(script.getName());
        if (fileLocalPath.isEmpty()) {
            downloadScriptFromServerAndSave(script);
        } else{
            script.setContent(fileLocalPath);
        }
        return script;
    }

    private ScriptDetailsDTO getScript(Long scriptId) throws IOException {
        final String URI = salmacHost + "/server/script/" + scriptId;
        RestTemplate restTemplate = new RestTemplate();
        ScriptDetailsDTO script;
        ResponseEntity<ScriptDetailsDTO> response = restTemplate.getForEntity(URI, ScriptDetailsDTO.class);
        if (response.getStatusCode().value() == 200) {
            script = response.getBody();
        } else {
            throw new IOException();
        }
        return script;
    }

    private void downloadScriptFromServerAndSave(ScriptDetailsDTO dto) throws IOException {
        final String URI = salmacHost + "/server/downloadscript/" + dto.getId();
        RestTemplate restTemplate = new RestTemplate();
        MultipartFile scriptFile;
        ResponseEntity<Resource> response = restTemplate.getForEntity(URI, Resource.class);
        if (response.getStatusCode().value() == 200) {
            InputStream inputStream = response.getBody().getInputStream();
            String fileName = fileStorageService.storeFile(inputStream, dto.getName());
            dto.setContent(fileName);
        } else {
            throw new IOException();
        }
    }
}
