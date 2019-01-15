package com.example.philip.alphafitness.userProfile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.philip.alphafitness.Calculator;
import com.example.philip.alphafitness.R;
import com.example.philip.alphafitness.database.WorkoutData;

import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {

    private EditText editText;
    private EditText inputName;

    private static String userName = "Peter Doe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView textView = findViewById(R.id.textView);
        textView.setText(userName);

        ListView listView = findViewById(R.id.profileDetailList);

        ProfileListAdapter adapter = new ProfileListAdapter(getList(), this);

        listView.setAdapter(adapter);

        final Button buttonGender = findViewById(R.id.buttonGender);
        final Context context = this;

        buttonGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Gender");

                View currentFocus = getCurrentFocus();
                if (currentFocus != null) {
                    currentFocus.clearFocus();
                }

                builder.setView(getInputGender());

                builder.setPositiveButton("Male", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonGender.setText("Male");
                    }
                });

                builder.setNegativeButton("Female", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonGender.setText("Female");
                    }
                });

                builder.show();
            }
        });

        final Button buttonWeight = findViewById(R.id.buttonWeight);

        if(Calculator.userWeight != 0.0){
            buttonWeight.setText(Double.toString(Calculator.userWeight)+"lbs");
        }

        buttonWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Gender");

                View currentFocus = getCurrentFocus();
                if (currentFocus != null) {
                    currentFocus.clearFocus();
                }

                builder.setView(getInputWeight());

                builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userWeight = editText.getText()+"";
                        buttonWeight.setText(userWeight+" lbs");
                        Calculator.userWeight = Double.parseDouble(userWeight);
                    }
                });

                builder.show();
            }
        });
    }


    private ArrayList<UserData> getList(){
        ArrayList<UserData> data = new ArrayList<>();
        Calculator calculator = new Calculator(this);
        Integer workoutCount = calculator.getWorkoutCount();

        WorkoutData workoutDetailsSum = calculator.getWorkoutDetailsSum();
        Double distance = workoutDetailsSum.getDistance();
        long duration = workoutDetailsSum.getDuration();
        Watch watch = new Watch(duration);
        Double calories = Calculator.getCalories(distance);

        WorkoutData workoutWeeklyAVG = calculator.getWorkoutDetailsWeeklyAVG();
        Double distanceWeekly = workoutWeeklyAVG.getDistance();
        long durationWeekly = workoutWeeklyAVG.getDuration();
        Watch watchWeekly = new Watch(durationWeekly);
        Integer workoutCountWeekly = workoutWeeklyAVG.getWorkoutCount();
        Double caloriesWeekly = Calculator.getCalories(distanceWeekly);

        Toast.makeText(this, Integer.toString(workoutDetailsSum.getSteps()), Toast.LENGTH_LONG).show();

        data.add(new UserData("Average/Weekly", null));
        data.add(new UserData("Distance", String.format(java.util.Locale.US, "%.3f", distanceWeekly)));
        data.add(new UserData("Time", Integer.toString(watchWeekly.getHours())+"h "+
                Integer.toString(watchWeekly.getMinutes())+"min "+
                Long.toString(watchWeekly.getSeconds())+"s"));
        data.add(new UserData("Workouts", Integer.toString(workoutCountWeekly)));
        data.add(new UserData("Calories Burned", String.format(java.util.Locale.US, "%.3f", caloriesWeekly)));


        data.add(new UserData("All Time", null));
        data.add(new UserData("Distance", String.format(java.util.Locale.US, "%.3f", distance)));
        data.add(new UserData("Time", Integer.toString(watch.getHours())+"h "+
                Integer.toString(watch.getMinutes())+"min "+
                Long.toString(watch.getSeconds())+"s"));
        data.add(new UserData("Workouts", Integer.toString(workoutCount)));
        data.add(new UserData("Calories Burned", String.format(java.util.Locale.US, "%.3f", calories)));

        return data;
    }


    private View getInputGender(){
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(30, 20, 30, 0);

        TextView textView = new TextView(this);
        textView.setWidth(300);
        textView.setText("Select your Gender");
        textView.setTextSize(20);

        linearLayout.addView(textView, layoutParams);
        return linearLayout;
    }

    private View getInputWeight(){
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(30, 20, 30, 0);


        TextView textView = new TextView(this);
        textView.setWidth(300);
        textView.setText("Insert your weight");
        textView.setTextSize(20);


        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout2.setLayoutParams(layoutParams2);

        editText = new EditText(this);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                200, LinearLayout.LayoutParams.WRAP_CONTENT));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        TextView textView2 = new TextView(this);
        textView2.setWidth(200);
        textView2.setText("lbs");
        textView2.setTextSize(20);

        linearLayout2.addView(editText);
        linearLayout2.addView(textView2);

        linearLayout.addView(textView, layoutParams);
        linearLayout.addView(linearLayout2, layoutParams);
        return linearLayout;
    }

    private View getInputName(){

        inputName = new EditText(this);
        inputName.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        return inputName;
    }

    public static String getName() {
        return userName;
    }

    public void changeName(final View textField){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change the Name");

        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
        }

        builder.setView(getInputName());

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userName = inputName.getText()+"";

                ((TextView)textField).setText(userName);
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }


}
