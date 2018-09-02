package com.marta.logistika.configuration;

import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return  new Class<?>[] {ApplicationConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[]{WebConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}

    @Override
    protected Filter[] getServletFilters(){
    	
        //кодировка
    	CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
    	characterEncodingFilter.setEncoding("UTF-8");
    	characterEncodingFilter.setForceEncoding(true);
    	
    	//поддержка HTTP методов
    	HiddenHttpMethodFilter httpMethodFilter = new HiddenHttpMethodFilter();

    	return new Filter[]{characterEncodingFilter, httpMethodFilter};
    	
    }
	
	
	
	
	
	
	
	

}
