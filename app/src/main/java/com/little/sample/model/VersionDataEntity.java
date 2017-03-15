package com.little.sample.model;


import com.little.visit.model.ResultEntity;

public class VersionDataEntity extends ResultEntity {
    public String updateInfo;
    public String download;
    public String version;
    public String versionCode;
    public int forceUpdate = 0;//0是不强制，1是强制
}