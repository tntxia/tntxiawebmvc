package com.tntxia.web.mvc.xml.parser;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Element;

import com.tntxia.web.mvc.WebRuntime;
import com.tntxia.web.mvc.entity.ScriptFile;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author tntxia
 * 
 */
public class MVCView extends CommonView {

	private String title;

	private String url;

	private List<ScriptFile> scriptFiles;

	private List<String> stylesheets;

	private List<String> templateFiles;

	private String charset = "utf-8";

	private boolean initFreeMarker;

	private Configuration cfg = null;

	private boolean isWholePage = true;

	private String version;

	private Map<String, Object> initParamMap = new HashMap<String, Object>();
	
	private Map<String, Object> root = new HashMap<String, Object>();

	@SuppressWarnings("rawtypes")
	public MVCView(Element el) {

		this.setEl(el);
		Element paramMapEl = (Element) el.selectSingleNode("initParams");

		if (paramMapEl == null) {
			return;
		}

		Iterator it = paramMapEl.elementIterator();
		while (it.hasNext()) {
			Element p = (Element) it.next();
			initParamMap.put(p.getName(), p.getText());

		}
		
		Element rootEl = (Element) el.selectSingleNode("root");
		
		if(rootEl==null){
			return;
		}
		it = rootEl.elementIterator();
		while (it.hasNext()) {
			Element p = (Element) it.next();
			if(p.isTextOnly()){
				root.put(p.getName(), p.getText());
			}

		}
		
	}

	public Map<String, Object> getInitParamMap() {
		return initParamMap;
	}

