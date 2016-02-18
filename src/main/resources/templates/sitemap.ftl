<meta http-equiv="content-type" content="application/xml" charset=utf-8"/>
<?xml version="2.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd">
<url>
  <loc>http://${hostname}/</loc>
  <changefreq>weekly</changefreq>
</url>
<url>
  <loc>http://${hostname}/Structures/graphical</loc>
  <changefreq>weekly</changefreq>
</url>
<url>
  <loc>http://${hostname}/Structures/structureSearch</loc>
  <changefreq>weekly</changefreq>
</url>
<url>
  <loc>http://${hostname}/Motifs/search</loc>
  <changefreq>weekly</changefreq>
</url>
<url>
  <loc>http://${hostname}/Motifs/listAll</loc>
  <changefreq>weekly</changefreq>
</url>
<url>
  <loc>http://${hostname}/Structures</loc>
  <changefreq>weekly</changefreq>
</url>
<url>
  <loc>http://${hostname}/Preferences/index</loc>
  <changefreq>weekly</changefreq>
</url>
<#list listAccs as itemAcc>
<url>
  <loc>http://${hostname}/Structures/Glycans/${itemAcc}</loc>
</url>
</#list>
</urlset>