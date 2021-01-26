package org.lybf.custom.views;
import android.content.Context;

public class EditView extends TextView{
    private final Context context;

	public EditView(Context context) {
		super(context);
		this.context = context;
		canEdit(true);
	}
    
    
}
