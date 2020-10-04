package com.taotao.controller;

import java.util.Map;

import com.taotao.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.utils.JsonUtils;

/*
 * 图片上传Controller
 */
@Controller
public class PictureController {
	@Autowired
	private PictureService pictureService;
	
	@RequestMapping("/pic/upload")
	@ResponseBody
	public String pictureUpload(MultipartFile uploadFile) {	
		Map result = pictureService.uploadPicture(uploadFile);
		String jsonResult = JsonUtils.objectToJson(result);
		return jsonResult;
	}
}
