<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="Balance">
        <html>
            <head><title><xsl:value-of select="name"/></title></head>
            <body>
                <h1><xsl:value-of select="name"/></h1>
                <table>
                    <tr>
                        <th><xsl:value-of select="left"/></th>
                        <th>Totaal</th>
                        <th>Totaal</th>
                        <th><xsl:value-of select="right"/></th>
                    </tr>
                    <xsl:for-each select="BalanceLine">
                        <tr>
                            <xsl:choose>
                                <xsl:when test="name1">
                                    <td>
                                        <xsl:element name="a">
                                            <xsl:attribute name="href">../Accounts/<xsl:value-of select="name1"/>.html</xsl:attribute>
                                            <xsl:value-of select="name1"/>
                                        </xsl:element>
                                    </td>
                                </xsl:when>
                                <xsl:otherwise>
                                    <td/></xsl:otherwise>
                            </xsl:choose>
                            <td><xsl:value-of select="amount1"/></td>
                            <td><xsl:value-of select="amount2"/></td>
                            <xsl:choose>
                                <xsl:when test="name2">
                                    <td>
                                        <xsl:element name="a">
                                            <xsl:attribute name="href">../Accounts/<xsl:value-of select="name2"/>.html</xsl:attribute>
                                            <xsl:value-of select="name2"/>
                                        </xsl:element>
                                    </td>
                                </xsl:when>
                                <xsl:otherwise>
                                    <td/></xsl:otherwise>
                            </xsl:choose>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>