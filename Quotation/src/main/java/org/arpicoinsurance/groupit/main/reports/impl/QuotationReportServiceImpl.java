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

import com.itextpdf.kernel.colors.ColorConstants;
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
import com.itextpdf.layout.property.VerticalAlignment;

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

		/*
		 * PdfWriter writer = new PdfWriter(baos); PdfDocument pdf = new
		 * PdfDocument(writer); Document document = new Document(pdf, PageSize.A4);
		 * document.setTopMargin(150);
		 */

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

		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);
		document.setTopMargin(120);
		document.setBottomMargin(10);

		// Agent Details
		float[] pointColumnWidths1 = { 90, 150 };
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
				agcell8.add(new Paragraph(": " + quotationDetails.getQuotation().getUser().getUserCode()).setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else if (val == 0) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": ............................ ").setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(
						new Paragraph(": ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
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
		///////////// End Of Agent Table//////////////////////

		document.add(new Paragraph(""));

		// sub header
		document.add(new Paragraph("ARPICO INVESTMENT PLUS").setFontSize(9).setCharacterSpacing(1));
		final SolidLine lineDrawer = new SolidLine(1f);
		document.add(new LineSeparator(lineDrawer));
		document.add(new Paragraph(""));

		// customer/Plan Details
		float[] pointColumnWidths2 = { 300, 150, 100, 130, 100 };
		Table cusTable = new Table(pointColumnWidths2);
		cusTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		///////////////////////// *Craeting Main Life Details*/
		Cell cuCellTh = new Cell();
		cuCellTh.setBorder(Border.NO_BORDER);
		cuCellTh.add(new Paragraph("Main Life Details").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cuCellTh);

		Cell cucellTh1 = new Cell();
		cucellTh1.setBorder(Border.NO_BORDER);
		cucellTh1.add(new Paragraph("Occupation").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh1);

		Cell cucellTh2 = new Cell();
		cucellTh2.setBorder(Border.NO_BORDER);
		cucellTh2.add(
				new Paragraph("DOB").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh2);

		Cell cucellTh3 = new Cell();
		cucellTh3.setBorder(Border.NO_BORDER);
		cucellTh3.add(
				new Paragraph("Age").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh3);

		Cell cucellTh4 = new Cell();
		cucellTh4.setBorder(Border.NO_BORDER);
		cucellTh4.add(new Paragraph("Gender").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cucellTh4);
		cusTable.startNewRow();

		Cell cuCellM1 = new Cell();
		cuCellM1.setBorder(Border.NO_BORDER);
		cuCellM1.add(new Paragraph(quotationDetails.getCustomerDetails().getCustName() != null
				? quotationDetails.getCustomerDetails().getCustName()
				: " ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM1);

		Cell cuCellM2 = new Cell();
		cuCellM2.setBorder(Border.NO_BORDER);
		cuCellM2.add(new Paragraph(mainLifeOccupation != null ? mainLifeOccupation : " ").setFontSize(9)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM2);

		Cell cuCellM3 = new Cell();
		cuCellM3.setBorder(Border.NO_BORDER);

		// Creating a Date Format for DOB
		SimpleDateFormat mainDob = new SimpleDateFormat("dd-MM-YYY");

		cuCellM3.add(new Paragraph(quotationDetails.getCustomerDetails().getCustDob() != null
				? mainDob.format(quotationDetails.getCustomerDetails().getCustDob())
				: " ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM3);

		Cell cuCellM4 = new Cell();
		cuCellM4.setBorder(Border.NO_BORDER);
		if (quoCustomer.getMainLifeAge() != null) {
			cuCellM4.add(new Paragraph(Integer.toString(quoCustomer.getMainLifeAge())).setFontSize(8)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCellM4.add(new Paragraph(" ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCellM4);

		Cell cuCellM5 = new Cell();
		cuCellM5.setBorder(Border.NO_BORDER);

		// Check MainLife Gender If M->Male, F->Female
		if (quotationDetails.getCustomerDetails().getCustGender() != null) {

			if (quotationDetails.getCustomerDetails().getCustGender().equalsIgnoreCase("M")) {
				cuCellM5.add(
						new Paragraph("Male").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			} else if (quotationDetails.getCustomerDetails().getCustGender().equalsIgnoreCase("F")) {
				cuCellM5.add(new Paragraph("Female").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		} else {

		}
		cusTable.addCell(cuCellM5);

		/////////////////////////// *End Craeting Main Life Details*/

		cusTable.startNewRow();

		// Create an Empty Line
		Cell cuCellEmptyP1 = new Cell(0, 5);
		cuCellEmptyP1.setBorder(Border.NO_BORDER);
		cuCellEmptyP1.add(new Paragraph("").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellEmptyP1);

		cusTable.startNewRow();

		// Create an Empty Line
		Cell cuCellEmptyP2 = new Cell(0, 5);
		cuCellEmptyP2.setBorder(Border.NO_BORDER);
		cuCellEmptyP2.add(new Paragraph("").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellEmptyP2);

		cusTable.startNewRow();

		// Plan Details Heading
		Cell cuCellThP = new Cell(0, 5);
		cuCellThP.setBorder(Border.NO_BORDER);
		cuCellThP.add(new Paragraph("Plan Details").setFontSize(9).setBold().setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10));
		cusTable.addCell(cuCellThP);

		cusTable.startNewRow();

		//////////////////// * Strat Creating Plan Details*/
		Cell cuCellPlanTh = new Cell();
		cuCellPlanTh.add(new Paragraph("Term").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cuCellPlanTh);

		Cell cuCellP1 = new Cell();
		cuCellP1.add(new Paragraph("Contribution").setFontSize(9).setTextAlignment(TextAlignment.CENTER)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cuCellP1);

		Cell cuCellP2 = new Cell();
		cuCellP2.add(new Paragraph("Method").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cuCellP2);

		Cell cuCellP3 = new Cell();
		cuCellP3.add(new Paragraph("Admin Fee / Cess ").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setBold()
				.setFixedLeading(10));
		cusTable.addCell(cuCellP3);

		Cell cuCellP4 = new Cell();
		cuCellP4.add(new Paragraph("Total Premium").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setBold()
				.setFixedLeading(10));
		cusTable.addCell(cuCellP4);

		cusTable.startNewRow();

		// Display Term
		Cell cuCellP5 = new Cell();
		if (quotationDetails.getPolTerm() != null) {
			cuCellP5.add(new Paragraph(Integer.toString(quotationDetails.getPolTerm())).setFontSize(9)
					.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		} else {
			cuCellP5.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		}
		cusTable.addCell(cuCellP5);

		// Display ModePremium
		Cell cuCellP6 = new Cell();
		if (quoCustomer.getModePremium() != null) {
			cuCellP6.add(new Paragraph(formatter.format(quoCustomer.getModePremium())).setFontSize(9)
					.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		} else {
			cuCellP6.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		}
		cusTable.addCell(cuCellP6);

		// Display paying Method
		Cell cuCellP7 = new Cell();
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

			cuCellP7.add(new Paragraph(modeMethod).setFontSize(9).setTextAlignment(TextAlignment.CENTER)
					.setFixedLeading(10));

		}
		cusTable.addCell(cuCellP7);

		// Calculate Tax Amount Double Tax + Double Admin Fee
		Cell cuCellP8 = new Cell();
		if (quotationDetails.getTaxAmount() != null) {
			cuCellP8.add(
					new Paragraph(formatter.format(quotationDetails.getTaxAmount() + quotationDetails.getAdminFee()))
							.setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		} else {
			cuCellP8.add(new Paragraph(" ").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));

		}
		cusTable.addCell(cuCellP8);

		// Display Total Premium
		Cell cuCellP9 = new Cell();

		if (quoCustomer.getTotPremium() != null) {
			cuCellP9.add(new Paragraph(formatter.format(quoCustomer.getTotPremium())).setFontSize(9)
					.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));

		} else {

			cuCellP7.add(new Paragraph(" ").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));

		}

		cusTable.addCell(cuCellP9);

		//////////////////// * End of Plan Details*/

		document.add(cusTable);
		//////////////////////////////// *End of MainLife/Spouse/Plan Details
		//////////////////////////////// Table*//////////

		document.add(new Paragraph("\n"));

		document.add(new Paragraph("Benefits").setFontSize(10).setBold().setUnderline().setCharacterSpacing(1));

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
				.setFontSize(9));
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
		abCell2.add(new Paragraph("\n"));
		abCell2.add(new Paragraph("Natural Death").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCell2);

		Cell abCell3 = new Cell();
		abCell3.add(new Paragraph(
				"Up to 3 years from the commencement date, " + "premium paid or account balance whichever is higher & "
						+ "after 3 years from the commencement date, "
						+ "account balance or 125% of the premium paid, whichever is higher.").setFontSize(9)
								.setTextAlignment(TextAlignment.JUSTIFIED));
		benAddTable.addCell(abCell3);

		/*
		 * Cell abCell3 = new Cell(); abCell3.add(new
		 * Paragraph("Twice the value of premium paid or account balance which ever is higher"
		 * ) .setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		 * benAddTable.addCell(abCell3);
		 */

		benAddTable.startNewRow();

		Cell abCell4 = new Cell();
		abCell4.add(new Paragraph("\n"));
		abCell4.add(new Paragraph("Accidental Death").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCell4);

		Cell abCell5 = new Cell();
		abCell5.add(new Paragraph(
				"Twice the value of premium paid, maximum up to Rs. 1,000,000 or account balance whichever is higher. "
						+ "If Accident death occurs age over 65 years company will pay premium paid or account balance whichever is higher.")
								.setFontSize(9).setTextAlignment(TextAlignment.JUSTIFIED));
		benAddTable.addCell(abCell5);

		/*
		 * Cell abCell5 = new Cell(); abCell5.add(new
		 * Paragraph("Account balance or 125% of the premium paid, which ever is higher"
		 * ).setFontSize(9) .setTextAlignment(TextAlignment.LEFT));
		 * benAddTable.addCell(abCell5);
		 */

		document.add(benAddTable);

		document.add(new Paragraph("\n"));

		document.add(new Paragraph("Special Notes").setFontSize(9).setBold().setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		// Creating a Special Notes List
		List list = new List(ListNumberingType.DECIMAL);
		list.setFontSize(9);

		/*
		 * ListItem item1 = new ListItem(); item1.add(new
		 * Paragraph("This is an indicative quote only and is valid for 31 May 2018.")
		 * .setFontSize(10).setFixedLeading(10)); list.add(item1);
		 */

		ListItem item1 = new ListItem();
		item1.add(new Paragraph("All Amounts are in Sri Lankan Rupees (LKR).").setFontSize(9).setFixedLeading(10));
		list.add(item1);
		/*
		 * ListItem item1 = new ListItem(); item1.add(new
		 * Paragraph("This is an indicative quote only and is valid for 31 May 2018.")
		 * .setFontSize(10).setFixedLeading(10)); list.add(item1);
		 */
		ListItem item2 = new ListItem();
		item2.add(new Paragraph("Initial policy processing fee of Rs 300 (Payable only with initial deposit).")
				.setFontSize(9).setFixedLeading(10));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("This is an indicative quote only and is valid for 30 days from date of issue.")
				.setFontSize(9).setFixedLeading(10));
		list.add(item3);

		document.add(list);

		document.add(new Paragraph("This is a system generated report and therefore does not require a signature.")
				.setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setFixedPosition(50, 10, 500));

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
		document.setTopMargin(30);
		document.setBottomMargin(10);

		// Agent Details
		float[] pointColumnWidths1 = { 90, 150 };
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
				agcell8.add(new Paragraph(": " + quotationDetails.getQuotation().getUser().getUserCode()).setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else if (val == 0) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": ............................ ").setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(
						new Paragraph(": ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
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

		// checking arpico investment plan or super investment plan
		if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("INVP")) {
			document.add(new Paragraph("ARPICO INVESTMENT PLAN").setFontSize(9).setCharacterSpacing(1));
		}

		final SolidLine lineDrawer = new SolidLine(1f);
		document.add(new LineSeparator(lineDrawer));
		document.add(new Paragraph(""));

		// customer/Spouse/Plan Details
		float[] pointColumnWidths2 = { 300, 150, 100, 100, 130 };
		Table cusTable = new Table(pointColumnWidths2);
		cusTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		///////////////////////// *Craeting Main Life Details*/
		Cell cuCellTh = new Cell();
		cuCellTh.setBorder(Border.NO_BORDER);
		cuCellTh.add(new Paragraph("Main Life Details").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cuCellTh);

		Cell cucellTh1 = new Cell();
		cucellTh1.setBorder(Border.NO_BORDER);
		cucellTh1.add(new Paragraph("Occupation").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh1);

		Cell cucellTh2 = new Cell();
		cucellTh2.setBorder(Border.NO_BORDER);
		cucellTh2.add(
				new Paragraph("DOB").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh2);

		Cell cucellTh3 = new Cell();
		cucellTh3.setBorder(Border.NO_BORDER);
		cucellTh3.add(
				new Paragraph("Age").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh3);

		Cell cucellTh4 = new Cell();
		cucellTh4.setBorder(Border.NO_BORDER);
		cucellTh4.add(new Paragraph("Gender").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cucellTh4);
		cusTable.startNewRow();

		Cell cuCellM1 = new Cell();
		cuCellM1.setBorder(Border.NO_BORDER);
		cuCellM1.add(new Paragraph(quotationDetails.getCustomerDetails().getCustName() != null
				? quotationDetails.getCustomerDetails().getCustName()
				: " ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM1);

		Cell cuCellM2 = new Cell();
		cuCellM2.setBorder(Border.NO_BORDER);
		cuCellM2.add(new Paragraph(mainLifeOccupation != null ? mainLifeOccupation : " ").setFontSize(9)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM2);

		Cell cuCellM3 = new Cell();
		cuCellM3.setBorder(Border.NO_BORDER);

		// Creating a Date Format for DOB
		SimpleDateFormat mainDob = new SimpleDateFormat("dd-MM-yyyy");

		cuCellM3.add(new Paragraph(quotationDetails.getCustomerDetails().getCustDob() != null
				? mainDob.format(quotationDetails.getCustomerDetails().getCustDob())
				: " ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM3);

		Cell cuCellM4 = new Cell();
		cuCellM4.setBorder(Border.NO_BORDER);
		if (quoCustomer.getMainLifeAge() != null) {
			cuCellM4.add(new Paragraph(Integer.toString(quoCustomer.getMainLifeAge())).setFontSize(8)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCellM4.add(new Paragraph(" ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCellM4);

		Cell cuCellM5 = new Cell();
		cuCellM5.setBorder(Border.NO_BORDER);

		// Check MainLife Gender If M->Male, F->Female
		if (quotationDetails.getCustomerDetails().getCustGender() != null) {

			if (quotationDetails.getCustomerDetails().getCustGender().equalsIgnoreCase("M")) {
				cuCellM5.add(
						new Paragraph("Male").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			} else if (quotationDetails.getCustomerDetails().getCustGender().equalsIgnoreCase("F")) {
				cuCellM5.add(new Paragraph("Female").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		} else {

		}
		cusTable.addCell(cuCellM5);

		/////////////////////////// *End Craeting Main Life Details*/

		cusTable.startNewRow();

		// Create an Empty Line
		Cell cuCellEmptyM = new Cell(0, 5);
		cuCellEmptyM.setBorder(Border.NO_BORDER);
		cuCellEmptyM.add(new Paragraph("").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellEmptyM);

		//////////////////////// * Strat Creating Spouse Details*/

		// checking Spouse is active or not
		if ((quoCustomer.getSpouseName()) != null) {

			Cell cuCellThS = new Cell();
			cuCellThS.setBorder(Border.NO_BORDER);
			cuCellThS.add(new Paragraph("Spouse Details").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10).setBold());
			cusTable.addCell(cuCellThS);

			cusTable.startNewRow();

			Cell cuCellS1 = new Cell();
			cuCellS1.setBorder(Border.NO_BORDER);
			cuCellS1.add(new Paragraph(quoCustomer.getSpouseName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCellS1);

		} else {

		}

		if ((quoCustomer.getSpouseOccupation()) != null) {

			Cell cuCellS2 = new Cell();
			cuCellS2.setBorder(Border.NO_BORDER);
			cuCellS2.add(new Paragraph(spouseOccupation).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCellS2);

		} else {

		}

		if (quotationDetails.getSpouseDetails() != null) {

			Cell cuCellS3 = new Cell();
			cuCellS3.setBorder(Border.NO_BORDER);
			cuCellS3.add(new Paragraph(mainDob.format(quotationDetails.getSpouseDetails().getCustDob())).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCellS3);

		} else {

		}

		if ((quoCustomer.getSpouseAge()) != null) {

			Cell cuCellS4 = new Cell();
			cuCellS4.setBorder(Border.NO_BORDER);
			cuCellS4.add(new Paragraph(Integer.toString(quoCustomer.getSpouseAge())).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCellS4);

		} else {

		}

		// Display Spouse Gender
		Cell cuCellS5 = new Cell();
		cuCellS5.setBorder(Border.NO_BORDER);

		if ((quotationDetails.getSpouseDetails()) != null) {

			if (quotationDetails.getSpouseDetails().getCustGender().equalsIgnoreCase("M")) {
				cuCellS5.add(
						new Paragraph("Male").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			} else if (quotationDetails.getSpouseDetails().getCustGender().equalsIgnoreCase("F")) {
				cuCellS5.add(new Paragraph("Female").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		} else {

		}

		cusTable.addCell(cuCellS5);

		/////////////////////////////////// * End of Spouse Details*/

		cusTable.startNewRow();

		// Create an Empty Line
		Cell cuCellEmptyP = new Cell(0, 5);
		cuCellEmptyP.setBorder(Border.NO_BORDER);
		cuCellEmptyP.add(new Paragraph("").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellEmptyP);

		//////////////////// * Strat Creating Plan Details*/
		Cell cuCellPlanTh = new Cell();
		cuCellPlanTh.add(new Paragraph("Plan Details").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cuCellPlanTh);

		Cell cuCellP1 = new Cell();
		cuCellP1.add(new Paragraph("Term").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cuCellP1);

		Cell cuCellP2 = new Cell();
		cuCellP2.add(new Paragraph("Premium").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cuCellP2);

		Cell cuCellP3 = new Cell();
		cuCellP3.add(new Paragraph("Method").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setBold()
				.setFixedLeading(10));
		cusTable.addCell(cuCellP3);

		Cell cuCellP4 = new Cell();
		cuCellP4.add(new Paragraph("Basic Sum Assured").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setBold()
				.setFixedLeading(10));
		cusTable.addCell(cuCellP4);

		cusTable.startNewRow();

		// Display Product Code
		Cell cuCellP5 = new Cell();
		if (quotationDetails.getQuotation().getProducts().getProductCode() != null) {
			cuCellP5.add(new Paragraph(quotationDetails.getQuotation().getProducts().getProductCode()).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCellP5.add(new Paragraph(" ").setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCellP5);

		Cell cuCellP6 = new Cell();
		if (quotationDetails.getPolTerm() != null) {
			cuCellP6.add(new Paragraph(Integer.toString(quotationDetails.getPolTerm())).setFontSize(9)
					.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		} else {
			cuCellP6.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		}
		cusTable.addCell(cuCellP6);

		Cell cuCellP7 = new Cell();
		if (quoCustomer.getModePremium() != null) {
			cuCellP7.add(new Paragraph(formatter.format(quoCustomer.getTotPremium())).setFontSize(9)
					.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		} else {
			cuCellP7.add(new Paragraph(" ").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));

		}
		cusTable.addCell(cuCellP7);

		Cell cuCellP8 = new Cell();

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

			cuCellP8.add(new Paragraph(modeMethod).setFontSize(9).setTextAlignment(TextAlignment.CENTER)
					.setFixedLeading(10));

		}
		cusTable.addCell(cuCellP8);

		// Display Basic Sum Assured
		Cell cuCellP9 = new Cell();

		if (benefitsLife.isEmpty()) {

		} else {

			for (QuoBenf bsa : benefitsLife) {
				System.out.println(bsa.getRiderCode());

				if (bsa.getRiderCode().equalsIgnoreCase("L2")) {

					if (bsa.getRiderSum() != null) {
						cuCellP9.add(new Paragraph(formatter.format(bsa.getRiderSum())).setFontSize(9)
								.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));

						cusTable.addCell(cuCellP9);

					} else {

					}

				}

			}

		}

		//////////////////// * End of Plan Details*/

		document.add(cusTable);
		//////////////////////////////// *End of MainLife/Spouse/Plan Details
		//////////////////////////////// Table*//////////

		document.add(new Paragraph("Benefits").setFontSize(9).setBold().setUnderline().setCharacterSpacing(1));

		//////////////////////////// Benefits Table
		//////////////////////////// FORMAT//////////////////////////////////////

		// Create Additional Benefits Table
		/* Declaring column sizes of the table respectively */
		float[] pointColumnWidths4 = { 500, 80, 80, 80, 80, 80, 80 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		// table headings of the Living Benefits
		Cell alCellth1 = new Cell(2, 0);
		alCellth1.setBorder(new SolidBorder(1));
		alCellth1.add(new Paragraph("Living Benefits").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1).setMarginTop(10));
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
			alCellBenf.setBorderLeft(new SolidBorder(1));

			// Getting ALL Benefits Name object and cast to an String
			String p = (String) hashMap.get("benName");
			String maturity = (String) hashMap.get("combination");

			System.out.println("Combinationnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn " + maturity.equalsIgnoreCase("L3"));

			// Check Maturity values not equl then Display
			if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {

				alCellBenf.add(new Paragraph(p).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(alCellBenf);
			}

			// Display Main Life Rider Amounts
			Cell alCellmA = new Cell();
			if (hashMap.get("mainAmt") != null) {

				// Getting ALL Benefits Name Combinations object and cast to an String
				String comb = (String) hashMap.get("combination");

				/* If benefit is WPB Print Amount as APPLIED */
				if (comb.equalsIgnoreCase("WPB")) {
					alCellmA.add(new Paragraph("Applied").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmA);

					// Check Maturity values not equl then Display
				} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
						&& !maturity.equalsIgnoreCase("L5")) {

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

			// Display Main Life Rider Premium
			Cell alCellmP = new Cell();
			if (hashMap.get("mainPre") != null) {

				if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
						&& !maturity.equalsIgnoreCase("L5")) {
					Double mPre = (Double) hashMap.get("mainPre");

					alCellmP.add(
							new Paragraph(formatter.format(mPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmP);
				}

			} else {
				alCellmP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellmP);
			}

			// Display Spouse Rider Amounts
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

				// IF spouse Rider Not Equals Maturity Values and Null
			} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {
				alCellsA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsA);
			}

			// Display Spouse Rider Premium
			Cell alCellsP = new Cell();
			if (hashMap.get("spousePre") != null) {
				Double spPre = (Double) hashMap.get("spousePre");
				alCellsP.add(
						new Paragraph(formatter.format(spPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsP);

			} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {
				alCellsP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsP);

			}

			// Display Child Rider Amounts
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

				// IF Child Riders Are not equal Maturities and Amount is null
			} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {

				alCellcA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcA);

			}

			// Display Child Riders Premium
			Cell alCellcP = new Cell();
			alCellcP.setBorderRight(new SolidBorder(1));

			if (hashMap.get("childPre") != null) {
				Double cPre = (Double) hashMap.get("childPre");

				alCellcP.add(
						new Paragraph(formatter.format(cPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcP);

				// IF Child Premiums Are not equal Maturities and Premium is null
			} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {
				alCellcP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcP);

			}

		}

		benAddTable.startNewRow();

		// Creating Empty Cell to seperate Benefits Table from Maturity table
		Cell abCellMat = new Cell(0, 7);
		abCellMat.setBorder(Border.NO_BORDER);
		abCellMat.setBorderTop(new SolidBorder(1));
		abCellMat.add(new Paragraph(" ").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMat);

		benAddTable.startNewRow();

		// Craeting Maturity Values Table
		Cell abCellMat1 = new Cell();
		abCellMat1.setBorder(new SolidBorder(1));
		abCellMat1.add(new Paragraph("Assumed Annual Dividend Rate of").setFontSize(9).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		benAddTable.addCell(abCellMat1);

		Cell abCellMat2 = new Cell(0, 2);
		abCellMat2.setBorder(new SolidBorder(1));

		abCellMat2.add(new Paragraph("8%").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMat2);

		Cell abCellMat3 = new Cell(0, 2);
		abCellMat3.setBorder(new SolidBorder(1));

		abCellMat3.add(new Paragraph("10%").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMat3);

		Cell abCellMat4 = new Cell(0, 2);
		abCellMat4.setBorder(new SolidBorder(1));
		abCellMat4.add(new Paragraph("12%").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMat4);

		benAddTable.startNewRow();

		Cell abCellMat5 = new Cell();
		abCellMat5.setBorderLeft(new SolidBorder(1));

		abCellMat5.add(new Paragraph("Illustrated Maturity Values").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMat5);

		// Fetch array to get Maturity Values
		for (QuoBenf matValue : benefitsLife) {

			if (matValue.getRiderCode().equalsIgnoreCase("L3")) {

				Cell abCellMat6 = new Cell(0, 2);
				if (matValue.getRiderSum() == 0.0) {
					abCellMat6.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT)
							.setCharacterSpacing(1));

				} else {
					abCellMat6.add(new Paragraph(formatter.format(matValue.getRiderSum())).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT).setCharacterSpacing(1));

				}
				benAddTable.addCell(abCellMat6);

			} else {

			}

			if (matValue.getRiderCode().equalsIgnoreCase("L4")) {

				Cell abCellMat7 = new Cell(0, 2);
				if (matValue.getRiderSum() == 0.0) {
					abCellMat7.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT)
							.setCharacterSpacing(1));

				} else {
					abCellMat7.add(new Paragraph(formatter.format(matValue.getRiderSum())).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT).setCharacterSpacing(1));

				}
				benAddTable.addCell(abCellMat7);

			} else {

			}

			if (matValue.getRiderCode().equalsIgnoreCase("L5")) {

				Cell abCellMat8 = new Cell(0, 2);
				abCellMat8.setBorderRight(new SolidBorder(1));
				if (matValue.getRiderSum() == 0.0) {
					abCellMat8.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT)
							.setCharacterSpacing(1));
				} else {
					abCellMat8.add(new Paragraph(formatter.format(matValue.getRiderSum())).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT).setCharacterSpacing(1));
				}
				benAddTable.addCell(abCellMat8);

			} else {

			}

		}

		benAddTable.startNewRow();

		// Creating Empty Cell to seperate Additional Benefits
		Cell abCellAddEty = new Cell(0, 7);
		abCellAddEty.setBorder(Border.NO_BORDER);
		abCellAddEty.setBorderTop(new SolidBorder(1));
		benAddTable.addCell(abCellAddEty);

		////////////////////// table headings of the Additional Benefits
		////////////////////// table\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		Cell abCellth1 = new Cell(2, 0);
		abCellth1.setBorder(new SolidBorder(1));

		abCellth1.add(new Paragraph("Additional Benefits").setFontSize(9).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1).setMarginTop(10));
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
		abCelld1.setBorderLeft(new SolidBorder(1));
		abCelld1.add(new Paragraph("Natural Death Cover").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld1);

		Cell abCelld2 = new Cell();
		if (ndc == 0.0) {
			abCelld2.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld2.add(new Paragraph(formatter.format(ndc)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld2);

		Cell abCelld3 = new Cell();
		if (ndcp == 0.0) {
			abCelld3.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld3.add(new Paragraph(formatter.format(ndcp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld3);

		Cell abCelld4 = new Cell();
		if (ndcs == 0.0) {
			abCelld4.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld4.add(new Paragraph(formatter.format(ndcs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld4);

		Cell abCelld5 = new Cell();
		if (ndcsp == 0.0) {
			abCelld5.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld5.add(new Paragraph(formatter.format(ndcsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld5);

		Cell abCelld6 = new Cell();
		abCelld6.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld6);

		Cell abCelld7 = new Cell();
		abCelld7.setBorderRight(new SolidBorder(1));

		abCelld7.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld7);

		benAddTable.startNewRow();

		Cell abCelld8 = new Cell();
		abCelld8.setBorderLeft(new SolidBorder(1));

		abCelld8.add(new Paragraph("Accidental Death Benefit").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld8);

		Cell abCelld9 = new Cell();
		if (adb == 0.0) {
			abCelld9.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld9.add(new Paragraph(formatter.format(adb)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld9);

		Cell abCelld10 = new Cell();
		if (adbp == 0.0) {
			abCelld10.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld10.add(new Paragraph(formatter.format(adbp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld10);

		Cell abCelld11 = new Cell();
		if (adbs == 0.0) {
			abCelld11.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld11.add(new Paragraph(formatter.format(adbs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld11);

		Cell abCelld12 = new Cell();
		if (adbsp == 0.0) {
			abCelld12.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld12.add(new Paragraph(formatter.format(adbsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld12);

		Cell abCelld13 = new Cell();
		abCelld13.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld13);

		Cell abCelld14 = new Cell();
		abCelld14.setBorderRight(new SolidBorder(1));

		abCelld14.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld14);

		benAddTable.startNewRow();

		Cell abCelld15 = new Cell();
		abCelld15.setBorderLeft(new SolidBorder(1));
		abCelld15.setBorderBottom(new SolidBorder(1));

		abCelld15.add(new Paragraph("Funeral Expenses").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld15);

		Cell abCelld16 = new Cell();
		abCelld16.setBorderBottom(new SolidBorder(1));
		if (feb == 0.0) {
			abCelld16.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld16.add(new Paragraph(formatter.format(feb)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld16);

		Cell abCelld117 = new Cell();
		abCelld117.setBorderBottom(new SolidBorder(1));
		if (febp == 0.0) {
			abCelld117.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld117.add(new Paragraph(formatter.format(febp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld117);

		Cell abCelld18 = new Cell();
		abCelld18.setBorderBottom(new SolidBorder(1));
		if (febs == 0.0) {
			abCelld18.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld18.add(new Paragraph(formatter.format(febs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld18);

		Cell abCelld19 = new Cell();
		abCelld19.setBorderBottom(new SolidBorder(1));
		if (febsp == 0.0) {
			abCelld19.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld19.add(new Paragraph(formatter.format(febsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld19);

		Cell abCelld20 = new Cell();
		abCelld20.setBorderBottom(new SolidBorder(1));
		abCelld20.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld20);

		Cell abCelld21 = new Cell();
		abCelld21.setBorderBottom(new SolidBorder(1));
		abCelld21.setBorderRight(new SolidBorder(1));
		abCelld21.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld21);

		///////////////////// End Of Additional Benefits Table
		///////////////////// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		document.add(benAddTable);

		/////////////////////////// END Of
		/////////////////////////// Benefits Table
		/////////////////////////// FORMAT///////////////////////////////////////

		//////////////////////////// * Medical Requirements
		//////////////////////////// Table*/////////////////////////
		java.util.List<MedicalRequirementsHelper> medicalDetails = medicalRequirementsDaoCustom
				.findByQuoDetail(quotationDetails.getQdId());

		document.add(new Paragraph(""));

		document.add(new Paragraph("Medical Requirements").setBold().setFontSize(9).setUnderline()
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));

		document.add(new Paragraph(""));

		// Medical Requirements Table
		float[] pointColumnWidths6 = { 160, 500 };
		Table medReqTable = new Table(pointColumnWidths6);
		medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell mrqCell1 = new Cell();
		mrqCell1.setBorder(Border.NO_BORDER);
		mrqCell1.add(new Paragraph("Main Life").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setCharacterSpacing(1));
		medReqTable.addCell(mrqCell1);

		if (medicalDetails.isEmpty()) {

			Cell mrqCell2 = new Cell();
			mrqCell2.setBorder(Border.NO_BORDER);
			mrqCell2.add(new Paragraph(": Not Applied ").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			medReqTable.addCell(mrqCell2);

		} else {

			// Creating a String Seperator ArrayList Seperate from seperator
			final String SEPARATOR = " , ";

			StringBuilder mainLife = new StringBuilder();
			StringBuilder spouse = new StringBuilder();

			for (MedicalRequirementsHelper medicalReq : medicalDetails) {

				// When MainLife having medical requiremnents
				if (medicalReq.getMainStatus().equalsIgnoreCase("R")) {
					mainLife.append(medicalReq.getMedicalReqname());
					mainLife.append(SEPARATOR);
					// When MainLife not having medical Requirements
				} else if (medicalReq.getMainStatus().equalsIgnoreCase("NR")) {

				} else {
					mainLife.append("NR");
					mainLife.append(SEPARATOR);
				}

				// When Spouse having medical Requirements
				if (medicalReq.getSpouseStatus().equalsIgnoreCase("R")) {
					spouse.append(medicalReq.getMedicalReqname());
					spouse.append(SEPARATOR);

					// When Spouse Not having medical Requirements
				} else if (medicalReq.getSpouseStatus().equalsIgnoreCase("NR")) {

				} else {
					spouse.append("NR");
					spouse.append(SEPARATOR);
				}

			}

			String mainMedical = mainLife.toString();
			mainMedical = mainMedical.substring(0, mainMedical.length() - SEPARATOR.length());

			Cell mrqCell2 = new Cell();
			mrqCell2.setBorder(Border.NO_BORDER);
			mrqCell2.add(new Paragraph(": " + mainMedical).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			medReqTable.addCell(mrqCell2);

			medReqTable.startNewRow();

			if (benefitsSpouse.isEmpty()) {

			} else {

				String spouseMedical = spouse.toString();

				// System.out.println("spouse medicalllllllllllllllll " + spouseMedical);

				if (!spouseMedical.endsWith("NR") && !spouseMedical.isEmpty()) {

					spouseMedical = spouseMedical.substring(0, spouseMedical.length() - SEPARATOR.length());

					Cell mrqCell3 = new Cell();
					mrqCell3.setBorder(Border.NO_BORDER);
					mrqCell3.add(new Paragraph("Spouse").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
							.setFixedLeading(10).setCharacterSpacing(1));
					medReqTable.addCell(mrqCell3);

					Cell mrqCell4 = new Cell();
					mrqCell4.setBorder(Border.NO_BORDER);
					mrqCell4.add(new Paragraph(": " + spouseMedical).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
							.setFixedLeading(10));
					medReqTable.addCell(mrqCell4);
				}
			}

		}

		document.add(medReqTable);

		/*
		 * try {
		 * 
		 * if (medicalDetails.isEmpty()) {
		 * 
		 * } else { document.add(new Paragraph(""));
		 * 
		 * document.add(new
		 * Paragraph("Medical Requirements").setBold().setFontSize(9).setUnderline()
		 * .setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing
		 * (1));
		 * 
		 * document.add(new Paragraph(""));
		 * 
		 * // Medical Requirements Table float[] pointColumnWidths6 = { 100, 500 };
		 * Table medReqTable = new Table(pointColumnWidths6);
		 * medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
		 * 
		 * // Creating a String Seperator ArrayList Seperate from seperator final String
		 * SEPARATOR = " , ";
		 * 
		 * StringBuilder mainLife = new StringBuilder(); StringBuilder spouse = new
		 * StringBuilder();
		 * 
		 * for (MedicalRequirementsHelper medicalReq : medicalDetails) {
		 * 
		 * // When MainLife having medical requiremnents if
		 * (medicalReq.getMainStatus().equalsIgnoreCase("R")) {
		 * mainLife.append(medicalReq.getMedicalReqname()); mainLife.append(SEPARATOR);
		 * // When MainLife not having medical Requirements } else if
		 * (medicalReq.getMainStatus().equalsIgnoreCase("NR")) {
		 * 
		 * } else { mainLife.append("NR"); mainLife.append(SEPARATOR); }
		 * 
		 * // When Spouse having medical Requirements if
		 * (medicalReq.getSpouseStatus().equalsIgnoreCase("R")) {
		 * spouse.append(medicalReq.getMedicalReqname()); spouse.append(SEPARATOR);
		 * 
		 * // When Spouse Not having medical Requirements } else if
		 * (medicalReq.getSpouseStatus().equalsIgnoreCase("NR")) {
		 * 
		 * } else { spouse.append("NR"); spouse.append(SEPARATOR); }
		 * 
		 * }
		 * 
		 * String mainMedical = mainLife.toString(); mainMedical =
		 * mainMedical.substring(0, mainMedical.length() - SEPARATOR.length());
		 * 
		 * Cell mrqCell1 = new Cell(); mrqCell1.setBorder(Border.NO_BORDER);
		 * mrqCell1.add(new
		 * Paragraph("Main Life").setBold().setFontSize(9).setTextAlignment(
		 * TextAlignment.LEFT) .setFixedLeading(10).setCharacterSpacing(1));
		 * medReqTable.addCell(mrqCell1);
		 * 
		 * Cell mrqCell2 = new Cell(); mrqCell2.setBorder(Border.NO_BORDER);
		 * mrqCell2.add(new Paragraph(": " +
		 * mainMedical).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
		 * .setFixedLeading(10)); medReqTable.addCell(mrqCell2);
		 * 
		 * medReqTable.startNewRow();
		 * 
		 * if (benefitsSpouse.isEmpty()) {
		 * 
		 * } else {
		 * 
		 * String spouseMedical = spouse.toString();
		 * 
		 * // System.out.println("spouse medicalllllllllllllllll " + spouseMedical);
		 * 
		 * if (!spouseMedical.endsWith("NR") && !spouseMedical.isEmpty()) {
		 * 
		 * spouseMedical = spouseMedical.substring(0, spouseMedical.length() -
		 * SEPARATOR.length());
		 * 
		 * Cell mrqCell3 = new Cell(); mrqCell3.setBorder(Border.NO_BORDER);
		 * mrqCell3.add(new Paragraph("Spouse").setBold().setFontSize(9)
		 * .setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing
		 * (1)); medReqTable.addCell(mrqCell3);
		 * 
		 * Cell mrqCell4 = new Cell(); mrqCell4.setBorder(Border.NO_BORDER);
		 * mrqCell4.add(new Paragraph(": " + spouseMedical).setFontSize(9)
		 * .setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		 * medReqTable.addCell(mrqCell4); } }
		 * 
		 * document.add(medReqTable);
		 * 
		 * }
		 * 
		 * } catch (Exception e) { e.printStackTrace();
		 * 
		 * }
		 */

		/////////////////////// *Start FinanCial Requirements*///////////////////

		// Creating A variable for Sum At Risk
		Double fiveM = 5000000.00;

		System.out.println(
				"sum at riskkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk" + quotationDetails.getSumAtRiskMain());

		if (quotationDetails.getSumAtRiskMain() != null) {

			document.add(new Paragraph(""));

			float[] finacialReqColoumWidth = { 150, 500 };
			Table finReqTbl = new Table(finacialReqColoumWidth);
			finReqTbl.setHorizontalAlignment(HorizontalAlignment.LEFT);

			Cell fReqCell1 = new Cell();
			fReqCell1.setBorder(Border.NO_BORDER);
			fReqCell1.add(new Paragraph("Financial Requirements").setBold().setFontSize(9).setUnderline()
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));
			finReqTbl.addCell(fReqCell1);

			Cell fReqCell2 = new Cell();

			// If Sum At Risk> 5000000
			if (quotationDetails.getSumAtRiskMain() >= fiveM) {

				fReqCell2.setBorder(Border.NO_BORDER);
				fReqCell2.add(new Paragraph(": Applied").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else {
				fReqCell2.setBorder(Border.NO_BORDER);
				fReqCell2.add(new Paragraph(": Not Applied").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

			finReqTbl.addCell(fReqCell2);

			document.add(finReqTbl);

			/*
			 * document.add(new
			 * Paragraph("Financial Requirements ").setBold().setFontSize(9).setUnderline()
			 * .setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing
			 * (1));
			 * 
			 * document.add(new
			 * Paragraph("Applied - Please refer Underwritting department for Financial Guidlines. "
			 * ) .setFontSize(9).setFixedLeading(10));
			 */

		}

		/////////////////////// *End of FinanCial Requirements*///////////////////

		document.add(new Paragraph(""));

		document.add(new Paragraph("Special Notes").setFontSize(9).setBold().setUnderline().setCharacterSpacing(1));

		// Creating a Special Notes List
		List list = new List(ListNumberingType.DECIMAL);
		list.setFontSize(9);

		ListItem item6 = new ListItem();
		item6.add(
				new Paragraph("Guranteed minimum dividend rate declared for " + calendar.get(Calendar.YEAR) + " - 9.0%")
						.setFontSize(9).setFixedLeading(10));
		list.add(item6);

		ListItem item1 = new ListItem();
		item1.add(
				new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.")
						.setFontSize(9).setFixedLeading(10));
		list.add(item1);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("All amounts are in Sri Lankan Rupees(LKR).").setFontSize(9).setFixedLeading(10));
		list.add(item3);

		ListItem item4 = new ListItem();
		item4.add(new Paragraph("Initial policy processing fee of Rs 300 (Payable only with initial deposit).")
				.setFontSize(9).setFixedLeading(10));
		list.add(item4);

		ListItem item5 = new ListItem();
		item5.add(new Paragraph(
				"In event of death by accident both Accident Cover and Natural Death Cover will be applicable.")
						.setFontSize(9).setFixedLeading(10));
		list.add(item5);

		ListItem item2 = new ListItem();
		item2.add(new Paragraph("This is an indicative quote only and is valid for 30 days from date of issue.")
				.setFontSize(9).setFixedLeading(10));
		list.add(item2);

		document.add(list);

		document.add(new Paragraph("This is a system generated report and therefore does not require a signature.")
				.setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setFixedPosition(50, 10, 500));

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
		item1.add(
				new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.")
						.setFontSize(10).setFixedLeading(10));
		list.add(item1);

		ListItem item2 = new ListItem();
		item2.add(new Paragraph("All amounts are in Sri Lankan Rupees(LKR).").setFontSize(10).setFixedLeading(10));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("Initial policy processing fee of Rs.450 (Payable only with initial deposit).")
				.setFontSize(10).setFixedLeading(10));
		list.add(item3);

		ListItem item4 = new ListItem();
		item4.add(new Paragraph(
				"In event of death by accident both Accident Cover and Natural Death Cover will be applicable.")
						.setFontSize(10).setFixedLeading(10));
		list.add(item4);

		ListItem item5 = new ListItem();
		item5.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date of issue.")
				.setFontSize(10).setFixedLeading(10));
		list.add(item5);

		document.add(list);

		document.add(new Paragraph("This is a system generated report and therefore does not require a signature.")
				.setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setFixedPosition(50, 10, 500));

		document.close();
		return baos.toByteArray();
	}

	// Creating Arpico Term Plan
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
		document.setTopMargin(30);
		document.setBottomMargin(10);

		// Agent Details
		float[] pointColumnWidths1 = { 90, 150 };
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
				agcell8.add(new Paragraph(": " + quotationDetails.getQuotation().getUser().getUserCode()).setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else if (val == 0) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": ............................ ").setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(
						new Paragraph(": ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
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

		document.add(new Paragraph("ARPICO TERM PLAN").setFontSize(9).setCharacterSpacing(1));

		final SolidLine lineDrawer = new SolidLine(1f);
		document.add(new LineSeparator(lineDrawer));
		document.add(new Paragraph(""));

		// customer/Spouse/Plan Details
		float[] pointColumnWidths2 = { 300, 150, 100, 100, 130 };
		Table cusTable = new Table(pointColumnWidths2);
		cusTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		///////////////////////// *Craeting Main Life Details*/
		Cell cuCellTh = new Cell();
		cuCellTh.setBorder(Border.NO_BORDER);
		cuCellTh.add(new Paragraph("Main Life Details").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cuCellTh);

		Cell cucellTh1 = new Cell();
		cucellTh1.setBorder(Border.NO_BORDER);
		cucellTh1.add(new Paragraph("Occupation").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh1);

		Cell cucellTh2 = new Cell();
		cucellTh2.setBorder(Border.NO_BORDER);
		cucellTh2.add(
				new Paragraph("DOB").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh2);

		Cell cucellTh3 = new Cell();
		cucellTh3.setBorder(Border.NO_BORDER);
		cucellTh3.add(
				new Paragraph("Age").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh3);

		Cell cucellTh4 = new Cell();
		cucellTh4.setBorder(Border.NO_BORDER);
		cucellTh4.add(new Paragraph("Gender").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cucellTh4);
		cusTable.startNewRow();

		Cell cuCellM1 = new Cell();
		cuCellM1.setBorder(Border.NO_BORDER);
		cuCellM1.add(new Paragraph(quotationDetails.getCustomerDetails().getCustName() != null
				? quotationDetails.getCustomerDetails().getCustName()
				: " ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM1);

		Cell cuCellM2 = new Cell();
		cuCellM2.setBorder(Border.NO_BORDER);
		cuCellM2.add(new Paragraph(mainLifeOccupation != null ? mainLifeOccupation : " ").setFontSize(9)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM2);

		Cell cuCellM3 = new Cell();
		cuCellM3.setBorder(Border.NO_BORDER);

		// Creating a Date Format for DOB
		SimpleDateFormat mainDob = new SimpleDateFormat("dd-MM-yyyy");

		cuCellM3.add(new Paragraph(quotationDetails.getCustomerDetails().getCustDob() != null
				? mainDob.format(quotationDetails.getCustomerDetails().getCustDob())
				: " ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM3);

		Cell cuCellM4 = new Cell();
		cuCellM4.setBorder(Border.NO_BORDER);
		if (quoCustomer.getMainLifeAge() != null) {
			cuCellM4.add(new Paragraph(Integer.toString(quoCustomer.getMainLifeAge())).setFontSize(8)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCellM4.add(new Paragraph(" ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCellM4);

		Cell cuCellM5 = new Cell();
		cuCellM5.setBorder(Border.NO_BORDER);

		// Check MainLife Gender If M->Male, F->Female
		if (quotationDetails.getCustomerDetails().getCustGender() != null) {

			if (quotationDetails.getCustomerDetails().getCustGender().equalsIgnoreCase("M")) {
				cuCellM5.add(
						new Paragraph("Male").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			} else if (quotationDetails.getCustomerDetails().getCustGender().equalsIgnoreCase("F")) {
				cuCellM5.add(new Paragraph("Female").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		} else {

		}
		cusTable.addCell(cuCellM5);

		/////////////////////////// *End Craeting Main Life Details*/

		cusTable.startNewRow();

		// Create an Empty Line
		Cell cuCellEmptyM = new Cell(0, 5);
		cuCellEmptyM.setBorder(Border.NO_BORDER);
		cuCellEmptyM.add(new Paragraph("").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellEmptyM);

		//////////////////////// * Strat Creating Spouse Details*/

		// checking Spouse is active or not
		if ((quoCustomer.getSpouseName()) != null) {

			Cell cuCellThS = new Cell();
			cuCellThS.setBorder(Border.NO_BORDER);
			cuCellThS.add(new Paragraph("Spouse Details").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10).setBold());
			cusTable.addCell(cuCellThS);

			cusTable.startNewRow();

			Cell cuCellS1 = new Cell();
			cuCellS1.setBorder(Border.NO_BORDER);
			cuCellS1.add(new Paragraph(quoCustomer.getSpouseName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCellS1);

		} else {

		}

		if ((quoCustomer.getSpouseOccupation()) != null) {

			Cell cuCellS2 = new Cell();
			cuCellS2.setBorder(Border.NO_BORDER);
			cuCellS2.add(new Paragraph(spouseOccupation).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCellS2);

		} else {

		}

		if (quotationDetails.getSpouseDetails() != null) {

			Cell cuCellS3 = new Cell();
			cuCellS3.setBorder(Border.NO_BORDER);
			cuCellS3.add(new Paragraph(mainDob.format(quotationDetails.getSpouseDetails().getCustDob())).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCellS3);

		} else {

		}

		if ((quoCustomer.getSpouseAge()) != null) {

			Cell cuCellS4 = new Cell();
			cuCellS4.setBorder(Border.NO_BORDER);
			cuCellS4.add(new Paragraph(Integer.toString(quoCustomer.getSpouseAge())).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCellS4);

		} else {

		}

		// Display Spouse Gender
		Cell cuCellS5 = new Cell();
		cuCellS5.setBorder(Border.NO_BORDER);

		if ((quotationDetails.getSpouseDetails()) != null) {

			if (quotationDetails.getSpouseDetails().getCustGender().equalsIgnoreCase("M")) {
				cuCellS5.add(
						new Paragraph("Male").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			} else if (quotationDetails.getSpouseDetails().getCustGender().equalsIgnoreCase("F")) {
				cuCellS5.add(new Paragraph("Female").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		} else {

		}

		cusTable.addCell(cuCellS5);

		/////////////////////////////////// * End of Spouse Details*/

		cusTable.startNewRow();

		// Create an Empty Line
		Cell cuCellEmptyP = new Cell(0, 5);
		cuCellEmptyP.setBorder(Border.NO_BORDER);
		cuCellEmptyP.add(new Paragraph("").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellEmptyP);

		//////////////////// * Strat Creating Plan Details*/
		Cell cuCellPlanTh = new Cell();
		cuCellPlanTh.add(new Paragraph("Plan Details").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cuCellPlanTh);

		Cell cuCellP1 = new Cell();
		cuCellP1.add(new Paragraph("Term").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cuCellP1);

		Cell cuCellP2 = new Cell();
		cuCellP2.add(new Paragraph("Premium").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cuCellP2);

		Cell cuCellP3 = new Cell();
		cuCellP3.add(new Paragraph("Method").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setBold()
				.setFixedLeading(10));
		cusTable.addCell(cuCellP3);

		Cell cuCellP4 = new Cell();
		cuCellP4.add(new Paragraph("Basic Sum Assured").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setBold()
				.setFixedLeading(10));
		cusTable.addCell(cuCellP4);

		cusTable.startNewRow();

		// Display Product Code
		Cell cuCellP5 = new Cell();
		if (quotationDetails.getQuotation().getProducts().getProductCode() != null) {
			cuCellP5.add(new Paragraph(quotationDetails.getQuotation().getProducts().getProductCode()).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCellP5.add(new Paragraph(" ").setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCellP5);

		Cell cuCellP6 = new Cell();
		if (quotationDetails.getPolTerm() != null) {
			cuCellP6.add(new Paragraph(Integer.toString(quotationDetails.getPolTerm())).setFontSize(9)
					.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		} else {
			cuCellP6.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		}
		cusTable.addCell(cuCellP6);

		Cell cuCellP7 = new Cell();
		if (quoCustomer.getModePremium() != null) {
			cuCellP7.add(new Paragraph(formatter.format(quoCustomer.getTotPremium())).setFontSize(9)
					.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		} else {
			cuCellP7.add(new Paragraph(" ").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));

		}
		cusTable.addCell(cuCellP7);

		Cell cuCellP8 = new Cell();

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

			cuCellP8.add(new Paragraph(modeMethod).setFontSize(9).setTextAlignment(TextAlignment.CENTER)
					.setFixedLeading(10));

		}
		cusTable.addCell(cuCellP8);

		// Display Basic Sum Assured
		Cell cuCellP9 = new Cell();

		if (benefitsLife.isEmpty()) {

		} else {

			for (QuoBenf bsa : benefitsLife) {
				System.out.println(bsa.getRiderCode());

				if (bsa.getRiderCode().equalsIgnoreCase("L2")) {

					if (bsa.getRiderSum() != null) {
						cuCellP9.add(new Paragraph(formatter.format(bsa.getRiderSum())).setFontSize(9)
								.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));

						cusTable.addCell(cuCellP9);

					} else {

					}

				}

			}

		}

		//////////////////// * End of Plan Details*/

		document.add(cusTable);
		//////////////////////////////// *End of MainLife/Spouse/Plan Details
		//////////////////////////////// Table*//////////

		document.add(new Paragraph("Benefits").setFontSize(9).setBold().setUnderline().setCharacterSpacing(1));
		////////////////

		//////////////////////////// Benefits Table
		//////////////////////////// FORMAT//////////////////////////////////////

		// Create Additional Benefits Table
		/* Declaring column sizes of the table respectively */
		float[] pointColumnWidths4 = { 500, 80, 80, 80, 80, 80, 80 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		// table headings of the Living Benefits
		Cell alCellth1 = new Cell(2, 0);
		alCellth1.setBorder(new SolidBorder(1));
		alCellth1.add(new Paragraph("Living Benefits").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1).setMarginTop(10));
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
			alCellBenf.setBorderLeft(new SolidBorder(1));

			// Getting ALL Benefits Name object and cast to an String
			String p = (String) hashMap.get("benName");
			String maturity = (String) hashMap.get("combination");

			System.out.println("Combinationnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn " + maturity.equalsIgnoreCase("L3"));

			// Check Maturity values not equl then Display
			if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {

				alCellBenf.add(new Paragraph(p).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(alCellBenf);
			}

			// Display Main Life Rider Amounts
			Cell alCellmA = new Cell();
			if (hashMap.get("mainAmt") != null) {

				// Getting ALL Benefits Name Combinations object and cast to an String
				String comb = (String) hashMap.get("combination");

				/* If benefit is WPB Print Amount as APPLIED */
				if (comb.equalsIgnoreCase("WPB")) {
					alCellmA.add(new Paragraph("Applied").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmA);

					// Check Maturity values not equl then Display
				} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
						&& !maturity.equalsIgnoreCase("L5")) {

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

			// Display Main Life Rider Premium
			Cell alCellmP = new Cell();
			if (hashMap.get("mainPre") != null) {

				if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
						&& !maturity.equalsIgnoreCase("L5")) {
					Double mPre = (Double) hashMap.get("mainPre");

					alCellmP.add(
							new Paragraph(formatter.format(mPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmP);
				}

			} else {
				alCellmP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellmP);
			}

			// Display Spouse Rider Amounts
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

				// IF spouse Rider Not Equals Maturity Values and Null
			} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {
				alCellsA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsA);
			}

			// Display Spouse Rider Premium
			Cell alCellsP = new Cell();
			if (hashMap.get("spousePre") != null) {
				Double spPre = (Double) hashMap.get("spousePre");

				alCellsP.add(
						new Paragraph(formatter.format(spPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsP);
			} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {
				alCellsP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsP);
			}

			// Display Child Rider Amounts
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

				// IF Child Riders Are not equal Maturities and Amount is null
			} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {

				alCellcA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcA);
			}

			// Display Child Riders Premium
			Cell alCellcP = new Cell();
			alCellcP.setBorderRight(new SolidBorder(1));

			if (hashMap.get("childPre") != null) {

				Double cPre = (Double) hashMap.get("childPre");

				alCellcP.add(
						new Paragraph(formatter.format(cPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcP);

			} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {
				alCellcP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcP);
			}

		}

		benAddTable.startNewRow();

		// Creating Empty Cell to seperate Additional Benefits
		Cell abCellAddEty = new Cell(0, 7);
		abCellAddEty.setBorder(Border.NO_BORDER);
		benAddTable.addCell(abCellAddEty);

		benAddTable.startNewRow();

		////////////////////// table headings of the Additional Benefits
		////////////////////// table\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		Cell abCellth1 = new Cell(2, 0);
		abCellth1.setBorder(new SolidBorder(1));
		abCellth1.add(new Paragraph("Additional Benefits").setFontSize(9).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1).setMarginTop(10));
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
		abCelld1.setBorderLeft(new SolidBorder(1));
		abCelld1.add(new Paragraph("Natural Death Cover").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld1);

		Cell abCelld2 = new Cell();
		if (ndc == 0.0) {
			abCelld2.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld2.add(new Paragraph(formatter.format(ndc)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld2);

		Cell abCelld3 = new Cell();
		if (ndcp == 0.0) {
			abCelld3.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld3.add(new Paragraph(formatter.format(ndcp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld3);

		Cell abCelld4 = new Cell();
		if (ndcs == 0.0) {
			abCelld4.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld4.add(new Paragraph(formatter.format(ndcs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld4);

		Cell abCelld5 = new Cell();
		if (ndcsp == 0.0) {
			abCelld5.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld5.add(new Paragraph(formatter.format(ndcsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld5);

		Cell abCelld6 = new Cell();
		abCelld6.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld6);

		Cell abCelld7 = new Cell();
		abCelld7.setBorderRight(new SolidBorder(1));

		abCelld7.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld7);

		benAddTable.startNewRow();

		Cell abCelld8 = new Cell();
		abCelld8.setBorderLeft(new SolidBorder(1));

		abCelld8.add(new Paragraph("Accidental Death Benefit").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld8);

		Cell abCelld9 = new Cell();
		if (adb == 0.0) {
			abCelld9.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld9.add(new Paragraph(formatter.format(adb)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld9);

		Cell abCelld10 = new Cell();
		if (adbp == 0.0) {
			abCelld10.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld10.add(new Paragraph(formatter.format(adbp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld10);

		Cell abCelld11 = new Cell();
		if (adbs == 0.0) {
			abCelld11.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld11.add(new Paragraph(formatter.format(adbs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld11);

		Cell abCelld12 = new Cell();
		if (adbsp == 0.0) {
			abCelld12.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld12.add(new Paragraph(formatter.format(adbsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld12);

		Cell abCelld13 = new Cell();
		abCelld13.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld13);

		Cell abCelld14 = new Cell();
		abCelld14.setBorderRight(new SolidBorder(1));

		abCelld14.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld14);

		benAddTable.startNewRow();

		Cell abCelld15 = new Cell();
		abCelld15.setBorderLeft(new SolidBorder(1));
		abCelld15.setBorderBottom(new SolidBorder(1));

		abCelld15.add(new Paragraph("Funeral Expenses").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld15);

		Cell abCelld16 = new Cell();
		abCelld16.setBorderBottom(new SolidBorder(1));
		if (feb == 0.0) {
			abCelld16.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld16.add(new Paragraph(formatter.format(feb)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld16);

		Cell abCelld117 = new Cell();
		abCelld117.setBorderBottom(new SolidBorder(1));
		if (febp == 0.0) {
			abCelld117.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld117.add(new Paragraph(formatter.format(febp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld117);

		Cell abCelld18 = new Cell();
		abCelld18.setBorderBottom(new SolidBorder(1));
		if (febs == 0.0) {
			abCelld18.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld18.add(new Paragraph(formatter.format(febs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld18);

		Cell abCelld19 = new Cell();
		abCelld19.setBorderBottom(new SolidBorder(1));
		if (febsp == 0.0) {
			abCelld19.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld19.add(new Paragraph(formatter.format(febsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld19);

		Cell abCelld20 = new Cell();
		abCelld20.setBorderBottom(new SolidBorder(1));
		abCelld20.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld20);

		Cell abCelld21 = new Cell();
		abCelld21.setBorderBottom(new SolidBorder(1));
		abCelld21.setBorderRight(new SolidBorder(1));

		abCelld21.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld21);

		///////////////////// End Of Additional Benefits Table
		///////////////////// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		document.add(benAddTable);

		/////////////////////////// END Of
		/////////////////////////// Benefits Table
		/////////////////////////// FORMAT///////////////////////////////////////

		//////////////////////////// * Medical Requirements
		//////////////////////////// Table*/////////////////////////
		java.util.List<MedicalRequirementsHelper> medicalDetails = medicalRequirementsDaoCustom
				.findByQuoDetail(quotationDetails.getQdId());

		document.add(new Paragraph(""));

		document.add(new Paragraph("Medical Requirements").setBold().setFontSize(9).setUnderline()
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));

		document.add(new Paragraph(""));

		// Medical Requirements Table
		float[] pointColumnWidths6 = { 160, 500 };
		Table medReqTable = new Table(pointColumnWidths6);
		medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell mrqCell1 = new Cell();
		mrqCell1.setBorder(Border.NO_BORDER);
		mrqCell1.add(new Paragraph("Main Life").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setCharacterSpacing(1));
		medReqTable.addCell(mrqCell1);

		if (medicalDetails.isEmpty()) {

			Cell mrqCell2 = new Cell();
			mrqCell2.setBorder(Border.NO_BORDER);
			mrqCell2.add(new Paragraph(": Not Applied ").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			medReqTable.addCell(mrqCell2);

		} else {

			// Creating a String Seperator ArrayList Seperate from seperator
			final String SEPARATOR = " , ";

			StringBuilder mainLife = new StringBuilder();
			StringBuilder spouse = new StringBuilder();

			for (MedicalRequirementsHelper medicalReq : medicalDetails) {

				// When MainLife having medical requiremnents
				if (medicalReq.getMainStatus().equalsIgnoreCase("R")) {
					mainLife.append(medicalReq.getMedicalReqname());
					mainLife.append(SEPARATOR);
					// When MainLife not having medical Requirements
				} else if (medicalReq.getMainStatus().equalsIgnoreCase("NR")) {

				} else {
					mainLife.append("NR");
					mainLife.append(SEPARATOR);
				}

				// When Spouse having medical Requirements
				if (medicalReq.getSpouseStatus().equalsIgnoreCase("R")) {
					spouse.append(medicalReq.getMedicalReqname());
					spouse.append(SEPARATOR);

					// When Spouse Not having medical Requirements
				} else if (medicalReq.getSpouseStatus().equalsIgnoreCase("NR")) {

				} else {
					spouse.append("NR");
					spouse.append(SEPARATOR);
				}

			}

			String mainMedical = mainLife.toString();
			mainMedical = mainMedical.substring(0, mainMedical.length() - SEPARATOR.length());

			Cell mrqCell2 = new Cell();
			mrqCell2.setBorder(Border.NO_BORDER);
			mrqCell2.add(new Paragraph(": " + mainMedical).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			medReqTable.addCell(mrqCell2);

			medReqTable.startNewRow();

			if (benefitsSpouse.isEmpty()) {

			} else {

				String spouseMedical = spouse.toString();

				// System.out.println("spouse medicalllllllllllllllll " + spouseMedical);

				if (!spouseMedical.endsWith("NR") && !spouseMedical.isEmpty()) {

					spouseMedical = spouseMedical.substring(0, spouseMedical.length() - SEPARATOR.length());

					Cell mrqCell3 = new Cell();
					mrqCell3.setBorder(Border.NO_BORDER);
					mrqCell3.add(new Paragraph("Spouse").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
							.setFixedLeading(10).setCharacterSpacing(1));
					medReqTable.addCell(mrqCell3);

					Cell mrqCell4 = new Cell();
					mrqCell4.setBorder(Border.NO_BORDER);
					mrqCell4.add(new Paragraph(": " + spouseMedical).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
							.setFixedLeading(10));
					medReqTable.addCell(mrqCell4);
				}
			}

		}

		document.add(medReqTable);
		/*
		 * try {
		 * 
		 * if (medicalDetails.isEmpty()) {
		 * 
		 * } else { document.add(new Paragraph(""));
		 * 
		 * document.add(new
		 * Paragraph("Medical Requirements").setBold().setFontSize(9).setUnderline()
		 * .setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing
		 * (1));
		 * 
		 * document.add(new Paragraph(""));
		 * 
		 * // Medical Requirements Table float[] pointColumnWidths6 = { 100, 500 };
		 * Table medReqTable = new Table(pointColumnWidths6);
		 * medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
		 * 
		 * // Creating a String Seperator ArrayList Seperate from seperator final String
		 * SEPARATOR = " , ";
		 * 
		 * StringBuilder mainLife = new StringBuilder(); StringBuilder spouse = new
		 * StringBuilder();
		 * 
		 * for (MedicalRequirementsHelper medicalReq : medicalDetails) {
		 * 
		 * // When MainLife having medical requiremnents if
		 * (medicalReq.getMainStatus().equalsIgnoreCase("R")) {
		 * mainLife.append(medicalReq.getMedicalReqname()); mainLife.append(SEPARATOR);
		 * // When MainLife not having medical Requirements } else if
		 * (medicalReq.getMainStatus().equalsIgnoreCase("NR")) {
		 * 
		 * } else { mainLife.append("NR"); mainLife.append(SEPARATOR); }
		 * 
		 * // When Spouse having medical Requirements if
		 * (medicalReq.getSpouseStatus().equalsIgnoreCase("R")) {
		 * spouse.append(medicalReq.getMedicalReqname()); spouse.append(SEPARATOR);
		 * 
		 * // When Spouse Not having medical Requirements } else if
		 * (medicalReq.getSpouseStatus().equalsIgnoreCase("NR")) {
		 * 
		 * } else { spouse.append("NR"); spouse.append(SEPARATOR); }
		 * 
		 * }
		 * 
		 * String mainMedical = mainLife.toString(); mainMedical =
		 * mainMedical.substring(0, mainMedical.length() - SEPARATOR.length());
		 * 
		 * Cell mrqCell1 = new Cell(); mrqCell1.setBorder(Border.NO_BORDER);
		 * mrqCell1.add(new
		 * Paragraph("Main Life").setBold().setFontSize(9).setTextAlignment(
		 * TextAlignment.LEFT) .setFixedLeading(10).setCharacterSpacing(1));
		 * medReqTable.addCell(mrqCell1);
		 * 
		 * Cell mrqCell2 = new Cell(); mrqCell2.setBorder(Border.NO_BORDER);
		 * mrqCell2.add(new Paragraph(": " +
		 * mainMedical).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
		 * .setFixedLeading(10)); medReqTable.addCell(mrqCell2);
		 * 
		 * medReqTable.startNewRow();
		 * 
		 * if (benefitsSpouse.isEmpty()) {
		 * 
		 * } else {
		 * 
		 * String spouseMedical = spouse.toString();
		 * 
		 * // System.out.println("spouse medicalllllllllllllllll " + spouseMedical);
		 * 
		 * if (!spouseMedical.endsWith("NR") && !spouseMedical.isEmpty()) {
		 * 
		 * spouseMedical = spouseMedical.substring(0, spouseMedical.length() -
		 * SEPARATOR.length());
		 * 
		 * Cell mrqCell3 = new Cell(); mrqCell3.setBorder(Border.NO_BORDER);
		 * mrqCell3.add(new Paragraph("Spouse").setBold().setFontSize(9)
		 * .setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing
		 * (1)); medReqTable.addCell(mrqCell3);
		 * 
		 * Cell mrqCell4 = new Cell(); mrqCell4.setBorder(Border.NO_BORDER);
		 * mrqCell4.add(new Paragraph(": " + spouseMedical).setFontSize(9)
		 * .setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		 * medReqTable.addCell(mrqCell4); } }
		 * 
		 * document.add(medReqTable);
		 * 
		 * }
		 * 
		 * } catch (Exception e) { e.printStackTrace();
		 * 
		 * }
		 */

		document.add(new Paragraph(""));

		/////////////////////// *Start FinanCial Requirements*///////////////////

		// Creating A variable for Sum At Risk
		Double fiveM = 5000000.00;

		System.out.println(
				"sum at riskkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk" + quotationDetails.getSumAtRiskMain());

		if (quotationDetails.getSumAtRiskMain() != null) {

			float[] finacialReqColoumWidth = { 150, 500 };
			Table finReqTbl = new Table(finacialReqColoumWidth);
			finReqTbl.setHorizontalAlignment(HorizontalAlignment.LEFT);

			Cell fReqCell1 = new Cell();
			fReqCell1.setBorder(Border.NO_BORDER);
			fReqCell1.add(new Paragraph("Financial Requirements").setBold().setFontSize(9).setUnderline()
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));
			finReqTbl.addCell(fReqCell1);

			Cell fReqCell2 = new Cell();

			// If Sum At Risk> 5000000
			if (quotationDetails.getSumAtRiskMain() >= fiveM) {

				fReqCell2.setBorder(Border.NO_BORDER);
				fReqCell2.add(new Paragraph(": Applied").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else {
				fReqCell2.setBorder(Border.NO_BORDER);
				fReqCell2.add(new Paragraph(": Not Applied").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

			finReqTbl.addCell(fReqCell2);

			document.add(finReqTbl);

		}

		document.add(new Paragraph(""));

		/////////////////////// *End of FinanCial Requirements*///////////////////

		document.add(new Paragraph("Special Notes").setFontSize(9).setBold().setUnderline().setCharacterSpacing(1));

		// Creating a Special Notes List
		List list = new List(ListNumberingType.DECIMAL);
		list.setFontSize(9);

		ListItem item1 = new ListItem();
		item1.add(
				new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.")
						.setFontSize(9).setFixedLeading(10));
		list.add(item1);

		ListItem item2 = new ListItem();
		item2.add(new Paragraph("All amounts are in Sri Lankan Rupees(LKR).").setFontSize(9).setFixedLeading(10));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("Initial policy processing fee of Rs. 300 (Payable only with initial deposit).")
				.setFontSize(9).setFixedLeading(10));
		list.add(item3);

		ListItem item4 = new ListItem();
		item4.add(new Paragraph(
				"In event of death by accident both Accident Cover and Natural Death Cover will be applicable.")
						.setFontSize(9).setFixedLeading(10));
		list.add(item4);

		ListItem item5 = new ListItem();
		item5.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date of issue.")
				.setFontSize(9).setFixedLeading(10));
		list.add(item5);

		document.add(list);

		document.add(new Paragraph("This is a system generated report and therefore does not require a signature.")
				.setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setFixedPosition(50, 10, 500));

		document.close();

		return baos.toByteArray();
	}

	// Creating End Quotation Report
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
		document.setTopMargin(30);
		document.setBottomMargin(10);

		// Agent Details
		float[] pointColumnWidths1 = { 90, 150 };
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
				agcell8.add(new Paragraph(": " + quotationDetails.getQuotation().getUser().getUserCode()).setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else if (val == 0) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": ............................ ").setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(
						new Paragraph(": ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
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

		document.add(new Paragraph("ARPICO ENDOWMENT PLAN").setFontSize(9).setCharacterSpacing(1));

		final SolidLine lineDrawer = new SolidLine(1f);
		document.add(new LineSeparator(lineDrawer));
		document.add(new Paragraph(""));

		// customer/Spouse/Plan Details
		float[] pointColumnWidths2 = { 300, 150, 100, 100, 130 };
		Table cusTable = new Table(pointColumnWidths2);
		cusTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		///////////////////////// *Craeting Main Life Details*/
		Cell cuCellTh = new Cell();
		cuCellTh.setBorder(Border.NO_BORDER);
		cuCellTh.add(new Paragraph("Main Life Details").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cuCellTh);

		Cell cucellTh1 = new Cell();
		cucellTh1.setBorder(Border.NO_BORDER);
		cucellTh1.add(new Paragraph("Occupation").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh1);

		Cell cucellTh2 = new Cell();
		cucellTh2.setBorder(Border.NO_BORDER);
		cucellTh2.add(
				new Paragraph("DOB").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh2);

		Cell cucellTh3 = new Cell();
		cucellTh3.setBorder(Border.NO_BORDER);
		cucellTh3.add(
				new Paragraph("Age").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh3);

		Cell cucellTh4 = new Cell();
		cucellTh4.setBorder(Border.NO_BORDER);
		cucellTh4.add(new Paragraph("Gender").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cucellTh4);
		cusTable.startNewRow();

		Cell cuCellM1 = new Cell();
		cuCellM1.setBorder(Border.NO_BORDER);
		cuCellM1.add(new Paragraph(quotationDetails.getCustomerDetails().getCustName() != null
				? quotationDetails.getCustomerDetails().getCustName()
				: " ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM1);

		Cell cuCellM2 = new Cell();
		cuCellM2.setBorder(Border.NO_BORDER);
		cuCellM2.add(new Paragraph(mainLifeOccupation != null ? mainLifeOccupation : " ").setFontSize(9)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM2);

		Cell cuCellM3 = new Cell();
		cuCellM3.setBorder(Border.NO_BORDER);

		// Creating a Date Format for DOB
		SimpleDateFormat mainDob = new SimpleDateFormat("dd-MM-yyyy");

		cuCellM3.add(new Paragraph(quotationDetails.getCustomerDetails().getCustDob() != null
				? mainDob.format(quotationDetails.getCustomerDetails().getCustDob())
				: " ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM3);

		Cell cuCellM4 = new Cell();
		cuCellM4.setBorder(Border.NO_BORDER);
		if (quoCustomer.getMainLifeAge() != null) {
			cuCellM4.add(new Paragraph(Integer.toString(quoCustomer.getMainLifeAge())).setFontSize(8)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCellM4.add(new Paragraph(" ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCellM4);

		Cell cuCellM5 = new Cell();
		cuCellM5.setBorder(Border.NO_BORDER);

		// Check MainLife Gender If M->Male, F->Female
		if (quotationDetails.getCustomerDetails().getCustGender() != null) {

			if (quotationDetails.getCustomerDetails().getCustGender().equalsIgnoreCase("M")) {
				cuCellM5.add(
						new Paragraph("Male").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			} else if (quotationDetails.getCustomerDetails().getCustGender().equalsIgnoreCase("F")) {
				cuCellM5.add(new Paragraph("Female").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		} else {

		}
		cusTable.addCell(cuCellM5);

		/////////////////////////// *End Craeting Main Life Details*/

		cusTable.startNewRow();

		// Create an Empty Line
		Cell cuCellEmptyM = new Cell(0, 5);
		cuCellEmptyM.setBorder(Border.NO_BORDER);
		cuCellEmptyM.add(new Paragraph("").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellEmptyM);

		//////////////////////// * Strat Creating Spouse Details*/

		// checking Spouse is active or not
		if ((quoCustomer.getSpouseName()) != null) {

			Cell cuCellThS = new Cell();
			cuCellThS.setBorder(Border.NO_BORDER);
			cuCellThS.add(new Paragraph("Spouse Details").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10).setBold());
			cusTable.addCell(cuCellThS);

			cusTable.startNewRow();

			Cell cuCellS1 = new Cell();
			cuCellS1.setBorder(Border.NO_BORDER);
			cuCellS1.add(new Paragraph(quoCustomer.getSpouseName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCellS1);

		} else {

		}

		if ((quoCustomer.getSpouseOccupation()) != null) {

			Cell cuCellS2 = new Cell();
			cuCellS2.setBorder(Border.NO_BORDER);
			cuCellS2.add(new Paragraph(spouseOccupation).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCellS2);

		} else {

		}

		if (quotationDetails.getSpouseDetails() != null) {

			Cell cuCellS3 = new Cell();
			cuCellS3.setBorder(Border.NO_BORDER);
			cuCellS3.add(new Paragraph(mainDob.format(quotationDetails.getSpouseDetails().getCustDob())).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCellS3);

		} else {

		}

		if ((quoCustomer.getSpouseAge()) != null) {

			Cell cuCellS4 = new Cell();
			cuCellS4.setBorder(Border.NO_BORDER);
			cuCellS4.add(new Paragraph(Integer.toString(quoCustomer.getSpouseAge())).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCellS4);

		} else {

		}

		// Display Spouse Gender
		Cell cuCellS5 = new Cell();
		cuCellS5.setBorder(Border.NO_BORDER);

		if ((quotationDetails.getSpouseDetails()) != null) {

			if (quotationDetails.getSpouseDetails().getCustGender().equalsIgnoreCase("M")) {
				cuCellS5.add(
						new Paragraph("Male").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			} else if (quotationDetails.getSpouseDetails().getCustGender().equalsIgnoreCase("F")) {
				cuCellS5.add(new Paragraph("Female").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		} else {

		}

		cusTable.addCell(cuCellS5);

		/////////////////////////////////// * End of Spouse Details*/

		cusTable.startNewRow();

		// Create an Empty Line
		Cell cuCellEmptyP = new Cell(0, 5);
		cuCellEmptyP.setBorder(Border.NO_BORDER);
		cuCellEmptyP.add(new Paragraph("").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellEmptyP);

		//////////////////// * Strat Creating Plan Details*/
		Cell cuCellPlanTh = new Cell();
		cuCellPlanTh.add(new Paragraph("Plan Details").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cuCellPlanTh);

		Cell cuCellP1 = new Cell();
		cuCellP1.add(new Paragraph("Term").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cuCellP1);

		Cell cuCellP2 = new Cell();
		cuCellP2.add(new Paragraph("Premium").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cuCellP2);

		Cell cuCellP3 = new Cell();
		cuCellP3.add(new Paragraph("Method").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setBold()
				.setFixedLeading(10));
		cusTable.addCell(cuCellP3);

		Cell cuCellP4 = new Cell();
		cuCellP4.add(new Paragraph("Basic Sum Assured").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setBold()
				.setFixedLeading(10));
		cusTable.addCell(cuCellP4);

		cusTable.startNewRow();

		// Display Product Code
		Cell cuCellP5 = new Cell();
		if (quotationDetails.getQuotation().getProducts().getProductCode() != null) {
			cuCellP5.add(new Paragraph(quotationDetails.getQuotation().getProducts().getProductCode()).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCellP5.add(new Paragraph(" ").setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCellP5);

		Cell cuCellP6 = new Cell();
		if (quotationDetails.getPolTerm() != null) {
			cuCellP6.add(new Paragraph(Integer.toString(quotationDetails.getPolTerm())).setFontSize(9)
					.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		} else {
			cuCellP6.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		}
		cusTable.addCell(cuCellP6);

		Cell cuCellP7 = new Cell();
		if (quoCustomer.getModePremium() != null) {
			cuCellP7.add(new Paragraph(formatter.format(quoCustomer.getTotPremium())).setFontSize(9)
					.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		} else {
			cuCellP7.add(new Paragraph(" ").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));

		}
		cusTable.addCell(cuCellP7);

		Cell cuCellP8 = new Cell();

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

			cuCellP8.add(new Paragraph(modeMethod).setFontSize(9).setTextAlignment(TextAlignment.CENTER)
					.setFixedLeading(10));

		}
		cusTable.addCell(cuCellP8);

		// Display Basic Sum Assured
		Cell cuCellP9 = new Cell();

		if (benefitsLife.isEmpty()) {

		} else {

			for (QuoBenf bsa : benefitsLife) {
				System.out.println(bsa.getRiderCode());

				if (bsa.getRiderCode().equalsIgnoreCase("L2")) {

					if (bsa.getRiderSum() != null) {
						cuCellP9.add(new Paragraph(formatter.format(bsa.getRiderSum())).setFontSize(9)
								.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));

						cusTable.addCell(cuCellP9);

					} else {

					}

				}

			}

		}

		//////////////////// * End of Plan Details*/

		document.add(cusTable);
		//////////////////////////////// *End of MainLife/Spouse/Plan Details
		//////////////////////////////// Table*//////////

		document.add(new Paragraph(""));

		document.add(new Paragraph("Benefits").setFontSize(9).setBold().setUnderline().setCharacterSpacing(1));

		//////////////////////////// Benefits Table
		//////////////////////////// FORMAT//////////////////////////////////////

		// Create Additional Benefits Table
		/* Declaring column sizes of the table respectively */
		float[] pointColumnWidths4 = { 500, 80, 80, 80, 80, 80, 80 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		// table headings of the Living Benefits
		Cell alCellth1 = new Cell(2, 0);
		alCellth1.setBorder(new SolidBorder(1));
		alCellth1.add(new Paragraph("Living Benefits").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1).setMarginTop(10));
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
			alCellBenf.setBorderLeft(new SolidBorder(1));

			// Getting ALL Benefits Name object and cast to an String
			String p = (String) hashMap.get("benName");
			String maturity = (String) hashMap.get("combination");

			System.out.println("Combinationnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn " + maturity.equalsIgnoreCase("L3"));

			// Check Maturity values not equl then Display
			if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {

				alCellBenf.add(new Paragraph(p).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(alCellBenf);
			}

			// Display Main Life Rider Amounts
			Cell alCellmA = new Cell();
			if (hashMap.get("mainAmt") != null) {

				// Getting ALL Benefits Name Combinations object and cast to an String
				String comb = (String) hashMap.get("combination");

				/* If benefit is WPB Print Amount as APPLIED */
				if (comb.equalsIgnoreCase("WPB")) {
					alCellmA.add(new Paragraph("Applied").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmA);

					// Check Maturity values not equl then Display
				} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
						&& !maturity.equalsIgnoreCase("L5")) {

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

			// Display Main Life Rider Premium
			Cell alCellmP = new Cell();
			if (hashMap.get("mainPre") != null) {

				if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
						&& !maturity.equalsIgnoreCase("L5")) {
					Double mPre = (Double) hashMap.get("mainPre");

					alCellmP.add(
							new Paragraph(formatter.format(mPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmP);
				}

			} else {
				alCellmP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellmP);
			}

			// Display Spouse Rider Amounts
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

				// IF spouse Rider Not Equals Maturity Values and Null
			} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {
				alCellsA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsA);
			}

			// Display Spouse Rider Premium
			Cell alCellsP = new Cell();
			if (hashMap.get("spousePre") != null) {
				Double spPre = (Double) hashMap.get("spousePre");

				alCellsP.add(
						new Paragraph(formatter.format(spPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsP);
			} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {
				alCellsP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsP);
			}

			// Display Child Rider Amounts
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

				// IF Child Riders Are not equal Maturities and Amount is null
			} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {

				alCellcA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcA);
			}

			// Display Child Riders Premium
			Cell alCellcP = new Cell();
			alCellcP.setBorderRight(new SolidBorder(1));

			if (hashMap.get("childPre") != null) {

				Double cPre = (Double) hashMap.get("childPre");

				alCellcP.add(
						new Paragraph(formatter.format(cPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcP);

			} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {
				alCellcP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcP);
			}

		}

		benAddTable.startNewRow();

		// Creating Empty Cell to seperate Additional Benefits
		Cell abCellAddEty = new Cell(0, 7);
		abCellAddEty.setBorder(Border.NO_BORDER);
		benAddTable.addCell(abCellAddEty);

		benAddTable.startNewRow();

		////////////////////// table headings of the Additional Benefits
		////////////////////// table\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		Cell abCellth1 = new Cell(2, 0);
		abCellth1.setBorder(new SolidBorder(1));
		abCellth1.add(new Paragraph("Additional Benefits").setFontSize(9).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1).setMarginTop(10));
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
		abCelld1.setBorderLeft(new SolidBorder(1));
		abCelld1.add(new Paragraph("Natural Death Cover").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld1);

		Cell abCelld2 = new Cell();
		if (ndc == 0.0) {
			abCelld2.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld2.add(new Paragraph(formatter.format(ndc)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld2);

		Cell abCelld3 = new Cell();
		if (ndcp == 0.0) {
			abCelld3.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld3.add(new Paragraph(formatter.format(ndcp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld3);

		Cell abCelld4 = new Cell();
		if (ndcs == 0.0) {
			abCelld4.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld4.add(new Paragraph(formatter.format(ndcs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld4);

		Cell abCelld5 = new Cell();
		if (ndcsp == 0.0) {
			abCelld5.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld5.add(new Paragraph(formatter.format(ndcsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld5);

		Cell abCelld6 = new Cell();
		abCelld6.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld6);

		Cell abCelld7 = new Cell();
		abCelld7.setBorderRight(new SolidBorder(1));

		abCelld7.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld7);

		benAddTable.startNewRow();

		Cell abCelld8 = new Cell();
		abCelld8.setBorderLeft(new SolidBorder(1));

		abCelld8.add(new Paragraph("Accidental Death Benefit").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld8);

		Cell abCelld9 = new Cell();
		if (adb == 0.0) {
			abCelld9.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld9.add(new Paragraph(formatter.format(adb)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld9);

		Cell abCelld10 = new Cell();
		if (adbp == 0.0) {
			abCelld10.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld10.add(new Paragraph(formatter.format(adbp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld10);

		Cell abCelld11 = new Cell();
		if (adbs == 0.0) {
			abCelld11.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld11.add(new Paragraph(formatter.format(adbs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld11);

		Cell abCelld12 = new Cell();
		if (adbsp == 0.0) {
			abCelld12.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld12.add(new Paragraph(formatter.format(adbsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld12);

		Cell abCelld13 = new Cell();
		abCelld13.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld13);

		Cell abCelld14 = new Cell();
		abCelld14.setBorderRight(new SolidBorder(1));

		abCelld14.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld14);

		benAddTable.startNewRow();

		Cell abCelld15 = new Cell();
		abCelld15.setBorderLeft(new SolidBorder(1));
		abCelld15.setBorderBottom(new SolidBorder(1));

		abCelld15.add(new Paragraph("Funeral Expenses").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld15);

		Cell abCelld16 = new Cell();
		abCelld16.setBorderBottom(new SolidBorder(1));
		if (feb == 0.0) {
			abCelld16.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld16.add(new Paragraph(formatter.format(feb)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld16);

		Cell abCelld117 = new Cell();
		abCelld117.setBorderBottom(new SolidBorder(1));
		if (febp == 0.0) {
			abCelld117.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld117.add(new Paragraph(formatter.format(febp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld117);

		Cell abCelld18 = new Cell();
		abCelld18.setBorderBottom(new SolidBorder(1));
		if (febs == 0.0) {
			abCelld18.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld18.add(new Paragraph(formatter.format(febs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld18);

		Cell abCelld19 = new Cell();
		abCelld19.setBorderBottom(new SolidBorder(1));
		if (febsp == 0.0) {
			abCelld19.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld19.add(new Paragraph(formatter.format(febsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld19);

		Cell abCelld20 = new Cell();
		abCelld20.setBorderBottom(new SolidBorder(1));
		abCelld20.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld20);

		Cell abCelld21 = new Cell();
		abCelld21.setBorderBottom(new SolidBorder(1));
		abCelld21.setBorderRight(new SolidBorder(1));
		abCelld21.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld21);

		///////////////////// End Of Additional Benefits Table
		///////////////////// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		document.add(benAddTable);

		/////////////////////////// END Of
		/////////////////////////// Benefits Table
		/////////////////////////// FORMAT///////////////////////////////////////

		//////////////////////////// * Medical Requirements
		//////////////////////////// Table*/////////////////////////
		java.util.List<MedicalRequirementsHelper> medicalDetails = medicalRequirementsDaoCustom
				.findByQuoDetail(quotationDetails.getQdId());

		document.add(new Paragraph(""));

		document.add(new Paragraph("Medical Requirements").setBold().setFontSize(9).setUnderline()
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));

		document.add(new Paragraph(""));

		// Medical Requirements Table
		float[] pointColumnWidths6 = { 160, 500 };
		Table medReqTable = new Table(pointColumnWidths6);
		medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell mrqCell1 = new Cell();
		mrqCell1.setBorder(Border.NO_BORDER);
		mrqCell1.add(new Paragraph("Main Life").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setCharacterSpacing(1));
		medReqTable.addCell(mrqCell1);

		if (medicalDetails.isEmpty()) {

			Cell mrqCell2 = new Cell();
			mrqCell2.setBorder(Border.NO_BORDER);
			mrqCell2.add(new Paragraph(": Not Applied ").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			medReqTable.addCell(mrqCell2);

		} else {

			// Creating a String Seperator ArrayList Seperate from seperator
			final String SEPARATOR = " , ";

			StringBuilder mainLife = new StringBuilder();
			StringBuilder spouse = new StringBuilder();

			for (MedicalRequirementsHelper medicalReq : medicalDetails) {

				// When MainLife having medical requiremnents
				if (medicalReq.getMainStatus().equalsIgnoreCase("R")) {
					mainLife.append(medicalReq.getMedicalReqname());
					mainLife.append(SEPARATOR);
					// When MainLife not having medical Requirements
				} else if (medicalReq.getMainStatus().equalsIgnoreCase("NR")) {

				} else {
					mainLife.append("NR");
					mainLife.append(SEPARATOR);
				}

				// When Spouse having medical Requirements
				if (medicalReq.getSpouseStatus().equalsIgnoreCase("R")) {
					spouse.append(medicalReq.getMedicalReqname());
					spouse.append(SEPARATOR);

					// When Spouse Not having medical Requirements
				} else if (medicalReq.getSpouseStatus().equalsIgnoreCase("NR")) {

				} else {
					spouse.append("NR");
					spouse.append(SEPARATOR);
				}

			}

			String mainMedical = mainLife.toString();
			mainMedical = mainMedical.substring(0, mainMedical.length() - SEPARATOR.length());

			Cell mrqCell2 = new Cell();
			mrqCell2.setBorder(Border.NO_BORDER);
			mrqCell2.add(new Paragraph(": " + mainMedical).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			medReqTable.addCell(mrqCell2);

			medReqTable.startNewRow();

			if (benefitsSpouse.isEmpty()) {

			} else {

				String spouseMedical = spouse.toString();

				// System.out.println("spouse medicalllllllllllllllll " + spouseMedical);

				if (!spouseMedical.endsWith("NR") && !spouseMedical.isEmpty()) {

					spouseMedical = spouseMedical.substring(0, spouseMedical.length() - SEPARATOR.length());

					Cell mrqCell3 = new Cell();
					mrqCell3.setBorder(Border.NO_BORDER);
					mrqCell3.add(new Paragraph("Spouse").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
							.setFixedLeading(10).setCharacterSpacing(1));
					medReqTable.addCell(mrqCell3);

					Cell mrqCell4 = new Cell();
					mrqCell4.setBorder(Border.NO_BORDER);
					mrqCell4.add(new Paragraph(": " + spouseMedical).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
							.setFixedLeading(10));
					medReqTable.addCell(mrqCell4);
				}
			}

		}

		document.add(medReqTable);

		// try {
		//
		// if (medicalDetails.isEmpty()) {
		//
		// } else {
		// document.add(new Paragraph(""));
		//
		// document.add(new Paragraph("Medical
		// Requirements").setBold().setFontSize(9).setUnderline()
		// .setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));
		//
		// // Medical Requirements Table
		// float[] pointColumnWidths6 = { 100, 500 };
		// Table medReqTable = new Table(pointColumnWidths6);
		// medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
		//
		// Creating a String Seperator ArrayList Seperate from seperator
		// final String SEPARATOR = " , ";
		//
		// StringBuilder mainLife = new StringBuilder();
		// StringBuilder spouse = new StringBuilder();
		//
		// for (MedicalRequirementsHelper medicalReq : medicalDetails) {
		//
		// // When MainLife having medical requiremnents
		// if (medicalReq.getMainStatus().equalsIgnoreCase("R")) {
		// mainLife.append(medicalReq.getMedicalReqname());
		// mainLife.append(SEPARATOR);
		// // When MainLife not having medical Requirements
		// } else if (medicalReq.getMainStatus().equalsIgnoreCase("NR")) {
		//
		// } else {
		// mainLife.append("NR");
		// mainLife.append(SEPARATOR);
		// }
		//
		// // When Spouse having medical Requirements
		// if (medicalReq.getSpouseStatus().equalsIgnoreCase("R")) {
		// spouse.append(medicalReq.getMedicalReqname());
		// spouse.append(SEPARATOR);
		//
		// // When Spouse Not having medical Requirements
		// } else if (medicalReq.getSpouseStatus().equalsIgnoreCase("NR")) {
		//
		// } else {
		// spouse.append("NR");
		// spouse.append(SEPARATOR);
		// }
		//
		// }
		//
		// String mainMedical = mainLife.toString();
		// mainMedical = mainMedical.substring(0, mainMedical.length() -
		// SEPARATOR.length());
		//
		// Cell mrqCell1 = new Cell();
		// mrqCell1.setBorder(Border.NO_BORDER);
		// mrqCell1.add(new Paragraph("Main
		// Life").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
		// .setFixedLeading(10).setCharacterSpacing(1));
		// medReqTable.addCell(mrqCell1);
		//
		// Cell mrqCell2 = new Cell();
		// mrqCell2.setBorder(Border.NO_BORDER);
		// mrqCell2.add(new Paragraph(": " +
		// mainMedical).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
		// .setFixedLeading(10));
		// medReqTable.addCell(mrqCell2);
		//
		// medReqTable.startNewRow();
		//
		// if (benefitsSpouse.isEmpty()) {
		//
		// } else {
		//
		// String spouseMedical = spouse.toString();
		//
		// // System.out.println("spouse medicalllllllllllllllll " + spouseMedical);
		//
		// if (!spouseMedical.endsWith("NR") && !spouseMedical.isEmpty()) {
		//
		// spouseMedical = spouseMedical.substring(0, spouseMedical.length() -
		// SEPARATOR.length());
		//
		// Cell mrqCell3 = new Cell();
		// mrqCell3.setBorder(Border.NO_BORDER);
		// mrqCell3.add(new Paragraph("Spouse").setBold().setFontSize(9)
		// .setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));
		// medReqTable.addCell(mrqCell3);
		//
		// Cell mrqCell4 = new Cell();
		// mrqCell4.setBorder(Border.NO_BORDER);
		// mrqCell4.add(new Paragraph(": " + spouseMedical).setFontSize(9)
		// .setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		// medReqTable.addCell(mrqCell4);
		// }
		// }
		//
		// document.add(medReqTable);
		//
		// }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		//
		// }

		/////////////////////// *Start FinanCial Requirements*///////////////////

		// Creating A variable for Sum At Risk
		Double fiveM = 5000000.00;

		System.out.println(
				"sum at riskkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk" + quotationDetails.getSumAtRiskMain());

		if (quotationDetails.getSumAtRiskMain() != null) {

			document.add(new Paragraph(""));

			float[] finacialReqColoumWidth = { 150, 500 };
			Table finReqTbl = new Table(finacialReqColoumWidth);
			finReqTbl.setHorizontalAlignment(HorizontalAlignment.LEFT);

			Cell fReqCell1 = new Cell();
			fReqCell1.setBorder(Border.NO_BORDER);
			fReqCell1.add(new Paragraph("Financial Requirements").setBold().setFontSize(9).setUnderline()
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));
			finReqTbl.addCell(fReqCell1);

			Cell fReqCell2 = new Cell();

			// If Sum At Risk> 5000000
			if (quotationDetails.getSumAtRiskMain() >= fiveM) {

				fReqCell2.setBorder(Border.NO_BORDER);
				fReqCell2.add(new Paragraph(": Applied").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else {
				fReqCell2.setBorder(Border.NO_BORDER);
				fReqCell2.add(new Paragraph(": Not Applied").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

			finReqTbl.addCell(fReqCell2);

			document.add(finReqTbl);

		}

		/////////////////////// *End of FinanCial Requirements*///////////////////

		document.add(new Paragraph(""));

		document.add(new Paragraph("Special Notes").setFontSize(9).setBold().setUnderline().setCharacterSpacing(1));

		// Creating a Special Notes List
		List list = new List(ListNumberingType.DECIMAL);
		list.setFontSize(9);

		ListItem item1 = new ListItem();
		item1.add(
				new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.")
						.setFontSize(9).setFixedLeading(10));
		list.add(item1);

		ListItem item2 = new ListItem();
		item2.add(new Paragraph("All amounts are in Sri Lankan Rupees(LKR).").setFontSize(9).setFixedLeading(10));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("Initial policy processing fee of Rs. 300 (Payable only with initial deposit).")
				.setFontSize(9).setFixedLeading(10));
		list.add(item3);

		ListItem item4 = new ListItem();
		item4.add(new Paragraph(
				"In event of death by accident both Accident Cover and Natural Death Cover will be applicable.")
						.setFontSize(9).setFixedLeading(10));
		list.add(item4);

		ListItem item5 = new ListItem();
		item5.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date of issue.")
				.setFontSize(9).setFixedLeading(10));
		list.add(item5);

		document.add(list);

		document.add(new Paragraph("This is a system generated report and therefore does not require a signature.")
				.setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setFixedPosition(50, 10, 500));

		document.close();

		return baos.toByteArray();
	}

	// Create Arpico School Fees Plan
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
		document.setTopMargin(30);
		document.setBottomMargin(10);

		// Agent Details
		float[] pointColumnWidths1 = { 90, 150 };
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
				agcell8.add(new Paragraph(": " + quotationDetails.getQuotation().getUser().getUserCode()).setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else if (val == 0) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": ............................ ").setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(
						new Paragraph(": ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
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

		document.add(new Paragraph("ARPICO SCHOOL FEES PLAN").setFontSize(9).setCharacterSpacing(1));
		document.add(new Paragraph("TOTAL PROTECTION OF CHILD SCHOOL FEE OR MONEY BACK GUARANTEE").setFontSize(9)
				.setCharacterSpacing(1).setFixedLeading(1));

		final SolidLine lineDrawer = new SolidLine(1f);
		document.add(new LineSeparator(lineDrawer));

		document.add(new Paragraph(""));

		// customer/Spouse/Plan Details
		float[] pointColumnWidths2 = { 300, 150, 70, 60, 200 };
		Table cusTable = new Table(pointColumnWidths2);
		cusTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		///////////////////////// *Craeting Main Life Details*/
		Cell cuCellTh = new Cell();
		cuCellTh.setBorder(Border.NO_BORDER);
		cuCellTh.add(new Paragraph("Main Life Details").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cuCellTh);

		Cell cucellTh1 = new Cell();
		cucellTh1.setBorder(Border.NO_BORDER);
		cucellTh1.add(new Paragraph("Occupation").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh1);

		Cell cucellTh2 = new Cell();
		cucellTh2.setBorder(Border.NO_BORDER);
		cucellTh2.add(
				new Paragraph("DOB").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh2);

		Cell cucellTh3 = new Cell();
		cucellTh3.setBorder(Border.NO_BORDER);
		cucellTh3.add(
				new Paragraph("Age").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh3);

		Cell cucellTh4 = new Cell();
		cucellTh4.setBorder(Border.NO_BORDER);
		cucellTh4.add(new Paragraph("Gender").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cucellTh4);
		cusTable.startNewRow();

		Cell cuCellM1 = new Cell();
		cuCellM1.setBorder(Border.NO_BORDER);
		cuCellM1.add(new Paragraph(quotationDetails.getCustomerDetails().getCustName() != null
				? quotationDetails.getCustomerDetails().getCustName()
				: " ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM1);

		Cell cuCellM2 = new Cell();
		cuCellM2.setBorder(Border.NO_BORDER);
		cuCellM2.add(new Paragraph(mainLifeOccupation != null ? mainLifeOccupation : " ").setFontSize(9)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM2);

		Cell cuCellM3 = new Cell();
		cuCellM3.setBorder(Border.NO_BORDER);

		// Creating a Date Format for DOB
		SimpleDateFormat mainDob = new SimpleDateFormat("dd-MM-yyyy");

		cuCellM3.add(new Paragraph(quotationDetails.getCustomerDetails().getCustDob() != null
				? mainDob.format(quotationDetails.getCustomerDetails().getCustDob())
				: " ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM3);

		Cell cuCellM4 = new Cell();
		cuCellM4.setBorder(Border.NO_BORDER);
		if (quoCustomer.getMainLifeAge() != null) {
			cuCellM4.add(new Paragraph(Integer.toString(quoCustomer.getMainLifeAge())).setFontSize(8)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCellM4.add(new Paragraph(" ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCellM4);

		Cell cuCellM5 = new Cell();
		cuCellM5.setBorder(Border.NO_BORDER);

		// Check MainLife Gender If M->Male, F->Female
		if (quotationDetails.getCustomerDetails().getCustGender() != null) {

			if (quotationDetails.getCustomerDetails().getCustGender().equalsIgnoreCase("M")) {
				cuCellM5.add(
						new Paragraph("Male").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			} else if (quotationDetails.getCustomerDetails().getCustGender().equalsIgnoreCase("F")) {
				cuCellM5.add(new Paragraph("Female").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		} else {

		}
		cusTable.addCell(cuCellM5);

		/////////////////////////// *End Craeting Main Life Details*/

		cusTable.startNewRow();

		// Create an Empty Line
		Cell cuCellEmptyM = new Cell(0, 5);
		cuCellEmptyM.setBorder(Border.NO_BORDER);
		cuCellEmptyM.add(new Paragraph("").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellEmptyM);

		//////////////////////// * Strat Creating Spouse Details*/

		// checking Spouse is active or not
		if ((quoCustomer.getSpouseName()) != null) {

			Cell cuCellThS = new Cell();
			cuCellThS.setBorder(Border.NO_BORDER);
			cuCellThS.add(new Paragraph("Spouse Details").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10).setBold());
			cusTable.addCell(cuCellThS);

			cusTable.startNewRow();

			Cell cuCellS1 = new Cell();
			cuCellS1.setBorder(Border.NO_BORDER);
			cuCellS1.add(new Paragraph(quoCustomer.getSpouseName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCellS1);

		} else {

		}

		if ((quoCustomer.getSpouseOccupation()) != null) {

			Cell cuCellS2 = new Cell();
			cuCellS2.setBorder(Border.NO_BORDER);
			cuCellS2.add(new Paragraph(spouseOccupation).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCellS2);

		} else {

		}

		if (quotationDetails.getSpouseDetails() != null) {

			Cell cuCellS3 = new Cell();
			cuCellS3.setBorder(Border.NO_BORDER);
			cuCellS3.add(new Paragraph(mainDob.format(quotationDetails.getSpouseDetails().getCustDob())).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCellS3);

		} else {

		}

		if ((quoCustomer.getSpouseAge()) != null) {

			Cell cuCellS4 = new Cell();
			cuCellS4.setBorder(Border.NO_BORDER);
			cuCellS4.add(new Paragraph(Integer.toString(quoCustomer.getSpouseAge())).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCellS4);

		} else {

		}

		// Display Spouse Gender
		Cell cuCellS5 = new Cell();
		cuCellS5.setBorder(Border.NO_BORDER);

		if ((quotationDetails.getSpouseDetails()) != null) {

			if (quotationDetails.getSpouseDetails().getCustGender().equalsIgnoreCase("M")) {
				cuCellS5.add(
						new Paragraph("Male").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			} else if (quotationDetails.getSpouseDetails().getCustGender().equalsIgnoreCase("F")) {
				cuCellS5.add(new Paragraph("Female").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		} else {

		}

		cusTable.addCell(cuCellS5);

		/////////////////////////////////// * End of Spouse Details*/

		cusTable.startNewRow();

		// Create an Empty Line
		Cell cuCellEmptyP = new Cell(0, 5);
		cuCellEmptyP.setBorder(Border.NO_BORDER);
		cuCellEmptyP.add(new Paragraph("").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellEmptyP);

		//////////////////// * Strat Creating Plan Details*/
		Cell cuCellPlanTh = new Cell();
		cuCellPlanTh.add(new Paragraph("Plan Details").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cuCellPlanTh);

		Cell cuCellP1 = new Cell();
		cuCellP1.add(new Paragraph("Term").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cuCellP1);

		Cell cuCellP2 = new Cell();
		cuCellP2.add(new Paragraph("Premium").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cuCellP2);

		Cell cuCellP3 = new Cell();
		cuCellP3.add(new Paragraph("Method").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setBold()
				.setFixedLeading(10));
		cusTable.addCell(cuCellP3);

		Cell cuCellP4 = new Cell();
		cuCellP4.add(new Paragraph("Monthly School Fee Expenses").setFontSize(9).setTextAlignment(TextAlignment.CENTER)
				.setBold().setFixedLeading(10));
		cusTable.addCell(cuCellP4);

		cusTable.startNewRow();

		// Display Product Code
		Cell cuCellP5 = new Cell();
		if (quotationDetails.getQuotation().getProducts().getProductCode() != null) {
			cuCellP5.add(new Paragraph(quotationDetails.getQuotation().getProducts().getProductCode()).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCellP5.add(new Paragraph(" ").setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCellP5);

		Cell cuCellP6 = new Cell();
		if (quotationDetails.getPolTerm() != null) {
			cuCellP6.add(new Paragraph(Integer.toString(quotationDetails.getPolTerm())).setFontSize(9)
					.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		} else {
			cuCellP6.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		}
		cusTable.addCell(cuCellP6);

		Cell cuCellP7 = new Cell();
		if (quoCustomer.getModePremium() != null) {
			cuCellP7.add(new Paragraph(formatter.format(quoCustomer.getTotPremium())).setFontSize(9)
					.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		} else {
			cuCellP7.add(new Paragraph(" ").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));

		}
		cusTable.addCell(cuCellP7);

		Cell cuCellP8 = new Cell();

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

			cuCellP8.add(new Paragraph(modeMethod).setFontSize(9).setTextAlignment(TextAlignment.CENTER)
					.setFixedLeading(10));

		}
		cusTable.addCell(cuCellP8);

		// Display Basic Sum Assured
		Cell cuCellP9 = new Cell();

		if (benefitsLife.isEmpty()) {

		} else {

			for (QuoBenf msfeb : benefitsLife) {
				System.out.println(msfeb.getRiderCode());

				if (msfeb.getRiderCode().equalsIgnoreCase("L10")) {

					if (msfeb.getRiderSum() != null) {
						cuCellP9.add(new Paragraph(
								formatter.format(msfeb.getRiderSum() / (12 * quotationDetails.getPolTerm())))
										.setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));

						cusTable.addCell(cuCellP9);

					} else {

					}

				}

			}

		}

		//////////////////// * End of Plan Details*/

		document.add(cusTable);
		//////////////////////////////// *End of MainLife/Spouse/Plan Details
		//////////////////////////////// Table*//////////

		document.add(new Paragraph("Benefits").setFontSize(9).setBold().setUnderline().setCharacterSpacing(1));

		//////////////////////////// Benefits Table
		//////////////////////////// FORMAT//////////////////////////////////////

		// Create Additional Benefits Table
		/* Declaring column sizes of the table respectively */
		float[] pointColumnWidths4 = { 500, 80, 80, 80, 80, 80, 80 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		// table headings of the Living Benefits
		Cell alCellth1 = new Cell(2, 0);
		alCellth1.setBorder(new SolidBorder(1));
		alCellth1.add(new Paragraph("Living Benefits").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1).setMarginTop(10));
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
				if (!quoBenf.getRiderCode().equalsIgnoreCase("L10") && (!quoBenf.getRiderCode().equalsIgnoreCase("ADB"))
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
			alCellBenf.setBorderLeft(new SolidBorder(1));

			// Getting ALL Benefits Name object and cast to an String
			String p = (String) hashMap.get("benName");
			String maturity = (String) hashMap.get("combination");

			System.out.println("Combinationnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn " + maturity.equalsIgnoreCase("L3"));

			// Check Maturity values not equl then Display
			if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {

				alCellBenf.add(new Paragraph(p).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(alCellBenf);
			}

			// Display Main Life Rider Amounts
			Cell alCellmA = new Cell();
			if (hashMap.get("mainAmt") != null) {

				// Getting ALL Benefits Name Combinations object and cast to an String
				String comb = (String) hashMap.get("combination");

				/* If benefit is WPB Print Amount as APPLIED */
				if (comb.equalsIgnoreCase("WPB")) {
					alCellmA.add(new Paragraph("Applied").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmA);

					// Check Maturity values not equl then Display
				} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
						&& !maturity.equalsIgnoreCase("L5")) {

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

			// Display Main Life Rider Premium
			Cell alCellmP = new Cell();
			if (hashMap.get("mainPre") != null) {

				if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
						&& !maturity.equalsIgnoreCase("L5")) {
					Double mPre = (Double) hashMap.get("mainPre");

					if (mPre == 0.0) {
						alCellmP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
						benAddTable.addCell(alCellmP);
					} else {
						alCellmP.add(new Paragraph(formatter.format(mPre)).setFontSize(9)
								.setTextAlignment(TextAlignment.RIGHT));
						benAddTable.addCell(alCellmP);
					}

				}

			} else {
				alCellmP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellmP);
			}

			// Display Spouse Rider Amounts
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

				// IF spouse Rider Null
			} else {
				alCellsA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsA);
			}

			// Display Spouse Rider Premium
			Cell alCellsP = new Cell();
			Double spPre = (Double) hashMap.get("spousePre");

			if (hashMap.get("spousePre") != null) {

				// Check if Premium is 0 display '-'
				if (spPre == 0.0) {
					alCellsP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellsP);

					// Display the Premium Amount
				} else {
					alCellsP.add(new Paragraph(formatter.format(spPre)).setFontSize(9)
							.setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellsP);
				}

				// if premium is null
			} else {
				alCellsP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsP);
			}

			// Display Child Rider Amounts
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

			} else {

				alCellcA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcA);
			}

			// Display Child Riders Premium
			Cell alCellcP = new Cell();
			alCellcP.setBorderRight(new SolidBorder(1));

			if (hashMap.get("childPre") != null) {
				Double cPre = (Double) hashMap.get("childPre");

				// Check if Premium is 0 display '-'
				if (cPre == 0.0) {
					alCellcP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellcP);

					// Display the Premium Amount
				} else {

					alCellcP.add(
							new Paragraph(formatter.format(cPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellcP);
				}

				// If Premium is null
			} else {
				alCellcP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcP);
			}

		}

		benAddTable.startNewRow();

		// Creating Empty Cell to seperate Additional Benefits
		Cell abCellAddEty = new Cell(0, 7);
		abCellAddEty.setBorder(Border.NO_BORDER);
		benAddTable.addCell(abCellAddEty);

		benAddTable.startNewRow();

		////////////////////// table headings of the Additional Benefits
		////////////////////// table\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		Cell abCellth1 = new Cell(2, 0);
		abCellth1.setBorder(new SolidBorder(1));
		abCellth1.add(new Paragraph("Additional Benefits").setFontSize(9).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1).setMarginTop(10));
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

		/*
		 * Declaring Variable for School Fee Expenses/Main(Amount/Premium) Accidental
		 * Death benefit/Main(Amount/Premium) Funeral Expenses/Main(Amount/Premium)
		 */
		Double msfeb = 0.0;
		Double msfebp = 0.0;

		Double adb = 0.0;
		Double adbp = 0.0;

		Double feb = 0.0;
		Double febp = 0.0;

		if (benefitsLife.isEmpty()) {

		} else {

			for (QuoBenf quoAddBenf : benefitsLife) {

				if (quoAddBenf.getRiderCode().equalsIgnoreCase("L10")) {
					/*
					 * School Fees Expenses Benf = L10 /(12 * Policy Term )
					 */
					msfeb = msfeb + (quoAddBenf.getRiderSum() / (12 * quotationDetails.getPolTerm()));
					msfebp = msfebp + quoAddBenf.getPremium();

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

		/* Printing All Additional Benefits Data */
		Cell abCelld1 = new Cell();
		abCelld1.setBorderLeft(new SolidBorder(1));
		abCelld1.add(new Paragraph("Monthly School Fee Expenses Benefit").setFontSize(9)
				.setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld1);

		Cell abCelld2 = new Cell();
		if (msfeb == 0.0) {
			abCelld2.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld2.add(new Paragraph(formatter.format(msfeb)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld2);

		Cell abCelld3 = new Cell();

		if (msfebp == 0.0) {
			abCelld3.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
			benAddTable.addCell(abCelld3);
		} else {
			abCelld3.add(new Paragraph(formatter.format(msfebp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
			benAddTable.addCell(abCelld3);
		}

		Cell abCelld4 = new Cell();
		abCelld4.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld4);

		Cell abCelld5 = new Cell();
		abCelld5.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld5);

		Cell abCelld6 = new Cell();
		abCelld6.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld6);

		Cell abCelld7 = new Cell();
		abCelld7.setBorderRight(new SolidBorder(1));

		abCelld7.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld7);

		benAddTable.startNewRow();

		Cell abCelld8 = new Cell();
		abCelld8.setBorderLeft(new SolidBorder(1));

		abCelld8.add(
				new Paragraph("Accidental Death Benefit Benefit").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld8);

		Cell abCelld9 = new Cell();
		if (adb == 0.0) {
			abCelld9.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld9.add(new Paragraph(formatter.format(adb)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld9);

		Cell abCelld10 = new Cell();
		if (adbp == 0.0) {
			abCelld10.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
			benAddTable.addCell(abCelld10);
		} else {
			abCelld10.add(new Paragraph(formatter.format(adbp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
			benAddTable.addCell(abCelld10);
		}

		Cell abCelld11 = new Cell();
		abCelld11.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld11);

		Cell abCelld12 = new Cell();
		abCelld12.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld12);

		Cell abCelld13 = new Cell();
		abCelld13.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld13);

		Cell abCelld14 = new Cell();
		abCelld14.setBorderRight(new SolidBorder(1));

		abCelld14.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld14);

		benAddTable.startNewRow();

		Cell abCelld15 = new Cell();
		abCelld15.setBorderLeft(new SolidBorder(1));
		abCelld15.setBorderBottom(new SolidBorder(1));

		abCelld15.add(new Paragraph("Funeral Expenses Benefit").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld15);

		Cell abCelld16 = new Cell();
		abCelld16.setBorderBottom(new SolidBorder(1));
		if (feb == 0.0) {
			abCelld16.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld16.add(new Paragraph(formatter.format(feb)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld16);

		Cell abCelld117 = new Cell();
		abCelld117.setBorderBottom(new SolidBorder(1));
		if (febp == 0.0) {
			abCelld117.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
			benAddTable.addCell(abCelld117);

		} else {
			abCelld117.add(new Paragraph(formatter.format(febp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
			benAddTable.addCell(abCelld117);

		}

		Cell abCelld18 = new Cell();
		abCelld18.setBorderBottom(new SolidBorder(1));
		abCelld18.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld18);

		Cell abCelld19 = new Cell();
		abCelld19.setBorderBottom(new SolidBorder(1));
		abCelld19.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld19);

		Cell abCelld20 = new Cell();
		abCelld20.setBorderBottom(new SolidBorder(1));
		abCelld20.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld20);

		Cell abCelld21 = new Cell();
		abCelld21.setBorderBottom(new SolidBorder(1));
		abCelld21.setBorderRight(new SolidBorder(1));
		abCelld21.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld21);

		///////////////////// End Of Additional Benefits Table
		///////////////////// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		document.add(benAddTable);

		/////////////////////////// END Of
		/////////////////////////// Benefits Table
		/////////////////////////// FORMAT///////////////////////////////////////

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
												RoundingMode.HALF_UP)).setScale(2).doubleValue())).setFontSize(9));

		//////////////////////////// * Medical Requirements
		//////////////////////////// Table*/////////////////////////
		java.util.List<MedicalRequirementsHelper> medicalDetails = medicalRequirementsDaoCustom
				.findByQuoDetail(quotationDetails.getQdId());

		document.add(new Paragraph("Medical Requirements").setBold().setFontSize(8).setUnderline()
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));

		// document.add(new Paragraph(""));

		// Medical Requirements Table
		float[] pointColumnWidths6 = { 160, 500 };
		Table medReqTable = new Table(pointColumnWidths6);
		medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell mrqCell1 = new Cell();
		mrqCell1.setBorder(Border.NO_BORDER);
		mrqCell1.add(new Paragraph("Main Life").setBold().setFontSize(8).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setCharacterSpacing(1));
		medReqTable.addCell(mrqCell1);

		if (medicalDetails.isEmpty()) {

			Cell mrqCell2 = new Cell();
			mrqCell2.setBorder(Border.NO_BORDER);
			mrqCell2.add(new Paragraph(": Not Applied ").setFontSize(8).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			medReqTable.addCell(mrqCell2);

		} else {

			// Creating a String Seperator ArrayList Seperate from seperator
			final String SEPARATOR = " , ";

			StringBuilder mainLife = new StringBuilder();
			StringBuilder spouse = new StringBuilder();

			for (MedicalRequirementsHelper medicalReq : medicalDetails) {

				// When MainLife having medical requiremnents
				if (medicalReq.getMainStatus().equalsIgnoreCase("R")) {
					mainLife.append(medicalReq.getMedicalReqname());
					mainLife.append(SEPARATOR);
					// When MainLife not having medical Requirements
				} else if (medicalReq.getMainStatus().equalsIgnoreCase("NR")) {

				} else {
					mainLife.append("NR");
					mainLife.append(SEPARATOR);
				}

				// When Spouse having medical Requirements
				if (medicalReq.getSpouseStatus().equalsIgnoreCase("R")) {
					spouse.append(medicalReq.getMedicalReqname());
					spouse.append(SEPARATOR);

					// When Spouse Not having medical Requirements
				} else if (medicalReq.getSpouseStatus().equalsIgnoreCase("NR")) {

				} else {
					spouse.append("NR");
					spouse.append(SEPARATOR);
				}

			}

			String mainMedical = mainLife.toString();
			mainMedical = mainMedical.substring(0, mainMedical.length() - SEPARATOR.length());

			Cell mrqCell2 = new Cell();
			mrqCell2.setBorder(Border.NO_BORDER);
			mrqCell2.add(new Paragraph(": " + mainMedical).setFontSize(8).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			medReqTable.addCell(mrqCell2);

			medReqTable.startNewRow();

			if (benefitsSpouse.isEmpty()) {

			} else {

				String spouseMedical = spouse.toString();

				// System.out.println("spouse medicalllllllllllllllll " + spouseMedical);

				if (!spouseMedical.endsWith("NR") && !spouseMedical.isEmpty()) {

					spouseMedical = spouseMedical.substring(0, spouseMedical.length() - SEPARATOR.length());

					Cell mrqCell3 = new Cell();
					mrqCell3.setBorder(Border.NO_BORDER);
					mrqCell3.add(new Paragraph("Spouse").setBold().setFontSize(8).setTextAlignment(TextAlignment.LEFT)
							.setFixedLeading(10).setCharacterSpacing(1));
					medReqTable.addCell(mrqCell3);

					Cell mrqCell4 = new Cell();
					mrqCell4.setBorder(Border.NO_BORDER);
					mrqCell4.add(new Paragraph(": " + spouseMedical).setFontSize(8).setTextAlignment(TextAlignment.LEFT)
							.setFixedLeading(10));
					medReqTable.addCell(mrqCell4);
				}
			}

		}

		document.add(medReqTable);

		// try {
		//
		// if (medicalDetails.isEmpty()) {
		//
		// } else {
		//
		// document.add(new Paragraph("Medical
		// Requirements").setBold().setFontSize(9).setUnderline()
		// .setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));
		//
		// // Medical Requirements Table
		// float[] pointColumnWidths6 = { 100, 500 };
		// Table medReqTable = new Table(pointColumnWidths6);
		// medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
		//
		// // Creating a String Seperator ArrayList Seperate from seperator
		// final String SEPARATOR = " , ";
		//
		// StringBuilder mainLife = new StringBuilder();
		// StringBuilder spouse = new StringBuilder();
		//
		// for (MedicalRequirementsHelper medicalReq : medicalDetails) {
		//
		// // When MainLife having medical requiremnents
		// if (medicalReq.getMainStatus().equalsIgnoreCase("R")) {
		// mainLife.append(medicalReq.getMedicalReqname());
		// mainLife.append(SEPARATOR);
		// // When MainLife not having medical Requirements
		// } else if (medicalReq.getMainStatus().equalsIgnoreCase("NR")) {
		//
		// } else {
		// mainLife.append("NR");
		// mainLife.append(SEPARATOR);
		// }
		//
		// // When Spouse having medical Requirements
		// if (medicalReq.getSpouseStatus().equalsIgnoreCase("R")) {
		// spouse.append(medicalReq.getMedicalReqname());
		// spouse.append(SEPARATOR);
		//
		// // When Spouse Not having medical Requirements
		// } else if (medicalReq.getSpouseStatus().equalsIgnoreCase("NR")) {
		//
		// } else {
		// spouse.append("NR");
		// spouse.append(SEPARATOR);
		// }
		//
		// }
		//
		// String mainMedical = mainLife.toString();
		// mainMedical = mainMedical.substring(0, mainMedical.length() -
		// SEPARATOR.length());
		//
		// Cell mrqCell1 = new Cell();
		// mrqCell1.setBorder(Border.NO_BORDER);
		// mrqCell1.add(new Paragraph("Main
		// Life").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
		// .setFixedLeading(10).setCharacterSpacing(1));
		// medReqTable.addCell(mrqCell1);
		//
		// Cell mrqCell2 = new Cell();
		// mrqCell2.setBorder(Border.NO_BORDER);
		// mrqCell2.add(new Paragraph(": " +
		// mainMedical).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
		// .setFixedLeading(10));
		// medReqTable.addCell(mrqCell2);
		//
		// medReqTable.startNewRow();
		//
		// if (benefitsSpouse.isEmpty()) {
		//
		// } else {
		//
		// String spouseMedical = spouse.toString();
		//
		// // System.out.println("spouse medicalllllllllllllllll " + spouseMedical);
		//
		// if (!spouseMedical.endsWith("NR") && !spouseMedical.isEmpty()) {
		//
		// spouseMedical = spouseMedical.substring(0, spouseMedical.length() -
		// SEPARATOR.length());
		//
		// Cell mrqCell3 = new Cell();
		// mrqCell3.setBorder(Border.NO_BORDER);
		// mrqCell3.add(new Paragraph("Spouse").setBold().setFontSize(9)
		// .setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));
		// medReqTable.addCell(mrqCell3);
		//
		// Cell mrqCell4 = new Cell();
		// mrqCell4.setBorder(Border.NO_BORDER);
		// mrqCell4.add(new Paragraph(": " + spouseMedical).setFontSize(9)
		// .setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		// medReqTable.addCell(mrqCell4);
		// }
		// }
		//
		// document.add(medReqTable);
		//
		// }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		//
		// }

		/////////////////////// *Start FinanCial Requirements*///////////////////

		// Creating A variable for Sum At Risk
		Double fiveM = 5000000.00;

		System.out.println(
				"sum at riskkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk" + quotationDetails.getSumAtRiskMain());

		if (quotationDetails.getSumAtRiskMain() != null) {

			document.add(new Paragraph(""));

			float[] finacialReqColoumWidth = { 150, 500 };
			Table finReqTbl = new Table(finacialReqColoumWidth);
			finReqTbl.setHorizontalAlignment(HorizontalAlignment.LEFT);

			Cell fReqCell1 = new Cell();
			fReqCell1.setBorder(Border.NO_BORDER);
			fReqCell1.add(new Paragraph("Financial Requirements").setBold().setFontSize(8).setUnderline()
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));
			finReqTbl.addCell(fReqCell1);

			Cell fReqCell2 = new Cell();

			// If Sum At Risk> 5000000
			if (quotationDetails.getSumAtRiskMain() >= fiveM) {

				fReqCell2.setBorder(Border.NO_BORDER);
				fReqCell2.add(new Paragraph(": Applied").setFontSize(8).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else {
				fReqCell2.setBorder(Border.NO_BORDER);
				fReqCell2.add(new Paragraph(": Not Applied").setFontSize(8).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

			finReqTbl.addCell(fReqCell2);

			document.add(finReqTbl);

		}

		/////////////////////// *End of FinanCial Requirements*///////////////////

		// document.add(new Paragraph(""));

		document.add(new Paragraph("Special Notes").setFontSize(9).setBold().setUnderline().setCharacterSpacing(1));

		// Creating a Special Notes List
		List list = new List(ListNumberingType.DECIMAL);
		list.setFontSize(9);

		ListItem item1 = new ListItem();
		item1.add(
				new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.")
						.setFontSize(9).setFixedLeading(9));
		list.add(item1);

		ListItem item2 = new ListItem();
		item2.add(new Paragraph("All amounts are in Sri Lankan Rupees (LKR).").setFontSize(9).setFixedLeading(9));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("Initial policy processing fee of Rs 300 (Payable only with initial deposit).")
				.setFontSize(9).setFixedLeading(9));
		list.add(item3);

		ListItem item4 = new ListItem();
		item4.add(new Paragraph("This is only a Quotation and not an Acceptance of Risk.").setFontSize(9)
				.setFixedLeading(9));
		list.add(item4);

		ListItem item5 = new ListItem();
		item5.add(new Paragraph(
				"In the case of Death of the child and if no claim has been made on the primary benefit during the policy term, Total premium paid up to date on the Primary Benefit (MSFB) will be refundered.")
						.setFontSize(9).setFixedLeading(9));
		list.add(item5);

		ListItem item6 = new ListItem();
		item6.add(new Paragraph(
				"If no claim has been made on the primary benefit during the policy term, total premium paid on the Primary Benefit (MSFB) premium will be refundered at the policy expiry date.")
						.setFontSize(9).setFixedLeading(9));
		list.add(item6);

		ListItem item7 = new ListItem();
		item7.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date of issue.")
				.setFontSize(9).setFixedLeading(9));
		list.add(item7);

		document.add(list);

		document.add(new Paragraph("This is a system generated report and therefore does not require a signature.")
				.setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setFixedPosition(50, 10, 500));

		document.close();

		return baos.toByteArray();
	}

	@Override
	public byte[] createARPReport(QuotationDetails quotationDetails, QuotationView quotationView,
			QuoCustomer quoCustomer) throws Exception {
		//System.out.println("8888888888");
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
		document.setTopMargin(30);
		document.setBottomMargin(20);

		document.setBackgroundColor(ColorConstants.BLUE);
		// Agent Details
		float[] pointColumnWidths1 = { 90, 150 };
		Table agtTable = new Table(pointColumnWidths1);
		agtTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell agCell1 = new Cell();
		agCell1.setBorder(Border.NO_BORDER);
		agCell1.add(new Paragraph("Date").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell1);

		Cell agcell2 = new Cell();
		agcell2.setBorder(Border.NO_BORDER);
		agcell2.add(new Paragraph(": " + date).setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell2);

		agtTable.startNewRow();

		Cell agCellQId = new Cell();
		agCellQId.setBorder(Border.NO_BORDER);
		agCellQId.add(
				new Paragraph("Quotation No ").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCellQId);
		Cell agCellQ = new Cell();
		agCellQ.setBorder(Border.NO_BORDER);
		agCellQ.add(new Paragraph(": " + quotationDetails.getQuotation().getId()).setFontSize(8)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCellQ);

		agtTable.startNewRow();

		Cell agCell3 = new Cell();
		agCell3.setBorder(Border.NO_BORDER);
		agCell3.add(
				new Paragraph("Branch Name").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell3);
		Cell agcell4 = new Cell();
		agcell4.setBorder(Border.NO_BORDER);
		agcell4.add(new Paragraph(quotationDetails.getQuotation().getUser().getBranch().getBranch_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getBranch().getBranch_Name()
				: ": ").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell4);

		agtTable.startNewRow();

		Cell agCell5 = new Cell();
		agCell5.setBorder(Border.NO_BORDER);
		agCell5.add(
				new Paragraph("Agent Name").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell5);
		Cell agcell6 = new Cell();
		agcell6.setBorder(Border.NO_BORDER);
		agcell6.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Name()
				: ": ").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell6);

		agtTable.startNewRow();

		Cell agcell7 = new Cell();
		agcell7.setBorder(Border.NO_BORDER);
		agcell7.add(
				new Paragraph("Agent Code").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
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
				agcell8.add(new Paragraph(": " + quotationDetails.getQuotation().getUser().getUserCode()).setFontSize(8)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else if (val == 0) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": ............................ ").setFontSize(8)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(
						new Paragraph(": ").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			}
		} else {
			agcell8.setBorder(Border.NO_BORDER);
			agcell8.add(new Paragraph(": ").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}

		agtTable.addCell(agcell8);

		agtTable.startNewRow();

		Cell agcell9 = new Cell();
		agcell9.setBorder(Border.NO_BORDER);
		agcell9.add(
				new Paragraph("Contact No").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell9);

		Cell agCell10 = new Cell();
		agCell10.setBorder(Border.NO_BORDER);
		agCell10.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Mobile() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Mobile()
				: ": ").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell10);

		document.add(agtTable);

		document.add(new Paragraph(""));

		document.add(new Paragraph("ARPICO RELIEF PLAN").setFontSize(8).setCharacterSpacing(1));

		final SolidLine lineDrawer = new SolidLine(1);
		document.add(new LineSeparator(lineDrawer));
		document.add(new Paragraph(""));

		// customer/Spouse/Plan Details
		float[] pointColumnWidths2 = { 300, 150, 100, 100, 150 };
		Table cusTable = new Table(pointColumnWidths2);
		cusTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		///////////////////////// *Craeting Main Life Details*/
		Cell cuCellTh = new Cell();
		cuCellTh.setBorder(Border.NO_BORDER);
		cuCellTh.add(new Paragraph("Main Life Details").setFontSize(8).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cuCellTh);

		Cell cucellTh1 = new Cell();
		cucellTh1.setBorder(Border.NO_BORDER);
		cucellTh1.add(new Paragraph("Occupation").setFontSize(8).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh1);

		Cell cucellTh2 = new Cell();
		cucellTh2.setBorder(Border.NO_BORDER);
		cucellTh2.add(
				new Paragraph("DOB").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh2);

		Cell cucellTh3 = new Cell();
		cucellTh3.setBorder(Border.NO_BORDER);
		cucellTh3.add(
				new Paragraph("Age").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh3);

		Cell cucellTh4 = new Cell();
		cucellTh4.setBorder(Border.NO_BORDER);
		cucellTh4.add(new Paragraph("Gender").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cucellTh4);
		cusTable.startNewRow();

		Cell cuCellM1 = new Cell();
		cuCellM1.setBorder(Border.NO_BORDER);
		cuCellM1.add(new Paragraph(quotationDetails.getCustomerDetails().getCustName() != null
				? quotationDetails.getCustomerDetails().getCustName()
				: " ").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM1);

		Cell cuCellM2 = new Cell();
		cuCellM2.setBorder(Border.NO_BORDER);
		cuCellM2.add(new Paragraph(mainLifeOccupation != null ? mainLifeOccupation : " ").setFontSize(8)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM2);

		Cell cuCellM3 = new Cell();
		cuCellM3.setBorder(Border.NO_BORDER);

		// Creating a Date Format for DOB
		SimpleDateFormat mainDob = new SimpleDateFormat("dd-MM-yyyy");

		cuCellM3.add(new Paragraph(quotationDetails.getCustomerDetails().getCustDob() != null
				? mainDob.format(quotationDetails.getCustomerDetails().getCustDob())
				: " ").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM3);

		Cell cuCellM4 = new Cell();
		cuCellM4.setBorder(Border.NO_BORDER);
		if (quoCustomer.getMainLifeAge() != null) {
			cuCellM4.add(new Paragraph(Integer.toString(quoCustomer.getMainLifeAge())).setFontSize(8)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCellM4.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCellM4);

		Cell cuCellM5 = new Cell();
		cuCellM5.setBorder(Border.NO_BORDER);

		// Check MainLife Gender If M->Male, F->Female
		if (quotationDetails.getCustomerDetails().getCustGender() != null) {

			if (quotationDetails.getCustomerDetails().getCustGender().equalsIgnoreCase("M")) {
				cuCellM5.add(
						new Paragraph("Male").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			} else if (quotationDetails.getCustomerDetails().getCustGender().equalsIgnoreCase("F")) {
				cuCellM5.add(new Paragraph("Female").setFontSize(8).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		} else {

		}
		cusTable.addCell(cuCellM5);

		/////////////////////////// *End Craeting Main Life Details*/

		cusTable.startNewRow();

		// Create an Empty Line
		Cell cuCellEmptyM = new Cell(0, 5);
		cuCellEmptyM.setBorder(Border.NO_BORDER);
		cuCellEmptyM.add(new Paragraph("").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellEmptyM);

		//////////////////////// * Strat Creating Spouse Details*/

		// checking Spouse is active or not
		if ((quoCustomer.getSpouseName()) != null) {

			Cell cuCellThS = new Cell();
			cuCellThS.setBorder(Border.NO_BORDER);
			cuCellThS.add(new Paragraph("Spouse Details").setFontSize(8).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10).setBold());
			cusTable.addCell(cuCellThS);

			cusTable.startNewRow();

			Cell cuCellS1 = new Cell();
			cuCellS1.setBorder(Border.NO_BORDER);
			cuCellS1.add(new Paragraph(quoCustomer.getSpouseName()).setFontSize(8).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCellS1);

		} else {

		}

		if ((quoCustomer.getSpouseOccupation()) != null) {

			Cell cuCellS2 = new Cell();
			cuCellS2.setBorder(Border.NO_BORDER);
			cuCellS2.add(new Paragraph(spouseOccupation).setFontSize(8).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCellS2);

		} else {

		}

		if (quotationDetails.getSpouseDetails() != null) {

			Cell cuCellS3 = new Cell();
			cuCellS3.setBorder(Border.NO_BORDER);
			cuCellS3.add(new Paragraph(mainDob.format(quotationDetails.getSpouseDetails().getCustDob())).setFontSize(8)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCellS3);

		} else {

		}

		if ((quoCustomer.getSpouseAge()) != null) {

			Cell cuCellS4 = new Cell();
			cuCellS4.setBorder(Border.NO_BORDER);
			cuCellS4.add(new Paragraph(Integer.toString(quoCustomer.getSpouseAge())).setFontSize(8)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCellS4);

		} else {

		}

		// Display Spouse Gender
		Cell cuCellS5 = new Cell();
		cuCellS5.setBorder(Border.NO_BORDER);

		if ((quotationDetails.getSpouseDetails()) != null) {

			if (quotationDetails.getSpouseDetails().getCustGender().equalsIgnoreCase("M")) {
				cuCellS5.add(
						new Paragraph("Male").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			} else if (quotationDetails.getSpouseDetails().getCustGender().equalsIgnoreCase("F")) {
				cuCellS5.add(new Paragraph("Female").setFontSize(8).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		} else {

		}

		cusTable.addCell(cuCellS5);

		/////////////////////////////////// * End of Spouse Details*/

		cusTable.startNewRow();

		// Create an Empty Line
		Cell cuCellEmptyP = new Cell(0, 5);
		cuCellEmptyP.setBorder(Border.NO_BORDER);
		cuCellEmptyP.add(new Paragraph("").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellEmptyP);

		cusTable.startNewRow();

		Cell cuCellThP = new Cell();
		cuCellThP.setBorder(Border.NO_BORDER);
		cuCellThP.add(new Paragraph("Plan Details").setFontSize(8).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cuCellThP);

		cusTable.startNewRow();

		//////////////////// * Strat Creating Plan Details*/
		Cell cuCellPlanTh = new Cell();
		cuCellPlanTh.add(new Paragraph("Term").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cuCellPlanTh);

		Cell cuCellP1 = new Cell();
		cuCellP1.add(new Paragraph("Paying Term").setFontSize(8).setTextAlignment(TextAlignment.CENTER)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cuCellP1);

		Cell cuCellP2 = new Cell();
		cuCellP2.add(new Paragraph("Premium").setFontSize(8).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cuCellP2);

		Cell cuCellP3 = new Cell();
		cuCellP3.add(new Paragraph("Method").setFontSize(8).setTextAlignment(TextAlignment.CENTER).setBold()
				.setFixedLeading(10));
		cusTable.addCell(cuCellP3);

		Cell cuCellP4 = new Cell();
		cuCellP4.add(new Paragraph("Basic Sum Assured").setFontSize(8).setTextAlignment(TextAlignment.CENTER).setBold()
				.setFixedLeading(10));
		cusTable.addCell(cuCellP4);

		cusTable.startNewRow();

		// Display Product Code
		Cell cuCellP5 = new Cell();
		if (quotationDetails.getPolTerm() != null) {
			cuCellP5.add(new Paragraph(Integer.toString(quotationDetails.getPolTerm())).setFontSize(8)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCellP5.add(new Paragraph(" ").setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCellP5);

		Cell cuCellP6 = new Cell();
		if (quotationDetails.getPaingTerm() != null) {
			cuCellP6.add(new Paragraph(quotationDetails.getPaingTerm()).setFontSize(8)
					.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		} else {
			cuCellP6.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		}
		cusTable.addCell(cuCellP6);

		Cell cuCellP7 = new Cell();
		if (quoCustomer.getModePremium() != null) {
			cuCellP7.add(new Paragraph(formatter.format(quoCustomer.getTotPremium())).setFontSize(8)
					.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		} else {
			cuCellP7.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));

		}
		cusTable.addCell(cuCellP7);

		Cell cuCellP8 = new Cell();

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

			cuCellP8.add(new Paragraph(modeMethod).setFontSize(8).setTextAlignment(TextAlignment.CENTER)
					.setFixedLeading(10));

		}
		cusTable.addCell(cuCellP8);

		// Display Basic Sum Assured
		Cell cuCellP9 = new Cell();

		if (benefitsLife.isEmpty()) {

		} else {

			for (QuoBenf bsa : benefitsLife) {

				if (bsa.getRiderCode().equalsIgnoreCase("L2")) {

					if (bsa.getRiderSum() != null) {
						cuCellP9.add(new Paragraph(formatter.format(bsa.getRiderSum())).setFontSize(8)
								.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));

						cusTable.addCell(cuCellP9);

					} else {

					}

				}

			}

		}

		//////////////////// * End of Plan Details*/

		document.add(cusTable);
		//////////////////////////////// *End of MainLife/Spouse/Plan Details
		//////////////////////////////// Table*//////////

		document.add(new Paragraph("Benefits").setFontSize(8).setBold().setUnderline().setCharacterSpacing(1));

		//////////////////////////// Benefits Table
		//////////////////////////// FORMAT//////////////////////////////////////

		// Create Additional Benefits Table
		/* Declaring column sizes of the table respectively */
		float[] pointColumnWidths4 = { 500, 80, 80, 80, 80, 80, 80 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		// table headings of the Living Benefits
		Cell alCellth1 = new Cell(2, 0);
		alCellth1.setBorder(new SolidBorder(1));
		alCellth1.add(new Paragraph("Living Benefits").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1).setMarginTop(10));
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
			alCellBenf.setBorderLeft(new SolidBorder(1));

			// Getting ALL Benefits Name object and cast to an String
			String p = (String) hashMap.get("benName");
			String maturity = (String) hashMap.get("combination");

			System.out.println("Combinationnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn " + maturity.equalsIgnoreCase("L3"));

			// Check Maturity values not equl then Display
			if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {

				alCellBenf.add(new Paragraph(p).setFontSize(8).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(alCellBenf);
			}

			// Display Main Life Rider Amounts
			Cell alCellmA = new Cell();
			if (hashMap.get("mainAmt") != null) {

				// Getting ALL Benefits Name Combinations object and cast to an String
				String comb = (String) hashMap.get("combination");

				/* If benefit is WPB Print Amount as APPLIED */
				if (comb.equalsIgnoreCase("WPB")) {
					alCellmA.add(new Paragraph("Applied").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmA);

					// Check Maturity values not equl then Display
				} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
						&& !maturity.equalsIgnoreCase("L5")) {

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

			// Display Main Life Rider Premium
			Cell alCellmP = new Cell();
			if (hashMap.get("mainPre") != null) {

				if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
						&& !maturity.equalsIgnoreCase("L5")) {
					Double mPre = (Double) hashMap.get("mainPre");

					alCellmP.add(
							new Paragraph(formatter.format(mPre)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmP);
				}

			} else {
				alCellmP.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellmP);
			}

			// Display Spouse Rider Amounts
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

				// IF spouse Rider Not Equals Maturity Values and Null
			} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {
				alCellsA.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsA);
			}

			// Display Spouse Rider Premium
			Cell alCellsP = new Cell();
			if (hashMap.get("spousePre") != null) {
				Double spPre = (Double) hashMap.get("spousePre");

				alCellsP.add(
						new Paragraph(formatter.format(spPre)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsP);
			} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {
				alCellsP.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsP);
			}

			// Display Child Rider Amounts
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

				// IF Child Riders Are not equal Maturities and Amount is null
			} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {

				alCellcA.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcA);
			}

			// Display Child Riders Premium
			Cell alCellcP = new Cell();
			alCellcP.setBorderRight(new SolidBorder(1));

			if (hashMap.get("childPre") != null) {

				Double cPre = (Double) hashMap.get("childPre");

				alCellcP.add(
						new Paragraph(formatter.format(cPre)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcP);

			} else if (!maturity.equalsIgnoreCase("L3") && !maturity.equalsIgnoreCase("L4")
					&& !maturity.equalsIgnoreCase("L5")) {
				alCellcP.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcP);
			}

		}

		benAddTable.startNewRow();

		// Creating Empty Cell to seperate Additional Benefits
		Cell abCellAddEty = new Cell(0, 7);
		abCellAddEty.setBorder(Border.NO_BORDER);
		benAddTable.addCell(abCellAddEty);

		benAddTable.startNewRow();

		////////////////////// table headings of the Additional Benefits
		////////////////////// table\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		Cell abCellth1 = new Cell(2, 0);
		abCellth1.setBorder(new SolidBorder(1));
		abCellth1.add(new Paragraph("Additional Benefits").setFontSize(8).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1).setMarginTop(10));
		benAddTable.addCell(abCellth1);

		Cell abCellth2 = new Cell(0, 2);
		abCellth2.setBorder(new SolidBorder(1));

		abCellth2.add(new Paragraph("Main Life").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellth2);

		Cell abCellth3 = new Cell(0, 2);
		abCellth3.setBorder(new SolidBorder(1));

		abCellth3.add(new Paragraph("Spouse").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellth3);

		Cell abCellth4 = new Cell(0, 2);
		abCellth4.setBorder(new SolidBorder(1));

		abCellth4.add(new Paragraph("Children").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellth4);

		benAddTable.startNewRow();

		Cell abCellMA = new Cell();
		abCellMA.setBorder(new SolidBorder(1));

		abCellMA.add(new Paragraph("Amount").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMA);

		Cell abCellMP = new Cell();
		abCellMP.setBorder(new SolidBorder(1));

		abCellMP.add(new Paragraph("Premium").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMP);

		Cell abCellSA = new Cell();
		abCellSA.setBorder(new SolidBorder(1));

		abCellSA.add(new Paragraph("Amount").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellSA);

		Cell abCellSP = new Cell();
		abCellSP.setBorder(new SolidBorder(1));

		abCellSP.add(new Paragraph("Premium").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellSP);

		Cell abCellCA = new Cell();
		abCellCA.setBorder(new SolidBorder(1));

		abCellCA.add(new Paragraph("Amount").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellCA);

		Cell abCellCP = new Cell();
		abCellCP.setBorder(new SolidBorder(1));

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
		abCelld1.setBorderLeft(new SolidBorder(1));
		abCelld1.add(new Paragraph("Natural Death Cover").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld1);

		Cell abCelld2 = new Cell();
		if (ndc == 0.0) {
			abCelld2.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld2.add(new Paragraph(formatter.format(ndc)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld2);

		Cell abCelld3 = new Cell();
		if (ndcp == 0.0) {
			abCelld3.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld3.add(new Paragraph(formatter.format(ndcp)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld3);

		Cell abCelld4 = new Cell();
		if (ndcs == 0.0) {
			abCelld4.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld4.add(new Paragraph(formatter.format(ndcs)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld4);

		Cell abCelld5 = new Cell();
		if (ndcsp == 0.0) {
			abCelld5.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld5.add(new Paragraph(formatter.format(ndcsp)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld5);

		Cell abCelld6 = new Cell();
		abCelld6.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld6);

		Cell abCelld7 = new Cell();
		abCelld7.setBorderRight(new SolidBorder(1));

		abCelld7.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld7);

		benAddTable.startNewRow();

		Cell abCelld8 = new Cell();
		abCelld8.setBorderLeft(new SolidBorder(1));

		abCelld8.add(new Paragraph("Accidental Death Benefit").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld8);

		Cell abCelld9 = new Cell();
		if (adb == 0.0) {
			abCelld9.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld9.add(new Paragraph(formatter.format(adb)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld9);

		Cell abCelld10 = new Cell();
		if (adbp == 0.0) {
			abCelld10.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld10.add(new Paragraph(formatter.format(adbp)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld10);

		Cell abCelld11 = new Cell();
		if (adbs == 0.0) {
			abCelld11.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld11.add(new Paragraph(formatter.format(adbs)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld11);

		Cell abCelld12 = new Cell();
		if (adbsp == 0.0) {
			abCelld12.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld12.add(new Paragraph(formatter.format(adbsp)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld12);

		Cell abCelld13 = new Cell();
		abCelld13.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld13);

		Cell abCelld14 = new Cell();
		abCelld14.setBorderRight(new SolidBorder(1));

		abCelld14.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld14);

		benAddTable.startNewRow();

		Cell abCelld15 = new Cell();
		abCelld15.setBorderLeft(new SolidBorder(1));
		abCelld15.setBorderBottom(new SolidBorder(1));
		abCelld15.add(new Paragraph("Funeral Expenses").setFontSize(8).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld15);

		Cell abCelld16 = new Cell();
		abCelld16.setBorderBottom(new SolidBorder(1));
		if (feb == 0.0) {
			abCelld16.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld16.add(new Paragraph(formatter.format(feb)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld16);

		Cell abCelld117 = new Cell();
		abCelld117.setBorderBottom(new SolidBorder(1));
		if (febp == 0.0) {
			abCelld117.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld117.add(new Paragraph(formatter.format(febp)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld117);

		Cell abCelld18 = new Cell();
		abCelld18.setBorderBottom(new SolidBorder(1));
		if (febs == 0.0) {
			abCelld18.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld18.add(new Paragraph(formatter.format(febs)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld18);

		Cell abCelld19 = new Cell();
		abCelld19.setBorderBottom(new SolidBorder(1));
		if (febsp == 0.0) {
			abCelld19.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld19.add(new Paragraph(formatter.format(febsp)).setFontSize(8).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld19);

		Cell abCelld20 = new Cell();
		abCelld20.setBorderBottom(new SolidBorder(1));
		abCelld20.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld20);

		Cell abCelld21 = new Cell();
		abCelld21.setBorderBottom(new SolidBorder(1));
		abCelld21.setBorderRight(new SolidBorder(1));
		abCelld21.add(new Paragraph("-").setFontSize(8).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld21);

		///////////////////// End Of Additional Benefits Table
		///////////////////// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		document.add(benAddTable);

		/////////////////////////// END Of
		/////////////////////////// Benefits Table
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

		for (QuoBenf maturity : benefitsLife) {

			if (maturity.getRiderCode().equalsIgnoreCase("L1")) {
				document.add(new Paragraph("Guranteed Maturity : " + formatter.format(maturity.getRiderSum()))
						.setFontSize(8));
			}

		}

		//////////////////////////// * Medical Requirements
		//////////////////////////// Table*/////////////////////////
		java.util.List<MedicalRequirementsHelper> medicalDetails = medicalRequirementsDaoCustom
				.findByQuoDetail(quotationDetails.getQdId());

		document.add(new Paragraph("Medical Requirements").setBold().setFontSize(8).setUnderline()
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));

		document.add(new Paragraph(""));

		// Medical Requirements Table
		float[] pointColumnWidths6 = { 160, 500 };
		Table medReqTable = new Table(pointColumnWidths6);
		medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell mrqCell1 = new Cell();
		mrqCell1.setBorder(Border.NO_BORDER);
		mrqCell1.add(new Paragraph("Main Life").setBold().setFontSize(8).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setCharacterSpacing(1));
		medReqTable.addCell(mrqCell1);

		if (medicalDetails.isEmpty()) {

			Cell mrqCell2 = new Cell();
			mrqCell2.setBorder(Border.NO_BORDER);
			mrqCell2.add(new Paragraph(": Not Applied ").setFontSize(8).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			medReqTable.addCell(mrqCell2);

		} else {

			// Creating a String Seperator ArrayList Seperate from seperator
			final String SEPARATOR = " , ";

			StringBuilder mainLife = new StringBuilder();
			StringBuilder spouse = new StringBuilder();

			for (MedicalRequirementsHelper medicalReq : medicalDetails) {

				// When MainLife having medical requiremnents
				if (medicalReq.getMainStatus().equalsIgnoreCase("R")) {
					mainLife.append(medicalReq.getMedicalReqname());
					mainLife.append(SEPARATOR);
					// When MainLife not having medical Requirements
				} else if (medicalReq.getMainStatus().equalsIgnoreCase("NR")) {

				} else {
					mainLife.append("NR");
					mainLife.append(SEPARATOR);
				}

				// When Spouse having medical Requirements
				if (medicalReq.getSpouseStatus().equalsIgnoreCase("R")) {
					spouse.append(medicalReq.getMedicalReqname());
					spouse.append(SEPARATOR);

					// When Spouse Not having medical Requirements
				} else if (medicalReq.getSpouseStatus().equalsIgnoreCase("NR")) {

				} else {
					spouse.append("NR");
					spouse.append(SEPARATOR);
				}

			}

			String mainMedical = mainLife.toString();
			mainMedical = mainMedical.substring(0, mainMedical.length() - SEPARATOR.length());

			Cell mrqCell2 = new Cell();
			mrqCell2.setBorder(Border.NO_BORDER);
			mrqCell2.add(new Paragraph(": " + mainMedical).setFontSize(8).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			medReqTable.addCell(mrqCell2);

			medReqTable.startNewRow();

			if (benefitsSpouse.isEmpty()) {

			} else {

				String spouseMedical = spouse.toString();

				// System.out.println("spouse medicalllllllllllllllll " + spouseMedical);

				if (!spouseMedical.endsWith("NR") && !spouseMedical.isEmpty()) {

					spouseMedical = spouseMedical.substring(0, spouseMedical.length() - SEPARATOR.length());

					Cell mrqCell3 = new Cell();
					mrqCell3.setBorder(Border.NO_BORDER);
					mrqCell3.add(new Paragraph("Spouse").setBold().setFontSize(8).setTextAlignment(TextAlignment.LEFT)
							.setFixedLeading(10).setCharacterSpacing(1));
					medReqTable.addCell(mrqCell3);

					Cell mrqCell4 = new Cell();
					mrqCell4.setBorder(Border.NO_BORDER);
					mrqCell4.add(new Paragraph(": " + spouseMedical).setFontSize(8).setTextAlignment(TextAlignment.LEFT)
							.setFixedLeading(10));
					medReqTable.addCell(mrqCell4);
				}
			}

		}

		document.add(medReqTable);
		// try {
		//
		// if (medicalDetails.isEmpty()) {
		//
		// } else {
		//
		// document.add(new Paragraph("Medical
		// Requirements").setBold().setFontSize(8).setUnderline()
		// .setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));
		//
		// // Medical Requirements Table
		// float[] pointColumnWidths6 = { 100, 500 };
		// Table medReqTable = new Table(pointColumnWidths6);
		// medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
		//
		// // Creating a String Seperator ArrayList Seperate from seperator
		// final String SEPARATOR = " , ";
		//
		// StringBuilder mainLife = new StringBuilder();
		// StringBuilder spouse = new StringBuilder();
		//
		// for (MedicalRequirementsHelper medicalReq : medicalDetails) {
		//
		// // When MainLife having medical requiremnents
		// if (medicalReq.getMainStatus().equalsIgnoreCase("R")) {
		// mainLife.append(medicalReq.getMedicalReqname());
		// mainLife.append(SEPARATOR);
		// // When MainLife not having medical Requirements
		// } else if (medicalReq.getMainStatus().equalsIgnoreCase("NR")) {
		//
		// } else {
		// mainLife.append("NR");
		// mainLife.append(SEPARATOR);
		// }
		//
		// // When Spouse having medical Requirements
		// if (medicalReq.getSpouseStatus().equalsIgnoreCase("R")) {
		// spouse.append(medicalReq.getMedicalReqname());
		// spouse.append(SEPARATOR);
		//
		// // When Spouse Not having medical Requirements
		// } else if (medicalReq.getSpouseStatus().equalsIgnoreCase("NR")) {
		//
		// } else {
		// spouse.append("NR");
		// spouse.append(SEPARATOR);
		// }
		//
		// }
		//
		// String mainMedical = mainLife.toString();
		// mainMedical = mainMedical.substring(0, mainMedical.length() -
		// SEPARATOR.length());
		//
		// Cell mrqCell1 = new Cell();
		// mrqCell1.setBorder(Border.NO_BORDER);
		// mrqCell1.add(new Paragraph("Main
		// Life").setBold().setFontSize(8).setTextAlignment(TextAlignment.LEFT)
		// .setFixedLeading(10).setCharacterSpacing(1));
		// medReqTable.addCell(mrqCell1);
		//
		// Cell mrqCell2 = new Cell();
		// mrqCell2.setBorder(Border.NO_BORDER);
		// mrqCell2.add(new Paragraph(": " +
		// mainMedical).setFontSize(8).setTextAlignment(TextAlignment.LEFT)
		// .setFixedLeading(10));
		// medReqTable.addCell(mrqCell2);
		//
		// medReqTable.startNewRow();
		//
		// if (benefitsSpouse.isEmpty()) {
		//
		// } else {
		//
		// String spouseMedical = spouse.toString();
		//
		// // System.out.println("spouse medicalllllllllllllllll " + spouseMedical);
		//
		// if (!spouseMedical.endsWith("NR") && !spouseMedical.isEmpty()) {
		//
		// spouseMedical = spouseMedical.substring(0, spouseMedical.length() -
		// SEPARATOR.length());
		//
		// Cell mrqCell3 = new Cell();
		// mrqCell3.setBorder(Border.NO_BORDER);
		// mrqCell3.add(new Paragraph("Spouse").setBold().setFontSize(8)
		// .setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));
		// medReqTable.addCell(mrqCell3);
		//
		// Cell mrqCell4 = new Cell();
		// mrqCell4.setBorder(Border.NO_BORDER);
		// mrqCell4.add(new Paragraph(": " + spouseMedical).setFontSize(8)
		// .setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		// medReqTable.addCell(mrqCell4);
		// }
		// }
		//
		// document.add(medReqTable);
		//
		// }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		//
		// }

		/////////////////////// *Start FinanCial Requirements*///////////////////

		// Creating A variable for Sum At Risk
		Double fiveM = 5000000.00;

		System.out.println(
				"sum at riskkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk" + quotationDetails.getSumAtRiskMain());

		if (quotationDetails.getSumAtRiskMain() != null) {

			document.add(new Paragraph(""));

			float[] finacialReqColoumWidth = { 150, 500 };
			Table finReqTbl = new Table(finacialReqColoumWidth);
			finReqTbl.setHorizontalAlignment(HorizontalAlignment.LEFT);

			Cell fReqCell1 = new Cell();
			fReqCell1.setBorder(Border.NO_BORDER);
			fReqCell1.add(new Paragraph("Financial Requirements").setBold().setFontSize(8).setUnderline()
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));
			finReqTbl.addCell(fReqCell1);

			Cell fReqCell2 = new Cell();

			// If Sum At Risk> 5000000
			if (quotationDetails.getSumAtRiskMain() >= fiveM) {

				fReqCell2.setBorder(Border.NO_BORDER);
				fReqCell2.add(new Paragraph(": Applied").setFontSize(8).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else {
				fReqCell2.setBorder(Border.NO_BORDER);
				fReqCell2.add(new Paragraph(": Not Applied").setFontSize(8).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

			finReqTbl.addCell(fReqCell2);

			document.add(finReqTbl);

		}

		/////////////////////// *End of FinanCial Requirements*///////////////////

		document.add(new Paragraph(""));

		document.add(new Paragraph("Special Notes").setFontSize(8).setBold().setUnderline().setCharacterSpacing(1));

		document.add(new Paragraph(""));

		// Creating a Special Notes List
		List list = new List(ListNumberingType.DECIMAL);
		list.setFontSize(8);

		ListItem item1 = new ListItem();
		item1.add(
				new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.")
						.setFontSize(8).setFixedLeading(10));
		list.add(item1);

		ListItem item2 = new ListItem();
		item2.add(new Paragraph("All amounts are in Sri Lankan Rupees (LKR).").setFontSize(8).setFixedLeading(10));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("Initial policy processing fee of Rs 300 (Payable only with initial deposit).")
				.setFontSize(8).setFixedLeading(10));
		list.add(item3);

		ListItem item4 = new ListItem();
		item4.add(new Paragraph("This is an indicative quoteonly and is valid for 30 days from date of issue.")
				.setFontSize(8).setFixedLeading(10));
		list.add(item4);

		document.add(list);

		document.add(new Paragraph("This is a system generated report and therefore does not require a signature.")
				.setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setFixedPosition(50, 10, 500));

		// Creating a VRIABLE TO GET THE NO OF PAGES
		System.out.println("number pagesssssssssssss " + pdf.getNumberOfPages());

		int n = pdf.getNumberOfPages();
		for (int i = 1; i <= n; i++) {

			// document.add(new Paragraph(String.format("Page %s of %s", i ,
			// n)).setFixedPosition(400, 600, 230));

			document.showTextAligned(
					new Paragraph(String.format("Page %s of %s", i, n)).setFontSize(9).setBold().setCharacterSpacing(1),
					559, 10, i, TextAlignment.RIGHT, VerticalAlignment.BOTTOM, 0);
			if (n > 1) {

			}

		}

		////////////////// End Of No of Pages/////////////////////////

		document.close();

		return baos.toByteArray();
	}

	@Override
	public byte[] createARTMReport(QuotationDetails quotationDetails, QuotationView quotationView,
			QuoCustomer quoCustomer) throws Exception {
		// getting current year
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(quotationDetails.getQuotationquotationCreateDate());

		String mainLifeOcc = quotationDetails.getCustomerDetails().getOccupation().getOcupationName();
		String mainLifeOccupation = "";

		System.out.println("L22222222222222222222 " + quotationDetails.getBaseSum());
		System.out.println("Retiremttttttt Ageeeeeeeeeeeeeeeeeeeeeee " + quotationDetails.getRetirmentAge());
		System.out.println("term of the policyyyyyyyyyyyyyyyyy" + quotationDetails.getPolTerm());
		System.out.println("cONTRIBUTIONNNNNNNNNNNNNNNNNNN" + quotationDetails.getPremium());

		System.out.println("retirenment paying perioddddddddddd " + quotationDetails.getPensionTerm());

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
		//ArrayList<QuoBenf> benefitsSpouse = quotationView.getSpouseBenf();
		// getting children benefits
		//ArrayList<QuoChildBenef> benefitsChild = quotationView.getChildBenf();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);
		document.setTopMargin(30);
		document.setBottomMargin(10);

		// Agent Details
		float[] pointColumnWidths1 = { 90, 150 };
		Table agtTable = new Table(pointColumnWidths1);
		agtTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell agCell1 = new Cell();
		agCell1.setBorder(Border.NO_BORDER);
		agCell1.add(new Paragraph("Date").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell1);

		Cell agcell2 = new Cell();
		agcell2.setBorder(Border.NO_BORDER);
		agcell2.add(new Paragraph(": " + date).setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell2);

		agtTable.startNewRow();

		Cell agCellQId = new Cell();
		agCellQId.setBorder(Border.NO_BORDER);
		agCellQId.add(
				new Paragraph("Quotation No ").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCellQId);
		Cell agCellQ = new Cell();
		agCellQ.setBorder(Border.NO_BORDER);
		agCellQ.add(new Paragraph(": " + quotationDetails.getQuotation().getId()).setFontSize(8)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCellQ);

		agtTable.startNewRow();

		Cell agCell3 = new Cell();
		agCell3.setBorder(Border.NO_BORDER);
		agCell3.add(
				new Paragraph("Branch Name").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell3);
		Cell agcell4 = new Cell();
		agcell4.setBorder(Border.NO_BORDER);
		agcell4.add(new Paragraph(quotationDetails.getQuotation().getUser().getBranch().getBranch_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getBranch().getBranch_Name()
				: ": ").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell4);

		agtTable.startNewRow();

		Cell agCell5 = new Cell();
		agCell5.setBorder(Border.NO_BORDER);
		agCell5.add(
				new Paragraph("Agent Name").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell5);
		Cell agcell6 = new Cell();
		agcell6.setBorder(Border.NO_BORDER);
		agcell6.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Name() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Name()
				: ": ").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell6);

		agtTable.startNewRow();

		Cell agcell7 = new Cell();
		agcell7.setBorder(Border.NO_BORDER);
		agcell7.add(
				new Paragraph("Agent Code").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
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
				agcell8.add(new Paragraph(": " + quotationDetails.getQuotation().getUser().getUserCode()).setFontSize(8)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else if (val == 0) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": ............................ ").setFontSize(8)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(
						new Paragraph(": ").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			}
		} else {

		}

		agtTable.addCell(agcell8);

		agtTable.startNewRow();

		Cell agcell9 = new Cell();
		agcell9.setBorder(Border.NO_BORDER);
		agcell9.add(
				new Paragraph("Contact No").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agcell9);

		Cell agCell10 = new Cell();
		agCell10.setBorder(Border.NO_BORDER);
		agCell10.add(new Paragraph(quotationDetails.getQuotation().getUser().getUser_Mobile() != null
				? ": " + quotationDetails.getQuotation().getUser().getUser_Mobile()
				: ": ").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		agtTable.addCell(agCell10);

		document.add(agtTable);

		document.add(new Paragraph(""));

		document.add(new Paragraph("ARPICO RETIREMENT PLAN").setFontSize(8).setCharacterSpacing(1));

		final SolidLine lineDrawer = new SolidLine(1f);
		document.add(new LineSeparator(lineDrawer));
		document.add(new Paragraph(""));

		// customer/Spouse/Plan Details
		float[] pointColumnWidths2 = { 300, 150, 100, 100, 130 };
		Table cusTable = new Table(pointColumnWidths2);
		cusTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		///////////////////////// *Craeting Main Life Details*/
		Cell cuCellTh = new Cell();
		cuCellTh.setBorder(Border.NO_BORDER);
		cuCellTh.add(new Paragraph("Main Life Details").setFontSize(8).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cuCellTh);

		Cell cucellTh1 = new Cell();
		cucellTh1.setBorder(Border.NO_BORDER);
		cucellTh1.add(new Paragraph("Occupation").setFontSize(8).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh1);

		Cell cucellTh2 = new Cell();
		cucellTh2.setBorder(Border.NO_BORDER);
		cucellTh2.add(
				new Paragraph("DOB").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh2);

		Cell cucellTh3 = new Cell();
		cucellTh3.setBorder(Border.NO_BORDER);
		cucellTh3.add(
				new Paragraph("Age").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh3);

		Cell cucellTh4 = new Cell();
		cucellTh4.setBorder(Border.NO_BORDER);
		cucellTh4.add(new Paragraph("Gender").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cucellTh4);
		cusTable.startNewRow();

		Cell cuCellM1 = new Cell();
		cuCellM1.setBorder(Border.NO_BORDER);
		cuCellM1.add(new Paragraph(quotationDetails.getCustomerDetails().getCustName() != null
				? quotationDetails.getCustomerDetails().getCustName()
				: " ").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM1);

		Cell cuCellM2 = new Cell();
		cuCellM2.setBorder(Border.NO_BORDER);
		cuCellM2.add(new Paragraph(mainLifeOccupation != null ? mainLifeOccupation : " ").setFontSize(8)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM2);

		Cell cuCellM3 = new Cell();
		cuCellM3.setBorder(Border.NO_BORDER);

		// Creating a Date Format for DOB
		SimpleDateFormat mainDob = new SimpleDateFormat("dd-MM-yyyy");

		cuCellM3.add(new Paragraph(quotationDetails.getCustomerDetails().getCustDob() != null
				? mainDob.format(quotationDetails.getCustomerDetails().getCustDob())
				: " ").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM3);

		Cell cuCellM4 = new Cell();
		cuCellM4.setBorder(Border.NO_BORDER);
		if (quoCustomer.getMainLifeAge() != null) {
			cuCellM4.add(new Paragraph(Integer.toString(quoCustomer.getMainLifeAge())).setFontSize(8)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCellM4.add(new Paragraph(" ").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCellM4);

		Cell cuCellM5 = new Cell();
		cuCellM5.setBorder(Border.NO_BORDER);

		// Check MainLife Gender If M->Male, F->Female
		if (quotationDetails.getCustomerDetails().getCustGender() != null) {

			if (quotationDetails.getCustomerDetails().getCustGender().equalsIgnoreCase("M")) {
				cuCellM5.add(
						new Paragraph("Male").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			} else if (quotationDetails.getCustomerDetails().getCustGender().equalsIgnoreCase("F")) {
				cuCellM5.add(new Paragraph("Female").setFontSize(8).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		} else {

		}
		cusTable.addCell(cuCellM5);

		/////////////////////////// *End Craeting Main Life Details*/

		cusTable.startNewRow();

		// Create an Empty Line
		Cell cuCellEmptyM = new Cell(0, 5);
		cuCellEmptyM.setBorder(Border.NO_BORDER);
		cuCellEmptyM.add(new Paragraph("").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellEmptyM);

		//////////////////////// * Strat Creating Spouse Details*/

		// checking Spouse is active or not
		if ((quoCustomer.getSpouseName()) != null) {

			Cell cuCellThS = new Cell();
			cuCellThS.setBorder(Border.NO_BORDER);
			cuCellThS.add(new Paragraph("Spouse Details").setFontSize(8).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10).setBold());
			cusTable.addCell(cuCellThS);

			cusTable.startNewRow();

			Cell cuCellS1 = new Cell();
			cuCellS1.setBorder(Border.NO_BORDER);
			cuCellS1.add(new Paragraph(quoCustomer.getSpouseName()).setFontSize(8).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCellS1);

		} else {

		}

		if ((quoCustomer.getSpouseOccupation()) != null) {

			Cell cuCellS2 = new Cell();
			cuCellS2.setBorder(Border.NO_BORDER);
			cuCellS2.add(new Paragraph(spouseOccupation).setFontSize(8).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCellS2);

		} else {

		}

		if (quotationDetails.getSpouseDetails() != null) {

			Cell cuCellS3 = new Cell();
			cuCellS3.setBorder(Border.NO_BORDER);
			cuCellS3.add(new Paragraph(mainDob.format(quotationDetails.getSpouseDetails().getCustDob())).setFontSize(8)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCellS3);

		} else {

		}

		if ((quoCustomer.getSpouseAge()) != null) {

			Cell cuCellS4 = new Cell();
			cuCellS4.setBorder(Border.NO_BORDER);
			cuCellS4.add(new Paragraph(Integer.toString(quoCustomer.getSpouseAge())).setFontSize(8)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCellS4);

		} else {

		}

		// Display Spouse Gender
		Cell cuCellS5 = new Cell();
		cuCellS5.setBorder(Border.NO_BORDER);

		if ((quotationDetails.getSpouseDetails()) != null) {

			if (quotationDetails.getSpouseDetails().getCustGender().equalsIgnoreCase("M")) {
				cuCellS5.add(
						new Paragraph("Male").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			} else if (quotationDetails.getSpouseDetails().getCustGender().equalsIgnoreCase("F")) {
				cuCellS5.add(new Paragraph("Female").setFontSize(8).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		} else {

		}

		cusTable.addCell(cuCellS5);

		/////////////////////////////////// * End of Spouse Details*/

		cusTable.startNewRow();

		// Create an Empty Line
		Cell cuCellEmptyP = new Cell(0, 5);
		cuCellEmptyP.setBorder(Border.NO_BORDER);
		cuCellEmptyP.add(new Paragraph("").setFontSize(8).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellEmptyP);

		cusTable.startNewRow();

		Cell cuCellThP = new Cell();
		cuCellThP.setBorder(Border.NO_BORDER);
		cuCellThP.add(new Paragraph("Plan Details").setFontSize(8).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cuCellThP);

		cusTable.startNewRow();

		//////////////////// * Strat Creating Plan Details*/
		Cell cuCellPlanTh = new Cell();
		cuCellPlanTh.add(new Paragraph("Term of the Policy").setFontSize(8).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cuCellPlanTh);

		Cell cuCellP1 = new Cell();
		cuCellP1.add(new Paragraph("Contribution").setFontSize(8).setTextAlignment(TextAlignment.CENTER)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cuCellP1);

		Cell cuCellP2 = new Cell();
		cuCellP2.add(new Paragraph("Method").setFontSize(8).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cuCellP2);

		Cell cuCellP3 = new Cell();
		cuCellP3.add(new Paragraph("Retirement Age").setFontSize(8).setTextAlignment(TextAlignment.CENTER).setBold()
				.setFixedLeading(10));
		cusTable.addCell(cuCellP3);

		Cell cuCellP4 = new Cell();
		cuCellP4.add(new Paragraph("Retirement Payment Period").setFontSize(8).setTextAlignment(TextAlignment.CENTER)
				.setBold().setFixedLeading(10));
		cusTable.addCell(cuCellP4);

		cusTable.startNewRow();

		// Display Product Code
		Cell cuCellP5 = new Cell();
		if (quotationDetails.getPolTerm() != null) {
			cuCellP5.add(new Paragraph(Integer.toString(quotationDetails.getPolTerm())).setFontSize(8)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCellP5.add(new Paragraph(" ").setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCellP5);

		Cell cuCellP6 = new Cell();
		if (quotationDetails.getPremium() != null) {
			cuCellP6.add(new Paragraph(formatter.format(quotationDetails.getPremium())).setFontSize(8)
					.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		} else {
			cuCellP6.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		}
		cusTable.addCell(cuCellP6);

		Cell cuCellP7 = new Cell();

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

			cuCellP7.add(new Paragraph(modeMethod).setFontSize(8).setTextAlignment(TextAlignment.CENTER)
					.setFixedLeading(10));

		}
		cusTable.addCell(cuCellP7);

		Cell cuCellP8 = new Cell();
		if (quotationDetails.getRetirmentAge() != null) {
			cuCellP8.add(new Paragraph(Integer.toString(quotationDetails.getRetirmentAge())).setFontSize(8)
					.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		} else {
			cuCellP8.add(new Paragraph("").setFontSize(8).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));

		}
		cusTable.addCell(cuCellP8);

		// Display Basic Sum Assured
		Cell cuCellP9 = new Cell();
		if (quotationDetails.getPensionTerm() != null) {
			cuCellP9.add(new Paragraph(Integer.toString(quotationDetails.getPensionTerm())).setFontSize(9)
					.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		} else {
			cuCellP9.add(new Paragraph("").setFontSize(8).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));

		}

		cusTable.addCell(cuCellP9);

		//////////////////// * End of Plan Details*/

		document.add(cusTable);
		//////////////////////////////// *End of MainLife/Spouse/Plan Details
		//////////////////////////////// Table*//////////

		// document.add(new Paragraph("\n"));
		/*
		 * document.add(new Paragraph("")); document.add(new Paragraph(""));
		 * 
		 * document.add(new
		 * Paragraph("Illustrated Maturity Benefits (Pension Fund Value)").setFontSize(8
		 * ).setBold() .setUnderline().setCharacterSpacing(1));
		 * 
		 * // Creating Benefits Table float[] pointColumnWidths3 = { 500, 230 }; Table
		 * benLivTable = new Table(pointColumnWidths3);
		 * benLivTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
		 * benLivTable.setBorder(new SolidBorder(1));
		 * 
		 * Cell cell1 = new Cell(); cell1.setBorder(new SolidBorder(1)); cell1.add(new
		 * Paragraph("").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
		 * .setCharacterSpacing(1)); benLivTable.addCell(cell1); Cell cell2 = new
		 * Cell(); cell2.setBorder(new SolidBorder(1)); cell2.add(new
		 * Paragraph("Illustrated Maturity Value (Rs. )").setFontSize(8).setBold()
		 * .setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		 * benLivTable.addCell(cell2);
		 * 
		 * benLivTable.startNewRow();
		 * 
		 * Double l14Amt = 0.0; Double l14Pre = 0.0;
		 * 
		 * Double l15Amt = 0.0; Double l15Pre = 0.0;
		 * 
		 * Double l16Amt = 0.0; Double l16Pre = 0.0;
		 * 
		 * if (benefitsLife.isEmpty()) {
		 * 
		 * } else { for (QuoBenf matVals : benefitsLife) { if
		 * (matVals.getRiderCode().equalsIgnoreCase("L14")) { l14Amt = l14Amt +
		 * matVals.getRiderSum(); l14Pre = l14Pre + matVals.getPremium();
		 * 
		 * }
		 * 
		 * if (matVals.getRiderCode().equalsIgnoreCase("L15")) { l15Amt = l15Amt +
		 * matVals.getRiderSum(); l15Pre = l15Pre + matVals.getPremium();
		 * 
		 * }
		 * 
		 * if (matVals.getRiderCode().equalsIgnoreCase("L16")) { l16Amt = l16Amt +
		 * matVals.getRiderSum(); l16Pre = l16Pre + matVals.getPremium();
		 * 
		 * }
		 * 
		 * } }
		 * 
		 * Cell cell3 = new Cell(); cell3.add(new
		 * Paragraph("Assumed Annual Dividend Rate 7%").setFontSize(8).setTextAlignment(
		 * TextAlignment.LEFT)); benLivTable.addCell(cell3); Cell cell4 = new Cell();
		 * cell4.add(new
		 * Paragraph(formatter.format(l14Amt)).setFontSize(8).setFontSize(9)
		 * .setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		 * benLivTable.addCell(cell4);
		 * 
		 * Cell cell5 = new Cell(); cell5.add(new
		 * Paragraph("Assumed Annual Dividend Rate 8%").setFontSize(8).setTextAlignment(
		 * TextAlignment.LEFT)); benLivTable.addCell(cell5); Cell cell6 = new Cell();
		 * cell6.add(new
		 * Paragraph(formatter.format(l15Amt)).setFontSize(8).setTextAlignment(
		 * TextAlignment.CENTER) .setFixedLeading(10)); benLivTable.addCell(cell6);
		 * 
		 * Cell cell7 = new Cell(); cell7.add(new
		 * Paragraph("Assumed Annual Dividend Rate 9%").setFontSize(8).setTextAlignment(
		 * TextAlignment.LEFT)); benLivTable.addCell(cell7); Cell cell8 = new Cell();
		 * cell8.add(new
		 * Paragraph(formatter.format(l16Amt)).setFontSize(8).setTextAlignment(
		 * TextAlignment.CENTER)); benLivTable.addCell(cell8);
		 * 
		 * document.add(benLivTable);
		 */

		//////////////// tempory cacculation

		Double l14Amt = 0.0;
		Double l14Pre = 0.0;

		Double l15Amt = 0.0;
		Double l15Pre = 0.0;

		Double l16Amt = 0.0;
		Double l16Pre = 0.0;
		if (benefitsLife.isEmpty()) {

		} else {
			for (QuoBenf matVals : benefitsLife) {
				if (matVals.getRiderCode().equalsIgnoreCase("L14")) {
					l14Amt = l14Amt + matVals.getRiderSum();
					l14Pre = l14Pre + matVals.getPremium();

				}

				if (matVals.getRiderCode().equalsIgnoreCase("L15")) {
					l15Amt = l15Amt + matVals.getRiderSum();
					l15Pre = l15Pre + matVals.getPremium();

				}

				if (matVals.getRiderCode().equalsIgnoreCase("L16")) {
					l16Amt = l16Amt + matVals.getRiderSum();
					l16Pre = l16Pre + matVals.getPremium();

				}

			}
		}
		////////////////

		document.add(new Paragraph(""));
		document.add(new Paragraph(""));

		document.add(new Paragraph("Illustrated Monthly Pension Income").setFontSize(8).setBold().setUnderline()
				.setCharacterSpacing(1));

		float[] pensionIncomeWidth = { 200, 250, 250, 250 };
		Table pensionTable = new Table(pensionIncomeWidth);

		Cell pensTh1 = new Cell();
		pensTh1.setBorder(new SolidBorder(1));
		pensTh1.add(new Paragraph("Retirement Payment Period").setFontSize(8).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		pensionTable.addCell(pensTh1);

		Cell pensTh2 = new Cell();
		pensTh2.setBorder(new SolidBorder(1));
		pensTh2.add(new Paragraph("Assumed Annual Dividend Rate 7%").setFontSize(8).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		pensionTable.addCell(pensTh2);

		Cell pensTh3 = new Cell();
		pensTh3.setBorder(new SolidBorder(1));
		pensTh3.add(new Paragraph("Assumed Annual Dividend Rate 8%").setFontSize(8).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		pensionTable.addCell(pensTh3);

		Cell pensTh4 = new Cell();
		pensTh4.setBorder(new SolidBorder(1));
		pensTh4.add(new Paragraph("Assumed Annual Dividend Rate 9%").setFontSize(8).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		pensionTable.addCell(pensTh4);

		pensionTable.startNewRow();

		Cell pensCell1 = new Cell();
		pensCell1.add(new Paragraph(
				quotationDetails.getPensionTerm() != null ? Integer.toString(quotationDetails.getPensionTerm()) : "")
						.setFontSize(8).setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		pensionTable.addCell(pensCell1);

		Cell pensCell2 = new Cell();
		pensCell2.add(new Paragraph(l14Pre != null ? formatter.format(l14Pre) : "-").setFontSize(8)
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		pensionTable.addCell(pensCell2);

		Cell pensCell3 = new Cell();
		pensCell3.add(new Paragraph(l15Pre != null ? formatter.format(l15Pre) : "-").setFontSize(8)
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		pensionTable.addCell(pensCell3);

		Cell pensCell4 = new Cell();
		pensCell4.add(new Paragraph(l16Pre != null ? formatter.format(l16Pre) : "-").setFontSize(8)
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		pensionTable.addCell(pensCell4);

		document.add(pensionTable);

		document.add(new Paragraph(""));
		document.add(new Paragraph(""));

		// Create Additional Benefits Table

		document.add(
				new Paragraph("Additional Benefits").setFontSize(8).setBold().setUnderline().setCharacterSpacing(1));

		float[] pointColumnWidths4 = { 300, 500 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(new SolidBorder(1));

		Cell abCell2 = new Cell();
		abCell2.add(new Paragraph("Death Benefit").setFontSize(8).setTextAlignment(TextAlignment.CENTER));
		benAddTable.addCell(abCell2);

		if (benefitsLife.isEmpty()) {

		} else {
			for (QuoBenf bsa : benefitsLife) {
				if (bsa.getRiderCode().equalsIgnoreCase("L2")) {

					Cell abCell3 = new Cell();
					abCell3.add(new Paragraph(
							bsa.getRiderSum() != null ? "Rs. " + formatter.format(bsa.getRiderSum()) : "-")
									.setFontSize(8).setTextAlignment(TextAlignment.CENTER));
					benAddTable.addCell(abCell3);

				}
			}

		}

		benAddTable.startNewRow();

		Cell abCell4 = new Cell();
		abCell4.add(new Paragraph("Waiver of Premium on TPD").setFontSize(8).setTextAlignment(TextAlignment.CENTER));
		benAddTable.addCell(abCell4);

		Cell abCell5 = new Cell();
		abCell5.add(new Paragraph(quotationDetails.getPayMode().equalsIgnoreCase("S") ? "Not Applied" : "Applied")
				.setFontSize(8).setTextAlignment(TextAlignment.CENTER));
		benAddTable.addCell(abCell5);

		document.add(benAddTable);

		//////////////////////////////////////////////////////////////////////

		document.add(new Paragraph(" "));
		/*
		 * java.util.List<PensionShedule> pensionSchedule =
		 * quotationDetails.getPensionShedules();
		 * 
		 * if (pensionSchedule.isEmpty()) {
		 * 
		 * } else {
		 * 
		 * document.add(new
		 * Paragraph("Illustration of Investment Account Values").setFontSize(8).setBold
		 * () .setUnderline().setCharacterSpacing(1));
		 * 
		 * float[] penScheduleColoumWidths = { 80, 100, 150, 150, 150, 150, 150, 150 };
		 * Table penSchTable = new Table(penScheduleColoumWidths);
		 * penSchTable.setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(new
		 * SolidBorder(2));
		 * 
		 * Cell pSchCellTh1 = new Cell(0, 2); pSchCellTh1.add(new
		 * Paragraph("").setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER)
		 * .setCharacterSpacing(1)); penSchTable.addCell(pSchCellTh1);
		 * 
		 * Cell pSchCellTh2 = new Cell(0, 2); pSchCellTh2.add(new
		 * Paragraph("Assumed Annual Dividended Rate of 7% (Rs.)").setFontSize(8).
		 * setBold() .setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		 * penSchTable.addCell(pSchCellTh2);
		 * 
		 * Cell pSchCellTh3 = new Cell(0, 2); pSchCellTh3.add(new
		 * Paragraph("Assumed Annual Dividend Rate of 8% (Rs.)").setFontSize(8).setBold(
		 * ) .setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		 * penSchTable.addCell(pSchCellTh3);
		 * 
		 * Cell pSchCellTh4 = new Cell(0, 2); pSchCellTh4.add(new
		 * Paragraph("Assumed Annual Dividend Rate of 9% (Rs.)").setFontSize(8).setBold(
		 * ) .setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		 * penSchTable.addCell(pSchCellTh4);
		 * 
		 * Cell pSchCellTh5 = new Cell(); pSchCellTh5.add(new
		 * Paragraph("Year").setFontSize(8).setBold().setTextAlignment(TextAlignment.
		 * CENTER) .setCharacterSpacing(1)); penSchTable.addCell(pSchCellTh5);
		 * 
		 * Cell pSchCellTh6 = new Cell(); pSchCellTh6.add(new
		 * Paragraph("Annual Premium").setFontSize(8).setBold()
		 * .setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		 * penSchTable.addCell(pSchCellTh6);
		 * 
		 * Cell pSchCellTh7 = new Cell(); pSchCellTh7.add(new
		 * Paragraph("Amount Payble on Death").setFontSize(8).setBold()
		 * .setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		 * penSchTable.addCell(pSchCellTh7);
		 * 
		 * Cell pSchCellTh8 = new Cell(); pSchCellTh8.add(new
		 * Paragraph("Investment Account Value").setFontSize(8).setBold()
		 * .setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		 * penSchTable.addCell(pSchCellTh8);
		 * 
		 * Cell pSchCellTh9 = new Cell(); pSchCellTh9.add(new
		 * Paragraph("Amount Payble on Death").setFontSize(8).setBold()
		 * .setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		 * penSchTable.addCell(pSchCellTh9);
		 * 
		 * Cell pSchCellTh10 = new Cell(); pSchCellTh10.add(new
		 * Paragraph("Investment Account Value").setFontSize(8).setBold()
		 * .setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		 * penSchTable.addCell(pSchCellTh10);
		 * 
		 * Cell pSchCellTh11 = new Cell(); pSchCellTh11.add(new
		 * Paragraph("Amount Payble on Death").setFontSize(8).setBold()
		 * .setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		 * penSchTable.addCell(pSchCellTh11);
		 * 
		 * Cell pSchCellTh12 = new Cell(); pSchCellTh12.add(new
		 * Paragraph("Investment Account Value").setFontSize(8).setBold()
		 * .setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		 * penSchTable.addCell(pSchCellTh12);
		 * 
		 * for (PensionShedule penShedule : pensionSchedule) {
		 * 
		 * if (penShedule.getMonth() % 12 == 0) {
		 * 
		 * Cell pSchCell1 = new Cell(); pSchCell1 .add(new Paragraph(
		 * penShedule.getPolyer() != null ? Integer.toString(penShedule.getPolyer()) :
		 * "-")) .setFontSize(8).setTextAlignment(TextAlignment.RIGHT);
		 * penSchTable.addCell(pSchCell1);
		 * 
		 * Integer payTerm = Integer.parseInt(quotationDetails.getPaingTerm());
		 * 
		 * if (quotationDetails.getPayMode().equalsIgnoreCase("S") &&
		 * penShedule.getPolyer() == 1) { Cell pSchCell2 = new Cell(); pSchCell2.add(new
		 * Paragraph( quotationDetails.getPremium() != null ?
		 * formatter.format(quotationDetails.getPremium()) : "-"))
		 * .setFontSize(8).setTextAlignment(TextAlignment.RIGHT);
		 * penSchTable.addCell(pSchCell2);
		 * 
		 * } else if (penShedule.getPolyer() <= payTerm &&
		 * !quotationDetails.getPayMode().equalsIgnoreCase("S")) {
		 * 
		 * Double contriution = 0.0;
		 * 
		 * if (quotationDetails.getPayMode().equalsIgnoreCase("M")) { contriution =
		 * contriution + (quotationDetails.getPremium() * 12); } else if
		 * (quotationDetails.getPayMode().equalsIgnoreCase("Y")) { contriution =
		 * contriution + (quotationDetails.getPremium() * 1);
		 * 
		 * } else if (quotationDetails.getPayMode().equalsIgnoreCase("Q")) { contriution
		 * = contriution + (quotationDetails.getPremium() * 4);
		 * System.out.println("Quartltyyyyyyyyyyyy " + contriution);
		 * 
		 * } else if (quotationDetails.getPayMode().equalsIgnoreCase("H")) { contriution
		 * = contriution + (quotationDetails.getPremium() * 2);
		 * System.out.println("half yearlyyyyyyyyyyyyyyyyyyyyyyyyyyyy " + contriution);
		 * 
		 * }
		 * 
		 * Cell pSchCell2 = new Cell(); pSchCell2.add(new Paragraph(contriution != null
		 * ? formatter.format(contriution) : "-"))
		 * .setFontSize(8).setTextAlignment(TextAlignment.RIGHT);
		 * penSchTable.addCell(pSchCell2);
		 * 
		 * } else { Cell pSchCell2 = new Cell(); pSchCell2.add(new
		 * Paragraph("0")).setFontSize(8).setTextAlignment(TextAlignment.RIGHT);
		 * penSchTable.addCell(pSchCell2); }
		 * 
		 * Cell pSchCell3 = new Cell(); pSchCell3.add(new Paragraph(
		 * quotationDetails.getBaseSum() != null ?
		 * formatter.format(quotationDetails.getBaseSum()) : "-"))
		 * .setFontSize(8).setTextAlignment(TextAlignment.RIGHT);
		 * penSchTable.addCell(pSchCell3);
		 * 
		 * Cell pSchCell4 = new Cell(); pSchCell4 .add(new Paragraph(
		 * penShedule.getClsFnd1() != null ? formatter.format(penShedule.getClsFnd1()) :
		 * "-")) .setFontSize(8).setTextAlignment(TextAlignment.RIGHT);
		 * penSchTable.addCell(pSchCell4);
		 * 
		 * Cell pSchCell5 = new Cell(); pSchCell5.add(new Paragraph(
		 * quotationDetails.getBaseSum() != null ?
		 * formatter.format(quotationDetails.getBaseSum()) : "-"))
		 * .setFontSize(8).setTextAlignment(TextAlignment.RIGHT);
		 * penSchTable.addCell(pSchCell5);
		 * 
		 * Cell pSchCell6 = new Cell(); pSchCell6 .add(new Paragraph(
		 * penShedule.getClsFnd2() != null ? formatter.format(penShedule.getClsFnd2()) :
		 * "-")) .setFontSize(8).setTextAlignment(TextAlignment.RIGHT);
		 * penSchTable.addCell(pSchCell6);
		 * 
		 * Cell pSchCell7 = new Cell(); pSchCell7.add(new Paragraph(
		 * quotationDetails.getBaseSum() != null ?
		 * formatter.format(quotationDetails.getBaseSum()) : "-"))
		 * .setFontSize(8).setTextAlignment(TextAlignment.RIGHT);
		 * penSchTable.addCell(pSchCell7);
		 * 
		 * Cell pSchCell8 = new Cell(); pSchCell8 .add(new Paragraph(
		 * penShedule.getClsFnd3() != null ? formatter.format(penShedule.getClsFnd3()) :
		 * "-")) .setFontSize(8).setTextAlignment(TextAlignment.RIGHT);
		 * penSchTable.addCell(pSchCell8);
		 * 
		 * }
		 * 
		 * }
		 * 
		 * document.add(penSchTable);
		 * 
		 * }
		 */

		/*
		 * document.add(new Paragraph("")); document.add(new Paragraph(""));
		 * 
		 * document.add(new
		 * Paragraph("Arpico Insurance Dividend Rate Declaration for Last Five Years").
		 * setFontSize(9) .setBold().setUnderline().setCharacterSpacing(1));
		 * 
		 * float[] dividendRateColsWidth = { 150, 100, 100, 100, 100, 100 }; Table
		 * divRateTable = new Table(dividendRateColsWidth);
		 * divRateTable.setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(new
		 * SolidBorder(1));
		 * 
		 * Cell divCellTh1 = new Cell(); divCellTh1.add(new
		 * Paragraph("Year").setFontSize(9).setBold().setTextAlignment(TextAlignment.
		 * CENTER)); divRateTable.addCell(divCellTh1);
		 * 
		 * Cell divCellTh2 = new Cell(); divCellTh2.add(new
		 * Paragraph("2012").setFontSize(9).setBold().setTextAlignment(TextAlignment.
		 * CENTER)); divRateTable.addCell(divCellTh2);
		 * 
		 * Cell divCellTh3 = new Cell(); divCellTh3.add(new
		 * Paragraph("2013").setFontSize(9).setBold().setTextAlignment(TextAlignment.
		 * CENTER)); divRateTable.addCell(divCellTh3);
		 * 
		 * Cell divCellTh4 = new Cell(); divCellTh4.add(new
		 * Paragraph("2014").setFontSize(9).setBold().setTextAlignment(TextAlignment.
		 * CENTER)); divRateTable.addCell(divCellTh4);
		 * 
		 * Cell divCellTh5 = new Cell(); divCellTh5.add(new
		 * Paragraph("2015").setFontSize(9).setBold().setTextAlignment(TextAlignment.
		 * CENTER)); divRateTable.addCell(divCellTh5);
		 * 
		 * Cell divCellTh6 = new Cell(); divCellTh6.add(new
		 * Paragraph("2016").setFontSize(9).setBold().setTextAlignment(TextAlignment.
		 * CENTER)); divRateTable.addCell(divCellTh6);
		 * 
		 * divRateTable.startNewRow();
		 * 
		 * Cell divCell1 = new Cell(); divCell1.add(new
		 * Paragraph("Declared Dividend Rate").setFontSize(9).setTextAlignment(
		 * TextAlignment.CENTER)); divRateTable.addCell(divCell1);
		 * 
		 * Cell divCell2 = new Cell(); divCell2.add(new
		 * Paragraph("").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
		 * ); divRateTable.addCell(divCell2);
		 * 
		 * Cell divCell3 = new Cell(); divCell3.add(new
		 * Paragraph("").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
		 * ); divRateTable.addCell(divCell3);
		 * 
		 * Cell divCell4 = new Cell(); divCell4.add(new
		 * Paragraph("").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
		 * ); divRateTable.addCell(divCell4);
		 * 
		 * Cell divCell5 = new Cell(); divCell5.add(new
		 * Paragraph("").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
		 * ); divRateTable.addCell(divCell5);
		 * 
		 * Cell divCell6 = new Cell(); divCell6.add(new
		 * Paragraph("").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
		 * ); divRateTable.addCell(divCell6);
		 * 
		 * document.add(divRateTable);
		 * 
		 * // getting Previous Year Calendar prevYear = Calendar.getInstance();
		 * prevYear.add(Calendar.YEAR, -1);
		 * 
		 * // System.out.println("yearrrrrrrrrrrrrrrrr " +prevYear.get(Calendar.YEAR));
		 * document.add(new
		 * Paragraph("** Guranteed minimum dividend rate declared for Last Year "+
		 * prevYear.get(Calendar.YEAR)).setFontSize(9));
		 */
		document.add(new Paragraph(""));

		document.add(new Paragraph("Special Notes").setFontSize(8).setBold().setUnderline().setCharacterSpacing(1));
		document.add(new Paragraph(""));

		// Creating a Special Notes List
		List list = new List(ListNumberingType.DECIMAL);
		list.setFontSize(9);

		ListItem item1 = new ListItem();
		item1.add(new Paragraph(
				"This Quotation is valid only for 30 days and any alteration made on this quotation are invalid. ")
						.setFontSize(8).setFixedLeading(10));
		list.add(item1);

		ListItem item2 = new ListItem();
		item2.add(new Paragraph("All Amounts are in Sri Lankan Rupees (LKR).").setFontSize(8).setFixedLeading(10));
		list.add(item2);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("Initial policy processing fee of Rs. 300 (Payable only with initial deposit).")
				.setFontSize(8).setFixedLeading(10));
		list.add(item3);

		document.add(list);

		document.add(new Paragraph("This is a system generated report and therefore does not require a signature.")
				.setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setFixedPosition(50, 10, 500));

		document.close();

		return baos.toByteArray();
	}

	@Override
	public byte[] createASIPReport(QuotationDetails quotationDetails, QuotationView quotationView,
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
		document.setTopMargin(30);
		document.setBottomMargin(10);

		// Agent Details
		float[] pointColumnWidths1 = { 90, 150 };
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
				agcell8.add(new Paragraph(": " + quotationDetails.getQuotation().getUser().getUserCode()).setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else if (val == 0) {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(new Paragraph(": ............................ ").setFontSize(9)
						.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));

			} else {
				agcell8.setBorder(Border.NO_BORDER);
				agcell8.add(
						new Paragraph(": ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
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

		document.add(new Paragraph("ARPICO SUPER INVESTMENT PLAN").setFontSize(9).setCharacterSpacing(1));

		final SolidLine lineDrawer = new SolidLine(1f);
		document.add(new LineSeparator(lineDrawer));
		document.add(new Paragraph(""));

		// customer/Spouse/Plan Details
		float[] pointColumnWidths2 = { 300, 150, 100, 100, 130 };
		Table cusTable = new Table(pointColumnWidths2);
		cusTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		///////////////////////// *Craeting Main Life Details*/
		Cell cuCellTh = new Cell();
		cuCellTh.setBorder(Border.NO_BORDER);
		cuCellTh.add(new Paragraph("Main Life Details").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cuCellTh);

		Cell cucellTh1 = new Cell();
		cucellTh1.setBorder(Border.NO_BORDER);
		cucellTh1.add(new Paragraph("Occupation").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh1);

		Cell cucellTh2 = new Cell();
		cucellTh2.setBorder(Border.NO_BORDER);
		cucellTh2.add(
				new Paragraph("DOB").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh2);

		Cell cucellTh3 = new Cell();
		cucellTh3.setBorder(Border.NO_BORDER);
		cucellTh3.add(
				new Paragraph("Age").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setBold());
		cusTable.addCell(cucellTh3);

		Cell cucellTh4 = new Cell();
		cucellTh4.setBorder(Border.NO_BORDER);
		cucellTh4.add(new Paragraph("Gender").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cucellTh4);
		cusTable.startNewRow();

		Cell cuCellM1 = new Cell();
		cuCellM1.setBorder(Border.NO_BORDER);
		cuCellM1.add(new Paragraph(quotationDetails.getCustomerDetails().getCustName() != null
				? quotationDetails.getCustomerDetails().getCustName()
				: " ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM1);

		Cell cuCellM2 = new Cell();
		cuCellM2.setBorder(Border.NO_BORDER);
		cuCellM2.add(new Paragraph(mainLifeOccupation != null ? mainLifeOccupation : " ").setFontSize(9)
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM2);

		Cell cuCellM3 = new Cell();
		cuCellM3.setBorder(Border.NO_BORDER);

		// Creating a Date Format for DOB
		SimpleDateFormat mainDob = new SimpleDateFormat("dd-MM-yyyy");

		cuCellM3.add(new Paragraph(quotationDetails.getCustomerDetails().getCustDob() != null
				? mainDob.format(quotationDetails.getCustomerDetails().getCustDob())
				: " ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellM3);

		Cell cuCellM4 = new Cell();
		cuCellM4.setBorder(Border.NO_BORDER);
		if (quoCustomer.getMainLifeAge() != null) {
			cuCellM4.add(new Paragraph(Integer.toString(quoCustomer.getMainLifeAge())).setFontSize(8)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCellM4.add(new Paragraph(" ").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCellM4);

		Cell cuCellM5 = new Cell();
		cuCellM5.setBorder(Border.NO_BORDER);

		// Check MainLife Gender If M->Male, F->Female
		if (quotationDetails.getCustomerDetails().getCustGender() != null) {

			if (quotationDetails.getCustomerDetails().getCustGender().equalsIgnoreCase("M")) {
				cuCellM5.add(
						new Paragraph("Male").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			} else if (quotationDetails.getCustomerDetails().getCustGender().equalsIgnoreCase("F")) {
				cuCellM5.add(new Paragraph("Female").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		} else {

		}
		cusTable.addCell(cuCellM5);

		/////////////////////////// *End Craeting Main Life Details*/

		cusTable.startNewRow();

		// Create an Empty Line
		Cell cuCellEmptyM = new Cell(0, 5);
		cuCellEmptyM.setBorder(Border.NO_BORDER);
		cuCellEmptyM.add(new Paragraph("").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellEmptyM);

		//////////////////////// * Strat Creating Spouse Details*/

		// checking Spouse is active or not
		if ((quoCustomer.getSpouseName()) != null) {

			Cell cuCellThS = new Cell();
			cuCellThS.setBorder(Border.NO_BORDER);
			cuCellThS.add(new Paragraph("Spouse Details").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10).setBold());
			cusTable.addCell(cuCellThS);

			cusTable.startNewRow();

			Cell cuCellS1 = new Cell();
			cuCellS1.setBorder(Border.NO_BORDER);
			cuCellS1.add(new Paragraph(quoCustomer.getSpouseName()).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCellS1);

		} else {

		}

		if ((quoCustomer.getSpouseOccupation()) != null) {

			Cell cuCellS2 = new Cell();
			cuCellS2.setBorder(Border.NO_BORDER);
			cuCellS2.add(new Paragraph(spouseOccupation).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			cusTable.addCell(cuCellS2);

		} else {

		}

		if (quotationDetails.getSpouseDetails() != null) {

			Cell cuCellS3 = new Cell();
			cuCellS3.setBorder(Border.NO_BORDER);
			cuCellS3.add(new Paragraph(mainDob.format(quotationDetails.getSpouseDetails().getCustDob())).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCellS3);

		} else {

		}

		if ((quoCustomer.getSpouseAge()) != null) {

			Cell cuCellS4 = new Cell();
			cuCellS4.setBorder(Border.NO_BORDER);
			cuCellS4.add(new Paragraph(Integer.toString(quoCustomer.getSpouseAge())).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			cusTable.addCell(cuCellS4);

		} else {

		}

		// Display Spouse Gender
		Cell cuCellS5 = new Cell();
		cuCellS5.setBorder(Border.NO_BORDER);

		if ((quotationDetails.getSpouseDetails()) != null) {

			if (quotationDetails.getSpouseDetails().getCustGender().equalsIgnoreCase("M")) {
				cuCellS5.add(
						new Paragraph("Male").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
			} else if (quotationDetails.getSpouseDetails().getCustGender().equalsIgnoreCase("F")) {
				cuCellS5.add(new Paragraph("Female").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

		} else {

		}

		cusTable.addCell(cuCellS5);

		/////////////////////////////////// * End of Spouse Details*/

		cusTable.startNewRow();

		// Create an Empty Line
		Cell cuCellEmptyP = new Cell(0, 5);
		cuCellEmptyP.setBorder(Border.NO_BORDER);
		cuCellEmptyP.add(new Paragraph("").setFontSize(9).setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		cusTable.addCell(cuCellEmptyP);

		//////////////////// * Strat Creating Plan Details*/
		Cell cuCellPlanTh = new Cell();
		cuCellPlanTh.add(new Paragraph("Plan Details").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setBold());
		cusTable.addCell(cuCellPlanTh);

		Cell cuCellP1 = new Cell();
		cuCellP1.add(new Paragraph("Term").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cuCellP1);

		Cell cuCellP2 = new Cell();
		cuCellP2.add(new Paragraph("Premium").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10)
				.setBold());
		cusTable.addCell(cuCellP2);

		Cell cuCellP3 = new Cell();
		cuCellP3.add(new Paragraph("Method").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setBold()
				.setFixedLeading(10));
		cusTable.addCell(cuCellP3);

		Cell cuCellP4 = new Cell();
		cuCellP4.add(new Paragraph("Basic Sum Assured").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setBold()
				.setFixedLeading(10));
		cusTable.addCell(cuCellP4);

		cusTable.startNewRow();

		// Display Product Code
		Cell cuCellP5 = new Cell();
		if (quotationDetails.getQuotation().getProducts().getProductCode() != null) {
			cuCellP5.add(new Paragraph(quotationDetails.getQuotation().getProducts().getProductCode()).setFontSize(9)
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		} else {
			cuCellP5.add(new Paragraph(" ").setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		}
		cusTable.addCell(cuCellP5);

		Cell cuCellP6 = new Cell();
		if (quotationDetails.getPolTerm() != null) {
			cuCellP6.add(new Paragraph(Integer.toString(quotationDetails.getPolTerm())).setFontSize(9)
					.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		} else {
			cuCellP6.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		}
		cusTable.addCell(cuCellP6);

		Cell cuCellP7 = new Cell();
		if (quoCustomer.getModePremium() != null) {
			cuCellP7.add(new Paragraph(formatter.format(quoCustomer.getTotPremium())).setFontSize(9)
					.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));
		} else {
			cuCellP7.add(new Paragraph(" ").setFontSize(9).setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));

		}
		cusTable.addCell(cuCellP7);

		Cell cuCellP8 = new Cell();

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

			cuCellP8.add(new Paragraph(modeMethod).setFontSize(9).setTextAlignment(TextAlignment.CENTER)
					.setFixedLeading(10));

		}
		cusTable.addCell(cuCellP8);

		// Display Basic Sum Assured
		Cell cuCellP9 = new Cell();

		if (benefitsLife.isEmpty()) {

		} else {

			for (QuoBenf bsa : benefitsLife) {
				System.out.println(bsa.getRiderCode());

				if (bsa.getRiderCode().equalsIgnoreCase("L2")) {

					if (bsa.getRiderSum() != null) {
						cuCellP9.add(new Paragraph(formatter.format(bsa.getRiderSum())).setFontSize(9)
								.setTextAlignment(TextAlignment.CENTER).setFixedLeading(10));

						cusTable.addCell(cuCellP9);

					} else {

					}

				}

			}

		}

		//////////////////// * End of Plan Details*/

		document.add(cusTable);
		//////////////////////////////// *End of MainLife/Spouse/Plan Details
		//////////////////////////////// Table*//////////

		document.add(new Paragraph("Benefits").setFontSize(9).setBold().setUnderline().setCharacterSpacing(1));

		//////////////////////////// Benefits Table
		//////////////////////////// FORMAT//////////////////////////////////////

		// Create Additional Benefits Table
		/* Declaring column sizes of the table respectively */
		float[] pointColumnWidths4 = { 500, 80, 80, 80, 80, 80, 80 };
		Table benAddTable = new Table(pointColumnWidths4);
		benAddTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		// table headings of the Living Benefits
		Cell alCellth1 = new Cell(2, 0);
		alCellth1.setBorder(new SolidBorder(1));
		alCellth1.add(new Paragraph("Living Benefits").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1).setMarginTop(10));
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
			alCellBenf.setBorderLeft(new SolidBorder(1));

			// Getting ALL Benefits Name object and cast to an String
			String p = (String) hashMap.get("benName");
			String maturity = (String) hashMap.get("combination");

			System.out.println("Combinationnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn " + maturity.equalsIgnoreCase("L3"));

			// Check Maturity values not equl then Display
			if (!maturity.equalsIgnoreCase("L11") && !maturity.equalsIgnoreCase("L12")
					&& !maturity.equalsIgnoreCase("L13")) {

				alCellBenf.add(new Paragraph(p).setFontSize(9).setTextAlignment(TextAlignment.LEFT));
				benAddTable.addCell(alCellBenf);
			}

			// Display Main Life Rider Amounts
			Cell alCellmA = new Cell();
			if (hashMap.get("mainAmt") != null) {

				// Getting ALL Benefits Name Combinations object and cast to an String
				String comb = (String) hashMap.get("combination");

				/* If benefit is WPB Print Amount as APPLIED */
				if (comb.equalsIgnoreCase("WPB")) {
					alCellmA.add(new Paragraph("Applied").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmA);

					// Check Maturity values not equl then Display
				} else if (!maturity.equalsIgnoreCase("L11") && !maturity.equalsIgnoreCase("L12")
						&& !maturity.equalsIgnoreCase("L13")) {

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

			// Display Main Life Rider Premium
			Cell alCellmP = new Cell();
			if (hashMap.get("mainPre") != null) {

				if (!maturity.equalsIgnoreCase("L11") && !maturity.equalsIgnoreCase("L12")
						&& !maturity.equalsIgnoreCase("L13")) {
					Double mPre = (Double) hashMap.get("mainPre");

					alCellmP.add(
							new Paragraph(formatter.format(mPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
					benAddTable.addCell(alCellmP);
				}

			} else {
				alCellmP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellmP);
			}

			// Display Spouse Rider Amounts
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

				// IF spouse Rider Not Equals Maturity Values and Null
			} else if (!maturity.equalsIgnoreCase("L11") && !maturity.equalsIgnoreCase("L12")
					&& !maturity.equalsIgnoreCase("L13")) {
				alCellsA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsA);
			}

			// Display Spouse Rider Premium
			Cell alCellsP = new Cell();
			if (hashMap.get("spousePre") != null) {
				Double spPre = (Double) hashMap.get("spousePre");

				alCellsP.add(
						new Paragraph(formatter.format(spPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsP);
			} else if (!maturity.equalsIgnoreCase("L11") && !maturity.equalsIgnoreCase("L12")
					&& !maturity.equalsIgnoreCase("L13")) {
				alCellsP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellsP);
			}

			// Display Child Rider Amounts
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

				// IF Child Riders Are not equal Maturities and Amount is null
			} else if (!maturity.equalsIgnoreCase("L11") && !maturity.equalsIgnoreCase("L12")
					&& !maturity.equalsIgnoreCase("L13")) {

				alCellcA.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcA);
			}

			// Display Child Riders Premium
			Cell alCellcP = new Cell();
			alCellcP.setBorderRight(new SolidBorder(1));

			if (hashMap.get("childPre") != null) {

				Double cPre = (Double) hashMap.get("childPre");

				alCellcP.add(
						new Paragraph(formatter.format(cPre)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcP);

			} else if (!maturity.equalsIgnoreCase("L11") && !maturity.equalsIgnoreCase("L12")
					&& !maturity.equalsIgnoreCase("L13")) {
				alCellcP.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
				benAddTable.addCell(alCellcP);
			}

		}

		benAddTable.startNewRow();

		// Creating Empty Cell to seperate Benefits Table from Maturity table
		Cell abCellMat = new Cell(0, 7);
		abCellMat.setBorder(Border.NO_BORDER);
		abCellMat.setBorderTop(new SolidBorder(1));
		abCellMat.add(new Paragraph(" ").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMat);

		benAddTable.startNewRow();

		// Craeting Maturity Values Table
		Cell abCellMat1 = new Cell();
		abCellMat1.setBorder(new SolidBorder(1));
		abCellMat1.add(new Paragraph("Assumed Annual Dividend Rate of").setFontSize(9).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1));
		benAddTable.addCell(abCellMat1);

		Cell abCellMat2 = new Cell(0, 2);
		abCellMat2.setBorder(new SolidBorder(1));

		abCellMat2.add(new Paragraph("8.0%").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMat2);

		Cell abCellMat3 = new Cell(0, 2);
		abCellMat3.setBorder(new SolidBorder(1));

		abCellMat3.add(new Paragraph("9.0%").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMat3);

		Cell abCellMat4 = new Cell(0, 2);
		abCellMat4.setBorder(new SolidBorder(1));
		abCellMat4.add(new Paragraph("10.0%").setFontSize(9).setBold().setTextAlignment(TextAlignment.CENTER)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMat4);

		benAddTable.startNewRow();

		Cell abCellMat5 = new Cell();
		abCellMat5.setBorderLeft(new SolidBorder(1));

		abCellMat5.add(new Paragraph("Illustrated Maturity Values").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setCharacterSpacing(1));
		benAddTable.addCell(abCellMat5);

		// Fetch array to get Maturity Values
		for (QuoBenf matValue : benefitsLife) {

			if (matValue.getRiderCode().equalsIgnoreCase("L11")) {

				Cell abCellMat6 = new Cell(0, 2);
				abCellMat6.add(new Paragraph(formatter.format(matValue.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT).setCharacterSpacing(1));
				benAddTable.addCell(abCellMat6);
			} else {

			}

			if (matValue.getRiderCode().equalsIgnoreCase("L12")) {

				Cell abCellMat7 = new Cell(0, 2);

				abCellMat7.add(new Paragraph(formatter.format(matValue.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT).setCharacterSpacing(1));
				benAddTable.addCell(abCellMat7);
			} else {

			}

			if (matValue.getRiderCode().equalsIgnoreCase("L13")) {

				Cell abCellMat8 = new Cell(0, 2);
				abCellMat8.setBorderRight(new SolidBorder(1));

				abCellMat8.add(new Paragraph(formatter.format(matValue.getRiderSum())).setFontSize(9)
						.setTextAlignment(TextAlignment.RIGHT).setCharacterSpacing(1));
				benAddTable.addCell(abCellMat8);
			} else {

			}

		}

		benAddTable.startNewRow();

		// Creating Empty Cell to seperate Additional Benefits
		Cell abCellAddEty = new Cell(0, 7);
		abCellAddEty.setBorder(Border.NO_BORDER);
		abCellAddEty.setBorderTop(new SolidBorder(1));
		benAddTable.addCell(abCellAddEty);

		////////////////////// table headings of the Additional Benefits
		////////////////////// table\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		Cell abCellth1 = new Cell(2, 0);
		abCellth1.setBorder(new SolidBorder(1));

		abCellth1.add(new Paragraph("Additional Benefits").setFontSize(9).setBold()
				.setTextAlignment(TextAlignment.CENTER).setCharacterSpacing(1).setMarginTop(10));
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
		abCelld1.setBorderLeft(new SolidBorder(1));
		abCelld1.add(new Paragraph("Natural Death Cover").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld1);

		Cell abCelld2 = new Cell();
		if (ndc == 0.0) {
			abCelld2.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld2.add(new Paragraph(formatter.format(ndc)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld2);

		Cell abCelld3 = new Cell();
		if (ndcp == 0.0) {
			abCelld3.add(new Paragraph(formatter.format(ndcp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld3.add(new Paragraph(formatter.format(ndcp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld3);

		Cell abCelld4 = new Cell();
		if (ndcs == 0.0) {
			abCelld4.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld4.add(new Paragraph(formatter.format(ndcs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld4);

		Cell abCelld5 = new Cell();
		if (ndcsp == 0.0) {
			abCelld5.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld5.add(new Paragraph(formatter.format(ndcsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld5);

		Cell abCelld6 = new Cell();
		abCelld6.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld6);

		Cell abCelld7 = new Cell();
		abCelld7.setBorderRight(new SolidBorder(1));

		abCelld7.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld7);

		benAddTable.startNewRow();

		Cell abCelld8 = new Cell();
		abCelld8.setBorderLeft(new SolidBorder(1));

		abCelld8.add(new Paragraph("Accidental Death Benefit").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld8);

		Cell abCelld9 = new Cell();
		if (adb == 0.0) {
			abCelld9.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld9.add(new Paragraph(formatter.format(adb)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld9);

		Cell abCelld10 = new Cell();
		if (adbp == 0.0) {
			abCelld10.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld10.add(new Paragraph(formatter.format(adbp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld10);

		Cell abCelld11 = new Cell();
		if (adbs == 0.0) {
			abCelld11.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld11.add(new Paragraph(formatter.format(adbs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld11);

		Cell abCelld12 = new Cell();
		if (adbsp == 0.0) {
			abCelld12.add(new Paragraph("-	").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld12.add(new Paragraph(formatter.format(adbsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld12);

		Cell abCelld13 = new Cell();
		abCelld13.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld13);

		Cell abCelld14 = new Cell();
		abCelld14.setBorderRight(new SolidBorder(1));

		abCelld14.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld14);

		benAddTable.startNewRow();

		Cell abCelld15 = new Cell();
		abCelld15.setBorderLeft(new SolidBorder(1));
		abCelld15.setBorderBottom(new SolidBorder(1));
		abCelld15.add(new Paragraph("Funeral Expenses").setFontSize(9).setTextAlignment(TextAlignment.LEFT));
		benAddTable.addCell(abCelld15);

		Cell abCelld16 = new Cell();
		abCelld16.setBorderBottom(new SolidBorder(1));
		if (feb == 0.0) {
			abCelld16.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld16.add(new Paragraph(formatter.format(feb)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld16);

		Cell abCelld117 = new Cell();
		abCelld117.setBorderBottom(new SolidBorder(1));
		if (febp == 0.0) {
			abCelld117.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld117.add(new Paragraph(formatter.format(febp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld117);

		Cell abCelld18 = new Cell();
		abCelld18.setBorderBottom(new SolidBorder(1));
		if (febs == 0.0) {
			abCelld18.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld18.add(new Paragraph(formatter.format(febs)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld18);

		Cell abCelld19 = new Cell();
		abCelld19.setBorderBottom(new SolidBorder(1));
		if (febsp == 0.0) {
			abCelld19.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		} else {
			abCelld19.add(new Paragraph(formatter.format(febsp)).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

		}
		benAddTable.addCell(abCelld19);

		Cell abCelld20 = new Cell();
		abCelld20.setBorderBottom(new SolidBorder(1));
		abCelld20.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld20);

		Cell abCelld21 = new Cell();
		abCelld21.setBorderBottom(new SolidBorder(1));
		abCelld21.setBorderRight(new SolidBorder(1));
		abCelld21.add(new Paragraph("-").setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
		benAddTable.addCell(abCelld21);

		///////////////////// End Of Additional Benefits Table
		///////////////////// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		document.add(benAddTable);

		/////////////////////////// END Of
		/////////////////////////// Benefits Table
		/////////////////////////// FORMAT///////////////////////////////////////

		//////////////////////////// * Medical Requirements
		//////////////////////////// Table*/////////////////////////

		java.util.List<MedicalRequirementsHelper> medicalDetails = medicalRequirementsDaoCustom
				.findByQuoDetail(quotationDetails.getQdId());

		document.add(new Paragraph(""));

		document.add(new Paragraph("Medical Requirements").setBold().setFontSize(9).setUnderline()
				.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));

		document.add(new Paragraph(""));

		// Medical Requirements Table
		float[] pointColumnWidths6 = { 160, 500 };
		Table medReqTable = new Table(pointColumnWidths6);
		medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

		Cell mrqCell1 = new Cell();
		mrqCell1.setBorder(Border.NO_BORDER);
		mrqCell1.add(new Paragraph("Main Life").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
				.setFixedLeading(10).setCharacterSpacing(1));
		medReqTable.addCell(mrqCell1);

		if (medicalDetails.isEmpty()) {

			Cell mrqCell2 = new Cell();
			mrqCell2.setBorder(Border.NO_BORDER);
			mrqCell2.add(new Paragraph(": Not Applied ").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			medReqTable.addCell(mrqCell2);

		} else {

			// Creating a String Seperator ArrayList Seperate from seperator
			final String SEPARATOR = " , ";

			StringBuilder mainLife = new StringBuilder();
			StringBuilder spouse = new StringBuilder();

			for (MedicalRequirementsHelper medicalReq : medicalDetails) {

				// When MainLife having medical requiremnents
				if (medicalReq.getMainStatus().equalsIgnoreCase("R")) {
					mainLife.append(medicalReq.getMedicalReqname());
					mainLife.append(SEPARATOR);
					// When MainLife not having medical Requirements
				} else if (medicalReq.getMainStatus().equalsIgnoreCase("NR")) {

				} else {
					mainLife.append("NR");
					mainLife.append(SEPARATOR);
				}

				// When Spouse having medical Requirements
				if (medicalReq.getSpouseStatus().equalsIgnoreCase("R")) {
					spouse.append(medicalReq.getMedicalReqname());
					spouse.append(SEPARATOR);

					// When Spouse Not having medical Requirements
				} else if (medicalReq.getSpouseStatus().equalsIgnoreCase("NR")) {

				} else {
					spouse.append("NR");
					spouse.append(SEPARATOR);
				}

			}

			String mainMedical = mainLife.toString();
			mainMedical = mainMedical.substring(0, mainMedical.length() - SEPARATOR.length());

			Cell mrqCell2 = new Cell();
			mrqCell2.setBorder(Border.NO_BORDER);
			mrqCell2.add(new Paragraph(": " + mainMedical).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
					.setFixedLeading(10));
			medReqTable.addCell(mrqCell2);

			medReqTable.startNewRow();

			if (benefitsSpouse.isEmpty()) {

			} else {

				String spouseMedical = spouse.toString();

				// System.out.println("spouse medicalllllllllllllllll " + spouseMedical);

				if (!spouseMedical.endsWith("NR") && !spouseMedical.isEmpty()) {

					spouseMedical = spouseMedical.substring(0, spouseMedical.length() - SEPARATOR.length());

					Cell mrqCell3 = new Cell();
					mrqCell3.setBorder(Border.NO_BORDER);
					mrqCell3.add(new Paragraph("Spouse").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
							.setFixedLeading(10).setCharacterSpacing(1));
					medReqTable.addCell(mrqCell3);

					Cell mrqCell4 = new Cell();
					mrqCell4.setBorder(Border.NO_BORDER);
					mrqCell4.add(new Paragraph(": " + spouseMedical).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
							.setFixedLeading(10));
					medReqTable.addCell(mrqCell4);
				}
			}

		}

		document.add(medReqTable);

		// try {
		//
		// if (medicalDetails.isEmpty()) {
		//
		// } else {
		// document.add(new Paragraph(""));
		//
		// document.add(new Paragraph("Medical
		// Requirements").setBold().setFontSize(9).setUnderline()
		// .setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));
		//
		// document.add(new Paragraph(""));
		//
		// // Medical Requirements Table
		// float[] pointColumnWidths6 = { 100, 500 };
		// Table medReqTable = new Table(pointColumnWidths6);
		// medReqTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
		//
		// // Creating a String Seperator ArrayList Seperate from seperator
		// final String SEPARATOR = " , ";
		//
		// StringBuilder mainLife = new StringBuilder();
		// StringBuilder spouse = new StringBuilder();
		//
		// for (MedicalRequirementsHelper medicalReq : medicalDetails) {
		//
		// // When MainLife having medical requiremnents
		// if (medicalReq.getMainStatus().equalsIgnoreCase("R")) {
		// mainLife.append(medicalReq.getMedicalReqname());
		// mainLife.append(SEPARATOR);
		// // When MainLife not having medical Requirements
		// } else if (medicalReq.getMainStatus().equalsIgnoreCase("NR")) {
		//
		// } else {
		// mainLife.append("NR");
		// mainLife.append(SEPARATOR);
		// }
		//
		// // When Spouse having medical Requirements
		// if (medicalReq.getSpouseStatus().equalsIgnoreCase("R")) {
		// spouse.append(medicalReq.getMedicalReqname());
		// spouse.append(SEPARATOR);
		//
		// // When Spouse Not having medical Requirements
		// } else if (medicalReq.getSpouseStatus().equalsIgnoreCase("NR")) {
		//
		// } else {
		// spouse.append("NR");
		// spouse.append(SEPARATOR);
		// }
		//
		// }
		//
		// String mainMedical = mainLife.toString();
		// mainMedical = mainMedical.substring(0, mainMedical.length() -
		// SEPARATOR.length());
		//
		// Cell mrqCell1 = new Cell();
		// mrqCell1.setBorder(Border.NO_BORDER);
		// mrqCell1.add(new Paragraph("Main
		// Life").setBold().setFontSize(9).setTextAlignment(TextAlignment.LEFT)
		// .setFixedLeading(10).setCharacterSpacing(1));
		// medReqTable.addCell(mrqCell1);
		//
		// Cell mrqCell2 = new Cell();
		// mrqCell2.setBorder(Border.NO_BORDER);
		// mrqCell2.add(new Paragraph(": " +
		// mainMedical).setFontSize(9).setTextAlignment(TextAlignment.LEFT)
		// .setFixedLeading(10));
		// medReqTable.addCell(mrqCell2);
		//
		// medReqTable.startNewRow();
		//
		// if (benefitsSpouse.isEmpty()) {
		//
		// } else {
		//
		// String spouseMedical = spouse.toString();
		//
		// // System.out.println("spouse medicalllllllllllllllll " + spouseMedical);
		//
		// if (!spouseMedical.endsWith("NR") && !spouseMedical.isEmpty()) {
		//
		// spouseMedical = spouseMedical.substring(0, spouseMedical.length() -
		// SEPARATOR.length());
		//
		// Cell mrqCell3 = new Cell();
		// mrqCell3.setBorder(Border.NO_BORDER);
		// mrqCell3.add(new Paragraph("Spouse").setBold().setFontSize(9)
		// .setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));
		// medReqTable.addCell(mrqCell3);
		//
		// Cell mrqCell4 = new Cell();
		// mrqCell4.setBorder(Border.NO_BORDER);
		// mrqCell4.add(new Paragraph(": " + spouseMedical).setFontSize(9)
		// .setTextAlignment(TextAlignment.LEFT).setFixedLeading(10));
		// medReqTable.addCell(mrqCell4);
		// }
		// }
		//
		// document.add(medReqTable);
		//
		// }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		//
		// }

		/////////////////////// *Start FinanCial Requirements*///////////////////

		// Creating A variable for Sum At Risk
		Double fiveM = 5000000.00;

		System.out.println(
				"sum at riskkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk" + quotationDetails.getSumAtRiskMain());

		if (quotationDetails.getSumAtRiskMain() != null) {

			document.add(new Paragraph(""));

			float[] finacialReqColoumWidth = { 150, 500 };
			Table finReqTbl = new Table(finacialReqColoumWidth);
			finReqTbl.setHorizontalAlignment(HorizontalAlignment.LEFT);

			Cell fReqCell1 = new Cell();
			fReqCell1.setBorder(Border.NO_BORDER);
			fReqCell1.add(new Paragraph("Financial Requirements").setBold().setFontSize(9).setUnderline()
					.setTextAlignment(TextAlignment.LEFT).setFixedLeading(10).setCharacterSpacing(1));
			finReqTbl.addCell(fReqCell1);

			Cell fReqCell2 = new Cell();

			// If Sum At Risk> 5000000
			if (quotationDetails.getSumAtRiskMain() >= fiveM) {

				fReqCell2.setBorder(Border.NO_BORDER);
				fReqCell2.add(new Paragraph(": Applied").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			} else {
				fReqCell2.setBorder(Border.NO_BORDER);
				fReqCell2.add(new Paragraph(": Not Applied").setFontSize(9).setTextAlignment(TextAlignment.LEFT)
						.setFixedLeading(10));

			}

			finReqTbl.addCell(fReqCell2);

			document.add(finReqTbl);

		}

		/////////////////////// *End of FinanCial Requirements*///////////////////

		document.add(new Paragraph(""));

		document.add(new Paragraph("Special Notes").setFontSize(9).setBold().setUnderline().setCharacterSpacing(1));

		// Creating a Special Notes List
		List list = new List(ListNumberingType.DECIMAL);
		list.setFontSize(9);

		ListItem item6 = new ListItem();
		item6.add(
				new Paragraph("Guranteed minimum dividend rate declared for " + calendar.get(Calendar.YEAR) + " - 9.0%")
						.setFontSize(9).setFixedLeading(10));
		list.add(item6);

		ListItem item1 = new ListItem();
		item1.add(
				new Paragraph("Premiums are on standard rates and could defer on life risk: Medical, Occupational etc.")
						.setFontSize(9).setFixedLeading(10));
		list.add(item1);

		ListItem item3 = new ListItem();
		item3.add(new Paragraph("All amounts are in Sri Lankan Rupees(LKR).").setFontSize(9).setFixedLeading(10));
		list.add(item3);

		ListItem item4 = new ListItem();
		item4.add(new Paragraph("Initial policy processing fee of Rs 300 (Payable only with initial deposit).")
				.setFontSize(9).setFixedLeading(10));
		list.add(item4);

		ListItem item5 = new ListItem();
		item5.add(new Paragraph(
				"In event of death by accident both Accident Cover and Natural Death Cover will be applicable.")
						.setFontSize(9).setFixedLeading(10));
		list.add(item5);

		ListItem item2 = new ListItem();
		item2.add(new Paragraph("This is an indicative quote only and is valid for 30 days from date of issue.")
				.setFontSize(9).setFixedLeading(10));
		list.add(item2);

		document.add(list);

		document.add(new Paragraph("This is a system generated report and therefore does not require a signature.")
				.setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER).setFixedPosition(50, 10, 500));

		document.close();

		return baos.toByteArray();

	}

}
