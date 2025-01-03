package gmapsfx;

import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class ArrayTester extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create WebView
        webView = new WebView(this);
        setContentView(webView);

        // Configure WebView settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Set a WebViewClient to handle loading within the app
        webView.setWebViewClient(new WebViewClient());

        // Add a JavaScript interface for communication with JavaScript
        webView.addJavascriptInterface(new JavaScriptInterface(), "Android");

        // Load the HTML file from the assets folder
        webView.loadUrl("file:///android_asset/arrays.html");
    }

    // JavaScript interface for communication with JavaScript in the WebView
    public class JavaScriptInterface {

        @JavascriptInterface
        public void displayTest() {
            runOnUiThread(() -> System.out.println("displayTest called from JavaScript"));
        }

        @JavascriptInterface
        public void displayArray(String array) {
            runOnUiThread(() -> System.out.println("Array from JavaScript: " + array));
        }

        @JavascriptInterface
        public void displayTestEnd() {
            runOnUiThread(() -> System.out.println("Test ended from JavaScript"));
        }
    }
}