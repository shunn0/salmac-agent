package com.salmac.agent.service;

import com.salmac.agent.entity.ServerTechniqueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class RestClient {

    @Autowired
    RestTemplate restTemplate;

    @Value("${salmac.host}")
    private String salmacHost;

    @Value("${salmac.host.self}")
    private String selfAddress;

    @Value("${server.port}")
    private String serverPort;

    @Value("${salmac.host.self.name}")
    private String selfName;

    public void sentHeartBeatRequest(){
        final String URI = salmacHost+"/server/heartbeat";
        //String ipAddr = Inet4Address.getLocalHost().getHostAddress()+":"+serverPort;
        RestTemplate restTemplate = new RestTemplate();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("agentIp", selfAddress);
        map.add("agentPort", serverPort);
        map.add("agentName", selfName);
        map.add("agentOS", System.getProperty("os.name"));
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        //ResponseEntity<String> response = restTemplate.pu( URI, request , String.class );
        // HttpEntity<String> request = new HttpEntity<>(ipAddr+":"+serverPort);

        ResponseEntity<String> response = restTemplate.postForEntity(URI, request, String.class);
        //.exchange(URI, HttpMethod.POST, request,String.class);
        System.out.println(LocalDateTime.now()+" : "+ response.getStatusCode());
    }

    public ResponseEntity getTechniqueDetail(Long serverId, Long techniqueId){
        final String URI = salmacHost+"/attackfx/server";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("serverId", serverId.toString());
        map.add("techniqueId", techniqueId.toString());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<ServerTechniqueDTO> response = restTemplate.getForEntity(URI, ServerTechniqueDTO.class, map);
        return response;
    }
}
