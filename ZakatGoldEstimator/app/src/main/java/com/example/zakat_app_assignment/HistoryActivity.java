package com.example.zakat_app_assignment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HistoryActivity extends AppCompatActivity {

    Toolbar abtToolbar;
    TextView historyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        abtToolbar = findViewById(R.id.history_toolbar); // Update the Toolbar ID here
        setSupportActionBar(abtToolbar);
        getSupportActionBar().setTitle("History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        historyTextView = findViewById(R.id.historyTextView); // Initialize TextView

        // Display zakat calculation history
        displayZakatHistory();
    }

    private void displayZakatHistory() {
        // Retrieve zakat calculation history from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("ZakatHistory", MODE_PRIVATE);
        String history = sharedPreferences.getString("ZakatHistoryKey", "");

        // Display zakat calculation history in TextView
        historyTextView.setText(history);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Go back to previous activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
