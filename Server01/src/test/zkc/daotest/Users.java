package com.zkc.daotest;

import java.sql.Date;

/**
 * @author zkc
 * @create 2020-04-23 18:09
 */
public class Users {
    public String filename;
    public String filesize;
    public String filetype;
    public Date filetime;
    public String filepath;
    public String filekey;

    @Override
    public String toString() {
        return "Users{" +
                "filename='" + filename + '\'' +
                ", filesize='" + filesize + '\'' +
                ", filetype='" + filetype + '\'' +
                ", filetime=" + filetime +
                ", filepath='" + filepath + '\'' +
                ", filekey='" + filekey + '\'' +
                '}';
    }

    public Users() {
    }

    public Users(String filename, String filesize, String filetype, Date filetime, String filepath, String filekey) {

        this.filename = filename;
        this.filesize = filesize;
        this.filetype = filetype;
        this.filetime = filetime;
        this.filepath = filepath;
        this.filekey = filekey;
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
}
