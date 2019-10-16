package com.tntxia.web.mvc.view;

import java.io.File;

import javax.servlet.http.HttpServletResponse;


/**
 * 返回结果为文件的视图
 * 
 * @author tntxia
 * 
 */
public class ImageView extends FileView {
	
	public ImageView() {
		this.setContentType("image/gif");
	}
	

	public void handleHeader(HttpServletResponse response,File file,String filename) {
		
	}

}
