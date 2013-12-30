<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
    <head>
      <title>
	Accounts
      </title>
    </head>
    <body>
      <h1>
        Accounts
      </h1>
      <xsl:for-each select="Accounts">
        <ul>
          <xsl:for-each select="Account">
            <li>
              <xsl:element name="a">
                <xsl:attribute name="href"><xsl:value-of select="name"/>.xml</xsl:attribute>
                  <xsl:value-of select="name"/>
              </xsl:element>
              (<xsl:value-of select="type"/>)
            </li>
          </xsl:for-each>
        </ul>
      </xsl:for-each>
    </body>
  </html>
</xsl:template>

</xsl:stylesheet>