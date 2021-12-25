package com.example.yourbook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

public class BookDetails extends AppCompatActivity {
    /**
     * This activity to get information of book from firebase
     */
    // declare variables
    Button DownloadBtn_aud, DownloadBtn_pdf;
    String bookName;
    RoundedImageView img;
    Button l, r;
    TextView title;
    public static String pdfURL, audioURL, imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        // defining IDs
        img = findViewById(R.id.Limg);
        l = findViewById(R.id.listen);
        r = findViewById((R.id.read));
        title = findViewById(R.id.Dname);

        // get book (Name, cover, pdf and audio) URL from MainAdapter activity activity
        bookName = getIntent().getExtras().get("Name").toString();
        imgUrl = getIntent().getExtras().get("url").toString();
        pdfURL = getIntent().getExtras().get("pdf").toString();
        audioURL = getIntent().getExtras().get("audio").toString();

        // set the tittle of the book to be shown
        title.setText(bookName);
        Glide.with(BookDetails.this).load(imgUrl).placeholder(R.mipmap.ic_launcher).into(img);
        // when user click read, pdf opens
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BookDetails.this, read.class);
                startActivity(i);
            }
        });
        //when user click listen, audio opens
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BookDetails.this, Listen.class);
                startActivity(i);
            }
        });

        //when user click download audio, audio downloads
        DownloadBtn_aud = (Button) findViewById(R.id.download_aud);
        DownloadBtn_aud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Download(audioURL);
            }
        });

        //when user click download pdf, pdf downloads
        DownloadBtn_pdf = (Button) findViewById(R.id.download_pdf);
        DownloadBtn_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Download(pdfURL);

            }
        });

    }

    // download method to download book or audio
    public void Download(String url) {   // get url from firebase
        String geturl = url;
        // change format to link to be understood by download manager
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(geturl));
        // set tittle of downloaded file
        String title = URLUtil.guessFileName(geturl, null, null);
        request.setTitle(title);
        // add description to file
        request.setDescription("Book in pocket application");
        // show downloading notification
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedOverMetered(true);
        // create download object
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        // send a download request with url
        downloadManager.enqueue(request);
        Toast.makeText(BookDetails.this, "Downloading....!", Toast.LENGTH_SHORT).show();
    }

}