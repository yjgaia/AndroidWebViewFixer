package co.hanul.studio.AndroidWebViewFixer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class AndroidWebViewFixer {

	private static ValueCallback<Uri> uploadMessage;

	private final static int FILE_CHOOSER_RESULT_CODE = 1;

	public static void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == FILE_CHOOSER_RESULT_CODE && uploadMessage != null) {
			Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
			uploadMessage.onReceiveValue(result);
			uploadMessage = null;
		}
	}

	public static boolean onKeyDown(WebView webView, int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return false;
	}

	@SuppressLint({ "NewApi", "SetJavaScriptEnabled", "SdCardPath" })
	public static void fix(final Activity activity, WebView webView, final ProgressBar progressBar, final String host, final String facebookRedirectURL) {

		// settings
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);

		webView.getSettings().setDatabaseEnabled(true);
		webView.getSettings().setDatabasePath("/data/data/" + webView.getContext().getPackageName() + "/databases/");

		webView.getSettings().setPluginsEnabled(true);
		webView.getSettings().setPluginState(PluginState.ON);

		// clients
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				if (url.startsWith("https://m.facebook.com/v2.0/dialog/oauth?redirect")) {
					view.loadUrl(facebookRedirectURL == null ? host : facebookRedirectURL);
				} else if (!url.startsWith("https://m.facebook.com/") && !url.startsWith(host) && (url.startsWith("http:") || url.startsWith("https:") || url.startsWith("mailto:") || url.startsWith("tel:") || url.startsWith("sms:"))) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					activity.startActivity(browserIntent);
					return true;
				} else {
					view.loadUrl(url);
				}

				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				CookieSyncManager.getInstance().sync();
			}
		});

		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int progress) {

				if (progressBar != null) {
					if (progress < 100) {
						progressBar.setVisibility(ProgressBar.VISIBLE);
					} else if (progress == 100) {
						progressBar.setVisibility(ProgressBar.GONE);
					}
					progressBar.setProgress(progress);
				}
			}

			// for Android < 3.0
			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				uploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
				activity.startActivityForResult(Intent.createChooser(i, "Choose a File"), FILE_CHOOSER_RESULT_CODE);
			}

			// for Android 3.0+
			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
				openFileChooser(uploadMsg);
			}

			// for Android 4.1+
			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
				openFileChooser(uploadMsg);
			}
		});
	}

	public static void fix(final Activity activity, WebView webView, final String host, final String facebookRedirectURL) {
		fix(activity, webView, null, host, facebookRedirectURL);
	}

	public static void fix(final Activity activity, WebView webView, final ProgressBar progressBar, final String host) {
		fix(activity, webView, progressBar, host, null);
	}

	public static void fix(final Activity activity, WebView webView, final String host) {
		fix(activity, webView, host, null);
	}
}
