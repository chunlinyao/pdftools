package pdftest;

import io.github.chunlinyao.pdftools.PDF;
import io.github.chunlinyao.pdftools.PageBuilder;
import io.github.chunlinyao.pdftools.PdfUtility;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPTableEvent;

public class PdfToolsTest {

	@Test
	public void testLabel() {
		File file;
		try {
			file = File.createTempFile("test", ".pdf");
			try (FileOutputStream fos = new FileOutputStream(file)) {
				new PDF() {
					{
						output(fos);

						page(new PageBuilder(this, PageSize.A4) {
							{
								label("TEST").at(100, 700).size(100, 10);
								label("YYYYYYYYYY").at(100, 100).size(100, 15);
								label("XXX\nXX\nXXX").at(100, 100)
										.size(100, 15);

							}
						});

						page().label("test").at(100, 600).size(100, 15)
								.label("someText").at(100, 500).size(100, 20);
						page().label("asdfasdf").at(100, 500).size(100, 20)
								.horizontalAlignment(Element.ALIGN_CENTER);
						label("XXX\nXX\nXXX").at(100, 100).size(100, 15).done();
						currentPage()
								.label("XXXXXXXXXXXXXXXXXXXX\nXXXXXXXXXXXXXXXXXXXXXXXXX")
								.noWrap().at(100, 200).size(100, 15);
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

	@Test
	public void testTable() {
		File file;
		try {
			file = File.createTempFile("test", ".pdf");
			try (FileOutputStream fos = new FileOutputStream(file)) {
				new PDF() {
					{
						output(fos);

						page(new PageBuilder(this) {
							{
								table(new float[] { 100, 200, 100 })
										.at(20, 600);

								currentTable()
										.addRow(16)
										.addCell("test table")
										.addCell(
												"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
										.color(Color.red);
								currentTable()
										.addRow(20)
										.addCell(
												"simplexxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
										.colSpan(2);
								currentTable().table.getDefaultCell()
										.setBorder(Rectangle.NO_BORDER);
								currentTable().table.getDefaultCell()
										.setCellEvent(new DottedCell());
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

	class DottedCell implements PdfPCellEvent {
		@Override
		public void cellLayout(PdfPCell cell, Rectangle position,
				PdfContentByte[] canvases) {
			PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
			canvas.setLineDash(3f, 3f);
			canvas.rectangle(position.getLeft(), position.getBottom(),
					position.getWidth(), position.getHeight());
			canvas.stroke();
		}
	}

	class DottedCells implements PdfPTableEvent {
		@Override
		public void tableLayout(PdfPTable table, float[][] widths,
				float[] heights, int headerRows, int rowStart,
				PdfContentByte[] canvases) {
			PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
			canvas.setLineDash(3f, 3f);
			float llx = widths[0][0];
			float urx = widths[0][widths[0].length - 1];
			for (int i = 0; i < heights.length; i++) {
				canvas.moveTo(llx, heights[i]);
				canvas.lineTo(urx, heights[i]);
			}
			for (int i = 0; i < widths.length; i++) {
				for (int j = 0; j < widths[i].length; j++) {
					canvas.moveTo(widths[i][j], heights[i]);
					canvas.lineTo(widths[i][j], heights[i + 1]);
				}
			}
			canvas.stroke();
		}
	}
}
