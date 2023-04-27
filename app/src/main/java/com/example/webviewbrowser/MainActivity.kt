package com.example.webviewbrowser

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import java.nio.file.WatchEvent


class MainActivity : AppCompatActivity() {

    private lateinit var urlField: EditText
    private lateinit var searchButton: ImageButton
    private lateinit var webViewField: WebView

    private var mPermissionRequest: PermissionRequest? = null

//    private val REQUEST_CAMERA_PERMISSION = 1
//    private val PERM_CAMERA = arrayOf<String>(Manifest.permission.CAMERA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_main)

        webViewField = findViewById(R.id.web_view_field)
        urlField = findViewById<EditText>(R.id.url_field)
//        searchButton = findViewById<ImageButton>(R.id.src_btn)
        urlField.addTextChangedListener(object : TextWatcher {


            override fun afterTextChanged(s: Editable) {
//                loadURL()
//                urlField.visibility = View.INVISIBLE
//                searchButton.visibility = View.VISIBLE

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                loadURL()
            }
        })
//        searchButton.setOnClickListener {
//            searchButton.visibility = View.INVISIBLE
//            urlField.visibility = View.VISIBLE
//        }

    }
    private fun loadURL() {
        val url = urlField.text.toString()
        webViewField.webViewClient = WebViewClient()

        webViewField.apply {
            loadUrl(url)
            settings.javaScriptEnabled = true
            settings.allowFileAccess = true
            settings.databaseEnabled = true
            settings.allowContentAccess =true
            settings.domStorageEnabled = true
            settings.builtInZoomControls = false
            settings.cacheMode = WebSettings.LOAD_DEFAULT
            settings.javaScriptCanOpenWindowsAutomatically = true}
             webViewField.webChromeClient = object : WebChromeClient() {
            // Grant permissions for cam
            override fun onPermissionRequest(request: PermissionRequest) {
                Log.i(TAG, "onPermissionRequest")
                mPermissionRequest = request
                val requestedResources = request.resources
                for (r in requestedResources) {
                    if (r == PermissionRequest.RESOURCE_VIDEO_CAPTURE) {
                        // In this sample, we only accept video capture request.
                        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                            .setTitle("Allow Permission to camera")
                            .setPositiveButton("Allow",
                                DialogInterface.OnClickListener { dialog, which ->
                                    dialog.dismiss()
                                    mPermissionRequest!!.grant(arrayOf<String>(PermissionRequest.RESOURCE_VIDEO_CAPTURE))
                                    Log.d(TAG, "Granted")
                                })
                            .setNegativeButton("Deny",
                                DialogInterface.OnClickListener { dialog, which ->
                                    dialog.dismiss()
                                    mPermissionRequest!!.deny()
                                    Log.d(TAG, "Denied")
                                })
                        val alertDialog: AlertDialog = alertDialogBuilder.create()
                        alertDialog.show()
                        break
                    }
                }
            }

            override fun onPermissionRequestCanceled(request: PermissionRequest) {
                super.onPermissionRequestCanceled(request)
                Toast.makeText(this@MainActivity, "Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }


}