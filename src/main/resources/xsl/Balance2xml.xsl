<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="Balance">
  <html>
    <head>
      <title>
        <xsl:value-of select="name"/>
      </title>
    </head>
    <body>
      <h1><xsl:value-of select="name"/></h1>
      <table>
        <tr><th><xsl:value-of select="left"/></th><th>Totaal</th><th>Totaal</th><th><xsl:value-of select="right"/></th></tr>
        <xsl:for-each select="line">
        <tr>
          <td><xsl:value-of select="name1"/></td><td><xsl:value-of select="amount1"/></td>    
          <td><xsl:value-of select="amount2"/></td><td><xsl:value-of select="name2"/></td>    
        </tr>
      </xsl:for-each>
      </table>
    </body>
  </html>
</xsl:template>

</xsl:stylesheet>