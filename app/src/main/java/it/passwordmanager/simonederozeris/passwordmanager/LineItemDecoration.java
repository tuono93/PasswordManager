package it.passwordmanager.simonederozeris.passwordmanager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Example of custom ItemDecoration
 */
public class LineItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * The default value for the line width
     */
    private static final float DEFAULT_LINE_WIDTH = 2.0f;

    /**
     * The number of pixel for the separation line
     */
    private final float mLineWidth;

    /**
     * The Paint to use for the line
     */
    private final Paint mPaint;

    /**
     * Creates a LineItemDecoration for a line of default width
     */
    public LineItemDecoration() {
        this(DEFAULT_LINE_WIDTH);
    }

    /**
     * Creates a LineItemDecoration for a line of the given width
     *
     * @param lineWidth The number of pixel of the separator line
     */
    public LineItemDecoration(final float lineWidth) {
        this.mLineWidth = lineWidth;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(lineWidth);
        mPaint.setColor(Color.GRAY);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, (int) Math.floor(mLineWidth));
        outRect.bottom = 30;
        outRect.top = 30;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            c.drawLine(
                    layoutManager.getDecoratedLeft(child),
                    layoutManager.getDecoratedBottom(child),
                    layoutManager.getDecoratedRight(child),
                    layoutManager.getDecoratedBottom(child),
                    mPaint);
        }
    }
}
