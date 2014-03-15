package io.github.chunlinyao.pdftools;

import java.awt.Color;
import java.util.ArrayList;

import com.lowagie.text.Chunk;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class LabelBuilder implements PdfElement {

	private final PageBuilder page;
	private final Phrase text;
	private final PdfPTable table;
	private float x;
	private float y;
	private float w = -1;
	private float h = -1;
	private boolean written = false;
	private Color color = Color.black;

	public LabelBuilder(final PageBuilder page, final String text) {
		this.page = page;
		this.text = Phrase.getInstance(16, text,
				new Font(PdfUtility.getSimsun(), 12f));
		this.table = new PdfPTable(new float[] { 1 });
		if (Boolean.getBoolean("pdf.test")) {
			getDefaultCell().setBorderColor(Color.red);
			getDefaultCell().setBorderWidth(0.5f);
		} else {
			getDefaultCell().setBorder(Rectangle.NO_BORDER);
		}
	}

	public LabelBuilder at(final float x, final float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public LabelBuilder size(final float w, final float h) {
		this.w = w;
		this.h = h;
		return this;
	}

	public LabelBuilder font(final Font font) {
		changeFont(new Font(font));
		return this;
	}

	public LabelBuilder noWrap() {
		getDefaultCell().setNoWrap(true);
		return this;
	}

	public LabelBuilder padding(final float padding) {
		return padding(padding, padding, padding, padding);
	}

	public LabelBuilder padding(final float top, final float right,
			final float bottom, final float left) {
		getDefaultCell().setPaddingLeft(left);
		getDefaultCell().setPaddingRight(right);
		getDefaultCell().setPaddingTop(top);
		getDefaultCell().setPaddingBottom(bottom);
		return this;
	}

	public LabelBuilder bgColor(final Color color) {
		getDefaultCell().setBackgroundColor(color);
		return this;
	}

	public LabelBuilder color(final Color color) {
		this.color = color;
		return this;
	}

	public LabelBuilder horizontalAlignment(final int align) {
		getDefaultCell().setHorizontalAlignment(align);
		return this;
	}

	public LabelBuilder verticalAlignment(final int align) {
		getDefaultCell().setVerticalAlignment(align);
		return this;
	}

	public LabelBuilder label(final String text) {
		return page.label(text);
	}

	@Override
	public void done() {
		if (written) {
			return;
		}
		text.getFont().setColor(color);
		layout();
		table.getRow(0).getCells()[0].getColumn().setText(text);
		table.getRow(0).setWidths(new float[] { table.getTotalWidth() });
		table.writeSelectedRows(0, -1, x, y, page.getWriter()
				.getDirectContent());
		written = true;
	}

	/**
	 * @param size
	 */
	private void changeFont(final Font font) {
		text.setFont(font);
		@SuppressWarnings("unchecked")
		final ArrayList<Chunk> chunks = text.getChunks();
		for (final Chunk c : chunks) {
			c.setFont(font);
		}
	}

	/**
	 * @param ct
	 * @param status
	 * @return
	 */
	private void layout() {
		configTable();
		table.addCell(text);
		final PdfPCell cell = table.getRow(0).getCells()[0];
		new ShrinkTextHelper(table.getDefaultCell().isNoWrap())
				.shrintTextToFitCell(cell, h, text);
	}

	/**
	 * @return
	 */
	private void configTable() {
		final float usize = text.getFont().getSize();
		final BaseFont ufont = text.getFont().getBaseFont();
		final float factor = ufont.getFontDescriptor(BaseFont.BBOXURY, 1)
				- ufont.getFontDescriptor(BaseFont.BBOXLLY, 1);
		if (w == -1) {
			w = ufont.getWidthPoint(text.getContent(), usize)
					+ getDefaultCell().getEffectivePaddingLeft()
					+ getDefaultCell().getEffectivePaddingRight() + 0.001f;
		}
		if (h == -1) {
			h = usize * factor + getDefaultCell().getEffectivePaddingBottom()
					+ getDefaultCell().getEffectivePaddingTop() + 0.001f;
		}
		this.table.setTotalWidth(w);
		this.table.getDefaultCell().setFixedHeight(h);
	}

	/**
	 * @return
	 */
	private PdfPCell getDefaultCell() {
		return this.table.getDefaultCell();
	}
}
