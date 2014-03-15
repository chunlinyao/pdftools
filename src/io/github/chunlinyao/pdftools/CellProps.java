package io.github.chunlinyao.pdftools;

import java.awt.Color;

import com.lowagie.text.Font;

public class CellProps {
	public CellProps(TableBuilder tableBuilder) {
		this.font = new Font(tableBuilder.font);
		this.color = tableBuilder.color;
	}

	public CellProps(CellProps row) {
		this.font = new Font(row.font);
		this.horizontalAlignment = row.horizontalAlignment;
		this.verticalAlignment = row.verticalAlignment;
		this.noWrap = row.noWrap;
		this.paddingLeft = row.paddingLeft;
		this.paddingRight = row.paddingRight;
		this.paddingTop = row.paddingTop;
		this.paddingBottom = row.paddingBottom;
		this.backgroundColor = row.backgroundColor;
		this.color = row.color;
	}

	Font font;
	Integer verticalAlignment;
	Integer horizontalAlignment;
	Float paddingLeft;
	Float paddingRight;
	Float paddingTop;
	Float paddingBottom;
	Boolean noWrap;
	Color backgroundColor;
	Color color;

}
