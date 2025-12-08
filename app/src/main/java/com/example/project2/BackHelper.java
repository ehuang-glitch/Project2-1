package com.example.project2;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class BackHelper {

    public static void attachBack(Activity activity) {
        Button back = activity.findViewById(R.id.btnPageBack);
        if (back != null) {
            back.setOnClickListener(v -> activity.finish());
        }
    }
}

