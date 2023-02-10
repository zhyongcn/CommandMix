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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuhua.CommandMix.pojo.CommandFromPhone;
import com.jiuhua.CommandMix.pojo.Constants;
import com.jiuhua.CommandMix.pojo.VersionInfo;
import com.jiuhua.CommandMix.service.MyMqttClient;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CommandMixController {

    @Autowired
    private MyMqttClient myMqttClient = null;

    // @RequestMapping("/command")
    @PostMapping("/command")
    @ResponseBody
    public CommandFromPhone toMqttBroker(@RequestBody CommandFromPhone commandFromPhone) {
        // 1 接收手机端传来的信息
        // 2 提取、解析信息,为MqttMessage
        // 3 转发出去
        // 4 需要向手机反馈信息吗？ headers？ 模块反馈信息？？

        // 重要：为什么没有这一句会空指针错误，实例还没有加载吗？？
        // 为什么现在还没有加载，最开始的程序不是已经运行了吗？？
        // *** 判断一下原来的客户端在不在？ 如果在，不要在生成了，springboot的bean不是单例吗？？ ***
        if (myMqttClient.getClient() == null) {
            myMqttClient.start();
            System.out.println("myMqttClient.start()，重新连接.......");
        }

        myMqttClient.publish(commandFromPhone);

        System.out.println(commandFromPhone.getTopic());
        System.out.println(commandFromPhone.getQos());
        System.out.println(commandFromPhone.getMessage());
        System.out.println(commandFromPhone.isRetained());

        return commandFromPhone;
    }

    @GetMapping("/test.well-known/acme-challenge/WOf00WZBkCyHK1qLqelwJi48KexGwsEG_2Tximb0Hyg")
    @ResponseBody
    public String CertbotCheck() {
        return "WOf00WZBkCyHK1qLqelwJi48KexGwsEG_2Tximb0Hyg.WjO8IvQjwr-UBvT9T7E8Je2TfRifgOSbrj2BxI2odPA";
    }

    // 提供待升级的Android app的信息
    @GetMapping("/upgrade/versioninfo")
    @ResponseBody
    public VersionInfo UpgradeVersionInfo() {
        System.out.println("有请求来获取升级信息");
        VersionInfo versioninfo = new VersionInfo();
        versioninfo.setVersionCode(2);
        versioninfo.setAppVersion("2.0.0");
        versioninfo.setDownloadUrl(Constants.DownloadUrl);
        versioninfo.setVersionDesc("1. this is a test about app upgrade.");
        return versioninfo;
    }

    // 以下三个方法提供待升级的Android apk
    // @GetMapping(value = "/downloadFile", consumes = MediaType.ALL_VALUE)
    @GetMapping("/upgrade/boiler.apk")
    void downloadBoilerApkFile(final HttpServletResponse response) throws Exception {

        // 获取文件 
        File file = new File("/home/ubuntu/upgrade/boiler.apk");
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

    @GetMapping("/upgrade/floorheat.apk")
    void downloadFloorheatApkFile(final HttpServletResponse response) throws Exception {

        // 获取文件
        File file = new File("/home/ubuntu/upgrade/floorheat.apk");
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

    @GetMapping("/upgrade/heatpumpSimple.apk")
    void downloadHeatpumpSimpleApkFile(final HttpServletResponse response) throws Exception {

        // 获取文件
        File file = new File("/home/ubuntu/upgrade/heatpumpSimple.apk");
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

    // INFO: 下面四个方法提供ESP8266的模块升级软件
    // 提供待升级的 NTC_sensor.bin
    @GetMapping("/upgrade/NTC_sensor.bin")
    void downloadNTCSensorFile(final HttpServletResponse response) throws Exception {

        // 获取文件
        File file = new File("/home/ubuntu/upgrade/NTC_sensor.bin");
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

        // 实现文件下载
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
            System.out.println("Download Esp8266bin successfully!");
        } catch (Exception e) {
            System.out.println("Download Esp8266bin failed!");
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

    // 提供待升级的 Boiler.bin
    @GetMapping("/upgrade/boiler.bin")
    void downloadBoilerFile(final HttpServletResponse response) throws Exception {

        // 获取文件
        File file = new File("/home/ubuntu/upgrade/boiler.bin");
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

        // 实现文件下载
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
            System.out.println("Download Esp8266bin successfully!");
        } catch (Exception e) {
            System.out.println("Download Esp8266bin failed!");
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

    // 提供待升级的 floorheat.bin
    @GetMapping("/upgrade/floorheat.bin")
    void downloadFloorheatFile(final HttpServletResponse response) throws Exception {

        // 获取文件
        File file = new File("/home/ubuntu/upgrade/floorheat.bin");
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

        // 实现文件下载
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
            System.out.println("Download Esp8266bin successfully!");
        } catch (Exception e) {
            System.out.println("Download Esp8266bin failed!");
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

    // 提供待升级的 fancoil.bin
    @GetMapping("/upgrade/fancoil.bin")
    void downloadFancoilFile(final HttpServletResponse response) throws Exception {

        // 获取文件
        File file = new File("/home/ubuntu/upgrade/fancoil.bin");
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

        // 实现文件下载
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
            System.out.println("Download Esp8266bin successfully!");
        } catch (Exception e) {
            System.out.println("Download Esp8266bin failed!");
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
