package nl.striemeijersgang0345.other;

import androidx.appcompat.app.AppCompatActivity;
import nl.striemeijersgang0345.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class MineSweeper extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_sweeper);

        WebView mWebView = findViewById(R.id.mine_webview);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/minesweeper/index.html");
    }

    @Override
    protected void onDestroy() {
        WebView mWebView = findViewById(R.id.mine_webview);
        mWebView.destroy();
        super.onDestroy();
    }
}
