package in.icho.ui.cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import in.icho.utils.Utilities;

public class DividerCell extends BaseCell {

	private static Paint paint;

	public DividerCell(Context context) {
		super(context);
		if (paint == null) {
			paint = new Paint();
			paint.setColor(0xffd9d9d9);
			paint.setStrokeWidth(1);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
				Utilities.dp(16) + 1);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawLine(getPaddingLeft(), Utilities.dp(8), getWidth()
				- getPaddingRight(), Utilities.dp(8), paint);
	}
}
