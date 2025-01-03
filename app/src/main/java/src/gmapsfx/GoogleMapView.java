package gmapsfx;



import android.content.Context;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapView extends FrameLayout {

    private WebView webview;
    private boolean initialized = false;
    private final List<gmapsfx.MapComponentInitializedListener> mapInitializedListeners = new ArrayList<>();
    private final List<gmapsfx.MapReadyListener> mapReadyListeners = new ArrayList<>();

    public GoogleMapView(Context context) {
        this(context, null);
    }

    public GoogleMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(null);
    }

    public GoogleMapView(Context context, AttributeSet attrs, String mapResourcePath) {
        super(context, attrs);
        init(mapResourcePath);
    }

    private void init(String mapResourcePath) {
        webview = new WebView(getContext());
        addView(webview, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webview.setWebViewClient(new WebViewClient());
        webview.addJavascriptInterface(new JSListener(), "Android");

        String htmlFile = (mapResourcePath != null) ? mapResourcePath : "file:///android_asset/index.html";
        webview.loadUrl(htmlFile);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                initialized = true;
                fireMapInitializedListeners();
            }
        });
    }

    public void addMapInitializedListener(gmapsfx.MapComponentInitializedListener listener) {
        mapInitializedListeners.add(listener);
    }

    public void removeMapInitializedListener(gmapsfx.MapComponentInitializedListener listener) {
        mapInitializedListeners.remove(listener);
    }

    public void addMapReadyListener(gmapsfx.MapReadyListener listener) {
        mapReadyListeners.add(listener);
    }

    public void removeMapReadyListener(gmapsfx.MapReadyListener listener) {
        mapReadyListeners.remove(listener);
    }

    private void fireMapInitializedListeners() {
        for (gmapsfx.MapComponentInitializedListener listener : mapInitializedListeners) {
            listener.mapInitialized();
        }
    }

    private void fireMapReadyListeners() {
        for (gmapsfx.MapReadyListener listener : mapReadyListeners) {
            listener.mapReady();
        }
    }

    public void executeJavascript(String script) {
        if (initialized) {
            webview.post(() -> webview.evaluateJavascript(script, null));
        } else {
            throw new IllegalStateException("Map is not initialized");
        }
    }

    public class JSListener {
        @JavascriptInterface
        public void log(String text) {
            System.out.println("JS log: " + text);
        }

        @JavascriptInterface
        public void onMapReady() {
            fireMapReadyListeners();
        }
    }
}