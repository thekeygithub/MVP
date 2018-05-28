package com.xczg.blockchain.yibaodapp.rest;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller("/")
public class DefaultView extends WebMvcConfigurerAdapter {

	//页面跳转至主页
	@Autowired
	private MessageSource messageSource;
	 @RequestMapping("/")
	    public String index() {
	        return "index";
	    }
	//页面跳转至最新交易
	 @RequestMapping("/NewestTra")
	    public String NewestTra() {
	        return "newestTra";
	    }
	//页面跳转至审计页面
	 @RequestMapping("/auditRecord")
	    public String test() {
	        return "auditRecord";
	    }

	 
	 //语言更换
	 @RequestMapping("/changeSessionLanauage")
     public void changeSessionLanauage(HttpServletRequest request,HttpServletResponse response,String lang){
            LocaleResolver localeResolver =RequestContextUtils.getLocaleResolver(request);
        	   if("zh".equals(lang)){
        		   localeResolver.setLocale(request, response, new Locale("zh", "CN"));
        	   }else if("en".equals(lang)){
        		   localeResolver.setLocale(request, response, new Locale("en", "US"));
        	   }
			
		
     }
}
