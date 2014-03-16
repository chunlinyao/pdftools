package com.lowagie.text.pdf;

import java.lang.reflect.Field;

public class ExtraHeightGetter {
	private static Field field = null;

	static {
		try {
			field = PdfPRow.class.getDeclaredField("extraHeights");
			field.setAccessible(true);
		} catch (NoSuchFieldException e) {
			// do nothing
		}
	}

	public static float[] getExtraHeights(PdfPRow row) {
		if (field != null) {
			try {
				return (float[]) field.get(row);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException("unable to get extra heightsF", e);
			}
		} else {
			throw new RuntimeException("unable to get extra heightsF");
		}
	}
}