package com.pirateseas.model.canvasmodel.ui;

import com.pirateseas.R;
import com.pirateseas.controller.androidGameAPI.Player;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
* @source: http://developer.android.com/reference/android/view/ViewGroup.html
* @see: http://stackoverflow.com/questions/4328838/create-a-custom-view-by-inflating-a-layout
*/
public class UIAdvance extends ViewGroup {

	private View uiAdvanceView;
	
	private ImageView imgIslandLeft;
	private ImageView imgIslandFront;
	private ImageView imgIslandRight;
	
	private ImageButton btnLeft;
	private ImageButton btnFront;
	private ImageButton btnRight;
	
	/** The amount of space used by children in the left gutter. */
    private int mLeftWidth;

    /** The amount of space used by children in the right gutter. */
    private int mRightWidth;

    /** These are used for computing child frames based on their gravity. */
    private final Rect mTmpContainerRect = new Rect();
    private final Rect mTmpChildRect = new Rect();
    
    public UIAdvance(Context context) {
		this(context, null);
	}
    
    public UIAdvance(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
    
    public UIAdvance(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		// Inflate layer view
		LayoutInflater inflater = LayoutInflater.from(context);
		uiAdvanceView = inflater.inflate(R.layout.advance_screen_layout, this, true);
		
		init(0);
	}
	
	public UIAdvance(Context context, Player player, int islandSide){
		this(context);
		
		init(islandSide);
	}
	
	private void init(int islandSide) {
		imgIslandLeft = (ImageView) findViewById(R.id.imgIslandLeft);
		imgIslandFront = (ImageView) findViewById(R.id.imgIslandFront);
		imgIslandRight = (ImageView) findViewById(R.id.imgIslandRight);
		
		switch (islandSide) {
		case 1:
			imgIslandLeft.setVisibility(View.VISIBLE);
			imgIslandFront.setVisibility(View.INVISIBLE);
			imgIslandRight.setVisibility(View.INVISIBLE);
			break;
		case 2:
			imgIslandLeft.setVisibility(View.INVISIBLE);
			imgIslandFront.setVisibility(View.VISIBLE);
			imgIslandRight.setVisibility(View.INVISIBLE);
			break;
		case 3:
			imgIslandLeft.setVisibility(View.INVISIBLE);
			imgIslandFront.setVisibility(View.INVISIBLE);
			imgIslandRight.setVisibility(View.VISIBLE);
			break;
		default:
			imgIslandLeft.setVisibility(View.INVISIBLE);
			imgIslandFront.setVisibility(View.INVISIBLE);
			imgIslandRight.setVisibility(View.INVISIBLE);
			break;
		}
		
		btnLeft = (ImageButton) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btnFront = (ImageButton) findViewById(R.id.btnFront);
		btnFront.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btnRight = (ImageButton) findViewById(R.id.btnRight);
		btnRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
     * Ask all children to measure themselves and compute the measurement of this
     * layout based on the children.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        // These keep track of the space we are using on the left and right for
        // views positioned there; we need member variables so we can also use
        // these for layout later.
        mLeftWidth = 0;
        mRightWidth = 0;

        // Measurement will ultimately be computing these values.
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        // Iterate through all children, measuring them and computing our dimensions
        // from their size.
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                // Measure the child.
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

                // Update our size information based on the layout params.  Children
                // that asked to be positioned on the left or right go in those gutters.
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.position == LayoutParams.POSITION_LEFT) {
                    mLeftWidth += Math.max(maxWidth,
                            child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                } else if (lp.position == LayoutParams.POSITION_RIGHT) {
                    mRightWidth += Math.max(maxWidth,
                            child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                } else {
                    maxWidth = Math.max(maxWidth,
                            child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                }
                maxHeight = Math.max(maxHeight,
                        child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }

        // Total width is the maximum width of all inner children plus the gutters.
        maxWidth += mLeftWidth + mRightWidth;

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        // Report our final dimensions.
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    /**
     * Position all children within this layout.
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();

        // These are the far left and right edges in which we are performing layout.
        int leftPos = getPaddingLeft();
        int rightPos = right - left - getPaddingRight();

        // This is the middle region inside of the gutter.
        final int middleLeft = leftPos + mLeftWidth;
        final int middleRight = rightPos - mRightWidth;

        // These are the top and bottom edges in which we are performing layout.
        final int parentTop = getPaddingTop();
        final int parentBottom = bottom - top - getPaddingBottom();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();

                // Compute the frame in which we are placing this child.
                if (lp.position == LayoutParams.POSITION_LEFT) {
                    mTmpContainerRect.left = leftPos + lp.leftMargin;
                    mTmpContainerRect.right = leftPos + width + lp.rightMargin;
                    leftPos = mTmpContainerRect.right;
                } else if (lp.position == LayoutParams.POSITION_RIGHT) {
                    mTmpContainerRect.right = rightPos - lp.rightMargin;
                    mTmpContainerRect.left = rightPos - width - lp.leftMargin;
                    rightPos = mTmpContainerRect.left;
                } else {
                    mTmpContainerRect.left = middleLeft + lp.leftMargin;
                    mTmpContainerRect.right = middleRight - lp.rightMargin;
                }
                mTmpContainerRect.top = parentTop + lp.topMargin;
                mTmpContainerRect.bottom = parentBottom - lp.bottomMargin;

                // Use the child's gravity and size to determine its final
                // frame within its container.
                Gravity.apply(lp.gravity, width, height, mTmpContainerRect, mTmpChildRect);

                // Place the child.
                child.layout(mTmpChildRect.left, mTmpChildRect.top,
                        mTmpChildRect.right, mTmpChildRect.bottom);
            }
        }
    }
    
    public static class LayoutParams extends MarginLayoutParams {
        /**
         * The gravity to apply with the View to which these layout parameters
         * are associated.
         */
        public int gravity = Gravity.TOP | Gravity.START;

        public static int POSITION_MIDDLE = 0;
        public static int POSITION_LEFT = 1;
        public static int POSITION_RIGHT = 2;

        public int position = POSITION_MIDDLE;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            // Pull the layout param values from the layout XML during
            // inflation.  This is not needed if you don't care about
            // changing the layout behavior in XML.
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
	
	@Override
	public void onDraw(Canvas canvas){
		uiAdvanceView.draw(canvas);
	}
	
}