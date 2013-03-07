<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="Account">
  <html>
    <head>
      <title>
        <xsl:value-of select="name"/>
      </title>
    </head>
    <body>
      <h1><xsl:value-of select="name"/></h1>
      <table>
        <tr><th>Nr</th><th>Datum</th><th>Debet</th><th>Credit</th><th>Omschrijving</th></tr>
        <xsl:for-each select="action">
          <tr>
            <td>
              <xsl:element name="a">
                <xsl:attribute name="href">
                  <xsl:value-of select="journal_html"/>#<xsl:value-of select="@id"/>
                </xsl:attribute>
                <xsl:attribute name="id">
                  <xsl:value-of select="@id"/>
                </xsl:attribute>
                <xsl:value-of select="nr"/>
              </xsl:element>
            </td>
            <td><xsl:value-of select="date"/></td>
            <td><xsl:value-of select="debit"/></td>
            <td><xsl:value-of select="credit"/></td>
            <td><xsl:value-of select="description"/></td>
          </tr>
        </xsl:for-each>
        <xsl:for-each select="closed">
          <tr>
            <td>XXX</td>
            <td>XX/XX/XXXX</td>
            <td><xsl:value-of select="debitTotal"/></td>
            <td><xsl:value-of select="creditTotal"/></td>
            <td>Saldo: <xsl:value-of select="saldo"/>(<xsl:value-of select="@type"/>)</td>
          </tr>
        </xsl:for-each>
      </table>
    </body>
  </html>
</xsl:template>

</xsl:stylesheet>