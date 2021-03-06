package com.goomo.travel.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.goomo.travel.model.FlightSearchModel;
import com.goomo.travel.viewmodel.GoomoViewModel;
import com.goomo.travel.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.goomo.travel.util.AppData;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    AutoCompleteTextView actSource, actDesignation;
    String[] airpot1 = {"CCU", "BLR", "BOM", "DEL", "GOI", "HYD", "IXC", "JAI", "MAA", "PNQ"};
    String[] airpot2 = {"CCU", "BLR", "BOM", "DEL", "GOI", "HYD", "IXC", "JAI", "MAA", "PNQ"};
    TextView txtDate, txtAdult, txtClass;
    ArrayAdapter<String> adapter1, adapter2;
    Calendar calendar;
    ImageView imgSwap;
    RequestQueue requestQueue;
    private GoomoViewModel goomoViewModel;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtAdult = (TextView) findViewById(R.id.txtAdult);
        txtClass = (TextView) findViewById(R.id.txtClass);
        imgSwap = (ImageView) findViewById(R.id.img_Swap);
        requestQueue = Volley.newRequestQueue(this);

        goomoViewModel = ViewModelProviders.of(this).get(GoomoViewModel.class);

        adapter1 = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, airpot1);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, airpot2);

        actSource = (AutoCompleteTextView) findViewById(R.id.actSource);
        actDesignation = (AutoCompleteTextView) findViewById(R.id.actDestination);
        actSource.setThreshold(1);
        actDesignation.setThreshold(1);
        actSource.setAdapter(adapter1);
        actDesignation.setAdapter(adapter2);
        actSource.setTextColor(Color.BLACK);
        actDesignation.setTextColor(Color.BLACK);

        imgSwap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String temp = actSource.getText().toString();
                actSource.setText(actDesignation.getText().toString());
                actDesignation.setText(temp);
            }
        });

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String formattedDate = dateformat.format(calendar.getTime());
        txtDate.setText(formattedDate);
    }

    public void datePicker(final View view) {
        final View dialogView = View.inflate(this, R.layout.date_alert, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        dialogView.findViewById(R.id.btn_Date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);

                calendar = new GregorianCalendar(
                        datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth());
                txtDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(dialogView);
        alertDialog.show();

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    public void searchFlights(View view) {

        String strClass = txtClass.getText().toString();

        FlightSearchModel model = new FlightSearchModel();
        if (!TextUtils.isEmpty(actSource.getText().toString())) {
            model.setmSource(actSource.getText().toString());
        } else {
            actSource.setError("Enter Source value");
        }
        if (!TextUtils.isEmpty(actDesignation.getText().toString())) {
            model.setmDestination(actDesignation.getText().toString());
        } else {
            actDesignation.setError("Enter Designation value");
        }
        if (!TextUtils.isEmpty(txtDate.getText().toString())) {
            model.setmTraveldate(txtDate.getText().toString());
            AppData.setTraveldate(txtDate.getText().toString());
        }
        model.setmAdultCount(1);
        AppData.setAdultCount("1");
        model.setmChildCount(0);
        model.setmTravelClass(strClass);
        model.setIndianResient(true);

        goomoViewModel.searchFlights(model);


    }
}
