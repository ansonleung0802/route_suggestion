/*
 * Copyright 2014 Geoff Capper.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gmapsfx.javascript.object;

import android.webkit.WebView;
import gmapsfx.javascript.JavascriptObject;
import gmapsfx.shapes.MapShapeOptions;

/**
 * Abstract class representing a MapShape on a Google Map.
 * Supports basic shape properties such as draggable, editable, and visible.
 */
public abstract class MapShape extends JavascriptObject {

    private WebView webView;

    public MapShape(String type, WebView webView) {
        super(type);
        this.webView = webView;
    }

    public MapShape(String type, MapShapeOptions opts, WebView webView) {
        super(type, opts);
        this.webView = webView;
    }

    /**
     * This method is called from the GoogleMap.addPolygon() method,
     * it should not be invoked directly.
     *
     * @param map The map to add this Polygon to.
     */
    protected void setMap(GoogleMap map) {
        executeJavascript(String.format("setMap(%s);", map.getMapVariable()));
    }

    // Gets the LatLngBounds of this shape.
    public void getBounds(JavascriptCallback callback) {
        executeJavascript("getBounds();", callback);
    }

    // Returns whether this shape can be dragged by the user.
    public void getDraggable(JavascriptCallback callback) {
        executeJavascript("getDraggable();", callback);
    }

    // Returns whether this shape can be edited by the user.
    public void getEditable(JavascriptCallback callback) {
        executeJavascript("getEditable();", callback);
    }

    // Returns whether this shape is visible on the map.
    public void getVisible(JavascriptCallback callback) {
        executeJavascript("getVisible();", callback);
    }

    // Allows the user to drag this shape over the map.
    public void setDraggable(boolean draggable) {
        executeJavascript(String.format("setDraggable(%b);", draggable));
    }

    // Allows the user to edit this shape.
    public void setEditable(boolean editable) {
        executeJavascript(String.format("setEditable(%b);", editable));
    }

    // Hides this shape if set to false.
    public void setVisible(boolean visible) {
        executeJavascript(String.format("setVisible(%b);", visible));
    }

    // Executes JavaScript code in the WebView.
    private void executeJavascript(String script) {
        webView.evaluateJavascript(script, null);
    }

    // Executes JavaScript with a callback for the result.
    private void executeJavascript(String script, JavascriptCallback callback) {
        webView.evaluateJavascript(script, callback::onResult);
    }

    // Interface for receiving JavaScript execution results.
    public interface JavascriptCallback {
        void onResult(String result);
    }
}