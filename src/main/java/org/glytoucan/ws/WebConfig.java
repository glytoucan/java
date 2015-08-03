package org.glytoucan.ws;

import org.glytoucan.view.LocalizationHandlerMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
    	LocalizationHandlerMapping mapping = new LocalizationHandlerMapping();
        registry.addInterceptor(mapping);
    }
}