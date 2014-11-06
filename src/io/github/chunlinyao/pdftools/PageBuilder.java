package io.github.chunlinyao.pdftools;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

public class PageBuilder {

	private final PDF pdf;

	private PdfElement current;

	public PageBuilder(final PDF pdf) {
		this.pdf = pdf;
		this.pdf.closeCurrentPage();
		pdf.getDoc().newPage();
	}

	public PageBuilder(final PDF pdf, final Rectangle pageSize) {
		this.pdf = pdf;
		this.pdf.closeCurrentPage();
		pdf.getDoc().setPageSize(pageSize);
		pdf.getDoc().newPage();
	}

	public LabelBuilder label(final String text) {
		setCurrent(new LabelBuilder(this, text));
		return (LabelBuilder) current();
	}
	public BarcodeBuilder barcode(final String text) {
		setCurrent(new BarcodeBuilder(this, text));
		return (BarcodeBuilder) current();
	}
	public TableBuilder table(final float[] widths) {
		setCurrent(new TableBuilder(this, widths));
		return (TableBuilder) current();
	}

	public TableBuilder currentTable() {
		return (TableBuilder) current();
	}

	public PdfWriter getWriter() {
		return pdf.getWriter();
	}

	public float mmToPt(float mm) {
		return PdfUtility.mmToPt(mm);
	}

	public float[] mmToPt(float[] mm) {
		return PdfUtility.mmToPt(mm);
	}

	void done() {
		setCurrent(null);
	}

	private void setCurrent(final PdfElement next) {
		if (current != null) {
			current.done();
		}
		current = next;
	}

	private PdfElement current() {
		return current;
	}
}
