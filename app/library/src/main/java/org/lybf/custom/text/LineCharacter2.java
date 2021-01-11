package org.lybf.custom.text;
import android.graphics.Paint;
import java.util.stream.IntStream;


public class LineCharacter2 implements CharSequence {

	private char[] raw;

	public LineCharacter2() {
		raw = new char[0];
	}


	public LineCharacter2(String string) {
		this.raw = new char[string.length()];
		string.getChars(0, string.length(), this.raw, 0);

	}
	public LineCharacter2(char[] chars) {
		this.raw = new char[chars.length];
		System.arraycopy(chars, 0, this.raw, 0, chars.length);

	}
	public float measureWidth(Paint paint) {
		return paint.measureText(raw.toString());
	}

	@Override
	public int length() {
		return raw.length;
	}

	@Override
	public char charAt(int index) {
		return raw[index];
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return raw.toString().subSequence(start, end);
	}

	public LineCharacter2 replace(int start, int end, String string) {
		int newLen =start + (end - start) + string.length();
		char[] temp = new char[raw.length];
		if (raw.length < newLen) {
			System.arraycopy(raw, 0, temp, 0, raw.length);
			//Copy chars
			raw = new char[newLen];
			System.arraycopy(temp, 0, raw, 0, start);
			string.getChars(0, string.length(), raw, start);
			System.arraycopy(temp, end, raw, end, temp.length - end);
		}

		return this;
	}

	@Override
	public String toString() {
		return raw.toString();
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
