package org.lybf.custom.views;
import org.lybf.custom.lang.CustomException;

public class BaseListener implements ITextViewListener ,ILineCharacterEditableChangeListener,
OnScrollChangedListener,TextChangeListener,ILoadTextListener {

    @Override
    public void success(TextView view) {
    }

    @Override
    public void failed(String msg, Exception e) {
    }
    

    @Override
    public void append(String string, int start, int end) {
    }

    @Override
    public void delete(int start, int end) {
    }

    @Override
    public void onWidthRefresh(float width) {
    }

    @Override
    public void onScroll(TextView view, int x, int y, int line) {
    }
    

    @Override
    public void done(TextView view, CustomException error) {
    }




}
