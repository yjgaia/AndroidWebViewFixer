# Android WebView Fixer
fix the poor Android WebView.
* fix file input.
* fix back button.
* fix facebook login.
* fix link to native browser.

## Install
Add AndroidWebViewFixer project to Library (eclipse) or add  bin/androidwebviewfixer.jar to your project's libs folder.
```java
import co.hanul.studio.AndroidWebViewFixer.AndroidWebViewFixer;
```

## Usage
There are 3 methods.
###### AndroidWebViewFixer.fix
```java
AndroidWebViewFixer.fix(activity, webView, progressBar, host, facebookRedirectURL)
AndroidWebViewFixer.fix(activity, webView, host, facebookRedirectURL)
AndroidWebViewFixer.fix(activity, webView, progressBar, host)
AndroidWebViewFixer.fix(activity, webView, host)
```

###### AndroidWebViewFixer.onKeyDown
```java
AndroidWebViewFixer.onKeyDown(webView, keyCode, event)
```

###### AndroidWebViewFixer.AndroidWebViewFixer
```java
AndroidWebViewFixer.onActivityResult(requestCode, resultCode, intent)
```

## Example
```java
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    webView = (WebView) findViewById(R.id.webview);
    progressBar = (ProgressBar) findViewById(R.id.progressbar);

    AndroidWebViewFixer.fix(this, webView, progressBar, host, host + "/home");

    webView.loadUrl(host + "/home");
}

@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (AndroidWebViewFixer.onKeyDown(webView, keyCode, event)) {
        return true;
    }
    return super.onKeyDown(keyCode, event);
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    AndroidWebViewFixer.onActivityResult(requestCode, resultCode, intent);
}
```

## License
[MIT](LICENSE)

## Author
[Young Jae Sim](https://github.com/Hanul)
