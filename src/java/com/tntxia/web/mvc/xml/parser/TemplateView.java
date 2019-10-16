package com.tntxia.web.mvc.xml.parser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.dom4j.Element;

/**
 * @author tntxia
 *
 */
public class TemplateView extends CommonView{
	
	private String templateFile;
	
	public TemplateView(Element el){
		
		this.templateFile = el.getTextTrim();
		
	}
	

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		
		File template = new File(request.getServletContext().getRealPath(templateFile));
		String templateContent = FileUtils.readFileToString(template,"UTF-8");
		writer.write(templateContent);
		writer.flush();
		writer.close();
		
				
	}
	
	

}
