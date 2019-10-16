package com.tntxia.web.mvc.entity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;

public class MultipartForm {
	
	private List<FileItem> fileItemList;
	
	private Map<String,String> fieldMapping;

	public List<FileItem> getFileItemList() {
		return fileItemList;
	}
	
	public FileItem getFileItem() {
		if (fileItemList==null || fileItemList.size()==0) {
			return null;
		}
		return this.fileItemList.get(0);
	}

	public void setFileItemList(List<FileItem> fileItemList) {
		this.fileItemList = fileItemList;
	}

	public Map<String, String> getFieldMapping() {
		return fieldMapping;
	}

	public void setFieldMapping(Map<String, String> fieldMapping) {
		this.fieldMapping = fieldMapping;
	}
	
	public String getString(String key) {
		return fieldMapping.get(key);
	}
	
	public List<String> save(String dir) throws Exception {
		
		if(fileItemList==null) {
			throw new Exception("文件为空！");
		}
		
		List<String> res = new ArrayList<String>();
		
		for(FileItem item : fileItemList) {

			// 注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：
			// c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
			// 处理获取到的上传文件的文件名的路径部分，只保留文件名部分
			
			// 获取item中的上传文件的输入流
			InputStream in = item.getInputStream();
			
			String fileName = item.getName();
			
			String ext = FilenameUtils.getExtension(fileName);
			String newFileName = UUID.randomUUID().toString().replaceAll("-", "")+"."+ext;
			
			String uploadPath = dir+ File.separator + newFileName;;

			// 创建一个文件输出流
			FileOutputStream out = new FileOutputStream(uploadPath);
			// 创建一个缓冲区
			byte buffer[] = new byte[1024];
			// 判断输入流中的数据是否已经读完的标识
			int len = 0;
			// 循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
			while ((len = in.read(buffer)) > 0) {
				// 使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" +
				// filename)当中
				out.write(buffer, 0, len);
			}
			
			// 关闭输入流
			in.close();
			// 关闭输出流
			out.close();
			// 删除处理文件上传时生成的临时文件
			item.delete();
			
			res.add(uploadPath);
		}
		return res;
	}

}
