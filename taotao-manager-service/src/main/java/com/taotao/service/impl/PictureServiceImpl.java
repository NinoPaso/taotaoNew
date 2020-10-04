package com.taotao.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.SftpUtils;
import com.taotao.service.PictureService;

@Service
public class PictureServiceImpl implements PictureService {
	// 从资源配置文件读取值
	@Value("${FTP_ADDRESS}")
	private String FTP_ADDRESS;
	@Value("${FTP_PORT}")
	private Integer FTP_PORT;
	@Value("${FTP_USERNAME}")
	private String FTP_USERNAME;
	@Value("${FTP_PASSWORD}")
	private String FTP_PASSWORD;
	@Value("${FTP_BASE_PATH}")
	private String FTP_BASE_PATH;
	@Value("${IMAGE_BASE_URL}")
	private String IMAGE_BASE_URL;

	@Override
	public Map uploadPicture(MultipartFile uploadFile) {
		Map resultMap = new HashMap<>();
		try {
			// 生成一个新的文件名
			String oldName = uploadFile.getOriginalFilename();
			String newName = IDUtils.genImageName();
			newName = "/" + newName + oldName.substring(oldName.lastIndexOf("."));
			// 图片上传
			// 定义图片上传日期地址文件夹
			String imageDate = new DateTime().toString("/yyyy/MM/dd");

			boolean result = SftpUtils.uploadSftpFile(FTP_PORT, FTP_ADDRESS, FTP_USERNAME, FTP_BASE_PATH,
					uploadFile.getInputStream(), FTP_PASSWORD, imageDate, newName);
			if (!result) {
				resultMap.put("error", 1);
				resultMap.put("message", "文件上传失败！");
				return resultMap;
			}
			resultMap.put("error", 0);
			resultMap.put("url", IMAGE_BASE_URL + imageDate + newName);
			return resultMap;
		} catch (JSchException | SftpException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultMap.put("error", 1);
			resultMap.put("message", "文件上传异常!!");
			return resultMap;
		}
	}

}
