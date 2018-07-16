package com.zz.map.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
public class FTPUtil {
    private static String ftpIP = PropertyUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertyUtil.getProperty("ftp.user");
    private static String ftpPass = PropertyUtil.getProperty("ftp.pass");

    private String ip;
    private int port;
    private String user;
    private String psw;
    private FTPClient ftpClient;

    public FTPUtil(String ip,int port,String user,String psw){
        this.ip = ip;
        this.port = port;
        this.psw = psw;
        this.user = user;
    }
    //返回是否上传成功
    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIP,21,ftpUser,ftpPass);
        boolean result = ftpUtil.uploadFile("img",fileList);
        return result;
    }
    //上传的具体逻辑
    //remotePath 是ftp服务器文件夹下一层的文件夹
    private boolean uploadFile(String remotePath,List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        //连接ftp服务器
        if(connectServer(this.ip,this.port,this.user,this.psw)){
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for(File f: fileList){
                    fis = new FileInputStream(f);
                    ftpClient.storeFile(f.getName(),fis);
                }

            } catch (IOException e) {
                uploaded = false;
                log.error("ftp upload error",e);
            } finally {
                fis.close();
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }


    //删除文件
    public static boolean deleteFile(String url) throws IOException{
        FTPUtil ftpUtil = new FTPUtil(ftpIP,21,ftpUser,ftpPass);
        String name = url.substring(url.lastIndexOf('/')+1,url.length());
        return ftpUtil.delete(name);
    }
    //删除文件
    private boolean delete(String name) throws IOException {
        boolean deleted = true;
        if(connectServer(this.ip,this.port,this.user,this.psw)){
            try{
                deleted = ftpClient.deleteFile(name);
            }catch (Exception e){
                deleted = false;
                log.error("ftp delete error",e);
            }finally {
                ftpClient.disconnect();
            }
        }else deleted = false;
        return deleted;
    }

    //连接ftp服务器
    private boolean connectServer(String ip,int port,String user , String psw){
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user,psw);
        } catch (IOException e) {
            log.error("ftp connect error",e);
        }
        return isSuccess;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

}
