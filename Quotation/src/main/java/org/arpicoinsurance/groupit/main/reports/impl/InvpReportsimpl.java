package org.arpicoinsurance.groupit.main.reports.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.arpicoinsurance.groupit.main.helper.QuoCustomer;
import org.arpicoinsurance.groupit.main.helper.QuotationView;
import org.arpicoinsurance.groupit.main.reports.InvpReports;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
//import com.itextpdf.kernel.font.PdfFont;
//import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.ListNumberingType;
import com.itextpdf.layout.property.TextAlignment;



@Service
public class InvpReportsimpl implements InvpReports{
	
	
	
	
	
	private static final String DEST = "./src/main/resources/Reports/";
	public static final String IMG = "./src/main/resources/Reports/logo.png";
	public static final String FONT= "./src/main/resources/Reports/FONTDIR/Times_New_Romance.ttf";
	//public static final String FONT  = "./Reports/resources/fonts/Times_New_Romance.ttf";
	
		
	@Override
	public String createINVPReport(QuotationView quotation) throws Exception {
        File file = new File(DEST+"INVP/"+quotation.getQuoDetailId().toString()+".pdf");
        if(file.exists()) {
        	file.delete();
        }
	    
        FileOutputStream fos = new FileOutputStream(DEST+"INVP/"+quotation.getQuoDetailId().toString()+".pdf");

        
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        QuoCustomer customer = quotation.getCustDetails();
                                             
        document.setMargins(30,25,30,25);
         System.out.println("dnhfjhdik");
        //logo
        Paragraph pLogo = new Paragraph();
        Image logo = new Image(ImageDataFactory.create(IMG));
        logo.setHeight(100);
        logo.setWidth(120);
        logo.setFixedPosition(460, 720);
        pLogo.add(logo);
        document.add(pLogo);
        
        //PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);
        //Paragraph p = new Paragraph("Arpico Insurance PLC Quotation").setFont(font);
        //document.add(p);   
        
        document.add(new Paragraph("Arpico Insurance PLC Quotation").setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));

        document.add(new Paragraph(" "));
        
        //Agent Details
        float [] pointColumnWidths1 = {70, 150}; 
        Table agtTable = new Table(pointColumnWidths1);
        agtTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
        
        Cell agCell1 = new Cell();
        agCell1.setBorder(Border.NO_BORDER);
        agCell1.add(new Paragraph("Date").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agCell1);
        Cell agcell2 = new Cell();
        agcell2.setBorder(Border.NO_BORDER);
        agcell2.add(new Paragraph(": Nov-18-2016 11:58:44").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell2);
        
        agtTable.startNewRow();
        
