package com.ebao.hospitaldapp.ipfs.entity;

import lombok.Data;

import java.util.List;

@Data
public class DmiResultEntity {
    private String validID;

    private String ID;

    private List<PointsBills> pointsBills ;

    private List<InterfaceHistory> interfaceHisList ;

    private String resTotal;
}
