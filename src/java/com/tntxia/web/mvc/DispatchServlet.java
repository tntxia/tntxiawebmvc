package com.tntxia.web.mvc;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;

import com.tntxia.web.mvc.xml.parser.MVCView;
import com.tntxia.web.mvc.xml.parser.RedirectView;
import com.tntxia.web.mvc.xml.parser.TemplateView;
import com.tntxia.xml.util.Dom4jUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 
 * 负责跳转的Servlet
 * @author tntxia
 * 
 */
@WebServlet(
// Servlet的访问URL，可以使用数组的方式配置多个访问路径
urlPatterns = { "*.do","*.action", "*.mvc", "*.ftl" },
// Servlet的初始化参数
initParams = { @WebInitParam(name = "config-file", value = "/WEB-INF/config/mvc.xml") }, 
name = "mvc", description = "tntxia web mvc 入口Servlet")
public class DispatchServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Map<String, Action> actionMapping = new HashMap<String, Action>();

	private boolean configLoad;

	private String mvcConfig;
	
	private String charset = "utf-8";
	
	private String version;
	
	private boolean initFreeMarker;
	
	// 负责管理FreeMarker模板的Configuration实例  
    private Configuration cfg = null;

	public DispatchServlet() {
		super();
	}

	/**
	 * 获取所有的action
	 * 
	 * @param doc
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<Action> getActionList(Document doc) {
		List<Action> actionlist = new ArrayList<Action>();
		List nodelist = doc.selectNodes("mvc/actionlist/action");
		for (int i = 0; i < nodelist.size(); i++) {
			Element el = (Element) nodelist.get(i);
			
			String type = Dom4jUtil.getProp(el, "type");
			
			Action action;
			
			CommonDoAction a = new CommonDoAction();
			// a.setUrl(Dom4jUtil.getProp(el, "url"));
			a.setCharset(charset);
			a.setClassName(el.getText());
			action = a;
			
			
			String url = Dom4jUtil.getProp(el, "url");
			if(url!=null){
				actionMapping.put(url, action);
			}
			
			action.setName(Dom4jUtil.getProp(el, "name"));
			actionlist.add(action);
		}
		return actionlist;
	}

	private Action getAction(List<Action> actionlist, String name) {
		for (Action action : actionlist) {
			
			String aName = action.getName();
			if(aName==null){
				continue;
			}
			
			if (aName.equals(name)) {
				return action;
			}
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	private void addMVCView(List nodelist){
		for(int i=0;i<nodelist.size();i++){
			
			Element el = (Element) nodelist.get(i);
			String url = Dom4jUtil.getProp(el, "url");
			
			MVCView view = new MVCView(el);
			view.setHandler(Dom4jUtil.getProp(el, "handler"));
			view.setTitle(Dom4jUtil.getProp(el, "title"));
			view.setWholePage(Dom4jUtil.getPropBool(el, "isWholePage"));
			view.setVersion(this.version);
			
			view.setUrl(url);
			view.fillStylesheets();
			view.fillScripts();
			view.fillTemplates();
			
			String viewCharset = Dom4jUtil.getProp(el, "charset");
			
			if(StringUtils.isNotEmpty(viewCharset)){
				view.setCharset(viewCharset);
			}else if(StringUtils.isNotEmpty(charset)){
				view.setCharset(charset);
			}else {
				view.setCharset("utf-8");
			}
			
			actionMapping.put(url, view);
			
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void addRedirectView(List nodelist){
		for(int i=0;i<nodelist.size();i++){
			
			Element el = (Element) nodelist.get(i);
			String url = Dom4jUtil.getProp(el, "url");
			
			RedirectView view = new RedirectView(el);
			actionMapping.put(url, view);
			
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void addTemplateView(List nodelist){
		for(int i=0;i<nodelist.size();i++){
			
			Element el = (Element) nodelist.get(i);
			String url = Dom4jUtil.getProp(el, "url");
			
			TemplateView view = new TemplateView(el);
			actionMapping.put(url, view);
			
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void setMVCViewList(Document doc) throws DocumentException{
		
		List nodelist = doc.selectNodes("mvc/mvcList/mvcView");
		addMVCView(nodelist);
		
		List includeList = doc.selectNodes("mvc/mvcList/include");
		
		for(int i=0;i<includeList.size();i++){
			Element el = (Element) includeList.get(i);
			String file = Dom4jUtil.getProp(el, "file");
			Document docInclude = Dom4jUtil.getDoc(this.getServletContext().getRealPath("/WEB-INF/config/")+File.separator+file);
			List includeNodelist = docInclude.selectNodes("mvcList/mvcView");
			addMVCView(includeNodelist);
			
			List includeRedirectViewList = docInclude.selectNodes("mvcList/redirectView");
			addRedirectView(includeRedirectViewList);
		}
		
		List includeTemplateXMLList = doc.selectNodes("mvc/templateList/include");
		for(int i=0;i<includeTemplateXMLList.size();i++){
			Element el = (Element) includeTemplateXMLList.get(i);
			String file = Dom4jUtil.getProp(el, "file");
			Document docInclude = Dom4jUtil.getDoc(this.getServletContext().getRealPath("/WEB-INF/config/")+File.separator+file);
			List includeNodelist = docInclude.selectNodes("templateList/template");
			addTemplateView(includeNodelist);
		}
		
	}

	@SuppressWarnings("rawtypes")
	private void setActionMapping(Document doc) throws DocumentException {
		
		setMVCViewList(doc);
		
		List<Action> actionlist = this.getActionList(doc);
		List nodelist = doc.selectNodes("mvc/action-mapping/dispatch");
		for (int i = 0; i < nodelist.size(); i++) {
			Element el = (Element) nodelist.get(i);
			String actionName = el.getTextTrim();
			String url = el.attributeValue("url");
			Action action = this.getAction(actionlist, actionName);
			if(action==null){
				System.out.print("warning:url:"+url+" response action null!");
			}
			actionMapping.put(url, action);
		}
	}

	private void initConfig() {
		
		// 读取MVC配置文件
		String configFile	= this.getServletContext().getRealPath(mvcConfig);
		
		try {
			Document document = Dom4jUtil.getDoc(configFile);
			
			Node root = document.getRootElement();
			
			String charset = Dom4jUtil.getProp(root, "charset");
			this.charset = charset;
			
			String version = Dom4jUtil.getProp(root, "version");
			this.version = version;
			
			setActionMapping(document);
			this.configLoad = true;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		mvcConfig = config.getInitParameter("config-file");

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	private void getFtl(String url,Map<String,Object> root,String basePath,HttpServletResponse response){
		
		try {
			
			Template t = cfg.getTemplate(url);
			
			// 使用模板文件的Charset作为本页面的charset  
	        // 使用text/html MIME-type  
			String charsetCode = charset.replaceAll("-", "").toLowerCase();
	        response.setContentType("text/html; charset="+charsetCode);
	        Writer out = response.getWriter();
	        
	        try {  
	        	if(root==null){
	        		root = new HashMap<String,Object>();
	        	}
	        	root.put("basePath", basePath);
	            t.process(root, out); // 往模板里写数据  
	        } catch (TemplateException e) {  
	            e.printStackTrace();  
	        } 
	        out.flush();
	        out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			
		}
	}
	
	private void doAction(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		if (!configLoad) {
			initConfig();
		}

		request.setCharacterEncoding("UTF-8");
		
		String uri = request.getRequestURI();
		String basePath = request.getContextPath();
		String url = uri.substring(basePath.length() + 1);
		
		String key;
		if(url.indexOf("!")!=-1){
			String[] actionSplit = url.split("\\.");
			if(actionSplit.length!=2){
				throw new Exception("非法路径");
			}
			String[] actionNameSplit = actionSplit[0].split("!");
			key = actionNameSplit[0]+"."+actionSplit[1];
			
		}else{
			key = url;
		}
		
		Action action = actionMapping.get(key);
		
		if(action==null){
			System.out.println("the url ["+url+"] map no action!!!");
			return;
		}
		
		action.execute(request, response);
		
	}
	
	private void initFreeMarker(){
		// 创建一个FreeMarker实例  
        cfg = new Configuration();  
        // 指定FreeMarker模板文件的位置  
        cfg.setServletContextForTemplateLoading(getServletContext(),  
                "/");
        cfg.setDefaultEncoding(charset);
        cfg.setClassicCompatible(true);
        cfg.setNumberFormat("0.####");
        initFreeMarker = true;
        
	}
	
	private void forView(HttpServletRequest request,
			HttpServletResponse response){
		
		if (!configLoad) {
			initConfig();
		}
		
		if(!initFreeMarker){
			initFreeMarker();
		}
		
		String uri = request.getRequestURI();
		String basePath = request.getContextPath();
		String url = uri.substring(basePath.length() + 1);
		if(url.trim().length()==0){
			url = "index.ftl";
		}
		
		getFtl(url,null,basePath,response);
		
	}

	/**
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String uri = request.getRequestURI();
		String basePath = request.getContextPath();
		String url = uri.substring(basePath.length() + 1);
		
		
		if(url.endsWith(".do") || url.endsWith(".mvc") || url.endsWith(".action")){
			try {
				doAction(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(url.endsWith(".ftl")){
			forView(request, response);
		}
		
	}
	

}