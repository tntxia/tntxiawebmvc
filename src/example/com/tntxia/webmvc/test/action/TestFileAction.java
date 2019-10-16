package com.tntxia.webmvc.test.action;

import java.io.File;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;

import com.tntxia.web.mvc.BaseAction;
import com.tntxia.web.mvc.WebRuntime;
import com.tntxia.web.mvc.entity.MultipartForm;
import com.tntxia.web.util.UUIDUtils;

public class TestFileAction extends BaseAction{
	
	public Map<String,Object> execute(WebRuntime runtime) throws Exception{
		
		MultipartForm form = runtime.getMultipartForm();
		String id = form.getString("id");
		FileItem fileItem = form.getFileItem();
		String fileName = fileItem.getName();
		String ext = FilenameUtils.getExtension(fileName);
		fileItem.write(new File("D:\\"+UUIDUtils.getUUID() + "." + ext));
		return this.success();
	}

}
