<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
    <head>
      <title>
	Balances
      </title>
    </head>
    <body>
      <h1>
        Balances
      </h1>
      <xsl:for-each select="Balances">
        <ul>
          <xsl:for-each select="Balance">
            <li>
              <xsl:element name="a">
                <xsl:attribute name="href">Balances/<xsl:value-of select="name"/>.html</xsl:attribute>
                <xsl:value-of select="name"/>
              </xsl:element>
            </li>
          </xsl:for-each>
        </ul>
      </xsl:for-each>
    </body>
  </html>
</xsl:template>

</xsl:stylesheet>