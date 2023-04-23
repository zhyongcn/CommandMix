package com.jiuhua.CommandMix.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuhua.CommandMix.pojo.Constants;
import com.jiuhua.CommandMix.pojo.VersionInfo;
import com.jiuhua.CommandMix.service.MyMqttClient;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class Upgrade {

    // 提供待升级的Android app的信息
    @GetMapping("/upgrade/versioninfo")
    @ResponseBody
    public VersionInfo UpgradeVersionInfo() {
        System.out.println("有请求来获取升级信息");
        VersionInfo versioninfo = new VersionInfo();
        versioninfo.setVersionCode(2);
        versioninfo.setAppVersion("2.0.0");
        versioninfo.setDownloadUrl(Constants.baseDownloadUrl);
        versioninfo.setVersionDesc("\n this is a test about app upgrade.");

        return versioninfo;
    }

    // 以下三个方法提供待升级的Android apk
    // @GetMapping(value = "/downloadFile", consumes = MediaType.ALL_VALUE)
    @GetMapping("/upgrade/boiler.apk")
    void downloadBoilerApkFile(final HttpServletResponse response) throws Exception {

        // 获取文件
        File file = new File("/root/upgrade/boiler.apk");//TODO: 这里环境不同，不同的主机不一样。
        // 文件名
        String fileName = file.getName();

        // 清空缓冲区，状态码和响应头(headers)
        response.reset();
        // 设置ContentType，响应内容为二进制数据流，编码为utf-8，此处设定的编码是文件内容的编码
        response.setContentType("application/octet-stream;charset=utf-8");
        // 以（Content-Disposition: attachment;
        // filename="filename.jpg"）格式设定默认文件名，设定utf编码，此处的编码是文件名的编码，使能正确显示中文文件名
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName
                + ";filename*=utf-8''" + URLEncoder.encode(fileName, "utf-8"));

        // 实现文件下载
        downloadFile(response, file);
    }

    @GetMapping("/upgrade/floorheat.apk")
    void downloadFloorheatApkFile(final HttpServletResponse response) throws Exception {

        // 获取文件
        File file = new File("/root/upgrade/floorheat.apk");
        // 文件名
        String fileName = file.getName();

        // 清空缓冲区，状态码和响应头(headers)
        response.reset();
        // 设置ContentType，响应内容为二进制数据流，编码为utf-8，此处设定的编码是文件内容的编码
        response.setContentType("application/octet-stream;charset=utf-8");
        // 以（Content-Disposition: attachment;
        // filename="filename.jpg"）格式设定默认文件名，设定utf编码，此处的编码是文件名的编码，使能正确显示中文文件名
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName
                + ";filename*=utf-8''" + URLEncoder.encode(fileName, "utf-8"));

        downloadFile(response, file);
    }

    @GetMapping("/upgrade/heatpumpSimple.apk")
    void downloadHeatpumpSimpleApkFile(final HttpServletResponse response) throws Exception {

        // 获取文件
        File file = new File("/root/upgrade/heatpumpSimple.apk");
        // 文件名
        String fileName = file.getName();

        // 清空缓冲区，状态码和响应头(headers)
        response.reset();
        // 设置ContentType，响应内容为二进制数据流，编码为utf-8，此处设定的编码是文件内容的编码
        response.setContentType("application/octet-stream;charset=utf-8");
        // 以（Content-Disposition: attachment;
        // filename="filename.jpg"）格式设定默认文件名，设定utf编码，此处的编码是文件名的编码，使能正确显示中文文件名
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName
                + ";filename*=utf-8''" + URLEncoder.encode(fileName, "utf-8"));

        downloadFile(response, file);
    }

    // 下面四个方法提供ESP8266的模块升级软件
    // 提供待升级的 NTC_sensor.bin
    @GetMapping("/upgrade/NTC_sensor.bin")
    void downloadNTCSensorFile(final HttpServletResponse response) throws Exception {

        // 获取文件
        File file = new File("/root/upgrade/NTC_sensor.bin");
        // 文件名
        String fileName = file.getName();

        // 清空缓冲区，状态码和响应头(headers)
        response.reset();
        // 设置ContentType，响应内容为二进制数据流，编码为utf-8，此处设定的编码是文件内容的编码
        response.setContentType("application/octet-stream;charset=utf-8");
        // 以（Content-Disposition: attachment;
        // filename="filename.jpg"）格式设定默认文件名，设定utf编码，此处的编码是文件名的编码，使能正确显示中文文件名
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + fileName + ";filename*=utf-8''" + URLEncoder.encode(fileName, "utf-8"));

        downloadFile(response, file);

    }

    // 提供待升级的 Boiler.bin
    @GetMapping("/upgrade/boiler.bin")
    void downloadBoilerFile(final HttpServletResponse response) throws Exception {

        // 获取文件
        File file = new File("/root/upgrade/boiler.bin");
        // 文件名
        String fileName = file.getName();

        // 清空缓冲区，状态码和响应头(headers)
        response.reset();
        // 设置ContentType，响应内容为二进制数据流，编码为utf-8，此处设定的编码是文件内容的编码
        response.setContentType("application/octet-stream;charset=utf-8");
        // 以（Content-Disposition: attachment;
        // filename="filename.jpg"）格式设定默认文件名，设定utf编码，此处的编码是文件名的编码，使能正确显示中文文件名
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + fileName + ";filename*=utf-8''" + URLEncoder.encode(fileName, "utf-8"));

        downloadFile(response, file);

    }

    // 提供待升级的 floorheat.bin
    @GetMapping("/upgrade/floorheat.bin")
    void downloadFloorheatFile(final HttpServletResponse response) throws Exception {

        // 获取文件
        File file = new File("/root/upgrade/floorheat.bin");
        // 文件名
        String fileName = file.getName();

        // 清空缓冲区，状态码和响应头(headers)
        response.reset();
        // 设置ContentType，响应内容为二进制数据流，编码为utf-8，此处设定的编码是文件内容的编码
        response.setContentType("application/octet-stream;charset=utf-8");
        // 以（Content-Disposition: attachment;
        // filename="filename.jpg"）格式设定默认文件名，设定utf编码，此处的编码是文件名的编码，使能正确显示中文文件名
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + fileName + ";filename*=utf-8''" + URLEncoder.encode(fileName, "utf-8"));

        downloadFile(response, file);

    }

    // 提供待升级的 fancoil.bin
    @GetMapping("/upgrade/fancoil.bin")
    void downloadFancoilFile(final HttpServletResponse response) throws Exception {

        // 获取文件
        File file = new File("/root/upgrade/fancoil.bin");
        // 文件名
        String fileName = file.getName();

        // 清空缓冲区，状态码和响应头(headers)
        response.reset();
        // 设置ContentType，响应内容为二进制数据流，编码为utf-8，此处设定的编码是文件内容的编码
        response.setContentType("application/octet-stream;charset=utf-8");
        // 以（Content-Disposition: attachment;
        // filename="filename.jpg"）格式设定默认文件名，设定utf编码，此处的编码是文件名的编码，使能正确显示中文文件名
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + fileName + ";filename*=utf-8''" + URLEncoder.encode(fileName, "utf-8"));

        downloadFile(response, file);
    }

    // 下载文件的工具
    private void downloadFile(final HttpServletResponse response, File file) {
        byte[] buffer = new byte[1024];
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            // 获取字节流
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            System.out.println("Download successfully!");
        } catch (Exception e) {
            System.out.println("Download failed!");
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
