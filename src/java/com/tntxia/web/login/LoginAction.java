package com.tntxia.web.login;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;

import com.tntxia.dbmanager.DBManager;
import com.tntxia.web.mvc.BaseAction;
import com.tntxia.xml.util.Dom4jUtil;

public class LoginAction extends BaseAction{
	
	
	@SuppressWarnings("rawtypes")
	public Map<String, Object> execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		String filePath = request.getServletContext().getRealPath("/WEB-INF/config/login.xml");
		Document doc = com.tntxia.xml.util.Dom4jUtil.getDoc(filePath);
		String usertable = Dom4jUtil.getText(doc, "/login/usertable");
		String loginid = Dom4jUtil.getText(doc, "/login/loginid");
		String datasource = Dom4jUtil.getText(doc, "/login/datasource");
		if(datasource==null) {
			datasource = "default";
		}
		
		DBManager dbManager = this.getDBManager(datasource);
		String username = request.getParameter("username");
		
		if(StringUtils.isEmpty(username)){
			return this.errorMsg("请输入用户名");
		}
		
		Map userinfo = dbManager.queryForMap("select * from "+usertable+" where "+loginid+"=?", new Object[]{username}, true);
		if(userinfo==null){
			return this.errorMsg("用户名不存在");
		}
		
		// 检查错误次数
		if(Dom4jUtil.existPath(doc, "/login/errorCountCheck")){
			String errorCountField = Dom4jUtil.getText(doc, "/login/errorCountCheck/field");
			int errCount = userinfo.get(errorCountField)==null?0:(Integer)userinfo.get(errorCountField);
			int errorCountMax = Integer.parseInt(Dom4jUtil.getText(doc, "/login/errorCountCheck/max"));
			if(errorCountMax>0 && errCount>=errorCountMax){
				String errCountMsg = Dom4jUtil.getText(doc, "/login/errorCountCheck/msg");
				return this.errorMsg(errCountMsg);
			}
		}
		
		
		String passwordid = Dom4jUtil.getText(doc, "/login/password");
		String password = request.getParameter("password");
		if(password==null || password.length()==0){
			return this.errorMsg("密码不能为空");
		}
		
		boolean loginCheck = password.equals(userinfo.get(passwordid));
		if(!loginCheck){
			if(Dom4jUtil.existPath(doc, "/login/errorCountCheck")){
				String errorCountField = Dom4jUtil.getText(doc, "/login/errorCountCheck/field");
				dbManager.update("update "+usertable+" set "+errorCountField+"="+errorCountField+"+1 where "+loginid+"=?", new Object[]{username});
			}
			
			String msg = Dom4jUtil.getText(doc, "/login/msg");
			return errorMsg(msg);
		}
		
		if(Dom4jUtil.existPath(doc, "/login/ipcheck")){
			String ipBindField = Dom4jUtil.getText(doc, "/login/ipcheck/isCheck/field");
			String isIpBindCompare = Dom4jUtil.getText(doc, "/login/ipcheck/isCheck/compare");
			
			if(userinfo.get(ipBindField)!=null && userinfo.get(ipBindField).equals(isIpBindCompare)){
				String userip = request.getRemoteAddr();
				String ipField = Dom4jUtil.getText(doc, "/login/ipcheck/field");
				String ip = (String)userinfo.get(ipField);
				if(!userip.equals(ip)){
					String ipcheckmsg = Dom4jUtil.getText(doc, "/login/ipcheck/msg");
					return errorMsg(ipcheckmsg);
				}
			}
		}
		
		if(Dom4jUtil.existPath(doc, "/login/errorCountCheck")){
			String errorCountField = Dom4jUtil.getText(doc, "/login/errorCountCheck/field");
			dbManager.update("update "+usertable+" set "+errorCountField+"=0 where "+loginid+"=?", new Object[]{username});
		}
		
		HttpSession session = request.getSession();
		session.setAttribute("userinfo", userinfo);
		session.setAttribute("username", username);
		return success();
	}

}
