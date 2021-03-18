import androidx.compose.foundation.text.appendInlineContent

class Quickjs {
  private var _rt: Long? = null
  private var _ctx: Long? = null
  // private var _objectClassId: Long?

  private fun _ensureEngine() {
    if (_rt != null) return
    val rt = QuickjsJni.jsNewRuntime()
    _rt = rt
    val ctx = QuickjsJni.jsNewContext(rt)
    _ctx = ctx
    // _objectClassId = jsNewClass(ctx, 'JavaObject')
  }

  fun close() {
    val rt = _rt
    val ctx = _ctx
    _rt = null
    _ctx = null
    if (ctx != null) QuickjsJni.jsFreeContext(ctx)
    if (rt == null) return
    QuickjsJni.jsFreeRuntime(rt)
  }

  fun evaluate(input: String, filename: String, evalFlags: Int): String {
    _ensureEngine()
    val ctx = _ctx!!
    val v = QuickjsJni.jsEval(ctx, input, filename, evalFlags)
    val ret = QuickjsJni.jsToString(ctx, v)
    QuickjsJni.jsFreeValue(ctx, v)
    return ret
  }

  companion object {
    val INSTANCE by lazy { Quickjs() }
  }
}

object QuickjsJni {
  external fun jsNewRuntime(): Long
  external fun jsNewContext(rt: Long): Long
  // private external fun jsNewClass(ctx: Long, name: String): Long
  external fun jsEval(ctx: Long, input: String, filename: String, evalFlags: Int): Long
  external fun jsFreeValue(ctx: Long, v: Long, free: Boolean = true)
  external fun jsToString(ctx: Long, v: Long): String
  external fun jsFreeContext(ctx: Long)
  external fun jsFreeRuntime(rt: Long)
  external fun jsIsIdentFirst(c: Int): Boolean
  external fun jsIsIdentNext(c: Int): Boolean

  init {
    jniLoadLibrary("quickjs")
  }
}
