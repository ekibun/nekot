class Quickjs {
  private var _rt: Long? = null
  private var _ctx: Long? = null
  // private var _objectClassId: Long?

  private fun _ensureEngine() {
    if (_rt != null) return
    val rt = jsNewRuntime()
    _rt = rt
    val ctx = jsNewContext(rt)
    _ctx = ctx
    // _objectClassId = jsNewClass(ctx, 'JavaObject')
  }

  fun close() {
    val rt = _rt
    val ctx = _ctx
    _rt = null
    _ctx = null
    if (ctx != null) jsFreeContext(ctx)
    if (rt == null) return
    jsFreeRuntime(rt)
  }

  fun evaluate(input: String, filename: String, evalFlags: Int): String {
    _ensureEngine()
    val ctx = _ctx!!
    val v = jsEval(ctx, input, filename, evalFlags)
    val ret = jsToString(ctx, v)
    jsFreeValue(ctx, v)
    return ret
  }

  private external fun jsNewRuntime(): Long
  private external fun jsNewContext(rt: Long): Long
  // private external fun jsNewClass(ctx: Long, name: String): Long
  private external fun jsEval(ctx: Long, input: String, filename: String, evalFlags: Int): Long
  private external fun jsFreeValue(ctx: Long, v: Long, free: Boolean = true)
  private external fun jsToString(ctx: Long, v: Long): String
  private external fun jsFreeContext(ctx: Long)
  private external fun jsFreeRuntime(rt: Long)

  // val rt by lazy { jsNewRuntime() }
  companion object {
    init {
      jniLoadLibrary("jniquickjs")
    }
  }
}
