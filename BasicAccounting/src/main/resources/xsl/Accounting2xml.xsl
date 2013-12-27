<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="Accounting">
  <html>
    <head>
      <title>
        <xsl:value-of select="name"/>
      </title>
    </head>
    <body>
      <h1>
        <xsl:value-of select="name"/>
      </h1>
      <ul>
        <xsl:for-each select="Accounts">
        <li>
          <xsl:element name="a">
            <xsl:attribute name="href">
              <xsl:value-of select="xml"/>
            </xsl:attribute>
            <xsl:value-of select="name"/>
          </xsl:element>
        </li>
        </xsl:for-each>
        <xsl:for-each select="Journals">
        <li>
          <xsl:element name="a">
            <xsl:attribute name="href">
              <xsl:value-of select="xml"/>
            </xsl:attribute>
            <xsl:value-of select="name"/>
          </xsl:element>
        </li>
        </xsl:for-each>
        <xsl:for-each select="Balances">
        <li>
          <xsl:element name="a">
            <xsl:attribute name="href">
              <xsl:value-of select="xml"/>
            </xsl:attribute>
            <xsl:value-of select="name"/>
          </xsl:element>
        </li>
        </xsl:for-each>
        <xsl:for-each select="Mortgages">
        <li>
          <xsl:element name="a">
            <xsl:attribute name="href">
              <xsl:value-of select="xml"/>
            </xsl:attribute>
            <xsl:value-of select="name"/>
          </xsl:element>
        </li>
        </xsl:for-each>
        <xsl:for-each select="CounterParties">
        <li>
          <xsl:element name="a">
            <xsl:attribute name="href">
              <xsl:value-of select="xml"/>
            </xsl:attribute>
            <xsl:value-of select="name"/>
          </xsl:element>
        </li>
        </xsl:for-each>
        <xsl:for-each select="Statements">
        <li>
          <xsl:element name="a">
            <xsl:attribute name="href">
              <xsl:value-of select="xml"/>
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