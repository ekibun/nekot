actual fun getPlatformName(): String = "Android"

actual fun jniLoadLibrary(name: String) {
  System.loadLibrary(name)
}