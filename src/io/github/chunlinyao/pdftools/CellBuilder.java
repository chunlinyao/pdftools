package io.github.chunlinyao.pdftools;

import java.awt.Color;

import com.lowagie.text.Chunk;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.Barcode;
import com.lowagie.text.pdf.PdfPCell;

public class CellBuilder extends CellProps {

	RowBuilder row;
	String text;

	int colSpan = 1;
	int rowSpan = 1;
	boolean barcode = false;

	public CellBuilder(final RowBuilder rowBuilder, final String text) {
		super(rowBuilder);
		this.row = rowBuilder;
		this.text = text;
	}

	public CellBuilder font(final Font font) {
		this.font = new Font(font);
		return this;
	}

	public CellBuilder bgColor(final Color color) {
		this.backgroundColor = color;
		return this;
	}

	public CellBuilder color(final Color color) {
		this.color = color;
		return this;
	}

	public CellBuilder verticalAlignment(final int valign) {
		this.verticalAlignment = valign;
		return this;
	}

	public CellBuilder horizontalAlignment(final int halign) {
		this.horizontalAlignment = halign;
		return this;
	}

	public CellBuilder colSpan(final int colSpan) {
		this.colSpan = colSpan;
		return this;
	}

	public CellBuilder rowSpan(final int span) {
		this.rowSpan = span;
		return this;
	}

	public CellBuilder noWrap() {
		this.noWrap = true;
		return this;
	}

	public CellBuilder wrap() {
		this.noWrap = false;
		return this;
	}

	public CellBuilder text(final String text) {
		this.text = text;
		return this;
	}

	public CellBuilder padding(final float padding) {
		return padding(padding, padding, padding, padding);
	}

	public CellBuilder padding(final float top, final float right, final float bottom, final float left) {
		paddingLeft = left;
		paddingRight = right;
		paddingTop = top;
		paddingBottom = bottom;
		return this;
	}

	public CellBuilder barcode() {
		barcode = true;
		return this;
	}

	public CellBuilder addCell(final String text) {
		return row.add(new CellBuilder(row, text));
	}

	PdfPCell toCell() {
		final PdfPCell ret = createPdfCell(row, row.getTable().getDefaultCell());
		applyProperty(ret);
		return ret;
	}

	/**
	 * @return
	 */
	protected PdfPCell createPdfCell(RowBuilder row, PdfPCell defaultCell) {
		return barcode ? createBarcodeCell(row, defaultCell) : createTextCell(row, defaultCell);
	}

	/**
	 * @param row
	 * @param defaultCell
	 * @return
	 */
	private PdfPCell createBarcodeCell(RowBuilder row, PdfPCell defaultCell) {
		final Barcode barcode = PdfUtility.createCode128(text);
		defaultCell.setPhrase(new Phrase(new Chunk(barcode.createImageWithBarcode(row.getDirectContent(), color, color), 0, 0)));
		final PdfPCell ret = new PdfPCell(row.getTable().getDefaultCell());
		defaultCell.setPhrase(null);
		return ret;
	}

	/**
	 * @param row
	 * @param defaultCell
	 * @return
	 */
	private PdfPCell createTextCell(RowBuilder row, PdfPCell defaultCell) {
		final Phrase phrase = createPhraseWithFont();
		defaultCell.setPhrase(phrase);
		final PdfPCell ret = new PdfPCell(row.getTable().getDefaultCell());
		defaultCell.setPhrase(null);
		return ret;
	}

	private void applyProperty(final PdfPCell ret) {
		ret.setFixedHeight(row.rowHeight);
		if (horizontalAlignment != null) {
			ret.setHorizontalAlignment(horizontalAlignment);
		}
		if (verticalAlignment != null) {
			ret.setVerticalAlignment(verticalAlignment);
		}
		if (noWrap != null) {
			ret.setNoWrap(noWrap);
		}
		if (barcode) {
			ret.setNoWrap(true);
		}
		if (paddingLeft != null) {
			ret.setPaddingLeft(paddingLeft);
		}
		if (paddingRight != null) {
			ret.setPaddingRight(paddingRight);
		}
		if (paddingTop != null) {
			ret.setPaddingTop(paddingTop);
		}
		if (paddingBottom != null) {
			ret.setPaddingBottom(paddingBottom);
		}
		if (backgroundColor != null) {
			ret.setBackgroundColor(backgroundColor);
		}
		if (color != null && ret.getPhrase() != null) {
			ret.getPhrase().getFont().setColor(color);
		}
		ret.setColspan(colSpan);
		ret.setRowspan(rowSpan);
	}

	/**
	 * @return
	 */
	private Phrase createPhraseWithFont() {
		final Phrase phrase = new Phrase(text, font);
		return phrase;
	}

}
