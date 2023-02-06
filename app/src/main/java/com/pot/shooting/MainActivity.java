package com.pot.shooting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import api.pot.shooting.qrcode.QRCode;

public class MainActivity extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.iv);

        scan();
    }

    String text = "Yo, on dit quoi?";
    Handler handler;
    private void code() {
        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    final Bitmap bitmap = QRCode.textToImageEncode(text);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            iv.setImageBitmap(bitmap);
                        }
                    });
                }catch (Exception e){}
            }
        }).start();
    }

    /**
     * --------------------------------------------------------------------------------
     * */

    private Context getContext() {
        return this;
    }

    /**
     * --------------------------------------------------------------------------------
     * */

    private void scan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() != null) {
                String datas = "Data: \n"+result.getContents()+"\n\nDecoded: \n"+ result.getContents();
                Toast.makeText(MainActivity.this, datas, Toast.LENGTH_LONG).show();
                text = datas;
                code();
            }
        }
    }
}
