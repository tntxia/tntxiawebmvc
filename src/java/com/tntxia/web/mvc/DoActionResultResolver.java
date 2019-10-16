package com.tntxia.web.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tntxia.web.mvc.view.ResultView;

/**
 * 
 * 返回结果的处理类
 * @author tntxia
 *
 */
public class DoActionResultResolver {
	
	private Object result;
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private String charset;
	
	public DoActionResultResolver(HttpServletRequest request,HttpServletResponse response,Object result,
			String charset){
		this.result = result;
		this.request = request;
		this.response = response;
		this.charset = charset;
		
	}
	
	public void resolve() throws IOException{
		
		if (result == null) {
			response.setCharacterEncoding(charset);
			response.setContentType("application/json");
			response.getWriter().print("没有信息返回");
			return;
		}

		// 如果返回值是FTLView类型，需要读取FreeMarker模板
		if (result instanceof ResultView) {
			ResultView view = (ResultView) result;
			view.resolve(request,response,charset);
		}else {
			
			if(StringUtils.isEmpty(charset)) {
				response.setCharacterEncoding("utf-8");
			}else {
				response.setCharacterEncoding(charset);
			}
			
			response.setContentType("application/json");
			response.getWriter().print(
					JSON.toJSONString(result,
							SerializerFeature.WriteDateUseDateFormat));
		}
	}

}
