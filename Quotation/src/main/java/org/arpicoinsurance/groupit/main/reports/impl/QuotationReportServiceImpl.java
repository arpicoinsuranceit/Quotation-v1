package org.arpicoinsurance.groupit.main.reports.impl;

import java.io.ByteArrayOutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.arpicoinsurance.groupit.main.dao.SheduleDao;
import org.arpicoinsurance.groupit.main.dao.custom.MedicalRequirementsDaoCustom;
import org.arpicoinsurance.groupit.main.helper.MedicalRequirementsHelper;
import org.arpicoinsurance.groupit.main.helper.QuoBenf;
import org.arpicoinsurance.groupit.main.helper.QuoChildBenef;
import org.arpicoinsurance.groupit.main.helper.QuoCustomer;
import org.arpicoinsurance.groupit.main.helper.QuotationView;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.model.Shedule;
import org.arpicoinsurance.groupit.main.reports.QuotationReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
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

	public static final String FONT = "./src/main/resources/Reports/FONTDIR/Times_New_Romance.ttf";

	// getting schedule details object
	@Autowired
	private SheduleDao sheduleDao;

	// getting medical requirements
	@Autowired
	private MedicalRequirementsDaoCustom medicalRequirementsDaoCustom;

	// paymode switch case variable
	String modeMethod;

	// Creating thousand Separater object
	DecimalFormat formatter = new DecimalFormat("###,###");

	// Creating Arpico Insurance Plus Report
	@Override
	public byte[] createAIPReport(QuotationDetails quotationDetails, QuotationView quotationView,
			QuoCustomer quoCustomer) throws Exception {

		// pdf save type output
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);
		document.setTopMargin(5);

		// mainlife benefits array
		ArrayList<QuoBenf> benefitsLife = quotationView.getMainLifeBenf();

		// Date format display
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date quoCreateDate = quotationDetails.getQuotationquotationCreateDate();
		String date = dateFormat.format(quoCreateDate);

		// getting current year
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(quotationDetails.getQuotationquotationCreateDate());

		// Setting Rounding mode
		formatter.setRoundingMode(RoundingMode.UP);

		String mainLifeOcc = quotationDetails.getCustomerDetails().getOccupation().getOcupationName();
		String mainLifeOccupation = "";

		// converting occupation to camel case
		if (mainLifeOcc != null) {

			char firstChar = mainLifeOcc.charAt(0);
			char firstCharToUpperCase = Character.toUpperCase(firstChar);
			mainLifeOccupation = mainLifeOccupation + firstCharToUpperCase;
			for (int i = 1; i < mainLifeOcc.length(); i++) {
				char currentChar = mainLifeOcc.charAt(i);
				char previousChar = mainLifeOcc.charAt(i - 1);
				if (previousChar == ' ') {
					char currentCharToUpperCase = Character.toUpperCase(currentChar);
					mainLifeOccupation = mainLifeOccupation + currentCharToUpperCase;
				} else {
					char currentCharToLowerCase = Character.toLowerCase(currentChar);
					mainLifeOccupation = mainLifeOccupation + currentCharToLowerCase;
				}
			}
		} else {

		}

		/*
		 * Setting new Font PdfFont font = PdfFontFactory.createFont(FONT,
		 * PdfEncodings.IDENTITY_H); Paragraph p = new
		 * Paragraph("Arpico Insurance PLC Quotation").setFont(font); document.add(p);
		 */

		// header
		document.add(new Paragraph("Arpico Insurance PLC Quotation").setBold().setFontSize(14)
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		// empty line
		document.add(new Paragraph(""));

		// Agent Details
		// width of coloumns
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
		agcell8.add(new Paragraph(quotationDetails.getQuotation().getUser().getUserCode() != null
				? ": " + quotationDetails.getQuotation().getUser().getUserCode()
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

		// adding agent table to document
		document.add(agtTable);

		document.add(new Paragraph(""));
		// sub header
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
		cuCell6.add(new Paragraph(mainLifeOccupation != null ? ": " + mainLifeOccupation : ": ").setFontSize(10)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCell6);
		cusTable.startNewRow();
		// adding customer details table to document
		document.add(cusTable);

		// creating Quotation Details Tables
		float[] pointColumnWidths5 = { 80, 150 };
		Table DtlTable = new Table(pointColumnWidths5);
		DtlTable.setFixedPosition(400, 620, 230);// top bottom width

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

			switch (quotationDetails.getPayMode()) {
			case "M":
				modeMethod = "Monthly";
				break;
			case "Y":
				modeMethod = "Yearly";
				break;
			case "Q":
				modeMethod = "Quartrly";
				break;
			case "H":
				modeMethod = "Half Yearly";
				break;
			case "S":
				modeMethod = "Single";
				break;
			default:
				modeMethod = " ";
				break;

			}
			dlcell4.add(new Paragraph(": " + modeMethod).setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));

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
			dlcell6.add(new Paragraph(": " + formatter.format(quoCustomer.getModePremium())).setFontSize(10)
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
			dlcell8.add(new Paragraph(": " + formatter.format(quotationDetails.getPolicyFee())).setFontSize(10)
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
			dlcell10.add(new Paragraph(": " + formatter.format(quoCustomer.getTotPremium())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell10.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell10);

		document.add(DtlTable);

		document.add(new Paragraph("\n"));
		document.add(new Paragraph(" "));

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

		if (benefitsLife != null) {

			for (QuoBenf quoBenf : benefitsLife) {

				Cell cell3 = new Cell();
				cell3.add(new Paragraph(quoBenf.getBenfName() != null ? quoBenf.getBenfName() : "").setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT));
				benLivTable.addCell(cell3);
				Cell cell4 = new Cell();
				cell4.setHeight(12);
				if (quoBenf.getRiderSum() != null) {
					cell4.add(new Paragraph(formatter.format(quoBenf.getRiderSum())).setFontSize(9)
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
				.setFontSize(8).setBold());

		document.close();

		return baos.toByteArray();
	}

	// test for committ
	public static String FONT1 = "./src/main/resources/Reports/FONTDIR/Times_New_Romance.ttf";

	// creating Arpico Investment Bond Report
	@Override
	public byte[] createAIBReport(QuotationDetails quotationDetails, QuotationView quotationView,
			QuoCustomer quoCustomer) throws Exception {

		String mainLifeOcc = quotationDetails.getCustomerDetails().getOccupation().getOcupationName();
		String mainLifeOccupation = "";

		// converting occupation to camel case
		if (mainLifeOcc != null) {
			char firstChar = mainLifeOcc.charAt(0);
			char firstCharToUpperCase = Character.toUpperCase(firstChar);
			mainLifeOccupation = mainLifeOccupation + firstCharToUpperCase;
			for (int i = 1; i < mainLifeOcc.length(); i++) {
				char currentChar = mainLifeOcc.charAt(i);
				char previousChar = mainLifeOcc.charAt(i - 1);
				if (previousChar == ' ') {
					char currentCharToUpperCase = Character.toUpperCase(currentChar);
					mainLifeOccupation = mainLifeOccupation + currentCharToUpperCase;
				} else {
					char currentCharToLowerCase = Character.toLowerCase(currentChar);
					mainLifeOccupation = mainLifeOccupation + currentCharToLowerCase;
				}
			}
		}

		// Setting Rounding Mode
		formatter.setRoundingMode(RoundingMode.UP);

		// setting date format
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date quoCreateDate = quotationDetails.getQuotationquotationCreateDate();
		String date = dateFormat.format(quoCreateDate);

		// gettinf main life benefits
		ArrayList<QuoBenf> benefitsLife = quotationView.getMainLifeBenf();

		// file output
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);
		document.setTopMargin(5);

		// header
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
		agcell8.add(new Paragraph(quotationDetails.getQuotation().getUser().getUserCode() != null
				? ": " + quotationDetails.getQuotation().getUser().getUserCode()
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

		document.add(new Paragraph(""));
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
		cuCell6.add(new Paragraph(mainLifeOccupation != null ? ": " + mainLifeOccupation : ": ").setFontSize(10)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCell6);

		document.add(cusTable);

		// creating Quotation Details Tables
		float[] pointColumnWidths5 = { 80, 150 };
		Table DtlTable = new Table(pointColumnWidths5);
		DtlTable.setFixedPosition(400, 620, 230);

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

			switch (quotationDetails.getPayMode()) {

			case "M":
				modeMethod = "Monthly";
				break;
			case "Y":
				modeMethod = "Yearly";
				break;
			case "Q":
				modeMethod = "Quartrly";
				break;
			case "H":
				modeMethod = "Half Yearly";
				break;
			case "S":
				modeMethod = "Single";
				break;
			default:
				modeMethod = " ";
				break;
			}

			dlcell4.add(new Paragraph(": " + modeMethod).setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));

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
			dlcell6.add(new Paragraph(": " + formatter.format(quoCustomer.getModePremium())).setFontSize(10)
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
			dlcell8.add(new Paragraph(": " + formatter.format(quotationDetails.getPolicyFee())).setFontSize(10)
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
			dlcell10.add(new Paragraph(": " + formatter.format(quoCustomer.getTotPremium())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell10.add(new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell10);

		document.add(DtlTable);

		document.add(new Paragraph("\n"));
		document.add(new Paragraph(""));

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

		if (benefitsLife != null) {

			for (QuoBenf quoBenf : benefitsLife) {

				Cell cell3 = new Cell();
				cell3.add(new Paragraph(quoBenf.getBenfName() != null ? quoBenf.getBenfName() : "").setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT));
				benLivTable.addCell(cell3);
				Cell cell4 = new Cell();
				cell4.setHeight(12);
				if (quoBenf.getRiderSum() != null) {
					cell4.add(new Paragraph(formatter.format(quoBenf.getRiderSum())).setFontSize(9)
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
				.setFontSize(8).setBold());

		document.close();

		return baos.toByteArray();
	}

	// Create Arpico Investment Plan Report
	@Override
	public byte[] createINVPReport(QuotationDetails quotationDetails, QuotationView quotationView,
			QuoCustomer quoCustomer) throws Exception {

		// getting current year
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(quotationDetails.getQuotationquotationCreateDate());

		String mainLifeOcc = quotationDetails.getCustomerDetails().getOccupation().getOcupationName();
		String mainLifeOccupation = "";

		String spouseOcc = quoCustomer.getSpouseOccupation();
		String spouseOccupation = "";

		// converting occupation to camel case
		if (mainLifeOcc != null) {

			char firstChar = mainLifeOcc.charAt(0);
			char firstCharToUpperCase = Character.toUpperCase(firstChar);
			mainLifeOccupation = mainLifeOccupation + firstCharToUpperCase;
			for (int i = 1; i < mainLifeOcc.length(); i++) {
				char currentChar = mainLifeOcc.charAt(i);
				char previousChar = mainLifeOcc.charAt(i - 1);
				if (previousChar == ' ') {
					char currentCharToUpperCase = Character.toUpperCase(currentChar);
					mainLifeOccupation = mainLifeOccupation + currentCharToUpperCase;
				} else {
					char currentCharToLowerCase = Character.toLowerCase(currentChar);
					mainLifeOccupation = mainLifeOccupation + currentCharToLowerCase;
				}
			}
		}

		if (spouseOcc != null) {

			char firstChar = spouseOcc.charAt(0);
			char firstCharToUpperCase = Character.toUpperCase(firstChar);
			spouseOccupation = spouseOccupation + firstCharToUpperCase;
			for (int i = 1; i < spouseOcc.length(); i++) {
				char currentChar = spouseOcc.charAt(i);
				char previousChar = spouseOcc.charAt(i - 1);
				if (previousChar == ' ') {
					char currentCharToUpperCase = Character.toUpperCase(currentChar);
					spouseOccupation = spouseOccupation + currentCharToUpperCase;
				} else {
					char currentCharToLowerCase = Character.toLowerCase(currentChar);
					spouseOccupation = spouseOccupation + currentCharToLowerCase;
				}
			}

		}

		// creating Rounding Mode
		formatter.setRoundingMode(RoundingMode.UP);

		// setting date format
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date quoCreateDate = quotationDetails.getQuotationquotationCreateDate();
		String date = dateFormat.format(quoCreateDate);

		// getting main life benifits
		ArrayList<QuoBenf> benefitsLife = quotationView.getMainLifeBenf();
		// gettinf spouse benefits
		ArrayList<QuoBenf> benefitsSpouse = quotationView.getSpouseBenf();
		// getting children benefits
		ArrayList<QuoChildBenef> benefitsChild = quotationView.getChildBenf();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);
		document.setTopMargin(5);

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
		agcell8.add(new Paragraph(quotationDetails.getQuotation().getUser().getUserCode() != null
				? ": " + quotationDetails.getQuotation().getUser().getUserCode()
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

		document.add(new Paragraph(""));

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
		cuCell6.add(new Paragraph(mainLifeOccupation != null ? ": " + mainLifeOccupation : ": ").setFontSize(10)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
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
			cuCell12.add(new Paragraph(": " + spouseOccupation).setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCell12);

		} else {

		}

		document.add(cusTable);

		// creating Quotation Details Tables
		float[] pointColumnWidths5 = { 80, 150 };
		Table DtlTable = new Table(pointColumnWidths5);
		DtlTable.setFixedPosition(400, 650, 230);

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

			switch (quotationDetails.getPayMode()) {
			case "M":
				modeMethod = "Monthly";
				break;
			case "Y":
				modeMethod = "Yearly";
				break;
			case "Q":
				modeMethod = "Quartrly";
				break;
			case "H":
				modeMethod = "Half Yearly";
				break;
			case "S":
				modeMethod = "Single";
				break;
			default:
				modeMethod = " ";
				break;
			}

			dlcell4.add(new Paragraph(": " + modeMethod).setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));

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
			dlcell6.add(new Paragraph(": " + formatter.format(quoCustomer.getModePremium())).setFontSize(10)
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

		// checking main life benefits having or not
		if (benefitsLife.isEmpty()) {

		} else {
			Cell abCell4 = new Cell(0, 3);
			abCell4.add(new Paragraph("Main Life").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(abCell4);

			benAddTable.startNewRow();
		}

		for (QuoBenf quoBenf : benefitsLife) {

			if (benefitsLife != null) {

				Cell abCell5 = new Cell();
				abCell5.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell5);
				Cell abCell6 = new Cell();
				abCell6.add(new Paragraph(formatter.format(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell6);
				Cell abCell7 = new Cell();
				abCell7.add(new Paragraph(formatter.format(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell7);

				benAddTable.startNewRow();

			} else {

			}

		}

		benAddTable.startNewRow();

		// checking spouse having benefits
		if (benefitsSpouse.isEmpty()) {

		} else {
			Cell abCell8 = new Cell(0, 3);
			abCell8.add(new Paragraph("Spouse").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(abCell8);

			benAddTable.startNewRow();
		}

		for (QuoBenf quoBenf : benefitsSpouse) {

			if (benefitsSpouse != null) {

				Cell abCell9 = new Cell();
				abCell9.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell9);
				Cell abCell10 = new Cell();
				abCell10.add(new Paragraph(formatter.format(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell10);
				Cell abCell11 = new Cell();
				abCell11.add(new Paragraph(formatter.format(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell11);

				benAddTable.startNewRow();
			} else {

			}

		}

		benAddTable.startNewRow();

		if (benefitsChild.isEmpty()) {

		} else {
			Cell abCell12 = new Cell(0, 3);
			abCell12.add(new Paragraph("Children").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(abCell12);

			benAddTable.startNewRow();

		}

		for (QuoChildBenef quoChild : benefitsChild) {

			if (benefitsChild != null) {

				// Calculating Child Age
				SimpleDateFormat dobFormat = new SimpleDateFormat("yyyy-MM-dd");
				LocalDate dateOfBirth = LocalDate.parse(dobFormat.format(quoChild.getChild().getChildDob()));
				LocalDate currentDate = LocalDate
						.parse(dobFormat.format(quotationDetails.getQuotationquotationCreateDate()));
				long diffInYears = ChronoUnit.YEARS.between(dateOfBirth, currentDate);
				diffInYears += 1;
				String childAge = Long.toString(diffInYears);

				Cell abCell13 = new Cell(0, 3);
				abCell13.add(
						new Paragraph("Name : " + quoChild.getChild().getChildName() + '\t' + '\t' + "Relationship : "
								+ quoChild.getChild().getChildRelation() + '\t' + '\t' + '\t' + "Age : " + childAge)
										.setFontSize(9).setBold().setTextAlignment(TextAlignment.LEFT)
										.setCharacterSpacing(1));
				benAddTable.addCell(abCell13);
				benAddTable.startNewRow();

				// getting benefits of child
				for (QuoBenf quoChildBenef : quoChild.getBenfs()) {

					Cell abCell14 = new Cell();
					abCell14.add(new Paragraph(quoChildBenef.getBenfName()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT));
					benAddTable.addCell(abCell14);
					Cell abCell15 = new Cell();
					abCell15.add(new Paragraph(formatter.format(quoChildBenef.getRiderSum())).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCell15);
					Cell abCell16 = new Cell();
					abCell16.add(new Paragraph(formatter.format(quoChildBenef.getPremium())).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCell16);
					benAddTable.startNewRow();

				}

			} else {

			}
		}

		document.add(benAddTable);

		try {
			java.util.List<MedicalRequirementsHelper> medicalDetails = medicalRequirementsDaoCustom
					.findByQuoDetail(quotationDetails.getQdId());

			if (medicalDetails.isEmpty()) {

			} else {
				document.add(new Paragraph(""));

				// Medical Requirements Table
				float[] pointColumnWidths6 = { 150, 150, 150 };
				Table medReqTable = new Table(pointColumnWidths6);
				medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

				Cell mrqCell1 = new Cell(0, 3);
				mrqCell1.setBorder(Border.NO_BORDER);
				mrqCell1.add(new Paragraph("Medical Requirements").setBold().setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell1);

				medReqTable.startNewRow();

				Cell mrqEtyCell = new Cell(0, 3);
				mrqEtyCell.setBorder(Border.NO_BORDER);
				medReqTable.addCell(mrqEtyCell);

				Cell mrqCell2 = new Cell();
				mrqCell2.add(new Paragraph("Requirements").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell2);

				Cell mrqCell3 = new Cell();
				mrqCell3.add(new Paragraph("Main Life").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell3);

				Cell mrqCell4 = new Cell();
				mrqCell4.add(new Paragraph("Spouse").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell4);

				medReqTable.startNewRow();
				///////
				for (MedicalRequirementsHelper medicalReq : medicalDetails) {

					Cell mrqCell5 = new Cell();
					mrqCell5.add(new Paragraph(medicalReq.getMedicalReqname()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
					medReqTable.addCell(mrqCell5);

					Cell mrqCell6 = new Cell();
					mrqCell6.add(new Paragraph(medicalReq.getMainStatus()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
					medReqTable.addCell(mrqCell6);

					Cell mrqCell7 = new Cell();
					mrqCell7.add(new Paragraph(medicalReq.getSpouseStatus()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
					medReqTable.addCell(mrqCell7);

					medReqTable.startNewRow();
				}
				document.add(medReqTable);

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		//////

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
		item6.add(new Paragraph(
				"Guranteed minimum dividend rate declared for " + calendar.get(Calendar.YEAR) + " - 7.25%")
						.setFontSize(10).setFixedLeading(2));
		list.add(item6);

		document.add(list);

		document.add(new Paragraph("\n"));
		document.add(new Paragraph("This is a system generated report and therefore does not require a signature.")
				.setFontSize(8).setBold());

		document.close();

		return baos.toByteArray();

	}

	// Creating DTA Report
	@Override
	public byte[] createDTAReport(QuotationDetails quotationDetails, QuotationView quotationView,
			QuoCustomer quoCustomer) throws Exception {

		String mainLifeOcc = quotationDetails.getCustomerDetails().getOccupation().getOcupationName();
		String mainLifeOccupation = "";

		String spouseOcc = quoCustomer.getSpouseOccupation();
		String spouseOccupation = "";

		// converting occupation to camel case
		if (mainLifeOcc != null) {

			char firstChar = mainLifeOcc.charAt(0);
			char firstCharToUpperCase = Character.toUpperCase(firstChar);
			mainLifeOccupation = mainLifeOccupation + firstCharToUpperCase;
			for (int i = 1; i < mainLifeOcc.length(); i++) {
				char currentChar = mainLifeOcc.charAt(i);
				char previousChar = mainLifeOcc.charAt(i - 1);
				if (previousChar == ' ') {
					char currentCharToUpperCase = Character.toUpperCase(currentChar);
					mainLifeOccupation = mainLifeOccupation + currentCharToUpperCase;
				} else {
					char currentCharToLowerCase = Character.toLowerCase(currentChar);
					mainLifeOccupation = mainLifeOccupation + currentCharToLowerCase;
				}
			}
		}

		if (spouseOcc != null) {

			char firstChar = spouseOcc.charAt(0);
			char firstCharToUpperCase = Character.toUpperCase(firstChar);
			spouseOccupation = spouseOccupation + firstCharToUpperCase;
			for (int i = 1; i < spouseOcc.length(); i++) {
				char currentChar = spouseOcc.charAt(i);
				char previousChar = spouseOcc.charAt(i - 1);
				if (previousChar == ' ') {
					char currentCharToUpperCase = Character.toUpperCase(currentChar);
					spouseOccupation = spouseOccupation + currentCharToUpperCase;
				} else {
					char currentCharToLowerCase = Character.toLowerCase(currentChar);
					spouseOccupation = spouseOccupation + currentCharToLowerCase;
				}
			}

		} else {

		}

		// seting rounding mode
		formatter.setRoundingMode(RoundingMode.UP);

		// setting date format
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date quoCreateDate = quotationDetails.getQuotationquotationCreateDate();
		String date = dateFormat.format(quoCreateDate);

		// main life benefits
		ArrayList<QuoBenf> benefitsLife = quotationView.getMainLifeBenf();
		// getting spouse benefits
		ArrayList<QuoBenf> benefitsSpouse = quotationView.getSpouseBenf();

		// shedule object
		java.util.List<Shedule> shedules = sheduleDao.findByQuotationDetails(quotationDetails);

		// bytearray output
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);
		document.setTopMargin(5);

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
		agcell8.add(new Paragraph(quotationDetails.getQuotation().getUser().getUserCode() != null
				? ": " + quotationDetails.getQuotation().getUser().getUserCode()
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

		document.add(new Paragraph(""));

		if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("DTA")) {
			document.add(new Paragraph("ARPICO DECRASING TERM ASSURANCE FOR HOUSING LOAN").setFontSize(10)
					.setUnderline().setCharacterSpacing(1));
		} else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("DTAPL")) {
			document.add(new Paragraph("ARPICO DECRASING TERM ASSURANCE FOR PERSONAL LOAN").setFontSize(10)
					.setUnderline().setCharacterSpacing(1));
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
		cuCell6.add(new Paragraph(mainLifeOccupation != null ? ": " + mainLifeOccupation : ": ").setFontSize(10)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
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
			cuCell12.add(new Paragraph(": " + spouseOccupation).setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCell12);

		} else {

		}

		document.add(cusTable);

		// creating Quotation Details Tables
		float[] pointColumnWidths5 = { 80, 150 };
		Table DtlTable = new Table(pointColumnWidths5);
		DtlTable.setFixedPosition(400, 620, 230);

		Cell dlcell1 = new Cell();
		dlcell1.setBorder(Border.NO_BORDER);
		dlcell1.add(new Paragraph("Interest Rate").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		DtlTable.addCell(dlcell1);
		Cell dlcell2 = new Cell();
		dlcell2.setBorder(Border.NO_BORDER);
		if (quotationDetails.getInterestRate() != null) {
			dlcell2.add(new Paragraph(": " + formatter.format(quotationDetails.getInterestRate())).setFontSize(10)
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
			dlcell4.add(new Paragraph(": " + formatter.format(quotationDetails.getBaseSum())).setFontSize(10)
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
			switch (quotationDetails.getPayMode()) {
			case "M":
				modeMethod = "Monthly";
				break;
			case "Y":
				modeMethod = "Yearly";
				break;
			case "Q":
				modeMethod = "Quartrly";
				break;
			case "H":
				modeMethod = "Half Yearly";
				break;
			case "S":
				modeMethod = "Single";
				break;
			default:
				modeMethod = " ";
				break;
			}

			dlcell8.add(new Paragraph(": " + modeMethod).setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));

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
			dlcell10.add(new Paragraph(": " + formatter.format(quoCustomer.getModePremium())).setFontSize(10)
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

		////////// Checking main Life
		if (benefitsLife.isEmpty()) {

		} else {
			Cell abCell4 = new Cell(0, 3);
			abCell4.add(new Paragraph("Main Life").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(abCell4);

			benAddTable.startNewRow();

		}

		Cell abCell5 = new Cell();
		abCell5.add(new Paragraph("Basic Sum Assured").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCell5);
		Cell abCell6 = new Cell();
		if (quotationDetails.getBaseSum() != null) {
			abCell6.add(new Paragraph(formatter.format(quotationDetails.getBaseSum())).setFontSize(9)
					.setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCell6.add(new Paragraph("").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		}
		benAddTable.addCell(abCell6);
		Cell abCell7 = new Cell();
		if (quoCustomer.getMode().equalsIgnoreCase("Q")) {
			if (quotationDetails.getPremiumMonth() != null) {
				abCell7.add(new Paragraph(formatter.format(quotationDetails.getPremiumQuater())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
			} else {

			}
		} else if (quoCustomer.getMode().equalsIgnoreCase("M")) {
			if (quotationDetails.getPremiumMonth() != null) {
				abCell7.add(new Paragraph(formatter.format(quotationDetails.getPremiumMonth())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
			} else {

			}
		} else if (quoCustomer.getMode().equalsIgnoreCase("Y")) {
			if (quotationDetails.getPremiumYear() != null) {
				abCell7.add(new Paragraph(formatter.format(quotationDetails.getPremiumYear())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
			} else {

			}
		} else if (quoCustomer.getMode().equalsIgnoreCase("S")) {
			if (quotationDetails.getPremiumSingle() != null) {
				abCell7.add(new Paragraph(formatter.format(quotationDetails.getPremiumSingle())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
			} else {

			}
		} else if (quoCustomer.getMode().equalsIgnoreCase("H")) {
			if (quotationDetails.getPremiumHalf() != null) {
				abCell7.add(new Paragraph(formatter.format(quotationDetails.getPremiumHalf())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
			} else {

			}
		}

		benAddTable.addCell(abCell7);

		benAddTable.startNewRow();

		if (benefitsLife != null) {

			for (QuoBenf quoBenf : benefitsLife) {

				Cell abCell8 = new Cell();
				abCell8.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell8);
				Cell abCell9 = new Cell();
				abCell9.add(new Paragraph(formatter.format(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell9);
				Cell abCell10 = new Cell();
				abCell10.add(new Paragraph(formatter.format(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell10);

				benAddTable.startNewRow();

			}

		}

		benAddTable.startNewRow();

		// checking spouse having benefits
		if (benefitsSpouse.isEmpty()) {

		} else {
			Cell abCell11 = new Cell(0, 3);
			abCell11.add(new Paragraph("Spouse").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(abCell11);

			benAddTable.startNewRow();

		}

		if (benefitsSpouse != null) {

			for (QuoBenf quoBenf : benefitsSpouse) {

				Cell abCell12 = new Cell();
				abCell12.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell12);
				Cell abCell13 = new Cell();
				abCell13.add(new Paragraph(formatter.format(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell13);
				Cell abCell14 = new Cell();
				abCell14.add(new Paragraph(formatter.format(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell14);

				benAddTable.startNewRow();
			}

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
				sdCell5.add(new Paragraph(formatter.format(polShdl.getOutSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
			} else {

			}
			scdTable.addCell(sdCell5);

			Cell sdCell6 = new Cell();
			if (polShdl.getLorned() != null) {
				sdCell6.add(new Paragraph(formatter.format(polShdl.getLorned())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
			} else {

			}
			scdTable.addCell(sdCell6);

			scdTable.startNewRow();
		}

		document.add(scdTable);

		try {
			java.util.List<MedicalRequirementsHelper> medicalDetails = medicalRequirementsDaoCustom
					.findByQuoDetail(quotationDetails.getQdId());

			if (medicalDetails.isEmpty()) {

			} else {
				document.add(new Paragraph(""));

				// Medical Requirements Table
				float[] pointColumnWidths7 = { 150, 150, 150 };
				Table medReqTable = new Table(pointColumnWidths7);
				medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

				Cell mrqCell1 = new Cell(0, 3);
				mrqCell1.setBorder(Border.NO_BORDER);
				mrqCell1.add(new Paragraph("Medical Requirements").setBold().setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell1);

				medReqTable.startNewRow();

				Cell mrqEtyCell = new Cell(0, 3);
				mrqEtyCell.setBorder(Border.NO_BORDER);
				medReqTable.addCell(mrqEtyCell);

				Cell mrqCell2 = new Cell();
				mrqCell2.add(new Paragraph("Requirements").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell2);

				Cell mrqCell3 = new Cell();
				mrqCell3.add(new Paragraph("Main Life").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell3);

				Cell mrqCell4 = new Cell();
				mrqCell4.add(new Paragraph("Spouse").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell4);

				medReqTable.startNewRow();
				///////
				for (MedicalRequirementsHelper medicalReq : medicalDetails) {

					Cell mrqCell5 = new Cell();
					mrqCell5.add(new Paragraph(medicalReq.getMedicalReqname()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
					medReqTable.addCell(mrqCell5);

					Cell mrqCell6 = new Cell();
					mrqCell6.add(new Paragraph(medicalReq.getMainStatus()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
					medReqTable.addCell(mrqCell6);

					Cell mrqCell7 = new Cell();
					mrqCell7.add(new Paragraph(medicalReq.getSpouseStatus()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
					medReqTable.addCell(mrqCell7);

					medReqTable.startNewRow();
				}
				document.add(medReqTable);

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		//////
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
				.setFontSize(8).setBold());

		document.close();
		return baos.toByteArray();
	}

	@Override
	public byte[] createATRMReport(QuotationDetails quotationDetails, QuotationView quotationView,
			QuoCustomer quoCustomer) throws Exception {

		String mainLifeOcc = quotationDetails.getCustomerDetails().getOccupation().getOcupationName();
		String mainLifeOccupation = "";

		String spouseOcc = quoCustomer.getSpouseOccupation();
		String spouseOccupation = "";

		// converting occupation to camel case
		if (mainLifeOcc != null) {

			char firstChar = mainLifeOcc.charAt(0);
			char firstCharToUpperCase = Character.toUpperCase(firstChar);
			mainLifeOccupation = mainLifeOccupation + firstCharToUpperCase;
			for (int i = 1; i < mainLifeOcc.length(); i++) {
				char currentChar = mainLifeOcc.charAt(i);
				char previousChar = mainLifeOcc.charAt(i - 1);
				if (previousChar == ' ') {
					char currentCharToUpperCase = Character.toUpperCase(currentChar);
					mainLifeOccupation = mainLifeOccupation + currentCharToUpperCase;
				} else {
					char currentCharToLowerCase = Character.toLowerCase(currentChar);
					mainLifeOccupation = mainLifeOccupation + currentCharToLowerCase;
				}
			}
		}

		if (spouseOcc != null) {
			char firstChar = spouseOcc.charAt(0);
			char firstCharToUpperCase = Character.toUpperCase(firstChar);
			spouseOccupation = spouseOccupation + firstCharToUpperCase;
			for (int i = 1; i < spouseOcc.length(); i++) {
				char currentChar = spouseOcc.charAt(i);
				char previousChar = spouseOcc.charAt(i - 1);
				if (previousChar == ' ') {
					char currentCharToUpperCase = Character.toUpperCase(currentChar);
					spouseOccupation = spouseOccupation + currentCharToUpperCase;
				} else {
					char currentCharToLowerCase = Character.toLowerCase(currentChar);
					spouseOccupation = spouseOccupation + currentCharToLowerCase;
				}
			}

		} else {

		}

		// Setting Rounding method
		formatter.setRoundingMode(RoundingMode.UP);

		// setting date format
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date quoCreateDate = quotationDetails.getQuotationquotationCreateDate();
		String date = dateFormat.format(quoCreateDate);

		// Mainlife Spouse Children Benefits
		ArrayList<QuoBenf> benefitsLife = quotationView.getMainLifeBenf();
		ArrayList<QuoBenf> benefitsSpouse = quotationView.getSpouseBenf();
		ArrayList<QuoChildBenef> benefitsChild = quotationView.getChildBenf();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);
		document.setTopMargin(5);

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
		agcell8.add(new Paragraph(quotationDetails.getQuotation().getUser().getUserCode() != null
				? ": " + quotationDetails.getQuotation().getUser().getUserCode()
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

		document.add(new Paragraph(""));

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
		cuCell6.add(new Paragraph(mainLifeOccupation != null ? ": " + mainLifeOccupation : ": ").setFontSize(10)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
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
			cuCell12.add(new Paragraph(": " + spouseOccupation).setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCell12);

		} else {

		}

		document.add(cusTable);

		// creating Quotation Details Tables
		float[] pointColumnWidths5 = { 80, 150 };
		Table DtlTable = new Table(pointColumnWidths5);
		DtlTable.setFixedPosition(400, 650, 230);

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

			switch (quotationDetails.getPayMode()) {
			case "M":
				modeMethod = "Monthly";
				break;
			case "Y":
				modeMethod = "Yearly";
				break;
			case "Q":
				modeMethod = "Quartrly";
				break;
			case "H":
				modeMethod = "Half Yearly";
				break;
			case "S":
				modeMethod = "Single";
				break;
			default:
				modeMethod = " ";
				break;
			}

			dlcell4.add(new Paragraph(": " + modeMethod).setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));

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
			dlcell6.add(new Paragraph(": " + formatter.format(quoCustomer.getModePremium())).setFontSize(10)
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

		// checking main life benefits having or not
		if (benefitsLife.isEmpty()) {

		} else {
			Cell abCell4 = new Cell(0, 3);
			abCell4.add(new Paragraph("Main Life").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(abCell4);

			benAddTable.startNewRow();
		}

		if (benefitsLife != null) {

			for (QuoBenf quoBenf : benefitsLife) {

				Cell abCell5 = new Cell();
				abCell5.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell5);
				Cell abCell6 = new Cell();
				abCell6.add(new Paragraph(formatter.format(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell6);
				Cell abCell7 = new Cell();
				abCell7.add(new Paragraph(formatter.format(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell7);

				benAddTable.startNewRow();

			}

		}
		benAddTable.startNewRow();

		// checking spouse having benefits
		if (benefitsLife.isEmpty()) {

		} else {
			Cell abCell8 = new Cell(0, 3);
			abCell8.add(new Paragraph("Spouse").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(abCell8);

			benAddTable.startNewRow();
		}

		if (benefitsSpouse != null) {

			for (QuoBenf quoBenf : benefitsSpouse) {

				Cell abCell9 = new Cell();
				abCell9.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell9);
				Cell abCell10 = new Cell();
				abCell10.add(new Paragraph(formatter.format(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell10);
				Cell abCell11 = new Cell();
				abCell11.add(new Paragraph(formatter.format(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell11);

				benAddTable.startNewRow();
			}

		} else {

		}

		benAddTable.startNewRow();

		if (benefitsChild.isEmpty()) {

		} else {
			Cell abCell12 = new Cell(0, 3);
			abCell12.add(new Paragraph("Children").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(abCell12);

			benAddTable.startNewRow();

		}

		if (benefitsChild != null) {

			for (QuoChildBenef quoChild : benefitsChild) {

				// Calculating Child Age
				SimpleDateFormat dobFormat = new SimpleDateFormat("yyyy-MM-dd");
				LocalDate dateOfBirth = LocalDate.parse(dobFormat.format(quoChild.getChild().getChildDob()));
				LocalDate currentDate = LocalDate
						.parse(dobFormat.format(quotationDetails.getQuotationquotationCreateDate()));
				long diffInYears = ChronoUnit.YEARS.between(dateOfBirth, currentDate);
				diffInYears += 1;
				String childAge = Long.toString(diffInYears);

				Cell abCell13 = new Cell(0, 3);
				abCell13.add(
						new Paragraph("Name : " + quoChild.getChild().getChildName() + '\t' + '\t' + " Relationship : "
								+ quoChild.getChild().getChildRelation() + '\t' + '\t' + '\t' + " Age : " + childAge)
										.setFontSize(9).setBold().setTextAlignment(TextAlignment.LEFT)
										.setCharacterSpacing(1));
				benAddTable.addCell(abCell13);
				benAddTable.startNewRow();

				// getting benefits of child
				for (QuoBenf quoChildBenef : quoChild.getBenfs()) {

					Cell abCell14 = new Cell();
					abCell14.add(new Paragraph(quoChildBenef.getBenfName()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT));
					benAddTable.addCell(abCell14);
					Cell abCell15 = new Cell();
					abCell15.add(new Paragraph(formatter.format(quoChildBenef.getRiderSum())).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCell15);
					Cell abCell16 = new Cell();
					abCell16.add(new Paragraph(formatter.format(quoChildBenef.getPremium())).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCell16);
					benAddTable.startNewRow();

				}

			}
		}
		document.add(benAddTable);

		try {
			java.util.List<MedicalRequirementsHelper> medicalDetails = medicalRequirementsDaoCustom
					.findByQuoDetail(quotationDetails.getQdId());

			if (medicalDetails.isEmpty()) {

			} else {
				document.add(new Paragraph(""));

				// Medical Requirements Table
				float[] pointColumnWidths7 = { 150, 150, 150 };
				Table medReqTable = new Table(pointColumnWidths7);
				medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

				Cell mrqCell1 = new Cell(0, 3);
				mrqCell1.setBorder(Border.NO_BORDER);
				mrqCell1.add(new Paragraph("Medical Requirements").setBold().setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell1);

				medReqTable.startNewRow();

				Cell mrqEtyCell = new Cell(0, 3);
				mrqEtyCell.setBorder(Border.NO_BORDER);
				medReqTable.addCell(mrqEtyCell);

				Cell mrqCell2 = new Cell();
				mrqCell2.add(new Paragraph("Requirements").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell2);

				Cell mrqCell3 = new Cell();
				mrqCell3.add(new Paragraph("Main Life").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell3);

				Cell mrqCell4 = new Cell();
				mrqCell4.add(new Paragraph("Spouse").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell4);

				medReqTable.startNewRow();
				///////
				for (MedicalRequirementsHelper medicalReq : medicalDetails) {

					Cell mrqCell5 = new Cell();
					mrqCell5.add(new Paragraph(medicalReq.getMedicalReqname()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
					medReqTable.addCell(mrqCell5);

					Cell mrqCell6 = new Cell();
					mrqCell6.add(new Paragraph(medicalReq.getMainStatus()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
					medReqTable.addCell(mrqCell6);

					Cell mrqCell7 = new Cell();
					mrqCell7.add(new Paragraph(medicalReq.getSpouseStatus()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
					medReqTable.addCell(mrqCell7);

					medReqTable.startNewRow();
				}
				document.add(medReqTable);

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		//////

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
				.setFontSize(8).setBold());

		document.close();

		return baos.toByteArray();
	}

	@Override
	public byte[] createEND1Report(QuotationDetails quotationDetails, QuotationView quotationView,
			QuoCustomer quoCustomer) throws Exception {

		String mainLifeOcc = quotationDetails.getCustomerDetails().getOccupation().getOcupationName();
		String mainLifeOccupation = "";

		String spouseOcc = quoCustomer.getSpouseOccupation();
		String spouseOccupation = "";

		// converting occupation to camel case
		if (mainLifeOcc != null) {

			char firstChar = mainLifeOcc.charAt(0);
			char firstCharToUpperCase = Character.toUpperCase(firstChar);
			mainLifeOccupation = mainLifeOccupation + firstCharToUpperCase;
			for (int i = 1; i < mainLifeOcc.length(); i++) {
				char currentChar = mainLifeOcc.charAt(i);
				char previousChar = mainLifeOcc.charAt(i - 1);
				if (previousChar == ' ') {
					char currentCharToUpperCase = Character.toUpperCase(currentChar);
					mainLifeOccupation = mainLifeOccupation + currentCharToUpperCase;
				} else {
					char currentCharToLowerCase = Character.toLowerCase(currentChar);
					mainLifeOccupation = mainLifeOccupation + currentCharToLowerCase;
				}
			}
		}

		if (spouseOcc != null) {

			char firstChar = spouseOcc.charAt(0);
			char firstCharToUpperCase = Character.toUpperCase(firstChar);
			spouseOccupation = spouseOccupation + firstCharToUpperCase;
			for (int i = 1; i < spouseOcc.length(); i++) {
				char currentChar = spouseOcc.charAt(i);
				char previousChar = spouseOcc.charAt(i - 1);
				if (previousChar == ' ') {
					char currentCharToUpperCase = Character.toUpperCase(currentChar);
					spouseOccupation = spouseOccupation + currentCharToUpperCase;
				} else {
					char currentCharToLowerCase = Character.toLowerCase(currentChar);
					spouseOccupation = spouseOccupation + currentCharToLowerCase;
				}
			}

		} else {

		}

		formatter.setRoundingMode(RoundingMode.UP);

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
		agcell8.add(new Paragraph(quotationDetails.getQuotation().getUser().getUserCode() != null
				? ": " + quotationDetails.getQuotation().getUser().getUserCode()
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
		document.add(new Paragraph(""));

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
		cuCell6.add(new Paragraph(mainLifeOccupation != null ? ": " + mainLifeOccupation : ": ").setFontSize(10)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
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
			cuCell12.add(new Paragraph(": " + spouseOccupation).setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCell12);

		} else {

		}

		document.add(cusTable);

		// creating Quotation Details Tables
		float[] pointColumnWidths5 = { 80, 150 };
		Table DtlTable = new Table(pointColumnWidths5);
		DtlTable.setFixedPosition(400, 650, 230);

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

			switch (quotationDetails.getPayMode()) {
			case "M":
				modeMethod = "Monthly";
				break;
			case "Y":
				modeMethod = "Yearly";
				break;
			case "Q":
				modeMethod = "Quartrly";
				break;
			case "H":
				modeMethod = "Half Yearly";
				break;
			case "S":
				modeMethod = "Single";
				break;
			default:
				modeMethod = " ";
				break;
			}
			dlcell4.add(new Paragraph(": " + modeMethod).setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));

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
			dlcell6.add(new Paragraph(": " + formatter.format(quoCustomer.getModePremium())).setFontSize(10)
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

		// checking main life benefits having or not
		if (benefitsLife.isEmpty()) {

		} else {

			Cell abCell4 = new Cell(0, 3);
			abCell4.add(new Paragraph("Main Life").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(abCell4);

			benAddTable.startNewRow();

		}

		for (QuoBenf quoBenf : benefitsLife) {

			if (benefitsLife != null) {

				Cell abCell5 = new Cell();
				abCell5.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell5);
				Cell abCell6 = new Cell();
				abCell6.add(new Paragraph(formatter.format(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell6);
				Cell abCell7 = new Cell();
				abCell7.add(new Paragraph(formatter.format(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell7);

				benAddTable.startNewRow();

			}
		}

		benAddTable.startNewRow();

		// checking spouse having benefits
		if (benefitsSpouse.isEmpty()) {

		} else {
			Cell abCell8 = new Cell(0, 3);
			abCell8.add(new Paragraph("Spouse").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(abCell8);

			benAddTable.startNewRow();

		}

		for (QuoBenf quoBenf : benefitsSpouse) {

			if (benefitsSpouse != null) {

				Cell abCell9 = new Cell();
				abCell9.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell9);
				Cell abCell10 = new Cell();
				abCell10.add(new Paragraph(formatter.format(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell10);
				Cell abCell11 = new Cell();
				abCell11.add(new Paragraph(formatter.format(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell11);

				benAddTable.startNewRow();
			}

		}

		benAddTable.startNewRow();

		///////// Checking Child is Active
		if (benefitsChild.isEmpty()) {

		} else {
			Cell abCell12 = new Cell(0, 3);
			abCell12.add(new Paragraph("Children").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(abCell12);

			benAddTable.startNewRow();

		}

		for (QuoChildBenef quoChild : benefitsChild) {

			if (benefitsChild != null) {

				// Calculating Child Age
				SimpleDateFormat dobFormat = new SimpleDateFormat("yyyy-MM-dd");
				LocalDate dateOfBirth = LocalDate.parse(dobFormat.format(quoChild.getChild().getChildDob()));
				LocalDate currentDate = LocalDate
						.parse(dobFormat.format(quotationDetails.getQuotationquotationCreateDate()));
				long diffInYears = ChronoUnit.YEARS.between(dateOfBirth, currentDate);
				diffInYears += 1;
				String childAge = Long.toString(diffInYears);

				Cell abCell13 = new Cell(0, 3);
				abCell13.add(
						new Paragraph("Name : " + quoChild.getChild().getChildName() + '\t' + '\t' + " Relationship : "
								+ quoChild.getChild().getChildRelation() + '\t' + '\t' + '\t' + " Age : " + childAge)
										.setFontSize(9).setBold().setTextAlignment(TextAlignment.LEFT)
										.setCharacterSpacing(1));
				benAddTable.addCell(abCell13);
				benAddTable.startNewRow();

				// loop again to get children benf
				for (QuoBenf quoChildBenef : quoChild.getBenfs()) {
					Cell abCell14 = new Cell();
					abCell14.add(new Paragraph(quoChildBenef.getBenfName()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT));
					benAddTable.addCell(abCell14);
					Cell abCell15 = new Cell();
					abCell15.add(new Paragraph(formatter.format(quoChildBenef.getRiderSum())).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCell15);
					Cell abCell16 = new Cell();
					abCell16.add(new Paragraph(formatter.format(quoChildBenef.getPremium())).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCell16);
					benAddTable.startNewRow();
				}

			}

		}

		document.add(benAddTable);

		try {
			java.util.List<MedicalRequirementsHelper> medicalDetails = medicalRequirementsDaoCustom
					.findByQuoDetail(quotationDetails.getQdId());

			if (medicalDetails.isEmpty()) {

			} else {
				document.add(new Paragraph(""));

				// Medical Requirements Table
				float[] pointColumnWidths7 = { 150, 150, 150 };
				Table medReqTable = new Table(pointColumnWidths7);
				medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

				Cell mrqCell1 = new Cell(0, 3);
				mrqCell1.setBorder(Border.NO_BORDER);
				mrqCell1.add(new Paragraph("Medical Requirements").setBold().setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell1);

				medReqTable.startNewRow();

				Cell mrqEtyCell = new Cell(0, 3);
				mrqEtyCell.setBorder(Border.NO_BORDER);
				medReqTable.addCell(mrqEtyCell);

				Cell mrqCell2 = new Cell();
				mrqCell2.add(new Paragraph("Requirements").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell2);

				Cell mrqCell3 = new Cell();
				mrqCell3.add(new Paragraph("Main Life").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell3);

				Cell mrqCell4 = new Cell();
				mrqCell4.add(new Paragraph("Spouse").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell4);

				medReqTable.startNewRow();
				///////
				for (MedicalRequirementsHelper medicalReq : medicalDetails) {

					Cell mrqCell5 = new Cell();
					mrqCell5.add(new Paragraph(medicalReq.getMedicalReqname()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
					medReqTable.addCell(mrqCell5);

					Cell mrqCell6 = new Cell();
					mrqCell6.add(new Paragraph(medicalReq.getMainStatus()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
					medReqTable.addCell(mrqCell6);

					Cell mrqCell7 = new Cell();
					mrqCell7.add(new Paragraph(medicalReq.getSpouseStatus()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
					medReqTable.addCell(mrqCell7);

					medReqTable.startNewRow();
				}
				document.add(medReqTable);

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		//////
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
				.setFontSize(8).setBold());

		document.close();

		return baos.toByteArray();
	}

	@Override
	public byte[] createASFPReport(QuotationDetails quotationDetails, QuotationView quotationView,
			QuoCustomer quoCustomer) throws Exception {

		String mainLifeOcc = quotationDetails.getCustomerDetails().getOccupation().getOcupationName();
		String mainLifeOccupation = "";

		String spouseOcc = quoCustomer.getSpouseOccupation();
		String spouseOccupation = "";

		// converting occupation to camel case
		if (mainLifeOcc != null) {

			char firstChar = mainLifeOcc.charAt(0);
			char firstCharToUpperCase = Character.toUpperCase(firstChar);
			mainLifeOccupation = mainLifeOccupation + firstCharToUpperCase;
			for (int i = 1; i < mainLifeOcc.length(); i++) {
				char currentChar = mainLifeOcc.charAt(i);
				char previousChar = mainLifeOcc.charAt(i - 1);
				if (previousChar == ' ') {
					char currentCharToUpperCase = Character.toUpperCase(currentChar);
					mainLifeOccupation = mainLifeOccupation + currentCharToUpperCase;
				} else {
					char currentCharToLowerCase = Character.toLowerCase(currentChar);
					mainLifeOccupation = mainLifeOccupation + currentCharToLowerCase;
				}
			}
		}

		if (spouseOcc != null) {

			char firstChar = spouseOcc.charAt(0);
			char firstCharToUpperCase = Character.toUpperCase(firstChar);
			spouseOccupation = spouseOccupation + firstCharToUpperCase;
			for (int i = 1; i < spouseOcc.length(); i++) {
				char currentChar = spouseOcc.charAt(i);
				char previousChar = spouseOcc.charAt(i - 1);
				if (previousChar == ' ') {
					char currentCharToUpperCase = Character.toUpperCase(currentChar);
					spouseOccupation = spouseOccupation + currentCharToUpperCase;
				} else {
					char currentCharToLowerCase = Character.toLowerCase(currentChar);
					spouseOccupation = spouseOccupation + currentCharToLowerCase;
				}
			}
		} else {

		}

		formatter.setRoundingMode(RoundingMode.UP);

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
		agcell8.add(new Paragraph(quotationDetails.getQuotation().getUser().getUserCode() != null
				? ": " + quotationDetails.getQuotation().getUser().getUserCode()
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

		document.add(new Paragraph(""));

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
		cuCell6.add(new Paragraph(mainLifeOccupation != null ? ": " + mainLifeOccupation : ": ").setFontSize(10)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
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
			cuCell12.add(new Paragraph(": " + spouseOccupation).setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCell12);

		} else {

		}

		document.add(cusTable);

		// creating Quotation Details Tables
		float[] pointColumnWidths5 = { 80, 150 };
		Table DtlTable = new Table(pointColumnWidths5);
		DtlTable.setFixedPosition(400, 620, 230);

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

			switch (quotationDetails.getPayMode()) {
			case "M":
				modeMethod = "Monthly";
				break;
			case "Y":
				modeMethod = "Yearly";
				break;
			case "Q":
				modeMethod = "Quartrly";
				break;
			case "H":
				modeMethod = "Half Yearly";
				break;
			case "S":
				modeMethod = "Single";
				break;
			default:
				modeMethod = " ";
				break;
			}

			dlcell4.add(new Paragraph(": " + modeMethod).setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));

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
			dlcell6.add(new Paragraph(": " + formatter.format(quoCustomer.getModePremium())).setFontSize(10)
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

		// checking main life benefits having or not
		if (benefitsLife.isEmpty()) {

		} else {
			Cell abCell4 = new Cell(0, 3);
			abCell4.add(new Paragraph("Main Life").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(abCell4);

			benAddTable.startNewRow();

		}

		if (benefitsLife != null) {
			for (QuoBenf quoBenf : benefitsLife) {

				Cell abCell5 = new Cell();
				abCell5.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell5);
				Cell abCell6 = new Cell();
				abCell6.add(new Paragraph(formatter.format(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell6);
				Cell abCell7 = new Cell();
				abCell7.add(new Paragraph(formatter.format(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell7);

				benAddTable.startNewRow();

			}

		}

		benAddTable.startNewRow();

		// checking spouse having benefits
		if (benefitsSpouse.isEmpty()) {

		} else {
			Cell abCell8 = new Cell(0, 3);
			abCell8.add(new Paragraph("Spouse").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(abCell8);

			benAddTable.startNewRow();

		}

		if (benefitsSpouse != null) {

			for (QuoBenf quoBenf : benefitsSpouse) {

				Cell abCell9 = new Cell();
				abCell9.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell9);
				Cell abCell10 = new Cell();
				abCell10.add(new Paragraph(formatter.format(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell10);
				Cell abCell11 = new Cell();
				abCell11.add(new Paragraph(formatter.format(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell11);

				benAddTable.startNewRow();
			}
		}

		benAddTable.startNewRow();

		if (benefitsChild.isEmpty()) {

		} else {
			Cell abCell12 = new Cell(0, 3);
			abCell12.add(new Paragraph("Children").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(abCell12);

			benAddTable.startNewRow();

		}

		if (benefitsChild != null) {

			for (QuoChildBenef quoChild : benefitsChild) {

				// Calculating Child Age
				SimpleDateFormat dobFormat = new SimpleDateFormat("yyyy-MM-dd");
				LocalDate dateOfBirth = LocalDate.parse(dobFormat.format(quoChild.getChild().getChildDob()));
				LocalDate currentDate = LocalDate
						.parse(dobFormat.format(quotationDetails.getQuotationquotationCreateDate()));
				long diffInYears = ChronoUnit.YEARS.between(dateOfBirth, currentDate);
				diffInYears += 1;
				String childAge = Long.toString(diffInYears);

				Cell abCell13 = new Cell(0, 3);
				abCell13.add(
						new Paragraph("Name : " + quoChild.getChild().getChildName() + '\t' + '\t' + " Relationship : "
								+ quoChild.getChild().getChildRelation() + '\t' + '\t' + '\t' + " Age : " + childAge)
										.setFontSize(9).setBold().setTextAlignment(TextAlignment.LEFT)
										.setCharacterSpacing(1));
				benAddTable.addCell(abCell13);
				benAddTable.startNewRow();

				// loop again to get children benf
				for (QuoBenf quoChildBenef : quoChild.getBenfs()) {
					Cell abCell14 = new Cell();
					abCell14.add(new Paragraph(quoChildBenef.getBenfName()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT));
					benAddTable.addCell(abCell14);
					Cell abCell15 = new Cell();
					abCell15.add(new Paragraph(formatter.format(quoChildBenef.getRiderSum())).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCell15);
					Cell abCell16 = new Cell();
					abCell16.add(new Paragraph(formatter.format(quoChildBenef.getPremium())).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCell16);
					benAddTable.startNewRow();
				}

			}
		}

		document.add(benAddTable);

		document.add(new Paragraph(""));
		document.add(new Paragraph(
				"If no claim arises during the policy term on the primary benefit Guranteed maturity value : 0.00")
						.setFontSize(10));
		document.add(new Paragraph(" "));

		try {
			java.util.List<MedicalRequirementsHelper> medicalDetails = medicalRequirementsDaoCustom
					.findByQuoDetail(quotationDetails.getQdId());

			if (medicalDetails.isEmpty()) {

			} else {
				document.add(new Paragraph(""));

				// Medical Requirements Table
				float[] pointColumnWidths7 = { 150, 150, 150 };
				Table medReqTable = new Table(pointColumnWidths7);
				medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

				Cell mrqCell1 = new Cell(0, 3);
				mrqCell1.setBorder(Border.NO_BORDER);
				mrqCell1.add(new Paragraph("Medical Requirements").setBold().setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell1);

				medReqTable.startNewRow();

				Cell mrqEtyCell = new Cell(0, 3);
				mrqEtyCell.setBorder(Border.NO_BORDER);
				medReqTable.addCell(mrqEtyCell);

				Cell mrqCell2 = new Cell();
				mrqCell2.add(new Paragraph("Requirements").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell2);

				Cell mrqCell3 = new Cell();
				mrqCell3.add(new Paragraph("Main Life").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell3);

				Cell mrqCell4 = new Cell();
				mrqCell4.add(new Paragraph("Spouse").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell4);

				medReqTable.startNewRow();
				///////
				for (MedicalRequirementsHelper medicalReq : medicalDetails) {

					Cell mrqCell5 = new Cell();
					mrqCell5.add(new Paragraph(medicalReq.getMedicalReqname()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
					medReqTable.addCell(mrqCell5);

					Cell mrqCell6 = new Cell();
					mrqCell6.add(new Paragraph(medicalReq.getMainStatus()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
					medReqTable.addCell(mrqCell6);

					Cell mrqCell7 = new Cell();
					mrqCell7.add(new Paragraph(medicalReq.getSpouseStatus()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
					medReqTable.addCell(mrqCell7);

					medReqTable.startNewRow();
				}
				document.add(medReqTable);

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		//////

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
				.setFontSize(8).setBold());

		document.close();

		return baos.toByteArray();
	}

	@Override
	public byte[] createARPReport(QuotationDetails quotationDetails, QuotationView quotationView,
			QuoCustomer quoCustomer) throws Exception {

		String mainLifeOcc = quotationDetails.getCustomerDetails().getOccupation().getOcupationName();
		String mainLifeOccupation = "";

		String spouseOcc = quoCustomer.getSpouseOccupation();
		String spouseOccupation = "";

		// converting occupation to camel case
		if (mainLifeOcc != null) {

			char firstChar = mainLifeOcc.charAt(0);
			char firstCharToUpperCase = Character.toUpperCase(firstChar);
			mainLifeOccupation = mainLifeOccupation + firstCharToUpperCase;
			for (int i = 1; i < mainLifeOcc.length(); i++) {
				char currentChar = mainLifeOcc.charAt(i);
				char previousChar = mainLifeOcc.charAt(i - 1);
				if (previousChar == ' ') {
					char currentCharToUpperCase = Character.toUpperCase(currentChar);
					mainLifeOccupation = mainLifeOccupation + currentCharToUpperCase;
				} else {
					char currentCharToLowerCase = Character.toLowerCase(currentChar);
					mainLifeOccupation = mainLifeOccupation + currentCharToLowerCase;
				}
			}
		}

		if (spouseOcc != null) {
			char firstChar = spouseOcc.charAt(0);
			char firstCharToUpperCase = Character.toUpperCase(firstChar);
			spouseOccupation = spouseOccupation + firstCharToUpperCase;
			for (int i = 1; i < spouseOcc.length(); i++) {
				char currentChar = spouseOcc.charAt(i);
				char previousChar = spouseOcc.charAt(i - 1);
				if (previousChar == ' ') {
					char currentCharToUpperCase = Character.toUpperCase(currentChar);
					spouseOccupation = spouseOccupation + currentCharToUpperCase;
				} else {
					char currentCharToLowerCase = Character.toLowerCase(currentChar);
					spouseOccupation = spouseOccupation + currentCharToLowerCase;
				}
			}
		} else {

		}

		formatter.setRoundingMode(RoundingMode.UP);

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
		agcell8.add(new Paragraph(quotationDetails.getQuotation().getUser().getUserCode() != null
				? ": " + quotationDetails.getQuotation().getUser().getUserCode()
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
		document.add(new Paragraph(""));

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
		cuCell6.add(new Paragraph(mainLifeOccupation != null ? ": " + mainLifeOccupation : ": ").setFontSize(10)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
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
			cuCell12.add(new Paragraph(": " + spouseOccupation).setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCell12);

		} else {

		}

		document.add(cusTable);

		// creating Quotation Details Tables
		float[] pointColumnWidths5 = { 80, 150 };
		Table DtlTable = new Table(pointColumnWidths5);
		DtlTable.setFixedPosition(400, 640, 230);

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

			switch (quotationDetails.getPayMode()) {
			case "M":
				modeMethod = "Monthly";
				break;
			case "Y":
				modeMethod = "Yearly";
				break;
			case "Q":
				modeMethod = "Quartrly";
				break;
			case "H":
				modeMethod = "Half Yearly";
				break;
			case "S":
				modeMethod = "Single";
				break;
			default:
				modeMethod = " ";
				break;
			}

			dlcell6.add(new Paragraph(": " + modeMethod).setFontSize(10).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));

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
			dlcell8.add(new Paragraph(": " + formatter.format(quoCustomer.getModePremium())).setFontSize(10)
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

		// checking main life benefits having or not
		if (benefitsLife.isEmpty()) {

		} else {
			Cell abCell4 = new Cell(0, 3);
			abCell4.add(new Paragraph("Main Life").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(abCell4);

			benAddTable.startNewRow();

		}

		if (benefitsLife != null) {

			for (QuoBenf quoBenf : benefitsLife) {

				Cell abCell5 = new Cell();
				abCell5.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell5);
				Cell abCell6 = new Cell();
				abCell6.add(new Paragraph(formatter.format(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell6);
				Cell abCell7 = new Cell();
				abCell7.add(new Paragraph(formatter.format(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell7);

				benAddTable.startNewRow();

			}
		}

		benAddTable.startNewRow();

		// checking spouse having benefits
		if (benefitsSpouse.isEmpty()) {

		} else {
			Cell abCell8 = new Cell(0, 3);
			abCell8.add(new Paragraph("Spouse").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(abCell8);

			benAddTable.startNewRow();

		}

		if (benefitsSpouse != null) {

			for (QuoBenf quoBenf : benefitsSpouse) {

				Cell abCell9 = new Cell();
				abCell9.add(new Paragraph(quoBenf.getBenfName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(abCell9);
				Cell abCell10 = new Cell();
				abCell10.add(new Paragraph(formatter.format(quoBenf.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell10);
				Cell abCell11 = new Cell();
				abCell11.add(new Paragraph(formatter.format(quoBenf.getPremium())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCell11);

				benAddTable.startNewRow();
			}
		}

		benAddTable.startNewRow();

		//// Cheking Children is active
		if (benefitsChild.isEmpty()) {

		} else {
			Cell abCell12 = new Cell(0, 3);
			abCell12.add(new Paragraph("Children").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(abCell12);

			benAddTable.startNewRow();

		}

		if (benefitsChild != null) {

			for (QuoChildBenef quoChild : benefitsChild) {

				SimpleDateFormat dobFormat = new SimpleDateFormat("yyyy-MM-dd");
				LocalDate dateOfBirth = LocalDate.parse(dobFormat.format(quoChild.getChild().getChildDob()));
				LocalDate currentDate = LocalDate
						.parse(dobFormat.format(quotationDetails.getQuotationquotationCreateDate()));
				long diffInYears = ChronoUnit.YEARS.between(dateOfBirth, currentDate);
				diffInYears += 1;
				String childAge = Long.toString(diffInYears);

				Cell abCell13 = new Cell(0, 3);
				abCell13.add(
						new Paragraph("Name : " + quoChild.getChild().getChildName() + '\t' + '\t' + " Relationship : "
								+ quoChild.getChild().getChildRelation() + '\t' + '\t' + " Age : " + childAge)
										.setFontSize(9).setBold().setTextAlignment(TextAlignment.LEFT)
										.setCharacterSpacing(1));
				benAddTable.addCell(abCell13);
				benAddTable.startNewRow();

				// loop again to get children benf
				for (QuoBenf quoChildBenef : quoChild.getBenfs()) {
					Cell abCell14 = new Cell();
					abCell14.add(new Paragraph(quoChildBenef.getBenfName()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT));
					benAddTable.addCell(abCell14);
					Cell abCell15 = new Cell();
					abCell15.add(new Paragraph(formatter.format(quoChildBenef.getRiderSum())).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCell15);
					Cell abCell16 = new Cell();
					abCell16.add(new Paragraph(formatter.format(quoChildBenef.getPremium())).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCell16);
					benAddTable.startNewRow();
				}

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

		try {
			java.util.List<MedicalRequirementsHelper> medicalDetails = medicalRequirementsDaoCustom
					.findByQuoDetail(quotationDetails.getQdId());

			if (medicalDetails.isEmpty()) {

			} else {
				document.add(new Paragraph(""));

				// Medical Requirements Table
				float[] pointColumnWidths7 = { 150, 150, 150 };
				Table medReqTable = new Table(pointColumnWidths7);
				medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

				Cell mrqCell1 = new Cell(0, 3);
				mrqCell1.setBorder(Border.NO_BORDER);
				mrqCell1.add(new Paragraph("Medical Requirements").setBold().setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell1);

				medReqTable.startNewRow();

				Cell mrqEtyCell = new Cell(0, 3);
				mrqEtyCell.setBorder(Border.NO_BORDER);
				medReqTable.addCell(mrqEtyCell);

				Cell mrqCell2 = new Cell();
				mrqCell2.add(new Paragraph("Requirements").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell2);

				Cell mrqCell3 = new Cell();
				mrqCell3.add(new Paragraph("Main Life").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell3);

				Cell mrqCell4 = new Cell();
				mrqCell4.add(new Paragraph("Spouse").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell4);

				medReqTable.startNewRow();
				///////
				for (MedicalRequirementsHelper medicalReq : medicalDetails) {

					Cell mrqCell5 = new Cell();
					mrqCell5.add(new Paragraph(medicalReq.getMedicalReqname()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
					medReqTable.addCell(mrqCell5);

					Cell mrqCell6 = new Cell();
					mrqCell6.add(new Paragraph(medicalReq.getMainStatus()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
					medReqTable.addCell(mrqCell6);

					Cell mrqCell7 = new Cell();
					mrqCell7.add(new Paragraph(medicalReq.getSpouseStatus()).setFontSize(9)
							.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
					medReqTable.addCell(mrqCell7);

					medReqTable.startNewRow();
				}
				document.add(medReqTable);

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		//////

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
				.setFontSize(8).setBold());

		document.close();

		return baos.toByteArray();
	}

}
