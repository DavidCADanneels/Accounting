<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="Journal">
  <html>
    <body>
      <h1><xsl:value-of select="name"/></h1>
      <table>
        <tr><th>Nr</th><th>Datum</th><th>Rekening</th><th>Debet</th><th>Credit</th><th>Omschrijving</th></tr>
        <xsl:for-each select="Transaction">
          <tr>
            <xsl:element name="td">
              <xsl:attribute name="id">
                <xsl:value-of select="@id"/>
              </xsl:attribute>
              <xsl:value-of select="nr"/>
            </xsl:element>
            <td><xsl:value-of select="date"/></td>
            <td>
              <xsl:element name="a">
                  <xsl:attribute name="href">../Accounts/<xsl:value-of select="Account"/>.xml#<xsl:value-of select="@id"/>
                </xsl:attribute>
                <xsl:value-of select="Account"/>
              </xsl:element>
            </td>
            <td><xsl:value-of select="debet"/></td>
            <td><xsl:value-of select="credit"/></td>
            <td><xsl:value-of select="description"/></td>
          </tr>
        </xsl:for-each>
      </table>
    </body>
  </html>
</xsl:template>

</xsl:stylesheet>