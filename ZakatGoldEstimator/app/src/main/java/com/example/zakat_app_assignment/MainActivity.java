package com.example.zakat_app_assignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etWeight, etGoldType, etGoldValue;
    Button btnCalculate;
    TextView tvTotalValue, tvZakatValue;
    Toolbar myToolbar;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etWeight = findViewById(R.id.etWeight);
        etGoldType = findViewById(R.id.etGoldType);
        etGoldValue = findViewById(R.id.etGoldValue);
        btnCalculate = findViewById(R.id.btnCalculate);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        tvZakatValue = findViewById(R.id.tvZakatValue);

        btnCalculate.setOnClickListener(this);

        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);

        // Get SharedPreferences instance
        sharedPreferences = getSharedPreferences("ZakatHistory", MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_share) {
            // Share intent code remains the same
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "My Zakat estimator application - https://github.com/aina47/ZakatGoldEstimator");
            startActivity(Intent.createChooser(shareIntent, null));
            return true;
        } else if (item.getItemId() == R.id.item_about) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        } else if (item.getItemId() == R.id.item_history) {
            Intent historyIntent = new Intent(this, HistoryActivity.class);
            startActivity(historyIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCalculate) {
            // Clear previous values
            tvTotalValue.setText("");
            tvZakatValue.setText("");

            // Validate and calculate
            if (validateInputs()) {
                double weight = Double.parseDouble(etWeight.getText().toString());
                int goldType = Integer.parseInt(etGoldType.getText().toString());
                double goldValue = Double.parseDouble(etGoldValue.getText().toString());

                double totalValue = weight * goldValue;
                tvTotalValue.setText("Total Gold Value: " + totalValue);

                double goldPayable = 0;
                double zakatValue = 0;

                if (goldType == 85) {
                    goldPayable = (weight - 85) > 0 ? (weight - 85) * goldValue : 0;
                } else if (goldType == 200) {
                    goldPayable = (weight - 200) > 0 ? (weight - 200) * goldValue : 0;
                }

                zakatValue = 0.025 * goldPayable;
                tvZakatValue.setText("Zakat Value: RM" + zakatValue);

                // Save calculation to SharedPreferences
                String history = sharedPreferences.getString("ZakatHistoryKey", "");
                String newCalculation = "Weight: " + weight + ", Gold Type: " + goldType + ", Gold Value: " + goldValue + ", Zakat: RM" + zakatValue + "\n\n";
                history = newCalculation + history;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ZakatHistoryKey", history);
                editor.apply();
            }
        }
    }

    // Validate input fields
    private boolean validateInputs() {
        String weightStr = etWeight.getText().toString();
        String goldTypeStr = etGoldType.getText().toString();
        String goldValueStr = etGoldValue.getText().toString();

        if (TextUtils.isEmpty(weightStr) || TextUtils.isEmpty(goldTypeStr) || TextUtils.isEmpty(goldValueStr)) {
            tvTotalValue.setText("Please fill in all fields.");
            return false;
        }

        try {
            double weight = Double.parseDouble(weightStr);
            int goldType = Integer.parseInt(goldTypeStr);
            double goldValue = Double.parseDouble(goldValueStr);

            if (weight <= 0 || goldValue <= 0 || (goldType != 85 && goldType != 200)) {
                tvTotalValue.setText("Invalid input. Please check values.");
                return false;
            }
        } catch (NumberFormatException e) {
            tvTotalValue.setText("Invalid input format. Please enter valid numbers.");
            return false;
        }

        return true;
    }
}
