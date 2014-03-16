package io.github.chunlinyao.pdftools;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.ExtraHeightGetter;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPRow;
import com.lowagie.text.pdf.PdfPTable;

public class TableBuilder implements PdfElement, Iterable<RowBuilder> {

	private final PageBuilder page;
	public final PdfPTable table;
	private float x;
	private float y;
	private float w = -1;
	private float h = -1;
	private boolean written = false;
	private RowBuilder currentRow;
	private final List<RowBuilder> rows = new ArrayList<>();

	Font font = new Font(PdfUtility.getFzSongTi(), 12f);
	Color color;

	public TableBuilder(final PageBuilder page, final float[] widths) {
		this.page = page;
		this.table = new PdfPTable(widths.length);
		try {
			this.table.setTotalWidth(widths);
			this.table.setLockedWidth(true);
		} catch (final DocumentException e) {
			// nothing todo
		}
		getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		getDefaultCell().setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		// getDefaultCell().setBorder(Rectangle.NO_BORDER);
	}

	public RowBuilder currentRow() {
		return currentRow;
	}

	public TableBuilder at(final float x, final float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public TableBuilder scaleTo(final float w, final float h) {
		this.w = w;
		this.h = h;
		return this;
	}

	public TableBuilder noWrap() {
		getDefaultCell().setNoWrap(true);
		return this;
	}

	public TableBuilder font(final Font font) {
		this.font = new Font(font);
		return this;
	}

	public TableBuilder padding(final float padding) {
		return padding(padding, padding, padding, padding);
	}

	public TableBuilder padding(final float top, final float right,
			final float bottom, final float left) {
		getDefaultCell().setPaddingLeft(left);
		getDefaultCell().setPaddingRight(right);
		getDefaultCell().setPaddingTop(top);
		getDefaultCell().setPaddingBottom(bottom);
		return this;
	}

	public TableBuilder bgColor(final Color color) {
		getDefaultCell().setBackgroundColor(color);
		return this;
	}

	public TableBuilder color(final Color color) {
		this.color = color;
		return this;
	}

	public TableBuilder horizontalAlignment(final int align) {
		getDefaultCell().setHorizontalAlignment(align);
		return this;
	}

	public TableBuilder verticalAlignment(final int align) {
		getDefaultCell().setVerticalAlignment(align);
		return this;
	}

	public TableBuilder table(final float[] widths) {
		return page.table(widths);
	}

	public RowBuilder addRow(final float rowHeight) {
		currentRow = new RowBuilder(this, rowHeight);
		rows.add(currentRow);
		return currentRow;
	}

	@Override
	public Iterator<RowBuilder> iterator() {
		return rows.iterator();
	}

	@Override
	public void done() {
		if (written) {
			return;
		}
		layout();
		final PdfContentByte directContent = page.getWriter()
				.getDirectContent();
		draw2(directContent);
		written = true;
	}

	public float mmToPt(float mm) {
		return PdfUtility.mmToPt(mm);
	}

	public float[] mmToPt(float[] mm) {
		return PdfUtility.mmToPt(mm);
	}

	private void draw2(PdfContentByte directContent) {
		table.writeSelectedRows(0, -1, x, y, directContent);
	}

	/**
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void fixItextBugs() {
		{// HACK fix rawSpan bugs.
			ArrayList tmp = table.getRows(0, table.getRows().size());
			table.getRows().clear();
			table.getRows().addAll(tmp);
		}
	}

	@SuppressWarnings("unused")
	private void draw1(PdfContentByte directContent) {
		ColumnText ct = new ColumnText(directContent);
		ct.setSimpleColumn(x, y - table.getTotalHeight() - 0.001f,
				x + table.getTotalWidth(), y);
		ct.addElement(table);
		try {
			ct.go();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param ct
	 * @param status
	 * @return
	 */
	private void layout() {
		confitTable();
		for (final RowBuilder row : rows) {
			for (final CellBuilder cell : row) {
				final PdfPCell pdfCell = cell.toCell();
				table.addCell(pdfCell);
			}
			table.completeRow();
		}
		fixItextBugs();
		for (Object row : table.getRows()) {
			PdfPRow lastRow = (PdfPRow) row;
			float[] extraHeights = ExtraHeightGetter.getExtraHeights(lastRow);
			for (int i = 0; i < lastRow.getCells().length; i++) {
				final PdfPCell pdfCell = lastRow.getCells()[i];
				if (pdfCell != null && pdfCell.getPhrase() != null) {
					new ShrinkTextHelper(pdfCell.isNoWrap())
							.shrintTextToFitCell(pdfCell, extraHeights[i]
									+ lastRow.getMaxHeights(),
									pdfCell.getPhrase());
					pdfCell.setPhrase(pdfCell.getPhrase());
				}
			}
			lastRow.setWidths(table.getAbsoluteWidths());
		}
	}

	/**
	 * @return
	 */
	private void confitTable() {
		// final float usize = text.getFont().getSize();
		// final BaseFont ufont = text.getFont().getBaseFont();
		// final float factor = ufont.getFontDescriptor(BaseFont.BBOXURY, 1) -
		// ufont.getFontDescriptor(BaseFont.BBOXLLY, 1);
		// if (w == -1) {
		// w = ufont.getWidthPoint(text.getContent(), usize) +
		// getDefaultCell().getEffectivePaddingLeft() +
		// getDefaultCell().getEffectivePaddingRight();
		// }
		// if (h == -1) {
		// h = usize * factor + getDefaultCell().getEffectivePaddingBottom() +
		// getDefaultCell().getEffectivePaddingTop();
		// }
		// this.table.setTotalWidth(w);
		// this.table.getDefaultCell().setFixedHeight(h);
	}

	/**
	 * @return
	 */
	PdfPCell getDefaultCell() {
		return this.table.getDefaultCell();
	}

}
