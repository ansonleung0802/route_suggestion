package gmapsfx.javascript;

import android.webkit.WebView;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Base class for any Google JavaScript object, adapted for Android using WebView.
 */
public class JavascriptObject {

    protected static Map<String, JavascriptObject> peerRegistry = new WeakHashMap<>();
    protected WebView webView;
    protected String variableName;
    protected static int objectCounter = 0;

    /**
     * Default constructor.
     */
    protected JavascriptObject(WebView webView) {
        this.webView = webView;
        this.variableName = getNextVariableName();
    }

    /**
     * Constructor for creating a JavaScript object with a specific type.
     *
     * @param webView The WebView instance for JavaScript execution.
     * @param type    The type of the JavaScript object.
     */
    protected JavascriptObject(WebView webView, String type) {
        this(webView);
        executeJavascript("var " + variableName + " = new " + type + "();");
    }

    /**
     * Executes JavaScript code in the WebView.
     *
     * @param script The JavaScript code to execute.
     */
    protected void executeJavascript(String script) {
        webView.post(() -> webView.evaluateJavascript(script, null));
    }

    /**
     * Gets the name of the next variable, which will be unique.
     *
     * @return The name of the next variable to create.
     */
    protected final String getNextVariableName() {
        return getClass().getSimpleName() + (objectCounter++);
    }

    /**
     * Gets the name of this variable within the JavaScript runtime.
     *
     * @return The name of this variable.
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * Sets a property on this JavaScript object.
     *
     * @param propertyName  The property name.
     * @param propertyValue The property value.
     */
    public void setProperty(String propertyName, Object propertyValue) {
        executeJavascript(variableName + "." + propertyName + " = " + formatValue(propertyValue) + ";");
    }

    /**
     * Formats a value for use in JavaScript.
     *
     * @param value The value to format.
     * @return The formatted value as a string.
     */
    private String formatValue(Object value) {
        if (value instanceof String) {
            return "\"" + value + "\"";
        } else if (value instanceof Boolean || value instanceof Number) {
            return value.toString();
        } else if (value instanceof JavascriptObject) {
            return ((JavascriptObject) value).getVariableName();
        }
        throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
    }

    /**
     * Invokes a JavaScript function on this object.
     *
     * @param function The function name.
     * @param args     The arguments for the function.
     */
    public void invokeJavascript(String function, Object... args) {
        StringBuilder script = new StringBuilder(variableName + "." + function + "(");
        for (int i = 0; i < args.length; i++) {
            script.append(formatValue(args[i]));
            if (i < args.length - 1) {
                script.append(",");
            }
        }
        script.append(");");
        executeJavascript(script.toString());
    }
}