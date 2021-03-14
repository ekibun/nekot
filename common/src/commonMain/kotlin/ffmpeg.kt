class Ffmpeg {
  external fun avformatAllocContext(): Long

  companion object {
    init {
      jniLoadLibrary("ffmpeg")
    }
  }
}