package org.lybf.custom.text;
import java.util.stream.IntStream;
import android.graphics.Paint;

public class LineCharacter implements CharSequence {

	private String raw;

	public LineCharacter() {
		raw = "";
	}


	public LineCharacter(String raw) {
		this.raw = raw;
	}

	public float measureWidth(Paint paint) {
		return paint.measureText(raw);
	}

	@Override
	public int length() {
		return raw.length();
	}

	@Override
	public char charAt(int index) {
		return raw.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return raw.subSequence(start, end);
	}

	public LineCharacter replace(int start, int end, String string) {
		raw = new StringBuilder("")
			.append(subSequence(0, start))
			.append(string)
			.append(subSequence(end, length()))
			.toString();
		return this;
	}

	@Override
	public String toString() {
		return raw;
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
