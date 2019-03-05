package com.learning.exam.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @author liu_yeye
 * @date 2018-08-06 15:46
 */
@Slf4j
public class FileUtils {
    private static final String DIR_PATH = "D:/myexam";
    public  static  String saveFile(MultipartFile file, String sonPath)   {
        try {
            if (!file.isEmpty()) {
                StringTokenizer st = new StringTokenizer(file.getOriginalFilename(), ".");
                String later = null;
                while(st.hasMoreTokens()) {
                    later = st.nextToken();
                }
                String time = Long.toString(System.currentTimeMillis());
                String path = DIR_PATH+sonPath +"/"+time+"."+later;
                FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(new File(path)));
                return path;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
