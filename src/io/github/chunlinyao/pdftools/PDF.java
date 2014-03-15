package io.github.chunlinyao.pdftools;

import java.io.OutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

public class PDF {

	private OutputStream os;

	private Document doc;

	private Rectangle pageSize = PageSize.A4;

	private PdfWriter writer;

	private PageBuilder currentPage;

	/**
	 * 出力先
	 * 
	 * @param os
	 * @return
	 */
	public PDF output(final OutputStream os) {
		this.os = os;
		return this;
	}

	public PDF pageSize(final Rectangle size) {
		pageSize = size;
		return this;
	}

	public PageBuilder page() {
		return page(new PageBuilder(this, pageSize));
	}

	/**
	 * @param pageSize
	 * @return
	 */
	public PageBuilder page(final Rectangle pageSize) {
		this.pageSize = pageSize;
		return page();
	}

	public PageBuilder page(final PageBuilder pageBuilder) {
		createDoc();
		closeCurrentPage();
		setCurrentPage(pageBuilder);
		return currentPage();
	}

	public PageBuilder currentPage() {
		return currentPage;
	}

	public LabelBuilder label(final String text) {
		return currentPage().label(text);
	}

	public void done() {
		currentPage().done();
		getDoc().close();
	}

	public float mmToPt(float mm) {
		return PdfUtility.mmToPt(mm);
	}

	public float[] mmToPt(float[] mm) {
		float[] ret = new float[mm.length];
		for (int i = 0; i < mm.length; i++) {
			ret[i] = mmToPt(mm[i]);
		}
		return ret;
	}

	Document getDoc() {
		createDoc();
		return doc;
	}

	PdfWriter getWriter() {
		return writer;
	}

	/**
	 * 
	 */
	private void createDoc() {
		if (doc == null) {
			doc = new Document(pageSize);
			try {
				writer = PdfWriter.getInstance(doc, os);
				writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
			} catch (final DocumentException e) {
				throw new RuntimeException(e);
			}
			doc.open();
		}
	}

	private void setCurrentPage(final PageBuilder newPage) {
		currentPage = newPage;
	}

	/**
	 * 
	 */
	private void closeCurrentPage() {
		if (currentPage != null) {
			currentPage.done();
		}
	}

}
