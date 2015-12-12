package in.icho.ui.cells;

import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import in.icho.R;
import in.icho.utils.Utilities;

public class DrawerProfileCell extends FrameLayout {

	private TextView nameTextView;
	private TextView regTextView;

	public DrawerProfileCell(Context context) {
		super(context);
		// setBackgroundResource(R.drawable.profile_cell_back);
		LayoutParams layoutParams;
		nameTextView = new TextView(context);
		nameTextView.setTextColor(context.getResources().getColor(
				R.color.primary));
		nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
		nameTextView.setTypeface(Utilities.getTypeface("fonts/rmedium.ttf"));
		nameTextView.setLines(1);
		nameTextView.setMaxLines(1);
		nameTextView.setSingleLine(true);
		nameTextView.setGravity(Gravity.LEFT);
		addView(nameTextView);
		layoutParams = (FrameLayout.LayoutParams) nameTextView
				.getLayoutParams();
		layoutParams.width = LayoutParams.MATCH_PARENT;
		layoutParams.height = LayoutParams.WRAP_CONTENT;
		layoutParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
		layoutParams.leftMargin = Utilities.dp(16);
		layoutParams.bottomMargin = Utilities.dp(28);
		layoutParams.rightMargin = Utilities.dp(16);
		nameTextView.setLayoutParams(layoutParams);

		regTextView = new TextView(context);
		regTextView.setTextColor(context.getResources().getColor(
				R.color.primary));
		regTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
		regTextView.setLines(1);
		regTextView.setMaxLines(1);
		regTextView.setSingleLine(true);
		regTextView.setGravity(Gravity.LEFT);
		addView(regTextView);
		layoutParams = (FrameLayout.LayoutParams) regTextView.getLayoutParams();
		layoutParams.width = LayoutParams.MATCH_PARENT;
		layoutParams.height = LayoutParams.WRAP_CONTENT;
		layoutParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
		layoutParams.leftMargin = Utilities.dp(16);
		layoutParams.bottomMargin = Utilities.dp(9);
		layoutParams.rightMargin = Utilities.dp(16);
		regTextView.setLayoutParams(layoutParams);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (Build.VERSION.SDK_INT >= 21) {
			super.onMeasure(
					widthMeasureSpec,
					MeasureSpec.makeMeasureSpec(Utilities.dp(148)
							+ Utilities.statusBarHeight, MeasureSpec.EXACTLY));
		} else {
			super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(
					Utilities.dp(148), MeasureSpec.EXACTLY));
		}
	}

	public void setUser() {// UserObj user) {
		// if (user == null) {
		// return;
		// }
		// // get name here
		// nameTextView.setText(ApplicationLoader.store.getUsername());
		// regTextView.setText(user.getRegNo());
	}
}
