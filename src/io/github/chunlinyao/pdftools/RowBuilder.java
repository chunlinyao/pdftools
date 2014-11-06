package io.github.chunlinyao.pdftools;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfContentByte;

public class RowBuilder extends CellProps implements Iterable<CellBuilder> {

	private final TableBuilder table;
	private CellBuilder currentCell;
	private final List<CellBuilder> cells = new ArrayList<>();
	float rowHeight;

	public RowBuilder(final TableBuilder tableBuilder) {
		this(tableBuilder, -1);
	}

	public RowBuilder(final TableBuilder tableBuilder, final float rowHeight) {
		super(tableBuilder);
		this.table = tableBuilder;
		this.rowHeight = rowHeight;
	}

	public CellBuilder addCell(final String text) {
		return add(new CellBuilder(this, text));
	}

	public CellBuilder add(final CellBuilder cellBuilder) {
		this.currentCell = cellBuilder;
		this.cells.add(cellBuilder);
		return currentCell();
	}

	/**
	 * @return
	 */
	public CellBuilder currentCell() {
		return this.currentCell;
	}

	public CellBuilder cellAt(final int i) {
		return cells.get(i);
	}

	@Override
	public Iterator<CellBuilder> iterator() {
		return cells.iterator();
	}

	public RowBuilder bgColor(final Color color) {
		this.backgroundColor = color;
		return this;
	}

	public RowBuilder color(final Color color) {
		this.color = color;
		return this;
	}

	public RowBuilder font(final Font font) {
		this.font = new Font(font);
		return this;
	}

	public RowBuilder verticalAlignment(final int valign) {
		this.verticalAlignment = valign;
		return this;
	}

	public RowBuilder horizontalAlignment(final int halign) {
		this.horizontalAlignment = halign;
		return this;
	}

	public RowBuilder noWrap() {
		this.noWrap = true;
		return this;
	}

	public RowBuilder wrap() {
		this.noWrap = false;
		return this;
	}

	public RowBuilder padding(final float padding) {
		return padding(padding, padding, padding, padding);
	}

	public RowBuilder padding(final float top, final float right, final float bottom, final float left) {
		paddingLeft = left;
		paddingRight = right;
		paddingTop = top;
		paddingBottom = bottom;
		return this;
	}

	TableBuilder getTable() {
		return table;
	}

	PdfContentByte getDirectContent() {
		return table.getDirectContent();
	}
}
