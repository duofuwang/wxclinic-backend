package com.dopoiv.clinic.common.utils;

import cn.hutool.core.io.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @author doverwong
 * @date 2021/3/6 19:18
 */
public class UploadUtil {

    public final static boolean SAVE_SUCCESS = true;
    public final static boolean SAVE_FAIL = false;
    public static boolean save(MultipartFile file, String path) {
        File dest = new File(FileUtil.touch(path, Objects.requireNonNull(file.getOriginalFilename())).getAbsolutePath()) ;
        System.out.println(dest.getPath());
        System.out.println(dest.getAbsolutePath());
        try {
            file.transferTo(dest);
            System.out.println("UploadUtil dest: " + dest.getName());
        } catch (IOException e) {
            e.printStackTrace();
            return SAVE_FAIL;
        }
        return SAVE_SUCCESS;
    }
}
