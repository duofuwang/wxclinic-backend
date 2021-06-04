package com.dopoiv.clinic.project.system.controller;

import cn.hutool.core.util.StrUtil;
import com.dopoiv.clinic.common.tools.BaseController;
import com.dopoiv.clinic.common.utils.UploadUtil;
import com.dopoiv.clinic.common.web.domain.R;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author doverwong
 * @date 2021/5/8 12:42
 */
@RestController
@RequestMapping("/system/obs")
public class SysObsController extends BaseController {

    @ApiOperation(value = "文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", paramType = "form", value = "文件", required = true)
    })
    @PostMapping("/upload")
    public R<String> upload(@RequestParam("file") MultipartFile file) {

        String filename = UploadUtil.save(file);
        if(StrUtil.isEmpty(filename)) {
            return R.error("上传失败");
        }
        return R.data("http://localhost:8787/clinic/static/" + filename);
    }
}
