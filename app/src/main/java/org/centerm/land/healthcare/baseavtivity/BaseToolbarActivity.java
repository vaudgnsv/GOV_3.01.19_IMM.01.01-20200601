package org.centerm.land.healthcare.baseavtivity;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.centerm.land.R;
import org.centerm.land.bassactivity.SettingToolbarActivity;


public abstract class BaseToolbarActivity extends SettingToolbarActivity {

    private Toolbar toolbar;
    private TextView titleLabel;

    protected void initWidgetToolbar() {
        toolbar = findViewById(R.id.toolbar);
        titleLabel = findViewById(R.id.titleLabel);
    }

    protected void setTitleToolbar(String title) {
        if (titleLabel != null) {
            titleLabel.setText(title);
        }
    }
}
