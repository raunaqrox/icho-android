package in.icho.ui.cells;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import in.icho.utils.Utilities;

public class DrawerActionCell extends FrameLayout {

	private TextView textView;

	public DrawerActionCell(Context context) {
		super(context);
		textView = new TextView(context);
		textView.setTextColor(0xffffffff);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
		textView.setTypeface(Utilities.getTypeface("fonts/rmedium.ttf"));
		textView.setLines(1);
		textView.setMaxLines(1);
		textView.setSingleLine(true);
		textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		textView.setCompoundDrawablePadding(Utilities.dp(34));
		addView(textView);
		LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams();
		layoutParams.width = LayoutParams.MATCH_PARENT;
		layoutParams.height = LayoutParams.MATCH_PARENT;
		layoutParams.gravity = Gravity.LEFT;
		layoutParams.leftMargin = Utilities.dp(14);
		layoutParams.rightMargin = Utilities.dp(16);
		textView.setLayoutParams(layoutParams);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(
				Utilities.dp(48), MeasureSpec.EXACTLY));
	}

	public void setTextAndIcon(String text, int resId) {
		textView.setText(text);
		textView.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
	}
}
