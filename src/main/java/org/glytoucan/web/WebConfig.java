package org.glytoucan.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.web.view.LocalizationHandlerMapping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class WebConfig extends WebMvcConfigurerAdapter {

  private static final Log logger = LogFactory.getLog(WebConfig.class);
  
  @Value("${gtc.version}")
  private String gtcVersion;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    LocalizationHandlerMapping mapping = new LocalizationHandlerMapping();
    logger.debug("SETTING GTC_VERSION:>" + gtcVersion);
    mapping.setGtcVersion(gtcVersion);
    registry.addInterceptor(mapping);
  }
}