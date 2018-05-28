package com.ebao.hospitaldapp.rest.controller;

import com.ebao.hospitaldapp.ipfs.IPFSoperations;
import com.ebao.hospitaldapp.neo.NEOoperations;
import com.ebao.hospitaldapp.rest.base.result.JsonRESTResult;
import com.ebao.hospitaldapp.rest.base.utils.HospitalAccountRedisService;
import com.ebao.hospitaldapp.rest.event.EbaoDmiResultEvent;
import com.ebao.hospitaldapp.rest.service.RedisKeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import com.ebao.hospitaldapp.rest.entity.EbaoDmiResultEntity;

@RestController
@RequestMapping(value = "/api")
public class EbaoDmiResultController {

    final Logger logger = LoggerFactory.getLogger(EbaoDmiResultController.class);

    private class DMIresult{
        public String rsCode = "0";
        public String message;
    }

    @Autowired
    private IPFSoperations ipfsOps;
    @Autowired
    private NEOoperations neOoperations;
    @Autowired
    private HospitalAccountRedisService hospitalAccountRedisService;
    @Autowired
    private RedisKeyService redisKeyService;
    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping(value = "/informDmiFinished", method = RequestMethod.POST)
    public String receiveBlockKey(@RequestBody EbaoDmiResultEntity res){
        applicationContext.publishEvent(new EbaoDmiResultEvent(this, res));
        return new JsonRESTResult(new DMIresult()).encode();
    }

    @RequestMapping(value = "/informDmiFinished2", method = RequestMethod.POST)
    public String receiveBlockKey2(@RequestBody EbaoDmiResultEntity res){
        applicationContext.publishEvent(new EbaoDmiResultEvent(this, res, true));
        return new JsonRESTResult(new DMIresult()).encode();
    }

}


