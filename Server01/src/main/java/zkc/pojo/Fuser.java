package com.zkc.pojo;

import java.sql.Date;

/**
 * @author zkc
 * @create 2020-04-23 21:32
 */
public class Fuser {
    public String filename;
    private String filesize;
    private String filetype;
    private Date filetime;
    private String filepath;
    private String filekey;
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public Date getFiletime() {
        return filetime;
    }

    public void setFiletime(Date filetime) {
        this.filetime = filetime;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilekey() {
        return filekey;
    }

    public void setFilekey(String filekey) {
        this.filekey = filekey;
    }

    @Override
    public String toString() {
        return "Fuser{" +
                "filename='" + filename + '\'' +
                ", filesize='" + filesize + '\'' +
                ", filetype='" + filetype + '\'' +
                ", filetime=" + filetime +
                ", filepath='" + filepath + '\'' +
                ", filekey='" + filekey + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }

    public Fuser(String filename, String filesize, String filetype, Date filetime, String filepath, String filekey, String uuid) {
        this.filename = filename;
        this.filesize = filesize;
        this.filetype = filetype;
        this.filetime = filetime;
        this.filepath = filepath;
        this.filekey = filekey;
        this.uuid = uuid;
    }

    public Fuser() {
    }
}
