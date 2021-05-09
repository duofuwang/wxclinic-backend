package com.dopoiv.clinic.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * 保存文件
 *
 * @author doverwong
 * @date 2021/3/6 19:18
 */
@Component
public class UploadUtil {

    public static String path;

    @Value("${web.upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        path = this.uploadPath;
    }

    /**
     * 保存文件
     * @param file 文件
     * @return filename | null 文件名
     */
    public static String save(MultipartFile file) {
        String filename = uniqueFilename(file.getOriginalFilename());
        // 创建文件
        File dest = new File(FileUtil.touch(
                path,
                Objects.requireNonNull(filename)
        ).getAbsolutePath());
        try {
            // 文件保存
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return filename;
    }

    /**
     * 重命名文件 不修改扩展名
     *
     * @param originalFilename 原文件名
     * @return originalFilename - uuid
     */
    public static String uniqueFilename(String originalFilename) {
        return FileUtil.getPrefix(originalFilename)
                + "-" + UUID.fastUUID()
                + "." + FileUtil.getSuffix(originalFilename);
    }
}
