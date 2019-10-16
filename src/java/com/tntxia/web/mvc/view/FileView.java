package com.tntxia.web.mvc.view;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;


/**
 * 返回结果为文件的视图
 * 
 * @author tntxia
 * 
 */
public class FileView implements ResultView {

	private String filePath;
	
	private String name;
	
	private String contentType;

	public String getFilePath() {
		return filePath;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public void handleHeader(HttpServletResponse response,File file,String filename) {
		// 设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
        response.addHeader("Content-Length", "" + file.length());
	}

	@Override
	public void resolve(HttpServletRequest request,
			HttpServletResponse response, String charset) {
		
		try {
			
			 // path是指欲下载的文件的路径。
            File file = new File(filePath);
            // 取得文件名。
            String filename = file.getName();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(filePath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
			
			// 清空response
            response.reset();
            
            if(StringUtils.isEmpty(name)) {
            	filename = file.getName();
            }else {
            	filename = URLEncoder.encode(name,"UTF-8") + "." + FilenameUtils.getExtension(file.getName());
            }
            
            handleHeader(response,file,filename);
            
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            
            if(contentType==null){
            	contentType = "application/octet-stream";
            }
            response.setContentType(contentType);
            toClient.write(buffer);
            toClient.flush();
            toClient.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
