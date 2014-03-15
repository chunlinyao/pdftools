package com.lowagie.text.pdf;

public class ExtraHeightGetter {

	public static float[] getExtraHeights(PdfPRow row) {
		return row.extraHeights;
	}
}