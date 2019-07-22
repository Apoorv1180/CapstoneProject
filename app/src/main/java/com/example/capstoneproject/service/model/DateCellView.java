package com.example.capstoneproject.service.model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.capstoneproject.R;

import sun.bob.mcalendarview.views.BaseCellView;
import sun.bob.mcalendarview.vo.DayData;

import static sun.bob.mcalendarview.MarkStyle.text;

public class DateCellView extends BaseCellView {
    public DateCellView(Context context) {
        super(context);
    }

    public DateCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setDisplayText(DayData day) {
        ((TextView) this.findViewById(R.id.id_cell_text)).setText(day.getText());
    }
}
