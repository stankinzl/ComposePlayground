package com.stanislavkinzl.composeplayground.screens.xmlnavigationsample

import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import com.stanislavkinzl.composeplayground.R

class XmlNavigationComponentActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_xml_navigation)
    }
}