<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
    <head>
      <title>
	Mortgages
      </title>
    </head>
    <body>
      <h1>
        Mortgages
      </h1>
      <xsl:for-each select="Mortgages">
        <table>
          <tr>
            <th>Name</th>
            <th>Total Amount</th>
            <th>Already payed</th>
            <th>Capital Account</th>
            <th>Intrest Account</th>
          </tr>
          <xsl:for-each select="Mortgage">
            <tr>
              <td>
                <xsl:element name="a">
                  <xsl:attribute name="href">Mortgages<xsl:value-of select="name"/>.html</xsl:attribute>
                  <xsl:value-of select="name"/>
                </xsl:element>
              </td>
              <td>
                <xsl:value-of select="total"/>
              </td>
              <td>
                <xsl:value-of select="nrPayed"/> times
              </td>
              <td>
                <xsl:element name="a">
                  <xsl:attribute name="href">../Accounts/<xsl:value-of select="CapitalAccount"/>.html</xsl:attribute>
                  <xsl:value-of select="CapitalAccount"/>
                </xsl:element>
              </td>
              <td>
                <xsl:element name="a">
                  <xsl:attribute name="href">../Accounts/<xsl:value-of select="IntrestAccount"/>.html</xsl:attribute>
                  <xsl:value-of select="IntrestAccount"/>
                </xsl:element>
              </td>
            </tr>
          </xsl:for-each>
        </table>
      </xsl:for-each>
    </body>
  </html>
</xsl:template>

</xsl:stylesheet>