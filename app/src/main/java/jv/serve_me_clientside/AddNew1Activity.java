package jv.serve_me_clientside;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
//import java.util.Objects;

public class AddNew1Activity extends Activity
{
    ArrayList<Artikl> ArtiklList;
    Artikl trenArtikl, artiklP1, artiklP2;

    ArrayList<Artikl> priloziList = new ArrayList<Artikl>();

    RadioGroup rg;
    ArrayList<CheckBox> cbList = new ArrayList<CheckBox>();

    CompoundButton.OnCheckedChangeListener occl;

    Artikl tipPalacinke = new Artikl();

    Button btnDodaj;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnew1);

        Bundle bundle = getIntent().getExtras();
        ArtiklList = (ArrayList<Artikl>) bundle.getSerializable("artikli");
        artiklP1 = (Artikl)bundle.getSerializable("p1");
        artiklP2 = (Artikl)bundle.getSerializable("p2");

        RadioButton rb1 = (RadioButton)findViewById(R.id.rbObicna);
        rb1.setTag(artiklP1);
        RadioButton rb2 = (RadioButton)findViewById(R.id.rbPohana);
        rb2.setTag(artiklP2);

        btnDodaj = (Button) findViewById(R.id.btnDodajPalacinku);
        btnDodaj.setEnabled(false);


        occl = new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                priloziList = new ArrayList<Artikl>();

                for (CheckBox cb : cbList)
                {
                    Artikl tag = (Artikl) cb.getTag();

                    if (cb.isChecked())
                    {
                        if (!priloziList.contains(tag))
                        {
                            priloziList.add(tag);
                        }
                    }
                    else
                    {
                        if (priloziList.contains(tag))
                        {
                            priloziList.remove(tag);
                        }
                    }
                }

                updateCijena();
            }
        };

        RadioGroup rg = (RadioGroup) findViewById(R.id.rgTip);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();

                if (isChecked) {
                    tipPalacinke = (Artikl) checkedRadioButton.getTag();
                    btnDodaj = (Button) findViewById(R.id.btnDodajPalacinku);
                    btnDodaj.setEnabled(true);
                }

                updateCijena();
            }
        });

        dodajPriloge();

    }

    void dodajPriloge()
    {
        LinearLayout llp = (LinearLayout) findViewById(R.id.llvPrilozi);
        cbList = new ArrayList<CheckBox>();

        for(Artikl a : ArtiklList)
        {
            int i = 0;
            String tempString;

            if (a.tip.equals(getResources().getString(R.string.CONST_prilog)))
            {
                CheckBox cb = new CheckBox(this);
                tempString = a.naziv + " (" + getResources().getString(R.string.cijena) + " " + a.cijena + " kn)";
                cb.setText(tempString);
                cb.setTag(a);
                cb.setChecked(false);
                cb.setOnCheckedChangeListener(occl);

                llp.addView(cb);

                cbList.add(cb);
            }
        }

    }

    public void updateCijena()
    {
        float tempCijena = 0.0f;
        TextView tv = (TextView) findViewById(R.id.tvCijenaPalacinkeIznos);

        tempCijena += tipPalacinke.cijena;

        for (Artikl p : priloziList)
        {
            tempCijena += p.cijena;
        }

        tv.setText(String.format("%.2f", tempCijena) + " kn");
    }

    public void btnDodajClick(View v)
    {
        Intent intent = new Intent();

        Narudzba n = new Narudzba(tipPalacinke, 1, priloziList);
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
