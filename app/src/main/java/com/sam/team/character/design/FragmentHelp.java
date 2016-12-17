package com.sam.team.character.design;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.sam.team.character.R;

/**
 * Created by pborisenko on 11/27/2016.
 */

public class FragmentHelp extends Fragment {
    private static final String TAG = "FragmentHelp";

    private Button mBtnGotIt;
    private Button mBtnContactUs;
    private WebView webView;
    private WebViewClient webViewClient;
    private String url;
    private ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Boolean initialLoad = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, null);

        webView = (WebView) view.findViewById(R.id.webview);
        webViewClient = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (initialLoad) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (initialLoad) {
                    progressBar.setVisibility(View.GONE);
                    initialLoad = false;
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        };
        webView.setWebViewClient(webViewClient);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(url);
                if (!initialLoad) {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            }
        });

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.help_fragment_title);

        int index = getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1;
        String name;
        if (index < 0) {
            // customer came from the first screen
            name = ActivityContainer.FragmentType.SYSTEM_PICKER.name();
        } else {
            name = getActivity().getSupportFragmentManager().getBackStackEntryAt(index).getName();
        }
        Log.d(TAG, "Show help for " + name);

        switch (name) {
            case "SYSTEM_PICKER": {
                url = getResources().getString(R.string.url_SYSTEM_PICKER);
                break;
            }
            case "EDIT_ELEMENT": {
                url = getResources().getString(R.string.url_EDIT_ELEMENT);
                break;
            }
            case "EDIT_FIELD": {
                url = getResources().getString(R.string.url_EDIT_FIELD);
                break;
            }
        }

        webView.loadUrl(url);

        mBtnGotIt = (Button) view.findViewById(R.id.btn_got_it);
        mBtnContactUs = (Button) view.findViewById(R.id.btn_contact_us);

        mBtnGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // return
                getActivity().onBackPressed();
            }
        });

        mBtnContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Letter to Support");
                String uriText =
                        "mailto:" + getResources().getString(R.string.support_email_address) +
                                "?subject=" +
                                Uri.encode(getResources().getString(R.string.support_email_subject)) +
                                "&body=" + Uri.encode(getSystemInfo());
                Uri uri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                startActivity(Intent.createChooser(sendIntent,
                        getResources().getString(R.string.support_email_dialog_title)));
            }
        });

        return view;
    }

    public String getSystemInfo() {
        String s="\n" + getResources().getString(R.string.support_email_body_title) + ":";
        s += "\n";
        s += "\nOS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        s += "\nOS API Level: " + android.os.Build.VERSION.SDK_INT;
        s += "\nDevice: " + android.os.Build.DEVICE;
        s += "\nModel (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";
        return s;
    }
}
