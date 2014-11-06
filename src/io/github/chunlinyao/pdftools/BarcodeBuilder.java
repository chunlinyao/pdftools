package io.github.chunlinyao.pdftools;

import java.awt.Color;

import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.Barcode;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class BarcodeBuilder implements PdfElement {

	private final PageBuilder page;
	private final String text;
	private final PdfPTable table;
	private float x;
	private float y;
	private float w = -1;
	private float h = -1;
	private boolean written = false;
	private Color color = Color.black;
	private Barcode barcode;

	public BarcodeBuilder(final PageBuilder page, final String text) {
		this.page = page;
		this.text = text;
		this.barcode = PdfUtility.createCode128(text);
		this.table = new PdfPTable(new float[] { 1 });
		if (Boolean.getBoolean("pdf.test")) {
			getDefaultCell().setBorderColor(Color.red);
			getDefaultCell().setBorderWidth(0.5f);
		} else {
			getDefaultCell().setBorder(Rectangle.NO_BORDER);
		}
	}

	public BarcodeBuilder at(final float x, final float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public BarcodeBuilder size(final float w, final float h) {
		this.w = w;
		this.h = h;
		return this;
	}

	public BarcodeBuilder padding(final float padding) {
		return padding(padding, padding, padding, padding);
	}

	public BarcodeBuilder padding(final float top, final float right, final float bottom, final float left) {
		getDefaultCell().setPaddingLeft(left);
		getDefaultCell().setPaddingRight(right);
		getDefaultCell().setPaddingTop(top);
		getDefaultCell().setPaddingBottom(bottom);
		return this;
	}

	public BarcodeBuilder bgColor(final Color color) {
		getDefaultCell().setBackgroundColor(color);
		return this;
	}

	public BarcodeBuilder color(final Color color) {
		this.color = color;
		return this;
	}

	public BarcodeBuilder horizontalAlignment(final int align) {
		getDefaultCell().setHorizontalAlignment(align);
		return this;
	}

	public BarcodeBuilder verticalAlignment(final int align) {
		getDefaultCell().setVerticalAlignment(align);
		return this;
	}

	public BarcodeBuilder barcode(final String text) {
		return page.barcode(text);
	}

	@Override
	public void done() {
		if (written) {
			return;
		}
		layout();
		table.getRow(0).setWidths(new float[] { table.getTotalWidth() });
		table.writeSelectedRows(0, -1, x, y, page.getWriter().getDirectContent());
		written = true;
	}

	/**
	 * @param ct
	 * @param status
	 * @return
	 */
	private void layout() {
		Image image = barcode.createImageWithBarcode(page.getWriter().getDirectContent(), color, color);
		configTable(image);
		table.addCell(image);
	}

	/**
	 * @param image
	 * @return
	 */
	private void configTable(Image image) {

		if (w == -1) {
			w = image.getWidth() + getDefaultCell().getEffectivePaddingLeft() + getDefaultCell().getEffectivePaddingRight() + 0.001f;
		}
		if (h == -1) {
			h = image.getHeight() + getDefaultCell().getEffectivePaddingBottom() + getDefaultCell().getEffectivePaddingTop() + 0.001f;
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
