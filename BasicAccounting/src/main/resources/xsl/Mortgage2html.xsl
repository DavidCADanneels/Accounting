<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="Mortgage">
  <html>
    <head>
      <title>
        <xsl:value-of select="name"/>
      </title>
    </head>
    <body>
      <h1><xsl:value-of select="name"/></h1>
      <table>
        <tr><th>Nr</th><th>Mensuality</th><th>Intrest</th><th>Capital</th><th>Remaining Capital</th></tr>
        <xsl:for-each select="line">
        <tr>
          <td><xsl:value-of select="nr"/></td>
          <td><xsl:value-of select="mensuality"/></td>
          <td><xsl:value-of select="intrest"/></td>
          <td><xsl:value-of select="capital"/></td>
          <td><xsl:value-of select="restCapital"/></td>
        </tr>
      </xsl:for-each>
      </table>
    </body>
  </html>
</xsl:template>

</xsl:stylesheet>