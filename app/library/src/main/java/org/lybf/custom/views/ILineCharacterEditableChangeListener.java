package org.lybf.custom.views;

public interface ILineCharacterEditableChangeListener {
    public void append(String string,int start,int end);
    public void delete(int start,int end);
    public void onWidthRefresh(float width)
    
}
