package com.example.philip.alphafitness;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.philip.alphafitness.background.StepCounter;


import java.util.ArrayList;
import java.util.List;

public class DetailView extends FragmentActivity implements Runnable {


    TextView averageSpeed;
    TextView maximumSpeed;
    TextView minimumSpeed;

    Calculator calculator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wokout_details);

        Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            super.onBackPressed();
            finish();
        }

        averageSpeed = findViewById(R.id.tv_average_speed);
        maximumSpeed = findViewById(R.id.tv_max_speed);
        minimumSpeed = findViewById(R.id.tv_min_speed);

        calculator = new Calculator(this);

        /**
         * Column Chart
         * */

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        //anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Steps", StepCounter.getSteps()));
        data.add(new ValueDataEntry("Calories", Calculator.getCalories()));

        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Average Steps Taken & Calories Burned");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Steps / Calories");
        cartesian.yAxis(0).title("Amount");

        anyChartView.setChart(cartesian);

        Thread thread = new Thread(this);
        thread.start();
    }



    private void setAverageSpeed(){

        Log.e("avgSpeed", Double.toString(calculator.getAverageSpeed()));
        averageSpeed.setText(String.format(java.util.Locale.US, "%.2f", calculator.getAverageSpeed()));

    }

    private void setMaximumSpeed(){

        maximumSpeed.setText(String.format(java.util.Locale.US, "%.2f", calculator.getMaxSpeed()));
    }

    private void setMinimumSpeed(){

        minimumSpeed.setText(String.format(java.util.Locale.US, "%.2f", calculator.getMinSpeed()));
    }


    @Override
    public void run() {
        setAverageSpeed();
        setMaximumSpeed();
        setMinimumSpeed();
    }
}