        Cell agCell3 = new Cell();
        agCell3.setBorder(Border.NO_BORDER);
        agCell3.add(new Paragraph("Branch Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agCell3);
        Cell agcell4 = new Cell();
        agcell4.setBorder(Border.NO_BORDER);
        agcell4.add(new Paragraph(": Ruwanwella").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell4);
        
        agtTable.startNewRow();
        
        Cell agCell5 = new Cell();
        agCell5.setBorder(Border.NO_BORDER);
        agCell5.add(new Paragraph("Agent Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agCell5);
        Cell agcell6 = new Cell();
        agcell6.setBorder(Border.NO_BORDER);
        agcell6.add(new Paragraph(": B.R.P.C. Kumara").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell6);
        
        agtTable.startNewRow();
        
        Cell agcell7 = new Cell();
        agcell7.setBorder(Border.NO_BORDER);
        agcell7.add(new Paragraph("Agent Code").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell7);
        Cell agcell8 = new Cell();
        agcell8.setBorder(Border.NO_BORDER);
        agcell8.add(new Paragraph(": 1010").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell8);
        
        agtTable.startNewRow();
        
        Cell agcell9 = new Cell();
        agcell9.setBorder(Border.NO_BORDER);
        agcell9.add(new Paragraph("Contact No").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell9);
        
        document.add(agtTable);
        
        document.add(new Paragraph("ARPICO INVESTMENT PLUS").setFontSize(10).setUnderline().setCharacterSpacing(1));
        document.add(new Paragraph(""));
        
        //customer Details
        float [] pointColumnWidths2 = {170, 200};
        Table cusTable = new Table(pointColumnWidths2);
        cusTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
        
        Cell cucell1 = new Cell();
        cucell1.setBorder(Border.NO_BORDER);
        cucell1.add(new Paragraph("Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell1);
        Cell cuCell2 = new Cell();
        cuCell2.setBorder(Border.NO_BORDER);
        cuCell2.add(new Paragraph(":" +customer.getMainLifeName()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cuCell2);
        
        cusTable.startNewRow();
        
        Cell cucell3 = new Cell();
        cucell3.setBorder(Border.NO_BORDER);
        cucell3.add(new Paragraph("Age Next Birthday (Life Assured)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell3);
        Cell cuCell4 = new Cell();
        cuCell4.setBorder(Border.NO_BORDER);
        cuCell4.add(new Paragraph(Integer.toString(customer.getMainLifeAge())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cuCell4);
        
        cusTable.startNewRow();
        
        Cell cucell5 = new Cell();
        cucell5.setBorder(Border.NO_BORDER);
        cucell5.add(new Paragraph("Occupation (Life Assured)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell5);
        Cell cuCell6 = new Cell();
        cuCell6.setBorder(Border.NO_BORDER);
        cuCell6.add(new Paragraph(": NOT SPECIFIED").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cuCell6);
        
        cusTable.startNewRow();
        
        Cell cucell7 = new Cell();
        cucell7.setBorder(Border.NO_BORDER);
        cucell7.add(new Paragraph("Term").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell7);
        Cell cuCell8 = new Cell();
        cuCell8.setBorder(Border.NO_BORDER);
        cuCell8.add(new Paragraph(": 6").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cuCell8);
        
        cusTable.startNewRow();
        
        Cell cucell9 = new Cell();
        cucell9.setBorder(Border.NO_BORDER);
        cucell9.add(new Paragraph("Mode").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell9);
        Cell cuCell10 = new Cell();
        cuCell10.setBorder(Border.NO_BORDER);
        cuCell10.add(new Paragraph(": Single Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cuCell10);
        
        cusTable.startNewRow();
        
        Cell cucell11 = new Cell();
        cucell11.setBorder(Border.NO_BORDER);
        cucell11.add(new Paragraph("Mode Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell11);
        Cell cuCell12 = new Cell();
        cuCell12.setBorder(Border.NO_BORDER);
        cuCell12.add(new Paragraph(": 200,850.00").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cuCell12);
        
        cusTable.startNewRow();
        
        Cell cucell13 = new Cell();
        cucell13.setBorder(Border.NO_BORDER);
        cucell13.add(new Paragraph("Policy Fees").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell13);
        Cell cuCell14 = new Cell();
        cuCell14.setBorder(Border.NO_BORDER);
        cuCell14.add(new Paragraph(": 300.00").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cuCell14);
        
        cusTable.startNewRow();
        
        Cell cucell15 = new Cell();
        cucell15.setBorder(Border.NO_BORDER);
        cucell15.add(new Paragraph("Total Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell15);
        Cell cuCell16 = new Cell();
        cuCell16.setBorder(Border.NO_BORDER);
        cuCell16.add(new Paragraph(": 201,105.00").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cuCell16);
        
        document.add(cusTable);
        
        document.add(new Paragraph(""));
        
        document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
        document.add(new Paragraph(""));
        

        //Creating Benefits Table
        float [] pointColumnWidths3 = {500, 200};
        Table benLivTable = new Table(pointColumnWidths3);
        benLivTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
        
        Cell cell1 = new Cell();
        cell1.setHeight(12);
        cell1.add(new Paragraph("Living Benefits").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benLivTable.addCell(cell1);           
        Cell cell2 = new Cell();
        cell1.setHeight(12);
        cell2.add(new Paragraph("Amount").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benLivTable.addCell(cell2);  
        
        /// for  
        /*
        if(prdcod="AIP"){
        
		} else if(prdcod="INVP"){
		
		} else if(prdcod="DTA" || prdcod="DTAPL"){
		
		}
         */
        benLivTable.startNewRow();
        
        Cell cell3 = new Cell();
        cell3.setHeight(12);
        cell3.add(new Paragraph("Illustrated Maturity Value 9.5%").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
        benLivTable.addCell(cell3);           
        Cell cell4 = new Cell();
        cell4.setHeight(12);
        cell4.add(new Paragraph("309,910.00").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
        benLivTable.addCell(cell4);  
        /////
        /// end for
        /*
        benLivTable.startNewRow();
        
        Cell cell5 = new Cell();
        cell5.add(new Paragraph("Illustrated Maturity Value 11.00%").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
        benLivTable.addCell(cell5);
        Cell cell6 = new Cell();
        cell6.add(new Paragraph("336,271.00").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
        benLivTable.addCell(cell6);
        
        benLivTable.startNewRow();
        
        Cell cell7 = new Cell();
        cell7.add(new Paragraph("Illustrated Maturity Value 12.5%").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
        benLivTable.addCell(cell7);
        Cell cell8 = new Cell();
        cell8.add(new Paragraph("364,474.00").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
        benLivTable.addCell(cell8);
        */
        document.add(benLivTable);
       
        document.add(new Paragraph("Guranteed minimum dividend rate declared for 2016").setFontSize(10));
        document.add(new Paragraph(""));
        
        //Create Additional Benefits Table
        float [] pointColumnWidths4 = {200, 500};
        Table benAddTable = new Table(pointColumnWidths4);
        benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

        Cell abCell1 = new Cell(0, 2);
        abCell1.add(new Paragraph("Additional Benefits").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell1); 
        
        benAddTable.startNewRow();
        
        Cell abCell2 = new Cell();
        abCell2.add(new Paragraph("Accidental Death").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
        benAddTable.addCell(abCell2);
        Cell abCell3 = new Cell();
        abCell3.add(new Paragraph("Twice the value of premium paid or account balance which ever is higher").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
        benAddTable.addCell(abCell3);        
        
        benAddTable.startNewRow();
        
        Cell abCell4 = new Cell();
        abCell4.add(new Paragraph("Natural Death").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
        benAddTable.addCell(abCell4);
        Cell abCell5 = new Cell();
        abCell5.add(new Paragraph("Account balance or 125% of the premium paid, which ever is higher").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
        benAddTable.addCell(abCell5);
        
        document.add(benAddTable);
        
        document.add(new Paragraph("Special Notes").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
        document.add(new Paragraph(""));
        
        //Creating a Special Notes List
        List list = new List(ListNumberingType.DECIMAL);
        list.setFontSize(10);
        ListItem item1 = new ListItem();
        item1.add(new Paragraph("This is an indicative quote only and is valid for 30 days from date of issue.").setFontSize(10));
        list.add(item1);
        
        ListItem item2 = new ListItem();
        item2.add(new Paragraph("All Amounts are in Sri Lankan Rupees (LKR).").setFontSize(10).setFixedLeading(1));
        list.add(item2);
        
        ListItem item3 = new ListItem();
        item3.add(new Paragraph("Initial policy processing fee of Rs 300 (Payable only with initial deposit).").setFontSize(10));
        list.add(item3);
        
        document.add(list);
        
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("This is a mobile generated document and therefore does not require a signature.").setFontSize(6).setBold().setFixedLeading(1));
        
        document.close();
       
		return "success";
	}
	
	

}