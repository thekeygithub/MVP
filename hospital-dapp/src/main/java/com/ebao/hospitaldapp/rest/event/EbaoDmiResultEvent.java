package com.ebao.hospitaldapp.rest.event;

import com.ebao.hospitaldapp.rest.entity.EbaoDmiResultEntity;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class EbaoDmiResultEvent extends ApplicationEvent{

    private EbaoDmiResultEntity ebaoDmiResultEntity;
    private boolean IPFSMode = false;

    public EbaoDmiResultEvent(Object source,EbaoDmiResultEntity ebaoDmiResultEntity, boolean IPFSMode) {
        super(source);
        this.IPFSMode = IPFSMode;
        this.ebaoDmiResultEntity = ebaoDmiResultEntity;
    }

    public EbaoDmiResultEvent(Object source,EbaoDmiResultEntity ebaoDmiResultEntity) {
        super(source);
        this.ebaoDmiResultEntity = ebaoDmiResultEntity;
    }
}
