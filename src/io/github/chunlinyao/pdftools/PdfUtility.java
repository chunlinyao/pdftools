package io.github.chunlinyao.pdftools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.Barcode39;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;

public class PdfUtility {

	public static float mmToPt(final float mm) {
		return mm * 10.0f / 254.0f * 72.0f;
	}

	public static float translateY(final float y) {
		return PageSize.A4.getHeight() - mmToPt(y);
	}

	public static void drawLine(final PdfContentByte cb, final float x1,
			final float y1, final float x2, final float y2) {
		cb.moveTo(x1, y1);
		cb.lineTo(x2, y2);
		cb.stroke();
	}

	public static void drawDottedLine(final PdfContentByte cb, final float x1,
			final float y1, final float x2, final float y2) {
		cb.setLineDash(1, 1, 0);
		cb.moveTo(x1, y1);
		cb.lineTo(x2, y2);
		cb.stroke();
	}

	public static BaseFont getSimsun() {
		BaseFont font;
		try {
			font = BaseFont.createFont("NSimSun", "UniGB-UCS2-H",
					BaseFont.NOT_EMBEDDED);
			return font;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static BaseFont getFzHeiTi() {
		BaseFont font;
		try {
			font = BaseFont.createFont(
					PdfUtility.class.getResource("FZHTJW.TTF").toString(),
					BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			return font;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static BaseFont getFzSongTi() {
		BaseFont font;
		try {
			font = BaseFont.createFont(
					PdfUtility.class.getResource("FZSSJW.TTF").toString(),
					BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			return font;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static Barcode39 getBarcode39(final String label) {
		Barcode39 code39;
		code39 = new Barcode39();
		code39.setCode(label);
		code39.setStartStopText(false);
		return code39;
	}

	public static void copy(final InputStream is, final OutputStream os)
			throws IOException {
		int n;
		final byte[] buffer = new byte[1024];
		while ((n = is.read(buffer)) > -1) {
			os.write(buffer, 0, n);
		}
		os.close();
	}
}
