package counterapp.esraakhaled.com.Rak3ah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ChoosePrayerActivity extends AppCompatActivity {

    Spinner prayers;
    Button selectPrayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_prayer);
        prayers = (Spinner) findViewById(R.id.spinner);
        selectPrayer = (Button) findViewById(R.id.select_prayer);
        ArrayAdapter<CharSequence> prayerAdapter = ArrayAdapter.createFromResource(
                this, R.array.Prayers, R.layout.spinner_layout);
        prayerAdapter.setDropDownViewResource(R.layout.spinner_layout);
        prayers.setAdapter(prayerAdapter);

        selectPrayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prayers.getSelectedItemPosition()>0) {
                    Intent i = new Intent(ChoosePrayerActivity.this, MainActivity.class);
                    i.putExtra("PRAYER_POSITION", prayers.getSelectedItemPosition());
                    i.putExtra("PRAYER_NAME", prayers.getSelectedItem().toString());
                    startActivity(i);
                    finish();
                }
                else {
                    Toast.makeText(ChoosePrayerActivity.this, "Please Select a Prayer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
