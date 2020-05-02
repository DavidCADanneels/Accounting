package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.VATFields

import java.util.logging.Level
import java.util.logging.Logger

import static be.dafke.Accounting.BusinessModelDao.XMLConstants.*

class LabelWriter {

    static void printLabels(Accounting accounting){
        String folderPath = "$DYMOPATH/$accounting.name"
        File folder = new File(folderPath)
        folder.mkdirs()
        accounting.contacts.businessObjects.each { Contact contact ->
            String fileName = "$contact.name$DYMO_EXTENSION"
            fileName = fileName.replaceAll('/', '')
            String path = "$folderPath/$fileName"
            File xmlFile = new File(path)
            printLabel(contact, xmlFile)
        }
    }

    static void printLabel(Contact contact, File xmlFile){
        try {
            Writer writer = new FileWriter(xmlFile)
            writer.write """\
<?xml version="1.0" encoding="utf-8"?>
<DesktopLabel Version="1">
  <DYMOLabel Version="3">
    <Description>DYMO Label</Description>
    <Orientation>Landscape</Orientation>
    <LabelName>LargeAddressS0722400</LabelName>
    <InitialLength>0</InitialLength>
    <BorderStyle>SolidLine</BorderStyle>
    <DYMORect>
      <DYMOPoint>
        <X>0.2233333</X>
        <Y>0.06</Y>
      </DYMOPoint>
      <Size>
        <Width>3.203333</Width>
        <Height>1.306667</Height>
      </Size>
    </DYMORect>
    <BorderColor>
      <SolidColorBrush>
        <Color A="1" R="0" G="0" B="0"></Color>
      </SolidColorBrush>
    </BorderColor>
    <BorderThickness>1</BorderThickness>
    <Show_Border>False</Show_Border>
    <DynamicLayoutManager>
      <RotationBehavior>ClearObjects</RotationBehavior>
      <LabelObjects>
        <AddressObject>
          <Name>Address</Name>
          <Brushes>
            <BackgroundBrush>
              <SolidColorBrush>
                <Color A="0" R="1" G="1" B="1"></Color>
              </SolidColorBrush>
            </BackgroundBrush>
            <BorderBrush>
              <SolidColorBrush>
                <Color A="1" R="0" G="0" B="0"></Color>
              </SolidColorBrush>
            </BorderBrush>
            <StrokeBrush>
              <SolidColorBrush>
                <Color A="1" R="0" G="0" B="0"></Color>
              </SolidColorBrush>
            </StrokeBrush>
            <FillBrush>
              <SolidColorBrush>
                <Color A="0" R="1" G="1" B="1"></Color>
              </SolidColorBrush>
            </FillBrush>
          </Brushes>
          <Rotation>Rotation0</Rotation>
          <OutlineThickness>1</OutlineThickness>
          <IsOutlined>False</IsOutlined>
          <BorderStyle>SolidLine</BorderStyle>
          <Margin>
            <DYMOThickness Left="0" Top="0" Right="0" Bottom="0" />
          </Margin>
          <HorizontalAlignment>Left</HorizontalAlignment>
          <VerticalAlignment>Middle</VerticalAlignment>
          <FitMode>AlwaysFit</FitMode>
          <IsVertical>False</IsVertical>
          <FormattedText>
            <FitMode>AlwaysFit</FitMode>
            <HorizontalAlignment>Left</HorizontalAlignment>
            <VerticalAlignment>Middle</VerticalAlignment>
            <IsVertical>False</IsVertical>
            <LineTextSpan>
              <TextSpan>
                <Text>${contact.name}</Text>
                <FontInfo>
                  <FontName>Arial</FontName>
                  <FontSize>21.4</FontSize>
                  <IsBold>False</IsBold>
                  <IsItalic>False</IsItalic>
                  <IsUnderline>False</IsUnderline>
                  <FontBrush>
                    <SolidColorBrush>
                      <Color A="1" R="0" G="0" B="0"></Color>
                    </SolidColorBrush>
                  </FontBrush>
                </FontInfo>
              </TextSpan>
            </LineTextSpan>
            <LineTextSpan>
              <TextSpan>
                <Text>${contact.streetAndNumber}</Text>
                <FontInfo>
                  <FontName>Arial</FontName>
                  <FontSize>21.4</FontSize>
                  <IsBold>False</IsBold>
                  <IsItalic>False</IsItalic>
                  <IsUnderline>False</IsUnderline>
                  <FontBrush>
                    <SolidColorBrush>
                      <Color A="1" R="0" G="0" B="0"></Color>
                    </SolidColorBrush>
                  </FontBrush>
                </FontInfo>
              </TextSpan>
            </LineTextSpan>
            <LineTextSpan>
              <TextSpan>
                <Text>${contact.postalCode} ${contact.city}</Text>
                <FontInfo>
                  <FontName>Arial</FontName>
                  <FontSize>21.4</FontSize>
                  <IsBold>False</IsBold>
                  <IsItalic>False</IsItalic>
                  <IsUnderline>False</IsUnderline>
                  <FontBrush>
                    <SolidColorBrush>
                      <Color A="1" R="0" G="0" B="0"></Color>
                    </SolidColorBrush>
                  </FontBrush>
                </FontInfo>
              </TextSpan>
            </LineTextSpan>
          </FormattedText>
          <BarcodePosition>None</BarcodePosition>
          <ObjectLayout>
            <DYMOPoint>
              <X>0.2238878</X>
              <Y>0.1179042</Y>
            </DYMOPoint>
            <Size>
              <Width>3.087325</Width>
              <Height>1.147157</Height>
            </Size>
          </ObjectLayout>
        </AddressObject>
      </LabelObjects>
    </DynamicLayoutManager>
  </DYMOLabel>
  <LabelApplication>Blank</LabelApplication>
  <DataTable>
    <Columns></Columns>
    <Rows></Rows>
  </DataTable>
</DesktopLabel> 
"""
            writer.flush()
            writer.close()
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VATFields.class.name).log(Level.SEVERE, null, ex)
        } catch (IOException ex) {
            Logger.getLogger(VATFields.class.name).log(Level.SEVERE, null, ex)
        }
    }
}
