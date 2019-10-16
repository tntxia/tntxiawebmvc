package com.tntxia.web.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.tntxia.web.mvc.entity.MultipartForm;
import com.tntxia.web.mvc.entity.ServletFileUpload;
import com.tntxia.web.mvc.entity.UserAgent;
import com.tntxia.web.util.EscapeUnescape;

public class WebRuntime {

	private HttpServletRequest request;

	private HttpServletResponse response;

	private Map<String, Object> initParamMap = new HashMap<String, Object>();
	
	private static int DEFAULT_PAGE_SIZE = 20;

	public HttpServletResponse getResponse() {
		return response;
	}

	public WebRuntime(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public Map<String, Object> getInitParamMap() {
		return initParamMap;
	}

	public void setInitParamMap(Map<String, Object> initParamMap) {
		this.initParamMap = initParamMap;
	}

	public String getParam(String name) {
		return request.getParameter(name);
	}

	public String unescape(String name) {
		return EscapeUnescape.unescape(request.getParameter(name));
	}

	public Boolean getBoolean(String name) {
		String value = this.getParam(name);
		return Boolean.valueOf(value);
	}
	
	public Integer getInt(String name) {
		String value = this.getParam(name);
		if(value==null) {
			return null;
		}
		return Integer.parseInt(value);
	}

	public String[] getParameterValues(String name) {
		return request.getParameterValues(name);
	}
	
	/**
	 * 获取分页对象
	 * @return
	 */
	public PageBean getPageBean() {
		return this.getPageBean(null);
	}
	
	/**
	 * 获取分页对象
	 * @param defaultPageSize
	 * @return
	 */
	public PageBean getPageBean(int defaultPageSize) {
		return this.getPageBean(new Integer(defaultPageSize));
	}

	/**
	 * 获取分页对象
	 * @param defaultPageSize
	 * @return
	 */
	public PageBean getPageBean(Integer defaultPageSize) {
		PageBean pageBean = new PageBean();
		String pageStr = request.getParameter("page");
		String pageSizeStr = request.getParameter("pageSize");
		int pageSize;
		if (StringUtils.isNumericSpace(pageStr)) {
			pageBean.setPage((Integer) Integer.valueOf(pageStr));
		}
		if (StringUtils.isNumericSpace(pageSizeStr)) {
			pageSize = (Integer) Integer.valueOf(pageSizeStr);
		}else {
			if(defaultPageSize!=null) {
				pageSize = defaultPageSize;
			}else {
				pageSize = DEFAULT_PAGE_SIZE;
			}
			
		}
		pageBean.setPageSize(pageSize);
		return pageBean;
	}

	public Map<String, Object> getJSONMap(String name)
			throws UnsupportedEncodingException, IOException {

		String json = request.getParameter(name);

		return JSON.parseObject(json);
	}

	public static final byte[] readBytes(InputStream is, int contentLen) {
		if (contentLen > 0) {
			int readLen = 0;

			int readLengthThisTime = 0;

			byte[] message = new byte[contentLen];

			try {

				while (readLen != contentLen) {

					readLengthThisTime = is.read(message, readLen, contentLen
							- readLen);

					if (readLengthThisTime == -1) {// Should not happen.
						break;
					}

					readLen += readLengthThisTime;
				}

				return message;
			} catch (IOException e) {
				// Ignore
				// e.printStackTrace();
			}
		}

		return new byte[] {};
	}

	public Map<String, Object> getJSONBody() throws IOException {

		request.setCharacterEncoding("UTF-8");
		int size = request.getContentLength();
		System.out.println(size);

		InputStream is = request.getInputStream();

		byte[] reqBodyBytes = readBytes(is, size);

		String res = new String(reqBodyBytes);

		return JSON.parseObject(res);

	}

	public HttpSession getSession() {
		HttpSession session = request.getSession();
		return session;
	}

	public String getSessionStr(String key) {
		HttpSession session = this.getSession();
		return (String) session.getAttribute(key);
	}

	public Cookie[] getCookies() {
		return request.getCookies();
	}

	public Cookie getCookie(String key) {
		Cookie[] cookies = getCookies();
		for (Cookie cookie : cookies) {
			if (key.equals(cookie.getName())) {
				return cookie;
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, String> getParamMap() {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration enu = request.getParameterNames();
		while (enu.hasMoreElements()) {
			String paraName = (String) enu.nextElement();
			res.put(paraName, request.getParameter(paraName));
			System.out
					.println(paraName + ": " + request.getParameter(paraName));
		}
		return res;
	}

	public ServletContext getServletContext() {
		return this.request.getServletContext();
	}

	public void invalideSession() {
		request.getSession().invalidate();
	}
	
	public void setSession(String key,Object value) {
		request.getSession().setAttribute(key, value);
	}

	public String getBasePath() {
		return request.getContextPath();
	}

	public String getRealPath(String path) {
		return request.getServletContext().getRealPath(path);
	}

	public String getIP() {
		return request.getRemoteAddr();
	}

	public MultipartForm getMultipartForm() throws FileUploadException, UnsupportedEncodingException {
		
		
		MultipartForm form = new MultipartForm();
		
		// 使用Apache文件上传组件处理文件上传步骤：
		// 1、创建一个DiskFileItemFactory工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// 2、创建一个文件上传解析器
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 解决上传文件名的中文乱码
		upload.setHeaderEncoding("UTF-8");
		// 3、判断提交上来的数据是否是上传表单的数据
		if (!ServletFileUpload.isMultipartContent(request)) {
			// 按照传统方式获取数据
			return null;
		}

		List<FileItem> list = upload.parseRequest(request);
		
		List<FileItem> fileItemList = new ArrayList<FileItem>();
		
		Map<String,String> fieldMapping = new HashMap<String,String>();
		
		for (FileItem item : list) {
			// 如果fileitem中封装的是普通输入项的数据
			if (item.isFormField()) {
				fieldMapping.put(item.getFieldName(), item.getString("UTF-8"));
			}else{
				fileItemList.add(item);
			}
		}
		
		form.setFileItemList(fileItemList);
		form.setFieldMapping(fieldMapping);
		
		return form;
	}
	
	public String getHeader(String key) {
		return request.getHeader(key);
	}
	
	public UserAgent getUserAgent() {
		UserAgent userAgent = new UserAgent();
		String agentStr = this.getHeader("User-Agent");
		if(agentStr.indexOf("Android")>0) {
			userAgent.setOs("Android");
		}else if(agentStr.indexOf("Windows")>0) {
			userAgent.setOs("Windows");
		}
		if(agentStr.indexOf("Mobile")>0) {
			userAgent.setMobile(true);
		}
		return userAgent;
	}
	
	public InputStream getInputStream() throws IOException {
		return request.getInputStream();
	}

}
