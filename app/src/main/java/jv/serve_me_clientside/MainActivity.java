package jv.serve_me_clientside;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
//import java.util.Objects;

public class MainActivity extends Activity {

    ArrayList<Artikl> ArtiklList = new ArrayList<Artikl>();
    Artikl artiklP1, artiklP2; ////////////////////////////////////////////////////////
    ArrayList<Narudzba> NarudzbaList = new ArrayList<Narudzba>();

    ArrayList<TableRow> TRList = new ArrayList<TableRow>();

    View.OnClickListener deleteClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteClicked(v);
        }
    };

    Button btnNaruci;
    TextView tvEmpty;

    //////////
    //private EditText serverIp;

    private Button connectPhones;

    //private String serverIpAddress = "192.168.0.123";
    private String serverIpAddress = "fe80::6aa0:f6ff:fe45:de5e%wlan0";
    private int socketNumber = 10001;

    private boolean connected = false;

    //fe80::6aa0:f6ff:fe45:de5e%wlan0
    /////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNaruci = (Button) findViewById(R.id.btnNaruci);
        btnNaruci.setEnabled(false);

        TextView tvKolicina = (TextView) findViewById(R.id.tvKolicina);
        tvKolicina.setGravity(Gravity.RIGHT);
        TextView tvCijenaKom = (TextView) findViewById(R.id.tvCijenaKom);
        tvCijenaKom.setGravity(Gravity.RIGHT);
        TextView tvCijena = (TextView) findViewById(R.id.tvCijena);
        tvCijena.setGravity(Gravity.RIGHT);

        /////////////////
        btnNaruci.setOnClickListener(connectListener);
        ///////////////

        //TEST
        loadTEST();
        artiklP1 = new Artikl(1, "Obična palačinka", getString(R.string.CONST_palacinka), (float) 10.0);
        artiklP2 = new Artikl(2, "Pohana palačinka", getString(R.string.CONST_palacinka), (float) 12.0);
        //TEST

        ispisNarudzbe();
    }

    private View.OnClickListener connectListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!connected)
            {
                if (!serverIpAddress.equals(""))
                {
                    ArrayList<Narudzba> outNarudzbaList = new ArrayList<Narudzba>();

                    for (Narudzba narudzba : NarudzbaList)
                    {
                        outNarudzbaList.add(narudzba);
                    }

                    Thread narudzbaThread = new Thread(new narudzbaSendThread());
                    narudzbaThread.start();

                    NarudzbaList = new ArrayList<Narudzba>();

                    btnNaruci.setEnabled(false);
                    ispisNarudzbe();

                    tvEmpty.setText(getString(R.string.hvala));
                }
            }
        }
    };

    public void dodaj1Click(View v) {
        Intent intent = new Intent(this, AddNew1Activity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("artikli", ArtiklList);
        bundle.putSerializable("p1", artiklP1);
        bundle.putSerializable("p2", artiklP2);

        intent.putExtras(bundle);

        startActivityForResult(intent, 1);
    }

    public void dodaj2Click(View v) {
        Intent intent = new Intent(this, AddNew2Activity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("artikli", ArtiklList);

        intent.putExtras(bundle);

        startActivityForResult(intent, 2);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                NarudzbaList.add((Narudzba) bundle.getSerializable("narudzba"));

                ispisNarudzbe();
                btnNaruci.setEnabled(true);
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                NarudzbaList.add((Narudzba) bundle.getSerializable("narudzba"));

                ispisNarudzbe();
                btnNaruci.setEnabled(true);
            }
        }
    }

    void ispisNarudzbe()
    {

        float textSize = 20;
        int tempDP;

        TableRow tempChild;

        TableLayout tl = (TableLayout) findViewById(R.id.mainTable);

        for (int i = 0; i < ((ViewGroup) tl).getChildCount(); i++) {
            tempChild = (TableRow) ((ViewGroup) tl).getChildAt(i);
            TRList.add(tempChild);
        }

        tl.removeView(findViewById(R.id.initRow));

        for (TableRow tr : TRList) {
            if (tr != findViewById(R.id.tableHeader)) {
                tl.removeView(tr);
            }
        }

        if (NarudzbaList.size() > 0)
        {

            float sumCijena = 0;

            for (int i = 0; i < NarudzbaList.size(); i++)
            {
                Narudzba n = NarudzbaList.get(i);

                TableRow tr = new TableRow(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                tr.setLayoutParams(lp);

                TextView tvNaziv = new TextView(this);
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                tempDP = (int) (16 * getResources().getDisplayMetrics().density + 0.5f);
                lp.setMargins(tempDP, 0, 0, 0);
                tvNaziv.setLayoutParams(lp);
                tvNaziv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                tvNaziv.setTextColor(Color.BLACK);
                tvNaziv.setGravity(Gravity.CENTER_VERTICAL);
                tvNaziv.setText(n.naziv);

                TextView tvKolicina = new TextView(this);
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                tvKolicina.setLayoutParams(lp);
                tvKolicina.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                tvKolicina.setTextColor(Color.BLACK);
                tvKolicina.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
                tvKolicina.setText(String.valueOf(n.kolicina));

                TextView tvCijenaKom = new TextView(this);
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                tvCijenaKom.setLayoutParams(lp);
                tvCijenaKom.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                tvCijenaKom.setTextColor(Color.BLACK);
                tvCijenaKom.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
                tvCijenaKom.setText(String.format("%.2f", n.cijena));

                TextView tvCijena = new TextView(this);
                lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                tvCijena.setLayoutParams(lp);
                tvCijena.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                tvCijena.setTextColor(Color.BLACK);
                tvCijena.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
                float tempCijena = n.cijena * n.kolicina;
                tvCijena.setText(String.format("%.2f", tempCijena));
                sumCijena += tempCijena;

                ImageButton ibDelete = new ImageButton(this);
                lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                tempDP = (int) (16 * getResources().getDisplayMetrics().density + 0.5f);
                lp.setMargins(5*tempDP, tempDP, 5*tempDP, tempDP);
                ibDelete.setLayoutParams(lp);
                ibDelete.setBackgroundResource(R.drawable.delete_icon_32);
                ibDelete.setTag(n);
                ibDelete.setOnClickListener(deleteClickedListener);

                tr.addView(tvNaziv);
                tr.addView(tvKolicina);
                tr.addView(tvCijenaKom);
                tr.addView(tvCijena);
                tr.addView(ibDelete);

                tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                if (n.tip.equals(getString(R.string.CONST_palacinka)))
                {
                    if (!n.prilozi.isEmpty())
                    {
                        for (Artikl p : n.prilozi)
                        {
                            tr = new TableRow(this);
                            lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                            tr.setLayoutParams(lp);

                            tvNaziv = new TextView(this);
                            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                            tempDP = (int) (16 * getResources().getDisplayMetrics().density + 0.5f);
                            lp.setMargins(tempDP, 0, 0, 0);
                            tvNaziv.setLayoutParams(lp);
                            tvNaziv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                            tvNaziv.setTextColor(Color.BLACK);
                            tvNaziv.setGravity(Gravity.CENTER_VERTICAL);
                            tvNaziv.setText("   + " + p.naziv);

                            tvKolicina = new TextView(this);
                            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                            tvKolicina.setLayoutParams(lp);
                            tvKolicina.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                            tvKolicina.setTextColor(Color.BLACK);
                            tvKolicina.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
                            tvKolicina.setText(" ");

                            tvCijenaKom = new TextView(this);
                            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                            tvCijenaKom.setLayoutParams(lp);
                            tvCijenaKom.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                            tvCijenaKom.setTextColor(Color.BLACK);
                            tvCijenaKom.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
                            tvCijenaKom.setText(" ");

                            tvCijena = new TextView(this);
                            lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
                            tvCijena.setLayoutParams(lp);
                            tvCijena.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                            tvCijena.setTextColor(Color.BLACK);
                            tvCijena.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
                            tvCijena.setText(String.format("%.2f", p.cijena));
                            sumCijena += p.cijena;

                            ImageButton ibEmpty = new ImageButton(this);
                            lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                            tempDP = (int) (16 * getResources().getDisplayMetrics().density + 0.5f);
                            lp.setMargins(5*tempDP, tempDP, 5*tempDP, tempDP);
                            ibEmpty.setLayoutParams(lp);
                            ibEmpty.setBackgroundResource(R.drawable.delete_icon_32);
                            ibEmpty.setVisibility(View.INVISIBLE);
                            ibEmpty.setEnabled(false);

                            tr.addView(tvNaziv);
                            tr.addView(tvKolicina);
                            tr.addView(tvCijenaKom);
                            tr.addView(tvCijena);
                            tr.addView(ibEmpty);

                            tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                        }
                    }

                }

            }

            TextView ukCijena = (TextView) findViewById(R.id.tvCijenaUkIznos);
            ukCijena.setText(String.format("%.2f", sumCijena) + " " + getString(R.string.kn));

        } else {
            TableRow tr = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            tr.setLayoutParams(lp);

            tvEmpty = new TextView(this);
            lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            tempDP = (int) (16 * getResources().getDisplayMetrics().density + 0.5f);
            lp.setMargins(tempDP, 0, 0, 0);
            tvEmpty.setLayoutParams(lp);
            tvEmpty.setText(getString(R.string.nema_artikala));
            tvEmpty.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

            tr.addView(tvEmpty);

            tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            TextView ukCijena = (TextView) findViewById(R.id.tvCijenaUkIznos);
            ukCijena.setText("0.00 kn");
        }


    }

    public void deleteClicked(View v) {
        Narudzba narudzbaZaBrisanje = (Narudzba) v.getTag();

        Iterator<Narudzba> i = NarudzbaList.iterator();

        while (i.hasNext()) {
            Narudzba n = i.next();

            if (n == narudzbaZaBrisanje) {
                i.remove();
            }
        }

        ispisNarudzbe();

    }

    void loadTEST()
    {
        Artikl tempArtikl = new Artikl(1, "Obična palačinka", getString(R.string.CONST_palacinka), (float) 10.0f);
        ArtiklList.add(tempArtikl);
        tempArtikl = new Artikl(2, "Pohana palačinka", getString(R.string.CONST_palacinka), (float) 12.0);
        ArtiklList.add(tempArtikl);
        tempArtikl = new Artikl(3, "Kava s mlijekom", getString(R.string.CONST_pice), (float) 8.0);
        ArtiklList.add(tempArtikl);
        tempArtikl = new Artikl(4, "Coca-cola", getString(R.string.CONST_pice), (float) 12.0);
        ArtiklList.add(tempArtikl);
        tempArtikl = new Artikl(5, "Karlovačko", getString(R.string.CONST_pice), (float) 14.0);
        ArtiklList.add(tempArtikl);
        tempArtikl = new Artikl(6, "Nutella", getString(R.string.CONST_prilog), (float) 2.0);
        ArtiklList.add(tempArtikl);
        tempArtikl = new Artikl(7, "Vanilija", getString(R.string.CONST_prilog), (float) 2.0);
        ArtiklList.add(tempArtikl);
        tempArtikl = new Artikl(8, "Banana", getString(R.string.CONST_prilog), (float) 3.0);
        ArtiklList.add(tempArtikl);
    }

    public class narudzbaSendThread implements Runnable {

        public void run() {

            String outString = "";

            /*
            String outString1 = "";

            for (Narudzba n1 : NarudzbaList) {

                String d1 = "|";
                String h = "#";
                outString1 += d1 + n1.id + d1 + n1.naziv + d1 + n1.tip + d1 + n1.cijena + d1 + n1.kolicina;
                if (n1.tip.equals(getString(R.string.CONST_palacinka)) && !n1.prilozi.isEmpty()) {
                    for (Artikl p : n1.prilozi) {
                        outString1 += h + p.id + h + p.naziv + h + p.tip + h + p.cijena;
                    }
                }
            }

            Log.d("ClientActivity", outString1);
            */

            try {
                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                Log.d("ClientActivity", "C: Connecting...");
                Socket socket = new Socket(serverAddr, socketNumber);
                connected = true;
                while (connected) {
                    try {
                        Log.d("ClientActivity", "C: Sending command.");
                        PrintWriter out = new PrintWriter(
                                new BufferedWriter(new OutputStreamWriter(
                                        socket.getOutputStream())), true);

                        for (Narudzba n : NarudzbaList)
                        {
                            String d = "|";
                            String h = "#";
                            outString += n.id + d + n.naziv + d + n.tip + d + n.cijena + n.kolicina;
                            if (n.tip.equals(getString(R.string.CONST_palacinka)) && !n.prilozi.isEmpty())
                            {
                                for (Artikl p : n.prilozi)
                                {
                                    outString += h + p.id + h + p.naziv + h + p.tip + h + p.cijena;
                                }
                            }

                            out.println(outString);
                            Log.d("ClientActivity", "C: Sent.");

                        }

                        connected = false;
                    } catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                    }
                }
                socket.close();
                Log.d("ClientActivity", "C: Closed.");
            } catch (Exception e) {
                Log.e("ClientActivity", "C: Error", e);
                connected = false;
            }
        }
    }
}
