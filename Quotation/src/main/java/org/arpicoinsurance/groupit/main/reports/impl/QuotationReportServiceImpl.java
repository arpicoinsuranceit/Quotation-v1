package org.arpicoinsurance.groupit.main.reports.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

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
import com.itextpdf.kernel.geom.PageSize;
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
	public static final String FONT = "./src/main/resources/Reports/FONTDIR/Times_New_Romance.ttf";

	@Autowired
	private SheduleDao sheduleDao;

	// Creating Arpico Insurance Plus Report
	@Override
	public byte[] createAIPReport(QuotationDetails quotationDetails, QuotationView quotationView,
			QuoCustomer quoCustomer) throws Exception {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(quotationDetails.getQuotationquotationCreateDate());

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date quoCreateDate = quotationDetails.getQuotationquotationCreateDate();
		String date = dateFormat.format(quoCreateDate);

		ArrayList<QuoBenf> benefitsLife = quotationView.getMainLifeBenf();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);
		document.setTopMargin(5);

		// Arpico Logo
		Paragraph pLogo = new Paragraph();
		Image logo = new Image(ImageDataFactory.create(IMG));
		logo.setHeight(100);
		logo.setWidth(120);
		logo.setFixedPosition(460, 720);
		pLogo.add(logo);
		document.add(pLogo);

		/*
		 * Setting new Font PdfFont font = PdfFontFactory.createFont(FONT,
		 * PdfEncodings.IDENTITY_H); Paragraph p = new
		 * Paragraph("Arpico Insurance PLC Quotation").setFont(font); document.add(p);
		 */

		document.add(new Paragraph("Arpico Insurance PLC Quotation").setBold().setFontSize(14)
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));

		document.add(new Paragraph(""));

		// Agent Details
		float[] pointColumnWidths1 = { 70, 200 };
		Table agtTable = new Table(pointColumnWidths1);
		agtTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell agCell1 = new Cell();
		agCell1.setBorder(Border.NO_BORDER);
		agCell1.add(new Paragraph("Date").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell1);
		Cell agcell2 = new Cell();
		agcell2.setBorder(Border.NO_BORDER);
		agcell2.add(
				new Paragraph(": " + date).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell2);

		agtTable.startNewRow();

		Cell agCell3 = new Cell();
		agCell3.setBorder(Border.NO_BORDER);
		agCell3.add(
				new Paragraph("Branch Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell3);
		Cell agcell4 = new Cell();
		agcell4.setBorder(Border.NO_BORDER);
		agcell4.add(new Paragraph(quotationDetails.getQuotation().getUser().getBranch().getBranch_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getBranch().getBranch_Name()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell4);

		agtTable.startNewRow();

		Cell agCell5 = new Cell();
		agCell5.setBorder(Border.NO_BORDER);
		agCell5.add(
				new Paragraph("Agent Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell5);
		Cell agcell6 = new Cell();
		agcell6.setBorder(Border.NO_BORDER);
		agcell6.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Name()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell6);

		agtTable.startNewRow();

		Cell agcell7 = new Cell();
		agcell7.setBorder(Border.NO_BORDER);
		agcell7.add(
				new Paragraph("Agent Code").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell7);
		Cell agcell8 = new Cell();
		agcell8.setBorder(Border.NO_BORDER);
		agcell8.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Code() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Code()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell8);

		agtTable.startNewRow();

		Cell agcell9 = new Cell();
		agcell9.setBorder(Border.NO_BORDER);
		agcell9.add(
				new Paragraph("Contact No").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell9);

		Cell agCell10 = new Cell();
		agCell10.setBorder(Border.NO_BORDER);
		agCell10.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Mobile() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Mobile()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell10);

		document.add(agtTable);

		document.add(new Paragraph("ARPICO INVESTMENT PLUS").setFontSize(10).setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		// customer Details Table
		float[] pointColumnWidths2 = { 100, 200 };
		Table cusTable = new Table(pointColumnWidths2);
		cusTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell cucell1 = new Cell();
		cucell1.setBorder(Border.NO_BORDER);
		cucell1.add(new Paragraph("Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cucell1);
		Cell cuCell2 = new Cell();
		cuCell2.setBorder(Border.NO_BORDER);
		cuCell2.add(new Paragraph(quotationDetails.getCustomerDetails().getCustName() != null
				? ": " + quotationDetails.getCustomerDetails().getCustName()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCell2);

		cusTable.startNewRow();

		Cell cucell3 = new Cell();
		cucell3.setBorder(Border.NO_BORDER);
		cucell3.add(new Paragraph("Age Next Birthday").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		cusTable.addCell(cucell3);
		Cell cuCell4 = new Cell();
		cuCell4.setBorder(Border.NO_BORDER);
		if (quoCustomer.getMainLifeAge() != null) {
			cuCell4.add(new Paragraph(": " + quoCustomer.getMainLifeAge()).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCell4.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCell4);

		cusTable.startNewRow();

		Cell cucell5 = new Cell();
		cucell5.setBorder(Border.NO_BORDER);
		cucell5.add(
				new Paragraph("Occupation").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cucell5);
		Cell cuCell6 = new Cell();
		cuCell6.setBorder(Border.NO_BORDER);
		cuCell6.add(new Paragraph(quotationDetails.getCustomerDetails().getOccupation().getOcupationName() != null
				? ": " + quotationDetails.getCustomerDetails().getOccupation().getOcupationName()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCell6);
		cusTable.startNewRow();

		document.add(cusTable);

		// creating Quotation Details Tables
		float[] pointColumnWidths5 = { 80, 150 };
		Table DtlTable = new Table(pointColumnWidths5);
		DtlTable.setFixedPosition(350, 620, 230);

		Cell dlcell1 = new Cell();
		dlcell1.setBorder(Border.NO_BORDER);
		dlcell1.add(new Paragraph("Term").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell1);
		Cell dlcell2 = new Cell();
		dlcell2.setBorder(Border.NO_BORDER);
		if (quotationDetails.getPolTerm() != null) {
			dlcell2.add(new Paragraph(": " + Integer.toString(quotationDetails.getPolTerm())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell2.add(new Paragraph(": ").setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		DtlTable.addCell(dlcell2);

		DtlTable.startNewRow();

		Cell dlcell3 = new Cell();
		dlcell3.setBorder(Border.NO_BORDER);
		dlcell3.add(new Paragraph("Mode").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell3);
		Cell dlcell4 = new Cell();
		dlcell4.setBorder(Border.NO_BORDER);

		if (quotationDetails.getPayMode() != null) {
			if (quotationDetails.getPayMode().equalsIgnoreCase("M")) {
				dlcell4.add(new Paragraph(": Monthly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("Y")) {
				dlcell4.add(new Paragraph(": Yearly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("Q")) {
				dlcell4.add(new Paragraph(": Quarterly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("H")) {
				dlcell4.add(new Paragraph(": Half Yearly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("S")) {
				dlcell4.add(new Paragraph(": Single Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		}
		DtlTable.addCell(dlcell4);

		DtlTable.startNewRow();

		Cell dlcell5 = new Cell();
		dlcell5.setBorder(Border.NO_BORDER);
		dlcell5.add(
				new Paragraph("Mode Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell5);
		Cell dlcell6 = new Cell();
		dlcell6.setBorder(Border.NO_BORDER);
		if (quoCustomer.getModePremium() != null) {
			dlcell6.add(new Paragraph(": " + Double.toString(quoCustomer.getModePremium())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell6.add(new Paragraph("").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell6);

		DtlTable.startNewRow();

		Cell dlcell7 = new Cell();
		dlcell7.setBorder(Border.NO_BORDER);
		dlcell7.add(
				new Paragraph("Policy Fees").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell7);
		Cell dlcell8 = new Cell();
		dlcell8.setBorder(Border.NO_BORDER);
		if (quotationDetails.getPolicyFee() != null) {
			dlcell8.add(new Paragraph(": " + Double.toString(quotationDetails.getPolicyFee())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		} else {
			dlcell8.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell8);

		DtlTable.startNewRow();

		Cell dlcell9 = new Cell();
		dlcell9.setBorder(Border.NO_BORDER);
		dlcell9.add(new Paragraph("Total Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		DtlTable.addCell(dlcell9);
		Cell dlcell10 = new Cell();
		dlcell10.setBorder(Border.NO_BORDER);
		if (quoCustomer.getTotPremium() != null) {
			dlcell10.add(new Paragraph(": " + Double.toString(quoCustomer.getTotPremium())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell10.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell10);

		document.add(DtlTable);

		document.add(new Paragraph("\n"));

		document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		// Creating Benefits Table
		float[] pointColumnWidths3 = { 500, 200 };
		Table benLivTable = new Table(pointColumnWidths3);
		benLivTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell cell1 = new Cell();
		cell1.setHeight(12);
		cell1.add(new Paragraph("Living Benefits").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benLivTable.addCell(cell1);
		Cell cell2 = new Cell();
		cell1.setHeight(12);
		cell2.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benLivTable.addCell(cell2);

		benLivTable.startNewRow();

		if (quotationView.getMainLifeBenf() != null) {

			for (QuoBenf quoBenf : benefitsLife) {

				Cell cell3 = new Cell();
				cell3.add(new Paragraph(quoBenf.getBenfName() != null ? quoBenf.getBenfName() : "").setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT));
				benLivTable.addCell(cell3);
				Cell cell4 = new Cell();
				cell4.setHeight(12);
				if (quoBenf.getRiderSum() != null) {
					cell4.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));

				} else {
					cell4.add(new Paragraph("").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

				}
				benLivTable.addCell(cell4);

				benLivTable.startNewRow();
			}
		} else {

		}

		document.add(benLivTable);

		document.add(new Paragraph("Guranteed minimum dividend rate declared for " + calendar.get(Calendar.YEAR))
				.setFontSize(10));
		document.add(new Paragraph(""));

		// Create Additional Benefits Table

		float[] pointColumnWidths4 = { 300, 500 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell abCell1 = new Cell(0, 2);
		abCell1.add(new Paragraph("Additional Benefits").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell1);

		benAddTable.startNewRow();

		Cell abCell2 = new Cell();
		abCell2.add(new Paragraph("Accidental Death").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCell2);
		Cell abCell3 = new Cell();
		abCell3.add(new Paragraph("Twice the value of premium paid or account balance which ever is higher")
				.setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCell3);

		benAddTable.startNewRow();

		Cell abCell4 = new Cell();
		abCell4.add(new Paragraph("Natural Death").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCell4);
		Cell abCell5 = new Cell();
		abCell5.add(new Paragraph("Account balance or 125% of the premium paid, which ever is higher").setFontSize(9)
				.setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCell5);

		document.add(benAddTable);

		document.add(new Paragraph(""));

		document.add(new Paragraph("Special Notes").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		// Creating a Special Notes List
		List list = new List(ListNumberingType.DECIMAL);
		list.setFontSize(10);
		ListItem item1 = new ListItem();
		item1.add(new Paragraph("This is an indicative quote only and is valid for 30 days from date of issue.")
				.setFontSize(10).setFixedLeading(2));
		list.add(item1);

		ListItem item2 = new ListItem();
		item2.add(new Paragraph("All Amounts are in Sri Lankan Rupees (LKR).").setFontSize(10).setFixedLeading(2));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("Initial policy processing fee of Rs 300 (Payable only with initial deposit).")
				.setFontSize(10).setFixedLeading(2));
		list.add(item3);

		document.add(list);

		document.add(new Paragraph("\n"));
		document.add(new Paragraph("This is a system generated report and therefore does not require a signature.")
				.setFontSize(7).setBold());

		document.close();

		return baos.toByteArray();
	}

	// creating Arpico Investment Bond Report
	@Override
	public byte[] createAIBReport(QuotationDetails quotationDetails, QuotationView quotationView,
			QuoCustomer quoCustomer) throws Exception {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date quoCreateDate = quotationDetails.getQuotationquotationCreateDate();
		String date = dateFormat.format(quoCreateDate);

		ArrayList<QuoBenf> benefitsLife = quotationView.getMainLifeBenf();

		// file output
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);
		document.setTopMargin(5);

		// Arpico Logo
		Paragraph pLogo = new Paragraph();
		Image logo = new Image(ImageDataFactory.create(IMG));
		logo.setHeight(100);
		logo.setWidth(120);
		logo.setFixedPosition(460, 720);
		pLogo.add(logo);
		document.add(pLogo);

		document.add(new Paragraph("Arpico Insurance PLC Quotation").setBold().setFontSize(14)
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));

		document.add(new Paragraph(" "));

		// Agent Details
		float[] pointColumnWidths1 = { 70, 200 };
		Table agtTable = new Table(pointColumnWidths1);
		agtTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell agCell1 = new Cell();
		agCell1.setBorder(Border.NO_BORDER);
		agCell1.add(new Paragraph("Date").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell1);
		Cell agcell2 = new Cell();
		agcell2.setBorder(Border.NO_BORDER);
		agcell2.add(
				new Paragraph(": " + date).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell2);

		agtTable.startNewRow();

		Cell agCell3 = new Cell();
		agCell3.setBorder(Border.NO_BORDER);
		agCell3.add(
				new Paragraph("Branch Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell3);
		Cell agcell4 = new Cell();
		agcell4.setBorder(Border.NO_BORDER);
		agcell4.add(new Paragraph(quotationDetails.getQuotation().getUser().getBranch().getBranch_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getBranch().getBranch_Name()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell4);

		agtTable.startNewRow();

		Cell agCell5 = new Cell();
		agCell5.setBorder(Border.NO_BORDER);
		agCell5.add(
				new Paragraph("Agent Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell5);
		Cell agcell6 = new Cell();
		agcell6.setBorder(Border.NO_BORDER);
		agcell6.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Name()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell6);

		agtTable.startNewRow();

		Cell agcell7 = new Cell();
		agcell7.setBorder(Border.NO_BORDER);
		agcell7.add(
				new Paragraph("Agent Code").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell7);
		Cell agcell8 = new Cell();
		agcell8.setBorder(Border.NO_BORDER);
		agcell8.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Code() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Code()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell8);

		agtTable.startNewRow();

		Cell agcell9 = new Cell();
		agcell9.setBorder(Border.NO_BORDER);
		agcell9.add(
				new Paragraph("Contact No").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell9);

		Cell agCell10 = new Cell();
		agCell10.setBorder(Border.NO_BORDER);
		agCell10.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Mobile() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Mobile()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell10);

		document.add(agtTable);

		document.add(new Paragraph("ARPICO INVESTMENT BOND").setFontSize(10).setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		// customer Details Table
		float[] pointColumnWidths2 = { 100, 200 };
		Table cusTable = new Table(pointColumnWidths2);
		cusTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell cucell1 = new Cell();
		cucell1.setBorder(Border.NO_BORDER);
		cucell1.add(new Paragraph("Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cucell1);
		Cell cuCell2 = new Cell();
		cuCell2.setBorder(Border.NO_BORDER);
		cuCell2.add(new Paragraph(quotationDetails.getCustomerDetails().getCustName() != null
				? ": " + quotationDetails.getCustomerDetails().getCustName()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCell2);

		cusTable.startNewRow();

		Cell cucell3 = new Cell();
		cucell3.setBorder(Border.NO_BORDER);
		cucell3.add(new Paragraph("Age Next Birthday").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		cusTable.addCell(cucell3);
		Cell cuCell4 = new Cell();
		cuCell4.setBorder(Border.NO_BORDER);
		if (quoCustomer.getMainLifeAge() != null) {
			cuCell4.add(new Paragraph(": " + quoCustomer.getMainLifeAge()).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCell4.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCell4);

		cusTable.startNewRow();

		Cell cucell5 = new Cell();
		cucell5.setBorder(Border.NO_BORDER);
		cucell5.add(
				new Paragraph("Occupation").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cucell5);
		Cell cuCell6 = new Cell();
		cuCell6.setBorder(Border.NO_BORDER);
		cuCell6.add(new Paragraph(quotationDetails.getCustomerDetails().getOccupation().getOcupationName() != null
				? ": " + quotationDetails.getCustomerDetails().getOccupation().getOcupationName()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCell6);

		document.add(cusTable);

		// creating Quotation Details Tables
		float[] pointColumnWidths5 = { 80, 150 };
		Table DtlTable = new Table(pointColumnWidths5);
		DtlTable.setFixedPosition(350, 620, 230);

		Cell dlcell1 = new Cell();
		dlcell1.setBorder(Border.NO_BORDER);
		dlcell1.add(new Paragraph("Term").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell1);
		Cell dlcell2 = new Cell();
		dlcell2.setBorder(Border.NO_BORDER);
		if (quotationDetails.getPolTerm() != null) {
			dlcell2.add(new Paragraph(": " + Integer.toString(quotationDetails.getPolTerm())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell2.add(new Paragraph(": ").setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		DtlTable.addCell(dlcell2);

		DtlTable.startNewRow();

		Cell dlcell3 = new Cell();
		dlcell3.setBorder(Border.NO_BORDER);
		dlcell3.add(new Paragraph("Mode").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell3);
		Cell dlcell4 = new Cell();
		dlcell4.setBorder(Border.NO_BORDER);

		if (quotationDetails.getPayMode() != null) {
			if (quotationDetails.getPayMode().equalsIgnoreCase("M")) {
				dlcell4.add(new Paragraph(": Monthly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("Y")) {
				dlcell4.add(new Paragraph(": Yearly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("Q")) {
				dlcell4.add(new Paragraph(": Quarterly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("H")) {
				dlcell4.add(new Paragraph(": Half Yearly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("S")) {
				dlcell4.add(new Paragraph(": Single Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		}
		DtlTable.addCell(dlcell4);

		DtlTable.startNewRow();

		Cell dlcell5 = new Cell();
		dlcell5.setBorder(Border.NO_BORDER);
		dlcell5.add(
				new Paragraph("Mode Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell5);
		Cell dlcell6 = new Cell();
		dlcell6.setBorder(Border.NO_BORDER);
		if (quoCustomer.getModePremium() != null) {
			dlcell6.add(new Paragraph(": " + Double.toString(quoCustomer.getModePremium())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell6.add(new Paragraph("").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell6);

		DtlTable.startNewRow();

		Cell dlcell7 = new Cell();
		dlcell7.setBorder(Border.NO_BORDER);
		dlcell7.add(
				new Paragraph("Policy Fees").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell7);
		Cell dlcell8 = new Cell();
		dlcell8.setBorder(Border.NO_BORDER);
		if (quotationDetails.getPolicyFee() != null) {
			dlcell8.add(new Paragraph(": " + Double.toString(quotationDetails.getPolicyFee())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		} else {
			dlcell8.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell8);

		DtlTable.startNewRow();

		Cell dlcell9 = new Cell();
		dlcell9.setBorder(Border.NO_BORDER);
		dlcell9.add(new Paragraph("Total Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		DtlTable.addCell(dlcell9);
		Cell dlcell10 = new Cell();
		dlcell10.setBorder(Border.NO_BORDER);
		if (quoCustomer.getTotPremium() != null) {
			dlcell10.add(new Paragraph(": " + Double.toString(quoCustomer.getTotPremium())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell10.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell10);

		document.add(DtlTable);

		document.add(new Paragraph("\n"));

		document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		// Creating Benefits Table
		float[] pointColumnWidths3 = { 500, 200 };
		Table benLivTable = new Table(pointColumnWidths3);
		benLivTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell cell1 = new Cell();
		cell1.setHeight(12);
		cell1.add(new Paragraph("Living Benefits").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benLivTable.addCell(cell1);
		Cell cell2 = new Cell();
		cell1.setHeight(12);
		cell2.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benLivTable.addCell(cell2);

		benLivTable.startNewRow();

		if (quotationView.getMainLifeBenf() != null) {

			for (QuoBenf quoBenf : benefitsLife) {

				Cell cell3 = new Cell();
				cell3.add(new Paragraph(quoBenf.getBenfName() != null ? quoBenf.getBenfName() : "").setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT));
				benLivTable.addCell(cell3);
				Cell cell4 = new Cell();
				cell4.setHeight(12);
				if (quoBenf.getRiderSum() != null) {
					cell4.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));

				} else {
					cell4.add(new Paragraph("").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

				}
				benLivTable.addCell(cell4);

				benLivTable.startNewRow();
			}
		} else {

		}

		document.add(benLivTable);

		document.add(new Paragraph("\n"));

		// Create Additional Benefits Table
		float[] pointColumnWidths4 = { 300, 500 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell abCell1 = new Cell(0, 2);
		abCell1.add(new Paragraph("Additional Benefits").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell1);

		benAddTable.startNewRow();

		Cell abCell2 = new Cell();
		abCell2.add(new Paragraph("Accidental Death").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCell2);
		Cell abCell3 = new Cell();
		abCell3.add(new Paragraph("Twice the value of premium paid or account balance which ever is higher")
				.setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCell3);

		benAddTable.startNewRow();

		Cell abCell4 = new Cell();
		abCell4.add(new Paragraph("Natural Death").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCell4);
		Cell abCell5 = new Cell();
		abCell5.add(new Paragraph("Account balance or 125% of the premium paid, which ever is higher").setFontSize(9)
				.setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCell5);

		document.add(benAddTable);

		document.add(new Paragraph(""));

		document.add(new Paragraph("Special Notes").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		// Creating a Special Notes List
		List list = new List(ListNumberingType.DECIMAL);
		list.setFontSize(10);
		ListItem item1 = new ListItem();
		item1.add(new Paragraph("This is an indicative quote only and is valid for 30 days from date of issue.")
				.setFontSize(10).setFixedLeading(2));
		list.add(item1);

		ListItem item2 = new ListItem();
		item2.add(new Paragraph("All Amounts are in Sri Lankan Rupees (LKR).").setFontSize(10).setFixedLeading(2));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("Initial policy processing fee of Rs 300 (Payable only with initial deposit).")
				.setFontSize(10).setFixedLeading(2));
		list.add(item3);

		document.add(list);

		document.add(new Paragraph("\n"));
		document.add(new Paragraph("This is a system generated report and therefore does not require a signature.")
				.setFontSize(7).setBold());

		document.close();

		return baos.toByteArray();
	}

	// Create Arpico Investment Plan Report
	@Override
	public byte[] createINVPReport(QuotationDetails quotationDetails, QuotationView quotationView,
			QuoCustomer quoCustomer) throws Exception {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date quoCreateDate = quotationDetails.getQuotationquotationCreateDate();
		String date = dateFormat.format(quoCreateDate);

		ArrayList<QuoBenf> benefitsLife = quotationView.getMainLifeBenf();
		ArrayList<QuoBenf> benefitsSpouse = quotationView.getSpouseBenf();
		ArrayList<QuoChildBenef> benefitsChild = quotationView.getChildBenf();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);
		document.setTopMargin(5);

		// logo
		Paragraph pLogo = new Paragraph();
		Image logo = new Image(ImageDataFactory.create(IMG));
		logo.setHeight(100);
		logo.setWidth(120);
		logo.setFixedPosition(460, 720);
		pLogo.add(logo);
		document.add(pLogo);

		document.add(new Paragraph("Arpico Insurance PLC Quotation").setBold().setFontSize(14)
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		document.add(new Paragraph(" "));

		// Agent Details
		float[] pointColumnWidths1 = { 70, 200 };
		Table agtTable = new Table(pointColumnWidths1);
		agtTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell agCell1 = new Cell();
		agCell1.setBorder(Border.NO_BORDER);
		agCell1.add(new Paragraph("Date").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell1);
		Cell agcell2 = new Cell();
		agcell2.setBorder(Border.NO_BORDER);
		agcell2.add(
				new Paragraph(": " + date).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell2);

		agtTable.startNewRow();

		Cell agCell3 = new Cell();
		agCell3.setBorder(Border.NO_BORDER);
		agCell3.add(
				new Paragraph("Branch Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell3);
		Cell agcell4 = new Cell();
		agcell4.setBorder(Border.NO_BORDER);
		agcell4.add(new Paragraph(quotationDetails.getQuotation().getUser().getBranch().getBranch_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getBranch().getBranch_Name()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell4);

		agtTable.startNewRow();

		Cell agCell5 = new Cell();
		agCell5.setBorder(Border.NO_BORDER);
		agCell5.add(
				new Paragraph("Agent Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell5);
		Cell agcell6 = new Cell();
		agcell6.setBorder(Border.NO_BORDER);
		agcell6.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Name()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell6);

		agtTable.startNewRow();

		Cell agcell7 = new Cell();
		agcell7.setBorder(Border.NO_BORDER);
		agcell7.add(
				new Paragraph("Agent Code").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell7);
		Cell agcell8 = new Cell();
		agcell8.setBorder(Border.NO_BORDER);
		agcell8.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Code() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Code()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell8);

		agtTable.startNewRow();

		Cell agcell9 = new Cell();
		agcell9.setBorder(Border.NO_BORDER);
		agcell9.add(
				new Paragraph("Contact No").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell9);

		Cell agCell10 = new Cell();
		agCell10.setBorder(Border.NO_BORDER);
		agCell10.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Mobile() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Mobile()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell10);

		document.add(agtTable);

		// checking arpico investment plan or super investment plan
		if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("INVP")) {
			document.add(new Paragraph("ARPICO INVESTMENT PLAN").setFontSize(10).setUnderline().setCharacterSpacing(1));
		} else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("ASIP")) {
			document.add(new Paragraph("ARPICO SUPER INVESTMENT PLAN").setFontSize(10).setUnderline()
					.setCharacterSpacing(1));
		}
		document.add(new Paragraph(""));

		// customer Details
		float[] pointColumnWidths2 = { 150, 200 };
		Table cusTable = new Table(pointColumnWidths2);
		cusTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell cucell1 = new Cell();
		cucell1.setBorder(Border.NO_BORDER);
		cucell1.add(new Paragraph("Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cucell1);
		Cell cuCell2 = new Cell();
		cuCell2.setBorder(Border.NO_BORDER);
		cuCell2.add(new Paragraph(quotationDetails.getCustomerDetails().getCustName() != null
				? ": " + quotationDetails.getCustomerDetails().getCustName()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCell2);

		cusTable.startNewRow();

		Cell cucell3 = new Cell();
		cucell3.setBorder(Border.NO_BORDER);
		cucell3.add(new Paragraph("Age Next Birthday").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		cusTable.addCell(cucell3);
		Cell cuCell4 = new Cell();
		cuCell4.setBorder(Border.NO_BORDER);
		if (quoCustomer.getMainLifeAge() != null) {
			cuCell4.add(new Paragraph(": " + quoCustomer.getMainLifeAge()).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCell4.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCell4);

		cusTable.startNewRow();

		Cell cucell5 = new Cell();
		cucell5.setBorder(Border.NO_BORDER);
		cucell5.add(
				new Paragraph("Occupation").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cucell5);
		Cell cuCell6 = new Cell();
		cuCell6.setBorder(Border.NO_BORDER);
		cuCell6.add(new Paragraph(quotationDetails.getCustomerDetails().getOccupation().getOcupationName() != null
				? ": " + quotationDetails.getCustomerDetails().getOccupation().getOcupationName()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCell6);

		cusTable.startNewRow();

		Cell cucelleempty = new Cell(0, 2);
		cucelleempty.setBorder(Border.NO_BORDER);
		cucelleempty.add(new Paragraph(" ").setFixedLeading(10));
		cusTable.addCell(cucelleempty);

		cusTable.startNewRow();

		Cell cucelleempty1 = new Cell(0, 2);
		cucelleempty1.setBorder(Border.NO_BORDER);
		cucelleempty1.add(new Paragraph(" ").setFixedLeading(10));
		cusTable.addCell(cucelleempty1);

		cusTable.startNewRow();

		// checking Spouse is active or not
		if ((quoCustomer.getSpouseName()) != null) {

			Cell cucell7 = new Cell();
			cucell7.setBorder(Border.NO_BORDER);
			cucell7.add(new Paragraph("Name Of Spouse").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cucell7);
			Cell cuCell8 = new Cell();
			cuCell8.setBorder(Border.NO_BORDER);
			cuCell8.add(new Paragraph(": " + quoCustomer.getSpouseName()).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCell8);

		} else {

		}

		cusTable.startNewRow();

		if ((quoCustomer.getSpouseAge()) != null) {

			Cell cucell9 = new Cell();
			cucell9.setBorder(Border.NO_BORDER);
			cucell9.add(new Paragraph("Age Next Birthday (Spouse)").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cucell9);
			Cell cuCell10 = new Cell();
			cuCell10.setBorder(Border.NO_BORDER);
			cuCell10.add(new Paragraph(": " + Integer.toString(quoCustomer.getSpouseAge())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCell10);

		} else {

		}

		cusTable.startNewRow();

		if ((quoCustomer.getSpouseOccupation()) != null) {

			Cell cucell11 = new Cell();
			cucell11.setBorder(Border.NO_BORDER);
			cucell11.add(new Paragraph("Occupation (Spouse)").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cucell11);
			Cell cuCell12 = new Cell();
			cuCell12.setBorder(Border.NO_BORDER);
			cuCell12.add(new Paragraph(": " + quoCustomer.getSpouseOccupation()).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCell12);

		} else {

		}

		document.add(cusTable);

		// creating Quotation Details Tables
		float[] pointColumnWidths5 = { 80, 150 };
		Table DtlTable = new Table(pointColumnWidths5);
		DtlTable.setFixedPosition(350, 650, 230);

		Cell dlcell1 = new Cell();
		dlcell1.setBorder(Border.NO_BORDER);
		dlcell1.add(new Paragraph("Term").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell1);
		Cell dlcell2 = new Cell();
		dlcell2.setBorder(Border.NO_BORDER);
		if (quotationDetails.getPolTerm() != null) {
			dlcell2.add(new Paragraph(": " + Integer.toString(quotationDetails.getPolTerm())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell2.add(new Paragraph(": ").setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		DtlTable.addCell(dlcell2);

		DtlTable.startNewRow();

		Cell dlcell3 = new Cell();
		dlcell3.setBorder(Border.NO_BORDER);
		dlcell3.add(new Paragraph("Mode").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell3);
		Cell dlcell4 = new Cell();
		dlcell4.setBorder(Border.NO_BORDER);

		if (quotationDetails.getPayMode() != null) {
			if (quotationDetails.getPayMode().equalsIgnoreCase("M")) {
				dlcell4.add(new Paragraph(": Monthly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("Y")) {
				dlcell4.add(new Paragraph(": Yearly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("Q")) {
				dlcell4.add(new Paragraph(": Quarterly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("H")) {
				dlcell4.add(new Paragraph(": Half Yearly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("S")) {
				dlcell4.add(new Paragraph(": Single Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		}
		DtlTable.addCell(dlcell4);

		DtlTable.startNewRow();

		Cell dlcell5 = new Cell();
		dlcell5.setBorder(Border.NO_BORDER);
		dlcell5.add(
				new Paragraph("Mode Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell5);
		Cell dlcell6 = new Cell();
		dlcell6.setBorder(Border.NO_BORDER);
		if (quoCustomer.getModePremium() != null) {
			dlcell6.add(new Paragraph(": " + Double.toString(quoCustomer.getModePremium())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell6.add(new Paragraph("").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell6);

		document.add(DtlTable);

		document.add(new Paragraph(""));

		document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		// Create Additional Benefits Table
		float[] pointColumnWidths4 = { 450, 150, 150 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell abCell1 = new Cell();
		abCell1.add(new Paragraph("Additional Benefits").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell1);
		Cell abCell2 = new Cell();
		abCell2.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell2);
		Cell abCell3 = new Cell();
		abCell3.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell3);

		benAddTable.startNewRow();

		Cell abCell4 = new Cell(0, 3);
		abCell4.add(new Paragraph("Main Life").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell4);

		benAddTable.startNewRow();

		// checking main life benefits having or not
		for (QuoBenf quoBenf : benefitsLife) {

			if (quoBenf.getBenfName() != null) {

				Cell abCell5 = new Cell();
				abCell5.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell5);
				Cell abCell6 = new Cell();
				abCell6.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell6);
				Cell abCell7 = new Cell();
				abCell7.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell7);

				benAddTable.startNewRow();

			} else {

			}

		}

		benAddTable.startNewRow();

		Cell abCell8 = new Cell(0, 3);
		abCell8.add(new Paragraph("Spouse").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell8);

		benAddTable.startNewRow();

		// checking spouse having benefits
		for (QuoBenf quoBenf : benefitsSpouse) {

			if (quoBenf.getBenfName() != null) {

				Cell abCell9 = new Cell();
				abCell9.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell9);
				Cell abCell10 = new Cell();
				abCell10.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell10);
				Cell abCell11 = new Cell();
				abCell11.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell11);

				benAddTable.startNewRow();
			} else {

			}

		}

		benAddTable.startNewRow();

		Cell abCell12 = new Cell(0, 3);
		abCell12.add(new Paragraph("Children").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell12);

		benAddTable.startNewRow();

		for (QuoChildBenef quoChild : benefitsChild) {

			if (quoChild.getChild().getChildName() != null) {

				Cell abCell13 = new Cell(0, 3);
				abCell13.add(new Paragraph("Name : " + quoChild.getChild().getChildName() + " Relationship : "
						+ quoChild.getChild().getChildRelation()).setFontSize(9).setBold()
								.setTextAlignment(TextAlignment.LEFT).setCharacterSpacing(1));
				benAddTable.addCell(abCell13);
				benAddTable.startNewRow();

				// loop again to get children benf
				for (QuoChildBenef quoChildBenef : benefitsChild) {
					Cell abCell14 = new Cell();
					abCell14.add(new Paragraph(quoChildBenef.getBenfs().get(0).getBenfName()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT));
					benAddTable.addCell(abCell14);
					Cell abCell15 = new Cell();
					abCell15.add(new Paragraph(Double.toString(quoChildBenef.getBenfs().get(0).getRiderSum()))
							.setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCell15);
					Cell abCell16 = new Cell();
					abCell16.add(new Paragraph(Double.toString(quoChild.getBenfs().get(0).getPremium())).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCell16);
					benAddTable.startNewRow();
				}

			} else {

			}

		}

		document.add(benAddTable);

		document.add(new Paragraph(""));
		document.add(new Paragraph("Special Notes").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		// Creating a Special Notes List
		List list = new List(ListNumberingType.DECIMAL);
		list.setFontSize(10);
		ListItem item1 = new ListItem();
		item1.add(new Paragraph(
				"If HRB / SUHRB is obtained, the total cover will be applicable for the whole family per policy year.")
						.setFontSize(10).setFixedLeading(2));
		list.add(item1);

		ListItem item2 = new ListItem();
		item2.add(
				new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.")
						.setFontSize(10).setFixedLeading(2));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date issue.")
				.setFontSize(10).setFixedLeading(2));
		list.add(item3);

		ListItem item4 = new ListItem();
		item4.add(new Paragraph("All amounts are in Sri Lankan Rupees(LKR).").setFontSize(10).setFixedLeading(2));
		list.add(item4);

		ListItem item5 = new ListItem();
		item5.add(new Paragraph(
				"In event of death by accident both Accident Cover and Natural Death Cover will be applicable.")
						.setFontSize(10).setFixedLeading(2));
		list.add(item5);

		ListItem item6 = new ListItem();
		item6.add(new Paragraph("Guranteed minimum dividend rate declared for 2016 - 7.25%").setFontSize(10)
				.setFixedLeading(2));
		list.add(item6);

		document.add(list);

		document.add(new Paragraph("\n"));
		document.add(new Paragraph("This is a system generated report and therefore does not require a signature.")
				.setFontSize(7).setBold());

		document.close();

		return baos.toByteArray();

	}

	// Creating DTA Report
	@Override
	public byte[] createDTAReport(QuotationDetails quotationDetails, QuotationView quotationView,
			QuoCustomer quoCustomer) throws Exception {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date quoCreateDate = quotationDetails.getQuotationquotationCreateDate();
		String date = dateFormat.format(quoCreateDate);

		ArrayList<QuoBenf> benefitsLife = quotationView.getMainLifeBenf();
		java.util.List<Shedule> shedules = sheduleDao.findByQuotationDetails(quotationDetails);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);
		document.setTopMargin(5);

		// logo
		Paragraph pLogo = new Paragraph();
		Image logo = new Image(ImageDataFactory.create(IMG));
		logo.setHeight(100);
		logo.setWidth(120);
		logo.setFixedPosition(460, 720);
		pLogo.add(logo);
		document.add(pLogo);

		document.add(new Paragraph("Arpico Insurance PLC Quotation").setBold().setFontSize(14)
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));

		document.add(new Paragraph(" "));

		// Agent Details
		float[] pointColumnWidths1 = { 70, 200 };
		Table agtTable = new Table(pointColumnWidths1);
		agtTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell agCell1 = new Cell();
		agCell1.setBorder(Border.NO_BORDER);
		agCell1.add(new Paragraph("Date").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell1);
		Cell agcell2 = new Cell();
		agcell2.setBorder(Border.NO_BORDER);
		agcell2.add(
				new Paragraph(": " + date).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell2);

		agtTable.startNewRow();

		Cell agCell3 = new Cell();
		agCell3.setBorder(Border.NO_BORDER);
		agCell3.add(
				new Paragraph("Branch Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell3);
		Cell agcell4 = new Cell();
		agcell4.setBorder(Border.NO_BORDER);
		agcell4.add(new Paragraph(quotationDetails.getQuotation().getUser().getBranch().getBranch_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getBranch().getBranch_Name()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell4);

		agtTable.startNewRow();

		Cell agCell5 = new Cell();
		agCell5.setBorder(Border.NO_BORDER);
		agCell5.add(
				new Paragraph("Agent Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell5);
		Cell agcell6 = new Cell();
		agcell6.setBorder(Border.NO_BORDER);
		agcell6.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Name()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell6);

		agtTable.startNewRow();

		Cell agcell7 = new Cell();
		agcell7.setBorder(Border.NO_BORDER);
		agcell7.add(
				new Paragraph("Agent Code").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell7);
		Cell agcell8 = new Cell();
		agcell8.setBorder(Border.NO_BORDER);
		agcell8.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Code() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Code()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell8);

		agtTable.startNewRow();

		Cell agcell9 = new Cell();
		agcell9.setBorder(Border.NO_BORDER);
		agcell9.add(
				new Paragraph("Contact No").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell9);

		Cell agCell10 = new Cell();
		agCell10.setBorder(Border.NO_BORDER);
		agCell10.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Mobile() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Mobile()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell10);

		document.add(agtTable);

		if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("DTA")) {
			document.add(new Paragraph("ARPICO DECRASING TERM ASSURANCE FOR HOUSING LOAN").setFontSize(10)
					.setUnderline().setCharacterSpacing(1));
		} else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("DTAPL")) {
			document.add(new Paragraph("ARPICO DECRASING TERM ASSURANCE FOR PERSONAL LOAN").setFontSize(10)
					.setUnderline().setCharacterSpacing(1));
		}

		document.add(new Paragraph(""));

		// customer Details
		float[] pointColumnWidths2 = { 100, 200 };
		Table cusTable = new Table(pointColumnWidths2);
		cusTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell cucell1 = new Cell();
		cucell1.setBorder(Border.NO_BORDER);
		cucell1.add(new Paragraph("Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cucell1);
		Cell cuCell2 = new Cell();
		cuCell2.setBorder(Border.NO_BORDER);
		cuCell2.add(new Paragraph(quotationDetails.getCustomerDetails().getCustName() != null
				? ": " + quotationDetails.getCustomerDetails().getCustName()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCell2);

		cusTable.startNewRow();

		Cell cucell3 = new Cell();
		cucell3.setBorder(Border.NO_BORDER);
		cucell3.add(new Paragraph("Age Next Birthday").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		cusTable.addCell(cucell3);
		Cell cuCell4 = new Cell();
		cuCell4.setBorder(Border.NO_BORDER);
		if (quoCustomer.getMainLifeAge() != null) {
			cuCell4.add(new Paragraph(": " + quoCustomer.getMainLifeAge()).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCell4.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCell4);

		cusTable.startNewRow();

		Cell cucell5 = new Cell();
		cucell5.setBorder(Border.NO_BORDER);
		cucell5.add(
				new Paragraph("Occupation").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cucell5);
		Cell cuCell6 = new Cell();
		cuCell6.setBorder(Border.NO_BORDER);
		cuCell6.add(new Paragraph(quotationDetails.getCustomerDetails().getOccupation().getOcupationName() != null
				? ": " + quotationDetails.getCustomerDetails().getOccupation().getOcupationName()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCell6);

		document.add(cusTable);

		// creating Quotation Details Tables
		float[] pointColumnWidths5 = { 80, 150 };
		Table DtlTable = new Table(pointColumnWidths5);
		DtlTable.setFixedPosition(350, 620, 230);

		Cell dlcell1 = new Cell();
		dlcell1.setBorder(Border.NO_BORDER);
		dlcell1.add(new Paragraph("Interest Rate").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		DtlTable.addCell(dlcell1);
		Cell dlcell2 = new Cell();
		dlcell2.setBorder(Border.NO_BORDER);
		if (quotationDetails.getInterestRate() != null) {
			dlcell2.add(new Paragraph(": " + Double.toString(quotationDetails.getInterestRate())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		} else {

		}
		DtlTable.addCell(dlcell2);

		DtlTable.startNewRow();

		Cell dlcell3 = new Cell();
		dlcell3.setBorder(Border.NO_BORDER);
		dlcell3.add(
				new Paragraph("Loan Amount").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell3);
		Cell dlcell4 = new Cell();
		dlcell4.setBorder(Border.NO_BORDER);
		if (quotationDetails.getBaseSum() != null) {
			dlcell4.add(new Paragraph(": " + Double.toString(quotationDetails.getBaseSum())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		} else {

		}
		DtlTable.addCell(dlcell4);

		Cell dlcell5 = new Cell();
		dlcell5.setBorder(Border.NO_BORDER);
		dlcell5.add(new Paragraph("Term").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell5);
		Cell dlcell6 = new Cell();
		dlcell6.setBorder(Border.NO_BORDER);
		if (quotationDetails.getPolTerm() != null) {
			dlcell6.add(new Paragraph(": " + Integer.toString(quotationDetails.getPolTerm())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell2.add(new Paragraph(": ").setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		DtlTable.addCell(dlcell6);

		DtlTable.startNewRow();

		Cell dlcell7 = new Cell();
		dlcell7.setBorder(Border.NO_BORDER);
		dlcell7.add(new Paragraph("Mode").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell7);
		Cell dlcell8 = new Cell();
		dlcell8.setBorder(Border.NO_BORDER);

		if (quotationDetails.getPayMode() != null) {
			if (quotationDetails.getPayMode().equalsIgnoreCase("M")) {
				dlcell8.add(new Paragraph(": Monthly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("Y")) {
				dlcell8.add(new Paragraph(": Yearly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("Q")) {
				dlcell8.add(new Paragraph(": Quarterly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("H")) {
				dlcell8.add(new Paragraph(": Half Yearly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("S")) {
				dlcell8.add(new Paragraph(": Single Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		}
		DtlTable.addCell(dlcell8);

		DtlTable.startNewRow();

		Cell dlcell9 = new Cell();
		dlcell9.setBorder(Border.NO_BORDER);
		dlcell9.add(
				new Paragraph("Mode Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell9);
		Cell dlcell10 = new Cell();
		dlcell10.setBorder(Border.NO_BORDER);
		if (quoCustomer.getModePremium() != null) {
			dlcell10.add(new Paragraph(": " + Double.toString(quoCustomer.getModePremium())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell10.add(new Paragraph("").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell10);

		document.add(DtlTable);

		document.add(new Paragraph("\n"));

		document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		// Create Additional Benefits Table
		float[] pointColumnWidths4 = { 450, 150, 150 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell abCell1 = new Cell();
		abCell1.add(new Paragraph("Additional Benefits").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell1);
		Cell abCell2 = new Cell();
		abCell2.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell2);
		Cell abCell3 = new Cell();
		abCell3.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell3);

		benAddTable.startNewRow();

		Cell abCell4 = new Cell(0, 3);
		abCell4.add(new Paragraph("Main Life").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell4);

		benAddTable.startNewRow();

		Cell abCell5 = new Cell();
		abCell5.add(new Paragraph("Basic Sum Assured").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCell5);
		Cell abCell6 = new Cell();
		if (quotationDetails.getBaseSum() != null) {
			abCell6.add(new Paragraph(Double.toString(quotationDetails.getBaseSum())).setFontSize(9)
					.setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCell6.add(new Paragraph("").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		}
		benAddTable.addCell(abCell6);
		Cell abCell7 = new Cell();
		if (quoCustomer.getMode().equalsIgnoreCase("Q")) {
			if (quotationDetails.getPremiumMonth() != null) {
				abCell7.add(new Paragraph(Double.toString(quotationDetails.getPremiumQuater())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
			} else {

			}
		} else if (quoCustomer.getMode().equalsIgnoreCase("M")) {
			if (quotationDetails.getPremiumMonth() != null) {
				abCell7.add(new Paragraph(Double.toString(quotationDetails.getPremiumMonth())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
			} else {

			}
		} else if (quoCustomer.getMode().equalsIgnoreCase("Y")) {
			if (quotationDetails.getPremiumYear() != null) {
				abCell7.add(new Paragraph(Double.toString(quotationDetails.getPremiumYear())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
			} else {

			}
		} else if (quoCustomer.getMode().equalsIgnoreCase("S")) {
			if (quotationDetails.getPremiumSingle() != null) {
				abCell7.add(new Paragraph(Double.toString(quotationDetails.getPremiumSingle())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
			} else {

			}
		} else if (quoCustomer.getMode().equalsIgnoreCase("H")) {
			if (quotationDetails.getPremiumHalf() != null) {
				abCell7.add(new Paragraph(Double.toString(quotationDetails.getPremiumHalf())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
			} else {

			}
		}

		benAddTable.addCell(abCell7);

		benAddTable.startNewRow();

		for (QuoBenf quoBenf : benefitsLife) {

			Cell abCell8 = new Cell();
			if (quoBenf.getBenfName() != null) {
				abCell8.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
			} else {

			}
			benAddTable.addCell(abCell8);

			Cell abCell9 = new Cell();
			if (quoBenf.getRiderSum() != null) {
				abCell9.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
			} else {

			}
			benAddTable.addCell(abCell9);

			Cell abCell10 = new Cell();
			if (quoBenf.getPremium() != null) {
				abCell10.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
			} else {

			}
			benAddTable.addCell(abCell10);

			benAddTable.startNewRow();

		}

		document.add(benAddTable);

		document.add(new Paragraph(""));

		document.add(new Paragraph("Schedule").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));

		document.add(new Paragraph(""));

		float[] pointColumnWidths6 = { 70, 100, 100 };
		Table scdTable = new Table(pointColumnWidths6);
		scdTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell sdCell1 = new Cell();
		sdCell1.add(new Paragraph("Year").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		scdTable.addCell(sdCell1);
		Cell sdCell2 = new Cell();
		sdCell2.add(new Paragraph("Loan Balance").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		scdTable.addCell(sdCell2);
		Cell sdCell3 = new Cell();
		sdCell3.add(new Paragraph("Loan Protection").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		scdTable.addCell(sdCell3);

		scdTable.startNewRow();

		for (Shedule polShdl : shedules) {
			Cell sdCell4 = new Cell();
			if (polShdl.getPolicyYear() != null) {
				sdCell4.add(new Paragraph(Integer.toString(polShdl.getPolicyYear())).setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT));
			} else {

			}
			scdTable.addCell(sdCell4);

			Cell sdCell5 = new Cell();
			if (polShdl.getOutSum() != null) {
				sdCell5.add(new Paragraph(Double.toString(polShdl.getOutSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
			} else {

			}
			scdTable.addCell(sdCell5);

			Cell sdCell6 = new Cell();
			if (polShdl.getLorned() != null) {
				sdCell6.add(new Paragraph(Double.toString(polShdl.getLorned())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
			} else {

			}
			scdTable.addCell(sdCell6);

			scdTable.startNewRow();
		}

		document.add(scdTable);

		document.add(new Paragraph("Special Notes").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));

		document.add(new Paragraph(""));

		// Creating a Special Notes List
		List list = new List(ListNumberingType.DECIMAL);
		list.setFontSize(10);
		ListItem item1 = new ListItem();
		item1.add(new Paragraph(
				"If HRB / SUHRB is obtained, the total cover will be applicable for the whole family per policy year.")
						.setFontSize(10).setFixedLeading(2));
		list.add(item1);

		ListItem item2 = new ListItem();
		item2.add(
				new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.")
						.setFontSize(10).setFixedLeading(2));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date issue.")
				.setFontSize(10).setFixedLeading(2));
		list.add(item3);

		ListItem item4 = new ListItem();
		item4.add(new Paragraph("All amounts are in Sri Lankan Rupees(LKR).").setFontSize(10).setFixedLeading(2));
		list.add(item4);

		ListItem item5 = new ListItem();
		item5.add(new Paragraph("Initial policy processing fee of Rs.450 (Payable only with initial deposit).")
				.setFontSize(10).setFixedLeading(2));
		list.add(item5);

		ListItem item6 = new ListItem();
		item6.add(new Paragraph(
				"In event of death by accident both Accident Cover and Natural Death Cover will be applicable.")
						.setFontSize(10).setFixedLeading(2));
		list.add(item6);

		document.add(list);

		document.add(new Paragraph("\n"));
		document.add(new Paragraph("This is a system generated report and therefore does not require a signature.")
				.setFontSize(7).setBold());

		document.close();
		return baos.toByteArray();
	}

	@Override
	public byte[] createATRMReport(QuotationDetails quotationDetails, QuotationView quotationView,
			QuoCustomer quoCustomer) throws Exception {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date quoCreateDate = quotationDetails.getQuotationquotationCreateDate();
		String date = dateFormat.format(quoCreateDate);

		ArrayList<QuoBenf> benefitsLife = quotationView.getMainLifeBenf();
		ArrayList<QuoBenf> benefitsSpouse = quotationView.getSpouseBenf();
		ArrayList<QuoChildBenef> benefitsChild = quotationView.getChildBenf();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);
		document.setTopMargin(5);

		// logo
		Paragraph pLogo = new Paragraph();
		Image logo = new Image(ImageDataFactory.create(IMG));
		logo.setHeight(100);
		logo.setWidth(120);
		logo.setFixedPosition(460, 720);
		pLogo.add(logo);
		document.add(pLogo);

		document.add(new Paragraph("Arpico Insurance PLC Quotation").setBold().setFontSize(14)
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));

		document.add(new Paragraph(" "));

		// Agent Details
		float[] pointColumnWidths1 = { 70, 200 };
		Table agtTable = new Table(pointColumnWidths1);
		agtTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell agCell1 = new Cell();
		agCell1.setBorder(Border.NO_BORDER);
		agCell1.add(new Paragraph("Date").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell1);
		Cell agcell2 = new Cell();
		agcell2.setBorder(Border.NO_BORDER);
		agcell2.add(
				new Paragraph(": " + date).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell2);

		agtTable.startNewRow();

		Cell agCell3 = new Cell();
		agCell3.setBorder(Border.NO_BORDER);
		agCell3.add(
				new Paragraph("Branch Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell3);
		Cell agcell4 = new Cell();
		agcell4.setBorder(Border.NO_BORDER);
		agcell4.add(new Paragraph(quotationDetails.getQuotation().getUser().getBranch().getBranch_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getBranch().getBranch_Name()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell4);

		agtTable.startNewRow();

		Cell agCell5 = new Cell();
		agCell5.setBorder(Border.NO_BORDER);
		agCell5.add(
				new Paragraph("Agent Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell5);
		Cell agcell6 = new Cell();
		agcell6.setBorder(Border.NO_BORDER);
		agcell6.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Name()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell6);

		agtTable.startNewRow();

		Cell agcell7 = new Cell();
		agcell7.setBorder(Border.NO_BORDER);
		agcell7.add(
				new Paragraph("Agent Code").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell7);
		Cell agcell8 = new Cell();
		agcell8.setBorder(Border.NO_BORDER);
		agcell8.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Code() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Code()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell8);

		agtTable.startNewRow();

		Cell agcell9 = new Cell();
		agcell9.setBorder(Border.NO_BORDER);
		agcell9.add(
				new Paragraph("Contact No").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell9);

		Cell agCell10 = new Cell();
		agCell10.setBorder(Border.NO_BORDER);
		agCell10.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Mobile() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Mobile()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell10);

		document.add(agtTable);

		document.add(new Paragraph("ARPICO TERM PLAN").setFontSize(10).setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		// customer Details
		float[] pointColumnWidths2 = { 150, 200 };
		Table cusTable = new Table(pointColumnWidths2);
		cusTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell cucell1 = new Cell();
		cucell1.setBorder(Border.NO_BORDER);
		cucell1.add(new Paragraph("Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cucell1);
		Cell cuCell2 = new Cell();
		cuCell2.setBorder(Border.NO_BORDER);
		cuCell2.add(new Paragraph(quotationDetails.getCustomerDetails().getCustName() != null
				? ": " + quotationDetails.getCustomerDetails().getCustName()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCell2);

		cusTable.startNewRow();

		Cell cucell3 = new Cell();
		cucell3.setBorder(Border.NO_BORDER);
		cucell3.add(new Paragraph("Age Next Birthday").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		cusTable.addCell(cucell3);
		Cell cuCell4 = new Cell();
		cuCell4.setBorder(Border.NO_BORDER);
		if (quoCustomer.getMainLifeAge() != null) {
			cuCell4.add(new Paragraph(": " + quoCustomer.getMainLifeAge()).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCell4.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCell4);

		cusTable.startNewRow();

		Cell cucell5 = new Cell();
		cucell5.setBorder(Border.NO_BORDER);
		cucell5.add(
				new Paragraph("Occupation").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cucell5);
		Cell cuCell6 = new Cell();
		cuCell6.setBorder(Border.NO_BORDER);
		cuCell6.add(new Paragraph(quotationDetails.getCustomerDetails().getOccupation().getOcupationName() != null
				? ": " + quotationDetails.getCustomerDetails().getOccupation().getOcupationName()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCell6);

		cusTable.startNewRow();

		Cell cucelleempty = new Cell(0, 2);
		cucelleempty.setBorder(Border.NO_BORDER);
		cucelleempty.add(new Paragraph(" ").setFixedLeading(10));
		cusTable.addCell(cucelleempty);

		cusTable.startNewRow();

		Cell cucelleempty1 = new Cell(0, 2);
		cucelleempty1.setBorder(Border.NO_BORDER);
		cucelleempty1.add(new Paragraph(" ").setFixedLeading(10));
		cusTable.addCell(cucelleempty1);

		cusTable.startNewRow();

		// checking Spouse is active or not
		if ((quoCustomer.getSpouseName()) != null) {

			Cell cucell7 = new Cell();
			cucell7.setBorder(Border.NO_BORDER);
			cucell7.add(new Paragraph("Name Of Spouse").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cucell7);
			Cell cuCell8 = new Cell();
			cuCell8.setBorder(Border.NO_BORDER);
			cuCell8.add(new Paragraph(": " + quoCustomer.getSpouseName()).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCell8);

		} else {

		}

		cusTable.startNewRow();

		if ((quoCustomer.getSpouseAge()) != null) {

			Cell cucell9 = new Cell();
			cucell9.setBorder(Border.NO_BORDER);
			cucell9.add(new Paragraph("Age Next Birthday (Spouse)").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cucell9);
			Cell cuCell10 = new Cell();
			cuCell10.setBorder(Border.NO_BORDER);
			cuCell10.add(new Paragraph(": " + Integer.toString(quoCustomer.getSpouseAge())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCell10);

		} else {

		}

		cusTable.startNewRow();

		if ((quoCustomer.getSpouseOccupation()) != null) {

			Cell cucell11 = new Cell();
			cucell11.setBorder(Border.NO_BORDER);
			cucell11.add(new Paragraph("Occupation (Spouse)").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cucell11);
			Cell cuCell12 = new Cell();
			cuCell12.setBorder(Border.NO_BORDER);
			cuCell12.add(new Paragraph(": " + quoCustomer.getSpouseOccupation()).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCell12);

		} else {

		}

		document.add(cusTable);

		// creating Quotation Details Tables
		float[] pointColumnWidths5 = { 80, 150 };
		Table DtlTable = new Table(pointColumnWidths5);
		DtlTable.setFixedPosition(350, 650, 230);

		Cell dlcell1 = new Cell();
		dlcell1.setBorder(Border.NO_BORDER);
		dlcell1.add(new Paragraph("Term").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell1);
		Cell dlcell2 = new Cell();
		dlcell2.setBorder(Border.NO_BORDER);
		if (quotationDetails.getPolTerm() != null) {
			dlcell2.add(new Paragraph(": " + Integer.toString(quotationDetails.getPolTerm())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell2.add(new Paragraph(": ").setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		DtlTable.addCell(dlcell2);

		DtlTable.startNewRow();

		Cell dlcell3 = new Cell();
		dlcell3.setBorder(Border.NO_BORDER);
		dlcell3.add(new Paragraph("Mode").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell3);
		Cell dlcell4 = new Cell();
		dlcell4.setBorder(Border.NO_BORDER);

		if (quotationDetails.getPayMode() != null) {
			if (quotationDetails.getPayMode().equalsIgnoreCase("M")) {
				dlcell4.add(new Paragraph(": Monthly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("Y")) {
				dlcell4.add(new Paragraph(": Yearly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("Q")) {
				dlcell4.add(new Paragraph(": Quarterly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("H")) {
				dlcell4.add(new Paragraph(": Half Yearly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("S")) {
				dlcell4.add(new Paragraph(": Single Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		}
		DtlTable.addCell(dlcell4);

		DtlTable.startNewRow();

		Cell dlcell5 = new Cell();
		dlcell5.setBorder(Border.NO_BORDER);
		dlcell5.add(
				new Paragraph("Mode Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell5);
		Cell dlcell6 = new Cell();
		dlcell6.setBorder(Border.NO_BORDER);
		if (quoCustomer.getModePremium() != null) {
			dlcell6.add(new Paragraph(": " + Double.toString(quoCustomer.getModePremium())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell6.add(new Paragraph("").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell6);

		document.add(DtlTable);

		document.add(new Paragraph(""));

		document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		// Create Additional Benefits Table
		float[] pointColumnWidths4 = { 450, 150, 150 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell abCell1 = new Cell();
		abCell1.add(new Paragraph("Additional Benefits").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell1);
		Cell abCell2 = new Cell();
		abCell2.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell2);
		Cell abCell3 = new Cell();
		abCell3.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell3);

		benAddTable.startNewRow();

		Cell abCell4 = new Cell(0, 3);
		abCell4.add(new Paragraph("Main Life").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell4);

		benAddTable.startNewRow();

		// checking main life benefits having or not
		for (QuoBenf quoBenf : benefitsLife) {

			if (quoBenf.getBenfName() != null) {

				Cell abCell5 = new Cell();
				abCell5.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell5);
				Cell abCell6 = new Cell();
				abCell6.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell6);
				Cell abCell7 = new Cell();
				abCell7.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell7);

				benAddTable.startNewRow();

			} else {

			}

		}

		benAddTable.startNewRow();

		Cell abCell8 = new Cell(0, 3);
		abCell8.add(new Paragraph("Spouse").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell8);

		benAddTable.startNewRow();

		// checking spouse having benefits
		for (QuoBenf quoBenf : benefitsSpouse) {

			if (quoBenf.getBenfName() != null) {

				Cell abCell9 = new Cell();
				abCell9.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell9);
				Cell abCell10 = new Cell();
				abCell10.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell10);
				Cell abCell11 = new Cell();
				abCell11.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell11);

				benAddTable.startNewRow();
			} else {

			}

		}

		benAddTable.startNewRow();

		Cell abCell12 = new Cell(0, 3);
		abCell12.add(new Paragraph("Children").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell12);

		benAddTable.startNewRow();

		for (QuoChildBenef quoChild : benefitsChild) {

			if (quoChild.getChild().getChildName() != null) {

				Cell abCell13 = new Cell(0, 3);
				abCell13.add(new Paragraph("Name : " + quoChild.getChild().getChildName() + " Relationship : "
						+ quoChild.getChild().getChildRelation()).setFontSize(9).setBold()
								.setTextAlignment(TextAlignment.LEFT).setCharacterSpacing(1));
				benAddTable.addCell(abCell13);
				benAddTable.startNewRow();

				// loop again to get children benf
				for (QuoChildBenef quoChildBenef : benefitsChild) {
					Cell abCell14 = new Cell();
					abCell14.add(new Paragraph(quoChildBenef.getBenfs().get(0).getBenfName()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT));
					benAddTable.addCell(abCell14);
					Cell abCell15 = new Cell();
					abCell15.add(new Paragraph(Double.toString(quoChildBenef.getBenfs().get(0).getRiderSum()))
							.setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCell15);
					Cell abCell16 = new Cell();
					abCell16.add(new Paragraph(Double.toString(quoChild.getBenfs().get(0).getPremium())).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCell16);
					benAddTable.startNewRow();
				}

			} else {

			}

		}

		document.add(benAddTable);

		document.add(new Paragraph(""));

		document.add(new Paragraph("Special Notes").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));

		document.add(new Paragraph(""));

		// Creating a Special Notes List
		List list = new List(ListNumberingType.DECIMAL);
		list.setFontSize(10);
		ListItem item1 = new ListItem();
		item1.add(new Paragraph(
				"If HRB / SUHRB is obtained, the total cover will be applicable for the whole family per policy year.")
						.setFontSize(10).setFixedLeading(2));
		list.add(item1);

		ListItem item2 = new ListItem();
		item2.add(
				new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.")
						.setFontSize(10).setFixedLeading(2));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date issue.")
				.setFontSize(10).setFixedLeading(2));
		list.add(item3);

		ListItem item4 = new ListItem();
		item4.add(new Paragraph("All amounts are in Sri Lankan Rupees(LKR).").setFontSize(10).setFixedLeading(2));
		list.add(item4);

		ListItem item5 = new ListItem();
		item5.add(new Paragraph("Initial policy processing fee of Rs. 300 (Payable only with initial deposit).")
				.setFontSize(10).setFixedLeading(2));
		list.add(item5);

		ListItem item6 = new ListItem();
		item6.add(new Paragraph(
				"In event of death by accident both Accident Cover and Natural Death Cover will be applicable.")
						.setFontSize(10).setFixedLeading(2));
		list.add(item6);

		document.add(list);

		document.add(new Paragraph("\n"));
		document.add(new Paragraph("This is a system generated report and therefore does not require a signature.")
				.setFontSize(7).setBold());

		document.close();

		return baos.toByteArray();
	}

	@Override
	public byte[] createEND1Report(QuotationDetails quotationDetails, QuotationView quotationView,
			QuoCustomer quoCustomer) throws Exception {

		ArrayList<QuoBenf> benefitsLife = quotationView.getMainLifeBenf();
		ArrayList<QuoBenf> benefitsSpouse = quotationView.getSpouseBenf();
		ArrayList<QuoChildBenef> benefitsChild = quotationView.getChildBenf();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date quoCreateDate = quotationDetails.getQuotationquotationCreateDate();
		String date = dateFormat.format(quoCreateDate);

		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);
		document.setTopMargin(5);

		// logo
		Paragraph pLogo = new Paragraph();
		Image logo = new Image(ImageDataFactory.create(IMG));
		logo.setHeight(100);
		logo.setWidth(120);
		logo.setFixedPosition(460, 720);
		pLogo.add(logo);
		document.add(pLogo);

		document.add(new Paragraph("Arpico Insurance PLC Quotation").setBold().setFontSize(14)
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));

		document.add(new Paragraph(" "));

		// Agent Details
		float[] pointColumnWidths1 = { 70, 200 };
		Table agtTable = new Table(pointColumnWidths1);
		agtTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell agCell1 = new Cell();
		agCell1.setBorder(Border.NO_BORDER);
		agCell1.add(new Paragraph("Date").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell1);
		Cell agcell2 = new Cell();
		agcell2.setBorder(Border.NO_BORDER);
		agcell2.add(
				new Paragraph(": " + date).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell2);

		agtTable.startNewRow();

		Cell agCell3 = new Cell();
		agCell3.setBorder(Border.NO_BORDER);
		agCell3.add(
				new Paragraph("Branch Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell3);
		Cell agcell4 = new Cell();
		agcell4.setBorder(Border.NO_BORDER);
		agcell4.add(new Paragraph(quotationDetails.getQuotation().getUser().getBranch().getBranch_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getBranch().getBranch_Name()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell4);

		agtTable.startNewRow();

		Cell agCell5 = new Cell();
		agCell5.setBorder(Border.NO_BORDER);
		agCell5.add(
				new Paragraph("Agent Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell5);
		Cell agcell6 = new Cell();
		agcell6.setBorder(Border.NO_BORDER);
		agcell6.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Name()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell6);

		agtTable.startNewRow();

		Cell agcell7 = new Cell();
		agcell7.setBorder(Border.NO_BORDER);
		agcell7.add(
				new Paragraph("Agent Code").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell7);
		Cell agcell8 = new Cell();
		agcell8.setBorder(Border.NO_BORDER);
		agcell8.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Code() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Code()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell8);

		agtTable.startNewRow();

		Cell agcell9 = new Cell();
		agcell9.setBorder(Border.NO_BORDER);
		agcell9.add(
				new Paragraph("Contact No").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell9);

		Cell agCell10 = new Cell();
		agCell10.setBorder(Border.NO_BORDER);
		agCell10.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Mobile() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Mobile()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell10);

		document.add(agtTable);

		document.add(new Paragraph("ARPICO ENDOWMENT PLAN").setFontSize(10).setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		// customer Details
		float[] pointColumnWidths2 = { 150, 200 };
		Table cusTable = new Table(pointColumnWidths2);
		cusTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell cucell1 = new Cell();
		cucell1.setBorder(Border.NO_BORDER);
		cucell1.add(new Paragraph("Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cucell1);
		Cell cuCell2 = new Cell();
		cuCell2.setBorder(Border.NO_BORDER);
		cuCell2.add(new Paragraph(quotationDetails.getCustomerDetails().getCustName() != null
				? ": " + quotationDetails.getCustomerDetails().getCustName()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCell2);

		cusTable.startNewRow();

		Cell cucell3 = new Cell();
		cucell3.setBorder(Border.NO_BORDER);
		cucell3.add(new Paragraph("Age Next Birthday").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		cusTable.addCell(cucell3);
		Cell cuCell4 = new Cell();
		cuCell4.setBorder(Border.NO_BORDER);
		if (quoCustomer.getMainLifeAge() != null) {
			cuCell4.add(new Paragraph(": " + quoCustomer.getMainLifeAge()).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCell4.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCell4);

		cusTable.startNewRow();

		Cell cucell5 = new Cell();
		cucell5.setBorder(Border.NO_BORDER);
		cucell5.add(
				new Paragraph("Occupation").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cucell5);
		Cell cuCell6 = new Cell();
		cuCell6.setBorder(Border.NO_BORDER);
		cuCell6.add(new Paragraph(quotationDetails.getCustomerDetails().getOccupation().getOcupationName() != null
				? ": " + quotationDetails.getCustomerDetails().getOccupation().getOcupationName()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCell6);

		cusTable.startNewRow();

		Cell cucelleempty = new Cell(0, 2);
		cucelleempty.setBorder(Border.NO_BORDER);
		cucelleempty.add(new Paragraph(" ").setFixedLeading(10));
		cusTable.addCell(cucelleempty);

		cusTable.startNewRow();

		Cell cucelleempty1 = new Cell(0, 2);
		cucelleempty1.setBorder(Border.NO_BORDER);
		cucelleempty1.add(new Paragraph(" ").setFixedLeading(10));
		cusTable.addCell(cucelleempty1);

		cusTable.startNewRow();

		// checking Spouse is active or not
		if ((quoCustomer.getSpouseName()) != null) {

			Cell cucell7 = new Cell();
			cucell7.setBorder(Border.NO_BORDER);
			cucell7.add(new Paragraph("Name Of Spouse").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cucell7);
			Cell cuCell8 = new Cell();
			cuCell8.setBorder(Border.NO_BORDER);
			cuCell8.add(new Paragraph(": " + quoCustomer.getSpouseName()).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCell8);

		} else {

		}

		cusTable.startNewRow();

		if ((quoCustomer.getSpouseAge()) != null) {

			Cell cucell9 = new Cell();
			cucell9.setBorder(Border.NO_BORDER);
			cucell9.add(new Paragraph("Age Next Birthday (Spouse)").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cucell9);
			Cell cuCell10 = new Cell();
			cuCell10.setBorder(Border.NO_BORDER);
			cuCell10.add(new Paragraph(": " + Integer.toString(quoCustomer.getSpouseAge())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCell10);

		} else {

		}

		cusTable.startNewRow();

		if ((quoCustomer.getSpouseOccupation()) != null) {

			Cell cucell11 = new Cell();
			cucell11.setBorder(Border.NO_BORDER);
			cucell11.add(new Paragraph("Occupation (Spouse)").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cucell11);
			Cell cuCell12 = new Cell();
			cuCell12.setBorder(Border.NO_BORDER);
			cuCell12.add(new Paragraph(": " + quoCustomer.getSpouseOccupation()).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCell12);

		} else {

		}

		document.add(cusTable);

		// creating Quotation Details Tables
		float[] pointColumnWidths5 = { 80, 150 };
		Table DtlTable = new Table(pointColumnWidths5);
		DtlTable.setFixedPosition(350, 650, 230);

		Cell dlcell1 = new Cell();
		dlcell1.setBorder(Border.NO_BORDER);
		dlcell1.add(new Paragraph("Term").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell1);
		Cell dlcell2 = new Cell();
		dlcell2.setBorder(Border.NO_BORDER);
		if (quotationDetails.getPolTerm() != null) {
			dlcell2.add(new Paragraph(": " + Integer.toString(quotationDetails.getPolTerm())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell2.add(new Paragraph(": ").setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		DtlTable.addCell(dlcell2);

		DtlTable.startNewRow();

		Cell dlcell3 = new Cell();
		dlcell3.setBorder(Border.NO_BORDER);
		dlcell3.add(new Paragraph("Mode").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell3);
		Cell dlcell4 = new Cell();
		dlcell4.setBorder(Border.NO_BORDER);

		if (quotationDetails.getPayMode() != null) {
			if (quotationDetails.getPayMode().equalsIgnoreCase("M")) {
				dlcell4.add(new Paragraph(": Monthly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("Y")) {
				dlcell4.add(new Paragraph(": Yearly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("Q")) {
				dlcell4.add(new Paragraph(": Quarterly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("H")) {
				dlcell4.add(new Paragraph(": Half Yearly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("S")) {
				dlcell4.add(new Paragraph(": Single Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		}
		DtlTable.addCell(dlcell4);

		DtlTable.startNewRow();

		Cell dlcell5 = new Cell();
		dlcell5.setBorder(Border.NO_BORDER);
		dlcell5.add(
				new Paragraph("Mode Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell5);
		Cell dlcell6 = new Cell();
		dlcell6.setBorder(Border.NO_BORDER);
		if (quoCustomer.getModePremium() != null) {
			dlcell6.add(new Paragraph(": " + Double.toString(quoCustomer.getModePremium())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell6.add(new Paragraph("").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell6);

		document.add(DtlTable);

		document.add(new Paragraph(""));

		document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		// Create Additional Benefits Table
		float[] pointColumnWidths4 = { 450, 150, 150 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell abCell1 = new Cell();
		abCell1.add(new Paragraph("Additional Benefits").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell1);
		Cell abCell2 = new Cell();
		abCell2.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell2);
		Cell abCell3 = new Cell();
		abCell3.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell3);

		benAddTable.startNewRow();

		Cell abCell4 = new Cell(0, 3);
		abCell4.add(new Paragraph("Main Life").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell4);

		benAddTable.startNewRow();

		// checking main life benefits having or not
		for (QuoBenf quoBenf : benefitsLife) {

			if (quoBenf.getBenfName() != null) {

				Cell abCell5 = new Cell();
				abCell5.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell5);
				Cell abCell6 = new Cell();
				abCell6.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell6);
				Cell abCell7 = new Cell();
				abCell7.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell7);

				benAddTable.startNewRow();

			} else {

			}

		}

		benAddTable.startNewRow();

		Cell abCell8 = new Cell(0, 3);
		abCell8.add(new Paragraph("Spouse").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell8);

		benAddTable.startNewRow();

		// checking spouse having benefits
		for (QuoBenf quoBenf : benefitsSpouse) {

			if (quoBenf.getBenfName() != null) {

				Cell abCell9 = new Cell();
				abCell9.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell9);
				Cell abCell10 = new Cell();
				abCell10.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell10);
				Cell abCell11 = new Cell();
				abCell11.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell11);

				benAddTable.startNewRow();
			} else {

			}

		}

		benAddTable.startNewRow();

		Cell abCell12 = new Cell(0, 3);
		abCell12.add(new Paragraph("Children").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell12);

		benAddTable.startNewRow();

		for (QuoChildBenef quoChild : benefitsChild) {

			if (quoChild.getChild().getChildName() != null) {

				Cell abCell13 = new Cell(0, 3);
				abCell13.add(new Paragraph("Name : " + quoChild.getChild().getChildName() + " Relationship : "
						+ quoChild.getChild().getChildRelation()).setFontSize(9).setBold()
								.setTextAlignment(TextAlignment.LEFT).setCharacterSpacing(1));
				benAddTable.addCell(abCell13);
				benAddTable.startNewRow();

				// loop again to get children benf
				for (QuoChildBenef quoChildBenef : benefitsChild) {
					Cell abCell14 = new Cell();
					abCell14.add(new Paragraph(quoChildBenef.getBenfs().get(0).getBenfName()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT));
					benAddTable.addCell(abCell14);
					Cell abCell15 = new Cell();
					abCell15.add(new Paragraph(Double.toString(quoChildBenef.getBenfs().get(0).getRiderSum()))
							.setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCell15);
					Cell abCell16 = new Cell();
					abCell16.add(new Paragraph(Double.toString(quoChild.getBenfs().get(0).getPremium())).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCell16);
					benAddTable.startNewRow();
				}

			} else {

			}

		}

		document.add(benAddTable);

		document.add(new Paragraph(""));

		document.add(new Paragraph("Special Notes").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));

		document.add(new Paragraph(""));

		// Creating a Special Notes List
		List list = new List(ListNumberingType.DECIMAL);
		list.setFontSize(10);
		ListItem item1 = new ListItem();
		item1.add(new Paragraph(
				"If HRB / SUHRB is obtained, the total cover will be applicable for the whole family per policy year.")
						.setFontSize(10).setFixedLeading(2));
		list.add(item1);

		ListItem item2 = new ListItem();
		item2.add(
				new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.")
						.setFontSize(10).setFixedLeading(2));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date issue.")
				.setFontSize(10).setFixedLeading(2));
		list.add(item3);

		ListItem item4 = new ListItem();
		item4.add(new Paragraph("All amounts are in Sri Lankan Rupees(LKR).").setFontSize(10).setFixedLeading(2));
		list.add(item4);

		ListItem item5 = new ListItem();
		item5.add(new Paragraph("Initial policy processing fee of Rs. 300 (Payable only with initial deposit).")
				.setFontSize(10).setFixedLeading(2));
		list.add(item5);

		ListItem item6 = new ListItem();
		item6.add(new Paragraph(
				"In event of death by accident both Accident Cover and Natural Death Cover will be applicable.")
						.setFontSize(10).setFixedLeading(2));
		list.add(item6);

		document.add(list);

		document.add(new Paragraph("\n"));
		document.add(new Paragraph("This is a system generated report and therefore does not require a signature.")
				.setFontSize(7).setBold());

		document.close();

		return baos.toByteArray();
	}

	@Override
	public byte[] createASFPReport(QuotationDetails quotationDetails, QuotationView quotationView,
			QuoCustomer quoCustomer) throws Exception {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date quoCreateDate = quotationDetails.getQuotationquotationCreateDate();
		String date = dateFormat.format(quoCreateDate);

		ArrayList<QuoBenf> benefitsLife = quotationView.getMainLifeBenf();
		ArrayList<QuoBenf> benefitsSpouse = quotationView.getSpouseBenf();
		ArrayList<QuoChildBenef> benefitsChild = quotationView.getChildBenf();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);
		document.setTopMargin(5);

		// logo
		Paragraph pLogo = new Paragraph();
		Image logo = new Image(ImageDataFactory.create(IMG));
		logo.setHeight(100);
		logo.setWidth(120);
		logo.setFixedPosition(460, 720);
		pLogo.add(logo);
		document.add(pLogo);

		document.add(new Paragraph("Arpico Insurance PLC Quotation").setBold().setFontSize(14)
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));

		document.add(new Paragraph(" "));

		// Agent Details
		float[] pointColumnWidths1 = { 70, 200 };
		Table agtTable = new Table(pointColumnWidths1);
		agtTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell agCell1 = new Cell();
		agCell1.setBorder(Border.NO_BORDER);
		agCell1.add(new Paragraph("Date").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell1);
		Cell agcell2 = new Cell();
		agcell2.setBorder(Border.NO_BORDER);
		agcell2.add(
				new Paragraph(": " + date).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell2);

		agtTable.startNewRow();

		Cell agCell3 = new Cell();
		agCell3.setBorder(Border.NO_BORDER);
		agCell3.add(
				new Paragraph("Branch Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell3);
		Cell agcell4 = new Cell();
		agcell4.setBorder(Border.NO_BORDER);
		agcell4.add(new Paragraph(quotationDetails.getQuotation().getUser().getBranch().getBranch_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getBranch().getBranch_Name()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell4);

		agtTable.startNewRow();

		Cell agCell5 = new Cell();
		agCell5.setBorder(Border.NO_BORDER);
		agCell5.add(
				new Paragraph("Agent Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell5);
		Cell agcell6 = new Cell();
		agcell6.setBorder(Border.NO_BORDER);
		agcell6.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Name()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell6);

		agtTable.startNewRow();

		Cell agcell7 = new Cell();
		agcell7.setBorder(Border.NO_BORDER);
		agcell7.add(
				new Paragraph("Agent Code").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell7);
		Cell agcell8 = new Cell();
		agcell8.setBorder(Border.NO_BORDER);
		agcell8.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Code() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Code()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell8);

		agtTable.startNewRow();

		Cell agcell9 = new Cell();
		agcell9.setBorder(Border.NO_BORDER);
		agcell9.add(
				new Paragraph("Contact No").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell9);

		Cell agCell10 = new Cell();
		agCell10.setBorder(Border.NO_BORDER);
		agCell10.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Mobile() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Mobile()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell10);

		document.add(agtTable);

		document.add(new Paragraph("ARPICO SCHOOL FEES PLAN").setFontSize(10).setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph("TOTAL PROTECTION OF CHILD SCHOOL FEE OR MONEY BACK GURANTEE").setFontSize(10)
				.setUnderline().setCharacterSpacing(1).setFixedLeading(1));

		document.add(new Paragraph("\n"));

		// customer Details
		float[] pointColumnWidths2 = { 150, 200 };
		Table cusTable = new Table(pointColumnWidths2);
		cusTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell cucell1 = new Cell();
		cucell1.setBorder(Border.NO_BORDER);
		cucell1.add(new Paragraph("Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cucell1);
		Cell cuCell2 = new Cell();
		cuCell2.setBorder(Border.NO_BORDER);
		cuCell2.add(new Paragraph(quotationDetails.getCustomerDetails().getCustName() != null
				? ": " + quotationDetails.getCustomerDetails().getCustName()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCell2);

		cusTable.startNewRow();

		Cell cucell3 = new Cell();
		cucell3.setBorder(Border.NO_BORDER);
		cucell3.add(new Paragraph("Age Next Birthday").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		cusTable.addCell(cucell3);
		Cell cuCell4 = new Cell();
		cuCell4.setBorder(Border.NO_BORDER);
		if (quoCustomer.getMainLifeAge() != null) {
			cuCell4.add(new Paragraph(": " + quoCustomer.getMainLifeAge()).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCell4.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCell4);

		cusTable.startNewRow();

		Cell cucell5 = new Cell();
		cucell5.setBorder(Border.NO_BORDER);
		cucell5.add(
				new Paragraph("Occupation").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cucell5);
		Cell cuCell6 = new Cell();
		cuCell6.setBorder(Border.NO_BORDER);
		cuCell6.add(new Paragraph(quotationDetails.getCustomerDetails().getOccupation().getOcupationName() != null
				? ": " + quotationDetails.getCustomerDetails().getOccupation().getOcupationName()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCell6);

		cusTable.startNewRow();

		Cell cucelleempty = new Cell(0, 2);
		cucelleempty.setBorder(Border.NO_BORDER);
		cucelleempty.add(new Paragraph(" ").setFixedLeading(10));
		cusTable.addCell(cucelleempty);

		cusTable.startNewRow();

		Cell cucelleempty1 = new Cell(0, 2);
		cucelleempty1.setBorder(Border.NO_BORDER);
		cucelleempty1.add(new Paragraph(" ").setFixedLeading(10));
		cusTable.addCell(cucelleempty1);

		cusTable.startNewRow();

		// checking Spouse is active or not
		if ((quoCustomer.getSpouseName()) != null) {

			Cell cucell7 = new Cell();
			cucell7.setBorder(Border.NO_BORDER);
			cucell7.add(new Paragraph("Name Of Spouse").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cucell7);
			Cell cuCell8 = new Cell();
			cuCell8.setBorder(Border.NO_BORDER);
			cuCell8.add(new Paragraph(": " + quoCustomer.getSpouseName()).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCell8);

		} else {

		}

		cusTable.startNewRow();

		if ((quoCustomer.getSpouseAge()) != null) {

			Cell cucell9 = new Cell();
			cucell9.setBorder(Border.NO_BORDER);
			cucell9.add(new Paragraph("Age Next Birthday (Spouse)").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cucell9);
			Cell cuCell10 = new Cell();
			cuCell10.setBorder(Border.NO_BORDER);
			cuCell10.add(new Paragraph(": " + Integer.toString(quoCustomer.getSpouseAge())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCell10);

		} else {

		}

		cusTable.startNewRow();

		if ((quoCustomer.getSpouseOccupation()) != null) {

			Cell cucell11 = new Cell();
			cucell11.setBorder(Border.NO_BORDER);
			cucell11.add(new Paragraph("Occupation (Spouse)").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cucell11);
			Cell cuCell12 = new Cell();
			cuCell12.setBorder(Border.NO_BORDER);
			cuCell12.add(new Paragraph(": " + quoCustomer.getSpouseOccupation()).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCell12);

		} else {

		}

		document.add(cusTable);

		// creating Quotation Details Tables
		float[] pointColumnWidths5 = { 80, 150 };
		Table DtlTable = new Table(pointColumnWidths5);
		DtlTable.setFixedPosition(350, 620, 230);

		Cell dlcell1 = new Cell();
		dlcell1.setBorder(Border.NO_BORDER);
		dlcell1.add(new Paragraph("Term").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell1);
		Cell dlcell2 = new Cell();
		dlcell2.setBorder(Border.NO_BORDER);
		if (quotationDetails.getPolTerm() != null) {
			dlcell2.add(new Paragraph(": " + Integer.toString(quotationDetails.getPolTerm())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell2.add(new Paragraph(": ").setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		DtlTable.addCell(dlcell2);

		DtlTable.startNewRow();

		Cell dlcell3 = new Cell();
		dlcell3.setBorder(Border.NO_BORDER);
		dlcell3.add(new Paragraph("Mode").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell3);
		Cell dlcell4 = new Cell();
		dlcell4.setBorder(Border.NO_BORDER);

		if (quotationDetails.getPayMode() != null) {
			if (quotationDetails.getPayMode().equalsIgnoreCase("M")) {
				dlcell4.add(new Paragraph(": Monthly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("Y")) {
				dlcell4.add(new Paragraph(": Yearly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("Q")) {
				dlcell4.add(new Paragraph(": Quarterly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("H")) {
				dlcell4.add(new Paragraph(": Half Yearly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("S")) {
				dlcell4.add(new Paragraph(": Single Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		}
		DtlTable.addCell(dlcell4);

		DtlTable.startNewRow();

		Cell dlcell5 = new Cell();
		dlcell5.setBorder(Border.NO_BORDER);
		dlcell5.add(
				new Paragraph("Mode Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell5);
		Cell dlcell6 = new Cell();
		dlcell6.setBorder(Border.NO_BORDER);
		if (quoCustomer.getModePremium() != null) {
			dlcell6.add(new Paragraph(": " + Double.toString(quoCustomer.getModePremium())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell6.add(new Paragraph("").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell6);

		document.add(DtlTable);

		document.add(new Paragraph(""));

		document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		// Create Additional Benefits Table
		float[] pointColumnWidths4 = { 450, 150, 150 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell abCell1 = new Cell();
		abCell1.add(new Paragraph("Additional Benefits").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell1);
		Cell abCell2 = new Cell();
		abCell2.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell2);
		Cell abCell3 = new Cell();
		abCell3.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell3);

		benAddTable.startNewRow();

		Cell abCell4 = new Cell(0, 3);
		abCell4.add(new Paragraph("Main Life").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell4);

		benAddTable.startNewRow();

		// checking main life benefits having or not
		for (QuoBenf quoBenf : benefitsLife) {

			if (quoBenf.getBenfName() != null) {

				Cell abCell5 = new Cell();
				abCell5.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell5);
				Cell abCell6 = new Cell();
				abCell6.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell6);
				Cell abCell7 = new Cell();
				abCell7.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell7);

				benAddTable.startNewRow();

			} else {

			}

		}

		benAddTable.startNewRow();

		Cell abCell8 = new Cell(0, 3);
		abCell8.add(new Paragraph("Spouse").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell8);

		benAddTable.startNewRow();

		// checking spouse having benefits
		for (QuoBenf quoBenf : benefitsSpouse) {

			if (quoBenf.getBenfName() != null) {

				Cell abCell9 = new Cell();
				abCell9.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell9);
				Cell abCell10 = new Cell();
				abCell10.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell10);
				Cell abCell11 = new Cell();
				abCell11.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell11);

				benAddTable.startNewRow();
			} else {

			}

		}

		benAddTable.startNewRow();

		Cell abCell12 = new Cell(0, 3);
		abCell12.add(new Paragraph("Children").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell12);

		benAddTable.startNewRow();

		for (QuoChildBenef quoChild : benefitsChild) {

			if (quoChild.getChild().getChildName() != null) {

				Cell abCell13 = new Cell(0, 3);
				abCell13.add(new Paragraph("Name : " + quoChild.getChild().getChildName() + " Relationship : "
						+ quoChild.getChild().getChildRelation()).setFontSize(9).setBold()
								.setTextAlignment(TextAlignment.LEFT).setCharacterSpacing(1));
				benAddTable.addCell(abCell13);
				benAddTable.startNewRow();

				// loop again to get children benf
				for (QuoChildBenef quoChildBenef : benefitsChild) {
					Cell abCell14 = new Cell();
					abCell14.add(new Paragraph(quoChildBenef.getBenfs().get(0).getBenfName()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT));
					benAddTable.addCell(abCell14);
					Cell abCell15 = new Cell();
					abCell15.add(new Paragraph(Double.toString(quoChildBenef.getBenfs().get(0).getRiderSum()))
							.setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCell15);
					Cell abCell16 = new Cell();
					abCell16.add(new Paragraph(Double.toString(quoChild.getBenfs().get(0).getPremium())).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCell16);
					benAddTable.startNewRow();
				}

			} else {

			}

		}

		document.add(benAddTable);

		document.add(new Paragraph(""));
		document.add(new Paragraph(
				"If no claim arises during the policy term on the primary benefit Guranteed maturity value : 0.00")
						.setFontSize(10));
		document.add(new Paragraph(" "));

		document.add(new Paragraph("Special Notes").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));

		document.add(new Paragraph(""));

		// Creating a Special Notes List
		List list = new List(ListNumberingType.DECIMAL);
		list.setFontSize(10);
		ListItem item1 = new ListItem();
		item1.add(new Paragraph(
				"If HRB / SUHRB is obtained, the total cover will be applicable for the whole family per policy year.")
						.setFontSize(10).setFixedLeading(2));
		list.add(item1);

		ListItem item2 = new ListItem();
		item2.add(
				new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.")
						.setFontSize(10).setFixedLeading(2));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date of issue.")
				.setFontSize(10).setFixedLeading(2));
		list.add(item3);

		ListItem item4 = new ListItem();
		item4.add(new Paragraph("All amounts are in Sri Lankan Rupees (LKR).").setFontSize(10).setFixedLeading(2));
		list.add(item4);

		ListItem item5 = new ListItem();
		item5.add(new Paragraph("This is only a Quotation and not an Acceptance of Risk.").setFontSize(10)
				.setFixedLeading(2));
		list.add(item5);

		ListItem item6 = new ListItem();
		item6.add(new Paragraph(
				"In the case of Death of the child and if no claim has been made on the primary benefit during the policy term, Total premium paid up to date on the Primary Benefit (MSFB) will be refundered.")
						.setFontSize(10).setFixedLeading(10).setFixedLeading(10));
		list.add(item6);

		ListItem item7 = new ListItem();
		// item7.setFixedPosition(20, 50, 450);
		item7.add(new Paragraph(
				"If no claim has been made on the primary benefit during the policy term, total premium paid on the Primary Benefit (MSFB) premium will be refundered at the policy expiry date.")
						.setFontSize(10).setFixedLeading(10).setFixedLeading(10));
		list.add(item7);

		document.add(list);

		document.add(new Paragraph("\n"));
		document.add(new Paragraph("This is a system generated report and therefore does not require a signature.")
				.setFontSize(7).setBold());

		document.close();

		return baos.toByteArray();
	}

	@Override
	public byte[] createARPReport(QuotationDetails quotationDetails, QuotationView quotationView,
			QuoCustomer quoCustomer) throws Exception {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date quoCreateDate = quotationDetails.getQuotationquotationCreateDate();
		String date = dateFormat.format(quoCreateDate);

		ArrayList<QuoBenf> benefitsLife = quotationView.getMainLifeBenf();
		ArrayList<QuoBenf> benefitsSpouse = quotationView.getSpouseBenf();
		ArrayList<QuoChildBenef> benefitsChild = quotationView.getChildBenf();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);
		document.setTopMargin(5);

		// logo
		Paragraph pLogo = new Paragraph();
		Image logo = new Image(ImageDataFactory.create(IMG));
		logo.setHeight(100);
		logo.setWidth(120);
		logo.setFixedPosition(460, 720);
		pLogo.add(logo);
		document.add(pLogo);

		document.add(new Paragraph("Arpico Insurance PLC Quotation").setBold().setFontSize(14)
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));

		document.add(new Paragraph(" "));

		// Agent Details
		float[] pointColumnWidths1 = { 70, 200 };
		Table agtTable = new Table(pointColumnWidths1);
		agtTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell agCell1 = new Cell();
		agCell1.setBorder(Border.NO_BORDER);
		agCell1.add(new Paragraph("Date").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell1);
		Cell agcell2 = new Cell();
		agcell2.setBorder(Border.NO_BORDER);
		agcell2.add(
				new Paragraph(": " + date).setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell2);

		agtTable.startNewRow();

		Cell agCell3 = new Cell();
		agCell3.setBorder(Border.NO_BORDER);
		agCell3.add(
				new Paragraph("Branch Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell3);
		Cell agcell4 = new Cell();
		agcell4.setBorder(Border.NO_BORDER);
		agcell4.add(new Paragraph(quotationDetails.getQuotation().getUser().getBranch().getBranch_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getBranch().getBranch_Name()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell4);

		agtTable.startNewRow();

		Cell agCell5 = new Cell();
		agCell5.setBorder(Border.NO_BORDER);
		agCell5.add(
				new Paragraph("Agent Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell5);
		Cell agcell6 = new Cell();
		agcell6.setBorder(Border.NO_BORDER);
		agcell6.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Name()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell6);

		agtTable.startNewRow();

		Cell agcell7 = new Cell();
		agcell7.setBorder(Border.NO_BORDER);
		agcell7.add(
				new Paragraph("Agent Code").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell7);
		Cell agcell8 = new Cell();
		agcell8.setBorder(Border.NO_BORDER);
		agcell8.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Code() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Code()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell8);

		agtTable.startNewRow();

		Cell agcell9 = new Cell();
		agcell9.setBorder(Border.NO_BORDER);
		agcell9.add(
				new Paragraph("Contact No").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell9);

		Cell agCell10 = new Cell();
		agCell10.setBorder(Border.NO_BORDER);
		agCell10.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Mobile() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Mobile()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell10);

		document.add(agtTable);

		document.add(new Paragraph("ARPICO RELIEF PLAN").setFontSize(10).setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		// customer Details
		float[] pointColumnWidths2 = { 150, 200 };
		Table cusTable = new Table(pointColumnWidths2);
		cusTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell cucell1 = new Cell();
		cucell1.setBorder(Border.NO_BORDER);
		cucell1.add(new Paragraph("Name").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cucell1);
		Cell cuCell2 = new Cell();
		cuCell2.setBorder(Border.NO_BORDER);
		cuCell2.add(new Paragraph(quotationDetails.getCustomerDetails().getCustName() != null
				? ": " + quotationDetails.getCustomerDetails().getCustName()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCell2);

		cusTable.startNewRow();

		Cell cucell3 = new Cell();
		cucell3.setBorder(Border.NO_BORDER);
		cucell3.add(new Paragraph("Age Next Birthday").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		cusTable.addCell(cucell3);
		Cell cuCell4 = new Cell();
		cuCell4.setBorder(Border.NO_BORDER);
		if (quoCustomer.getMainLifeAge() != null) {
			cuCell4.add(new Paragraph(": " + quoCustomer.getMainLifeAge()).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCell4.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCell4);

		cusTable.startNewRow();

		Cell cucell5 = new Cell();
		cucell5.setBorder(Border.NO_BORDER);
		cucell5.add(
				new Paragraph("Occupation").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cucell5);
		Cell cuCell6 = new Cell();
		cuCell6.setBorder(Border.NO_BORDER);
		cuCell6.add(new Paragraph(quotationDetails.getCustomerDetails().getOccupation().getOcupationName() != null
				? ": " + quotationDetails.getCustomerDetails().getOccupation().getOcupationName()
				: ": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCell6);

		cusTable.startNewRow();

		Cell cucelleempty = new Cell(0, 2);
		cucelleempty.setBorder(Border.NO_BORDER);
		cucelleempty.add(new Paragraph(" ").setFixedLeading(10));
		cusTable.addCell(cucelleempty);

		cusTable.startNewRow();

		Cell cucelleempty1 = new Cell(0, 2);
		cucelleempty1.setBorder(Border.NO_BORDER);
		cucelleempty1.add(new Paragraph(" ").setFixedLeading(10));
		cusTable.addCell(cucelleempty1);

		cusTable.startNewRow();

		// checking Spouse is active or not
		if ((quoCustomer.getSpouseName()) != null) {

			Cell cucell7 = new Cell();
			cucell7.setBorder(Border.NO_BORDER);
			cucell7.add(new Paragraph("Name Of Spouse").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cucell7);
			Cell cuCell8 = new Cell();
			cuCell8.setBorder(Border.NO_BORDER);
			cuCell8.add(new Paragraph(": " + quoCustomer.getSpouseName()).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCell8);

		} else {

		}

		cusTable.startNewRow();

		if ((quoCustomer.getSpouseAge()) != null) {

			Cell cucell9 = new Cell();
			cucell9.setBorder(Border.NO_BORDER);
			cucell9.add(new Paragraph("Age Next Birthday (Spouse)").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cucell9);
			Cell cuCell10 = new Cell();
			cuCell10.setBorder(Border.NO_BORDER);
			cuCell10.add(new Paragraph(": " + Integer.toString(quoCustomer.getSpouseAge())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCell10);

		} else {

		}

		cusTable.startNewRow();

		if ((quoCustomer.getSpouseOccupation()) != null) {

			Cell cucell11 = new Cell();
			cucell11.setBorder(Border.NO_BORDER);
			cucell11.add(new Paragraph("Occupation (Spouse)").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cucell11);
			Cell cuCell12 = new Cell();
			cuCell12.setBorder(Border.NO_BORDER);
			cuCell12.add(new Paragraph(": " + quoCustomer.getSpouseOccupation()).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCell12);

		} else {

		}

		document.add(cusTable);

		// creating Quotation Details Tables
		float[] pointColumnWidths5 = { 80, 150 };
		Table DtlTable = new Table(pointColumnWidths5);
		DtlTable.setFixedPosition(350, 640, 230);

		Cell dlcell1 = new Cell();
		dlcell1.setBorder(Border.NO_BORDER);
		dlcell1.add(new Paragraph("Term").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell1);
		Cell dlcell2 = new Cell();
		dlcell2.setBorder(Border.NO_BORDER);
		if (quotationDetails.getPolTerm() != null) {
			dlcell2.add(new Paragraph(": " + Integer.toString(quotationDetails.getPolTerm())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell2.add(new Paragraph(": ").setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		DtlTable.addCell(dlcell2);

		DtlTable.startNewRow();

		Cell dlcell3 = new Cell();
		dlcell3.setBorder(Border.NO_BORDER);
		dlcell3.add(
				new Paragraph("Paying Term").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell3);
		Cell dlcell4 = new Cell();
		dlcell4.setBorder(Border.NO_BORDER);
		if (quotationDetails.getPolTerm() != null) {
			dlcell4.add(new Paragraph(": " + (quotationDetails.getPaingTerm())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell4.add(new Paragraph(": ").setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		DtlTable.addCell(dlcell4);

		DtlTable.startNewRow();

		Cell dlcell5 = new Cell();
		dlcell5.setBorder(Border.NO_BORDER);
		dlcell5.add(new Paragraph("Mode").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell5);
		Cell dlcell6 = new Cell();
		dlcell6.setBorder(Border.NO_BORDER);

		if (quotationDetails.getPayMode() != null) {
			if (quotationDetails.getPayMode().equalsIgnoreCase("M")) {
				dlcell6.add(new Paragraph(": Monthly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("Y")) {
				dlcell6.add(new Paragraph(": Yearly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("Q")) {
				dlcell6.add(new Paragraph(": Quarterly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("H")) {
				dlcell6.add(new Paragraph(": Half Yearly Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else if (quotationDetails.getPayMode().equalsIgnoreCase("S")) {
				dlcell6.add(new Paragraph(": Single Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		}
		DtlTable.addCell(dlcell6);

		DtlTable.startNewRow();

		Cell dlcell7 = new Cell();
		dlcell7.setBorder(Border.NO_BORDER);
		dlcell7.add(
				new Paragraph("Mode Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell7);
		Cell dlcell8 = new Cell();
		dlcell8.setBorder(Border.NO_BORDER);
		if (quoCustomer.getModePremium() != null) {
			dlcell8.add(new Paragraph(": " + Double.toString(quoCustomer.getModePremium())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell8.add(new Paragraph("").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell8);

		document.add(DtlTable);

		document.add(new Paragraph(""));

		document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		// Create Additional Benefits Table
		float[] pointColumnWidths4 = { 450, 150, 150 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell abCell1 = new Cell();
		abCell1.add(new Paragraph("Additional Benefits").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell1);
		Cell abCell2 = new Cell();
		abCell2.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell2);
		Cell abCell3 = new Cell();
		abCell3.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell3);

		benAddTable.startNewRow();

		Cell abCell4 = new Cell(0, 3);
		abCell4.add(new Paragraph("Main Life").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell4);

		benAddTable.startNewRow();

		// checking main life benefits having or not
		for (QuoBenf quoBenf : benefitsLife) {

			if (quoBenf.getBenfName() != null) {

				Cell abCell5 = new Cell();
				abCell5.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell5);
				Cell abCell6 = new Cell();
				abCell6.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell6);
				Cell abCell7 = new Cell();
				abCell7.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell7);

				benAddTable.startNewRow();

			} else {

			}

		}

		benAddTable.startNewRow();

		Cell abCell8 = new Cell(0, 3);
		abCell8.add(new Paragraph("Spouse").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell8);

		benAddTable.startNewRow();

		// checking spouse having benefits
		for (QuoBenf quoBenf : benefitsSpouse) {

			if (quoBenf.getBenfName() != null) {

				Cell abCell9 = new Cell();
				abCell9.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell9);
				Cell abCell10 = new Cell();
				abCell10.add(new Paragraph(Double.toString(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell10);
				Cell abCell11 = new Cell();
				abCell11.add(new Paragraph(Double.toString(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell11);

				benAddTable.startNewRow();
			} else {

			}

		}

		benAddTable.startNewRow();

		Cell abCell12 = new Cell(0, 3);
		abCell12.add(new Paragraph("Children").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCell12);

		benAddTable.startNewRow();

		for (QuoChildBenef quoChild : benefitsChild) {

			if (quoChild.getChild().getChildName() != null) {

				Cell abCell13 = new Cell(0, 3);
				abCell13.add(new Paragraph("Name : " + quoChild.getChild().getChildName() + " Relationship : "
						+ quoChild.getChild().getChildRelation()).setFontSize(9).setBold()
								.setTextAlignment(TextAlignment.LEFT).setCharacterSpacing(1));
				benAddTable.addCell(abCell13);
				benAddTable.startNewRow();

				// loop again to get children benf
				for (QuoChildBenef quoChildBenef : benefitsChild) {
					Cell abCell14 = new Cell();
					abCell14.add(new Paragraph(quoChildBenef.getBenfs().get(0).getBenfName()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT));
					benAddTable.addCell(abCell14);
					Cell abCell15 = new Cell();
					abCell15.add(new Paragraph(Double.toString(quoChildBenef.getBenfs().get(0).getRiderSum()))
							.setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCell15);
					Cell abCell16 = new Cell();
					abCell16.add(new Paragraph(Double.toString(quoChild.getBenfs().get(0).getPremium())).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCell16);
					benAddTable.startNewRow();
				}

			} else {

			}

		}

		document.add(benAddTable);

		document.add(new Paragraph("Sum assured increase every year by 2.5%").setFontSize(10));

		// Policy Summary Details
		float[] pointColumnWidths6 = { 40, 100, 100, 100, 100 };
		Table polSmyTable = new Table(pointColumnWidths6);
		polSmyTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell psCell1 = new Cell(2, 0);
		psCell1.add(new Paragraph("Year").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		polSmyTable.addCell(psCell1);
		Cell psCell2 = new Cell(2, 0);
		psCell2.add(new Paragraph("Premium per year for Basic Policy").setFontSize(9).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		polSmyTable.addCell(psCell2);
		Cell psCell3 = new Cell(2, 0);
		psCell3.add(new Paragraph("Total Premium Paid per year").setFontSize(9).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		polSmyTable.addCell(psCell3);
		Cell psCell4 = new Cell(0, 2);
		psCell4.add(new Paragraph("Minimum Life Benefits").setFontSize(9).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		polSmyTable.addCell(psCell4);

		polSmyTable.startNewRow();

		Cell psCell5 = new Cell();
		psCell5.add(new Paragraph("Protection against Natural Death").setFontSize(9).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		polSmyTable.addCell(psCell5);
		Cell psCell6 = new Cell();
		psCell6.add(new Paragraph("Protection against Accident Death").setFontSize(9).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		polSmyTable.addCell(psCell6);

		polSmyTable.startNewRow();

		Cell psCell7 = new Cell();
		psCell7.add(new Paragraph("1").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		polSmyTable.addCell(psCell7);
		Cell psCell8 = new Cell();
		psCell8.add(new Paragraph("47,808.00").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		polSmyTable.addCell(psCell8);
		Cell psCell9 = new Cell();
		psCell9.add(new Paragraph("96,852.00").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		polSmyTable.addCell(psCell9);
		Cell psCell10 = new Cell();
		psCell10.add(new Paragraph("506,250.00").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		polSmyTable.addCell(psCell10);
		Cell psCell11 = new Cell();
		psCell11.add(new Paragraph("1,506,250.00").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		polSmyTable.addCell(psCell11);

		document.add(polSmyTable);

		document.add(new Paragraph(""));
		document.add(new Paragraph("Guranteed Maturity : 343,750.00").setFontSize(10));
		document.add(new Paragraph(" "));

		document.add(new Paragraph("Special Notes").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));

		document.add(new Paragraph(""));

		// Creating a Special Notes List
		List list = new List(ListNumberingType.DECIMAL);
		list.setFontSize(10);
		ListItem item1 = new ListItem();
		item1.add(new Paragraph(
				"If HRB / SUHRB is obtained, the total cover will be applicable for the whole family per policy year.")
						.setFontSize(10).setFixedLeading(2));
		list.add(item1);

		ListItem item2 = new ListItem();
		item2.add(
				new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.")
						.setFontSize(10).setFixedLeading(2));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date of issue.")
				.setFontSize(10).setFixedLeading(2));
		list.add(item3);

		ListItem item4 = new ListItem();
		item4.add(new Paragraph("All amounts are in Sri Lankan Rupees (LKR).").setFontSize(10).setFixedLeading(2));
		list.add(item4);

		document.add(list);

		document.add(new Paragraph("\n"));
		document.add(new Paragraph("This is a system generated report and therefore does not require a signature.")
				.setFontSize(7).setBold());

		document.close();

		return baos.toByteArray();
	}

}
