package org.lybf.custom.text;
import android.graphics.Paint;
import java.util.stream.IntStream;

public class Line implements CharSequence {

    private StringBuffer raw;


    public Line() {
        raw = new StringBuffer("");
    }


    public Line(String raw) {
        this.raw = new StringBuffer(raw);
    }



    public float measureWidth(Paint paint) {
        return paint.measureText(raw.toString());
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

    
    public void append(String text) {
        raw.append(text);
    }

    public Line insert(int index, String text) {
        //raw.insert(index, StringBuffer);
        return this;
    }
    public Line replace(int start, int end, String text) {
        raw.replace(start, end, text);
        return this;
    }
    public Line delete(int index) {
        return delete(index, index);
    }
    public Line delete(int start, int end) {
        raw.delete(start, end);
        return this;
    }
    public int indexOf(String str) {
        return raw.indexOf(str);
    }

    public int indexOf(String str, int fromIndex) {
        return raw.indexOf(str, fromIndex);
    }

    public int lastIndexOf(String str) {
        return raw.lastIndexOf(str);
    }

    public int lastIndexOf(String str, int fromIndex) {
        return raw.lastIndexOf(str, fromIndex);
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
