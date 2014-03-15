package com.lowagie.text.pdf;

import java.io.IOException;
import java.io.PrintStream;

import com.lowagie.text.DocumentException;

public class FontMetrics extends TrueTypeFontUnicode {

	public static void main(String[] args) throws Exception {
		// 使いたいフォント。ttc の場合は foo.ttc,0 のようにインデックス指定する。
		// for (String fontPath : new String[] {
		// FontMetrics.class.getResource("msgothic.ttc").toString() + ",0",
		// FontMetrics.class.getResource("msgothic.ttc").toString() + ",1",
		// FontMetrics.class.getResource("msmincho.ttc").toString() + ",0",
		// FontMetrics.class.getResource("msmincho.ttc").toString() + ",1"

		for (String fontPath : new String[] {
				FontMetrics.class.getResource("simhei.ttf").toString(),
				FontMetrics.class.getResource("simsun.ttc").toString() + ",0",
				FontMetrics.class.getResource("simsun.ttc").toString() + ",1"

		}) {
			FontMetrics fm = new FontMetrics(fontPath);
			// プロパティファイルの出力先
			String dir = "tools/com/lowagie/text/pdf/fonts/";
			PrintStream out = new PrintStream(dir + fm.getPostscriptFontName()
					+ ".properties");
			fm.dump(out);
			out.close();
		}
	}

	public FontMetrics(String ttFile) throws DocumentException, IOException {
		super(ttFile, "Identity-H", true, null, true);
	}

	public void dump(PrintStream out) throws DocumentException {
		// CJKFont cjkFont = new CJKFont("HeiseiKakuGo-W5", "UniJIS-UCS2-H",
		// false);
		CJKFont cjkFont = new CJKFont("STSong-Light", "UniGB-UCS2-H", false);
		System.out.println(getPostscriptFontName() + ".properties");

		out.println("Flags=" + getFlags());
		out.println("FontBBox=[" + getFontDescriptor(BBOXLLX) + " "
				+ getFontDescriptor(BBOXLLY) + " " + getFontDescriptor(BBOXURX)
				+ " " + getFontDescriptor(BBOXURY) + "]");
		out.println("ItalicAngle=" + getFontDescriptor(ITALICANGLE));
		out.println("Ascent=" + getFontDescriptor(ASCENT));
		out.println("Descent=" + getFontDescriptor(DESCENT));
		out.println("CapHeight=" + getFontDescriptor(CAPHEIGHT));
		out.println("StemV=80");
		out.println("Registry=Adobe");
		//out.println("Ordering=Japan1");
		out.println("Ordering=GB1");
		out.println("Supplement=4");

		out.print("W=");
		for (int i = 0; i < 0xffff; ++i) {
			int width = getWidth(i);
			int cid = cjkFont.getCidCode(i);
			if (cid != 0 && width != 0 && width != 1000) {
				out.print("" + cid + " " + width + " ");
			}
		}
		out.println();
		out.println("W2=");
	}

	public int getFontDescriptor(int key) {
		return (int) getFontDescriptor(key, 1000);
	}

	public int getFlags() {
		int flags = 0;
		if (isFixedPitch)
			flags |= 1;
		flags |= fontSpecific ? 4 : 32;
		if ((head.macStyle & 2) != 0)
			flags |= 64;
		if ((head.macStyle & 1) != 0)
			flags |= 262144;
		return flags;
	}
}