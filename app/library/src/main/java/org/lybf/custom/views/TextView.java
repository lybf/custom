package org.lybf.custom.views;
import android.annotation.NonNull;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Scroller;
import java.util.ArrayList;
import org.lybf.custom.R;
import org.lybf.custom.annation.Param;
import org.lybf.custom.text.LineCharacter;
import org.lybf.custom.text.LineCharacterEditable;

/*
 *  @Author :lybf
 *  @Date : 2020-11-18
 *  灵感来自于ListView的加载原理
 *  通过只加载当前可视范围的内容，大大提高效率
 *
 *   nameSpace :
 *      xmlns:tv="http://schemas.android.com/apk/res-auto/auto"
 *
 *   xml.
 *     <org.lybf.costum.view.TextView
 *       android:layout_width="match_parent"
 *       android:layout_height="match_parent"
 *       tv:text="text"/>
 *
 *
 *
 ****************Attrs*******************************************
 *-----Name---------------------------------------type-----------
 *  @attr name org.lybf.costum:lineNumbColor  <color>
 *  @attr name org.lybf.costum:lineNumber     <boolean>
 *  @attr name org.lybf.costum:scrollable     <boolean>
 *  @attr name org.lybf.costum:text           <string>
 *  @attr name org.lybf.costum:textColor      <color>
 *  @attr name org.lybf.costum:textSize       <int>
 *  @attr name org.lybf.costum:wordWarp       <boolean>
 ***************************************************************
 */
public class TextView extends View {
	private static final float default_textSize = 30;

	public static final int default_backgroundColor = 0xFF2B2B2B;

	public static final int default_backgroundColor2 = 0xFF373737;

	public static final int default_textColor = 0xFFFFD740;

	public static final int default_lineNumberColor = 0xFF23BBFF;

	private final Context mContext;

	private LineCharacterEditable mEditable = new LineCharacterEditable();



	private OnScrollChangedListener onScrollChangedListener;

	private Paint mPaint = new Paint();
	private Paint mTextPaint = new Paint();

	private float textSize = default_textSize;

	//colors
	private int textColor = default_textColor;
	private int lineNumberColor = default_lineNumberColor;
	private int backgroundColor = default_backgroundColor;
	private int backgroundColor2 = default_backgroundColor2;


	private float ScrollX = 0,ScrollY = 0;
	private float lastScrollX = 0,lastScrollY = 0;
	private float lastDownX,lastDownY;
	private float lastDownRawX,lastDownRawY;


	private float lineNumberWidth;
	private float FontHeight;

	private int maxDisplayLine;

	private boolean scrollable = true;

	private boolean wordWarp;

	private boolean showLineNumber = true;

	private Direction slideMode;

	private boolean lockSlide = false;

	private long lastTouchTime;

	private VelocityTracker mVelocityTracker;

	private Direction direction = Direction.UP;

	private int mTouchSlop;

	private int mMinimumVelocity;

	private int mMaximumVelocity;

	


	public void setScrollable(boolean scrollable) {
		this.scrollable = scrollable;
	}

	public boolean isScrollable() {
		return scrollable;
	}





	public enum Direction {
		VERTICAL,
		ORIZONTAL,
		UP,
		LEFT,RIGHT,
		DOWN
		}

