package com.taotao.common.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpUtils {
	private SftpUtils() {
	}

	public static void uploadSftpFile(String ftpHost, int ftpPort, String ftpUserName, String remoteFilepath,
			InputStream input, String privateKey,String filePath,String fileName) throws JSchException, SftpException {
		Session session = getSftpSession(ftpHost, ftpPort, ftpUserName, null, privateKey);
		uploadSftpFile(remoteFilepath, input, session,filePath,fileName);
	}

	public static boolean uploadSftpFile(int ftpPort, String ftpHost, String ftpUserName, String remoteFilepath,
			InputStream input, String password,String filePath,String fileName) throws JSchException, SftpException {
		boolean result = false;
		try {
			Session session = getSftpSession(ftpHost, ftpPort, ftpUserName, password, null);
			uploadSftpFile(remoteFilepath, input, session,filePath,fileName);
			result = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static void uploadSftpFile(String remoteFilepath, InputStream input, Session session,String filePath,String fileName)
			throws JSchException, SftpException {
		//创建布尔变量标识
		boolean ret = false;
		Channel channel = session.openChannel("sftp");
		channel.connect();
		ChannelSftp chSftp = (ChannelSftp) channel;
		try {
			chSftp.cd(remoteFilepath+filePath);
			ret = true;
		} catch(SftpException e) {
			//目录不存在创建新文件夹
			String[] dirs = (remoteFilepath+filePath).split("/");
			String tempPath = "";
			for (String dir : dirs) {
				if (null == dir || "".equals(dir))
					continue;
				 tempPath+="/"+dir;
				 try {
						chSftp.cd(tempPath);
						ret = true;
					} catch (SftpException ex) {
						try {
							chSftp.mkdir(tempPath);
							chSftp.cd(tempPath);
							ret = true;
						} catch (SftpException e1) {
							ret = false;
						} catch (Exception e1) {
							ret = false;
						}
					} catch (Exception e1) {
						ret = false;
					}
			}
			ret = true;
		} catch (Exception e1) {
			ret = false;
		}
		remoteFilepath = remoteFilepath+filePath+fileName;
		try {
			chSftp.put(input, remoteFilepath);
		} finally {
			chSftp.quit();
			channel.disconnect();
			session.disconnect();
		}
	}

	/**
	 * 通过sftp下载文件数据
	 * 
	 * @param ftpHost
	 * @param ftpUserName
	 * @param ftpPort
	 * @param remoteFile
	 * @param localFilePath
	 * @throws JSchException
	 * @throws SftpException
	 */
	public static void downloadSftpFile(String ftpHost, int ftpPort, String ftpUserName, String remoteFilepath,
			String localFilePath, String privateKey) throws JSchException, SftpException {
		Session session = getSftpSession(ftpHost, ftpPort, ftpUserName, null, privateKey);
		downloadSftpFile(remoteFilepath, localFilePath, session);
	}

	public static void downloadSftpFile(int ftpPort, String ftpHost, String ftpUserName, String remoteFilepath,
			String localFilePath, String password) throws JSchException, SftpException {
		Session session = getSftpSession(ftpHost, ftpPort, ftpUserName, password, null);
		downloadSftpFile(remoteFilepath, localFilePath, session);
	}

	/**
	 * 下载文件
	 */
	private static void downloadSftpFile(String remoteFilepath, String localFilePath, Session session)
			throws JSchException, SftpException {
		Channel channel = session.openChannel("sftp");
		channel.connect();
		ChannelSftp chSftp = (ChannelSftp) channel;

		try {
			if (remoteFilepath.startsWith("/")) {
				chSftp.cd("/");
			}
			chSftp.get(remoteFilepath, localFilePath);
		} finally {
			chSftp.quit();
			channel.disconnect();
			session.disconnect();
		}
	}

	/**
	 * 获取sftp连接session
	 */
	private static Session getSftpSession(String ftpHost, int ftpPort, String ftpUserName, String password,
			String privateKey) throws JSchException {
		JSch jsch = new JSch();
		if (StringUtils.isNotBlank(privateKey)) {
			jsch.addIdentity(privateKey);
		}
		Session session = jsch.getSession(ftpUserName, ftpHost, ftpPort);
		session.setTimeout(100000);
		if (StringUtils.isNotBlank(password)) {
			session.setPassword(password);
		}

		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();

		return session;
	}

}
