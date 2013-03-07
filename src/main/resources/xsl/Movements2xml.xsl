<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
    <head>
      <title>
        Movements
      </title>
    </head>
    <body>
      <h1>
        Movements
      </h1>
      <xsl:for-each select="Movements">
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
          <xsl:for-each select="Movement">
            <tr>
              <td><xsl:value-of select="Statement"/></td>
              <td><xsl:value-of select="Sequence"/></td>
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