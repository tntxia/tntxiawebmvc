package com.tntxia.web.mvc.view;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tntxia.web.freemarker.FreeMarkerFactory;

/**
 * FreeMarker视图
 * 
 * @author tntxia
 * 
 */
public class FTLView implements ResultView {

	private String path;

	private Map<String, Object> root;

	public FTLView(String path) {
		this.path = path;
	}

	public FTLView(String path, Map<String, Object> root) {
		this.path = path;
		this.root = root;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, Object> getRoot() {
		return root;
	}

	public void setRoot(Map<String, Object> root) {
		this.root = root;
	}

	@Override
	public void resolve(HttpServletRequest request,HttpServletResponse response, String charset) {
		
		String basePath = request.getContextPath();
		
		// 使用模板文件的Charset作为本页面的charset
		// 使用text/html MIME-type
		String charsetCode = charset.replaceAll("-", "").toLowerCase();
		response.setContentType("text/html; charset=" + charsetCode);
		try {
			Writer out = response.getWriter();
			Map<String, Object> root = this.getRoot();
			root.put("basePath", basePath);
			String templateResult = FreeMarkerFactory.getTemplateResult(
					this.getPath(), root, charset);
			out.write(templateResult);
			out.flush();
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

}
