package org.arpicoinsurance.groupit.main.reports.impl;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.BenefitsDao;
import org.arpicoinsurance.groupit.main.dao.SheduleDao;
import org.arpicoinsurance.groupit.main.dao.custom.AipPrintSheduleDaoCustom;
import org.arpicoinsurance.groupit.main.dao.custom.MedicalRequirementsDaoCustom;
import org.arpicoinsurance.groupit.main.helper.AipPrintShedule;
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
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
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

	@Autowired
	private MedicalRequirementsDaoCustom medicalRequirementsDaoCustom;

	// AIP Schedule
	@Autowired
	private AipPrintSheduleDaoCustom aipPrintSheduleDaoCustom;

	// To get Benefit Combination
	@Autowired
	private BenefitsDao benefictDao;

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
		document.setTopMargin(150);

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

		// empty line
		document.add(new Paragraph(""));

		// Agent Details
		// width of coloumns
		float[] pointColumnWidths1 = { 90, 200 };
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

		Cell agCellQId = new Cell();
		agCellQId.setBorder(Border.NO_BORDER);
		agCellQId.add(new Paragraph("Quotation No ").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		agtTable.addCell(agCellQId);
		Cell agCellQ = new Cell();
		agCellQ.setBorder(Border.NO_BORDER);
		agCellQ.add(new Paragraph(": " + quotationDetails.getQuotation().getId()).setFontSize(10)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCellQ);

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
		if (quotationDetails.getQuotation().getUser().getUserCode() != null) {
			String code = quotationDetails.getQuotation().getUser().getUserCode();
			Integer val = 0;

			try {
				Integer.parseInt(code);
				val = 1;

			} catch (NumberFormatException e) {
				val = 0;
			}

			if (val == 1) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": " + quotationDetails.getQuotation().getUser().getUserCode())
						.setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else if (val == 0) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": ............................ ").setFontSize(10)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(
						new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			}
		} else {

		}

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
		document.add(new Paragraph("ARPICO INVESTMENT PLUS").setFontSize(10).setCharacterSpacing(1));
		final SolidLine lineDrawer = new SolidLine(1f);
		document.add(new LineSeparator(lineDrawer));
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
		float[] pointColumnWidths5 = { 100, 100 };
		Table DtlTable = new Table(pointColumnWidths5);
		DtlTable.setFixedPosition(400, 490, 230);// top bottom width

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
				new Paragraph("Contribution").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell3);
		Cell dlcell4 = new Cell();
		dlcell4.setBorder(Border.NO_BORDER);
		if (quoCustomer.getModePremium() != null) {
			dlcell4.add(new Paragraph(": " + formatter.format(quoCustomer.getModePremium())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell4.add(new Paragraph("").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell4);

		DtlTable.startNewRow();

		Cell dlcell5 = new Cell();
		dlcell5.setBorder(Border.NO_BORDER);
		dlcell5.add(new Paragraph("Paying Method").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
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
		dlcell7.add(new Paragraph("Admin Fee / Cess").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		DtlTable.addCell(dlcell7);
		Cell dlcell8 = new Cell();
		dlcell8.setBorder(Border.NO_BORDER);
		if (quotationDetails.getPolicyFee() != null) {
			dlcell8.add(new Paragraph(
					": " + formatter.format(quotationDetails.getTaxAmount() + quotationDetails.getAdminFee()))
							.setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

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
		benLivTable.setBorder(new SolidBorder(1));

		Cell cell1 = new Cell();
		cell1.setBorder(new SolidBorder(1));
		cell1.add(new Paragraph("Living Benefits").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benLivTable.addCell(cell1);
		Cell cell2 = new Cell();
		cell2.setBorder(new SolidBorder(1));
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

		document.add(new Paragraph("** Guranteed minimum dividend rate declared for " + calendar.get(Calendar.YEAR))
				.setFontSize(10));
		document.add(new Paragraph(""));

		// Create Additional Benefits Table

		float[] pointColumnWidths4 = { 300, 500 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(new SolidBorder(1));

		Cell abCell1 = new Cell(0, 2);
		abCell1.setBorder(new SolidBorder(1));
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
		/*ListItem item1 = new ListItem();
		item1.add(new Paragraph("This is an indicative quote only and is valid for 30 days from date of issue.")
				.setFontSize(10).setFixedLeading(10));
		list.add(item1);*/

		ListItem item1 = new ListItem();
		item1.add(new Paragraph("This is an indicative quote only and is valid for 31 May 2018.")
				.setFontSize(10).setFixedLeading(10));
		list.add(item1);
		
		ListItem item2 = new ListItem();
		item2.add(new Paragraph("All Amounts are in Sri Lankan Rupees (LKR).").setFontSize(10).setFixedLeading(10));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("Initial policy processing fee of Rs 300 (Payable only with initial deposit).")
				.setFontSize(10).setFixedLeading(10));
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
		float[] pointColumnWidths1 = { 90, 200 };
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

		Cell agCellQId = new Cell();
		agCellQId.setBorder(Border.NO_BORDER);
		agCellQId.add(new Paragraph("Quotation No ").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		agtTable.addCell(agCellQId);
		Cell agCellQ = new Cell();
		agCellQ.setBorder(Border.NO_BORDER);
		agCellQ.add(new Paragraph(": " + quotationDetails.getQuotation().getId()).setFontSize(10)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCellQ);

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
		document.setTopMargin(50);

		// Agent Details
		float[] pointColumnWidths1 = { 90, 150 };
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

		Cell agCellQId = new Cell();
		agCellQId.setBorder(Border.NO_BORDER);
		agCellQId.add(new Paragraph("Quotation No ").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		agtTable.addCell(agCellQId);
		Cell agCellQ = new Cell();
		agCellQ.setBorder(Border.NO_BORDER);
		agCellQ.add(new Paragraph(": " + quotationDetails.getQuotation().getId()).setFontSize(10)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCellQ);

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
		if (quotationDetails.getQuotation().getUser().getUserCode() != null) {
			String code = quotationDetails.getQuotation().getUser().getUserCode();
			Integer val = 0;

			try {
				Integer.parseInt(code);
				val = 1;

			} catch (NumberFormatException e) {
				val = 0;
			}

			if (val == 1) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": " + quotationDetails.getQuotation().getUser().getUserCode())
						.setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else if (val == 0) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": ............................ ").setFontSize(10)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(
						new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			}
		} else {

		}

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
			document.add(new Paragraph("ARPICO INVESTMENT PLAN").setFontSize(10).setCharacterSpacing(1));
		} else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("ASIP")) {
			document.add(new Paragraph("ARPICO SUPER INVESTMENT PLAN").setFontSize(10).setCharacterSpacing(1));
		}

		final SolidLine lineDrawer = new SolidLine(1f);
		document.add(new LineSeparator(lineDrawer));
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
		DtlTable.setFixedPosition(400, 625, 230);

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
				new Paragraph("Mode Premium").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell3);
		Cell dlcell4 = new Cell();
		dlcell4.setBorder(Border.NO_BORDER);
		if (quoCustomer.getModePremium() != null) {
			dlcell4.add(new Paragraph(": " + formatter.format(quoCustomer.getTotPremium())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell4.add(new Paragraph("").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell4);

		Cell dlcell5 = new Cell();
		dlcell5.setBorder(Border.NO_BORDER);
		dlcell5.add(new Paragraph("Paying Method").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
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

		document.add(DtlTable);

		document.add(new Paragraph(""));

		document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		//////////////////////////// TEST OLD
		//////////////////////////// FORMAT//////////////////////////////////////

		// Create Additional Benefits Table
		/* Declaring column sizes of the table respectively */
		float[] pointColumnWidths4 = { 450, 100, 100, 100, 100, 100, 100 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(new SolidBorder(1));

		// table headings of the Living Benefits
		Cell alCellth1 = new Cell(2, 0);
		alCellth1.setBorder(new SolidBorder(1));
		alCellth1.add(new Paragraph("Living Benefits").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellth1);

		Cell alCellth2 = new Cell(0, 2);
		alCellth2.setBorder(new SolidBorder(1));
		alCellth2.add(new Paragraph("Main Life").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellth2);

		Cell alCellth3 = new Cell(0, 2);
		alCellth3.setBorder(new SolidBorder(1));
		alCellth3.add(new Paragraph("Spouse").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellth3);

		Cell alCellth4 = new Cell(0, 2);
		alCellth4.setBorder(new SolidBorder(1));
		alCellth4.add(new Paragraph("Children").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellth4);

		benAddTable.startNewRow();

		Cell alCellMA = new Cell();
		alCellMA.setBorder(new SolidBorder(1));
		alCellMA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellMA);

		Cell alCellMP = new Cell();
		alCellMP.setBorder(new SolidBorder(1));
		alCellMP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellMP);

		Cell alCellSA = new Cell();
		alCellSA.setBorder(new SolidBorder(1));
		alCellSA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellSA);

		Cell alCellSP = new Cell();
		alCellSP.setBorder(new SolidBorder(1));
		alCellSP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellSP);

		Cell alCellCA = new Cell();
		alCellCA.setBorder(new SolidBorder(1));
		alCellCA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellCA);

		Cell alCellCP = new Cell();
		alCellCP.setBorder(new SolidBorder(1));
		alCellCP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellCP);

		/////////////////// End Of the Table Heading ////////////

		benAddTable.startNewRow();

		// Getting a HashMap to an ArayList
		ArrayList<HashMap<String, Object>> benifList = new ArrayList<>();

		// Checking MainLife Having Benefits
		if (benefitsLife.isEmpty()) {

		} else {

			for (QuoBenf quoBenf : benefitsLife) {

				HashMap<String, Object> benefitDetailMap = new HashMap<>();

				/*
				 * Do not Print Basic Sum Assured,ATPB,ADB and FEB Its Printed on Additional
				 * Benefits Table
				 */
				if (!quoBenf.getRiderCode().equalsIgnoreCase("L2") && (!quoBenf.getRiderCode().equalsIgnoreCase("ATPB"))
						&& (!quoBenf.getRiderCode().equalsIgnoreCase("ADB"))
						&& (!quoBenf.getRiderCode().equalsIgnoreCase("FEB"))) {

					// Cheking the Combination of the benefits by passing Rider Code
					if (benefictDao.findByRiderCode(quoBenf.getRiderCode()) != null) {
						benefitDetailMap.put("combination",
								benefictDao.findByRiderCode(quoBenf.getRiderCode()).getBenefictCombination());

						benefitDetailMap.put("benName", quoBenf.getBenfName());
						benefitDetailMap.put("mainAmt", quoBenf.getRiderSum());
						benefitDetailMap.put("mainPre", quoBenf.getPremium());

						benifList.add(benefitDetailMap);
					}

				}

			}

		}

		// checking Spouse Having Benefits
		if (benefitsSpouse.isEmpty()) {

		} else {
			for (QuoBenf quoBenf : benefitsSpouse) {

				if (!quoBenf.getRiderCode().equalsIgnoreCase("ADBS") && (!quoBenf.getRiderCode().equalsIgnoreCase("SCB")
						&& (!quoBenf.getRiderCode().equalsIgnoreCase("FEBS")))) {
					Integer isAvailable = 0;

					for (HashMap<String, Object> benefitDetailMap : benifList) {

						if (benefitDetailMap.get("combination")
								.equals(benefictDao.findByRiderCode(quoBenf.getRiderCode()).getBenefictCombination())) {
							isAvailable = 1;
							benefitDetailMap.put("spouseAmt", quoBenf.getRiderSum());
							benefitDetailMap.put("spousePre", quoBenf.getPremium());
						}

					}

					/*
					 * If doesn't math with main life benefit combination put new benefits of the
					 * spouse to HASHMAP
					 */
					if (isAvailable == 0) {

						HashMap<String, Object> benefitDetailMap = new HashMap<>();

						benefitDetailMap.put("combination",
								benefictDao.findByRiderCode(quoBenf.getRiderCode()).getBenefictCombination());
						benefitDetailMap.put("benName", quoBenf.getBenfName());
						benefitDetailMap.put("spouseAmt", quoBenf.getRiderSum());
						benefitDetailMap.put("spousePre", quoBenf.getPremium());

						benifList.add(benefitDetailMap);
					}

				}

			}

		}

		// Checking Child having Benefits
		if (benefitsChild.isEmpty()) {

		} else {

			/* Declaring variables to Calculate the total of Premium of all Children */
			Double cibc = 0.0;
			Double hbc = 0.0;
			Double hcbic = 0.0;

			for (QuoChildBenef quoChild : benefitsChild) {

				for (QuoBenf quoChildBenef : quoChild.getBenfs()) {

					if (quoChildBenef.getRiderCode().equalsIgnoreCase("CIBC")) {
						cibc = cibc + quoChildBenef.getPremium();
					} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HBC")) {

						hbc = hbc + quoChildBenef.getPremium();
					} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HCBIC")
							|| quoChildBenef.getRiderCode().equalsIgnoreCase("SHCBIC")) {

						hcbic = hcbic + quoChildBenef.getPremium();

					}

					Integer isAvailable = 0;

					for (HashMap<String, Object> benefitDetailMap : benifList) {

						if (benefitDetailMap.get("combination").equals(
								benefictDao.findByRiderCode(quoChildBenef.getRiderCode()).getBenefictCombination())) {
							isAvailable = 1;
							benefitDetailMap.put("childAmt", quoChildBenef.getRiderSum());

							if (quoChildBenef.getRiderCode().equalsIgnoreCase("CIBC")) {
								benefitDetailMap.put("childPre", cibc);

							} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HBC")) {
								benefitDetailMap.put("childPre", hbc);

							} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HCBIC")
									|| quoChildBenef.getRiderCode().equalsIgnoreCase("SHCBIC")) {
								benefitDetailMap.put("childPre", hcbic);

							}
						}

					}

					/*
					 * If benefits combination of the children not in ArrayList put new child
					 * benefits to the HASHMAP
					 */
					if (isAvailable == 0) {

						HashMap<String, Object> benefitDetailMap = new HashMap<>();

						benefitDetailMap.put("combination",
								benefictDao.findByRiderCode(quoChildBenef.getRiderCode()).getBenefictCombination());
						benefitDetailMap.put("benName", quoChildBenef.getBenfName());
						benefitDetailMap.put("childAmt", quoChildBenef.getRiderSum());

						if (quoChildBenef.getRiderCode().equalsIgnoreCase("CIBC")) {
							benefitDetailMap.put("childPre", cibc);

						} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HBC")) {
							benefitDetailMap.put("childPre", hbc);

						} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HCBIC")
								|| quoChildBenef.getRiderCode().equalsIgnoreCase("SHCBIC")) {
							benefitDetailMap.put("childPre", hcbic);

						}

						// Adding Full benefits details Hash Map to ArrayList
						benifList.add(benefitDetailMap);
					}
				}
			}

		}

		// Getting Full Benf HashMap using foreach
		for (HashMap<String, Object> hashMap : benifList) {

			Cell alCellBenf = new Cell();

			// Getting ALL Benefits Name object and cast to an String
			String p = (String) hashMap.get("benName");

			// System.out.println("combinationnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn "
			// +hashMap.get("combination"));
			alCellBenf.add(new Paragraph(p).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
			benAddTable.addCell(alCellBenf);

			//
			Cell alCellmA = new Cell();
			if (hashMap.get("mainAmt") != null) {

				// Getting ALL Benefits Name Combinations object and cast to an String
				String comb = (String) hashMap.get("combination");

				/* If benefit is WPB Print Amount as APPLIED */
				if (comb.equalsIgnoreCase("WPB")) {
					alCellmA.add(new Paragraph("Applied").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmA);

				} else {

					Double mAmt = (Double) hashMap.get("mainAmt");

					alCellmA.add(
							new Paragraph(formatter.format(mAmt)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmA);
				}

				// If Main Amount NULL
			} else {

				alCellmA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellmA);
			}

			Cell alCellmP = new Cell();
			if (hashMap.get("mainPre") != null) {
				Double mPre = (Double) hashMap.get("mainPre");

				alCellmP.add(
						new Paragraph(formatter.format(mPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellmP);
			} else {
				alCellmP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellmP);
			}

			Cell alCellsA = new Cell();

			if (hashMap.get("spouseAmt") != null) {
				String comb = (String) hashMap.get("combination");

				/* If Spouse having WPB or HCBF Spouse Amount will print as APPLIED */
				if (comb.equalsIgnoreCase("WPB") || comb.equalsIgnoreCase("HCBF")) {

					alCellsA.add(new Paragraph("Applied").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellsA);

					// Print Spouse Amount
				} else {
					Double spAmt = (Double) hashMap.get("spouseAmt");

					alCellsA.add(new Paragraph(formatter.format(spAmt)).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellsA);
				}

				// IF spouse Amount is null
			} else {
				alCellsA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsA);
			}

			Cell alCellsP = new Cell();
			if (hashMap.get("spousePre") != null) {
				Double spPre = (Double) hashMap.get("spousePre");

				alCellsP.add(
						new Paragraph(formatter.format(spPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsP);
			} else {
				alCellsP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsP);
			}

			Cell alCellcA = new Cell();
			if (hashMap.get("childAmt") != null) {
				String comb = (String) hashMap.get("combination");

				/* If Children get HCBF Amount Will Print as APPLIED */
				if (comb.equalsIgnoreCase("HCBF")) {

					alCellcA.add(new Paragraph("Applied").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellcA);

					// Display Amount
				} else if (!comb.equalsIgnoreCase("HCBF")) {

					Double cAmt = (Double) hashMap.get("childAmt");

					alCellcA.add(
							new Paragraph(formatter.format(cAmt)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellcA);

				}

				// IF Child Amount is NULL Display
			} else {

				alCellcA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcA);
			}

			Cell alCellcP = new Cell();
			if (hashMap.get("childPre") != null) {

				Double cPre = (Double) hashMap.get("childPre");

				alCellcP.add(
						new Paragraph(formatter.format(cPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcP);

			} else {
				alCellcP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcP);
			}

			benAddTable.startNewRow();

		}

		////////////////////// table headings of the Additional Benefits
		////////////////////// table\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		Cell abCellth1 = new Cell(2, 0);
		abCellth1.setBorderTop(new SolidBorder(2));
		abCellth1.add(new Paragraph("Additional Benefits").setFontSize(9).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		benAddTable.addCell(abCellth1);

		Cell abCellth2 = new Cell(0, 2);
		abCellth2.setBorderTop(new SolidBorder(2));
		abCellth2.add(new Paragraph("Main Life").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellth2);

		Cell abCellth3 = new Cell(0, 2);
		abCellth3.setBorderTop(new SolidBorder(2));
		abCellth3.add(new Paragraph("Spouse").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellth3);

		Cell abCellth4 = new Cell(0, 2);
		abCellth4.setBorderTop(new SolidBorder(2));
		abCellth4.add(new Paragraph("Children").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellth4);

		benAddTable.startNewRow();

		Cell abCellMA = new Cell();
		abCellMA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMA);

		Cell abCellMP = new Cell();
		abCellMP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMP);

		Cell abCellSA = new Cell();
		abCellSA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellSA);

		Cell abCellSP = new Cell();
		abCellSP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellSP);

		Cell abCellCA = new Cell();
		abCellCA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellCA);

		Cell abCellCP = new Cell();
		abCellCP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellCP);

		benAddTable.startNewRow();

		/*
		 * Declaring Variable for Natural Death Cover/Main/Spouse(Amount/Premium)
		 * Accidental Death benefit/Main/Spouse(Amount/Premium) Funeral
		 * Expenses/Main/Spouse(Amount/Premium)
		 */
		Double ndc = 0.0;
		Double ndcp = 0.0;

		Double ndcs = 0.0;
		Double ndcsp = 0.0;

		Double adb = 0.0;
		Double adbp = 0.0;

		Double adbs = 0.0;
		Double adbsp = 0.0;

		Double feb = 0.0;
		Double febp = 0.0;

		Double febs = 0.0;
		Double febsp = 0.0;

		if (benefitsLife.isEmpty()) {

		} else {

			for (QuoBenf quoAddBenf : benefitsLife) {

				if (quoAddBenf.getRiderCode().equalsIgnoreCase("L2")
						|| (quoAddBenf.getRiderCode().equalsIgnoreCase("ATPB"))) {
					/*
					 * Natural Death Cover = Basic Sum Assured + Additional Term Protection Benefit
					 */
					ndc = ndc + quoAddBenf.getRiderSum();
					ndcp = ndcp + quoAddBenf.getPremium();

				}
				if (quoAddBenf.getRiderCode().equalsIgnoreCase("ADB")) {
					// only Accidental Death Benefit
					adb = quoAddBenf.getRiderSum();
					adbp = adbp + quoAddBenf.getPremium();

				}
				if (quoAddBenf.getRiderCode().equalsIgnoreCase("FEB")) {
					// only Funeral Expenses Benefit
					feb = quoAddBenf.getRiderSum();
					febp = febp + quoAddBenf.getPremium();

				}

			}
		}

		// check Spouse having Benefits
		if (benefitsSpouse.isEmpty()) {

		} else {

			for (QuoBenf quoSpoBenf : benefitsSpouse) {

				if (quoSpoBenf.getRiderCode().equalsIgnoreCase("SCB")) {
					// only Spouse Cover Benefit
					ndcs = quoSpoBenf.getRiderSum();
					ndcsp = quoSpoBenf.getPremium();

				}
				if (quoSpoBenf.getRiderCode().equalsIgnoreCase("ADBS")) {
					// only Accidental Death Benefit
					adbs = quoSpoBenf.getRiderSum();
					adbsp = quoSpoBenf.getPremium();

				}
				if (quoSpoBenf.getRiderCode().equalsIgnoreCase("FEBS")) {
					// only Funeral Expenses Benefit
					febs = quoSpoBenf.getRiderSum();
					febsp = quoSpoBenf.getPremium();

				}

			}

		}

		/* Printing All Additional Benefits Data */
		Cell abCelld1 = new Cell();
		abCelld1.add(new Paragraph("Natural Death Cover").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld1);

		Cell abCelld2 = new Cell();
		abCelld2.add(new Paragraph(formatter.format(ndc)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld2);

		Cell abCelld3 = new Cell();
		abCelld3.add(new Paragraph(formatter.format(ndcp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld3);

		Cell abCelld4 = new Cell();
		abCelld4.add(new Paragraph(formatter.format(ndcs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld4);

		Cell abCelld5 = new Cell();
		abCelld5.add(new Paragraph(formatter.format(ndcsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld5);

		Cell abCelld6 = new Cell();
		abCelld6.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld6);

		Cell abCelld7 = new Cell();
		abCelld7.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld7);

		benAddTable.startNewRow();

		Cell abCelld8 = new Cell();
		abCelld8.add(new Paragraph("Accidental Death Benefit").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld8);

		Cell abCelld9 = new Cell();
		abCelld9.add(new Paragraph(formatter.format(adb)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld9);

		Cell abCelld10 = new Cell();
		abCelld10.add(new Paragraph(formatter.format(adbp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld10);

		Cell abCelld11 = new Cell();
		abCelld11.add(new Paragraph(formatter.format(adbs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld11);

		Cell abCelld12 = new Cell();
		abCelld12.add(new Paragraph(formatter.format(adbsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld12);

		Cell abCelld13 = new Cell();
		abCelld13.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld13);

		Cell abCelld14 = new Cell();
		abCelld14.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld14);

		benAddTable.startNewRow();

		Cell abCelld15 = new Cell();
		abCelld15.add(new Paragraph("Funeral Expenses").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld15);

		Cell abCelld16 = new Cell();
		abCelld16.add(new Paragraph(formatter.format(feb)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld16);

		Cell abCelld117 = new Cell();
		abCelld117.add(new Paragraph(formatter.format(febp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld117);

		Cell abCelld18 = new Cell();
		abCelld18.add(new Paragraph(formatter.format(febs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld18);

		Cell abCelld19 = new Cell();
		abCelld19.add(new Paragraph(formatter.format(febsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld19);

		Cell abCelld20 = new Cell();
		abCelld20.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld20);

		Cell abCelld21 = new Cell();
		abCelld21.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld21);

		///////////////////// End Of Additional Benefits Table
		///////////////////// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		document.add(benAddTable);

		/////////////////////////// END OLD
		/////////////////////////// FORMAT///////////////////////////////////////

		// Medical Requirements Table
		try {
			java.util.List<MedicalRequirementsHelper> medicalDetails = medicalRequirementsDaoCustom
					.findByQuoDetail(quotationDetails.getQdId());

			if (medicalDetails.isEmpty()) {

			} else {
				document.add(new Paragraph(""));

				document.add(new Paragraph("Medical Requirements").setBold().setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));

				document.add(new Paragraph(""));

				// Medical Requirements Table
				float[] pointColumnWidths6 = { 150, 100, 100 };
				Table medReqTable = new Table(pointColumnWidths6);
				medReqTable.setBorder(new SolidBorder(1));
				medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

				Cell mrqCell2 = new Cell();
				mrqCell2.setBorder(new SolidBorder(1));
				mrqCell2.add(new Paragraph("Requirements").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell2);

				Cell mrqCell3 = new Cell();
				mrqCell3.setBorder(new SolidBorder(1));
				mrqCell3.add(new Paragraph("Main Life").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell3);

				Cell mrqCell4 = new Cell();
				mrqCell4.setBorder(new SolidBorder(1));
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
						.setFontSize(10).setFixedLeading(10));
		list.add(item1);

		ListItem item2 = new ListItem();
		item2.add(
				new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.")
						.setFontSize(10).setFixedLeading(10));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date issue.")
				.setFontSize(10).setFixedLeading(10));
		list.add(item3);

		ListItem item4 = new ListItem();
		item4.add(new Paragraph("Abbrevations ; Y - Yes, R - Required, NR - Not Required").setFontSize(10)
				.setFixedLeading(10));
		list.add(item4);

		ListItem item5 = new ListItem();
		item5.add(new Paragraph("All amounts are in Sri Lankan Rupees(LKR).").setFontSize(10).setFixedLeading(10));
		list.add(item5);

		ListItem item6 = new ListItem();
		item6.add(new Paragraph("Initial policy processing fee of Rs 300 (Payable only with initial deposit).")
				.setFontSize(10).setFixedLeading(10));
		list.add(item6);

		ListItem item7 = new ListItem();
		item7.add(new Paragraph(
				"In event of death by accident both Accident Cover and Natural Death Cover will be applicable.")
						.setFontSize(10).setFixedLeading(10));
		list.add(item7);

		ListItem item8 = new ListItem();
		item8.add(
				new Paragraph("Guranteed minimum dividend rate declared for " + calendar.get(Calendar.YEAR) + " - 9.0%")
						.setFontSize(10).setFixedLeading(10));
		list.add(item8);

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
		document.setTopMargin(50);

		// Agent Details
		float[] pointColumnWidths1 = { 90, 200 };
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

		Cell agCellQId = new Cell();
		agCellQId.setBorder(Border.NO_BORDER);
		agCellQId.add(new Paragraph("Quotation No ").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		agtTable.addCell(agCellQId);
		Cell agCellQ = new Cell();
		agCellQ.setBorder(Border.NO_BORDER);
		agCellQ.add(new Paragraph(": " + quotationDetails.getQuotation().getId()).setFontSize(10)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCellQ);

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
		if (quotationDetails.getQuotation().getUser().getUserCode() != null) {
			String code = quotationDetails.getQuotation().getUser().getUserCode();
			Integer val = 0;

			try {
				Integer.parseInt(code);
				val = 1;

			} catch (NumberFormatException e) {
				val = 0;
			}

			if (val == 1) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": " + quotationDetails.getQuotation().getUser().getUserCode())
						.setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else if (val == 0) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": ............................ ").setFontSize(10)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(
						new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			}
		} else {

		}

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
					.setCharacterSpacing(1));
		} else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("DTAPL")) {
			document.add(new Paragraph("ARPICO DECRASING TERM ASSURANCE FOR PERSONAL LOAN").setFontSize(10)
					.setCharacterSpacing(1));
		}

		final SolidLine lineDrawer = new SolidLine(1f);
		document.add(new LineSeparator(lineDrawer));

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
		DtlTable.setFixedPosition(400, 600, 230);

		Cell dlcell1 = new Cell();
		dlcell1.setBorder(Border.NO_BORDER);
		dlcell1.add(new Paragraph("Interest Rate ").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
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
		dlcell7.add(new Paragraph("Paying Method").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
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
			dlcell10.add(new Paragraph(": " + formatter.format(quoCustomer.getTotPremium())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell10.add(new Paragraph("").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell10);

		document.add(DtlTable);

		document.add(new Paragraph("\n"));

		if (benefitsLife.size() == 1) {
			// System.out.println("sizeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee 1" );
		} else {

			document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
			document.add(new Paragraph(""));

			//////////////////////////// TEST OLD
			//////////////////////////// FORMAT//////////////////////////////////////

			// Create Additional Benefits Table
			/* Declaring column sizes of the table respectively */
			float[] pointColumnWidths4 = { 500, 120, 120, 120, 120 };
			Table benAddTable = new Table(pointColumnWidths4);
			benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(new SolidBorder(1));

			// table headings of the Living Benefits
			Cell alCellth1 = new Cell(2, 0);
			alCellth1.setBorder(new SolidBorder(1));
			alCellth1.add(new Paragraph("Living Benefits").setFontSize(9).setBold()
					.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
			benAddTable.addCell(alCellth1);

			Cell alCellth2 = new Cell(0, 2);
			alCellth2.setBorder(new SolidBorder(1));
			alCellth2.add(new Paragraph("Main Life").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(alCellth2);

			Cell alCellth3 = new Cell(0, 2);
			alCellth3.setBorder(new SolidBorder(1));
			alCellth3.add(new Paragraph("Spouse").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(alCellth3);

			benAddTable.startNewRow();

			Cell alCellMA = new Cell();
			alCellMA.setBorder(new SolidBorder(1));
			alCellMA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(alCellMA);

			Cell alCellMP = new Cell();
			alCellMP.setBorder(new SolidBorder(1));
			alCellMP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(alCellMP);

			Cell alCellSA = new Cell();
			alCellSA.setBorder(new SolidBorder(1));
			alCellSA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(alCellSA);

			Cell alCellSP = new Cell();
			alCellSP.setBorder(new SolidBorder(1));
			alCellSP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
					.setCharacterSpacing(1));
			benAddTable.addCell(alCellSP);

			/////////////////// End Of the Table Heading ////////////

			benAddTable.startNewRow();

			// Getting a HashMap to an ArayList
			ArrayList<HashMap<String, Object>> benifList = new ArrayList<>();

			// Checking MainLife Having Benefits
			if (benefitsLife.isEmpty()) {

			} else {

				for (QuoBenf quoBenf : benefitsLife) {

					HashMap<String, Object> benefitDetailMap = new HashMap<>();

					/*
					 * Do not Print Basic Sum Assured,ATPB,ADB and FEB Its Printed on Additional
					 * Benefits Table
					 */
					if (!quoBenf.getRiderCode().equalsIgnoreCase("L2")
							&& (!quoBenf.getRiderCode().equalsIgnoreCase("ATPB"))
							&& (!quoBenf.getRiderCode().equalsIgnoreCase("ADB"))
							&& (!quoBenf.getRiderCode().equalsIgnoreCase("FEB"))) {

						// Cheking the Combination of the benefits by passing Rider Code
						if (benefictDao.findByRiderCode(quoBenf.getRiderCode()) != null) {
							benefitDetailMap.put("combination",
									benefictDao.findByRiderCode(quoBenf.getRiderCode()).getBenefictCombination());

							benefitDetailMap.put("benName", quoBenf.getBenfName());
							benefitDetailMap.put("mainAmt", quoBenf.getRiderSum());
							benefitDetailMap.put("mainPre", quoBenf.getPremium());

							benifList.add(benefitDetailMap);
						}

					}

				}

			}

			// checking Spouse Having Benefits
			if (benefitsSpouse.isEmpty()) {

			} else {
				for (QuoBenf quoBenf : benefitsSpouse) {

					if (!quoBenf.getRiderCode().equalsIgnoreCase("ADBS")
							&& (!quoBenf.getRiderCode().equalsIgnoreCase("SCB")
									&& (!quoBenf.getRiderCode().equalsIgnoreCase("FEBS")))) {
						Integer isAvailable = 0;

						for (HashMap<String, Object> benefitDetailMap : benifList) {

							if (benefitDetailMap.get("combination").equals(
									benefictDao.findByRiderCode(quoBenf.getRiderCode()).getBenefictCombination())) {
								isAvailable = 1;
								benefitDetailMap.put("spouseAmt", quoBenf.getRiderSum());
								benefitDetailMap.put("spousePre", quoBenf.getPremium());
							}

						}

						/*
						 * If doesn't math with main life benefit combination put new benefits of the
						 * spouse to HASHMAP
						 */
						if (isAvailable == 0) {

							HashMap<String, Object> benefitDetailMap = new HashMap<>();

							benefitDetailMap.put("combination",
									benefictDao.findByRiderCode(quoBenf.getRiderCode()).getBenefictCombination());
							benefitDetailMap.put("benName", quoBenf.getBenfName());
							benefitDetailMap.put("spouseAmt", quoBenf.getRiderSum());
							benefitDetailMap.put("spousePre", quoBenf.getPremium());

							benifList.add(benefitDetailMap);
						}

					}

				}

			}

			// Getting Full Benf HashMap using foreach
			for (HashMap<String, Object> hashMap : benifList) {

				Cell alCellBenf = new Cell();

				// Getting ALL Benefits Name object and cast to an String
				String p = (String) hashMap.get("benName");

				// System.out.println("combinationnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn "
				// +hashMap.get("combination"));
				alCellBenf.add(new Paragraph(p).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(alCellBenf);

				//
				Cell alCellmA = new Cell();
				if (hashMap.get("mainAmt") != null) {

					// Getting ALL Benefits Name Combinations object and cast to an String
					String comb = (String) hashMap.get("combination");

					/* If benefit is WPB Print Amount as APPLIED */
					if (comb.equalsIgnoreCase("WPB")) {
						alCellmA.add(new Paragraph("Applied").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
						benAddTable.addCell(alCellmA);

					} else {

						Double mAmt = (Double) hashMap.get("mainAmt");

						alCellmA.add(new Paragraph(formatter.format(mAmt)).setFontSize(9)
								.setTextAlignment(TextAlignment.RIGHT));
						benAddTable.addCell(alCellmA);
					}

					// If Main Amount NULL
				} else {

					alCellmA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmA);
				}

				Cell alCellmP = new Cell();
				if (hashMap.get("mainPre") != null) {
					Double mPre = (Double) hashMap.get("mainPre");

					alCellmP.add(
							new Paragraph(formatter.format(mPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmP);
				} else {
					alCellmP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmP);
				}

				Cell alCellsA = new Cell();

				if (hashMap.get("spouseAmt") != null) {
					String comb = (String) hashMap.get("combination");

					/* If Spouse having WPB or HCBF Spouse Amount will print as APPLIED */
					if (comb.equalsIgnoreCase("WPB") || comb.equalsIgnoreCase("HCBF")) {

						alCellsA.add(new Paragraph("Applied").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
						benAddTable.addCell(alCellsA);

						// Print Spouse Amount
					} else {
						Double spAmt = (Double) hashMap.get("spouseAmt");

						alCellsA.add(new Paragraph(formatter.format(spAmt)).setFontSize(9)
								.setTextAlignment(TextAlignment.RIGHT));
						benAddTable.addCell(alCellsA);
					}

					// IF spouse Amount is null
				} else {
					alCellsA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellsA);
				}

				Cell alCellsP = new Cell();
				if (hashMap.get("spousePre") != null) {
					Double spPre = (Double) hashMap.get("spousePre");

					alCellsP.add(new Paragraph(formatter.format(spPre)).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellsP);
				} else {
					alCellsP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellsP);
				}

				benAddTable.startNewRow();

			}

			document.add(benAddTable);

			/////////////////////////// END OLD
			/////////////////////////// FORMAT///////////////////////////////////////

			document.add(new Paragraph(""));

		}

		document.add(new Paragraph("Schedule").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));

		document.add(new Paragraph(""));

		float[] pointColumnWidths6 = { 70, 100, 100 };
		Table scdTable = new Table(pointColumnWidths6);
		scdTable.setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(new SolidBorder(1));

		Cell sdCell1 = new Cell();
		sdCell1.setBorder(new SolidBorder(1));
		sdCell1.add(new Paragraph("Year").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		scdTable.addCell(sdCell1);
		Cell sdCell2 = new Cell();
		sdCell2.setBorder(new SolidBorder(1));
		sdCell2.add(new Paragraph("Loan Balance").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		scdTable.addCell(sdCell2);
		Cell sdCell3 = new Cell();
		sdCell3.setBorder(new SolidBorder(1));
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

		// Medical Requirements
		try {
			java.util.List<MedicalRequirementsHelper> medicalDetails = medicalRequirementsDaoCustom
					.findByQuoDetail(quotationDetails.getQdId());

			if (medicalDetails.isEmpty()) {

			} else {
				document.add(new Paragraph(""));

				document.add(new Paragraph("Medical Requirements").setBold().setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));

				document.add(new Paragraph(""));

				// Medical Requirements Table
				float[] pointColumnWidths7 = { 150, 100, 100 };
				Table medReqTable = new Table(pointColumnWidths7);
				medReqTable.setBorder(new SolidBorder(1));
				medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

				Cell mrqCell2 = new Cell();
				mrqCell2.setBorder(new SolidBorder(1));
				mrqCell2.add(new Paragraph("Requirements").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell2);

				Cell mrqCell3 = new Cell();
				mrqCell3.setBorder(new SolidBorder(1));
				mrqCell3.add(new Paragraph("Main Life").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell3);

				Cell mrqCell4 = new Cell();
				mrqCell4.setBorder(new SolidBorder(1));
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
						.setFontSize(10).setFixedLeading(10));
		list.add(item1);

		ListItem item2 = new ListItem();
		item2.add(
				new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.")
						.setFontSize(10).setFixedLeading(10));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date issue.")
				.setFontSize(10).setFixedLeading(10));
		list.add(item3);

		ListItem item4 = new ListItem();
		item4.add(new Paragraph("Abbrevations ; Y - Yes, R - Required, NR - Not Required").setFontSize(10)
				.setFixedLeading(10));
		list.add(item4);

		ListItem item5 = new ListItem();
		item5.add(new Paragraph("All amounts are in Sri Lankan Rupees(LKR).").setFontSize(10).setFixedLeading(10));
		list.add(item5);

		ListItem item6 = new ListItem();
		item6.add(new Paragraph("Initial policy processing fee of Rs.450 (Payable only with initial deposit).")
				.setFontSize(10).setFixedLeading(10));
		list.add(item6);

		ListItem item7 = new ListItem();
		item7.add(new Paragraph(
				"In event of death by accident both Accident Cover and Natural Death Cover will be applicable.")
						.setFontSize(10).setFixedLeading(10));
		list.add(item7);

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
		document.setTopMargin(50);

		// Agent Details
		float[] pointColumnWidths1 = { 90, 200 };
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

		Cell agCellQId = new Cell();
		agCellQId.setBorder(Border.NO_BORDER);
		agCellQId.add(new Paragraph("Quotation No ").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		agtTable.addCell(agCellQId);
		Cell agCellQ = new Cell();
		agCellQ.setBorder(Border.NO_BORDER);
		agCellQ.add(new Paragraph(": " + quotationDetails.getQuotation().getId()).setFontSize(10)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCellQ);

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
		if (quotationDetails.getQuotation().getUser().getUserCode() != null) {
			String code = quotationDetails.getQuotation().getUser().getUserCode();
			Integer val = 0;

			try {
				Integer.parseInt(code);
				val = 1;

			} catch (NumberFormatException e) {
				val = 0;
			}

			if (val == 1) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": " + quotationDetails.getQuotation().getUser().getUserCode())
						.setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else if (val == 0) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": ............................ ").setFontSize(10)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(
						new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			}
		} else {

		}

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

		document.add(new Paragraph("ARPICO TERM PLAN").setFontSize(10).setCharacterSpacing(1));

		final SolidLine lineDrawer = new SolidLine(1f);
		document.add(new LineSeparator(lineDrawer));
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
		DtlTable.setFixedPosition(400, 625, 230);

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
		dlcell3.add(new Paragraph("Paying Method").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
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
			dlcell6.add(new Paragraph(": " + formatter.format(quoCustomer.getTotPremium())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell6.add(new Paragraph("").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell6);

		document.add(DtlTable);

		document.add(new Paragraph(""));

		document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		//////////////////////////// TEST OLD
		//////////////////////////// FORMAT//////////////////////////////////////

		// Create Additional Benefits Table
		/* Declaring column sizes of the table respectively */
		float[] pointColumnWidths4 = { 450, 100, 100, 100, 100, 100, 100 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(new SolidBorder(1));

		// table headings of the Living Benefits
		Cell alCellth1 = new Cell(2, 0);
		alCellth1.setBorder(new SolidBorder(1));
		alCellth1.add(new Paragraph("Living Benefits").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellth1);

		Cell alCellth2 = new Cell(0, 2);
		alCellth2.setBorder(new SolidBorder(1));
		alCellth2.add(new Paragraph("Main Life").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellth2);

		Cell alCellth3 = new Cell(0, 2);
		alCellth3.setBorder(new SolidBorder(1));
		alCellth3.add(new Paragraph("Spouse").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellth3);

		Cell alCellth4 = new Cell(0, 2);
		alCellth4.setBorder(new SolidBorder(1));
		alCellth4.add(new Paragraph("Children").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellth4);

		benAddTable.startNewRow();

		Cell alCellMA = new Cell();
		alCellMA.setBorder(new SolidBorder(1));
		alCellMA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellMA);

		Cell alCellMP = new Cell();
		alCellMP.setBorder(new SolidBorder(1));
		alCellMP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellMP);

		Cell alCellSA = new Cell();
		alCellSA.setBorder(new SolidBorder(1));
		alCellSA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellSA);

		Cell alCellSP = new Cell();
		alCellSP.setBorder(new SolidBorder(1));
		alCellSP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellSP);

		Cell alCellCA = new Cell();
		alCellCA.setBorder(new SolidBorder(1));
		alCellCA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellCA);

		Cell alCellCP = new Cell();
		alCellCP.setBorder(new SolidBorder(1));
		alCellCP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellCP);

		/////////////////// End Of the Table Heading ////////////

		benAddTable.startNewRow();

		// Getting a HashMap to an ArayList
		ArrayList<HashMap<String, Object>> benifList = new ArrayList<>();

		// Checking MainLife Having Benefits
		if (benefitsLife.isEmpty()) {

		} else {

			for (QuoBenf quoBenf : benefitsLife) {

				HashMap<String, Object> benefitDetailMap = new HashMap<>();

				/*
				 * Do not Print Basic Sum Assured,ATPB,ADB and FEB Its Printed on Additional
				 * Benefits Table
				 */
				if (!quoBenf.getRiderCode().equalsIgnoreCase("L2") && (!quoBenf.getRiderCode().equalsIgnoreCase("ATPB"))
						&& (!quoBenf.getRiderCode().equalsIgnoreCase("ADB"))
						&& (!quoBenf.getRiderCode().equalsIgnoreCase("FEB"))) {

					// Cheking the Combination of the benefits by passing Rider Code
					if (benefictDao.findByRiderCode(quoBenf.getRiderCode()) != null) {
						benefitDetailMap.put("combination",
								benefictDao.findByRiderCode(quoBenf.getRiderCode()).getBenefictCombination());

						benefitDetailMap.put("benName", quoBenf.getBenfName());
						benefitDetailMap.put("mainAmt", quoBenf.getRiderSum());
						benefitDetailMap.put("mainPre", quoBenf.getPremium());

						benifList.add(benefitDetailMap);
					}

				}

			}

		}

		// checking Spouse Having Benefits
		if (benefitsSpouse.isEmpty()) {

		} else {
			for (QuoBenf quoBenf : benefitsSpouse) {

				if (!quoBenf.getRiderCode().equalsIgnoreCase("ADBS") && (!quoBenf.getRiderCode().equalsIgnoreCase("SCB")
						&& (!quoBenf.getRiderCode().equalsIgnoreCase("FEBS")))) {
					Integer isAvailable = 0;

					for (HashMap<String, Object> benefitDetailMap : benifList) {

						if (benefitDetailMap.get("combination")
								.equals(benefictDao.findByRiderCode(quoBenf.getRiderCode()).getBenefictCombination())) {
							isAvailable = 1;
							benefitDetailMap.put("spouseAmt", quoBenf.getRiderSum());
							benefitDetailMap.put("spousePre", quoBenf.getPremium());
						}

					}

					/*
					 * If doesn't math with main life benefit combination put new benefits of the
					 * spouse to HASHMAP
					 */
					if (isAvailable == 0) {

						HashMap<String, Object> benefitDetailMap = new HashMap<>();

						benefitDetailMap.put("combination",
								benefictDao.findByRiderCode(quoBenf.getRiderCode()).getBenefictCombination());
						benefitDetailMap.put("benName", quoBenf.getBenfName());
						benefitDetailMap.put("spouseAmt", quoBenf.getRiderSum());
						benefitDetailMap.put("spousePre", quoBenf.getPremium());

						benifList.add(benefitDetailMap);
					}

				}

			}

		}

		// Checking Child having Benefits
		if (benefitsChild.isEmpty()) {

		} else {

			/* Declaring variables to Calculate the total of Premium of all Children */
			Double cibc = 0.0;
			Double hbc = 0.0;
			Double hcbic = 0.0;

			for (QuoChildBenef quoChild : benefitsChild) {

				for (QuoBenf quoChildBenef : quoChild.getBenfs()) {

					if (quoChildBenef.getRiderCode().equalsIgnoreCase("CIBC")) {
						cibc = cibc + quoChildBenef.getPremium();
					} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HBC")) {

						hbc = hbc + quoChildBenef.getPremium();
					} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HCBIC")
							|| quoChildBenef.getRiderCode().equalsIgnoreCase("SHCBIC")) {

						hcbic = hcbic + quoChildBenef.getPremium();

					}

					Integer isAvailable = 0;

					for (HashMap<String, Object> benefitDetailMap : benifList) {

						if (benefitDetailMap.get("combination").equals(
								benefictDao.findByRiderCode(quoChildBenef.getRiderCode()).getBenefictCombination())) {
							isAvailable = 1;
							benefitDetailMap.put("childAmt", quoChildBenef.getRiderSum());

							if (quoChildBenef.getRiderCode().equalsIgnoreCase("CIBC")) {
								benefitDetailMap.put("childPre", cibc);

							} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HBC")) {
								benefitDetailMap.put("childPre", hbc);

							} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HCBIC")
									|| quoChildBenef.getRiderCode().equalsIgnoreCase("SHCBIC")) {
								benefitDetailMap.put("childPre", hcbic);

							}
						}

					}

					/*
					 * If benefits combination of the children not in ArrayList put new child
					 * benefits to the HASHMAP
					 */
					if (isAvailable == 0) {

						HashMap<String, Object> benefitDetailMap = new HashMap<>();

						benefitDetailMap.put("combination",
								benefictDao.findByRiderCode(quoChildBenef.getRiderCode()).getBenefictCombination());
						benefitDetailMap.put("benName", quoChildBenef.getBenfName());
						benefitDetailMap.put("childAmt", quoChildBenef.getRiderSum());

						if (quoChildBenef.getRiderCode().equalsIgnoreCase("CIBC")) {
							benefitDetailMap.put("childPre", cibc);

						} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HBC")) {
							benefitDetailMap.put("childPre", hbc);

						} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HCBIC")
								|| quoChildBenef.getRiderCode().equalsIgnoreCase("SHCBIC")) {
							benefitDetailMap.put("childPre", hcbic);

						}

						// Adding Full benefits details Hash Map to ArrayList
						benifList.add(benefitDetailMap);
					}
				}
			}

		}

		// Getting Full Benf HashMap using foreach
		for (HashMap<String, Object> hashMap : benifList) {

			Cell alCellBenf = new Cell();

			// Getting ALL Benefits Name object and cast to an String
			String p = (String) hashMap.get("benName");

			// System.out.println("combinationnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn "
			// +hashMap.get("combination"));
			alCellBenf.add(new Paragraph(p).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
			benAddTable.addCell(alCellBenf);

			//
			Cell alCellmA = new Cell();
			if (hashMap.get("mainAmt") != null) {

				// Getting ALL Benefits Name Combinations object and cast to an String
				String comb = (String) hashMap.get("combination");

				/* If benefit is WPB Print Amount as APPLIED */
				if (comb.equalsIgnoreCase("WPB")) {
					alCellmA.add(new Paragraph("Applied").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmA);

				} else {

					Double mAmt = (Double) hashMap.get("mainAmt");

					alCellmA.add(
							new Paragraph(formatter.format(mAmt)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmA);
				}

				// If Main Amount NULL
			} else {

				alCellmA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellmA);
			}

			Cell alCellmP = new Cell();
			if (hashMap.get("mainPre") != null) {
				Double mPre = (Double) hashMap.get("mainPre");

				alCellmP.add(
						new Paragraph(formatter.format(mPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellmP);
			} else {
				alCellmP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellmP);
			}

			Cell alCellsA = new Cell();

			if (hashMap.get("spouseAmt") != null) {
				String comb = (String) hashMap.get("combination");

				/* If Spouse having WPB or HCBF Spouse Amount will print as APPLIED */
				if (comb.equalsIgnoreCase("WPB") || comb.equalsIgnoreCase("HCBF")) {

					alCellsA.add(new Paragraph("Applied").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellsA);

					// Print Spouse Amount
				} else {
					Double spAmt = (Double) hashMap.get("spouseAmt");

					alCellsA.add(new Paragraph(formatter.format(spAmt)).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellsA);
				}

				// IF spouse Amount is null
			} else {
				alCellsA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsA);
			}

			Cell alCellsP = new Cell();
			if (hashMap.get("spousePre") != null) {
				Double spPre = (Double) hashMap.get("spousePre");

				alCellsP.add(
						new Paragraph(formatter.format(spPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsP);
			} else {
				alCellsP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsP);
			}

			Cell alCellcA = new Cell();
			if (hashMap.get("childAmt") != null) {
				String comb = (String) hashMap.get("combination");

				/* If Children get HCBF Amount Will Print as APPLIED */
				if (comb.equalsIgnoreCase("HCBF")) {

					alCellcA.add(new Paragraph("Applied").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellcA);

					// Display Amount
				} else if (!comb.equalsIgnoreCase("HCBF")) {

					Double cAmt = (Double) hashMap.get("childAmt");

					alCellcA.add(
							new Paragraph(formatter.format(cAmt)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellcA);

				}

				// IF Child Amount is NULL Display
			} else {

				alCellcA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcA);
			}

			Cell alCellcP = new Cell();
			if (hashMap.get("childPre") != null) {

				Double cPre = (Double) hashMap.get("childPre");

				alCellcP.add(
						new Paragraph(formatter.format(cPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcP);

			} else {
				alCellcP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcP);
			}

			benAddTable.startNewRow();

		}

		////////////////////// table headings of the Additional Benefits
		////////////////////// table\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		Cell abCellth1 = new Cell(2, 0);
		abCellth1.setBorderTop(new SolidBorder(2));
		abCellth1.add(new Paragraph("Additional Benefits").setFontSize(9).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		benAddTable.addCell(abCellth1);

		Cell abCellth2 = new Cell(0, 2);
		abCellth2.setBorderTop(new SolidBorder(2));
		abCellth2.add(new Paragraph("Main Life").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellth2);

		Cell abCellth3 = new Cell(0, 2);
		abCellth3.setBorderTop(new SolidBorder(2));
		abCellth3.add(new Paragraph("Spouse").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellth3);

		Cell abCellth4 = new Cell(0, 2);
		abCellth4.setBorderTop(new SolidBorder(2));
		abCellth4.add(new Paragraph("Children").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellth4);

		benAddTable.startNewRow();

		Cell abCellMA = new Cell();
		abCellMA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMA);

		Cell abCellMP = new Cell();
		abCellMP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMP);

		Cell abCellSA = new Cell();
		abCellSA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellSA);

		Cell abCellSP = new Cell();
		abCellSP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellSP);

		Cell abCellCA = new Cell();
		abCellCA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellCA);

		Cell abCellCP = new Cell();
		abCellCP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellCP);

		benAddTable.startNewRow();

		/*
		 * Declaring Variable for Natural Death Cover/Main/Spouse(Amount/Premium)
		 * Accidental Death benefit/Main/Spouse(Amount/Premium) Funeral
		 * Expenses/Main/Spouse(Amount/Premium)
		 */
		Double ndc = 0.0;
		Double ndcp = 0.0;

		Double ndcs = 0.0;
		Double ndcsp = 0.0;

		Double adb = 0.0;
		Double adbp = 0.0;

		Double adbs = 0.0;
		Double adbsp = 0.0;

		Double feb = 0.0;
		Double febp = 0.0;

		Double febs = 0.0;
		Double febsp = 0.0;

		if (benefitsLife.isEmpty()) {

		} else {

			for (QuoBenf quoAddBenf : benefitsLife) {

				if (quoAddBenf.getRiderCode().equalsIgnoreCase("L2")
						|| (quoAddBenf.getRiderCode().equalsIgnoreCase("ATPB"))) {
					/*
					 * Natural Death Cover = Basic Sum Assured + Additional Term Protection Benefit
					 */
					ndc = ndc + quoAddBenf.getRiderSum();
					ndcp = ndcp + quoAddBenf.getPremium();

				}
				if (quoAddBenf.getRiderCode().equalsIgnoreCase("ADB")) {
					// only Accidental Death Benefit
					adb = quoAddBenf.getRiderSum();
					adbp = adbp + quoAddBenf.getPremium();

				}
				if (quoAddBenf.getRiderCode().equalsIgnoreCase("FEB")) {
					// only Funeral Expenses Benefit
					feb = quoAddBenf.getRiderSum();
					febp = febp + quoAddBenf.getPremium();

				}

			}
		}

		// check Spouse having Benefits
		if (benefitsSpouse.isEmpty()) {

		} else {

			for (QuoBenf quoSpoBenf : benefitsSpouse) {

				if (quoSpoBenf.getRiderCode().equalsIgnoreCase("SCB")) {
					// only Spouse Cover Benefit
					ndcs = quoSpoBenf.getRiderSum();
					ndcsp = quoSpoBenf.getPremium();

				}
				if (quoSpoBenf.getRiderCode().equalsIgnoreCase("ADBS")) {
					// only Accidental Death Benefit
					adbs = quoSpoBenf.getRiderSum();
					adbsp = quoSpoBenf.getPremium();

				}
				if (quoSpoBenf.getRiderCode().equalsIgnoreCase("FEBS")) {
					// only Funeral Expenses Benefit
					febs = quoSpoBenf.getRiderSum();
					febsp = quoSpoBenf.getPremium();

				}

			}

		}

		/* Printing All Additional Benefits Data */
		Cell abCelld1 = new Cell();
		abCelld1.add(new Paragraph("Natural Death Cover").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld1);

		Cell abCelld2 = new Cell();
		abCelld2.add(new Paragraph(formatter.format(ndc)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld2);

		Cell abCelld3 = new Cell();
		abCelld3.add(new Paragraph(formatter.format(ndcp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld3);

		Cell abCelld4 = new Cell();
		abCelld4.add(new Paragraph(formatter.format(ndcs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld4);

		Cell abCelld5 = new Cell();
		abCelld5.add(new Paragraph(formatter.format(ndcsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld5);

		Cell abCelld6 = new Cell();
		abCelld6.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld6);

		Cell abCelld7 = new Cell();
		abCelld7.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld7);

		benAddTable.startNewRow();

		Cell abCelld8 = new Cell();
		abCelld8.add(new Paragraph("Accidental Death Benefit").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld8);

		Cell abCelld9 = new Cell();
		abCelld9.add(new Paragraph(formatter.format(adb)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld9);

		Cell abCelld10 = new Cell();
		abCelld10.add(new Paragraph(formatter.format(adbp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld10);

		Cell abCelld11 = new Cell();
		abCelld11.add(new Paragraph(formatter.format(adbs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld11);

		Cell abCelld12 = new Cell();
		abCelld12.add(new Paragraph(formatter.format(adbsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld12);

		Cell abCelld13 = new Cell();
		abCelld13.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld13);

		Cell abCelld14 = new Cell();
		abCelld14.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld14);

		benAddTable.startNewRow();

		Cell abCelld15 = new Cell();
		abCelld15.add(new Paragraph("Funeral Expenses").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld15);

		Cell abCelld16 = new Cell();
		abCelld16.add(new Paragraph(formatter.format(feb)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld16);

		Cell abCelld117 = new Cell();
		abCelld117.add(new Paragraph(formatter.format(febp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld117);

		Cell abCelld18 = new Cell();
		abCelld18.add(new Paragraph(formatter.format(febs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld18);

		Cell abCelld19 = new Cell();
		abCelld19.add(new Paragraph(formatter.format(febsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld19);

		Cell abCelld20 = new Cell();
		abCelld20.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld20);

		Cell abCelld21 = new Cell();
		abCelld21.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld21);

		///////////////////// End Of Additional Benefits Table
		///////////////////// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		document.add(benAddTable);

		/////////////////////////// END OLD
		/////////////////////////// FORMAT///////////////////////////////////////

		// Medical Requirements
		try {
			java.util.List<MedicalRequirementsHelper> medicalDetails = medicalRequirementsDaoCustom
					.findByQuoDetail(quotationDetails.getQdId());

			if (medicalDetails.isEmpty()) {

			} else {
				document.add(new Paragraph(""));

				document.add(new Paragraph("Medical Requirements").setBold().setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));

				document.add(new Paragraph(""));

				// Medical Requirements Table
				float[] pointColumnWidths6 = { 150, 100, 100 };
				Table medReqTable = new Table(pointColumnWidths6);
				medReqTable.setBorder(new SolidBorder(1));
				medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

				Cell mrqCell2 = new Cell();
				mrqCell2.setBorder(new SolidBorder(1));
				mrqCell2.add(new Paragraph("Requirements").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell2);

				Cell mrqCell3 = new Cell();
				mrqCell3.setBorder(new SolidBorder(1));
				mrqCell3.add(new Paragraph("Main Life").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell3);

				Cell mrqCell4 = new Cell();
				mrqCell4.setBorder(new SolidBorder(1));
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
						.setFontSize(10).setFixedLeading(10));
		list.add(item1);

		ListItem item2 = new ListItem();
		item2.add(
				new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.")
						.setFontSize(10).setFixedLeading(10));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date issue.")
				.setFontSize(10).setFixedLeading(10));
		list.add(item3);

		ListItem item4 = new ListItem();
		item4.add(new Paragraph("Abbrevations ; Y - Yes, R - Required, NR - Not Required").setFontSize(10)
				.setFixedLeading(10));
		list.add(item4);

		ListItem item5 = new ListItem();
		item5.add(new Paragraph("All amounts are in Sri Lankan Rupees(LKR).").setFontSize(10).setFixedLeading(10));
		list.add(item5);

		ListItem item6 = new ListItem();
		item6.add(new Paragraph("Initial policy processing fee of Rs. 300 (Payable only with initial deposit).")
				.setFontSize(10).setFixedLeading(10));
		list.add(item6);

		ListItem item7 = new ListItem();
		item7.add(new Paragraph(
				"In event of death by accident both Accident Cover and Natural Death Cover will be applicable.")
						.setFontSize(10).setFixedLeading(10));
		list.add(item7);

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
		document.setTopMargin(50);

		// Agent Details
		float[] pointColumnWidths1 = { 90, 200 };
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

		Cell agCellQId = new Cell();
		agCellQId.setBorder(Border.NO_BORDER);
		agCellQId.add(new Paragraph("Quotation No ").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		agtTable.addCell(agCellQId);
		Cell agCellQ = new Cell();
		agCellQ.setBorder(Border.NO_BORDER);
		agCellQ.add(new Paragraph(": " + quotationDetails.getQuotation().getId()).setFontSize(10)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCellQ);

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
		if (quotationDetails.getQuotation().getUser().getUserCode() != null) {
			String code = quotationDetails.getQuotation().getUser().getUserCode();
			Integer val = 0;

			try {
				Integer.parseInt(code);
				val = 1;

			} catch (NumberFormatException e) {
				val = 0;
			}

			if (val == 1) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": " + quotationDetails.getQuotation().getUser().getUserCode())
						.setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else if (val == 0) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": ............................ ").setFontSize(10)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(
						new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			}
		} else {

		}

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

		document.add(new Paragraph("ARPICO ENDOWMENT PLAN").setFontSize(10).setCharacterSpacing(1));

		final SolidLine lineDrawer = new SolidLine(1f);
		document.add(new LineSeparator(lineDrawer));
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
		DtlTable.setFixedPosition(400, 630, 230);

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
		dlcell3.add(new Paragraph("Paying Method").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
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
			dlcell6.add(new Paragraph(": " + formatter.format(quoCustomer.getTotPremium())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell6.add(new Paragraph("").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell6);

		document.add(DtlTable);

		document.add(new Paragraph(""));

		document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		//////////////////////////// TEST OLD
		//////////////////////////// FORMAT//////////////////////////////////////

		// Create Additional Benefits Table
		/* Declaring column sizes of the table respectively */
		float[] pointColumnWidths4 = { 450, 100, 100, 100, 100, 100, 100 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(new SolidBorder(1));

		// table headings of the Living Benefits
		Cell alCellth1 = new Cell(2, 0);
		alCellth1.setBorder(new SolidBorder(1));
		alCellth1.add(new Paragraph("Living Benefits").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellth1);

		Cell alCellth2 = new Cell(0, 2);
		alCellth2.setBorder(new SolidBorder(1));
		alCellth2.add(new Paragraph("Main Life").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellth2);

		Cell alCellth3 = new Cell(0, 2);
		alCellth3.setBorder(new SolidBorder(1));
		alCellth3.add(new Paragraph("Spouse").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellth3);

		Cell alCellth4 = new Cell(0, 2);
		alCellth4.setBorder(new SolidBorder(1));
		alCellth4.add(new Paragraph("Children").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellth4);

		benAddTable.startNewRow();

		Cell alCellMA = new Cell();
		alCellMA.setBorder(new SolidBorder(1));
		alCellMA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellMA);

		Cell alCellMP = new Cell();
		alCellMP.setBorder(new SolidBorder(1));
		alCellMP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellMP);

		Cell alCellSA = new Cell();
		alCellSA.setBorder(new SolidBorder(1));
		alCellSA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellSA);

		Cell alCellSP = new Cell();
		alCellSP.setBorder(new SolidBorder(1));
		alCellSP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellSP);

		Cell alCellCA = new Cell();
		alCellCA.setBorder(new SolidBorder(1));
		alCellCA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellCA);

		Cell alCellCP = new Cell();
		alCellCP.setBorder(new SolidBorder(1));
		alCellCP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellCP);

		/////////////////// End Of the Table Heading ////////////

		benAddTable.startNewRow();

		// Getting a HashMap to an ArayList
		ArrayList<HashMap<String, Object>> benifList = new ArrayList<>();

		// Checking MainLife Having Benefits
		if (benefitsLife.isEmpty()) {

		} else {

			for (QuoBenf quoBenf : benefitsLife) {

				HashMap<String, Object> benefitDetailMap = new HashMap<>();

				/*
				 * Do not Print Basic Sum Assured,ATPB,ADB and FEB Its Printed on Additional
				 * Benefits Table
				 */
				if (!quoBenf.getRiderCode().equalsIgnoreCase("L2") && (!quoBenf.getRiderCode().equalsIgnoreCase("ATPB"))
						&& (!quoBenf.getRiderCode().equalsIgnoreCase("ADB"))
						&& (!quoBenf.getRiderCode().equalsIgnoreCase("FEB"))) {

					// Cheking the Combination of the benefits by passing Rider Code
					if (benefictDao.findByRiderCode(quoBenf.getRiderCode()) != null) {
						benefitDetailMap.put("combination",
								benefictDao.findByRiderCode(quoBenf.getRiderCode()).getBenefictCombination());

						benefitDetailMap.put("benName", quoBenf.getBenfName());
						benefitDetailMap.put("mainAmt", quoBenf.getRiderSum());
						benefitDetailMap.put("mainPre", quoBenf.getPremium());

						benifList.add(benefitDetailMap);
					}

				}

			}

		}

		// checking Spouse Having Benefits
		if (benefitsSpouse.isEmpty()) {

		} else {
			for (QuoBenf quoBenf : benefitsSpouse) {

				if (!quoBenf.getRiderCode().equalsIgnoreCase("ADBS") && (!quoBenf.getRiderCode().equalsIgnoreCase("SCB")
						&& (!quoBenf.getRiderCode().equalsIgnoreCase("FEBS")))) {
					Integer isAvailable = 0;

					for (HashMap<String, Object> benefitDetailMap : benifList) {

						if (benefitDetailMap.get("combination")
								.equals(benefictDao.findByRiderCode(quoBenf.getRiderCode()).getBenefictCombination())) {
							isAvailable = 1;
							benefitDetailMap.put("spouseAmt", quoBenf.getRiderSum());
							benefitDetailMap.put("spousePre", quoBenf.getPremium());
						}

					}

					/*
					 * If doesn't math with main life benefit combination put new benefits of the
					 * spouse to HASHMAP
					 */
					if (isAvailable == 0) {

						HashMap<String, Object> benefitDetailMap = new HashMap<>();

						benefitDetailMap.put("combination",
								benefictDao.findByRiderCode(quoBenf.getRiderCode()).getBenefictCombination());
						benefitDetailMap.put("benName", quoBenf.getBenfName());
						benefitDetailMap.put("spouseAmt", quoBenf.getRiderSum());
						benefitDetailMap.put("spousePre", quoBenf.getPremium());

						benifList.add(benefitDetailMap);
					}

				}

			}

		}

		// Checking Child having Benefits
		if (benefitsChild.isEmpty()) {

		} else {

			/* Declaring variables to Calculate the total of Premium of all Children */
			Double cibc = 0.0;
			Double hbc = 0.0;
			Double hcbic = 0.0;

			for (QuoChildBenef quoChild : benefitsChild) {

				for (QuoBenf quoChildBenef : quoChild.getBenfs()) {

					if (quoChildBenef.getRiderCode().equalsIgnoreCase("CIBC")) {
						cibc = cibc + quoChildBenef.getPremium();
					} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HBC")) {

						hbc = hbc + quoChildBenef.getPremium();
					} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HCBIC")
							|| quoChildBenef.getRiderCode().equalsIgnoreCase("SHCBIC")) {

						hcbic = hcbic + quoChildBenef.getPremium();

					}

					Integer isAvailable = 0;

					for (HashMap<String, Object> benefitDetailMap : benifList) {

						if (benefitDetailMap.get("combination").equals(
								benefictDao.findByRiderCode(quoChildBenef.getRiderCode()).getBenefictCombination())) {
							isAvailable = 1;
							benefitDetailMap.put("childAmt", quoChildBenef.getRiderSum());

							if (quoChildBenef.getRiderCode().equalsIgnoreCase("CIBC")) {
								benefitDetailMap.put("childPre", cibc);

							} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HBC")) {
								benefitDetailMap.put("childPre", hbc);

							} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HCBIC")
									|| quoChildBenef.getRiderCode().equalsIgnoreCase("SHCBIC")) {
								benefitDetailMap.put("childPre", hcbic);

							}
						}

					}

					/*
					 * If benefits combination of the children not in ArrayList put new child
					 * benefits to the HASHMAP
					 */
					if (isAvailable == 0) {

						HashMap<String, Object> benefitDetailMap = new HashMap<>();

						benefitDetailMap.put("combination",
								benefictDao.findByRiderCode(quoChildBenef.getRiderCode()).getBenefictCombination());
						benefitDetailMap.put("benName", quoChildBenef.getBenfName());
						benefitDetailMap.put("childAmt", quoChildBenef.getRiderSum());

						if (quoChildBenef.getRiderCode().equalsIgnoreCase("CIBC")) {
							benefitDetailMap.put("childPre", cibc);

						} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HBC")) {
							benefitDetailMap.put("childPre", hbc);

						} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HCBIC")
								|| quoChildBenef.getRiderCode().equalsIgnoreCase("SHCBIC")) {
							benefitDetailMap.put("childPre", hcbic);

						}

						// Adding Full benefits details Hash Map to ArrayList
						benifList.add(benefitDetailMap);
					}
				}
			}

		}

		// Getting Full Benf HashMap using foreach
		for (HashMap<String, Object> hashMap : benifList) {

			Cell alCellBenf = new Cell();

			// Getting ALL Benefits Name object and cast to an String
			String p = (String) hashMap.get("benName");

			// System.out.println("combinationnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn "
			// +hashMap.get("combination"));
			alCellBenf.add(new Paragraph(p).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
			benAddTable.addCell(alCellBenf);

			//
			Cell alCellmA = new Cell();
			if (hashMap.get("mainAmt") != null) {

				// Getting ALL Benefits Name Combinations object and cast to an String
				String comb = (String) hashMap.get("combination");

				/* If benefit is WPB Print Amount as APPLIED */
				if (comb.equalsIgnoreCase("WPB")) {
					alCellmA.add(new Paragraph("Applied").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmA);

				} else {

					Double mAmt = (Double) hashMap.get("mainAmt");

					alCellmA.add(
							new Paragraph(formatter.format(mAmt)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmA);
				}

				// If Main Amount NULL
			} else {

				alCellmA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellmA);
			}

			Cell alCellmP = new Cell();
			if (hashMap.get("mainPre") != null) {
				Double mPre = (Double) hashMap.get("mainPre");

				alCellmP.add(
						new Paragraph(formatter.format(mPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellmP);
			} else {
				alCellmP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellmP);
			}

			Cell alCellsA = new Cell();

			if (hashMap.get("spouseAmt") != null) {
				String comb = (String) hashMap.get("combination");

				/* If Spouse having WPB or HCBF Spouse Amount will print as APPLIED */
				if (comb.equalsIgnoreCase("WPB") || comb.equalsIgnoreCase("HCBF")) {

					alCellsA.add(new Paragraph("Applied").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellsA);

					// Print Spouse Amount
				} else {
					Double spAmt = (Double) hashMap.get("spouseAmt");

					alCellsA.add(new Paragraph(formatter.format(spAmt)).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellsA);
				}

				// IF spouse Amount is null
			} else {
				alCellsA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsA);
			}

			Cell alCellsP = new Cell();
			if (hashMap.get("spousePre") != null) {
				Double spPre = (Double) hashMap.get("spousePre");

				alCellsP.add(
						new Paragraph(formatter.format(spPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsP);
			} else {
				alCellsP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsP);
			}

			Cell alCellcA = new Cell();
			if (hashMap.get("childAmt") != null) {
				String comb = (String) hashMap.get("combination");

				/* If Children get HCBF Amount Will Print as APPLIED */
				if (comb.equalsIgnoreCase("HCBF")) {

					alCellcA.add(new Paragraph("Applied").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellcA);

					// Display Amount
				} else if (!comb.equalsIgnoreCase("HCBF")) {

					Double cAmt = (Double) hashMap.get("childAmt");

					alCellcA.add(
							new Paragraph(formatter.format(cAmt)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellcA);

				}

				// IF Child Amount is NULL Display
			} else {

				alCellcA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcA);
			}

			Cell alCellcP = new Cell();
			if (hashMap.get("childPre") != null) {

				Double cPre = (Double) hashMap.get("childPre");

				alCellcP.add(
						new Paragraph(formatter.format(cPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcP);

			} else {
				alCellcP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcP);
			}

			benAddTable.startNewRow();

		}

		////////////////////// table headings of the Additional Benefits
		////////////////////// table\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		Cell abCellth1 = new Cell(2, 0);
		abCellth1.setBorderTop(new SolidBorder(2));
		abCellth1.add(new Paragraph("Additional Benefits").setFontSize(9).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		benAddTable.addCell(abCellth1);

		Cell abCellth2 = new Cell(0, 2);
		abCellth2.setBorderTop(new SolidBorder(2));
		abCellth2.add(new Paragraph("Main Life").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellth2);

		Cell abCellth3 = new Cell(0, 2);
		abCellth3.setBorderTop(new SolidBorder(2));
		abCellth3.add(new Paragraph("Spouse").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellth3);

		Cell abCellth4 = new Cell(0, 2);
		abCellth4.setBorderTop(new SolidBorder(2));
		abCellth4.add(new Paragraph("Children").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellth4);

		benAddTable.startNewRow();

		Cell abCellMA = new Cell();
		abCellMA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMA);

		Cell abCellMP = new Cell();
		abCellMP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMP);

		Cell abCellSA = new Cell();
		abCellSA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellSA);

		Cell abCellSP = new Cell();
		abCellSP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellSP);

		Cell abCellCA = new Cell();
		abCellCA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellCA);

		Cell abCellCP = new Cell();
		abCellCP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellCP);

		benAddTable.startNewRow();

		/*
		 * Declaring Variable for Natural Death Cover/Main/Spouse(Amount/Premium)
		 * Accidental Death benefit/Main/Spouse(Amount/Premium) Funeral
		 * Expenses/Main/Spouse(Amount/Premium)
		 */
		Double ndc = 0.0;
		Double ndcp = 0.0;

		Double ndcs = 0.0;
		Double ndcsp = 0.0;

		Double adb = 0.0;
		Double adbp = 0.0;

		Double adbs = 0.0;
		Double adbsp = 0.0;

		Double feb = 0.0;
		Double febp = 0.0;

		Double febs = 0.0;
		Double febsp = 0.0;

		if (benefitsLife.isEmpty()) {

		} else {

			for (QuoBenf quoAddBenf : benefitsLife) {

				if (quoAddBenf.getRiderCode().equalsIgnoreCase("L2")
						|| (quoAddBenf.getRiderCode().equalsIgnoreCase("ATPB"))) {
					/*
					 * Natural Death Cover = Basic Sum Assured + Additional Term Protection Benefit
					 */
					ndc = ndc + quoAddBenf.getRiderSum();
					ndcp = ndcp + quoAddBenf.getPremium();

				}
				if (quoAddBenf.getRiderCode().equalsIgnoreCase("ADB")) {
					// only Accidental Death Benefit
					adb = quoAddBenf.getRiderSum();
					adbp = adbp + quoAddBenf.getPremium();

				}
				if (quoAddBenf.getRiderCode().equalsIgnoreCase("FEB")) {
					// only Funeral Expenses Benefit
					feb = quoAddBenf.getRiderSum();
					febp = febp + quoAddBenf.getPremium();

				}

			}
		}

		// check Spouse having Benefits
		if (benefitsSpouse.isEmpty()) {

		} else {

			for (QuoBenf quoSpoBenf : benefitsSpouse) {

				if (quoSpoBenf.getRiderCode().equalsIgnoreCase("SCB")) {
					// only Spouse Cover Benefit
					ndcs = quoSpoBenf.getRiderSum();
					ndcsp = quoSpoBenf.getPremium();

				}
				if (quoSpoBenf.getRiderCode().equalsIgnoreCase("ADBS")) {
					// only Accidental Death Benefit
					adbs = quoSpoBenf.getRiderSum();
					adbsp = quoSpoBenf.getPremium();

				}
				if (quoSpoBenf.getRiderCode().equalsIgnoreCase("FEBS")) {
					// only Funeral Expenses Benefit
					febs = quoSpoBenf.getRiderSum();
					febsp = quoSpoBenf.getPremium();

				}

			}

		}

		/* Printing All Additional Benefits Data */
		Cell abCelld1 = new Cell();
		abCelld1.add(new Paragraph("Natural Death Cover").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld1);

		Cell abCelld2 = new Cell();
		abCelld2.add(new Paragraph(formatter.format(ndc)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld2);

		Cell abCelld3 = new Cell();
		abCelld3.add(new Paragraph(formatter.format(ndcp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld3);

		Cell abCelld4 = new Cell();
		abCelld4.add(new Paragraph(formatter.format(ndcs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld4);

		Cell abCelld5 = new Cell();
		abCelld5.add(new Paragraph(formatter.format(ndcsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld5);

		Cell abCelld6 = new Cell();
		abCelld6.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld6);

		Cell abCelld7 = new Cell();
		abCelld7.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld7);

		benAddTable.startNewRow();

		Cell abCelld8 = new Cell();
		abCelld8.add(new Paragraph("Accidental Death Benefit").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld8);

		Cell abCelld9 = new Cell();
		abCelld9.add(new Paragraph(formatter.format(adb)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld9);

		Cell abCelld10 = new Cell();
		abCelld10.add(new Paragraph(formatter.format(adbp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld10);

		Cell abCelld11 = new Cell();
		abCelld11.add(new Paragraph(formatter.format(adbs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld11);

		Cell abCelld12 = new Cell();
		abCelld12.add(new Paragraph(formatter.format(adbsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld12);

		Cell abCelld13 = new Cell();
		abCelld13.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld13);

		Cell abCelld14 = new Cell();
		abCelld14.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld14);

		benAddTable.startNewRow();

		Cell abCelld15 = new Cell();
		abCelld15.add(new Paragraph("Funeral Expenses").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld15);

		Cell abCelld16 = new Cell();
		abCelld16.add(new Paragraph(formatter.format(feb)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld16);

		Cell abCelld117 = new Cell();
		abCelld117.add(new Paragraph(formatter.format(febp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld117);

		Cell abCelld18 = new Cell();
		abCelld18.add(new Paragraph(formatter.format(febs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld18);

		Cell abCelld19 = new Cell();
		abCelld19.add(new Paragraph(formatter.format(febsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld19);

		Cell abCelld20 = new Cell();
		abCelld20.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld20);

		Cell abCelld21 = new Cell();
		abCelld21.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld21);

		///////////////////// End Of Additional Benefits Table
		///////////////////// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		document.add(benAddTable);

		/////////////////////////// END OLD
		/////////////////////////// FORMAT///////////////////////////////////////

		// Medical Requirements
		try {
			java.util.List<MedicalRequirementsHelper> medicalDetails = medicalRequirementsDaoCustom
					.findByQuoDetail(quotationDetails.getQdId());

			if (medicalDetails.isEmpty()) {

			} else {
				document.add(new Paragraph(""));

				document.add(new Paragraph("Medical Requirements").setBold().setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));

				document.add(new Paragraph(""));

				// Medical Requirements Table
				float[] pointColumnWidths6 = { 150, 100, 100 };
				Table medReqTable = new Table(pointColumnWidths6);
				medReqTable.setBorder(new SolidBorder(1));
				medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

				Cell mrqCell2 = new Cell();
				mrqCell2.setBorder(new SolidBorder(1));
				mrqCell2.add(new Paragraph("Requirements").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell2);

				Cell mrqCell3 = new Cell();
				mrqCell3.setBorder(new SolidBorder(1));
				mrqCell3.add(new Paragraph("Main Life").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell3);

				Cell mrqCell4 = new Cell();
				mrqCell4.setBorder(new SolidBorder(1));
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
						.setFontSize(10).setFixedLeading(10));
		list.add(item1);

		ListItem item2 = new ListItem();
		item2.add(
				new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.")
						.setFontSize(10).setFixedLeading(10));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date issue.")
				.setFontSize(10).setFixedLeading(10));
		list.add(item3);

		ListItem item4 = new ListItem();
		item4.add(new Paragraph("Abbrevations ; Y - Yes, R - Required, NR - Not Required").setFontSize(10)
				.setFixedLeading(10));
		list.add(item4);

		ListItem item5 = new ListItem();
		item5.add(new Paragraph("All amounts are in Sri Lankan Rupees(LKR).").setFontSize(10).setFixedLeading(10));
		list.add(item5);

		ListItem item6 = new ListItem();
		item6.add(new Paragraph("Initial policy processing fee of Rs. 300 (Payable only with initial deposit).")
				.setFontSize(10).setFixedLeading(10));
		list.add(item6);

		ListItem item7 = new ListItem();
		item7.add(new Paragraph(
				"In event of death by accident both Accident Cover and Natural Death Cover will be applicable.")
						.setFontSize(10).setFixedLeading(10));
		list.add(item7);

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
		document.setTopMargin(50);

		// Agent Details
		float[] pointColumnWidths1 = { 90, 200 };
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

		Cell agCellQId = new Cell();
		agCellQId.setBorder(Border.NO_BORDER);
		agCellQId.add(new Paragraph("Quotation No ").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		agtTable.addCell(agCellQId);
		Cell agCellQ = new Cell();
		agCellQ.setBorder(Border.NO_BORDER);
		agCellQ.add(new Paragraph(": " + quotationDetails.getQuotation().getId()).setFontSize(10)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCellQ);

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
		if (quotationDetails.getQuotation().getUser().getUserCode() != null) {
			String code = quotationDetails.getQuotation().getUser().getUserCode();
			Integer val = 0;

			try {
				Integer.parseInt(code);
				val = 1;

			} catch (NumberFormatException e) {
				val = 0;
			}

			if (val == 1) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": " + quotationDetails.getQuotation().getUser().getUserCode())
						.setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else if (val == 0) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": ............................ ").setFontSize(10)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(
						new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			}
		} else {

		}

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

		document.add(new Paragraph("ARPICO SCHOOL FEES PLAN").setFontSize(10).setCharacterSpacing(1));
		document.add(new Paragraph("TOTAL PROTECTION OF CHILD SCHOOL FEE OR MONEY BACK GUARANTEE").setFontSize(10)
				.setCharacterSpacing(1).setFixedLeading(1));

		final SolidLine lineDrawer = new SolidLine(1f);
		document.add(new LineSeparator(lineDrawer));

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
		dlcell3.add(new Paragraph("Paying Method").setFontSize(10).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
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
			dlcell6.add(new Paragraph(": " + formatter.format(quoCustomer.getTotPremium())).setFontSize(10)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell6.add(new Paragraph("").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell6);

		document.add(DtlTable);

		document.add(new Paragraph(""));

		document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		//////////////////////////// TEST OLD
		//////////////////////////// FORMAT//////////////////////////////////////

		// Create Additional Benefits Table
		float[] pointColumnWidths4 = { 450, 100, 100, 100, 100, 100, 100 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(new SolidBorder(1));

		// table headings
		Cell abCellth1 = new Cell(2, 0);
		abCellth1.setBorder(new SolidBorder(1));
		abCellth1.add(new Paragraph("Additional Benefits").setFontSize(9).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		benAddTable.addCell(abCellth1);

		Cell abCellth2 = new Cell(0, 2);
		abCellth2.setBorder(new SolidBorder(1));
		abCellth2.add(new Paragraph("Main Life").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellth2);
		Cell abCellth3 = new Cell(0, 2);
		abCellth3.setBorder(new SolidBorder(1));
		abCellth3.add(new Paragraph("Spouse").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellth3);
		Cell abCellth4 = new Cell(0, 2);
		abCellth4.setBorder(new SolidBorder(1));
		abCellth4.add(new Paragraph("Children").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellth4);

		benAddTable.startNewRow();

		Cell abCellMA = new Cell();
		abCellMA.setBorder(new SolidBorder(1));
		abCellMA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMA);

		Cell abCellMP = new Cell();
		abCellMP.setBorder(new SolidBorder(1));
		abCellMP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMP);

		Cell abCellSA = new Cell();
		abCellSA.setBorder(new SolidBorder(1));
		abCellSA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellSA);

		Cell abCellSP = new Cell();
		abCellSP.setBorder(new SolidBorder(1));
		abCellSP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellSP);

		Cell abCellCA = new Cell();
		abCellCA.setBorder(new SolidBorder(1));
		abCellCA.add(new Paragraph("Amount").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellCA);

		Cell abCellCP = new Cell();
		abCellCP.setBorder(new SolidBorder(1));
		abCellCP.add(new Paragraph("Premium").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellCP);

		benAddTable.startNewRow();

		// Getting a HashMap to an ArayList
		ArrayList<HashMap<String, Object>> benifList = new ArrayList<>();

		// Checking MainLife Having Benefits
		if (benefitsLife.isEmpty()) {

		} else {

			for (QuoBenf quoBenf : benefitsLife) {

				HashMap<String, Object> benefitDetailMap = new HashMap<>();

				if (benefictDao.findByRiderCode(quoBenf.getRiderCode()) != null) {
					benefitDetailMap.put("combination",
							benefictDao.findByRiderCode(quoBenf.getRiderCode()).getBenefictCombination());

					if (quoBenf.getRiderCode().equalsIgnoreCase("L10")) {

						Object aspMonthly = (Object) "Monthly School Fee Expenses Benefit";
						benefitDetailMap.put("benName", aspMonthly);

						Double schoolFee = (Double) quoBenf.getRiderSum() / (12 * quotationDetails.getPolTerm());
						benefitDetailMap.put("mainAmt", schoolFee);
						benefitDetailMap.put("mainPre", quoBenf.getPremium());

						benifList.add(benefitDetailMap);

					} else {
						benefitDetailMap.put("benName", quoBenf.getBenfName());
						benefitDetailMap.put("mainAmt", quoBenf.getRiderSum());
						benefitDetailMap.put("mainPre", quoBenf.getPremium());

						benifList.add(benefitDetailMap);
					}

				}

			}

		}

		// checking Spouse Having Benefits
		if (benefitsSpouse.isEmpty()) {

		} else {
			for (QuoBenf quoBenf : benefitsSpouse) {

				Integer isAvailable = 0;

				for (HashMap<String, Object> benefitDetailMap : benifList) {
					if (benefitDetailMap.get("combination")
							.equals(benefictDao.findByRiderCode(quoBenf.getRiderCode()).getBenefictCombination())) {
						isAvailable = 1;
						benefitDetailMap.put("spouseAmt", quoBenf.getRiderSum());
						benefitDetailMap.put("spousePre", quoBenf.getPremium());
					}
				}

				if (isAvailable == 0) {

					HashMap<String, Object> benefitDetailMap = new HashMap<>();

					benefitDetailMap.put("combination",
							benefictDao.findByRiderCode(quoBenf.getRiderCode()).getBenefictCombination());
					benefitDetailMap.put("benName", quoBenf.getBenfName());
					benefitDetailMap.put("spouseAmt", quoBenf.getRiderSum());
					benefitDetailMap.put("spousePre", quoBenf.getPremium());

					benifList.add(benefitDetailMap);
				}

			}

		}

		// Checking Child having Benefits
		if (benefitsChild.isEmpty()) {

		} else {
			Double cibc = 0.0;
			Double hbc = 0.0;
			Double hcbic = 0.0;

			for (QuoChildBenef quoChild : benefitsChild) {

				for (QuoBenf quoChildBenef : quoChild.getBenfs()) {

					if (quoChildBenef.getRiderCode().equalsIgnoreCase("CIBC")) {
						cibc = cibc + quoChildBenef.getPremium();
					} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HBC")) {

						hbc = hbc + quoChildBenef.getPremium();
					} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HCBIC")
							|| quoChildBenef.getRiderCode().equalsIgnoreCase("SHCBIC")) {

						hcbic = hcbic + quoChildBenef.getPremium();
					}
					Integer isAvailable = 0;

					for (HashMap<String, Object> benefitDetailMap : benifList) {

						if (benefitDetailMap.get("combination").equals(
								benefictDao.findByRiderCode(quoChildBenef.getRiderCode()).getBenefictCombination())) {
							isAvailable = 1;
							benefitDetailMap.put("childAmt", quoChildBenef.getRiderSum());

							if (quoChildBenef.getRiderCode().equalsIgnoreCase("CIBC")) {
								benefitDetailMap.put("childPre", cibc);

							} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HBC")) {
								benefitDetailMap.put("childPre", hbc);

							} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HCBIC")
									|| quoChildBenef.getRiderCode().equalsIgnoreCase("SHCBIC")) {
								benefitDetailMap.put("childPre", hcbic);

							}
						}

					}

					if (isAvailable == 0) {

						HashMap<String, Object> benefitDetailMap = new HashMap<>();

						benefitDetailMap.put("combination",
								benefictDao.findByRiderCode(quoChildBenef.getRiderCode()).getBenefictCombination());
						benefitDetailMap.put("benName", quoChildBenef.getBenfName());
						benefitDetailMap.put("childAmt", quoChildBenef.getRiderSum());

						if (quoChildBenef.getRiderCode().equalsIgnoreCase("CIBC")) {
							benefitDetailMap.put("childPre", cibc);

						} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HBC")) {
							benefitDetailMap.put("childPre", hbc);

						} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HCBIC")
								|| quoChildBenef.getRiderCode().equalsIgnoreCase("SHCBIC")) {
							benefitDetailMap.put("childPre", hcbic);

						}

						benifList.add(benefitDetailMap);
					}
				}
			}

		}

		// Getting Full Benf HashMap
		for (HashMap<String, Object> hashMap : benifList) {

			Cell abCellBenf = new Cell();
			// Getting ALL Benf Name Combinattons
			String p = (String) hashMap.get("benName");
			abCellBenf.add(new Paragraph(p).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
			benAddTable.addCell(abCellBenf);

			Cell abCellmA = new Cell();
			if (hashMap.get("mainAmt") != null) {
				Double mAmt = (Double) hashMap.get("mainAmt");
				abCellmA.add(
						new Paragraph(formatter.format(mAmt)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCellmA);

			} else {
				abCellmA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCellmA);
			}

			Cell abCellmP = new Cell();
			if (hashMap.get("mainPre") != null) {
				Double mPre = (Double) hashMap.get("mainPre");

				abCellmP.add(
						new Paragraph(formatter.format(mPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCellmP);
			} else {
				abCellmP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCellmP);
			}

			Cell abCellsA = new Cell();

			if (hashMap.get("spouseAmt") != null) {

				String comb = (String) hashMap.get("combination");

				if (comb.equalsIgnoreCase("HCBF")) {
					abCellsA.add(new Paragraph("Applied").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCellsA);

				} else if (!comb.equalsIgnoreCase("HCBF")) {
					Double spAmt = (Double) hashMap.get("spouseAmt");

					abCellsA.add(new Paragraph(formatter.format(spAmt)).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCellsA);
				}

			} else {
				abCellsA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCellsA);
			}

			Cell abCellsP = new Cell();
			if (hashMap.get("spousePre") != null) {
				Double spPre = (Double) hashMap.get("spousePre");

				abCellsP.add(
						new Paragraph(formatter.format(spPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCellsP);
			} else {
				abCellsP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCellsP);
			}

			Cell abCellcA = new Cell();

			if (hashMap.get("childAmt") != null) {
				String comb = (String) hashMap.get("combination");

				if (comb.equalsIgnoreCase("HCBF")) {
					abCellcA.add(new Paragraph("Applied").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCellcA);

				} else if (!comb.equalsIgnoreCase("HCBF")) {
					Double cAmt = (Double) hashMap.get("childAmt");

					abCellcA.add(
							new Paragraph(formatter.format(cAmt)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(abCellcA);
				}

			} else {
				abCellcA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCellcA);
			}

			Cell abCellcP = new Cell();
			if (hashMap.get("childPre") != null) {
				Double cPre = (Double) hashMap.get("childPre");

				abCellcP.add(
						new Paragraph(formatter.format(cPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCellcP);
			} else {
				abCellcP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(abCellcP);
			}

			benAddTable.startNewRow();
		}

		document.add(benAddTable);

		/////////////////////////// END OLD
		/////////////////////////// FORMAT///////////////////////////////////////

		document.add(new Paragraph(""));
		BigDecimal sfpo = new BigDecimal(0.0);
		BigDecimal rdrprm = new BigDecimal(0.0);
		for (QuoBenf quoBenf : benefitsLife) {
			if (quoBenf.getRiderCode().equalsIgnoreCase("SFPO")) {
				sfpo = new BigDecimal(quoBenf.getRiderSum());
			} else if (quoBenf.getRiderCode().equalsIgnoreCase("L10")) {
				rdrprm = new BigDecimal(quoBenf.getPremium());
			}
		}

		document.add(new Paragraph(
				"If no claim arises during the policy term on the primary benefit Guranteed maturity value : "
						+ formatter.format((((rdrprm.multiply(
								new BigDecimal(new CalculationUtils().getPayterm(quotationDetails.getPayMode()))))
										.multiply(new BigDecimal(quotationDetails.getPolTerm()))).add(sfpo).setScale(0,
												RoundingMode.HALF_UP)).setScale(2).doubleValue())).setFontSize(10));

		// Medical Requirements
		try {
			java.util.List<MedicalRequirementsHelper> medicalDetails = medicalRequirementsDaoCustom
					.findByQuoDetail(quotationDetails.getQdId());

			if (medicalDetails.isEmpty()) {

			} else {
				document.add(new Paragraph(""));

				document.add(new Paragraph("Medical Requirements").setBold().setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));

				document.add(new Paragraph(""));

				// Medical Requirements Table
				float[] pointColumnWidths6 = { 150, 100, 100 };
				Table medReqTable = new Table(pointColumnWidths6);
				medReqTable.setBorder(new SolidBorder(1));
				medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

				Cell mrqCell2 = new Cell();
				mrqCell2.setBorder(new SolidBorder(1));
				mrqCell2.add(new Paragraph("Requirements").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell2);

				Cell mrqCell3 = new Cell();
				mrqCell3.setBorder(new SolidBorder(1));
				mrqCell3.add(new Paragraph("Main Life").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell3);

				Cell mrqCell4 = new Cell();
				mrqCell4.setBorder(new SolidBorder(1));
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

					if (benefitsSpouse.isEmpty()) {

						Cell mrqCell7 = new Cell();
						mrqCell7.add(new Paragraph("NR").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
								.setFixedLeading(10));
						medReqTable.addCell(mrqCell7);

					} else {

						Cell mrqCell7 = new Cell();
						mrqCell7.add(new Paragraph(medicalReq.getSpouseStatus()).setFontSize(9)
								.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
						medReqTable.addCell(mrqCell7);
					}

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
						.setFontSize(10).setFixedLeading(10));
		list.add(item1);

		ListItem item2 = new ListItem();
		item2.add(
				new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.")
						.setFontSize(10).setFixedLeading(10));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date of issue.")
				.setFontSize(10).setFixedLeading(10));
		list.add(item3);

		ListItem item4 = new ListItem();
		item4.add(new Paragraph("Abbrevations ; Y - Yes, R - Required, NR - Not Required").setFontSize(10)
				.setFixedLeading(10));
		list.add(item4);

		ListItem item5 = new ListItem();
		item5.add(new Paragraph("All amounts are in Sri Lankan Rupees (LKR).").setFontSize(10).setFixedLeading(10));
		list.add(item5);

		ListItem item6 = new ListItem();
		item6.add(new Paragraph("Initial policy processing fee of Rs 300 (Payable only with initial deposit).")
				.setFontSize(10).setFixedLeading(10));
		list.add(item6);

		ListItem item7 = new ListItem();
		item7.add(new Paragraph("This is only a Quotation and not an Acceptance of Risk.").setFontSize(10)
				.setFixedLeading(10));
		list.add(item7);

		ListItem item8 = new ListItem();
		item8.add(new Paragraph(
				"In the case of Death of the child and if no claim has been made on the primary benefit during the policy term, Total premium paid up to date on the Primary Benefit (MSFB) will be refundered.")
						.setFontSize(10).setFixedLeading(10).setFixedLeading(10));
		list.add(item8);

		ListItem item9 = new ListItem();
		// item7.setFixedPosition(20, 50, 450);
		item9.add(new Paragraph(
				"If no claim has been made on the primary benefit during the policy term, total premium paid on the Primary Benefit (MSFB) premium will be refundered at the policy expiry date.")
						.setFontSize(10).setFixedLeading(10).setFixedLeading(10));
		list.add(item9);

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
		document.setTopMargin(50);

		document.add(new Paragraph(" "));

		// Agent Details
		float[] pointColumnWidths1 = { 90, 200 };
		Table agtTable = new Table(pointColumnWidths1);
		agtTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell agCell1 = new Cell();
		agCell1.setBorder(Border.NO_BORDER);
		agCell1.add(new Paragraph("Date").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell1);
		Cell agcell2 = new Cell();
		agcell2.setBorder(Border.NO_BORDER);
		agcell2.add(new Paragraph(": " + date).setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell2);

		agtTable.startNewRow();

		Cell agCellQId = new Cell();
		agCellQId.setBorder(Border.NO_BORDER);
		agCellQId.add(
				new Paragraph("Quotation No ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCellQId);
		Cell agCellQ = new Cell();
		agCellQ.setBorder(Border.NO_BORDER);
		agCellQ.add(new Paragraph(": " + quotationDetails.getQuotation().getId()).setFontSize(9)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCellQ);

		agtTable.startNewRow();

		Cell agCell3 = new Cell();
		agCell3.setBorder(Border.NO_BORDER);
		agCell3.add(
				new Paragraph("Branch Name").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell3);
		Cell agcell4 = new Cell();
		agcell4.setBorder(Border.NO_BORDER);
		agcell4.add(new Paragraph(quotationDetails.getQuotation().getUser().getBranch().getBranch_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getBranch().getBranch_Name()
				: ": ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell4);

		agtTable.startNewRow();

		Cell agCell5 = new Cell();
		agCell5.setBorder(Border.NO_BORDER);
		agCell5.add(
				new Paragraph("Agent Name").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell5);
		Cell agcell6 = new Cell();
		agcell6.setBorder(Border.NO_BORDER);
		agcell6.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Name()
				: ": ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell6);

		agtTable.startNewRow();

		Cell agcell7 = new Cell();
		agcell7.setBorder(Border.NO_BORDER);
		agcell7.add(
				new Paragraph("Agent Code").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell7);

		Cell agcell8 = new Cell();
		if (quotationDetails.getQuotation().getUser().getUserCode() != null) {
			String code = quotationDetails.getQuotation().getUser().getUserCode();
			Integer val = 0;

			try {
				Integer.parseInt(code);
				val = 1;

			} catch (NumberFormatException e) {
				val = 0;
			}

			if (val == 1) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": " + quotationDetails.getQuotation().getUser().getUserCode())
						.setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else if (val == 0) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": ............................ ").setFontSize(10)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(
						new Paragraph(": ").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			}
		} else {

		}

		agtTable.addCell(agcell8);

		agtTable.startNewRow();

		Cell agcell9 = new Cell();
		agcell9.setBorder(Border.NO_BORDER);
		agcell9.add(
				new Paragraph("Contact No").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell9);

		Cell agCell10 = new Cell();
		agCell10.setBorder(Border.NO_BORDER);
		agCell10.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Mobile() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Mobile()
				: ": ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell10);

		document.add(agtTable);
		document.add(new Paragraph(""));

		document.add(new Paragraph("ARPICO RELIEF PLAN").setFontSize(9).setCharacterSpacing(1));

		final SolidLine lineDrawer = new SolidLine(1f);
		document.add(new LineSeparator(lineDrawer));
		document.add(new Paragraph(""));

		// customer Details
		float[] pointColumnWidths2 = { 150, 200 };
		Table cusTable = new Table(pointColumnWidths2);
		cusTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell cucell1 = new Cell();
		cucell1.setBorder(Border.NO_BORDER);
		cucell1.add(new Paragraph("Name").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cucell1);
		Cell cuCell2 = new Cell();
		cuCell2.setBorder(Border.NO_BORDER);
		cuCell2.add(new Paragraph(quotationDetails.getCustomerDetails().getCustName() != null
				? ": " + quotationDetails.getCustomerDetails().getCustName()
				: ": ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCell2);

		cusTable.startNewRow();

		Cell cucell3 = new Cell();
		cucell3.setBorder(Border.NO_BORDER);
		cucell3.add(new Paragraph("Age Next Birthday").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		cusTable.addCell(cucell3);
		Cell cuCell4 = new Cell();
		cuCell4.setBorder(Border.NO_BORDER);
		if (quoCustomer.getMainLifeAge() != null) {
			cuCell4.add(new Paragraph(": " + quoCustomer.getMainLifeAge()).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCell4.add(new Paragraph(": ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCell4);

		cusTable.startNewRow();

		Cell cucell5 = new Cell();
		cucell5.setBorder(Border.NO_BORDER);
		cucell5.add(
				new Paragraph("Occupation").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cucell5);
		Cell cuCell6 = new Cell();
		cuCell6.setBorder(Border.NO_BORDER);
		cuCell6.add(new Paragraph(mainLifeOccupation != null ? ": " + mainLifeOccupation : ": ").setFontSize(9)
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
			cucell7.add(new Paragraph("Name Of Spouse").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cucell7);
			Cell cuCell8 = new Cell();
			cuCell8.setBorder(Border.NO_BORDER);
			cuCell8.add(new Paragraph(": " + quoCustomer.getSpouseName()).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCell8);

		} else {

		}

		cusTable.startNewRow();

		if ((quoCustomer.getSpouseAge()) != null) {

			Cell cucell9 = new Cell();
			cucell9.setBorder(Border.NO_BORDER);
			cucell9.add(new Paragraph("Age Next Birthday (Spouse)").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cucell9);
			Cell cuCell10 = new Cell();
			cuCell10.setBorder(Border.NO_BORDER);
			cuCell10.add(new Paragraph(": " + Integer.toString(quoCustomer.getSpouseAge())).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCell10);

		} else {

		}

		cusTable.startNewRow();

		if ((quoCustomer.getSpouseOccupation()) != null) {

			Cell cucell11 = new Cell();
			cucell11.setBorder(Border.NO_BORDER);
			cucell11.add(new Paragraph("Occupation (Spouse)").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cucell11);
			Cell cuCell12 = new Cell();
			cuCell12.setBorder(Border.NO_BORDER);
			cuCell12.add(new Paragraph(": " + spouseOccupation).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCell12);

		} else {

		}

		document.add(cusTable);

		// creating Quotation Details Tables
		float[] pointColumnWidths5 = { 80, 150 };
		Table DtlTable = new Table(pointColumnWidths5);
		DtlTable.setFixedPosition(400, 605, 230);

		Cell dlcell1 = new Cell();
		dlcell1.setBorder(Border.NO_BORDER);
		dlcell1.add(new Paragraph("Term").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell1);
		Cell dlcell2 = new Cell();
		dlcell2.setBorder(Border.NO_BORDER);
		if (quotationDetails.getPolTerm() != null) {
			dlcell2.add(new Paragraph(": " + Integer.toString(quotationDetails.getPolTerm())).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell2.add(new Paragraph(": ").setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		DtlTable.addCell(dlcell2);

		DtlTable.startNewRow();

		Cell dlcell3 = new Cell();
		dlcell3.setBorder(Border.NO_BORDER);
		dlcell3.add(
				new Paragraph("Paying Term").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell3);
		Cell dlcell4 = new Cell();
		dlcell4.setBorder(Border.NO_BORDER);
		if (quotationDetails.getPolTerm() != null) {
			dlcell4.add(new Paragraph(": " + (quotationDetails.getPaingTerm())).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell4.add(new Paragraph(": ").setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		DtlTable.addCell(dlcell4);

		DtlTable.startNewRow();

		Cell dlcell5 = new Cell();
		dlcell5.setBorder(Border.NO_BORDER);
		dlcell5.add(
				new Paragraph("Paying Method").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
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

			dlcell6.add(new Paragraph(": " + modeMethod).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));

		}
		DtlTable.addCell(dlcell6);

		DtlTable.startNewRow();

		Cell dlcell7 = new Cell();
		dlcell7.setBorder(Border.NO_BORDER);
		dlcell7.add(
				new Paragraph("Mode Premium").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		DtlTable.addCell(dlcell7);
		Cell dlcell8 = new Cell();
		dlcell8.setBorder(Border.NO_BORDER);
		if (quoCustomer.getModePremium() != null) {
			dlcell8.add(new Paragraph(": " + formatter.format(quoCustomer.getTotPremium())).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			dlcell8.add(new Paragraph("").setFontSize(10).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

		}
		DtlTable.addCell(dlcell8);

		document.add(DtlTable);

		// document.add(new Paragraph(""));

		document.add(new Paragraph("Benefits").setFontSize(9).setBold().setUnderline().setCharacterSpacing(1));
		// document.add(new Paragraph(""));

		//////////////////////////// TEST OLD
		//////////////////////////// FORMAT//////////////////////////////////////

		// Create Additional Benefits Table
		/* Declaring column sizes of the table respectively */
		float[] pointColumnWidths4 = { 450, 100, 100, 100, 100, 100, 100 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(new SolidBorder(1));

		// table headings of the Living Benefits
		Cell alCellth1 = new Cell(2, 0);
		alCellth1.setBorder(new SolidBorder(1));
		alCellth1.add(new Paragraph("Living Benefits").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellth1);

		Cell alCellth2 = new Cell(0, 2);
		alCellth2.setBorder(new SolidBorder(1));
		alCellth2.add(new Paragraph("Main Life").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellth2);

		Cell alCellth3 = new Cell(0, 2);
		alCellth3.setBorder(new SolidBorder(1));
		alCellth3.add(new Paragraph("Spouse").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellth3);

		Cell alCellth4 = new Cell(0, 2);
		alCellth4.setBorder(new SolidBorder(1));
		alCellth4.add(new Paragraph("Children").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellth4);

		benAddTable.startNewRow();

		Cell alCellMA = new Cell();
		alCellMA.setBorder(new SolidBorder(1));
		alCellMA.add(new Paragraph("Amount").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellMA);

		Cell alCellMP = new Cell();
		alCellMP.setBorder(new SolidBorder(1));
		alCellMP.add(new Paragraph("Premium").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellMP);

		Cell alCellSA = new Cell();
		alCellSA.setBorder(new SolidBorder(1));
		alCellSA.add(new Paragraph("Amount").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellSA);

		Cell alCellSP = new Cell();
		alCellSP.setBorder(new SolidBorder(1));
		alCellSP.add(new Paragraph("Premium").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellSP);

		Cell alCellCA = new Cell();
		alCellCA.setBorder(new SolidBorder(1));
		alCellCA.add(new Paragraph("Amount").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellCA);

		Cell alCellCP = new Cell();
		alCellCP.setBorder(new SolidBorder(1));
		alCellCP.add(new Paragraph("Premium").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(alCellCP);

		/////////////////// End Of the Table Heading ////////////

		benAddTable.startNewRow();

		// Getting a HashMap to an ArayList
		ArrayList<HashMap<String, Object>> benifList = new ArrayList<>();

		// Checking MainLife Having Benefits
		if (benefitsLife.isEmpty()) {

		} else {

			for (QuoBenf quoBenf : benefitsLife) {

				HashMap<String, Object> benefitDetailMap = new HashMap<>();

				/*
				 * Do not Print Basic Sum Assured,ATPB,ADB and FEB Its Printed on Additional
				 * Benefits Table
				 */
				if (!quoBenf.getRiderCode().equalsIgnoreCase("L2") && (!quoBenf.getRiderCode().equalsIgnoreCase("ATPB"))
						&& (!quoBenf.getRiderCode().equalsIgnoreCase("ADB"))
						&& (!quoBenf.getRiderCode().equalsIgnoreCase("FEB"))) {

					// Cheking the Combination of the benefits by passing Rider Code
					if (benefictDao.findByRiderCode(quoBenf.getRiderCode()) != null) {
						benefitDetailMap.put("combination",
								benefictDao.findByRiderCode(quoBenf.getRiderCode()).getBenefictCombination());

						benefitDetailMap.put("benName", quoBenf.getBenfName());
						benefitDetailMap.put("mainAmt", quoBenf.getRiderSum());
						benefitDetailMap.put("mainPre", quoBenf.getPremium());

						benifList.add(benefitDetailMap);
					}

				}

			}

		}

		// checking Spouse Having Benefits
		if (benefitsSpouse.isEmpty()) {

		} else {
			for (QuoBenf quoBenf : benefitsSpouse) {

				if (!quoBenf.getRiderCode().equalsIgnoreCase("ADBS") && (!quoBenf.getRiderCode().equalsIgnoreCase("SCB")
						&& (!quoBenf.getRiderCode().equalsIgnoreCase("FEBS")))) {
					Integer isAvailable = 0;

					for (HashMap<String, Object> benefitDetailMap : benifList) {

						if (benefitDetailMap.get("combination")
								.equals(benefictDao.findByRiderCode(quoBenf.getRiderCode()).getBenefictCombination())) {
							isAvailable = 1;
							benefitDetailMap.put("spouseAmt", quoBenf.getRiderSum());
							benefitDetailMap.put("spousePre", quoBenf.getPremium());
						}

					}

					/*
					 * If doesn't math with main life benefit combination put new benefits of the
					 * spouse to HASHMAP
					 */
					if (isAvailable == 0) {

						HashMap<String, Object> benefitDetailMap = new HashMap<>();

						benefitDetailMap.put("combination",
								benefictDao.findByRiderCode(quoBenf.getRiderCode()).getBenefictCombination());
						benefitDetailMap.put("benName", quoBenf.getBenfName());
						benefitDetailMap.put("spouseAmt", quoBenf.getRiderSum());
						benefitDetailMap.put("spousePre", quoBenf.getPremium());

						benifList.add(benefitDetailMap);
					}

				}

			}

		}

		// Checking Child having Benefits
		if (benefitsChild.isEmpty()) {

		} else {

			/* Declaring variables to Calculate the total of Premium of all Children */
			Double cibc = 0.0;
			Double hbc = 0.0;
			Double hcbic = 0.0;

			for (QuoChildBenef quoChild : benefitsChild) {

				for (QuoBenf quoChildBenef : quoChild.getBenfs()) {

					if (quoChildBenef.getRiderCode().equalsIgnoreCase("CIBC")) {
						cibc = cibc + quoChildBenef.getPremium();
					} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HBC")) {

						hbc = hbc + quoChildBenef.getPremium();
					} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HCBIC")
							|| quoChildBenef.getRiderCode().equalsIgnoreCase("SHCBIC")) {

						hcbic = hcbic + quoChildBenef.getPremium();

					}

					Integer isAvailable = 0;

					for (HashMap<String, Object> benefitDetailMap : benifList) {

						if (benefitDetailMap.get("combination").equals(
								benefictDao.findByRiderCode(quoChildBenef.getRiderCode()).getBenefictCombination())) {
							isAvailable = 1;
							benefitDetailMap.put("childAmt", quoChildBenef.getRiderSum());

							if (quoChildBenef.getRiderCode().equalsIgnoreCase("CIBC")) {
								benefitDetailMap.put("childPre", cibc);

							} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HBC")) {
								benefitDetailMap.put("childPre", hbc);

							} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HCBIC")
									|| quoChildBenef.getRiderCode().equalsIgnoreCase("SHCBIC")) {
								benefitDetailMap.put("childPre", hcbic);

							}
						}

					}

					/*
					 * If benefits combination of the children not in ArrayList put new child
					 * benefits to the HASHMAP
					 */
					if (isAvailable == 0) {

						HashMap<String, Object> benefitDetailMap = new HashMap<>();

						benefitDetailMap.put("combination",
								benefictDao.findByRiderCode(quoChildBenef.getRiderCode()).getBenefictCombination());
						benefitDetailMap.put("benName", quoChildBenef.getBenfName());
						benefitDetailMap.put("childAmt", quoChildBenef.getRiderSum());

						if (quoChildBenef.getRiderCode().equalsIgnoreCase("CIBC")) {
							benefitDetailMap.put("childPre", cibc);

						} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HBC")) {
							benefitDetailMap.put("childPre", hbc);

						} else if (quoChildBenef.getRiderCode().equalsIgnoreCase("HCBIC")
								|| quoChildBenef.getRiderCode().equalsIgnoreCase("SHCBIC")) {
							benefitDetailMap.put("childPre", hcbic);

						}

						// Adding Full benefits details Hash Map to ArrayList
						benifList.add(benefitDetailMap);
					}
				}
			}

		}

		// Getting Full Benf HashMap using foreach
		for (HashMap<String, Object> hashMap : benifList) {

			Cell alCellBenf = new Cell();

			// Getting ALL Benefits Name object and cast to an String
			String p = (String) hashMap.get("benName");

			// System.out.println("combinationnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn "
			// +hashMap.get("combination"));
			alCellBenf.add(new Paragraph(p).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
			benAddTable.addCell(alCellBenf);

			//
			Cell alCellmA = new Cell();
			if (hashMap.get("mainAmt") != null) {

				// Getting ALL Benefits Name Combinations object and cast to an String
				String comb = (String) hashMap.get("combination");

				/* If benefit is WPB Print Amount as APPLIED */
				if (comb.equalsIgnoreCase("WPB")) {
					alCellmA.add(new Paragraph("Applied").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmA);

				} else {

					Double mAmt = (Double) hashMap.get("mainAmt");

					alCellmA.add(
							new Paragraph(formatter.format(mAmt)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmA);
				}

				// If Main Amount NULL
			} else {

				alCellmA.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellmA);
			}

			Cell alCellmP = new Cell();
			if (hashMap.get("mainPre") != null) {
				Double mPre = (Double) hashMap.get("mainPre");

				alCellmP.add(
						new Paragraph(formatter.format(mPre)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellmP);
			} else {
				alCellmP.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellmP);
			}

			Cell alCellsA = new Cell();

			if (hashMap.get("spouseAmt") != null) {
				String comb = (String) hashMap.get("combination");

				/* If Spouse having WPB or HCBF Spouse Amount will print as APPLIED */
				if (comb.equalsIgnoreCase("WPB") || comb.equalsIgnoreCase("HCBF")) {

					alCellsA.add(new Paragraph("Applied").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellsA);

					// Print Spouse Amount
				} else {
					Double spAmt = (Double) hashMap.get("spouseAmt");

					alCellsA.add(new Paragraph(formatter.format(spAmt)).setFontSize(8)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellsA);
				}

				// IF spouse Amount is null
			} else {
				alCellsA.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsA);
			}

			Cell alCellsP = new Cell();
			if (hashMap.get("spousePre") != null) {
				Double spPre = (Double) hashMap.get("spousePre");

				alCellsP.add(
						new Paragraph(formatter.format(spPre)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsP);
			} else {
				alCellsP.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsP);
			}

			Cell alCellcA = new Cell();
			if (hashMap.get("childAmt") != null) {
				String comb = (String) hashMap.get("combination");

				/* If Children get HCBF Amount Will Print as APPLIED */
				if (comb.equalsIgnoreCase("HCBF")) {

					alCellcA.add(new Paragraph("Applied").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellcA);

					// Display Amount
				} else if (!comb.equalsIgnoreCase("HCBF")) {

					Double cAmt = (Double) hashMap.get("childAmt");

					alCellcA.add(
							new Paragraph(formatter.format(cAmt)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellcA);

				}

				// IF Child Amount is NULL Display
			} else {

				alCellcA.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcA);
			}

			Cell alCellcP = new Cell();
			if (hashMap.get("childPre") != null) {

				Double cPre = (Double) hashMap.get("childPre");

				alCellcP.add(
						new Paragraph(formatter.format(cPre)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcP);

			} else {
				alCellcP.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcP);
			}

			benAddTable.startNewRow();

		}

		////////////////////// table headings of the Additional Benefits
		////////////////////// table\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		Cell abCellth1 = new Cell(2, 0);
		abCellth1.setBorderTop(new SolidBorder(2));
		abCellth1.add(new Paragraph("Additional Benefits").setFontSize(8).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		benAddTable.addCell(abCellth1);

		Cell abCellth2 = new Cell(0, 2);
		abCellth2.setBorderTop(new SolidBorder(2));
		abCellth2.add(new Paragraph("Main Life").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellth2);

		Cell abCellth3 = new Cell(0, 2);
		abCellth3.setBorderTop(new SolidBorder(2));
		abCellth3.add(new Paragraph("Spouse").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellth3);

		Cell abCellth4 = new Cell(0, 2);
		abCellth4.setBorderTop(new SolidBorder(2));
		abCellth4.add(new Paragraph("Children").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellth4);

		benAddTable.startNewRow();

		Cell abCellMA = new Cell();
		abCellMA.add(new Paragraph("Amount").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMA);

		Cell abCellMP = new Cell();
		abCellMP.add(new Paragraph("Premium").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMP);

		Cell abCellSA = new Cell();
		abCellSA.add(new Paragraph("Amount").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellSA);

		Cell abCellSP = new Cell();
		abCellSP.add(new Paragraph("Premium").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellSP);

		Cell abCellCA = new Cell();
		abCellCA.add(new Paragraph("Amount").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellCA);

		Cell abCellCP = new Cell();
		abCellCP.add(new Paragraph("Premium").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellCP);

		benAddTable.startNewRow();

		/*
		 * Declaring Variable for Natural Death Cover/Main/Spouse(Amount/Premium)
		 * Accidental Death benefit/Main/Spouse(Amount/Premium) Funeral
		 * Expenses/Main/Spouse(Amount/Premium)
		 */
		Double ndc = 0.0;
		Double ndcp = 0.0;

		Double ndcs = 0.0;
		Double ndcsp = 0.0;

		Double adb = 0.0;
		Double adbp = 0.0;

		Double adbs = 0.0;
		Double adbsp = 0.0;

		Double feb = 0.0;
		Double febp = 0.0;

		Double febs = 0.0;
		Double febsp = 0.0;

		if (benefitsLife.isEmpty()) {

		} else {

			for (QuoBenf quoAddBenf : benefitsLife) {

				if (quoAddBenf.getRiderCode().equalsIgnoreCase("L2")
						|| (quoAddBenf.getRiderCode().equalsIgnoreCase("ATPB"))) {
					/*
					 * Natural Death Cover = Basic Sum Assured + Additional Term Protection Benefit
					 */
					ndc = ndc + quoAddBenf.getRiderSum();
					ndcp = ndcp + quoAddBenf.getPremium();

				}
				if (quoAddBenf.getRiderCode().equalsIgnoreCase("ADB")) {
					// only Accidental Death Benefit
					adb = quoAddBenf.getRiderSum();
					adbp = adbp + quoAddBenf.getPremium();

				}
				if (quoAddBenf.getRiderCode().equalsIgnoreCase("FEB")) {
					// only Funeral Expenses Benefit
					feb = quoAddBenf.getRiderSum();
					febp = febp + quoAddBenf.getPremium();

				}

			}
		}

		// check Spouse having Benefits
		if (benefitsSpouse.isEmpty()) {

		} else {

			for (QuoBenf quoSpoBenf : benefitsSpouse) {

				if (quoSpoBenf.getRiderCode().equalsIgnoreCase("SCB")) {
					// only Spouse Cover Benefit
					ndcs = quoSpoBenf.getRiderSum();
					ndcsp = quoSpoBenf.getPremium();

				}
				if (quoSpoBenf.getRiderCode().equalsIgnoreCase("ADBS")) {
					// only Accidental Death Benefit
					adbs = quoSpoBenf.getRiderSum();
					adbsp = quoSpoBenf.getPremium();

				}
				if (quoSpoBenf.getRiderCode().equalsIgnoreCase("FEBS")) {
					// only Funeral Expenses Benefit
					febs = quoSpoBenf.getRiderSum();
					febsp = quoSpoBenf.getPremium();

				}

			}

		}

		/* Printing All Additional Benefits Data */
		Cell abCelld1 = new Cell();
		abCelld1.add(new Paragraph("Natural Death Cover").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld1);

		Cell abCelld2 = new Cell();
		abCelld2.add(new Paragraph(formatter.format(ndc)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld2);

		Cell abCelld3 = new Cell();
		abCelld3.add(new Paragraph(formatter.format(ndcp)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld3);

		Cell abCelld4 = new Cell();
		abCelld4.add(new Paragraph(formatter.format(ndcs)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld4);

		Cell abCelld5 = new Cell();
		abCelld5.add(new Paragraph(formatter.format(ndcsp)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld5);

		Cell abCelld6 = new Cell();
		abCelld6.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld6);

		Cell abCelld7 = new Cell();
		abCelld7.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld7);

		benAddTable.startNewRow();

		Cell abCelld8 = new Cell();
		abCelld8.add(new Paragraph("Accidental Death Benefit").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld8);

		Cell abCelld9 = new Cell();
		abCelld9.add(new Paragraph(formatter.format(adb)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld9);

		Cell abCelld10 = new Cell();
		abCelld10.add(new Paragraph(formatter.format(adbp)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld10);

		Cell abCelld11 = new Cell();
		abCelld11.add(new Paragraph(formatter.format(adbs)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld11);

		Cell abCelld12 = new Cell();
		abCelld12.add(new Paragraph(formatter.format(adbsp)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld12);

		Cell abCelld13 = new Cell();
		abCelld13.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld13);

		Cell abCelld14 = new Cell();
		abCelld14.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld14);

		benAddTable.startNewRow();

		Cell abCelld15 = new Cell();
		abCelld15.add(new Paragraph("Funeral Expenses").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld15);

		Cell abCelld16 = new Cell();
		abCelld16.add(new Paragraph(formatter.format(feb)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld16);

		Cell abCelld117 = new Cell();
		abCelld117.add(new Paragraph(formatter.format(febp)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld117);

		Cell abCelld18 = new Cell();
		abCelld18.add(new Paragraph(formatter.format(febs)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld18);

		Cell abCelld19 = new Cell();
		abCelld19.add(new Paragraph(formatter.format(febsp)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld19);

		Cell abCelld20 = new Cell();
		abCelld20.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld20);

		Cell abCelld21 = new Cell();
		abCelld21.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld21);

		///////////////////// End Of Additional Benefits Table
		///////////////////// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		document.add(benAddTable);

		/////////////////////////// END OLD
		/////////////////////////// FORMAT///////////////////////////////////////

		document.add(new Paragraph("* Sum assured increase every year by 2.5%").setFontSize(8));

		//////////////////////////////// Schedule Table\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		try {

			java.util.List<AipPrintShedule> aipshedule = aipPrintSheduleDaoCustom
					.findByQuoDetail(quotationDetails.getQdId());

			if (aipshedule.isEmpty()) {

			} else {

				// Policy Summary Details
				float[] pointColumnWidths6 = { 70, 150, 150, 150, 150 };
				Table polSmyTable = new Table(pointColumnWidths6);
				polSmyTable.setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(new SolidBorder(1));

				Cell psCell1 = new Cell(2, 0);
				psCell1.setBorder(new SolidBorder(1));
				psCell1.add(new Paragraph("Year").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
						.setCharacterSpacing(1));
				polSmyTable.addCell(psCell1);
				Cell psCell2 = new Cell(2, 0);
				psCell2.setBorder(new SolidBorder(1));
				psCell2.add(new Paragraph("Premium per year for Basic Policy").setFontSize(8).setBold()
						.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
				polSmyTable.addCell(psCell2);
				Cell psCell3 = new Cell(2, 0);
				psCell3.setBorder(new SolidBorder(1));
				psCell3.add(new Paragraph("Total Premium Paid per year").setFontSize(8).setBold()
						.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
				polSmyTable.addCell(psCell3);
				Cell psCell4 = new Cell(0, 2);
				psCell4.setBorderLeft(new SolidBorder(1));
				psCell4.setBorderBottom(new SolidBorder(1));
				psCell4.add(new Paragraph("Minimum Life Benefits").setFontSize(8).setBold()
						.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
				polSmyTable.addCell(psCell4);

				polSmyTable.startNewRow();

				Cell psCell5 = new Cell();
				psCell5.setBorderLeft(new SolidBorder(1));
				psCell5.setBorderRight(new SolidBorder(1));
				psCell5.setBorderBottom(new SolidBorder(1));
				psCell5.add(new Paragraph("Protection against Natural Death").setFontSize(8).setBold()
						.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
				polSmyTable.addCell(psCell5);
				Cell psCell6 = new Cell();
				psCell6.setBorderLeft(new SolidBorder(1));
				psCell6.setBorderBottom(new SolidBorder(1));
				psCell6.add(new Paragraph("Protection against Accident Death").setFontSize(8).setBold()
						.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
				polSmyTable.addCell(psCell6);

				polSmyTable.startNewRow();

				// Loop all summary details
				for (AipPrintShedule scheduleVals : aipshedule) {

					// new decimal format with 2 decimal places
					DecimalFormat formatVal = new DecimalFormat("###,###.00");

					Cell psCell7 = new Cell();

					if (scheduleVals.getPolyer() != null) {

						psCell7.add(new Paragraph(Integer.toString(scheduleVals.getPolyer())).setFontSize(8)
								.setTextAlignment(TextAlignment.CENTER));

					} else {
						psCell7.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.CENTER));
					}

					polSmyTable.addCell(psCell7);

					Cell psCell8 = new Cell();
					if (scheduleVals.getRdrprm() != null) {

						// if value is 0.0 print relief
						if (scheduleVals.getPrmpad() == 0.0) {
							psCell8.add(new Paragraph("Relief").setFontSize(8).setTextAlignment(TextAlignment.CENTER));

							// print value
						} else {
							psCell8.add(new Paragraph(formatVal.format(scheduleVals.getRdrprm())).setFontSize(8)
									.setTextAlignment(TextAlignment.CENTER));
						}

						// if null print '-'
					} else {
						psCell8.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.CENTER));
					}

					polSmyTable.addCell(psCell8);

					Cell psCell9 = new Cell();
					if (scheduleVals.getPrmpad() != null) {

						if (scheduleVals.getPrmpad() == 0.0) {
							psCell9.add(new Paragraph("Relief").setFontSize(8).setTextAlignment(TextAlignment.CENTER));

						} else {
							psCell9.add(new Paragraph(formatVal.format(scheduleVals.getPrmpad())).setFontSize(8)
									.setTextAlignment(TextAlignment.CENTER));

						}

					} else {
						psCell9.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.CENTER));

					}
					polSmyTable.addCell(psCell9);

					Cell psCell10 = new Cell();
					if (scheduleVals.getMlbpad() != null) {

						if (scheduleVals.getMlbpad() == 0.0) {
							psCell10.add(new Paragraph("Relief").setFontSize(8).setTextAlignment(TextAlignment.CENTER));

						} else {
							psCell10.add(new Paragraph(formatVal.format(scheduleVals.getMlbpad())).setFontSize(8)
									.setTextAlignment(TextAlignment.CENTER));
						}

					} else {
						psCell10.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.CENTER));
					}
					polSmyTable.addCell(psCell10);

					Cell psCell11 = new Cell();

					if (scheduleVals.getMlbad() != null) {

						if (scheduleVals.getMlbad() == 0.0) {
							psCell11.add(new Paragraph("Relief").setFontSize(8).setTextAlignment(TextAlignment.CENTER));

						} else {
							psCell11.add(new Paragraph(formatVal.format(scheduleVals.getMlbad())).setFontSize(8)
									.setTextAlignment(TextAlignment.CENTER));
						}

					} else {
						psCell11.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.CENTER));
					}

					polSmyTable.addCell(psCell11);

					polSmyTable.startNewRow();

				}

				document.add(polSmyTable);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		//////////////////////////////// End of Schedule
		//////////////////////////////// Table\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

		// document.add(new Paragraph(""));

		for (QuoBenf maturity : benefitsLife) {

			if (maturity.getRiderCode().equalsIgnoreCase("L1")) {
				document.add(new Paragraph("Guranteed Maturity : " + formatter.format(maturity.getRiderSum()))
						.setFontSize(8));
			}

		}

		// Medical Requirements
		try {
			java.util.List<MedicalRequirementsHelper> medicalDetails = medicalRequirementsDaoCustom
					.findByQuoDetail(quotationDetails.getQdId());

			if (medicalDetails.isEmpty()) {

			} else {
				document.add(new Paragraph(""));

				document.add(new Paragraph("Medical Requirements").setBold().setFontSize(8)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));

				document.add(new Paragraph(""));

				// Medical Requirements Table
				float[] pointColumnWidths7 = { 150, 100, 100 };
				Table medReqTable = new Table(pointColumnWidths7);
				medReqTable.setBorder(new SolidBorder(1));
				medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

				Cell mrqCell2 = new Cell();
				mrqCell2.setBorder(new SolidBorder(1));
				mrqCell2.add(new Paragraph("Requirements").setBold().setFontSize(8).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell2);

				Cell mrqCell3 = new Cell();
				mrqCell3.setBorder(new SolidBorder(1));
				mrqCell3.add(new Paragraph("Main Life").setBold().setFontSize(8).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell3);

				Cell mrqCell4 = new Cell();
				mrqCell4.setBorder(new SolidBorder(1));
				mrqCell4.add(new Paragraph("Spouse").setBold().setFontSize(8).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10).setCharacterSpacing(1));
				medReqTable.addCell(mrqCell4);

				medReqTable.startNewRow();
				///////
				for (MedicalRequirementsHelper medicalReq : medicalDetails) {

					Cell mrqCell5 = new Cell();
					mrqCell5.add(new Paragraph(medicalReq.getMedicalReqname()).setFontSize(8)
							.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
					medReqTable.addCell(mrqCell5);

					Cell mrqCell6 = new Cell();
					mrqCell6.add(new Paragraph(medicalReq.getMainStatus()).setFontSize(8)
							.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
					medReqTable.addCell(mrqCell6);

					Cell mrqCell7 = new Cell();
					mrqCell7.add(new Paragraph(medicalReq.getSpouseStatus()).setFontSize(8)
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
		// document.add(new Paragraph(""));

		document.add(new Paragraph("Special Notes").setFontSize(8).setBold().setUnderline().setCharacterSpacing(1));

		document.add(new Paragraph(""));

		// Creating a Special Notes List
		List list = new List(ListNumberingType.DECIMAL);
		list.setFontSize(10);
		ListItem item1 = new ListItem();
		item1.add(new Paragraph(
				"If HRB / SUHRB is obtained, the total cover will be applicable for the whole family per policy year.")
						.setFontSize(8).setFixedLeading(5));
		list.add(item1);

		ListItem item2 = new ListItem();
		item2.add(
				new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.")
						.setFontSize(8).setFixedLeading(5));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date of issue.")
				.setFontSize(8).setFixedLeading(5));
		list.add(item3);

		ListItem item4 = new ListItem();
		item4.add(new Paragraph("Abbrevations ; Y - Yes, R - Required, NR - Not Required").setFontSize(8)
				.setFixedLeading(5));
		list.add(item4);

		ListItem item5 = new ListItem();
		item5.add(new Paragraph("All amounts are in Sri Lankan Rupees (LKR).").setFontSize(8).setFixedLeading(5));
		list.add(item5);

		ListItem item6 = new ListItem();
		item6.add(new Paragraph("Initial policy processing fee of Rs 300 (Payable only with initial deposit).")
				.setFontSize(8).setFixedLeading(5));
		list.add(item6);

		document.add(list);

		// document.add(new Paragraph("\n"));
		document.add(new Paragraph("This is a system generated report and therefore does not require a signature.")
				.setFontSize(8).setBold());

		document.close();

		return baos.toByteArray();
	}

}
