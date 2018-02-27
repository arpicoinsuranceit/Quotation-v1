package org.arpicoinsurance.groupit.main.reports.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

//import org.arpicoinsurance.groupit.main.dao.RateCardATFESCDao;
import org.arpicoinsurance.groupit.main.dao.SheduleDao;
//import org.arpicoinsurance.groupit.main.helper.EditQuotation;
//import org.arpicoinsurance.groupit.main.helper.MainLife;
import org.arpicoinsurance.groupit.main.helper.QuoBenf;
import org.arpicoinsurance.groupit.main.helper.QuoChildBenef;
import org.arpicoinsurance.groupit.main.helper.QuoCustomer;
import org.arpicoinsurance.groupit.main.helper.QuotationView;
//import org.arpicoinsurance.groupit.main.helper.Spouse;
//import org.arpicoinsurance.groupit.main.model.CustomerDetails;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.model.Shedule;
import org.arpicoinsurance.groupit.main.reports.QuotationReportService;
//import org.arpicoinsurance.groupit.main.service.Quo_Benef_DetailsService;
//import org.arpicoinsurance.groupit.main.service.impl.Quo_Benef_DetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.io.image.ImageDataFactory;
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
@Transactional
public class QuotationReportServiceImpl implements QuotationReportService {

	private static final String DEST = "./src/main/resources/Reports/";
	public static final String IMG = "./src/main/resources/Reports/logo.png";
	public static final String FONT= "./src/main/resources/Reports/FONTDIR/Times_New_Romance.ttf";
	
	@Autowired
	private SheduleDao sheduleDao;
	

	//Creating Arpico Insurance Plus Report
	@Override
	public String createAIPReport(QuotationDetails quotationDetails, QuotationView quotationView, QuoCustomer quoCustomer) throws Exception {
	
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(quotationDetails.getQuotationquotationCreateDate());
		
	    ArrayList<QuoBenf> benefitsLife = quotationView.getMainLifeBenf();


		//pdf save path
		File file = new File(DEST+"INVP/"+quotationDetails.getQdId().toString()+".pdf");
			
	        if(file.exists()) {
	        	file.delete();
	        }
		    
	        //file output
	        FileOutputStream fos = new FileOutputStream(file);

	        
	        PdfWriter writer = new PdfWriter(fos);
	        PdfDocument pdf = new PdfDocument(writer);
	        Document document = new Document(pdf);
	        
            document.setMargins(30,25,30,25);
	        
	        //Arpico Logo
	        Paragraph pLogo = new Paragraph();
	        Image logo = new Image(ImageDataFactory.create(IMG));
	        logo.setHeight(100);
	        logo.setWidth(120);
	        logo.setFixedPosition(460, 720);
	        pLogo.add(logo);
	        document.add(pLogo);
	        
	        /*Setting new Font
	        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);
	        Paragraph p = new Paragraph("Arpico Insurance PLC Quotation").setFont(font);
	        document.add(p);  */ 
	        
	        document.add(new Paragraph("Arpico Insurance PLC Quotation").setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));

	        document.add(new Paragraph(" "));
	        
	        //Agent Details
	        float [] pointColumnWidths1 = {70, 150}; 
	        Table agtTable = new Table(pointColumnWidths1);
	        agtTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
	        
