package com.github.shadowsocks.plugin

import com.github.shadowsocks.App.Companion.app

object NoPlugin : Plugin() {
    override val id: String get() = ""
    override val label: CharSequence get() = app.getText(com.perqin.shadowsocksq.R.string.plugin_disabled)
}
