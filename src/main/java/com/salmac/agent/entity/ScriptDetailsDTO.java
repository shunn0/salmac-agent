package com.salmac.agent.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ScriptDetailsDTO implements Serializable {

    private Long id;
    private String name;
    private String content;
    private String targetOS;
    private String purpose;
    private ScriptType scriptType;
    private String status;
    private LocalDateTime lastUpdateTime;

    public ScriptDetailsDTO(){

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTargetOS() {
        return targetOS;
    }

    public void setTargetOS(String targetOS) {
        this.targetOS = targetOS;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public ScriptType getScriptType() {
        return scriptType;
    }

    public void setScriptType(ScriptType scriptType) {
        this.scriptType = scriptType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}