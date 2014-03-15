/**
 * 
 */
package io.github.chunlinyao.pdftools;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author yaochunlin
 * 
 */
public class NUpTool {

	/**
	 * Manipulates a PDF file src with the file dest as result
	 * 
	 * @param src
	 *            the original PDF
	 * @param dest
	 *            the resulting PDF
	 * @param pow
	 *            the PDF will be N-upped with N = Math.pow(2, pow);
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void manipulatePdf(byte[] src, OutputStream dest, int pow,
			Rectangle destPageSize) throws IOException, DocumentException {
		// reader for the src file
		PdfReader reader = new PdfReader(src);
		// initializations
		Rectangle pageSize = reader.getPageSize(1);
		Rectangle newSize = (pow % 2) == 0 ? new Rectangle(pageSize.getWidth(),
				pageSize.getHeight()) : new Rectangle(pageSize.getHeight(),
				pageSize.getWidth());
		if (destPageSize != null) {
			newSize = destPageSize;
		}
		Rectangle unitSize = new Rectangle(newSize.getWidth(),
				newSize.getHeight());
		for (int i = 0; i < pow; i++) {
			unitSize = new Rectangle(unitSize.getHeight() / 2,
					unitSize.getWidth());
		}
		int n = (int) Math.pow(2, pow);
		int r = (int) Math.pow(2, pow / 2);
		int c = n / r;
		// step 1
		Document document = new Document(newSize, 0, 0, 0, 0);
		// step 2
		PdfWriter writer = PdfWriter.getInstance(document, dest);
		// step 3
		document.open();
		// step 4
		PdfContentByte cb = writer.getDirectContent();
		PdfImportedPage page;
		Rectangle currentSize;
		float offsetX, offsetY, factor;
		int total = reader.getNumberOfPages();
		for (int i = 0; i < total;) {
			if (i % n == 0) {
				document.newPage();
			}
			currentSize = reader.getPageSize(i+1);
			factor = Math.min(unitSize.getWidth() / currentSize.getWidth(),
					unitSize.getHeight() / currentSize.getHeight());
			offsetX = unitSize.getWidth() * ((i % n) % c)
					+ (unitSize.getWidth() - (currentSize.getWidth() * factor))
					/ 2f;
			offsetY = newSize.getHeight()
					- (unitSize.getHeight() * (((i % n) / c) + 1))
					+ (unitSize.getHeight() - (currentSize.getHeight() * factor))
					/ 2f;
			page = writer.getImportedPage(reader, i+1);
			cb.addTemplate(page, factor, 0, 0, factor, offsetX, offsetY);
			++i;
		}
		// step 5
		document.close();
	}

}
