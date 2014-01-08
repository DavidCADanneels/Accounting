<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="Journal">
        <html>
            <body>
                <h1><xsl:value-of select="name"/></h1>
                <table>
                    <tr><th>Nr</th><th>Datum</th><th>Rekening</th><th>Debet</th><th>Credit</th><th>Omschrijving</th></tr>
                    <xsl:apply-templates/>
                </table>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="Transaction">
        <xsl:apply-templates select="Booking"/>
    </xsl:template>

    <xsl:template match="Transaction/Booking">
        <tr>
            <!--<td><xsl:value-of select="position()"/></td><td></td>-->
            <xsl:choose>
                <xsl:when test="position()=1">
                    <xsl:element name="td">
                    <xsl:attribute name="id"><xsl:value-of select="../id"/></xsl:attribute>
                    <xsl:value-of select="../../abbr"/><xsl:value-of select="../id"/>
                    </xsl:element>
                    <td><xsl:value-of select="../date"/></td>
                </xsl:when>
                <xsl:otherwise>
                    <td></td><td></td>
                </xsl:otherwise>
            </xsl:choose>

            <td>
                <xsl:element name="a">
                    <xsl:attribute name="href">../Accounts/<xsl:value-of select="Account"/>.html#<xsl:value-of select="id"/></xsl:attribute>
                    <xsl:value-of select="Account"/>
              </xsl:element>
            </td>
            <td><xsl:value-of select="debet"/></td>
            <td><xsl:value-of select="credit"/></td>

            <xsl:choose>
                <xsl:when test="position()!=1">
                    <td></td>
                </xsl:when>
                <xsl:otherwise>
                    <td><xsl:value-of select="../description"/></td>
                </xsl:otherwise>
            </xsl:choose>
        </tr>
    </xsl:template>

</xsl:stylesheet>