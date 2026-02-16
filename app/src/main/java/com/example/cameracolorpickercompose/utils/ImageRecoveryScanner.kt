//package com.example.cameracolorpickercompose
//
//import android.content.Context
//
//import android.os.Build
//
//import android.os.Environment
//
//import android.provider.MediaStore
//
//import android.util.Log
//
//import com.example.filerecoveryadvanced.core.callbacks.ScanCallback
//
//import com.example.filerecoveryadvanced.domain.RecoveredImage
//
//import kotlinx.coroutines.*
//
//import java.io.File
//
//import java.io.FileInputStream
//
//class ImageRecoveryScanner(private val context: Context) {
//
//    companion object {
//
//        private const val TAG = "ImageRecoveryScanner"
//
//        private const val MIN_FILE_SIZE = 1024L
//
//        private const val MAX_FILE_SIZE = 100 * 1024 * 1024L
//
//        private val IMAGE_EXTENSIONS = setOf(
//
//            "jpg", "jpeg", "png", "gif", "bmp", "webp", "heic", "heif"
//
//        )
//
//    }
//
//    private var scanJob: Job? = null
//
//    private val visibleImagePaths = mutableSetOf<String>()
//
//    fun scanForRecoverableImages(callback: ScanCallback) {
//
//        scanJob = CoroutineScope(Dispatchers.IO).launch {
//
//            try {
//
//                callback.onScanStarted()
//
//                loadVisibleImagePaths()
//
//                Log.d(TAG, "Loaded ${visibleImagePaths.size} visible images from MediaStore")
//
//                val allImageFiles = mutableListOf<File>()
//
//                val rootDirs = getRootScanDirectories()
//
//                var scannedCount = 0
//
//                val totalDirs = rootDirs.size
//
//                for ((index, dir) in rootDirs.withIndex()) {
//
//                    if (scanJob?.isCancelled == true) break
//
//                    callback.onProgress(
//
//                        (index * 100 / totalDirs).coerceAtMost(90),
//
//                        "Scanning: ${dir.absolutePath}",
//
//                        0
//
//                    )
//
//                    if (dir.exists() && dir.canRead()) {
//
//                        findImageFilesRecursive(dir, allImageFiles)
//
//                    }
//
//                    scannedCount++
//
//                }
//
//                val recoverableImages = allImageFiles
//
//                    .filter { file ->
//
//                        val absPath = file.absolutePath
//
//                        file.length() in MIN_FILE_SIZE..MAX_FILE_SIZE &&
//
//                                !visibleImagePaths.contains(absPath) &&
//
//                                isValidImageFile(file)
//
//                    }
//
//                    .mapNotNull { file ->
//
//                        createRecoveredImage(file, isDeleted = true)
//
//                    }
//
//                callback.onProgress(100, "Scan completed", recoverableImages.size)
//
//                recoverableImages.forEach { callback.onImageFound(it) }
//
//                callback.onScanCompleted(recoverableImages)
//
//                Log.d(TAG, "Found ${recoverableImages.size} recoverable (deleted/hidden) images")
//
//            } catch (e: Exception) {
//
//                Log.e(TAG, "Scan error", e)
//
//                callback.onError("Scan failed: ${e.message}")
//
//            }
//
//        }
//
//    }
//
//    private suspend fun loadVisibleImagePaths() {
//
//        withContext(Dispatchers.IO) {
//
//            visibleImagePaths.clear()
//
//            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//
//                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
//
//            } else {
//
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//
//            }
//
//            val projection = arrayOf(MediaStore.Images.Media.DATA)
//
//            val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//
//                "${MediaStore.Images.Media.IS_PENDING} = 0 AND ${MediaStore.Images.Media.IS_TRASHED} = 0"
//
//            } else null
//
//            try {
//
//                context.contentResolver.query(uri, projection, selection, null, null)
//                    ?.use { cursor ->
//
//                        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//
//                        while (cursor.moveToNext()) {
//
//                            val path = cursor.getString(columnIndex)
//
//                            if (path != null) visibleImagePaths.add(path)
//
//                        }
//
//                    }
//
//            } catch (e: Exception) {
//
//                Log.w(TAG, "Failed to query MediaStore", e)
//
//            }
//
//        }
//
//    }
//
//    private fun getRootScanDirectories(): List<File> {
//
//        val primary = Environment.getExternalStorageDirectory()
//
//        val dirs = mutableListOf<File>()
//
//        dirs.add(primary)
//
//        listOf(
//            "DCIM",
//            "Pictures",
//            "Screenshots",
//            "Download",
//            "WhatsApp/Media/WhatsApp Images"
//        ).forEach {
//
//            val dir = File(primary, it)
//
//            if (dir.exists()) dirs.add(dir)
//
//        }
//
//        listOf(
//
//            ".Trash", ".trash", ".bin",
//
//            "DCIM/.trashed", "Pictures/.trashed",
//
//            "Android/data/com.sec.android.gallery3d/files/.trash",
//
//            "Android/data/com.google.android.apps.photos/files/trash"
//
//        ).forEach {
//
//            val trashDir = File(primary, it)
//
//            if (trashDir.exists() && trashDir.canRead()) dirs.add(trashDir)
//
//        }
//
//        return dirs.distinct()
//
//    }
//
//    private fun findImageFilesRecursive(directory: File, result: MutableList<File>) {
//
//        try {
//
//            directory.listFiles()?.forEach { file ->
//
//                if (scanJob?.isCancelled == true) return
//
//                if (file.isDirectory) {
//
//                    if (shouldSkipDirectory(file)) return@forEach
//
//                    findImageFilesRecursive(file, result)
//
//                } else if (isPotentialImageFile(file)) {
//
//                    result.add(file)
//
//                }
//
//            }
//
//        } catch (e: Exception) {
//
//            Log.w(TAG, "Access denied: ${directory.absolutePath}")
//
//        }
//
//    }
//
//    private fun shouldSkipDirectory(dir: File): Boolean {
//
//        val path = dir.absolutePath.lowercase()
//
//        val blocked = listOf("/android/data/", "/android/obb/", ".cache", "cache", "temp")
//
//        return blocked.any { path.contains(it) } || dir.name.startsWith(".")
//
//    }
//
//    private fun isPotentialImageFile(file: File): Boolean {
//
//        val name = file.name.lowercase()
//
//        return IMAGE_EXTENSIONS.any { name.endsWith(it) }
//
//    }
//
//    private fun isValidImageFile(file: File): Boolean {
//
//        return try {
//
//            FileInputStream(file).use { stream ->
//
//                val buffer = ByteArray(12)
//
//                val read = stream.read(buffer)
//
//                if (read < 4) return false
//
//                when {
//
//                    buffer.startsWith(
//                        byteArrayOf(
//                            0xFF.toByte(),
//                            0xD8.toByte(),
//                            0xFF.toByte()
//                        )
//                    ) -> true // JPEG
//
//                    buffer.startsWith(
//                        byteArrayOf(
//                            0x89.toByte(),
//                            0x50.toByte(),
//                            0x4E.toByte(),
//                            0x47.toByte()
//                        )
//                    ) -> true // PNG
//
//                    buffer.startsWith(
//                        byteArrayOf(
//                            0x47.toByte(),
//                            0x49.toByte(),
//                            0x46.toByte()
//                        )
//                    ) -> true // GIF
//
//                    buffer.startsWith(byteArrayOf(0x42.toByte(), 0x4D.toByte())) -> true // BMP
//
//                    buffer.startsWith(
//                        byteArrayOf(
//                            0x52.toByte(),
//                            0x49.toByte(),
//                            0x46.toByte(),
//                            0x46.toByte()
//                        )
//                    ) -> true // WEBP/RIFF
//
//                    else -> true // Accept others if extension matches
//
//                }
//
//            }
//
//        } catch (e: Exception) {
//
//            false
//
//        }
//
//    }
//
//    private fun createRecoveredImage(file: File, isDeleted: Boolean): RecoveredImage? {
//
//        return try {
//
//            val thumbnail = extractThumbnail(file)
//
//            RecoveredImage(
//
//                path = file.absolutePath,
//
//                size = file.length(),
//
//                originalName = file.name,
//
//                signature = byteArrayOf(),
//
//                isDeleted = isDeleted,
//
//                thumbnail = thumbnail
//
//            )
//
//        } catch (e: Exception) {
//
//            null
//
//        }
//
//    }
//
//    private fun extractThumbnail(file: File): ByteArray? {
//
//        return try {
//
//            FileInputStream(file).use { stream ->
//
//                val buffer = ByteArray(2048.coerceAtMost(file.length().toInt()))
//
//                val read = stream.read(buffer)
//
//                if (read > 0) buffer.copyOf(read) else null
//
//            }
//
//        } catch (e: Exception) {
//
//            null
//
//        }
//
//    }
//
//    private fun ByteArray.startsWith(prefix: ByteArray): Boolean {
//
//        if (size < prefix.size) return false
//
//        for (i in prefix.indices) {
//
//            if (this[i] != prefix[i]) return false
//
//        }
//
//        return true
//
//    }
//
//    fun cancelScan() {
//
//        scanJob?.cancel()
//
//        scanJob = null
//
//        Log.d(TAG, "Scan cancelled")
//
//    }
//
//    fun isScanning(): Boolean = scanJob?.isActive == true
//}
//
//data class RecoveredImage(
//    val path: String,
//    val size: Long,
//    val originalName: String? = null,
//    val signature: ByteArray,
//    val isDeleted: Boolean = true,
//    val thumbnail: ByteArray? = null
//) {
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//        return path == (other as RecoveredImage).path
//    }
//
//    override fun hashCode(): Int {
//        return path.hashCode()
//    }
//}
//
//interface ScanCallback {
//    fun onScanStarted()
//    fun onProgress(progress: Int, currentPath: String, foundCount: Int)
//    fun onImageFound(image: RecoveredImage)
//    fun onScanCompleted(images: List<RecoveredImage>)
//    fun onError(error: String)
//}