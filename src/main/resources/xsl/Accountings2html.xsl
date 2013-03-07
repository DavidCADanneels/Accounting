<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="Accountings">
  <html>
    <head>
      <title>
	Accountings
      </title>
    </head>
    <body>
      <h1>
        Accountings
      </h1>
      <ul>
        <xsl:for-each select="Accounting">
        <li>
          <xsl:element name="a">
            <xsl:attribute name="href">
              <xsl:value-of select="html"/>
            </xsl:attribute>
          <xsl:value-of select="name"/>
          </xsl:element>
        </li>
        </xsl:for-each>
      </ul>
    </body>
  </html>
</xsl:template>

</xsl:stylesheet>