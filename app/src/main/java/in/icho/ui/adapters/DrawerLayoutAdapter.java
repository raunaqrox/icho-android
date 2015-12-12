package in.icho.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import in.icho.ui.cells.DividerCell;
import in.icho.ui.cells.DrawerActionCell;
import in.icho.ui.cells.DrawerProfileCell;
import in.icho.ui.cells.EmptyCell;

public class DrawerLayoutAdapter extends BaseAdapter {

	private Context mContext;

	public DrawerLayoutAdapter(Context context) {
		mContext = context;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int i) {
		return !(i == 0 || i == 1 || i == 4 || i == 6);
	}

	@Override
	public int getCount() {
		return 9;
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		int type = getItemViewType(i);
		System.out.println(i + "|" + type);
		if (type == 0) {
			if (view == null) {
				view = new DrawerProfileCell(mContext);
			}
			// ((DrawerProfileCell) view).setUser(new Store(
			// ApplicationLoader.applicationContext).getUser());

		} else if (type == 1) {
			if (view == null) {
				view = new EmptyCell(mContext, 8);
			}
		} else if (type == 2) {
			if (view == null) {
				view = new DividerCell(mContext);
			}
		} else if (type == 3) {
			if (view == null) {
				view = new DrawerActionCell(mContext);
			}
			DrawerActionCell actionCell = (DrawerActionCell) view;
			if (i == 2) {
				// actionCell.setTextAndIcon("Files",
				// R.drawable.ic_home_folder);
			} else if (i == 3) {
				// actionCell.setTextAndIcon("Downloads",
				// R.drawable.ic_downloads);
			} else if (i == 5) {
				// actionCell.setTextAndIcon("WEBSIS", R.drawable.ic_att);
			} else if (i == 7) {
				// actionCell.setTextAndIcon("Settings",
				// R.drawable.ic_settings);
			} else if (i == 8) {
				// actionCell.setTextAndIcon("About", R.drawable.ic_about);
			}
		}

		return view;
	}

	@Override
	public int getItemViewType(int i) {
		if (i == 0) {
			return 0;
		} else if (i == 1) {
			return 1;
		} else if (i == 4 || i == 6) {
			return 2;
		}
		return 3;
	}

	@Override
	public int getViewTypeCount() {
		return 4;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
}