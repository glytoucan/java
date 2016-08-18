package org.glytoucan.web.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

@XmlAccessorType(value = XmlAccessType.NONE)
@XmlRootElement(name = "sitemap")
public class XmlSiteMap {
    @XmlElement
    private String loc;

    @XmlElement
    private String lastmod = new DateTime().toString(DateTimeFormat.forPattern("yyyy-MM-dd"));

    public XmlSiteMap() {
    }

    public XmlSiteMap(String loc) {
        this.loc = loc;
    }

    public String getLoc() {
        return loc;
    }

    public String getLastmod() {
        return lastmod;
    }
}