package net.kidfeng.android.iconreader;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author Kid Feng <Kid.Stargazer@gmail.com>
 * create@ Jul 7, 2010 5:59:11 PM
 */
public class Home extends Activity {
	private static final String TAG = "IconReader";
	private static final int DETAIL_DIALOG = 0;
	private static final int ABOUT_DIALOG = 1;
	private static GridAdapter mAdapter;
	private static int currentPosition = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
        initGrid();
    }
    
    private void initGrid() {
        GridView grid = (GridView)findViewById(R.id.grid);
        if(mAdapter == null) {
        	mAdapter = new GridAdapter();        	
        }
        grid.setAdapter(mAdapter);
        grid.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		currentPosition = position;
        		showDialog(DETAIL_DIALOG);
        	}
        });
	}
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch(id) {
    	case DETAIL_DIALOG:
    		return createDetailDialog();
    	case ABOUT_DIALOG:
    		return createAboutDialog();
    	default:
        	return super.onCreateDialog(id);
    	}
    }
    
    private Dialog createDetailDialog(){
    	Builder builder = new Builder(this);
    	builder.setTitle(R.string.detail_dailog_title);
    	builder.setIcon(mAdapter.iconIds[currentPosition]);
    	builder.setMessage(mAdapter.texts[currentPosition]);
    	builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				removeDialog(DETAIL_DIALOG);
			}
		});
    	return builder.create();
    }
    
    private Dialog createAboutDialog(){
    	Builder builder = new Builder(this);
    	builder.setTitle(R.string.about_title);
    	View view = getLayoutInflater().inflate(R.layout.about_dialog, null);
    	builder.setView(view);
    	builder.setPositiveButton(R.string.more_app, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					ApplicationInfo info = getPackageManager().getApplicationInfo("com.android.vending", PackageManager.GET_META_DATA);
					if(info != null) {
						Intent intent = new Intent(
								Intent.ACTION_VIEW,
								Uri.parse("market://search?q=pub:\"Kid.F\""));
						startActivity(intent);
						return;
					} else {
						Toast.makeText(Home.this, R.string.no_market, Toast.LENGTH_LONG);
					}
				} catch (NameNotFoundException e) {/*do nothing*/}
			}
		});
    	return builder.create();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.home, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.about:
    		showDialog(ABOUT_DIALOG);
    	}
    	return super.onOptionsItemSelected(item);
    }
    
	private class GridAdapter extends BaseAdapter {
    	private final int[] iconIds;
    	private final String[] texts;
    	private final Drawable[] drawables;
    	public GridAdapter() {
    		Class<?> clazz = android.R.drawable.class;
    		Field[] fields = clazz.getDeclaredFields();
    		int size = fields.length;
    		iconIds = new int[size];
    		texts = new String[size];
    		drawables = new Drawable[size];
    		for (int i = 0; i < size; i++) {
    			try {
					iconIds[i] = fields[i].getInt(null);
					texts[i] = "android.R.drawable.".concat(fields[i].getName());
					drawables[i] = getResources().getDrawable(iconIds[i]);
				} catch (IllegalArgumentException e) {
					Log.e(TAG, "error on reading resource id", e);
				} catch (IllegalAccessException e) {
					Log.e(TAG, "error on reading resource id", e);
				}
			}
    		
		}
    	
		@Override
		public int getCount() {
			return iconIds.length;
		}

		@Override
		public Integer getItem(int position) {
			return iconIds[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView icon;
			if(convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.item, null);
				icon = (ImageView)convertView.findViewById(R.id.icon);
				convertView.setTag(icon);
			} else {
				icon = (ImageView)convertView.getTag();
			}
			icon.setImageDrawable(drawables[position]);
			return convertView;
		}	
    }
}