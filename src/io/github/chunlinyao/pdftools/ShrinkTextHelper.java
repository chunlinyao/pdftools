package io.github.chunlinyao.pdftools;

import java.util.ArrayList;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPRow;

public class ShrinkTextHelper {
	private static final int MIN_FONT_SIZE = 4;

	private final boolean noWrap;

	/**
	 * デフォルトコンストラクタ。
	 */
	public ShrinkTextHelper(final boolean noWrap) {
		super();
		this.noWrap = noWrap;
	}

	/**
	 * @param cell
	 * @param maxHeight
	 */
	public void shrintTextToFitCell(final PdfPCell cell, float maxHeight,
			final Phrase text) {
		final ColumnText ct = ColumnText.duplicate(cell.getColumn());
		float right, top, left, bottom;
		right = cell.getRight() - cell.getEffectivePaddingRight();
		top = cell.getTop() - cell.getEffectivePaddingTop();
		left = cell.getLeft() + cell.getEffectivePaddingLeft();
		bottom = cell.getTop() + cell.getEffectivePaddingBottom() - maxHeight;
		// HACK fix vertical align middle
		cell.setFixedHeight(maxHeight);
		float usize = text.getFont().getSize();
		int lines = 0;
		if (noWrap) {
			lines = countLines(text.getContent());
		}
		if (usize > MIN_FONT_SIZE) {
			final float step = Math.max((usize - MIN_FONT_SIZE) / 10, 0.2f);
			PdfPRow.setColumn(ct, left, bottom, right, top);
			final float origYline = ct.getYLine();
			ct.setAlignment(cell.getHorizontalAlignment());
			for (; usize > MIN_FONT_SIZE; usize -= step) {
				ct.setYLine(origYline);
				changeFontSize(text, usize);
				ct.setText(text);
				try {
					final int status = ct.go(true);
					if ((status & ColumnText.NO_MORE_COLUMN) == 0
							&& (noWrap == false || lines >= ct
									.getLinesWritten())) {
						break;
					}
				} catch (final DocumentException e) {
					throw new ExceptionConverter(e);
				}
			}
		}
		if (usize < MIN_FONT_SIZE) {
			usize = MIN_FONT_SIZE;
		}
		changeFontSize(text, usize);
	}

	private static int countLines(final String str) {
		if (str == null) {
			return 1;
		} else {
			final String[] lines = str.split("\r\n|\r|\n");
			return lines.length;
		}
	}

	/**
	 * @param size
	 */
	private void changeFontSize(final Phrase text, final float size) {
		text.getFont().setSize(size);
		copyFontFromPharseToChunk(text);
	}

	/**
	 * @param font
	 */
	private void copyFontFromPharseToChunk(final Phrase text) {
		final Font font = text.getFont();
		@SuppressWarnings("unchecked")
		final ArrayList<Chunk> chunks = text.getChunks();
		for (final Chunk c : chunks) {
			c.setFont(font);
		}
	}

}
