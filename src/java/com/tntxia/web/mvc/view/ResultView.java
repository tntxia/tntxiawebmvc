package com.tntxia.web.mvc.view;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ResultView {
	
	void resolve(HttpServletRequest request,HttpServletResponse response,String charset) throws IOException;

}