	/*
	 *
	 *@param context
	 */
    public TextView(@NonNull Context context) {
		this(context, null);
	}
    public TextView(@NonNull Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
    public TextView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}
    public TextView(@NonNull Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		this.mEditable = new LineCharacterEditable();
		this.mContext = context;
		try {
			TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.textView, defStyleAttr, defStyleRes);
			this.textSize = typeArray.getDimension(R.attr.textSize, default_textSize);
			this.textColor = typeArray.getColor(R.attr.textColor, default_textColor);
			this.lineNumberColor = typeArray.getColor(R.attr.lineNumbColor, default_lineNumberColor);
			this.mEditable.setText("" + typeArray.getString(R.attr.text));
			this.scrollable = typeArray.getBoolean(R.attr.scrollable, false);
			this.wordWarp = typeArray.getBoolean(R.attr.wordWarp, false);

		} catch (Exception e) {
			e.printStackTrace();
		}
		this.mTextPaint.setTextSize(textSize);
		this.mTextPaint.setColor(textColor);
		this.mTextPaint.setStrokeWidth(5);
		this.mPaint.setStrokeWidth(5);
		this.mPaint.setTextSize(textSize);
		this.mPaint.setColor(lineNumberColor);
		this.mEditable.setPaint(mTextPaint);
		this.maxDisplayLine = (int) (this.getHeight() / this.getFontHeight());
		setBackgroundColor(backgroundColor);
		initScrollView();

	}

	private void initScrollView() {
		final ViewConfiguration configuration = ViewConfiguration.get(mContext);
		this.mTouchSlop = configuration.getScaledTouchSlop();
		this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
		this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
	}



	public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
		this.onScrollChangedListener = onScrollChangedListener;
	}


	public void showLineNumber(boolean showLineNumber) {
		this.showLineNumber = showLineNumber;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
		this.mEditable.refeshWidth();
		this.FontHeight = getFontHeight();
	}
	public float getTextSize(){
		return textSize;
	}
	
	public void setBackgroundColor2(int backgroundColor2) {
		this.backgroundColor2 = backgroundColor2;
	}

	public int getBackgroundColor2() {
		return backgroundColor2;
	}

	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	public void setLineNumberColor(int lineNumberColor) {
		this.lineNumberColor = lineNumberColor;
	}

	public int getLineNumberColor() {
		return lineNumberColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public int getTextColor() {
		return textColor;
	}
	
	private int getMaxDisplayLine() {
		this.maxDisplayLine = (int) Math.ceil((this.getHeight() / this.getFontHeight()));
		return maxDisplayLine;
	}

	private float getFontHeight() {
	    Paint.FontMetrics fontMetrics =	mTextPaint.getFontMetrics();
		this.FontHeight = fontMetrics.descent - fontMetrics.ascent;
		return FontHeight;
	}

	public float getTextWidth() {
		return this.mEditable.getTextWidth();
	}

	public float getTextHeight() {
		return this.mEditable.getLineCount() * getFontHeight();
	}


	public LineCharacterEditable getText() {
		return this.mEditable;
	}

	public void setText(@Param(Name="String")String string) {
		this.scrollTo(0, 0);
		this.lastScrollX = 0;
		this.lastScrollY = 0;
		this.mEditable.clear();
		this.mEditable.append(string);
		invalidate();
	}

	public void append(@Param(Name="String")String string) {
		this.mEditable.append(string);
		if (getLastLine() < mEditable.getLineCount())
			invalidate();
	}

	public void loadText(String filePath, ITextViewListener listener) {

	}


	private float lastX,lastY,lastX2,lastY2;
	public void analysisTouchEvent(MotionEvent event) {
		int action = event.getActionMasked();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				lastX = event.getX(0);
				lastY = event.getY(0);
				lastX2 = event.getX(1);
				lastY2 = event.getY(1);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		initVelocityTrackerIfNotExists();
		this.mVelocityTracker.addMovement(event);
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				this.lastDownX = event.getX();
				this.lastDownY = event.getY();
				this.lastDownRawX = event.getRawX();
				this.lastDownRawY = event.getRawY();
				this.lastScrollX = ScrollX;
				this.lastScrollY = ScrollY;
				this.lastTouchTime = System.currentTimeMillis();
				this.lockSlide = false;
				break;

			case MotionEvent.ACTION_MOVE:

				mVelocityTracker.computeCurrentVelocity(1000);
				float vX = mVelocityTracker.getXVelocity();
				float vY = mVelocityTracker.getYVelocity();
				float mx = lastDownRawX - event.getRawX() ;
				float my = lastDownRawY - event.getRawY() ;
				if (!lockSlide) {
					if (Math.abs(vY) * 3 > Math.abs(vX)) {
						this.slideMode = Direction.VERTICAL;
					} else {
						this.slideMode = Direction.ORIZONTAL;
					}
					this.lockSlide = true;
				}
				if (lockSlide && scrollable) {
					if (slideMode == Direction.ORIZONTAL) {
						scrollTo(lastScrollX + mx, lastScrollY);
					} else if (slideMode == Direction.VERTICAL) {
						scrollTo(lastScrollX, lastScrollY + my);
					}
				}
				break;

			case MotionEvent.ACTION_UP:
				this.mVelocityTracker.computeCurrentVelocity(1000);
				float vX2 = mVelocityTracker.getXVelocity();
				float vY2 = mVelocityTracker.getYVelocity();
				//判断是否滑动
				if ((Math.abs(vX2) > mMinimumVelocity && Math.abs(vY2) > mMinimumVelocity)) {
					if (lockSlide && scrollable) {
						if (slideMode == Direction.VERTICAL) {
							if ((vY2 < 0))
								this.direction = Direction.UP;
							else direction = Direction.DOWN;
						} else if (slideMode == Direction.ORIZONTAL) {
							if ((vX2 < 0))
								this.direction = Direction.LEFT;
							else direction = Direction.RIGHT;
						}
					}
				}
				recycleVelocityTracker();
				break;
		}
		return true;
	}


	private void initVelocityTrackerIfNotExists() {
		if (mVelocityTracker == null) {
			this.mVelocityTracker = VelocityTracker.obtain();
			this.mVelocityTracker.computeCurrentVelocity(1000);
		}
	}

	private void recycleVelocityTracker() {
		if (mVelocityTracker != null) {
			this.mVelocityTracker.clear();
			this.mVelocityTracker.recycle();
			this.mVelocityTracker = null;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawLineNumber(canvas);
		drawText(canvas);
	}


	private void drawLineNumber(Canvas canvas) {
		if (!showLineNumber)return;
		this.lineNumberWidth = - mPaint.measureText("" + mEditable.getLineCount()) + 5;
		Paint p = new Paint();
		p.setColor(backgroundColor2);
		canvas.drawRect(-ScrollX, 0, -(lineNumberWidth + ScrollX), getHeight(), p);
		int line = getFirstLine();
		float offset = -ScrollY % getFontHeight();
		float y = 0;
		for (int i = line ; i < line + getMaxDisplayLine() + 2; i++) {
			if (i > mEditable.getLineCount())break;
			canvas.drawText("" + (i), -ScrollX, y * getFontHeight() + offset , mPaint);
			y ++;
		}
		canvas.save();
	}

	private void drawText(Canvas canvas) {
		int first = getFirstLine();
		int last = getLastLine();
		ArrayList<LineCharacter> lines =  mEditable.getLines(first, last + 2);
		float offset = -ScrollY % FontHeight;
		int curr = 0;
		for (LineCharacter line : lines) {
			canvas.drawText(line.toString(), 
							/* X */-(lineNumberWidth + ScrollX), 
							/*  Y */curr * getFontHeight() + offset,
							/* Paint */mTextPaint);
			curr++;
		}
		canvas.save();
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		Parcelable superData = super.onSaveInstanceState();
		bundle.putParcelable("superData", superData);
		bundle.putString("txt", mEditable.toString());
		bundle.putInt("backgroundColor", backgroundColor);
		bundle.putInt("backgroundColor2", backgroundColor2);
		bundle.putFloat("textSize", textSize);
		bundle.putFloat("FontHeight", FontHeight);
		bundle.putInt("lineNumberColor", lineNumberColor);
		bundle.putFloat("lineNumberWidth", lineNumberWidth);
		bundle.putFloat("textWidth", mEditable.getTextWidth());
		bundle.putFloat("ScrollX", ScrollX);
		bundle.putFloat("ScrollY", ScrollY);
		bundle.putFloat("lastScrollX", lastScrollX);
		bundle.putFloat("lastScrollY", lastScrollY);
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		Bundle bundle = (Bundle)state;
		Parcelable superData = bundle.getParcelable("superData");
		String string = bundle.getString("txt", mEditable.toString());
		this.mEditable.setText(string);
		this.backgroundColor = bundle.getInt("backgroundColor", backgroundColor);
		this.backgroundColor2 = bundle.getInt("backgroundColor2", backgroundColor2);
		this.textSize = bundle.getFloat("textSize", textSize);
		this.FontHeight = bundle.getFloat("FontHeight", FontHeight);
		this.lineNumberColor = bundle.getInt("lineNumberColor", lineNumberColor);
		this.lineNumberWidth = bundle.getFloat("lineNumberWidth", lineNumberWidth);
		this.mEditable.setTextWidth(bundle.getFloat("textWidth", mEditable.getTextWidth()));
		this.ScrollX = (int) bundle.getFloat("ScrollX", ScrollX);
		this.ScrollY = (int) bundle.getFloat("ScrollY", ScrollY);
		this.lastScrollX =  bundle.getFloat("lastScrollX", lastScrollX);
		this.lastScrollY = bundle.getFloat("lastScrollY", lastScrollY);
		invalidate();
		super.onRestoreInstanceState(superData);
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		maxDisplayLine = (int)(getHeight() / getFontHeight());
		invalidate();
	}


	@Override
	public void scrollTo(int x, int y) {
		scrollTo((float)x, (float)y);
	}
	public void scrollTo(float x, float y) {
		this.ScrollX = x;
		this.ScrollY = y;
		if (onScrollChangedListener != null) {
			onScrollChangedListener.onScroll(this, (int) x, (int)y, calculateLinePosition(ScrollY));
		}
		checkScroll();
	}


	@Override
	public void scrollBy(int x, int y) {
		scrollBy((float)x, y);
	}
	public void scrollBy(float offsetX, float offsetY) {
		int mScrollX = (int) (ScrollX + offsetX);
		int mScrollY = (int) (ScrollY + offsetY);
		if (onScrollChangedListener != null) {
			onScrollChangedListener.onScroll(this, mScrollX, mScrollY, calculateLinePosition(ScrollY));
		}
		checkScroll();
	}

	public void scrollToLine(@Param(Name="Line")int line) {
		if (line <= 0)
			this.scrollTo(ScrollX, 0);
		if (line < mEditable.getLineCount())
			this.scrollTo(ScrollX, calculatePosition(line));
		else if (line > mEditable.getLineCount())
			this.scrollTo(ScrollX, calculatePosition(mEditable.getLineCount()));
	}

	private void checkScroll() {
		if (ScrollX < 0) {
			this.ScrollX = 0;
		}
		if (ScrollX > (mEditable.getTextWidth() + 10)) {
			this.ScrollX = mEditable.getTextWidth() + 10;
		}
		if (ScrollY < 0) {
			this.ScrollY = 0;
		}
		if (ScrollY > mEditable.getLineCount() * FontHeight + 10) {
			this.ScrollY = mEditable.getLineCount() * FontHeight + 10;
		}
		invalidate();
	}

	public int getFirstLine() {
		return calculateLinePosition(ScrollY);
	}

	public int getLastLine() {
		return calculateLinePosition((ScrollY + getHeight()));
	}


	private int calculateLinePosition(float y) {
		if (y <= 0) {
			return 0;
		} else if ((y) / getFontHeight() < mEditable.getLineCount()) {
			int pos = (int)(y / FontHeight);
			return pos;
		} else {
			return this.mEditable.getLineCount();
		}
	}

	public float calculatePosition(int line) {
		return line * getFontHeight();
	}

	/*
	 * Developing
	 */
	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {

		return super.onCreateInputConnection(outAttrs);
	}

	public class IInputConnection extends BaseInputConnection {
		private TextView view;
		public IInputConnection(TextView view , boolean fullEditor) {
			super(view, fullEditor);
			this.view = view;
		}


		public android.text.Editable getEditable() {
			return Editable.Factory.getInstance().newEditable(view.getText().toString());
		}



	}


}
