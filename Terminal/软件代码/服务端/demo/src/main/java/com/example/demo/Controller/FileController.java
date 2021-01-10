package com.example.demo.Controller;

import com.example.demo.State.BackState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.MulticastChannel;
import java.util.UUID;


@Controller
public class FileController {

    @ResponseBody
    @PostMapping(value = "/picUpload")
    public BackState fileUpload(@RequestParam(value = "file")MultipartFile file, Model model, HttpServletRequest request){
        if(file.isEmpty()){
           return new BackState(false,"file is null");
        }try {
            InputStream in=file.getInputStream();
            int b;
            FileOutputStream outputStream=new FileOutputStream("D://sbtemp//test.png");
            byte[] bytes=new byte[1024];
            while(in.read(bytes)!=-1){
                    outputStream.write(bytes);
                }
        }catch (Exception e){
            System.out.println(e.toString());
        }
        String fileName=file.getOriginalFilename();
        System.out.println(fileName);
        //获取后缀名
        //String suffixName=fileName.substring(fileName.lastIndexOf("."));
        String filePath="D://sbtemp//";//上传路径
        //fileName= UUID.randomUUID()+suffixName;//新文件名
        File dest=new File(filePath+fileName);
        //新建路径
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }
//        try{
//            //转移至dest
//            file.transferTo(dest);
//        } catch (IOException e) {
//            return new BackState(false,e.toString());
//        }
        return new BackState(true,"successful");
    }
}
