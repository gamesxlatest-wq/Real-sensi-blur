package com.realsensi.app.shizuku

import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuRemoteProcess

/**
 * Optimizador nativo con Shizuku (vía depuración inalámbrica o ADB).
 * Ejecuta comandos de sistema sin requerir ROOT.
 */
object ShizukuOptimizer {

    fun isShizukuAvailable(): Boolean {
        return try {
            Shizuku.pingBinder() && Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED
        } catch (e: Exception) {
            false
        }
    }

    fun cleanRamAndTrimCaches(): Boolean {
        return executeShizukuCommand("pm trim-caches 999999999999 && am kill-all")
    }

    fun setGameDriverPriority(packageName: String = "com.dts.freefireth"): Boolean {
        return executeShizukuCommand("device_config put game_overlay $packageName mode=2,downscaleFactor=0.9")
    }

    fun setDpiDensity(dpi: Int): Boolean {
        return executeShizukuCommand("wm density $dpi")
    }

    private fun executeShizukuCommand(command: String): Boolean {
        return try {
            val process: ShizukuRemoteProcess = Shizuku.newProcess(arrayOf("sh", "-c", command), null, null)
            process.waitFor()
            process.exitValue() == 0
        } catch (e: Exception) {
            false
        }
    }
}