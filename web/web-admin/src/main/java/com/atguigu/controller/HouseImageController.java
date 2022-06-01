package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.service.HouseImageService;
import com.atguigu.util.util.FileUtil;
import com.atguigu.util.util.QiniuUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
@RequestMapping("/houseImage")
public class HouseImageController extends BaseController {
    @Reference
    private HouseImageService houseImageService;

    private final static String PAGE_UPLOED_SHOW = "house/upload";
    private static final String SHOW_ACTION = "redirect:/house/";

    @GetMapping("/uploadShow/{houseId}/{type}")
    public String uploadShow(@PathVariable("houseId") Long houseId, @PathVariable("type") Integer type, Model model){
        model.addAttribute("houseId",houseId);
        model.addAttribute("type",type);
        return PAGE_UPLOED_SHOW;
    }

    @ResponseBody
    @PostMapping("/upload/{houseId}/{type}")
    public Result upload(@PathVariable("houseId") Long houseId,
                         @PathVariable("type") Integer type,
                         @RequestParam("file") MultipartFile[] multipartFiles) throws IOException {
        for (MultipartFile multipartFile : multipartFiles) {
            //1.0 上传到骑牛云
            String originalFilename = multipartFile.getOriginalFilename();
            String uuidName = FileUtil.getUUIDName(originalFilename);
            QiniuUtils.upload2Qiniu(multipartFile.getBytes(),uuidName);

            //2.1 拼接图片的url
            String url = QiniuUtils.getUrl(uuidName);
            //2.1 新建一个houseImage对象
            HouseImage houseImage = new HouseImage();
            houseImage.setHouseId(houseId);
            houseImage.setImageName(uuidName);
            houseImage.setImageUrl(url);
            houseImage.setType(type);

            //2.0 将图片路径存到sql
            houseImageService.insert(houseImage);
        }
        return Result.ok();
    }


    @GetMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId,
                         @PathVariable("id") Long id,
                         Model model){
        //1.0 从七牛云删除
        HouseImage houseImage = houseImageService.getById(id);
        QiniuUtils.deleteFileFromQiniu(houseImage.getImageName());
        //2.0 从数据库删除
        houseImageService.delete(id);

        return SHOW_ACTION+houseId;
    }
}
