<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
    <head>
      <title>
	CounterParties
      </title>
    </head>
    <body>
      <h1>
        CounterParties
      </h1>
      <xsl:for-each select="CounterParties">
        <table border="1">
          <tr>
            <th>Name</th>
            <th>Alias</th>
            <th>AccountName</th>
            <th>BankAccount</th>
            <th>BIC</th>
            <th>Currency</th>
          </tr>
          <xsl:for-each select="CounterParty">
            <tr>
              <td><xsl:value-of select="name"/></td>
              <td><xsl:value-of select="Alias"/></td>
              <td>
                <xsl:element name="a">
                  <xsl:attribute name="href">../Accounts/<xsl:value-of select="AccountName"/>.html</xsl:attribute>
                  <xsl:value-of select="AccountName"/>
                </xsl:element>
              </td>
              <td><xsl:value-of select="BankAccount"/></td>
              <td><xsl:value-of select="BIC"/></td>
              <td><xsl:value-of select="Currency"/></td>
            </tr>
          </xsl:for-each>
        </table>
      </xsl:for-each>
    </body>
  </html>
</xsl:template>

</xsl:stylesheet>