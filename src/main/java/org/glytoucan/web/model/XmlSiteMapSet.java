package org.glytoucan.web.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(value = XmlAccessType.NONE)
@XmlRootElement(name = "sitemapindex", namespace = "http://www.sitemaps.org/schemas/sitemap/0.9")
public class XmlSiteMapSet {

    @XmlElements({@XmlElement(name = "sitemap", type = XmlSiteMap.class)})
    private Collection<XmlSiteMap> xmlUrls = new ArrayList<XmlSiteMap>();

    public void addSiteMap(XmlSiteMap xmlUrl) {
        xmlUrls.add(xmlUrl);
    }

    public Collection<XmlSiteMap> getXmlSiteMaps() {
        return xmlUrls;
    }
}