	        Cell agCell1 = new Cell();
	        agCell1.setBorder(Border.NO_BORDER);
	        agCell1.add(new Paragraph("Date").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        agtTable.addCell(agCell1);
	        Cell agcell2 = new Cell();
	        agcell2.setBorder(Border.NO_BORDER);
	        agcell2.add(new Paragraph(quotationDetails.getQuotationquotationCreateDate()!=null ? ": "+quotationDetails.getQuotationquotationCreateDate() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        agtTable.addCell(agcell2);
	        
	        agtTable.startNewRow();
	        
	        Cell agCell3 = new Cell();
	        agCell3.setBorder(Border.NO_BORDER);
	        agCell3.add(new Paragraph("Branch Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        agtTable.addCell(agCell3);
	        Cell agcell4 = new Cell();
	        agcell4.setBorder(Border.NO_BORDER);
	        agcell4.add(new Paragraph(quotationDetails.getQuotation().getUser().getBranch().getBranch_Name()!=null ? ": "+quotationDetails.getQuotation().getUser().getBranch().getBranch_Name() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        agtTable.addCell(agcell4);
	        
	        agtTable.startNewRow();
	        
	        Cell agCell5 = new Cell();
	        agCell5.setBorder(Border.NO_BORDER);
	        agCell5.add(new Paragraph("Agent Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        agtTable.addCell(agCell5);
	        Cell agcell6 = new Cell();
	        agcell6.setBorder(Border.NO_BORDER);
	        agcell6.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Name()!=null ? ": "+quotationDetails.getQuotation().getUser().getUser_Name() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        agtTable.addCell(agcell6);
	        
	        agtTable.startNewRow();
	        
	        Cell agcell7 = new Cell();
	        agcell7.setBorder(Border.NO_BORDER);
	        agcell7.add(new Paragraph("Agent Code").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        agtTable.addCell(agcell7);
	        Cell agcell8 = new Cell();
	        agcell8.setBorder(Border.NO_BORDER);
	        agcell8.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Code()!=null ? ": "+quotationDetails.getQuotation().getUser().getUser_Code() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        agtTable.addCell(agcell8);
	        
	        agtTable.startNewRow();
	        
	        Cell agcell9 = new Cell();
	        agcell9.setBorder(Border.NO_BORDER);
	        agcell9.add(new Paragraph("Contact No").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        agtTable.addCell(agcell9);
	        
	        Cell agCell10 = new Cell();
	        agCell10.setBorder(Border.NO_BORDER);
        	agCell10.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Mobile()!=null ? ": "+quotationDetails.getQuotation().getUser().getUser_Mobile() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        agtTable.addCell(agCell10);

	        document.add(agtTable);
	        
	        document.add(new Paragraph("ARPICO INVESTMENT PLUS").setFontSize(10).setUnderline().setCharacterSpacing(1));
	        document.add(new Paragraph(""));
	        
	        //customer Details Table
	        float [] pointColumnWidths2 = {170, 200};
	        Table cusTable = new Table(pointColumnWidths2);
	        cusTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
	        
	        Cell cucell1 = new Cell();
	        cucell1.setBorder(Border.NO_BORDER);
	        cucell1.add(new Paragraph("Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        cusTable.addCell(cucell1);
	        Cell cuCell2 = new Cell();
	        cuCell2.setBorder(Border.NO_BORDER);
	        cuCell2.add(new Paragraph(quotationDetails.getCustomerDetails().getCustName()!=null ? ": "+quotationDetails.getCustomerDetails().getCustName() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        cusTable.addCell(cuCell2);
	       
	        cusTable.startNewRow();
	        
	        Cell cucell3 = new Cell();
	        cucell3.setBorder(Border.NO_BORDER);
	        cucell3.add(new Paragraph("Age Next Birthday (Life Assured)").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        cusTable.addCell(cucell3);
	        Cell cuCell4 = new Cell();
	        cuCell4.setBorder(Border.NO_BORDER);
	        if(quoCustomer.getMainLifeAge()!=null) {
		        cuCell4.add(new Paragraph(": "+quoCustomer.getMainLifeAge()).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        }else {
		        cuCell4.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        }
	        cusTable.addCell(cuCell4);
	        
	        cusTable.startNewRow();
	        
	        Cell cucell5 = new Cell();
	        cucell5.setBorder(Border.NO_BORDER);
	        cucell5.add(new Paragraph("Occupation (Life Assured)").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        cusTable.addCell(cucell5);
	        Cell cuCell6 = new Cell();
	        cuCell6.setBorder(Border.NO_BORDER);
	        cuCell6.add(new Paragraph(quotationDetails.getCustomerDetails().getOccupation().getOcupationName()!=null ? ": "+quotationDetails.getCustomerDetails().getOccupation().getOcupationName() : ": " ).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        cusTable.addCell(cuCell6);
	        cusTable.startNewRow();
	        
	        Cell cucell7 = new Cell();
	        cucell7.setBorder(Border.NO_BORDER);
	        cucell7.add(new Paragraph("Term").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        cusTable.addCell(cucell7);
	        Cell cuCell8 = new Cell();
	        cuCell8.setBorder(Border.NO_BORDER);
	        if(quotationDetails.getTopTerm()!=null) {
		        cuCell8.add(new Paragraph(": " + Integer.toString(quotationDetails.getTopTerm())).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        }else {
		        cuCell8.add(new Paragraph(": ").setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        }
	        cusTable.addCell(cuCell8);
	        
	        cusTable.startNewRow();
	        
	        Cell cucell9 = new Cell();
	        cucell9.setBorder(Border.NO_BORDER);
	        cucell9.add(new Paragraph("Mode").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        cusTable.addCell(cucell9);
	        Cell cuCell10 = new Cell();
	        cuCell10.setBorder(Border.NO_BORDER);
	        cuCell10.add(new Paragraph(quotationDetails.getPayMode()!=null ? ": "+quotationDetails.getPayMode() : ": " ).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        cusTable.addCell(cuCell10);
	        
	        cusTable.startNewRow();
	        
	        Cell cucell11 = new Cell();
	        cucell11.setBorder(Border.NO_BORDER);
	        cucell11.add(new Paragraph("Mode Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        cusTable.addCell(cucell11);
	        Cell cuCell12 = new Cell();
	        cuCell12.setBorder(Border.NO_BORDER);
	        if(quoCustomer.getModePremium()!=null) {
		        cuCell12.add(new Paragraph(": " + Double.toString(quoCustomer.getModePremium())).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        }else {
		        cuCell12.add(new Paragraph("").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));

	       }
	        cusTable.addCell(cuCell12);
	        
	        cusTable.startNewRow();
	        
	        Cell cucell13 = new Cell();
	        cucell13.setBorder(Border.NO_BORDER);
	        cucell13.add(new Paragraph("Policy Fees").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        cusTable.addCell(cucell13);
	        Cell cuCell14 = new Cell();
	        cuCell14.setBorder(Border.NO_BORDER);
	        if(quotationDetails.getPolicyFee()!=null) {
		        cuCell14.add(new Paragraph(": "+Double.toString(quotationDetails.getPolicyFee())).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));

	        }else {
		        cuCell14.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));

	        }
	        cusTable.addCell(cuCell14);
	        
	        cusTable.startNewRow();
	        
	        Cell cucell15 = new Cell();
	        cucell15.setBorder(Border.NO_BORDER);
	        cucell15.add(new Paragraph("Total Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        cusTable.addCell(cucell15);
	        Cell cuCell16 = new Cell();
	        cuCell16.setBorder(Border.NO_BORDER);
	        if(quoCustomer.getTotPremium()!=null) {
		        cuCell16.add(new Paragraph(": " +Double.toString(quoCustomer.getTotPremium())).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
	        }else {
		        cuCell16.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));

	        }
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
	        
	        benLivTable.startNewRow();

	        //hard code yet
	        Cell cell3 = new Cell();
	        cell3.add(new Paragraph("Illustrated Maturity Value 9.5%").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
	        benLivTable.addCell(cell3);           
	        Cell cell4 = new Cell();
	        cell4.setHeight(12);
	        cell4.add(new Paragraph("309,910.00").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
	        benLivTable.addCell(cell4);  
	       
	        benLivTable.startNewRow();
	        
	     //hard code yet
	        Cell cell5 = new Cell();
	        cell5.add(new Paragraph("Illustrated Maturity Value 11.00%").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
	        benLivTable.addCell(cell5);           
	        Cell cell6 = new Cell();
	        cell6.add(new Paragraph("336,271.00").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
	        benLivTable.addCell(cell6); 
	       
	        benLivTable.startNewRow();
	        //hard code yet
	        Cell cell7 = new Cell();
	        cell7.add(new Paragraph("Illustrated Maturity Value 12.5%").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
	        benLivTable.addCell(cell7);           
	        Cell cell8 = new Cell();
	        cell8.add(new Paragraph("364,271.00").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
	        benLivTable.addCell(cell8); 
	        
	       
	        document.add(benLivTable);

	        document.add(new Paragraph("Guranteed minimum dividend rate declared for "+calendar.get(Calendar.YEAR)).setFontSize(10));
	        document.add(new Paragraph(""));
	        
	        //Create Additional Benefits Table
	        		
	        float [] pointColumnWidths4 = {300, 500};
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

	     
							
	        document.add(new Paragraph(""));
	        
	        document.add(new Paragraph("Special Notes").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
	        document.add(new Paragraph(""));
	        
	        //Creating a Special Notes List
	        List list = new List(ListNumberingType.DECIMAL);
	        list.setFontSize(10);
	        ListItem item1 = new ListItem();
	        item1.add(new Paragraph("This is an indicative quote only and is valid for 30 days from date of issue.").setFontSize(10).setFixedLeading(2));
	        list.add(item1);
	        
	        ListItem item2 = new ListItem();
	        item2.add(new Paragraph("All Amounts are in Sri Lankan Rupees (LKR).").setFontSize(10).setFixedLeading(2));
	        list.add(item2);
	        
	        ListItem item3 = new ListItem();
	        item3.add(new Paragraph("Initial policy processing fee of Rs 300 (Payable only with initial deposit).").setFontSize(10).setFixedLeading(2));
	        list.add(item3);
	        
	        document.add(list);
	        
	        document.add(new Paragraph("\n"));
	        document.add(new Paragraph("This is a system generated report and therefore does not require a signature.").setFontSize(7).setBold());
	        
	        document.close();
		
	        
			return "AIP Success";
		}
	
	//creating Arpico Investment Bond Report
	@Override
	public String createAIBReport(QuotationDetails quotationDetails, QuotationView quotationView, QuoCustomer quoCustomer) throws Exception {
		//create mainlife details object
				QuoCustomer customer = quotationView.getCustDetails();

				//pdf save path
				File file = new File(DEST+"INVP/"+quotationDetails.getQdId().toString()+".pdf");
					
			        if(file.exists()) {
			        	file.delete();
			        }
				    
			        //file output
			        FileOutputStream fos = new FileOutputStream(file);

			        
			        PdfWriter writer = new PdfWriter(fos);
			        PdfDocument pdf = new PdfDocument(writer);
			        Document document = new Document(pdf);
			        
		            document.setMargins(30,25,30,25);
			        
			        //Arpico Logo
			        Paragraph pLogo = new Paragraph();
			        Image logo = new Image(ImageDataFactory.create(IMG));
			        logo.setHeight(100);
			        logo.setWidth(120);
			        logo.setFixedPosition(460, 720);
			        pLogo.add(logo);
			        document.add(pLogo);
			        
			        /*Setting new Font
			        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);
			        Paragraph p = new Paragraph("Arpico Insurance PLC Quotation").setFont(font);
			        document.add(p);  */ 
			        
			        document.add(new Paragraph("Arpico Insurance PLC Quotation").setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));

			        document.add(new Paragraph(" "));
			        
			        //Agent Details
			        float [] pointColumnWidths1 = {70, 150}; 
			        Table agtTable = new Table(pointColumnWidths1);
			        agtTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
			        
			        Cell agCell1 = new Cell();
			        agCell1.setBorder(Border.NO_BORDER);
			        agCell1.add(new Paragraph("Date").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        agtTable.addCell(agCell1);
			        Cell agcell2 = new Cell();
			        agcell2.setBorder(Border.NO_BORDER);
			        agcell2.add(new Paragraph(quotationDetails.getQuotationquotationCreateDate()!=null ? ": "+quotationDetails.getQuotationquotationCreateDate() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        agtTable.addCell(agcell2);
			        
			        agtTable.startNewRow();
			        
			        Cell agCell3 = new Cell();
			        agCell3.setBorder(Border.NO_BORDER);
			        agCell3.add(new Paragraph("Branch Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        agtTable.addCell(agCell3);
			        Cell agcell4 = new Cell();
			        agcell4.setBorder(Border.NO_BORDER);
			        agcell4.add(new Paragraph(quotationDetails.getQuotation().getUser().getBranch().getBranch_Name()!=null ? ": "+quotationDetails.getQuotation().getUser().getBranch().getBranch_Name() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        agtTable.addCell(agcell4);
			        
			        agtTable.startNewRow();
			        
			        Cell agCell5 = new Cell();
			        agCell5.setBorder(Border.NO_BORDER);
			        agCell5.add(new Paragraph("Agent Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        agtTable.addCell(agCell5);
			        Cell agcell6 = new Cell();
			        agcell6.setBorder(Border.NO_BORDER);
			        agcell6.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Name()!=null ? ": "+quotationDetails.getQuotation().getUser().getUser_Name() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        agtTable.addCell(agcell6);
			        
			        agtTable.startNewRow();
			        
			        Cell agcell7 = new Cell();
			        agcell7.setBorder(Border.NO_BORDER);
			        agcell7.add(new Paragraph("Agent Code").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        agtTable.addCell(agcell7);
			        Cell agcell8 = new Cell();
			        agcell8.setBorder(Border.NO_BORDER);
			        agcell8.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Code()!=null ? ": "+quotationDetails.getQuotation().getUser().getUser_Code() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        agtTable.addCell(agcell8);
			        
			        agtTable.startNewRow();
			        
			        Cell agcell9 = new Cell();
			        agcell9.setBorder(Border.NO_BORDER);
			        agcell9.add(new Paragraph("Contact No").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        agtTable.addCell(agcell9);
			        
			        Cell agCell10 = new Cell();
			        agCell10.setBorder(Border.NO_BORDER);
		        	agCell10.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Mobile()!=null ? ": "+quotationDetails.getQuotation().getUser().getUser_Mobile() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        agtTable.addCell(agCell10);

			        document.add(agtTable);
			        
			        document.add(new Paragraph("ARPICO INVESTMENT BOND").setFontSize(10).setUnderline().setCharacterSpacing(1));
			        document.add(new Paragraph(""));
			        
			        //customer Details Table
			        float [] pointColumnWidths2 = {170, 200};
			        Table cusTable = new Table(pointColumnWidths2);
			        cusTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
			        
			        Cell cucell1 = new Cell();
			        cucell1.setBorder(Border.NO_BORDER);
			        cucell1.add(new Paragraph("Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        cusTable.addCell(cucell1);
			        Cell cuCell2 = new Cell();
			        cuCell2.setBorder(Border.NO_BORDER);
			        //cuCell2.add(new Paragraph(customer.getMainLifeName()!=null ? ": "+customer.getMainLifeName() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        cusTable.addCell(cuCell2);
			       
			        cusTable.startNewRow();
			        
			        Cell cucell3 = new Cell();
			        cucell3.setBorder(Border.NO_BORDER);
			        cucell3.add(new Paragraph("Age Next Birthday (Life Assured)").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        cusTable.addCell(cucell3);
			        Cell cuCell4 = new Cell();
			        cuCell4.setBorder(Border.NO_BORDER);
			        if(customer.getMainLifeAge()!=null) {
				        cuCell4.add(new Paragraph(": " + Integer.toString(customer.getMainLifeAge())).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        }else {
				        cuCell4.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
			        }
			        cusTable.addCell(cuCell4);
			        
			        cusTable.startNewRow();
			        
			        Cell cucell5 = new Cell();
			        cucell5.setBorder(Border.NO_BORDER);
			        cucell5.add(new Paragraph("Occupation (Life Assured)").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        cusTable.addCell(cucell5);
			        Cell cuCell6 = new Cell();
			        cuCell6.setBorder(Border.NO_BORDER);
			        cuCell6.add(new Paragraph(customer.getMainLifeOccupation()!=null ? ": "+customer.getMainLifeOccupation() : ": " ).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        cusTable.addCell(cuCell6);
			        
			        cusTable.startNewRow();
			        
			        Cell cucell7 = new Cell();
			        cucell7.setBorder(Border.NO_BORDER);
			        cucell7.add(new Paragraph("Term").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        cusTable.addCell(cucell7);
			        Cell cuCell8 = new Cell();
			        cuCell8.setBorder(Border.NO_BORDER);
			        if(customer.getTerm()!=null) {
				        cuCell8.add(new Paragraph(": " + Integer.toString(customer.getTerm())).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        }else {
				        cuCell8.add(new Paragraph(": ").setTextAlignment(TextAlignment.LEFT));
			        }
			        cusTable.addCell(cuCell8);
			        
			        cusTable.startNewRow();
			        
			        Cell cucell9 = new Cell();
			        cucell9.setBorder(Border.NO_BORDER);
			        cucell9.add(new Paragraph("Mode").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        cusTable.addCell(cucell9);
			        Cell cuCell10 = new Cell();
			        cuCell10.setBorder(Border.NO_BORDER);
			        cuCell10.add(new Paragraph(customer.getMode()!=null ? ": "+customer.getMode() : ": " ).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        cusTable.addCell(cuCell10);
			        
			        cusTable.startNewRow();
			        
			        Cell cucell11 = new Cell();
			        cucell11.setBorder(Border.NO_BORDER);
			        cucell11.add(new Paragraph("Mode Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        cusTable.addCell(cucell11);
			        Cell cuCell12 = new Cell();
			        cuCell12.setBorder(Border.NO_BORDER);
			        if(customer.getModePremium()!=null) {
				        cuCell12.add(new Paragraph(": " + Double.toString(customer.getModePremium())).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        }else {
				        cuCell12.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));

			        }
			        cusTable.addCell(cuCell12);
			        
			        cusTable.startNewRow();
			        
			        Cell cucell13 = new Cell();
			        cucell13.setBorder(Border.NO_BORDER);
			        cucell13.add(new Paragraph("Policy Fees").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        cusTable.addCell(cucell13);
			        Cell cuCell14 = new Cell();
			        cuCell14.setBorder(Border.NO_BORDER);
			        cuCell14.add(new Paragraph(": 300.00").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        cusTable.addCell(cuCell14);
			        
			        cusTable.startNewRow();
			        
			        Cell cucell15 = new Cell();
			        cucell15.setBorder(Border.NO_BORDER);
			        cucell15.add(new Paragraph("Total Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        cusTable.addCell(cucell15);
			        Cell cuCell16 = new Cell();
			        cuCell16.setBorder(Border.NO_BORDER);
			        if(customer.getTotPremium()!=null) {
				        cuCell16.add(new Paragraph(": " +Double.toString(customer.getTotPremium())).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
			        }else {
				        cuCell16.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));

			        }
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
			        
			        benLivTable.startNewRow();
			        //hard code yet
			        Cell cell3 = new Cell();
			        cell3.add(new Paragraph("Guranteed Maturity Value 5.00%").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
			        benLivTable.addCell(cell3);           
			        Cell cell4 = new Cell();
			        cell4.setHeight(12);
			        cell4.add(new Paragraph("309,910.00").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
			        benLivTable.addCell(cell4);  
			       
			       
			        document.add(benLivTable);
			       
			        document.add(new Paragraph(""));
			        
			        //Create Additional Benefits Table
			        		
			        float [] pointColumnWidths4 = {300, 500};
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
			     	abCell3.add(new Paragraph("50% of the premium (sum invested) or 500,000 whichever is lower together with premium and accumilated amount").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
			     	benAddTable.addCell(abCell3);
			     	 	    
			     	benAddTable.startNewRow();
			    		    
			     	Cell abCell4 = new Cell();
			     	abCell4.add(new Paragraph("Natural Death").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
			     	benAddTable.addCell(abCell4);
			     	Cell abCell5 = new Cell();
			     	abCell5.add(new Paragraph("20% of the premium (sum invested) or 250,000 whichever is lower together with premium and accumilated amount").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
			     	benAddTable.addCell(abCell5);
			     	
			     	document.add(benAddTable);

			     
									
			        document.add(new Paragraph(""));
			        
			        document.add(new Paragraph("Special Notes").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
			        document.add(new Paragraph(""));
			        
			        //Creating a Special Notes List
			        List list = new List(ListNumberingType.DECIMAL);
			        list.setFontSize(10);
			        ListItem item1 = new ListItem();
			        item1.add(new Paragraph("This is an indicative quote only and is valid for 30 days from date of issue.").setFontSize(10).setFixedLeading(1));
			        list.add(item1);
			        
			        ListItem item2 = new ListItem();
			        item2.add(new Paragraph("All Amounts are in Sri Lankan Rupees (LKR).").setFontSize(10).setFixedLeading(1));
			        list.add(item2);
			        
			        ListItem item3 = new ListItem();
			        item3.add(new Paragraph("Initial policy processing fee of Rs 300 (Payable only with initial deposit).").setFontSize(10).setFixedLeading(1));
			        list.add(item3);
			        
			        document.add(list);
			        
			        document.add(new Paragraph("\n"));
			        document.add(new Paragraph("This is a system generated report and therefore does not require a signature.").setFontSize(7).setBold());
			        
			        document.close();
			        
			        return "AIB Success";
	}


	//Create Arpico Investment Plan Report
	@Override
	public String createINVPReport(QuotationDetails quotationDetails, QuotationView quotationView, QuoCustomer quoCustomer) throws Exception {
		
		QuoCustomer customer = quotationView.getCustDetails();
		ArrayList<QuoBenf> benefitsLife = quotationView.getMainLifeBenf();
		ArrayList<QuoBenf> benefitsSpouse = quotationView.getSpouseBenf();
		ArrayList<QuoChildBenef> benefitsChild = quotationView.getChildBenf();
				
		File file = new File(DEST+"INVP/"+quotationDetails.getQdId().toString()+".pdf");
		
        if(file.exists()) {
        	file.delete();
        }
	    
        FileOutputStream fos = new FileOutputStream(DEST+"INVP/"+quotationDetails.getQdId().toString()+".pdf");
        
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        document.setMargins(25,25,30,25);
        
        //logo
        Paragraph pLogo = new Paragraph();
        Image logo = new Image(ImageDataFactory.create(IMG));
        logo.setHeight(100);
        logo.setWidth(120);
        logo.setFixedPosition(460, 720);
        pLogo.add(logo);
        document.add(pLogo);
    
        document.add(new Paragraph("Arpico Insurance PLC Quotation").setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        document.add(new Paragraph(" "));
        
        //Agent Details
        float [] pointColumnWidths1 = {70, 150}; 
        Table agtTable = new Table(pointColumnWidths1);
        agtTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
        
        Cell agCell1 = new Cell();
        agCell1.setBorder(Border.NO_BORDER);
        agCell1.add(new Paragraph("Date").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
        agtTable.addCell(agCell1);
        Cell agcell2 = new Cell();
        agcell2.setBorder(Border.NO_BORDER);
        agcell2.add(new Paragraph(quotationDetails.getQuotationquotationCreateDate()!=null ? ": "+quotationDetails.getQuotationquotationCreateDate() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
        agtTable.addCell(agcell2);
        agtTable.startNewRow();
        
        Cell agCell3 = new Cell();
        agCell3.setBorder(Border.NO_BORDER);
        agCell3.add(new Paragraph("Branch Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
        agtTable.addCell(agCell3);
        Cell agcell4 = new Cell();
        agcell4.setBorder(Border.NO_BORDER);
        agcell4.add(new Paragraph(quotationDetails.getQuotation().getUser().getBranch().getBranch_Name()!=null ? ": "+quotationDetails.getQuotation().getUser().getBranch().getBranch_Name() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
        agtTable.addCell(agcell4);
        
        agtTable.startNewRow();
        
        Cell agCell5 = new Cell();
        agCell5.setBorder(Border.NO_BORDER);
        agCell5.add(new Paragraph("Agent Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
        agtTable.addCell(agCell5);
        Cell agcell6 = new Cell();
        agcell6.setBorder(Border.NO_BORDER);
        agcell6.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Name()!=null ? ": "+quotationDetails.getQuotation().getUser().getUser_Name() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
        agtTable.addCell(agcell6);
        
        agtTable.startNewRow();
        
        Cell agcell7 = new Cell();
        agcell7.setBorder(Border.NO_BORDER);
        agcell7.add(new Paragraph("Agent Code").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
        agtTable.addCell(agcell7);
        Cell agcell8 = new Cell();
        agcell8.setBorder(Border.NO_BORDER);
        agcell8.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Code()!=null ? ": "+quotationDetails.getQuotation().getUser().getUser_Code() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
        agtTable.addCell(agcell8);
        
        agtTable.startNewRow();
        
        Cell agcell9 = new Cell();
        agcell9.setBorder(Border.NO_BORDER);
        agcell9.add(new Paragraph("Contact No").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
        agtTable.addCell(agcell9);
        
        Cell agCell10 = new Cell();
        agCell10.setBorder(Border.NO_BORDER);
        agCell10.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Mobile()!=null ? ": "+quotationDetails.getQuotation().getUser().getUser_Mobile() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(8));
        agtTable.addCell(agCell10);
        
        document.add(agtTable);
        
        //checking arpico investment plan or super investment plan
        if(quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("INVP")) {
            document.add(new Paragraph("ARPICO INVESTMENT PLAN").setFontSize(10).setUnderline().setCharacterSpacing(1));
        }else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("ASIP")) {
            document.add(new Paragraph("ARPICO SUPER INVESTMENT PLAN").setFontSize(10).setUnderline().setCharacterSpacing(1));
		}
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
        cuCell2.add(new Paragraph(customer.getMainLifeName()!=null ? ": "+customer.getMainLifeName() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cuCell2);

        cusTable.startNewRow();
        
        Cell cucell3 = new Cell();
        cucell3.setBorder(Border.NO_BORDER);
        cucell3.add(new Paragraph("Age Next Birthday (Life Assured)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell3);
        Cell cuCell4 = new Cell();
        cuCell4.setBorder(Border.NO_BORDER);
        if (customer.getMainLifeAge()!=null) {
            cuCell4.add(new Paragraph(": " + Integer.toString(customer.getMainLifeAge())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
		}else {
	        cuCell4.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));

		}
        cusTable.addCell(cuCell4);
        
        cusTable.startNewRow();
      
      	Cell cucell5 = new Cell();
        cucell5.setBorder(Border.NO_BORDER);
        cucell5.add(new Paragraph("Occupation (Life Assured)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell5);
        Cell cuCell6 = new Cell();
        cuCell6.setBorder(Border.NO_BORDER);
        cuCell6.add(new Paragraph(customer.getMainLifeOccupation()!=null ? ": "+customer.getMainLifeOccupation() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cuCell6);

        cusTable.startNewRow();
        
        //checking Spouse is active or not
        if((customer.getSpouseName())!=null) {
        	
            Cell cucell7 = new Cell();
            cucell7.setBorder(Border.NO_BORDER);
            cucell7.add(new Paragraph("Name Of Spouse").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell7);
            Cell cuCell8 = new Cell();
            cuCell8.setBorder(Border.NO_BORDER);
            cuCell8.add(new Paragraph(": " +customer.getSpouseName()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cuCell8);
        
        }else {
        	
        }
        
        cusTable.startNewRow();
      
        if((customer.getSpouseAge())!=null) {
        	  
            Cell cucell9 = new Cell();
            cucell9.setBorder(Border.NO_BORDER);
            cucell9.add(new Paragraph("Age Next Birthday (Spouse)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell9);
            Cell cuCell10 = new Cell();
            cuCell10.setBorder(Border.NO_BORDER);
            cuCell10.add(new Paragraph(": " + Integer.toString(customer.getSpouseAge())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cuCell10);

        }else {

        }
        
        cusTable.startNewRow();
       
        if((customer.getSpouseOccupation())!=null) {
        	 
            Cell cucell11 = new Cell();
            cucell11.setBorder(Border.NO_BORDER);
            cucell11.add(new Paragraph("Occupation (Spouse)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell11);
            Cell cuCell12 = new Cell();
            cuCell12.setBorder(Border.NO_BORDER);
            cuCell12.add(new Paragraph(": " + customer.getSpouseOccupation()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cuCell12);

        }else {

        }
        
        cusTable.startNewRow();
        
        Cell cucell13 = new Cell();
        cucell13.setBorder(Border.NO_BORDER);
        cucell13.add(new Paragraph("Term").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell13);
        Cell cuCell14 = new Cell();
        cuCell14.setBorder(Border.NO_BORDER);
        if((customer.getTerm())!=null) {
            cuCell14.add(new Paragraph(": " + Integer.toString(customer.getTerm())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }else {
            cuCell14.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }
        cusTable.addCell(cuCell14);

        cusTable.startNewRow();
        
        Cell cucell15 = new Cell();
        cucell15.setBorder(Border.NO_BORDER);
        cucell15.add(new Paragraph("Mode").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell15);
        Cell cuCell16 = new Cell();
        cuCell16.setBorder(Border.NO_BORDER);
        if((customer.getMode())!=null) {
             cuCell16.add(new Paragraph(": " +customer.getMode()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }else {
            cuCell16.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));

        }
        cusTable.addCell(cuCell16);

        cusTable.startNewRow();

        Cell cucell17 = new Cell();
        cucell17.setBorder(Border.NO_BORDER);
        cucell17.add(new Paragraph("Mode Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell17);
        Cell cuCell18 = new Cell();
        cuCell18.setBorder(Border.NO_BORDER);
        if((customer.getModePremium())!=null) {
            cuCell18.add(new Paragraph(": " +Double.toString(customer.getModePremium())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }else {
            cuCell18.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }
        cusTable.addCell(cuCell18);

        document.add(cusTable);
        
        document.add(new Paragraph(""));
        
        document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
        document.add(new Paragraph(""));
       
        //Create Additional Benefits Table
        float [] pointColumnWidths4 = {450, 150, 150};
        Table benAddTable = new Table(pointColumnWidths4);
        benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

        Cell abCell1 = new Cell();
        abCell1.add(new Paragraph("Additional Benefits").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell1); 
        Cell abCell2 = new Cell();
        abCell2.add(new Paragraph("Amount").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell2); 
        Cell abCell3 = new Cell();
        abCell3.add(new Paragraph("Premium").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell3); 
        
        benAddTable.startNewRow();
        
        Cell abCell4 = new Cell(0,3);
        abCell4.add(new Paragraph("Main Life").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell4);
        
        benAddTable.startNewRow();
        
        //checking main life benefits having or not
        for (QuoBenf quoBenf : benefitsLife) {
        	
        	if(quoBenf.getBenfName()!=null) {
        		
                Cell abCell5 = new Cell();
                abCell5.add(new Paragraph(quoBenf.getBenfName()).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
         	    benAddTable.addCell(abCell5);
         	    Cell abCell6 = new Cell();
         	    abCell6.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
         	    benAddTable.addCell(abCell6);
         	    Cell abCell7 = new Cell();
         	    abCell7.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
         	    benAddTable.addCell(abCell7);
         	    
         	    benAddTable.startNewRow();
        	
        	}else {
        		
        	}
        
		}
			
        benAddTable.startNewRow();
    	
		Cell abCell8 = new Cell(0,3);
	    abCell8.add(new Paragraph("Spouse").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
	    benAddTable.addCell(abCell8);
	        
	    benAddTable.startNewRow();
	    
        //checking spouse having benefits
        for (QuoBenf quoBenf  : benefitsSpouse) {
        	
        	if(quoBenf.getBenfName()!=null) {
        	
        	    Cell abCell9 = new Cell();
        	    abCell9.add(new Paragraph(quoBenf.getBenfName()).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
        	    benAddTable.addCell(abCell9);
        	    Cell abCell10 = new Cell();
        	    abCell10.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
        	    benAddTable.addCell(abCell10);
        	    Cell abCell11 = new Cell();
        	    abCell11.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
        	    benAddTable.addCell(abCell11);
        	    
        	    benAddTable.startNewRow();
        	}else {
        		
        	}
       
		}
        
        benAddTable.startNewRow();
        
		Cell abCell12 = new Cell(0,3);
	    abCell12.add(new Paragraph("Children").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
	    benAddTable.addCell(abCell12);
	    
	    benAddTable.startNewRow();

        for (QuoChildBenef quoChild : benefitsChild) {
        	
        	if(quoChild.getChild().getChildName()!=null) {
        		
        	    Cell abCell13 = new Cell(0,3);
        	    abCell13.add(new Paragraph("Name : "+quoChild.getChild().getChildName()+ " Relationship : "+quoChild.getChild().getChildRelation()).setFontSize(8).setBold().setTextAlignment(TextAlignment.LEFT).setCharacterSpacing(1));
        	    benAddTable.addCell(abCell13);
        	    benAddTable.startNewRow();
        	      
        	    	//loop again to get children benf
        	         for (QuoChildBenef quoChildBenef : benefitsChild) {
        	        	 Cell abCell14 = new Cell();
        	             abCell14.add(new Paragraph(quoChildBenef.getBenfs().get(0).getBenfName()).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
        	             benAddTable.addCell(abCell14);
        	             Cell abCell15 = new Cell();
        	             abCell15.add(new Paragraph(Double.toString(quoChildBenef.getBenfs().get(0).getRiderSum())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
        	             benAddTable.addCell(abCell15);
        	             Cell abCell16 = new Cell();
        	             abCell16.add(new Paragraph(Double.toString(quoChild.getBenfs().get(0).getPremium())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
        	             benAddTable.addCell(abCell16);
        	             benAddTable.startNewRow();
        				}
        	        	
        	}else {
        		
        	}
       
		}
        
        document.add(benAddTable);
        
        document.add(new Paragraph(""));
        document.add(new Paragraph("Special Notes").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
        document.add(new Paragraph(""));
        
        //Creating a Special Notes List
        List list = new List(ListNumberingType.DECIMAL);
        list.setFontSize(10);
        ListItem item1 = new ListItem();
        item1.add(new Paragraph("If HRB / SUHRB is obtained, the total cover will be applicable for the whole family per policy year.").setFontSize(10).setFixedLeading(1));
        list.add(item1);
        
        ListItem item2 = new ListItem();
        item2.add(new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.").setFontSize(10).setFixedLeading(1));
        list.add(item2);
        
        ListItem item3 = new ListItem();
        item3.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date issue.").setFontSize(10).setFixedLeading(1));
        list.add(item3);
        
        ListItem item4 = new ListItem();
        item4.add(new Paragraph("All amounts are in Sri Lankan Rupees(LKR).").setFontSize(10).setFixedLeading(1));
        list.add(item4);
        
        ListItem item5 = new ListItem();
        item5.add(new Paragraph("In event of death by accident both Accident Cover and Natural Death Cover will be applicable.").setFontSize(10).setFixedLeading(1));
        list.add(item5);
        
        ListItem item6 = new ListItem();
        item6.add(new Paragraph("Guranteed minimum dividend rate declared for 2016 - 7.25%").setFontSize(10).setFixedLeading(1));
        list.add(item6);
        
        document.add(list);
        
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("This is a system generated report and therefore does not require a signature.").setFontSize(7).setBold());
        
        document.close();
		
		return "INVP Success";
	
	}

	//Creating DTA Report
	@Override
	public String createDTAReport(QuotationDetails quotationDetails, QuotationView quotationView, QuoCustomer quoCustomer) throws Exception {
		
		QuoCustomer customer = quotationView.getCustDetails();
		ArrayList<QuoBenf> benefitsLife = quotationView.getMainLifeBenf();
		java.util.List<Shedule> shedules = sheduleDao.findByQuotationDetails(quotationDetails);
		
		
		File file = new File(DEST+"INVP/"+quotationDetails.getQdId().toString()+".pdf");
		
        if(file.exists()) {
        	file.delete();
        }
	    
        FileOutputStream fos = new FileOutputStream(DEST+"INVP/"+quotationDetails.getQdId().toString()+".pdf");

        
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        
        document.setMargins(30,25,30,25);
        
        //logo
        Paragraph pLogo = new Paragraph();
        Image logo = new Image(ImageDataFactory.create(IMG));
        logo.setHeight(100);
        logo.setWidth(120);
        logo.setFixedPosition(460, 720);
        pLogo.add(logo);
        document.add(pLogo);
    
        
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
        agcell2.add(new Paragraph(": " +quotationDetails.getQuotationquotationCreateDate()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell2);
        
        agtTable.startNewRow();
        
        Cell agCell3 = new Cell();
        agCell3.setBorder(Border.NO_BORDER);
        agCell3.add(new Paragraph("Branch Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agCell3);
        Cell agcell4 = new Cell();
        agcell4.setBorder(Border.NO_BORDER);
        agcell4.add(new Paragraph(": " +quotationDetails.getQuotation().getUser().getBranch().getBranch_Name()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell4);
        
        agtTable.startNewRow();
        
        Cell agCell5 = new Cell();
        agCell5.setBorder(Border.NO_BORDER);
        agCell5.add(new Paragraph("Agent Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agCell5);
        Cell agcell6 = new Cell();
        agcell6.setBorder(Border.NO_BORDER);
        agcell6.add(new Paragraph(": " +quotationDetails.getQuotation().getUser().getUser_Name()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell6);
        
        agtTable.startNewRow();
        
        Cell agcell7 = new Cell();
        agcell7.setBorder(Border.NO_BORDER);
        agcell7.add(new Paragraph("Agent Code").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell7);
        Cell agcell8 = new Cell();
        agcell8.setBorder(Border.NO_BORDER);
        agcell8.add(new Paragraph(": " +quotationDetails.getQuotation().getUser().getUser_Code()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell8);
        
        agtTable.startNewRow();
        
        Cell agcell9 = new Cell();
        agcell9.setBorder(Border.NO_BORDER);
        agcell9.add(new Paragraph("Contact No").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell9);
        
        Cell agCell10 = new Cell();
        agCell10.setBorder(Border.NO_BORDER);
        agCell10.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Mobile()!=null ? ": "+quotationDetails.getQuotation().getUser().getUser_Mobile() : ": " ).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agCell10);
        
        document.add(agtTable);
        
        if(quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("DTA")) {
            document.add(new Paragraph("ARPICO DECRASING TERM ASSURANCE FOR HOUSING LOAN").setFontSize(10).setUnderline().setCharacterSpacing(1));
        }else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("DTAPL")) {
            document.add(new Paragraph("ARPICO DECRASING TERM ASSURANCE FOR PERSONAL LOAN").setFontSize(10).setUnderline().setCharacterSpacing(1));
		}
        
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
        	cuCell2.add(new Paragraph(customer.getMainLifeName() != null ? ": " +customer.getMainLifeName() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cuCell2);
      
        cusTable.startNewRow();
        
        	Cell cucell3 = new Cell();
	        cucell3.setBorder(Border.NO_BORDER);
	        cucell3.add(new Paragraph("Age Next Birthday (Life Assured)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
	        cusTable.addCell(cucell3);
	        Cell cuCell4 = new Cell();
	        cuCell4.setBorder(Border.NO_BORDER);
	        if(customer.getMainLifeAge()!=null) {
		        cuCell4.add(new Paragraph(": " + Integer.toString(customer.getMainLifeAge())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));

	        }else {
	        	
	        }
            cusTable.addCell(cuCell4);
     
        cusTable.startNewRow();
      
	    	Cell cucell5 = new Cell();
	        cucell5.setBorder(Border.NO_BORDER);
	        cucell5.add(new Paragraph("Occupation (Life Assured)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
	        cusTable.addCell(cucell5);
	        Cell cuCell6 = new Cell();
	        cuCell6.setBorder(Border.NO_BORDER);	 
	        cuCell6.add(new Paragraph(customer.getMainLifeOccupation() !=null ? ": "+customer.getMainLifeOccupation() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
	        cusTable.addCell(cuCell6);

       cusTable.startNewRow();
        
            Cell cucell7 = new Cell();
            cucell7.setBorder(Border.NO_BORDER);
            cucell7.add(new Paragraph("Interest Rate").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell7);
            Cell cuCell8 = new Cell();
            cuCell8.setBorder(Border.NO_BORDER);
            if(quotationDetails.getInterestRate()!=null) {
                cuCell8.add(new Paragraph(": " +Double.toString(quotationDetails.getInterestRate())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));

            }else {
            	
            }
            cusTable.addCell(cuCell8);
        
        cusTable.startNewRow();
      
            Cell cucell9 = new Cell();
            cucell9.setBorder(Border.NO_BORDER);
            cucell9.add(new Paragraph("Loan Amount").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell9);
            Cell cuCell10 = new Cell();
            cuCell10.setBorder(Border.NO_BORDER);
            if(quotationDetails.getBaseSum()!=null) {
                cuCell10.add(new Paragraph(": " +Double.toString(quotationDetails.getBaseSum())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));

            }else {
            	
            }
            cusTable.addCell(cuCell10);

        cusTable.startNewRow();
             
            Cell cucell11 = new Cell();
            cucell11.setBorder(Border.NO_BORDER);
            cucell11.add(new Paragraph("Term").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell11);
            Cell cuCell12 = new Cell();
            cuCell12.setBorder(Border.NO_BORDER);
            if(customer.getTerm()!=null) {
                cuCell12.add(new Paragraph(": " + Integer.toString(customer.getTerm())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));

            }else {
            	
            }
            cusTable.addCell(cuCell12);
     
        cusTable.startNewRow();
        
        	 Cell cucell13 = new Cell();
        	 cucell13.setBorder(Border.NO_BORDER);
        	 cucell13.add(new Paragraph("Mode").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
             cusTable.addCell(cucell13);
             Cell cuCell14 = new Cell();
             cuCell14.setBorder(Border.NO_BORDER);
             cuCell14.add(new Paragraph(customer.getMode()!=null ? ": "+customer.getMode() : ": " ).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
             cusTable.addCell(cuCell14);
    
        cusTable.startNewRow();

        	Cell cucell15 = new Cell();
        	cucell15.setBorder(Border.NO_BORDER);
        	cucell15.add(new Paragraph("Mode Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell15);
            Cell cuCell16 = new Cell();
            cuCell16.setBorder(Border.NO_BORDER);
         if(customer.getModePremium()!=null) {
        	 cuCell16.add(new Paragraph(": " +Double.toString(customer.getModePremium())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
         }else {
        	 cuCell16.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));

         }
         cusTable.addCell(cuCell16);

           
        document.add(cusTable);
        
        document.add(new Paragraph(""));
        
        document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
        document.add(new Paragraph(""));
       
        
        //Create Additional Benefits Table
        float [] pointColumnWidths4 = {450, 150, 150};
        Table benAddTable = new Table(pointColumnWidths4);
        benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

        Cell abCell1 = new Cell();
        abCell1.add(new Paragraph("Additional Benefits").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell1); 
        Cell abCell2 = new Cell();
        abCell2.add(new Paragraph("Amount").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell2); 
        Cell abCell3 = new Cell();
        abCell3.add(new Paragraph("Premium").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell3); 
        
        benAddTable.startNewRow();
        
        Cell abCell4 = new Cell(0,3);
        abCell4.add(new Paragraph("Main Life").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell4);
        
        benAddTable.startNewRow();
        
        Cell abCell5 = new Cell();
     	abCell5.add(new Paragraph("Basic Sum Assured").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
	    benAddTable.addCell(abCell5);
	    Cell abCell6 = new Cell();
	    if(quotationDetails.getBaseSum()!=null) {
		    abCell6.add(new Paragraph(Double.toString(quotationDetails.getBaseSum())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

	    }else {
		    abCell6.add(new Paragraph("null ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
	    }
	    benAddTable.addCell(abCell6);
	    Cell abCell7 = new Cell();
	    if(customer.getMode().equalsIgnoreCase("Q")) {
	    	if(quotationDetails.getPremiumMonth()!=null) {
			    abCell7.add(new Paragraph(Double.toString(quotationDetails.getPremiumQuater())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
	    	}else {
	    		
	    	}
	    }else if (customer.getMode().equalsIgnoreCase("M")) {
	    	if(quotationDetails.getPremiumMonth()!=null) {
			    abCell7.add(new Paragraph(Double.toString(quotationDetails.getPremiumMonth())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
	    	}else {
	    	
	    	}
		}else if (customer.getMode().equalsIgnoreCase("Y")) {
			if(quotationDetails.getPremiumYear()!=null) {
			    abCell7.add(new Paragraph(Double.toString(quotationDetails.getPremiumYear())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
	    	}else {
	    	
	    	}
		}else if (customer.getMode().equalsIgnoreCase("S")) {
			if(quotationDetails.getPremiumSingle()!=null) {
			    abCell7.add(new Paragraph(Double.toString(quotationDetails.getPremiumSingle())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
	    	}else {

	    	}
		}else if (customer.getMode().equalsIgnoreCase("H")) {
			if(quotationDetails.getPremiumHalf()!=null) {
			    abCell7.add(new Paragraph(Double.toString(quotationDetails.getPremiumHalf())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
	    	}else {
	    	
	    	}
		}
	    
	    benAddTable.addCell(abCell7);
        
        benAddTable.startNewRow();
        
        	for (QuoBenf quoBenf : benefitsLife) {
             	
            Cell abCell8 = new Cell();
            if(quoBenf.getBenfName()!=null) {
                abCell8.add(new Paragraph(quoBenf.getBenfName()).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
            }else {
				
			}
      	    benAddTable.addCell(abCell8);
      	    
      	    Cell abCell9 = new Cell();
      	    if(quoBenf.getRiderSum()!=null) {
          	    abCell9.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
      	    }else {
      	    	
      	    }
      	    benAddTable.addCell(abCell9);
      	    
      	    Cell abCell10 = new Cell();
      	    if(quoBenf.getPremium()!=null) {
          	    abCell10.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
      	    }else {

      	    }
    	    benAddTable.addCell(abCell10); 
      	    	
      	    benAddTable.startNewRow();
     		
        }
     
        document.add(benAddTable);
        
        document.add(new Paragraph(""));
        
        document.add(new Paragraph("Schedule").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
        
        document.add(new Paragraph(""));

        float [] pointColumnWidths5 = {70, 100, 100}; 
        Table scdTable = new Table(pointColumnWidths5);
        scdTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
        
        Cell sdCell1 = new Cell();
        sdCell1.add(new Paragraph("Year").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        scdTable.addCell(sdCell1);
        Cell sdCell2 = new Cell();
        sdCell2.add(new Paragraph("Loan Balance").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        scdTable.addCell(sdCell2);
        Cell sdCell3 = new Cell();
        sdCell3.add(new Paragraph("Loan Protection").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        scdTable.addCell(sdCell3);
        
        scdTable.startNewRow();
        
        for (Shedule polShdl : shedules) {
        	Cell sdCell4 = new Cell();
        	if(polShdl.getPolicyYear()!=null) {
                sdCell4.add(new Paragraph(Integer.toString(polShdl.getPolicyYear())).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
        	}else {
        		
        	}
            scdTable.addCell(sdCell4);
        	
            Cell sdCell5 = new Cell();
            if(polShdl.getOutSum()!=null) {
                sdCell5.add(new Paragraph(Double.toString(polShdl.getOutSum())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
            }else {
            	
            }
            scdTable.addCell(sdCell5);
            
            Cell sdCell6 = new Cell();
            if(polShdl.getLorned()!=null) {
                sdCell6.add(new Paragraph(Double.toString(polShdl.getLorned())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
            }else {
            	
            }
            scdTable.addCell(sdCell6);
            
            scdTable.startNewRow();
        }
         
        document.add(scdTable);
        
        document.add(new Paragraph("Special Notes").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
        
        document.add(new Paragraph(""));
        
        //Creating a Special Notes List
        List list = new List(ListNumberingType.DECIMAL);
        list.setFontSize(10);
        ListItem item1 = new ListItem();
        item1.add(new Paragraph("If HRB / SUHRB is obtained, the total cover will be applicable for the whole family per policy year.").setFontSize(10).setFixedLeading(1));
        list.add(item1);
        
        ListItem item2 = new ListItem();
        item2.add(new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.").setFontSize(10));
        list.add(item2);
        
        ListItem item3 = new ListItem();
        item3.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date issue.").setFontSize(10).setFixedLeading(1));
        list.add(item3);
        
        ListItem item4 = new ListItem();
        item4.add(new Paragraph("All amounts are in Sri Lankan Rupees(LKR).").setFontSize(10));
        list.add(item4);
        
        ListItem item5 = new ListItem();
        item5.add(new Paragraph("Initial policy processing fee of Rs.450 (Payable only with initial deposit).").setFontSize(10).setFixedLeading(1));
        list.add(item5);
        
        ListItem item6 = new ListItem();
        item6.add(new Paragraph("In event of death by accident both Accident Cover and Natural Death Cover will be applicable.").setFontSize(10));
        list.add(item6);
        
        document.add(list);
        
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("This is a system generated report and therefore does not require a signature.").setFontSize(7).setBold());
        
        document.close();		
        return "DTA Success";
	}


	@Override
	public String createATRMReport(QuotationDetails quotationDetails, QuotationView quotationView, QuoCustomer quoCustomer) throws Exception {
		
		QuoCustomer customer = quotationView.getCustDetails();
		ArrayList<QuoBenf> benefitsLife = quotationView.getMainLifeBenf();
		ArrayList<QuoBenf> benefitsSpouse = quotationView.getSpouseBenf();
		ArrayList<QuoChildBenef> benefitsChild = quotationView.getChildBenf();
				
		File file = new File(DEST+"INVP/"+quotationDetails.getQdId().toString()+".pdf");
		
        if(file.exists()) {
        	file.delete();
        }
	    
        FileOutputStream fos = new FileOutputStream(DEST+"INVP/"+quotationDetails.getQdId().toString()+".pdf");

        
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        
        document.setMargins(30,25,30,25);
        
        //logo
        Paragraph pLogo = new Paragraph();
        Image logo = new Image(ImageDataFactory.create(IMG));
        logo.setHeight(100);
        logo.setWidth(120);
        logo.setFixedPosition(460, 720);
        pLogo.add(logo);
        document.add(pLogo);
    
        
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
        agcell2.add(new Paragraph(": " +quotationDetails.getQuotationquotationCreateDate()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell2);
        
        agtTable.startNewRow();
        
        Cell agCell3 = new Cell();
        agCell3.setBorder(Border.NO_BORDER);
        agCell3.add(new Paragraph("Branch Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agCell3);
        Cell agcell4 = new Cell();
        agcell4.setBorder(Border.NO_BORDER);
        agcell4.add(new Paragraph(": " +quotationDetails.getQuotation().getUser().getBranch().getBranch_Name()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell4);
        
        agtTable.startNewRow();
        
        Cell agCell5 = new Cell();
        agCell5.setBorder(Border.NO_BORDER);
        agCell5.add(new Paragraph("Agent Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agCell5);
        Cell agcell6 = new Cell();
        agcell6.setBorder(Border.NO_BORDER);
        agcell6.add(new Paragraph(": " +quotationDetails.getQuotation().getUser().getUser_Name()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell6);
        
        agtTable.startNewRow();
        
        Cell agcell7 = new Cell();
        agcell7.setBorder(Border.NO_BORDER);
        agcell7.add(new Paragraph("Agent Code").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell7);
        Cell agcell8 = new Cell();
        agcell8.setBorder(Border.NO_BORDER);
        agcell8.add(new Paragraph(": " +quotationDetails.getQuotation().getUser().getUser_Code()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell8);
        
        agtTable.startNewRow();
        
        Cell agcell9 = new Cell();
        agcell9.setBorder(Border.NO_BORDER);
        agcell9.add(new Paragraph("Contact No").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell9);
        
        Cell agCell10 = new Cell();
        agCell10.setBorder(Border.NO_BORDER);
        agCell10.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Mobile()!=null ? ": "+quotationDetails.getQuotation().getUser().getUser_Mobile() : " ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agCell10);
        
        document.add(agtTable);
        
        document.add(new Paragraph("ARPICO TERM PLAN").setFontSize(10).setUnderline().setCharacterSpacing(1));
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
        cuCell2.add(new Paragraph(customer.getMainLifeName() != null ? ": " +customer.getMainLifeName() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cuCell2);

        cusTable.startNewRow();
        
        Cell cucell3 = new Cell();
        cucell3.setBorder(Border.NO_BORDER);
        cucell3.add(new Paragraph("Age Next Birthday (Life Assured)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell3);
        Cell cuCell4 = new Cell();
        cuCell4.setBorder(Border.NO_BORDER);
        if(customer.getMainLifeAge()!=null) {
            cuCell4.add(new Paragraph(": " + Integer.toString(customer.getMainLifeAge())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));

        }else {
            cuCell4.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));

        }
        cusTable.addCell(cuCell4);
        
        cusTable.startNewRow();
      
      	Cell cucell5 = new Cell();
        cucell5.setBorder(Border.NO_BORDER);
        cucell5.add(new Paragraph("Occupation (Life Assured)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell5);
        Cell cuCell6 = new Cell();
        cuCell6.setBorder(Border.NO_BORDER);
        cuCell6.add(new Paragraph(customer.getMainLifeOccupation()!=null ? ": "+customer.getMainLifeOccupation() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cuCell6);

        cusTable.startNewRow();
        

        if((customer.getSpouseName())!=null) {
        	
            Cell cucell7 = new Cell();
            cucell7.setBorder(Border.NO_BORDER);
            cucell7.add(new Paragraph("Name Of Spouse").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell7);
            Cell cuCell8 = new Cell();
            cuCell8.setBorder(Border.NO_BORDER);
            cuCell8.add(new Paragraph(": " +customer.getSpouseName()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cuCell8);
        
        }else {
        	
        }
        
        cusTable.startNewRow();
      
        if((customer.getSpouseAge())!=null) {
        	  
            Cell cucell9 = new Cell();
            cucell9.setBorder(Border.NO_BORDER);
            cucell9.add(new Paragraph("Age Next Birthday (Spouse)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell9);
            Cell cuCell10 = new Cell();
            cuCell10.setBorder(Border.NO_BORDER);
            cuCell10.add(new Paragraph(": " + Integer.toString(customer.getSpouseAge())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cuCell10);

        }else {

        }
        
        cusTable.startNewRow();
       
        if((customer.getSpouseOccupation())!=null) {
        	 
            Cell cucell11 = new Cell();
            cucell11.setBorder(Border.NO_BORDER);
            cucell11.add(new Paragraph("Occupation (Spouse)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell11);
            Cell cuCell12 = new Cell();
            cuCell12.setBorder(Border.NO_BORDER);
            cuCell12.add(new Paragraph(": " + customer.getSpouseOccupation()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cuCell12);

        }else {

        }
        
        cusTable.startNewRow();
        
            Cell cucell13 = new Cell();
            cucell13.setBorder(Border.NO_BORDER);
            cucell13.add(new Paragraph("Term").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell13);
            Cell cuCell14 = new Cell();
            cuCell14.setBorder(Border.NO_BORDER);
            
        if((customer.getTerm())!=null) {
            cuCell14.add(new Paragraph(": " + Integer.toString(customer.getTerm())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }else {
            cuCell14.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));

        }
            cusTable.addCell(cuCell14);

        cusTable.startNewRow();
        
        	 Cell cucell15 = new Cell();
             cucell15.setBorder(Border.NO_BORDER);
             cucell15.add(new Paragraph("Mode").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
             cusTable.addCell(cucell15);
             Cell cuCell16 = new Cell();
             cuCell16.setBorder(Border.NO_BORDER);

        if((customer.getMode())!=null) {
             cuCell16.add(new Paragraph(": " +customer.getMode()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }else {
            cuCell16.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }
        cusTable.addCell(cuCell16);

        cusTable.startNewRow();

        	Cell cucell17 = new Cell();
            cucell17.setBorder(Border.NO_BORDER);
            cucell17.add(new Paragraph("Mode Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell17);
            Cell cuCell18 = new Cell();
            cuCell18.setBorder(Border.NO_BORDER);
        
        if((customer.getModePremium())!=null) {
            cuCell18.add(new Paragraph(": " +Double.toString(customer.getModePremium())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }else {
            cuCell18.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }
        cusTable.addCell(cuCell18);

        document.add(cusTable);
        
        document.add(new Paragraph(""));
        
        document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
        document.add(new Paragraph(""));
       
        
        //Create Additional Benefits Table
        float [] pointColumnWidths4 = {450, 150, 150};
        Table benAddTable = new Table(pointColumnWidths4);
        benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

        Cell abCell1 = new Cell();
        abCell1.add(new Paragraph("Additional Benefits").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell1); 
        Cell abCell2 = new Cell();
        abCell2.add(new Paragraph("Amount").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell2); 
        Cell abCell3 = new Cell();
        abCell3.add(new Paragraph("Premium").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell3); 
        
        benAddTable.startNewRow();
        
        Cell abCell4 = new Cell(0,3);
        abCell4.add(new Paragraph("Main Life").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell4);
        
        benAddTable.startNewRow();
     
        for (QuoBenf quoBenf : benefitsLife) {
        	
        	Cell abCell5 = new Cell();

        	if(quoBenf.getBenfName()!=null) {
            	abCell5.add(new Paragraph(quoBenf.getBenfName()).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
        	}else {
            	abCell5.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.LEFT));

        	} 	        
        	benAddTable.addCell(abCell5);

 	        Cell abCell6 = new Cell();
 	        if(quoBenf.getRiderSum()!=null) {
 	 	        abCell6.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
 	        }else {
 	 	        abCell6.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

 	        }
 	        benAddTable.addCell(abCell6);
 	        
 	        Cell abCell7 = new Cell();
 	        if(quoBenf.getPremium()!=null) {
 	 	        abCell7.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
 	        }else {
 	 	        abCell7.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

 	        }
 	        benAddTable.addCell(abCell7);
 	       
 	        benAddTable.startNewRow();
		}
			
        benAddTable.startNewRow();
        
        Cell abCell8 = new Cell(0,3);
        abCell8.add(new Paragraph("Spouse").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell8);
        
        benAddTable.startNewRow();

        //check having a spouse
        if(quotationView.getSpouseBenf()!=null) {
        	 for (QuoBenf quoBenf  : benefitsSpouse) {
             	
             	Cell abCell9 = new Cell();
             	if(quoBenf.getBenfName()!=null) {
                     abCell9.add(new Paragraph(quoBenf.getBenfName()).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
             	}else {
                     abCell9.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
             	}
                  benAddTable.addCell(abCell9);
                  
                  Cell abCell10 = new Cell();
                  if(quoBenf.getRiderSum()!=null) {
                      abCell10.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }else {
                      abCell10.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }
                  benAddTable.addCell(abCell10);
                  
                  Cell abCell11 = new Cell();
                  if(quoBenf.getPremium()!=null) {
                      abCell11.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }else {
                      abCell11.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }
                  benAddTable.addCell(abCell11);
                  benAddTable.startNewRow();
     		}
        }else {
        	
        }
        
        benAddTable.startNewRow();

        //check if no child in arraylist

        if(quotationView.getChildBenf()!=null) {
        	Cell abCell12 = new Cell(0,3);
        	abCell12.add(new Paragraph("Children").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        	benAddTable.addCell(abCell12);
     
        	benAddTable.startNewRow();
        	
        	for (QuoChildBenef quoChild : benefitsChild) {
             	 Cell abCell13 = new Cell(0,3);
                 abCell13.add(new Paragraph("Name : "+quoChild.getChild().getChildName()+ " Relationship : "+quoChild.getChild().getChildRelation()).setFontSize(8).setBold().setTextAlignment(TextAlignment.LEFT).setCharacterSpacing(1));
                 benAddTable.addCell(abCell13);
                 benAddTable.startNewRow();
            
               for (QuoChildBenef quoChildBenef : benefitsChild) {
            	  	 
             	 Cell abCell14 = new Cell();
             	 
             	 if(quoChildBenef.getBenfs().get(0).getBenfName()!=null) {
                     abCell14.add(new Paragraph(quoChildBenef.getBenfs().get(0).getBenfName()).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
             	 }else {
                     abCell14.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
             	 }
                  benAddTable.addCell(abCell14);
                  
                  Cell abCell15 = new Cell();
                  if(quoChildBenef.getBenfs().get(0).getRiderSum()!=null) {
                      abCell15.add(new Paragraph(Double.toString(quoChildBenef.getBenfs().get(0).getRiderSum())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }else {
                      abCell15.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }
                  benAddTable.addCell(abCell15);
                  
                  Cell abCell16 = new Cell();
                  if(quoChild.getBenfs().get(0).getPremium()!=null) {
                      abCell16.add(new Paragraph(Double.toString(quoChild.getBenfs().get(0).getPremium())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }else {
                      abCell16.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

                  }
                  benAddTable.addCell(abCell16);
                  
                  benAddTable.startNewRow();
     			}
        
     		}
        	
        }else {
        	
        }
      
        document.add(benAddTable);
        
        document.add(new Paragraph(""));
        
        document.add(new Paragraph("Special Notes").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
        
        document.add(new Paragraph(""));
        
        //Creating a Special Notes List
        List list = new List(ListNumberingType.DECIMAL);
        list.setFontSize(10);
        ListItem item1 = new ListItem();
        item1.add(new Paragraph("If HRB / SUHRB is obtained, the total cover will be applicable for the whole family per policy year.").setFontSize(10).setFixedLeading(1));
        list.add(item1);
        
        ListItem item2 = new ListItem();
        item2.add(new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.").setFontSize(10));
        list.add(item2);
        
        ListItem item3 = new ListItem();
        item3.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date issue.").setFontSize(10).setFixedLeading(1));
        list.add(item3);
        
        ListItem item4 = new ListItem();
        item4.add(new Paragraph("All amounts are in Sri Lankan Rupees(LKR).").setFontSize(10));
        list.add(item4);
        
        ListItem item5 = new ListItem();
        item5.add(new Paragraph("Initial policy processing fee of Rs. 300 (Payable only with initial deposit).").setFontSize(10).setFixedLeading(1));
        list.add(item5);
        
        ListItem item6 = new ListItem();
        item6.add(new Paragraph("In event of death by accident both Accident Cover and Natural Death Cover will be applicable.").setFontSize(10));
        list.add(item6);
        
        document.add(list);
        
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("This is a system generated report and therefore does not require a signature.").setFontSize(7).setBold());
        
        document.close();		
        
        return "ATRM Success";
	}


	@Override
	public String createEND1Report(QuotationDetails quotationDetails, QuotationView quotationView, QuoCustomer quoCustomer) throws Exception {
		QuoCustomer customer = quotationView.getCustDetails();
		ArrayList<QuoBenf> benefitsLife = quotationView.getMainLifeBenf();
		ArrayList<QuoBenf> benefitsSpouse = quotationView.getSpouseBenf();
		ArrayList<QuoChildBenef> benefitsChild = quotationView.getChildBenf();
				
		File file = new File(DEST+"INVP/"+quotationDetails.getQdId().toString()+".pdf");
		
        if(file.exists()) {
        	file.delete();
        }
	    
        FileOutputStream fos = new FileOutputStream(DEST+"INVP/"+quotationDetails.getQdId().toString()+".pdf");

        
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        
        document.setMargins(30,25,30,25);
        
        //logo
        Paragraph pLogo = new Paragraph();
        Image logo = new Image(ImageDataFactory.create(IMG));
        logo.setHeight(100);
        logo.setWidth(120);
        logo.setFixedPosition(460, 720);
        pLogo.add(logo);
        document.add(pLogo);
    
        
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
        agcell2.add(new Paragraph(": " +quotationDetails.getQuotationquotationCreateDate()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell2);
        
        agtTable.startNewRow();
        
        Cell agCell3 = new Cell();
        agCell3.setBorder(Border.NO_BORDER);
        agCell3.add(new Paragraph("Branch Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agCell3);
        Cell agcell4 = new Cell();
        agcell4.setBorder(Border.NO_BORDER);
        agcell4.add(new Paragraph(": " +quotationDetails.getQuotation().getUser().getBranch().getBranch_Name()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell4);
        
        agtTable.startNewRow();
        
        Cell agCell5 = new Cell();
        agCell5.setBorder(Border.NO_BORDER);
        agCell5.add(new Paragraph("Agent Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agCell5);
        Cell agcell6 = new Cell();
        agcell6.setBorder(Border.NO_BORDER);
        agcell6.add(new Paragraph(": " +quotationDetails.getQuotation().getUser().getUser_Name()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell6);
        
        agtTable.startNewRow();
        
        Cell agcell7 = new Cell();
        agcell7.setBorder(Border.NO_BORDER);
        agcell7.add(new Paragraph("Agent Code").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell7);
        Cell agcell8 = new Cell();
        agcell8.setBorder(Border.NO_BORDER);
        agcell8.add(new Paragraph(": " +quotationDetails.getQuotation().getUser().getUser_Code()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell8);
        
        agtTable.startNewRow();
        
        Cell agcell9 = new Cell();
        agcell9.setBorder(Border.NO_BORDER);
        agcell9.add(new Paragraph("Contact No").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell9);
        
        Cell agCell10 = new Cell();
        agCell10.setBorder(Border.NO_BORDER);
        agCell10.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Mobile()!=null ? ": "+quotationDetails.getQuotation().getUser().getUser_Mobile() : " ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agCell10);
        
        document.add(agtTable);
        
        document.add(new Paragraph("ARPICO ENDOWMENT PLAN").setFontSize(10).setUnderline().setCharacterSpacing(1));
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
        cuCell2.add(new Paragraph(customer.getMainLifeName() != null ? ": " +customer.getMainLifeName() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cuCell2);

        cusTable.startNewRow();
        
        Cell cucell3 = new Cell();
        cucell3.setBorder(Border.NO_BORDER);
        cucell3.add(new Paragraph("Age Next Birthday (Life Assured)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell3);
        Cell cuCell4 = new Cell();
        cuCell4.setBorder(Border.NO_BORDER);
        if(customer.getMainLifeAge()!=null) {
            cuCell4.add(new Paragraph(": " + Integer.toString(customer.getMainLifeAge())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));

        }else {
            cuCell4.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));

        }
        cusTable.addCell(cuCell4);
        
        cusTable.startNewRow();
      
      	Cell cucell5 = new Cell();
        cucell5.setBorder(Border.NO_BORDER);
        cucell5.add(new Paragraph("Occupation (Life Assured)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell5);
        Cell cuCell6 = new Cell();
        cuCell6.setBorder(Border.NO_BORDER);
        cuCell6.add(new Paragraph(customer.getMainLifeOccupation()!=null ? ": "+customer.getMainLifeOccupation() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cuCell6);

        cusTable.startNewRow();
        

        if((customer.getSpouseName())!=null) {
        	
            Cell cucell7 = new Cell();
            cucell7.setBorder(Border.NO_BORDER);
            cucell7.add(new Paragraph("Name Of Spouse").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell7);
            Cell cuCell8 = new Cell();
            cuCell8.setBorder(Border.NO_BORDER);
            cuCell8.add(new Paragraph(": " +customer.getSpouseName()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cuCell8);
        
        }else {
        	
        }
        
        cusTable.startNewRow();
      
        if((customer.getSpouseAge())!=null) {
        	  
            Cell cucell9 = new Cell();
            cucell9.setBorder(Border.NO_BORDER);
            cucell9.add(new Paragraph("Age Next Birthday (Spouse)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell9);
            Cell cuCell10 = new Cell();
            cuCell10.setBorder(Border.NO_BORDER);
            cuCell10.add(new Paragraph(": " + Integer.toString(customer.getSpouseAge())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cuCell10);

        }else {

        }
        
        cusTable.startNewRow();
       
        if((customer.getSpouseOccupation())!=null) {
        	 
            Cell cucell11 = new Cell();
            cucell11.setBorder(Border.NO_BORDER);
            cucell11.add(new Paragraph("Occupation (Spouse)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell11);
            Cell cuCell12 = new Cell();
            cuCell12.setBorder(Border.NO_BORDER);
            cuCell12.add(new Paragraph(": " + customer.getSpouseOccupation()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cuCell12);

        }else {

        }
        
        cusTable.startNewRow();
        
            Cell cucell13 = new Cell();
            cucell13.setBorder(Border.NO_BORDER);
            cucell13.add(new Paragraph("Term").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell13);
            Cell cuCell14 = new Cell();
            cuCell14.setBorder(Border.NO_BORDER);
            
        if((customer.getTerm())!=null) {
            cuCell14.add(new Paragraph(": " + Integer.toString(customer.getTerm())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }else {
            cuCell14.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));

        }
            cusTable.addCell(cuCell14);

        cusTable.startNewRow();
        
        	 Cell cucell15 = new Cell();
             cucell15.setBorder(Border.NO_BORDER);
             cucell15.add(new Paragraph("Mode").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
             cusTable.addCell(cucell15);
             Cell cuCell16 = new Cell();
             cuCell16.setBorder(Border.NO_BORDER);

        if((customer.getMode())!=null) {
             cuCell16.add(new Paragraph(": " +customer.getMode()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }else {
            cuCell16.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }
        cusTable.addCell(cuCell16);

        cusTable.startNewRow();

        	Cell cucell17 = new Cell();
            cucell17.setBorder(Border.NO_BORDER);
            cucell17.add(new Paragraph("Mode Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell17);
            Cell cuCell18 = new Cell();
            cuCell18.setBorder(Border.NO_BORDER);
        
        if((customer.getModePremium())!=null) {
            cuCell18.add(new Paragraph(": " +Double.toString(customer.getModePremium())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }else {
            cuCell18.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }
        cusTable.addCell(cuCell18);

        document.add(cusTable);
        
        document.add(new Paragraph(""));
        
        document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
        document.add(new Paragraph(""));
       
        
        //Create Additional Benefits Table
        float [] pointColumnWidths4 = {450, 150, 150};
        Table benAddTable = new Table(pointColumnWidths4);
        benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

        Cell abCell1 = new Cell();
        abCell1.add(new Paragraph("Additional Benefits").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell1); 
        Cell abCell2 = new Cell();
        abCell2.add(new Paragraph("Amount").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell2); 
        Cell abCell3 = new Cell();
        abCell3.add(new Paragraph("Premium").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell3); 
        
        benAddTable.startNewRow();
        
        Cell abCell4 = new Cell(0,3);
        abCell4.add(new Paragraph("Main Life").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell4);
        
        benAddTable.startNewRow();
     
        for (QuoBenf quoBenf : benefitsLife) {
        	
        	Cell abCell5 = new Cell();

        	if(quoBenf.getBenfName()!=null) {
            	abCell5.add(new Paragraph(quoBenf.getBenfName()).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
        	}else {
            	abCell5.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.LEFT));

        	} 	        
        	benAddTable.addCell(abCell5);

 	        Cell abCell6 = new Cell();
 	        if(quoBenf.getRiderSum()!=null) {
 	 	        abCell6.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
 	        }else {
 	 	        abCell6.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

 	        }
 	        benAddTable.addCell(abCell6);
 	        
 	        Cell abCell7 = new Cell();
 	        if(quoBenf.getPremium()!=null) {
 	 	        abCell7.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
 	        }else {
 	 	        abCell7.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

 	        }
 	        benAddTable.addCell(abCell7);
 	       
 	        benAddTable.startNewRow();
		}
			
        benAddTable.startNewRow();
        
       

        //check having a spouse
   	 for (QuoBenf quoBenf  : benefitsSpouse) {

        if(quoBenf.getBenfName()!=null) {
             	
        	 Cell abCell8 = new Cell(0,3);
             abCell8.add(new Paragraph("Spouse").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
             benAddTable.addCell(abCell8);
             
             benAddTable.startNewRow();
             	Cell abCell9 = new Cell();
             	if(quoBenf.getBenfName()!=null) {
                     abCell9.add(new Paragraph(quoBenf.getBenfName()).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
             	}else {
                     abCell9.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
             	}
                  benAddTable.addCell(abCell9);
                  
                  Cell abCell10 = new Cell();
                  if(quoBenf.getRiderSum()!=null) {
                      abCell10.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }else {
                      abCell10.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }
                  benAddTable.addCell(abCell10);
                  
                  Cell abCell11 = new Cell();
                  if(quoBenf.getPremium()!=null) {
                      abCell11.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }else {
                      abCell11.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }
                  benAddTable.addCell(abCell11);
                  benAddTable.startNewRow();
     		}
        	else {
        	
        	}
           
   	 	}
        benAddTable.startNewRow();

        //check if no child in arraylist
    	for (QuoChildBenef quoChild : benefitsChild) {
    		
    	
        
        if(quoChild.getChild().getChildName()!=null) {
        	Cell abCell12 = new Cell(0,3);
        	abCell12.add(new Paragraph("Children").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        	benAddTable.addCell(abCell12);
     
        	benAddTable.startNewRow();
        	
             	 Cell abCell13 = new Cell(0,3);
                 abCell13.add(new Paragraph("Name : "+quoChild.getChild().getChildName()+ " Relationship : "+quoChild.getChild().getChildRelation()).setFontSize(8).setBold().setTextAlignment(TextAlignment.LEFT).setCharacterSpacing(1));
                 benAddTable.addCell(abCell13);
                 benAddTable.startNewRow();
            
               for (QuoChildBenef quoChildBenef : benefitsChild) {
            	  	 
             	 Cell abCell14 = new Cell();
             	 
             	 if(quoChildBenef.getBenfs().get(0).getBenfName()!=null) {
                     abCell14.add(new Paragraph(quoChildBenef.getBenfs().get(0).getBenfName()).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
             	 }else {
                     abCell14.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
             	 }
                  benAddTable.addCell(abCell14);
                  
                  Cell abCell15 = new Cell();
                  if(quoChildBenef.getBenfs().get(0).getRiderSum()!=null) {
                      abCell15.add(new Paragraph(Double.toString(quoChildBenef.getBenfs().get(0).getRiderSum())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }else {
                      abCell15.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }
                  benAddTable.addCell(abCell15);
                  
                  Cell abCell16 = new Cell();
                  if(quoChild.getBenfs().get(0).getPremium()!=null) {
                      abCell16.add(new Paragraph(Double.toString(quoChild.getBenfs().get(0).getPremium())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }else {
                      abCell16.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

                  }
                  benAddTable.addCell(abCell16);
                  
                  benAddTable.startNewRow();
     			}
            	
        }else {
        	
        }
     		}
       
      
        document.add(benAddTable);
        
        document.add(new Paragraph(""));
        
        document.add(new Paragraph("Special Notes").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
        
        document.add(new Paragraph(""));
        
        //Creating a Special Notes List
        List list = new List(ListNumberingType.DECIMAL);
        list.setFontSize(10);
        ListItem item1 = new ListItem();
        item1.add(new Paragraph("If HRB / SUHRB is obtained, the total cover will be applicable for the whole family per policy year.").setFontSize(10).setFixedLeading(1));
        list.add(item1);
        
        ListItem item2 = new ListItem();
        item2.add(new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.").setFontSize(10));
        list.add(item2);
        
        ListItem item3 = new ListItem();
        item3.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date issue.").setFontSize(10).setFixedLeading(1));
        list.add(item3);
        
        ListItem item4 = new ListItem();
        item4.add(new Paragraph("All amounts are in Sri Lankan Rupees(LKR).").setFontSize(10));
        list.add(item4);
        
        ListItem item5 = new ListItem();
        item5.add(new Paragraph("Initial policy processing fee of Rs. 300 (Payable only with initial deposit).").setFontSize(10).setFixedLeading(1));
        list.add(item5);
        
        ListItem item6 = new ListItem();
        item6.add(new Paragraph("In event of death by accident both Accident Cover and Natural Death Cover will be applicable.").setFontSize(10));
        list.add(item6);
        
        document.add(list);
        
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("This is a system generated report and therefore does not require a signature.").setFontSize(7).setBold());
        
        document.close();		
        
        return "END1 Success";
	}


	@Override
	public String createASFPReport(QuotationDetails quotationDetails, QuotationView quotationView, QuoCustomer quoCustomer) throws Exception {
		QuoCustomer customer = quotationView.getCustDetails();
		ArrayList<QuoBenf> benefitsLife = quotationView.getMainLifeBenf();
		ArrayList<QuoBenf> benefitsSpouse = quotationView.getSpouseBenf();
		ArrayList<QuoChildBenef> benefitsChild = quotationView.getChildBenf();
				
		File file = new File(DEST+"INVP/"+quotationDetails.getQdId().toString()+".pdf");
		
        if(file.exists()) {
        	file.delete();
        }
	    
        FileOutputStream fos = new FileOutputStream(DEST+"INVP/"+quotationDetails.getQdId().toString()+".pdf");

        
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        
        document.setMargins(30,25,30,25);
        
        //logo
        Paragraph pLogo = new Paragraph();
        Image logo = new Image(ImageDataFactory.create(IMG));
        logo.setHeight(100);
        logo.setWidth(120);
        logo.setFixedPosition(460, 720);
        pLogo.add(logo);
        document.add(pLogo);
    
        
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
        agcell2.add(new Paragraph(": " +quotationDetails.getQuotationquotationCreateDate()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell2);
        
        agtTable.startNewRow();
        
        Cell agCell3 = new Cell();
        agCell3.setBorder(Border.NO_BORDER);
        agCell3.add(new Paragraph("Branch Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agCell3);
        Cell agcell4 = new Cell();
        agcell4.setBorder(Border.NO_BORDER);
        agcell4.add(new Paragraph(": " +quotationDetails.getQuotation().getUser().getBranch().getBranch_Name()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell4);
        
        agtTable.startNewRow();
        
        Cell agCell5 = new Cell();
        agCell5.setBorder(Border.NO_BORDER);
        agCell5.add(new Paragraph("Agent Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agCell5);
        Cell agcell6 = new Cell();
        agcell6.setBorder(Border.NO_BORDER);
        agcell6.add(new Paragraph(": " +quotationDetails.getQuotation().getUser().getUser_Name()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell6);
        
        agtTable.startNewRow();
        
        Cell agcell7 = new Cell();
        agcell7.setBorder(Border.NO_BORDER);
        agcell7.add(new Paragraph("Agent Code").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell7);
        Cell agcell8 = new Cell();
        agcell8.setBorder(Border.NO_BORDER);
        agcell8.add(new Paragraph(": " +quotationDetails.getQuotation().getUser().getUser_Code()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell8);
        
        agtTable.startNewRow();
        
        Cell agcell9 = new Cell();
        agcell9.setBorder(Border.NO_BORDER);
        agcell9.add(new Paragraph("Contact No").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell9);
        
        Cell agCell10 = new Cell();
        agCell10.setBorder(Border.NO_BORDER);
        agCell10.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Mobile()!=null ? ": "+quotationDetails.getQuotation().getUser().getUser_Mobile() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agCell10);
        
        document.add(agtTable);
        
        document.add(new Paragraph("ARPICO SCHOOL FEES PLAN").setFontSize(10).setUnderline().setCharacterSpacing(1));
        document.add(new Paragraph("TOTAL PROTECTION OF CHILD SCHOOL FEE OR MONEY BACK GURANTEE").setFontSize(10).setUnderline().setCharacterSpacing(1).setFixedLeading(1));
        
        document.add(new Paragraph("\n"));
        
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
        cuCell2.add(new Paragraph(customer.getMainLifeName() != null ? ": " +customer.getMainLifeName() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cuCell2);

        cusTable.startNewRow();
        
        Cell cucell3 = new Cell();
        cucell3.setBorder(Border.NO_BORDER);
        cucell3.add(new Paragraph("Age Next Birthday (Life Assured)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell3);
        Cell cuCell4 = new Cell();
        cuCell4.setBorder(Border.NO_BORDER);
        if(customer.getMainLifeAge()!=null) {
            cuCell4.add(new Paragraph(": " + Integer.toString(customer.getMainLifeAge())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));

        }else {
            cuCell4.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));

        }
        cusTable.addCell(cuCell4);
        
        cusTable.startNewRow();
      
      	Cell cucell5 = new Cell();
        cucell5.setBorder(Border.NO_BORDER);
        cucell5.add(new Paragraph("Occupation (Life Assured)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell5);
        Cell cuCell6 = new Cell();
        cuCell6.setBorder(Border.NO_BORDER);
        cuCell6.add(new Paragraph(customer.getMainLifeOccupation()!=null ? ": "+customer.getMainLifeOccupation() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cuCell6);

        cusTable.startNewRow();
        

        if((customer.getSpouseName())!=null) {
        	
            Cell cucell7 = new Cell();
            cucell7.setBorder(Border.NO_BORDER);
            cucell7.add(new Paragraph("Name Of Spouse").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell7);
            Cell cuCell8 = new Cell();
            cuCell8.setBorder(Border.NO_BORDER);
            cuCell8.add(new Paragraph(": " +customer.getSpouseName()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cuCell8);
        
        }else {
        	
        }
        
        cusTable.startNewRow();
      
        if((customer.getSpouseAge())!=null) {
        	  
            Cell cucell9 = new Cell();
            cucell9.setBorder(Border.NO_BORDER);
            cucell9.add(new Paragraph("Age Next Birthday (Spouse)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell9);
            Cell cuCell10 = new Cell();
            cuCell10.setBorder(Border.NO_BORDER);
            cuCell10.add(new Paragraph(": " + Integer.toString(customer.getSpouseAge())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cuCell10);

        }else {

        }
        
        cusTable.startNewRow();
       
        if((customer.getSpouseOccupation())!=null) {
        	 
            Cell cucell11 = new Cell();
            cucell11.setBorder(Border.NO_BORDER);
            cucell11.add(new Paragraph("Occupation (Spouse)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell11);
            Cell cuCell12 = new Cell();
            cuCell12.setBorder(Border.NO_BORDER);
            cuCell12.add(new Paragraph(": " + customer.getSpouseOccupation()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cuCell12);

        }else {

        }
        
        cusTable.startNewRow();
        
            Cell cucell13 = new Cell();
            cucell13.setBorder(Border.NO_BORDER);
            cucell13.add(new Paragraph("Term").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell13);
            Cell cuCell14 = new Cell();
            cuCell14.setBorder(Border.NO_BORDER);
            
        if((customer.getTerm())!=null) {
            cuCell14.add(new Paragraph(": " + Integer.toString(customer.getTerm())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }else {
            cuCell14.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));

        }
            cusTable.addCell(cuCell14);

        cusTable.startNewRow();
        
        	 Cell cucell15 = new Cell();
             cucell15.setBorder(Border.NO_BORDER);
             cucell15.add(new Paragraph("Mode").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
             cusTable.addCell(cucell15);
             Cell cuCell16 = new Cell();
             cuCell16.setBorder(Border.NO_BORDER);

        if((customer.getMode())!=null) {
             cuCell16.add(new Paragraph(": " +customer.getMode()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }else {
            cuCell16.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }
        cusTable.addCell(cuCell16);

        cusTable.startNewRow();

        	Cell cucell17 = new Cell();
            cucell17.setBorder(Border.NO_BORDER);
            cucell17.add(new Paragraph("Mode Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell17);
            Cell cuCell18 = new Cell();
            cuCell18.setBorder(Border.NO_BORDER);
        
        if((customer.getModePremium())!=null) {
            cuCell18.add(new Paragraph(": " +Double.toString(customer.getModePremium())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }else {
            cuCell18.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }
        cusTable.addCell(cuCell18);

        document.add(cusTable);
        
        document.add(new Paragraph(""));
        
        document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
        document.add(new Paragraph(""));
       
        
        //Create Additional Benefits Table
        float [] pointColumnWidths4 = {450, 150, 150};
        Table benAddTable = new Table(pointColumnWidths4);
        benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

        Cell abCell1 = new Cell();
        abCell1.add(new Paragraph("Additional Benefits").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell1); 
        Cell abCell2 = new Cell();
        abCell2.add(new Paragraph("Amount").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell2); 
        Cell abCell3 = new Cell();
        abCell3.add(new Paragraph("Premium").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell3); 
        
        benAddTable.startNewRow();
        
        Cell abCell4 = new Cell(0,3);
        abCell4.add(new Paragraph("Main Life").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell4);
        
        benAddTable.startNewRow();
     
        for (QuoBenf quoBenf : benefitsLife) {
        	
        	Cell abCell5 = new Cell();

        	if(quoBenf.getBenfName()!=null) {
            	abCell5.add(new Paragraph(quoBenf.getBenfName()).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
        	}else {
            	abCell5.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.LEFT));

        	} 	        
        	benAddTable.addCell(abCell5);

 	        Cell abCell6 = new Cell();
 	        if(quoBenf.getRiderSum()!=null) {
 	 	        abCell6.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
 	        }else {
 	 	        abCell6.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

 	        }
 	        benAddTable.addCell(abCell6);
 	        
 	        Cell abCell7 = new Cell();
 	        if(quoBenf.getPremium()!=null) {
 	 	        abCell7.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
 	        }else {
 	 	        abCell7.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

 	        }
 	        benAddTable.addCell(abCell7);
 	       
 	        benAddTable.startNewRow();
		}
			
        benAddTable.startNewRow();
        
        Cell abCell8 = new Cell(0,3);
        abCell8.add(new Paragraph("Spouse").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell8);
        
        benAddTable.startNewRow();

        //check having a spouse
        if(quotationView.getSpouseBenf()!=null) {
        	 for (QuoBenf quoBenf  : benefitsSpouse) {
             	
             	Cell abCell9 = new Cell();
             	if(quoBenf.getBenfName()!=null) {
                     abCell9.add(new Paragraph(quoBenf.getBenfName()).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
             	}else {
                     abCell9.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
             	}
                  benAddTable.addCell(abCell9);
                  
                  Cell abCell10 = new Cell();
                  if(quoBenf.getRiderSum()!=null) {
                      abCell10.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }else {
                      abCell10.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }
                  benAddTable.addCell(abCell10);
                  
                  Cell abCell11 = new Cell();
                  if(quoBenf.getPremium()!=null) {
                      abCell11.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }else {
                      abCell11.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }
                  benAddTable.addCell(abCell11);
                  benAddTable.startNewRow();
     		}
        }else {
        	
        }
        
        benAddTable.startNewRow();

        //check if no child in arraylist

        if(quotationView.getChildBenf()!=null) {
        	Cell abCell12 = new Cell(0,3);
        	abCell12.add(new Paragraph("Children").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        	benAddTable.addCell(abCell12);
     
        	benAddTable.startNewRow();
        	
        	for (QuoChildBenef quoChild : benefitsChild) {
             	 Cell abCell13 = new Cell(0,3);
                 abCell13.add(new Paragraph("Name : "+quoChild.getChild().getChildName()+ " Relationship : "+quoChild.getChild().getChildRelation()).setFontSize(8).setBold().setTextAlignment(TextAlignment.LEFT).setCharacterSpacing(1));
                 benAddTable.addCell(abCell13);
                 benAddTable.startNewRow();
            
               for (QuoChildBenef quoChildBenef : benefitsChild) {
            	  	 
             	 Cell abCell14 = new Cell();
             	 
             	 if(quoChildBenef.getBenfs().get(0).getBenfName()!=null) {
                     abCell14.add(new Paragraph(quoChildBenef.getBenfs().get(0).getBenfName()).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
             	 }else {
                     abCell14.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
             	 }
                  benAddTable.addCell(abCell14);
                  
                  Cell abCell15 = new Cell();
                  if(quoChildBenef.getBenfs().get(0).getRiderSum()!=null) {
                      abCell15.add(new Paragraph(Double.toString(quoChildBenef.getBenfs().get(0).getRiderSum())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }else {
                      abCell15.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }
                  benAddTable.addCell(abCell15);
                  
                  Cell abCell16 = new Cell();
                  if(quoChild.getBenfs().get(0).getPremium()!=null) {
                      abCell16.add(new Paragraph(Double.toString(quoChild.getBenfs().get(0).getPremium())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }else {
                      abCell16.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

                  }
                  benAddTable.addCell(abCell16);
                  
                  benAddTable.startNewRow();
     			}
        
     		}
        	
        }else {
        	
        }
      
        document.add(benAddTable);
        
        document.add(new Paragraph(""));
        document.add(new Paragraph("If no claim arises during the policy term on the primary benefit Guranteed maturity value : 0.00").setFontSize(10));
        document.add(new Paragraph(" "));

        
        document.add(new Paragraph("Special Notes").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
        
        document.add(new Paragraph(""));
        
        //Creating a Special Notes List
        List list = new List(ListNumberingType.DECIMAL);
        list.setFontSize(10);
        ListItem item1 = new ListItem();
        item1.add(new Paragraph("If HRB / SUHRB is obtained, the total cover will be applicable for the whole family per policy year.").setFontSize(10).setFixedLeading(1));
        list.add(item1);
        
        ListItem item2 = new ListItem();
        item2.add(new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.").setFontSize(10));
        list.add(item2);
        
        ListItem item3 = new ListItem();
        item3.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date of issue.").setFontSize(10).setFixedLeading(1));
        list.add(item3);
        
        ListItem item4 = new ListItem();
        item4.add(new Paragraph("All amounts are in Sri Lankan Rupees (LKR).").setFontSize(10));
        list.add(item4);
        
        ListItem item5 = new ListItem();
        item5.add(new Paragraph("This is only a Quotation and not an Acceptance of Risk.").setFontSize(10).setFixedLeading(1));
        list.add(item5);
        
        ListItem item6 = new ListItem();
        item6.add(new Paragraph("In the case of Death of the child and if no claim has been made on the primary benefit during the policy term, Total premium paid up to date on the Primary Benefit (MSFB) will be refundered.").setFontSize(10).setFixedLeading(10));
        list.add(item6);
        
        ListItem item7 = new ListItem();
        //item7.setFixedPosition(20, 50, 450);
        item7.add(new Paragraph("If no claim has been made on the primary benefit during the policy term, total premium paid on the Primary Benefit (MSFB) premium will be refundered at the policy expiry date.").setFontSize(10).setFixedLeading(10));
        list.add(item7);
        
        document.add(list);
        
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("This is a system generated report and therefore does not require a signature.").setFontSize(7).setBold());
        
        document.close();		
        
        return "ASFP Success";
	}

	@Override
	public String createARPReport(QuotationDetails quotationDetails, QuotationView quotationView, QuoCustomer quoCustomer) throws Exception {
		QuoCustomer customer = quotationView.getCustDetails();
		ArrayList<QuoBenf> benefitsLife = quotationView.getMainLifeBenf();
		ArrayList<QuoBenf> benefitsSpouse = quotationView.getSpouseBenf();
		ArrayList<QuoChildBenef> benefitsChild = quotationView.getChildBenf();
				
		File file = new File(DEST+"INVP/"+quotationDetails.getQdId().toString()+".pdf");
		
        if(file.exists()) {
        	file.delete();
        }
	    
        FileOutputStream fos = new FileOutputStream(DEST+"INVP/"+quotationDetails.getQdId().toString()+".pdf");

        
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        
        document.setMargins(30,25,30,25);
        
        //logo
        Paragraph pLogo = new Paragraph();
        Image logo = new Image(ImageDataFactory.create(IMG));
        logo.setHeight(100);
        logo.setWidth(120);
        logo.setFixedPosition(460, 720);
        pLogo.add(logo);
        document.add(pLogo);
    
        
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
        agcell2.add(new Paragraph(": " +quotationDetails.getQuotationquotationCreateDate()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell2);
        
        agtTable.startNewRow();
        
        Cell agCell3 = new Cell();
        agCell3.setBorder(Border.NO_BORDER);
        agCell3.add(new Paragraph("Branch Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agCell3);
        Cell agcell4 = new Cell();
        agcell4.setBorder(Border.NO_BORDER);
        agcell4.add(new Paragraph(": " +quotationDetails.getQuotation().getUser().getBranch().getBranch_Name()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell4);
        
        agtTable.startNewRow();
        
        Cell agCell5 = new Cell();
        agCell5.setBorder(Border.NO_BORDER);
        agCell5.add(new Paragraph("Agent Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agCell5);
        Cell agcell6 = new Cell();
        agcell6.setBorder(Border.NO_BORDER);
        agcell6.add(new Paragraph(": " +quotationDetails.getQuotation().getUser().getUser_Name()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell6);
        
        agtTable.startNewRow();
        
        Cell agcell7 = new Cell();
        agcell7.setBorder(Border.NO_BORDER);
        agcell7.add(new Paragraph("Agent Code").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell7);
        Cell agcell8 = new Cell();
        agcell8.setBorder(Border.NO_BORDER);
        agcell8.add(new Paragraph(": " +quotationDetails.getQuotation().getUser().getUser_Code()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell8);
        
        agtTable.startNewRow();
        
        Cell agcell9 = new Cell();
        agcell9.setBorder(Border.NO_BORDER);
        agcell9.add(new Paragraph("Contact No").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agcell9);
        
        Cell agCell10 = new Cell();
        agCell10.setBorder(Border.NO_BORDER);
        agCell10.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Mobile()!=null ? ": "+quotationDetails.getQuotation().getUser().getUser_Mobile() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        agtTable.addCell(agCell10);
        
        document.add(agtTable);
        
        document.add(new Paragraph("ARPICO RELIEF PLAN").setFontSize(10).setUnderline().setCharacterSpacing(1));
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
        cuCell2.add(new Paragraph(customer.getMainLifeName() != null ? ": " +customer.getMainLifeName() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cuCell2);

        cusTable.startNewRow();
        
        Cell cucell3 = new Cell();
        cucell3.setBorder(Border.NO_BORDER);
        cucell3.add(new Paragraph("Age Next Birthday (Life Assured)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell3);
        Cell cuCell4 = new Cell();
        cuCell4.setBorder(Border.NO_BORDER);
        if(customer.getMainLifeAge()!=null) {
            cuCell4.add(new Paragraph(": " + Integer.toString(customer.getMainLifeAge())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));

        }else {
            cuCell4.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));

        }
        cusTable.addCell(cuCell4);
        
        cusTable.startNewRow();
      
      	Cell cucell5 = new Cell();
        cucell5.setBorder(Border.NO_BORDER);
        cucell5.add(new Paragraph("Occupation (Life Assured)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell5);
        Cell cuCell6 = new Cell();
        cuCell6.setBorder(Border.NO_BORDER);
        cuCell6.add(new Paragraph(customer.getMainLifeOccupation()!=null ? ": "+customer.getMainLifeOccupation() : ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cuCell6);

        cusTable.startNewRow();
        

        if((customer.getSpouseName())!=null) {
        	
            Cell cucell7 = new Cell();
            cucell7.setBorder(Border.NO_BORDER);
            cucell7.add(new Paragraph("Name Of Spouse").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell7);
            Cell cuCell8 = new Cell();
            cuCell8.setBorder(Border.NO_BORDER);
            cuCell8.add(new Paragraph(": " +customer.getSpouseName()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cuCell8);
        
        }else {
        	
        }
        
        cusTable.startNewRow();
      
        if((customer.getSpouseAge())!=null) {
        	  
            Cell cucell9 = new Cell();
            cucell9.setBorder(Border.NO_BORDER);
            cucell9.add(new Paragraph("Age Next Birthday (Spouse)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell9);
            Cell cuCell10 = new Cell();
            cuCell10.setBorder(Border.NO_BORDER);
            cuCell10.add(new Paragraph(": " + Integer.toString(customer.getSpouseAge())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cuCell10);

        }else {

        }
        
        cusTable.startNewRow();
       
        if((customer.getSpouseOccupation())!=null) {
        	 
            Cell cucell11 = new Cell();
            cucell11.setBorder(Border.NO_BORDER);
            cucell11.add(new Paragraph("Occupation (Spouse)").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell11);
            Cell cuCell12 = new Cell();
            cuCell12.setBorder(Border.NO_BORDER);
            cuCell12.add(new Paragraph(": " + customer.getSpouseOccupation()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cuCell12);

        }else {

        }
        
        cusTable.startNewRow();
        
            Cell cucell13 = new Cell();
            cucell13.setBorder(Border.NO_BORDER);
            cucell13.add(new Paragraph("Term").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
            cusTable.addCell(cucell13);
            Cell cuCell14 = new Cell();
            cuCell14.setBorder(Border.NO_BORDER);
            
        if((customer.getTerm())!=null) {
            cuCell14.add(new Paragraph(": " + Integer.toString(customer.getTerm())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }else {
            cuCell14.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));

        }
            cusTable.addCell(cuCell14);

        cusTable.startNewRow();
        
        Cell cucell15 = new Cell();
        cucell15.setBorder(Border.NO_BORDER);
        cucell15.add(new Paragraph("Paying Term").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell15);
        Cell cuCell16 = new Cell();
        cuCell16.setBorder(Border.NO_BORDER);
        if(quotationDetails.getPayTerm()!=null) {
        	cuCell16.add(new Paragraph(": " + Integer.toString(quotationDetails.getPayTerm())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }else {
        	cuCell16.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));

        }
        cusTable.addCell(cuCell16);

        cusTable.startNewRow();
        	
        Cell cucell17 = new Cell();
        cucell17.setBorder(Border.NO_BORDER);
        cucell17.add(new Paragraph("Mode").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell17);
        Cell cuCell18 = new Cell();
        cuCell18.setBorder(Border.NO_BORDER);

        if((customer.getMode())!=null) {
        	cuCell18.add(new Paragraph(": " +customer.getMode()).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }else {
        	cuCell18.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }
        cusTable.addCell(cuCell18);

        cusTable.startNewRow();

        Cell cucell19 = new Cell();
        cucell19.setBorder(Border.NO_BORDER);
        cucell19.add(new Paragraph("Mode Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        cusTable.addCell(cucell19);
        Cell cuCell20 = new Cell();
        cuCell20.setBorder(Border.NO_BORDER);
        
        if((customer.getModePremium())!=null) {
        	cuCell20.add(new Paragraph(": " +Double.toString(customer.getModePremium())).setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }else {
        	cuCell20.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT));
        }
        cusTable.addCell(cuCell20);

        document.add(cusTable);
        
        document.add(new Paragraph(""));
        
        document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
        document.add(new Paragraph(""));
       
        
        //Create Additional Benefits Table
        float [] pointColumnWidths4 = {450, 150, 150};
        Table benAddTable = new Table(pointColumnWidths4);
        benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

        Cell abCell1 = new Cell();
        abCell1.add(new Paragraph("Additional Benefits").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell1); 
        Cell abCell2 = new Cell();
        abCell2.add(new Paragraph("Amount").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell2); 
        Cell abCell3 = new Cell();
        abCell3.add(new Paragraph("Premium").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell3); 
        
        benAddTable.startNewRow();
        
        Cell abCell4 = new Cell(0,3);
        abCell4.add(new Paragraph("Main Life").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell4);
        
        benAddTable.startNewRow();
     
        for (QuoBenf quoBenf : benefitsLife) {
        	
        	Cell abCell5 = new Cell();

        	if(quoBenf.getBenfName()!=null) {
            	abCell5.add(new Paragraph(quoBenf.getBenfName()).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
        	}else {
            	abCell5.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.LEFT));

        	} 	        
        	benAddTable.addCell(abCell5);

 	        Cell abCell6 = new Cell();
 	        if(quoBenf.getRiderSum()!=null) {
 	 	        abCell6.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
 	        }else {
 	 	        abCell6.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

 	        }
 	        benAddTable.addCell(abCell6);
 	        
 	        Cell abCell7 = new Cell();
 	        if(quoBenf.getPremium()!=null) {
 	 	        abCell7.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
 	        }else {
 	 	        abCell7.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

 	        }
 	        benAddTable.addCell(abCell7);
 	       
 	        benAddTable.startNewRow();
		}
			
        benAddTable.startNewRow();
        
        Cell abCell8 = new Cell(0,3);
        abCell8.add(new Paragraph("Spouse").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        benAddTable.addCell(abCell8);
        
        benAddTable.startNewRow();

        //check having a spouse
        if(quotationView.getSpouseBenf()!=null) {
        	 for (QuoBenf quoBenf  : benefitsSpouse) {
             	
             	Cell abCell9 = new Cell();
             	if(quoBenf.getBenfName()!=null) {
                     abCell9.add(new Paragraph(quoBenf.getBenfName()).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
             	}else {
                     abCell9.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
             	}
                  benAddTable.addCell(abCell9);
                  
                  Cell abCell10 = new Cell();
                  if(quoBenf.getRiderSum()!=null) {
                      abCell10.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }else {
                      abCell10.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }
                  benAddTable.addCell(abCell10);
                  
                  Cell abCell11 = new Cell();
                  if(quoBenf.getPremium()!=null) {
                      abCell11.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }else {
                      abCell11.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }
                  benAddTable.addCell(abCell11);
                  benAddTable.startNewRow();
     		}
        }else {
        	
        }
        
        benAddTable.startNewRow();

        //check if no child in arraylist
        if(quotationView.getChildBenf()!=null) {
        	Cell abCell12 = new Cell(0,3);
        	abCell12.add(new Paragraph("Children").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
        	benAddTable.addCell(abCell12);
     
        	benAddTable.startNewRow();
        	
        	for (QuoChildBenef quoChild : benefitsChild) {
             	 Cell abCell13 = new Cell(0,3);
                 abCell13.add(new Paragraph("Name : "+quoChild.getChild().getChildName()+ " Relationship : "+quoChild.getChild().getChildRelation()).setFontSize(8).setBold().setTextAlignment(TextAlignment.LEFT).setCharacterSpacing(1));
                 benAddTable.addCell(abCell13);
                 benAddTable.startNewRow();
            
               for (QuoChildBenef quoChildBenef : benefitsChild) {
            	  	 
             	 Cell abCell14 = new Cell();
             	 
             	 if(quoChildBenef.getBenfs().get(0).getBenfName()!=null) {
                     abCell14.add(new Paragraph(quoChildBenef.getBenfs().get(0).getBenfName()).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
             	 }else {
                     abCell14.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
             	 }
                  benAddTable.addCell(abCell14);
                  
                  Cell abCell15 = new Cell();
                  if(quoChildBenef.getBenfs().get(0).getRiderSum()!=null) {
                      abCell15.add(new Paragraph(Double.toString(quoChildBenef.getBenfs().get(0).getRiderSum())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }else {
                      abCell15.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }
                  benAddTable.addCell(abCell15);
                  
                  Cell abCell16 = new Cell();
                  if(quoChild.getBenfs().get(0).getPremium()!=null) {
                      abCell16.add(new Paragraph(Double.toString(quoChild.getBenfs().get(0).getPremium())).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
                  }else {
                      abCell16.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

                  }
                  benAddTable.addCell(abCell16);
                  
                  benAddTable.startNewRow();
     			}
        
     		}
        	
        }else {
        	
        }
      
        document.add(benAddTable);
        
        document.add(new Paragraph("Sum assured increase every year by 2.5%").setFontSize(10));
        
        //Policy Summary Details
        float [] pointColumnWidths5 = {40, 100, 100, 100, 100}; 
        Table polSmyTable = new Table(pointColumnWidths5);
        polSmyTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
        
        Cell psCell1 = new Cell(2, 0);
        psCell1.add(new Paragraph("Year").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
    	polSmyTable.addCell(psCell1);
    	Cell psCell2 = new Cell(2, 0);
    	psCell2.add(new Paragraph("Premium per year for Basic Policy").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
    	polSmyTable.addCell(psCell2);
    	Cell psCell3 = new Cell(2, 0);
    	psCell3.add(new Paragraph("Total Premium Paid per year").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
    	polSmyTable.addCell(psCell3);
    	Cell psCell4 = new Cell(0, 2);
    	psCell4.add(new Paragraph("Minimum Life Benefits").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
    	polSmyTable.addCell(psCell4);
    	
    	polSmyTable.startNewRow();
    	
    	Cell psCell5 = new Cell();
    	psCell5.add(new Paragraph("Protection against Natural Death").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
    	polSmyTable.addCell(psCell5);
    	Cell psCell6 = new Cell();
    	psCell6.add(new Paragraph("Protection against Accident Death").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
    	polSmyTable.addCell(psCell6);
    	
    	polSmyTable.startNewRow();
    	
    	Cell psCell7 = new Cell();
    	psCell7.add(new Paragraph("1").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
    	polSmyTable.addCell(psCell7);
    	Cell psCell8 = new Cell();
    	psCell8.add(new Paragraph("47,808.00").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
    	polSmyTable.addCell(psCell8);
    	Cell psCell9 = new Cell();
    	psCell9.add(new Paragraph("96,852.00").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
    	polSmyTable.addCell(psCell9);
    	Cell psCell10 = new Cell();
    	psCell10.add(new Paragraph("506,250.00").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
    	polSmyTable.addCell(psCell10);
    	Cell psCell11 = new Cell();
    	psCell11.add(new Paragraph("1,506,250.00").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
    	polSmyTable.addCell(psCell11);
    	
        document.add(polSmyTable);
        
        document.add(new Paragraph(""));
        document.add(new Paragraph("Guranteed Maturity : 343,750.00").setFontSize(10));
        document.add(new Paragraph(" "));

        
        document.add(new Paragraph("Special Notes").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
        
        document.add(new Paragraph(""));
        
        //Creating a Special Notes List
        List list = new List(ListNumberingType.DECIMAL);
        list.setFontSize(10);
        ListItem item1 = new ListItem();
        item1.add(new Paragraph("If HRB / SUHRB is obtained, the total cover will be applicable for the whole family per policy year.").setFontSize(10).setFixedLeading(1));
        list.add(item1);
        
        ListItem item2 = new ListItem();
        item2.add(new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.").setFontSize(10));
        list.add(item2);
        
        ListItem item3 = new ListItem();
        item3.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date of issue.").setFontSize(10).setFixedLeading(1));
        list.add(item3);
        
        ListItem item4 = new ListItem();
        item4.add(new Paragraph("All amounts are in Sri Lankan Rupees (LKR).").setFontSize(10));
        list.add(item4);
        
        document.add(list);
        
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("This is a system generated report and therefore does not require a signature.").setFontSize(7).setBold());
        
        document.close();		
        
        return "ARP Success";
	}

}
