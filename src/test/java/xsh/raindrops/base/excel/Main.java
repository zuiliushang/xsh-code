package xsh.raindrops.base.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

import xsh.raindrops.struct.dom.org.w3c.dom.Dictionary;

public class Main {
	
	@Test
	public void test01() throws IOException {
		InputStream excelFile = Main.class.getClassLoader().getResourceAsStream("test.xls");
		HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
		HSSFSheet sheet = workbook.getSheet("SQL Results");
		Iterator<Row> iterator = sheet.iterator();
		if (iterator.hasNext()) {//第一行为头
			iterator.next();
		}
		while (iterator.hasNext()) {
			Row row = iterator.next();
			Dictionary dictionary = new Dictionary();
			dictionary.setDepartName(row.getCell(9).getStringCellValue());
			dictionary.setDicExplain(row.getCell(12).getStringCellValue());
			dictionary.setDicName(row.getCell(10).getStringCellValue());
			dictionary.setDicResult(row.getCell(11).getStringCellValue());
			dictionary.setDicUnit(row.getCell(13).getStringCellValue());
			dictionary.setDicCode(row.getCell(1).getStringCellValue());
			System.out.println(dictionary);
		}
	}
	
}
