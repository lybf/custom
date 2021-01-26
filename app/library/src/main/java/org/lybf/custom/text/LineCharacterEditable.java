package org.lybf.custom.text;

import android.graphics.Paint;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class LineCharacterEditable implements CharSequence {




	private ArrayList<LineCharacter> lines = new ArrayList<LineCharacter>();

    
	private Paint mPaint;

	private float maxWidth;

	private Position maxWidthPos;

    private int lastModifiedHashCode;

	public LineCharacterEditable() {

	}

	public LineCharacterEditable(String string) {
		append(string);
	}

	public LineCharacterEditable(LineCharacterEditable line) {
		this.lines = line.lines;
		this.mPaint = line.mPaint;
		this.maxWidth = line.maxWidth;
		this.maxWidthPos = line.maxWidthPos;
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



	public LineCharacterEditable append(String text) {
        if (text == null) {
            lines.add(new LineCharacter("null"));
            return this;
        }

        String[] strs = text.split("\n");//System.lineSeparator());
        // if (strs.length > 1) {
        for (String s : strs) {
            LineCharacter line = new LineCharacter(s);
            float w = line.measureWidth(mPaint);
            if (maxWidth < w) {
                maxWidth = w;
            }
            lines.add(line);
        }
        /*} else {
         int len = getLineCount();
         LineCharacter lastLine ;
         if (len > 0)lastLine = getLine(lines.size() - 1);
         else lastLine = new LineCharacter("");
         lastLine.append(text);
         lines.add(lastLine);

         }*/
        return this;
	}

    public LineCharacterEditable newLine(){
        lines.add(new LineCharacter());
        return this;
    }


	public int getLineCount() {
		return lines.size();
	}

	public void refeshWidth() {
		initWidth();
	}

	private void initWidth() {    
		if (mPaint == null)return;
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


	public LineCharacterEditable replace(int position, String string) {
		return replace(position, position, string);
	}

	public LineCharacterEditable replace(int start, int end, String string) {
		Position pos = getPosition(start);
		Position pos2 = getPosition(end);
		String[] strs = string.split(System.lineSeparator());
		if (pos.line == pos2.line) {
			if (strs.length > 0) {

			}
		} else {

		}
		return this;
	}

	public LineCharacterEditable clear() {
		lines.clear();
		maxWidth = 0;
		maxWidthPos = null;
		return this;
	}

    private void p() {
        lastModifiedHashCode = lines.hashCode();
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

	private boolean hasLineSeparator(String string) {
		return string.contains(System.lineSeparator());
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
        /*
         int len = length();
         if (index > 0)return new Position(getLineCount(), len);
         */
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