	public void setInitParamMap(Map<String, Object> initParamMap) {
		this.initParamMap = initParamMap;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<ScriptFile> getScriptFiles() {
		return scriptFiles;
	}

	public void setScriptFiles(List<ScriptFile> scriptFiles) {
		this.scriptFiles = scriptFiles;
	}

	public List<String> getStylesheets() {
		return stylesheets;
	}

	public void setStylesheets(List<String> stylesheets) {
		this.stylesheets = stylesheets;
	}

	public boolean isWholePage() {
		return isWholePage;
	}

	public void setWholePage(boolean isWholePage) {
		this.isWholePage = isWholePage;
	}

	public List<String> getTemplateFiles() {
		return templateFiles;
	}

	public void setTemplateFiles(List<String> templateFiles) {
		this.templateFiles = templateFiles;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@SuppressWarnings({ "rawtypes" })
	public void fillStylesheets() {
		if (stylesheets == null) {
			stylesheets = new ArrayList<String>();
		}
		Element el = this.getEl();
		List stylesheetElList = el.selectNodes("stylesheets/stylesheet");
		if (stylesheets!=null) {
			for (int i = 0; i < stylesheetElList.size(); i++) {
				Element stylesheetEl = (Element) stylesheetElList.get(i);
				stylesheets.add(stylesheetEl.getText());
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void putInAttribute(ScriptFile scriptFile, Element scriptEl) {
		List attrEls = scriptEl.elements();
		for (int i = 0; i < attrEls.size(); i++) {
			Element attr = (Element) attrEls.get(i);
			String name = attr.getName();
			String text = attr.getTextTrim();
			scriptFile.setAttribute(name, text);
		}
	}

	@SuppressWarnings("rawtypes")
	public void fillScripts() {

		if (scriptFiles == null) {
			scriptFiles = new ArrayList<ScriptFile>();
		}
		Element el = this.getEl();
		List scripts = el.selectNodes("scripts/script");
		for (int i = 0; i < scripts.size(); i++) {
			Element scriptEl = (Element) scripts.get(i);
			ScriptFile scriptFile = new ScriptFile();
			if (scriptEl.hasMixedContent()) {
				putInAttribute(scriptFile, scriptEl);
			} else {
				scriptFile.setAttribute("src", scriptEl.getText());
			}
			scriptFile.setAttribute("charset", "utf-8");
			scriptFiles.add(scriptFile);
		}
	}

	@SuppressWarnings("rawtypes")
	public void fillTemplates() {

		if (templateFiles == null) {
			templateFiles = new ArrayList<String>();
		}
		Element el = this.getEl();
		List scripts = el.selectNodes("templates/template");
		if (scripts!=null) {
			for (int i = 0; i < scripts.size(); i++) {
				Element templateEl = (Element) scripts.get(i);
				templateFiles.add(templateEl.getText());
			}
		}
	}

	private void initFreeMarker(ServletContext context) {
		// 创建一个FreeMarker实例
		cfg = new Configuration();
		// 指定FreeMarker模板文件的位置
		cfg.setServletContextForTemplateLoading(context, "/");
		cfg.setDefaultEncoding(charset);
		cfg.setClassicCompatible(true);
		cfg.setNumberFormat("0.####");
		initFreeMarker = true;

	}

	private String getTemplateString(ServletContext context, String template,
			Map<String, Object> root) {

		if (!initFreeMarker) {
			initFreeMarker(context);
		}

		String res = null;

		try {
			Template t = cfg.getTemplate(template);

			StringWriter out = new StringWriter();

			try {
				if (root == null) {
					root = new HashMap<String, Object>();
				}
				t.process(root, out); // 往模板里写数据
			} catch (TemplateException e) {
				e.printStackTrace();
			}
			res = out.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return res;

	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String basePath = request.getContextPath();

		response.setCharacterEncoding(charset);
		PrintWriter writer = response.getWriter();

		if (isWholePage) {
			writer.write("<!DOCTYPE html>");
			writer.write("<html>");
			writer.write("<head>");

			writer.write("<title>");
			writer.write(title);
			writer.write("</title>");
			writer.write("<meta name=\"renderer\" content=\"webkit\">");
			writer.write("<meta name=\"force-rendering\" content=\"webkit\"/>");
			writer.write("<meta http-equiv='X-UA-Compatible' content='IE=Edge'>");
			writer.write("<meta charset='" + charset + "'>");

			List<String> stylesheets = this.getStylesheets();

			for (String stylesheet : stylesheets) {
				
				String href = stylesheet;
				if(!href.startsWith("/")) {
					href = basePath+ "/"+href;
				}
				writer.write("<link rel='stylesheet' href='" + href + "'>");
			}

			writer.write("<script>var webRoot='" + basePath + "';</script>");

			List<ScriptFile> scripts = this.getScriptFiles();
			for (ScriptFile script : scripts) {
				String scriptStr = "<script ";
				Map<String,String> attributes = script.getAttributes();
				
				for(Map.Entry<String, String> entry : attributes.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					if (key.equals("src")) {
						String src = value;
						if (src!=null) {
							// 如果不是以/开头，则认为是相对路径，加入basePath
							if(!src.startsWith("/")) {
								src = basePath + "/" + src;
							}
							if (version != null) {
								src += "?version=" + version;
							}
						}
						value = src;
					}
					scriptStr += key + "='"+value+"' ";
				}
				scriptStr+="></script>";
				writer.write(scriptStr);
			}

			writer.write("</head>");
			writer.write("<body>");
		}

		String handler = this.getHandler();
		
		// 当前执行的局部对象，用来记录当前的结果
		Map<String,Object> rootExecute = new HashMap<String,Object>();
		// 将公共的Root数据放入对象中
		rootExecute.putAll(root);

		if (handler != null) {
			Object obj = null;

			try {
				obj = createObject(handler);
			} catch (ClassNotFoundException e) {
				writer.write("处理类：" + handler + "不存在！");
				writer.flush();
				writer.close();

				return;
				// e.printStackTrace();
			}

			WebRuntime runtime = new WebRuntime(request, response);
			runtime.setInitParamMap(initParamMap);

			// 从Handler中获取Root对象
			Map<String,Object> rootHandler = (Map<String, Object>) this.executeMethod(obj, "execute",
					runtime);
			
			if(rootHandler!=null){
				rootExecute.putAll(rootHandler);
			}
		}

		rootExecute.put("basePath", basePath);
		rootExecute.put("currentDate", new Date(System.currentTimeMillis()));
		

		List<String> templates = this.getTemplateFiles();

		for (String template : templates) {
			String templateString = getTemplateString(
					request.getServletContext(), template, rootExecute);
			writer.write(templateString);

		}

		if (isWholePage) {
			writer.write("</body>");
			writer.write("</html>");
		}
		writer.flush();
		writer.close();

	}

}
