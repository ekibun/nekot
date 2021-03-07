object Quickjs {
  external fun jsNewRuntime(): Long

  val rt by lazy { jsNewRuntime() }
  init {
    jniLoadLibrary("jniquickjs")
  }
}