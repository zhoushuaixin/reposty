package cn.itcast.bos.test;

import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

public class TestPDF {

	public static void main(String[] args) throws Exception {
		// 创建文档
		Document document = new Document();
		// 获取pdf的实例
		PdfWriter.getInstance(document, new FileOutputStream("test.pdf"));
		document.open();
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		document.add(new Paragraph("Hello 传智播客!",new Font(bfChinese)));
		document.close();
	}
}
