package com.tntxia.web.mvc.xml.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Element;

import com.tntxia.web.mvc.WebRuntime;

/**
 * @author tntxia
 *
 */
public class RedirectView extends CommonView{
	
	private Map<String,String> forwardMap = new HashMap<String,String>();
	
	@SuppressWarnings("rawtypes")
	public RedirectView(Element el){
		
		this.setEl(el);
		this.setHandler(el.attributeValue("handler"));
		List nodeList = el.selectNodes("forward");
		for(int i=0;i<nodeList.size();i++){
			Element forward = (Element) nodeList.get(i);
			String name = forward.attribute("name").getValue();
			String url = forward.getTextTrim();
			forwardMap.put(name, url);
		}
		
	}
	
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String handler = this.getHandler();
		
		if(handler!=null){
			Object obj = null;
			
			try {
				obj = this.createObject(handler);
			} catch (ClassNotFoundException e) {
				return;
				// e.printStackTrace();
			}
			
			WebRuntime runtime = new WebRuntime(request,response);
			
			Map<String,Object> res = (Map<String,Object>)this.executeMethod(obj, "execute", runtime);
			
			String returnForward = (String) res.get("forward");
			
			System.out.println("returnForward.................."+returnForward);
			
			if(returnForward==null) {
				response.getWriter().write("no result return for redirect!!!");
				return;
			}
			
			if(forwardMap.get(returnForward)==null) {
				response.getWriter().write("forward map did't contain redirect result for "+returnForward+"!!!");
				return;
			}
			
			request.getRequestDispatcher(forwardMap.get(returnForward)).forward(request, response);
			
		}
		
		
		
	}
	
	

}
