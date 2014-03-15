/**
 * 
 */
package pdftest;

import io.github.chunlinyao.pdftools.NUpTool;
import io.github.chunlinyao.pdftools.PDF;
import io.github.chunlinyao.pdftools.PageBuilder;
import io.github.chunlinyao.pdftools.PdfUtility;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;

/**
 * @author yaochunlin
 * 
 */
public class CreditPointPrintTest {

	private static final Font DEFAULT_FONT = new Font(PdfUtility.getFzSongTi(),
			10);

	@Test
	public void testTable() {
		File file;
		try {
			file = File.createTempFile("test", ".pdf");
			try (FileOutputStream fos = new FileOutputStream(file)) {
				creditPdf(fos);
			} catch (final FileNotFoundException e) {
				e.printStackTrace();
			}
			Desktop.getDesktop().open(file);
		} catch (final IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * @param fos
	 */
	protected void creditPdf(final OutputStream os) {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		new PDF() {
			{
				output(bos);
				pageSize(new Rectangle(PageSize.A4.getWidth() / 2f,
						PageSize.A4.getHeight() / 2f));
				for (int i = 0; i < 7; i++) {
					page(createOnePage(this));
				}
			}

		}.done();
		try {
			new NUpTool().manipulatePdf(bos.toByteArray(), os, 2, PageSize.A4);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return
	 */
	private PageBuilder createOnePage(PDF pdf) {
		return new PageBuilder(pdf) {
			private float X = 15f;
			private float Y = 129f;
			{
				firstTable();
				secondTable();
				thirdTable();
			}

			/**
			 * 
			 */
			private void thirdTable() {
				table(mmToPt(new float[] { 76f })).at(mmToPt(X),
						mmToPt(Y - 7f * 4f - 6f * 9f)).font(DEFAULT_FONT);

				float rowHeight = mmToPt(26);
				currentTable().addRow(rowHeight).addCell("签证单位意见(签章)")
						.horizontalAlignment(PdfPCell.ALIGN_LEFT)
						.verticalAlignment(PdfPCell.ALIGN_TOP)
						.padding(mmToPt(3), mmToPt(1), 2, mmToPt(1));
				label("年     月     日").at(mmToPt(X + 48f), mmToPt(Y - 102f))
						.font(DEFAULT_FONT);
			}

			/**
			 * 
			 */
			private void secondTable() {
				table(mmToPt(new float[] { 8, 51, 17 })).at(mmToPt(X),
						mmToPt(Y - 7f * 4f)).font(DEFAULT_FONT);

				float rowHeight = mmToPt(6);
				currentTable().addRow(rowHeight).addCell("学\n习\n内\n容")
						.rowSpan(9).addCell("科目")
						.horizontalAlignment(PdfPCell.ALIGN_JUSTIFIED_ALL)
						.padding(2, mmToPt(10), 2, mmToPt(10))
						.addCell("考核(考试)\n结果");
				for (int i = 0; i < 8; i++) {
					currentTable().addRow(rowHeight).addCell("").addCell("");
				}
			}

			/**
			 * 
			 */
			private void firstTable() {
				table(mmToPt(new float[] { 17, 24, 18, 17 })).at(mmToPt(X),
						mmToPt(Y)).font(DEFAULT_FONT);

				float rowHeight = mmToPt(7);
				currentTable().addRow(rowHeight).addCell("主办部门")
						.addCell(String.valueOf(++i)).colSpan(3);
				currentTable().addRow(rowHeight).addCell("办学单位").addCell("")
						.colSpan(3);
				currentTable().addRow(rowHeight).addCell("学习形式").addCell("")
						.addCell("总学时(分)");
				currentTable().addRow(rowHeight).addCell("学习时间").addCell("")
						.colSpan(3);
			}
		};
	}

	private static int i = 0;
}
