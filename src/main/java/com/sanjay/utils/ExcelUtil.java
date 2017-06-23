package com.sanjay.utils;

import java.io.File;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;

public class ExcelUtil {
	private String fileName;
	private String sheetName;
	private Workbook workbook = null;
	private Sheet sheet = null;

	public ExcelUtil(String fileName, String sheetName) {
		this.fileName = fileName;
		this.sheetName = sheetName;
	}

	public void close() {
		this.workbook.close();
	}

	public String getCellData(int rowNo, int colNo) throws BiffException,
			IOException {
		if (this.workbook == null) {
			this.workbook = Workbook.getWorkbook(new File(this.fileName));
			this.sheet = this.workbook.getSheet(this.sheetName);
			this.sheet.getSettings().setAutomaticFormulaCalculation(true);
		}
		String cellValue = this.sheet.getCell(colNo, rowNo).getContents()
				.toString();
		if (cellValue.equalsIgnoreCase("currentDate")) {
			return DateUtil.getDate();
		} else if (cellValue.equalsIgnoreCase("randomValue")) {
			String randomValue = "Auto"
					+ String.valueOf(RandomValue.getrandomValue());
			if (randomValue.length() > 20) {
				randomValue = StringUtils.left(randomValue, 20);
			}
			return randomValue;
		} else {
			return cellValue;
		}
	}
	public Object[][] getSheetInfoAsObjectArray(boolean withHeader)
			throws BiffException, IOException {
		String[][] dataSheet = null;
		int startRow = 0;
		if (this.workbook == null) {
			this.workbook = Workbook.getWorkbook(new File(this.fileName));
		}
		this.sheet = this.workbook.getSheet(this.sheetName);
		this.sheet.getSettings().setAutomaticFormulaCalculation(true);
		int totalRows = this.sheet.getRows();
		int totalCols = this.sheet.getColumns();

		if (withHeader) {
			dataSheet = new String[totalRows][totalCols];

		} else {
			dataSheet = new String[totalRows - 1][totalCols];
			startRow = 1;
		}
		int r = 0;
		for (int i = startRow; i < totalRows; i++, r++) {
			for (int j = 0; j < totalCols; j++) {
				dataSheet[r][j] = getCellData(i, j);
			}
		}

		return dataSheet;
	}

	// Coulmn wise data reading
	public Object[][] getVerticleInfoAsObjectArray(boolean withHeader)
			throws BiffException, IOException {
		String[][] dataSheet = null;
		int startRow = 0;
		if (this.workbook == null) {
			this.workbook = Workbook.getWorkbook(new File(this.fileName));
			this.sheet = this.workbook.getSheet(this.sheetName);
			this.sheet.getSettings().setAutomaticFormulaCalculation(true);
		}
		int totalRows = this.sheet.getRows();
		int totalCols = this.sheet.getColumns();
		if (withHeader) {
			dataSheet = new String[totalCols][totalRows];

		} else {
			dataSheet = new String[totalCols - 1][totalRows];
			startRow = 1;
		}
		int r = 0;
		for (int i = startRow; i < totalCols; i++, r++) {
			for (int j = 0; j < totalRows; j++) {
				dataSheet[r][j] = getCellData(j, i);

			}
		}

		return dataSheet;
	}

	public String getSheetName() {
		return this.sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
}
