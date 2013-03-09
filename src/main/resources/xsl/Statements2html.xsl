<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
    <head>
      <title>
        Statements
      </title>
    </head>
    <body>
      <h1>
        Statements
      </h1>
      <xsl:for-each select="Statements">
        <table>
          <tr>
            <th>Statement</th>
            <th>Sequence</th>
            <th>Date</th>
            <th>Sign</th>
            <th>Amount</th>
            <th>CounterParty</th>
            <th>TransactionCode</th>
            <th>Communication</th>
          </tr>
          <xsl:for-each select="Statement">
            <tr>
              <td><xsl:value-of select="ID1"/></td>
              <td><xsl:value-of select="ID2"/></td>
              <td><xsl:value-of select="Date"/></td>
              <td><xsl:value-of select="Sign"/></td>
              <td><xsl:value-of select="Amount"/></td>
              <td><xsl:value-of select="CounterParty"/></td>
              <td><xsl:value-of select="TransactionCode"/></td>
              <td><xsl:value-of select="Communication"/></td>
            </tr>
          </xsl:for-each>
        </table>
      </xsl:for-each>
    </body>
  </html>
</xsl:template>

</xsl:stylesheet>