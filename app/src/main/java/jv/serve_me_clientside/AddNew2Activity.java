package jv.serve_me_clientside;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//import java.util.Objects;

public class AddNew2Activity extends Activity {

    ArrayList<Artikl> ArtiklList;
    ArrayList<Artikl> PicaList;

    Artikl trenArtikl;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnew2);

        Bundle bundle = getIntent().getExtras();
        ArtiklList = (ArrayList<Artikl>) bundle.getSerializable("artikli");

        PicaList = new ArrayList<Artikl>();

        for (Artikl a : ArtiklList)
        {
            if (a.tip.equals(getResources().getString(R.string.CONST_pice)))
            {
                PicaList.add(a);
            }
        }


        spinner = (Spinner) findViewById(R.id.spinnerMain);
        updateSpinnerData(spinner);

        enableBtnDodaj(0, 0);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                TextView t = (TextView) findViewById(R.id.tvKolicinaIznos);
                int trenKolicina = Integer.parseInt(t.getText().toString());

                enableBtnDodaj(position, trenKolicina);

                updateCijena();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
                updateCijena();
            }

        });
    }


    void enableBtnDodaj(int pos, int kol)
    {
        Button btnDodaj = (Button) findViewById(R.id.btnDodaj);

        if ((pos > 0) && (kol > 0))
        {
            btnDodaj.setEnabled(true);
        }
        else btnDodaj.setEnabled(false);
    }


    public void btnKolicinaMinusClick(View v)
    {
        TextView tv = (TextView) findViewById(R.id.tvKolicinaIznos);

        int trenKolicina = Integer.parseInt(tv.getText().toString());

        if (trenKolicina > 1) trenKolicina -= 1;
        else trenKolicina = 0;

        tv.setText(String.valueOf(trenKolicina));

        updateCijena();

        enableBtnDodaj(spinner.getSelectedItemPosition(), trenKolicina);
    }

    public void btnKolicinaPlusClick(View v)
    {
        TextView tv = (TextView) findViewById(R.id.tvKolicinaIznos);

        int trenKolicina = Integer.parseInt(tv.getText().toString());
        trenKolicina += 1;

        tv.setText(String.valueOf(trenKolicina));

        updateCijena();

        enableBtnDodaj(spinner.getSelectedItemPosition(), trenKolicina);
    }

    void updateSpinnerData(Spinner spinner)
    {
        ArrayList<Map<String, String>> spinnerDisplayData = new ArrayList<Map<String, String>>();

        Map<String, String> tempMap = new HashMap<String, String>(2);
        tempMap.put("text", getResources().getString(R.string.spinner_init_text));
        tempMap.put("subtext", " ");

        spinnerDisplayData.add(tempMap);

        for(Artikl p : PicaList) {
            tempMap = new HashMap<String, String>(2);
            tempMap.put("text", p.naziv);
            tempMap.put("subtext", "Cijena: " + String.format("%.2f", p.cijena) + " kn");
            spinnerDisplayData.add(tempMap);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, spinnerDisplayData, android.R.layout.two_line_list_item,

                new String[] {"text", "subtext"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});

        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    public void updateCijena()
    {
        float tempCijena = 0.0f;
        TextView tv = (TextView) findViewById(R.id.tvCijenaPicaIznos);

        if (spinner.getSelectedItemPosition() > 0)
        {
            int pos = spinner.getSelectedItemPosition() - 1;
            Artikl p = PicaList.get(pos);

            TextView t = (TextView) findViewById(R.id.tvKolicinaIznos);
            int kol = Integer.parseInt(t.getText().toString());

            tempCijena = p.cijena * kol;
        }

        tv.setText(String.format("%.2f", tempCijena) + " kn");
    }

    public void btnDodajClick(View v)
    {
        Intent intent = new Intent();

        int pos = spinner.getSelectedItemPosition() - 1;
        Artikl p = PicaList.get(pos);

        TextView t = (TextView) findViewById(R.id.tvKolicinaIznos);
        int kol = Integer.parseInt(t.getText().toString());

        Narudzba n = new Narudzba(p, kol);
        intent.putExtra("narudzba", n);

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void btnOdustaniClick(View v)
    {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}
