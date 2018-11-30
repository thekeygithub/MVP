package com.xczg.blockchain.yibaodapp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 */
@WebServlet(name="download",urlPatterns="/download")
public class DownloadServlet extends HttpServlet{
	@Override
	public void init() throws ServletException {
	}

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		request.setCharacterEncoding("utf-8");
		String contentType = request.getParameter("contentType");
		String filePathOri = request.getParameter("filePath");
		System.out.println("filePathOri："+filePathOri);
		String urlType = request.getParameter("urlType");
		if ( contentType == null || "".equals(contentType) ) contentType="octet-stream";
		if (filePathOri == null || "".equals(filePathOri)) return;

		if ( filePathOri.startsWith(".") ) return ;
		if ( filePathOri.indexOf("..") != -1 ) return ;
		
		String fileId="";
		if ( filePathOri.indexOf("_") != -1 ) {
			fileId = filePathOri.substring(0, filePathOri.indexOf("_"));
		}
		String filePath = "D:/UserImg/" + filePathOri;
		System.out.println("filePath："+filePath);
//		filePath = filePath.replaceAll("IADDI", "+");
//		if ( !isExist(fileId,filePath) ) {
//			if ( !"".equals(Configure.SYSTEM_DOWNLOAD_DIR2) ) {
//				filePath = Configure.SYSTEM_DOWNLOAD_DIR2 + filePathOri;
//				filePath = filePath.replaceAll("IADDI", "+");
//			}
//		}
		
		if ( "inline".equals(urlType)) {
			urlType = "inline";
		} else {
			urlType = "attachment";
		}
		String fileName = request.getParameter("filePath");
		int index = filePath.lastIndexOf('/');
		String dir=filePath.substring(0, index);
		if (fileName == null || "".equals(fileName)) {
			if (index != -1){
				fileName = filePath.substring(index + 1);
			}else if ((index = filePath.lastIndexOf("\\")) != -1){
				fileName = filePath.substring(index + 1);
			}else{
				fileName = filePath;
			}
		}
		fileName=fileName.replaceAll("IADDI", "+");
		response.setContentType("application/"+contentType);
		response.setHeader("Content-Disposition", urlType + "; filename="+new String(fileName.getBytes(), "ISO8859-1"));
		//response.setHeader("Content-Disposition", urlType + "; filename="+fileName);

		try {
			ServletOutputStream out = response.getOutputStream();
			System.out.println("filePath try:"+filePath);
			File file = new File(filePath);
			if (file.exists()) {
				InputStream in = new FileInputStream(file);
				byte[] buffer = new byte[8192];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
				out.flush();
				out.close();
				in.close();
			} else {
				if ( fileId != null && !"".equals(fileId) ) {
					try{
						long t=Long.parseLong(fileId);
						if ( t > 1000 ) {
							File folder = new File(dir);
							if ( folder.isDirectory() ) {
								File[] files=folder.listFiles();
								for (int i=0;i<files.length;i++) {
									if ( files[i].getName().startsWith(fileId) ) {
										InputStream in = new FileInputStream(files[i]);
										byte[] buffer = new byte[8192];
										int len = 0;
										while ((len = in.read(buffer)) != -1) {
											out.write(buffer, 0, len);
										}
										out.flush();
										out.close();
										in.close();
										break;
									}
								}
							}
						}
					}catch(Exception e){

					}
				}
			}
			out = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean isExist(String fileId,String filePath){
		File file = new File(filePath);
		if (file.exists()) {
			return true;
		} else {
			if ( fileId != null && !"".equals(fileId) ) {
				int index = filePath.lastIndexOf('/');
				String dir=filePath.substring(0, index);
				try{
					long t=Long.parseLong(fileId);
					if ( t > 1000 ) {
						File folder = new File(dir);
						if ( folder.isDirectory() ) {
							File[] files=folder.listFiles();
							for (int i=0;i<files.length;i++) {
								if ( files[i].getName().startsWith(fileId) ) {
									return true;
								}
							}
						}
					}
				}catch(Exception e){
				}
			}
		}
		return false;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
	}
}