package com.tntxia.web.mvc;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.tntxia.web.util.DatasourceStore;
import com.tntxia.xml.util.Dom4jUtil;

/**
 * 
 * 初始化类
 * @author tntxia
 *
 */
@WebListener
public class InitListener implements ServletContextListener {
	
	private static Logger logger = Logger.getLogger(InitListener.class);

    /**
     * Default constructor. 
     */
    public InitListener() {
    	
    }
    
    @SuppressWarnings("rawtypes")
	private void loadSQLFromXML(ServletContext context){
    	String sqlXml = context.getRealPath("/WEB-INF/config/sql.xml");
    	File file = new File(sqlXml);
    	if(!file.exists() || !file.isFile()){
    		return;
    	}
    	try {
			Document doc = Dom4jUtil.getDoc(sqlXml);
			List sqlList = doc.selectNodes("/sqlmapping/sql");
			for(int i=0;i<sqlList.size();i++){
				Element el = (Element) sqlList.get(i);
				String id = el.attributeValue("id");
				SqlCache.put(id, el.getText());
				
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
    }
    
    private void loadSQLFromFile(ServletContext context) throws IOException{
    	String sqlDir = context.getRealPath("/WEB-INF/config/sql");
    	File dir = new File(sqlDir);
    	
    	if(!dir.exists()){
    		System.out.println("no sql file");
    		return;
    	}
    	
    	for(File sqlFile : dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				
				return name!=null && name.toLowerCase().endsWith(".sql");
			}
		})){
    		String name = sqlFile.getName();
    		String id = FilenameUtils.getBaseName(name);
    		String sql = FileUtils.readFileToString(sqlFile,"UTF-8");
    		SqlCache.put(id, sql);
    	}
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
	public void contextInitialized(ServletContextEvent event) {
		
		String dbPath = event.getServletContext().getRealPath("/WEB-INF/config/db.xml");
		
    	try {
    		DatasourceStore.init(dbPath);
        	ServletContext context = event.getServletContext();
        	loadSQLFromXML(context);
			loadSQLFromFile(context);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (DocumentException e) {
			e.printStackTrace();
			logger.error(e);
		}
    	
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    	System.out.println("tntxia web mvc 监听器退出！");
    }
	
}
