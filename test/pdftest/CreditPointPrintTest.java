/**
 * 
 */
package pdftest;

import io.github.chunlinyao.pdftools.PDF;
import io.github.chunlinyao.pdftools.PageBuilder;
import io.github.chunlinyao.pdftools.PdfUtility;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.lowagie.text.Font;
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
				new PDF() {
					{
						output(fos);
						pageSize(new Rectangle(mmToPt(80), mmToPt(110)));
						page(new PageBuilder(this) {
							{
								firstTable();
								secondTable();
								table(mmToPt(new float[] { 76f })).at(
										mmToPt(2f),
										mmToPt(109f - 7f * 4f - 6f * 9f)).font(
										DEFAULT_FONT);

								float rowHeight = mmToPt(26);
								currentTable()
										.addRow(rowHeight)
										.addCell("签证单位意见(签章)")
										.horizontalAlignment(
												PdfPCell.ALIGN_LEFT)
										.verticalAlignment(PdfPCell.ALIGN_TOP)
										.padding(mmToPt(3), mmToPt(1), 2,
												mmToPt(1));

								label("年     月     日")
										.at(mmToPt(50), mmToPt(7)).font(
												DEFAULT_FONT);
							}

							/**
							 * 
							 */
							protected void secondTable() {
								table(mmToPt(new float[] { 8, 51, 17 })).at(
										mmToPt(2), mmToPt(109 - 7f * 4f)).font(
										DEFAULT_FONT);

								float rowHeight = mmToPt(6);
								currentTable()
										.addRow(rowHeight)
										.addCell("学\n习\n内\n容")
										.rowSpan(9)
										.addCell("科目")
										.horizontalAlignment(
												PdfPCell.ALIGN_JUSTIFIED_ALL)
										.padding(2, mmToPt(10), 2, mmToPt(10))
										.addCell("考核(考试)\n结果");
								for (int i = 0; i < 8; i++) {
									currentTable().addRow(rowHeight)
											.addCell("").addCell("");
								}
							}

							/**
							 * 
							 */
							private void firstTable() {
								table(mmToPt(new float[] { 17, 24, 18, 17 }))
										.at(mmToPt(2), mmToPt(109)).font(
												DEFAULT_FONT);

								float rowHeight = mmToPt(7);
								currentTable().addRow(rowHeight)
										.addCell("主办部门").addCell("").colSpan(3);
								currentTable().addRow(rowHeight)
										.addCell("办学单位").addCell("").colSpan(3);
								currentTable().addRow(rowHeight)
										.addCell("学习形式").addCell("")
										.addCell("总学时(分)");
								currentTable().addRow(rowHeight)
										.addCell("学习时间").addCell("").colSpan(3);
							}
						});

					}

				}.done();
			} catch (final FileNotFoundException e) {
				e.printStackTrace();
			}
			Desktop.getDesktop().open(file);
		} catch (final IOException e1) {
			e1.printStackTrace();
		}
	}
}
