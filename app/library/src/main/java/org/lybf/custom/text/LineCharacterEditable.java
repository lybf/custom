package org.lybf.custom.text;

import android.graphics.Paint;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class LineCharacterEditable implements CharSequence {

	private ArrayList<LineCharacter> lines = new ArrayList<LineCharacter>();

	private Paint mPaint;

	private float maxWidth;

	private Position maxWidthPos;

	public LineCharacterEditable() {

	}

	public LineCharacterEditable(String string) {
		append(string);
	}

	public void setTextWidth(float width) {
		maxWidth = width;
	}

	public void setPaint(Paint paint) {
		this.mPaint = paint;
		initWidth();
	}

	public Paint getPaint() {
		return mPaint;
	}

	public void setText(String string) {
		append(string);
	}

	public LineCharacterEditable append(String string) {
		if (string == null) {
			lines.add(new LineCharacter("null"));
			return this;
		}

		String[] strs = string.split(System.lineSeparator());
		for (String s : strs) {
			LineCharacter line = new LineCharacter(s);
			float w = line.measureWidth(mPaint);
			if (maxWidth < w) {
				maxWidth = w;
			}
			lines.add(line);
		}
		return this;
	}

	public int getLineCount() {
		return lines.size();
	}

	public void refeshWidth() {
		initWidth();
	}

	private void initWidth() {    
		for (int i = 0; i < getLineCount() ; i++) {
			maxWidth = Math.max(maxWidth, getLine(i).measureWidth(mPaint));
		}
	}

	public float getTextWidth() {
		return maxWidth;
	}

	@Override
	public int length() {
		int len = -1;
		for (LineCharacter line : lines) {
			len += line.length() + 1;
		}
		return len;
	}


	public float measureWidth(LineCharacter line, Paint paint) {
		return line.measureWidth(paint);
	}

	@Override
	public char charAt(int index) {
		Position pos = getPosition(index);
		return getLine(pos.line).charAt(pos.index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		StringBuilder sb = new StringBuilder();
		Position s = getPosition(start);
		Position e = getPosition(end);

		LineCharacter firstLine = getLine(s.line);
		sb.append(firstLine.subSequence(0, s.index));
		for (int i = s.line ; i < e.line ; i++) {
			sb.append(getLine(i).toString());
		}
		LineCharacter lastLine = getLine(e.line);
		sb.append(lastLine.subSequence(e.index, lastLine.length()));

		return sb.toString();
	}


	public LineCharacterEditable insert() {
		return this;
	}


	public LineCharacterEditable replace(int position, String string) {
		return replace(position, position, string);
	}
	public LineCharacterEditable replace(int start, int end, String string) {
		Position pos = getPosition(start);
		Position pos2 = getPosition(end);
		if (pos.line == pos2.line) {

		}
		return this;
	}

	public LineCharacterEditable clear() {
		lines.clear();
		maxWidth = 0;
		maxWidthPos = null;
		return this;
	}

	public ArrayList<LineCharacter> getLines(int start, int end) {
		ArrayList<LineCharacter> lines2= new ArrayList<LineCharacter>();
		int s = start ,e = end;
		if (start < 0)s = 0;
		if (end > getLineCount())e = getLineCount();
		for (; s < e ; s++) {
			lines2.add(getLine(s));
		}
		return lines2;
	}

	public LineCharacter getLine(int index) {
		return lines.get(index);
	}

	public Position getPosition(int index) {
		int posi = -1;
		int lin = 0;
		for (LineCharacter line : lines) {
			if (posi < index) {
				posi += line.length() + 1;
			} else {
				posi -= index;
				return new Position(lin, posi);
			}
			lin++;
		}
		return new Position(-1, -1);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (LineCharacter line : lines) {
			sb.append(line.toString());
			sb.append("\n");
		}

		return sb.toString();
	}


    public static class Position {
		public int line;
		public int index;

		public Position() {}
		public Position(int line, int index) {
			this.line = line;
			this.index = index;
		}

		public void setLine(int line) {
			this.line = line;
		}
		public int getLine() {
			return line;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		public int getIndex() {
			return index;
		}
	}
    @Override
	public IntStream chars() {
		return null;
	}

	@Override
	public IntStream codePoints() {
		return null;
	}



}
