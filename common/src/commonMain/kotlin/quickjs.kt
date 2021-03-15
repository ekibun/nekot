import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle

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

  private fun parseTokenCurry(
      builder: AnnotatedString.Builder,
      strPtr: Long,
      ctx: Long,
      s: Long,
      endOnCurry: Boolean = true
  ) {
    var tok = 0
    val addAnnotated = {
      val tokStart = (QuickjsJni.jsGetParseStateTokenPtr(s) - strPtr).toInt()
      val tokEnd = (QuickjsJni.jsGetParseStateBufPtr(s) - strPtr).toInt()
      when(tok) {
        QuickjsJni.TOK_NUMBER-> {
          SpanStyle(color = Color.Cyan)
        }
        QuickjsJni.TOK_STRING, QuickjsJni.TOK_TEMPLATE -> {
          SpanStyle(color = Color.Magenta)
        }
        else -> if (tok > QuickjsJni.TOK_EOF && tok < 0){
          SpanStyle(color = Color.Blue)
        } else null
      }?.let {
        builder.addStyle(it, tokStart, tokEnd)
      }
    }
    do {
      tok = QuickjsJni.jsParseNextToken(ctx, s)
      if (tok == QuickjsJni.TOK_ERROR) {
        QuickjsJni.jsSetParseStateBufPtr(s, QuickjsJni.jsGetParseStateBufPtr(s) + 1)
        continue
      }
      if (endOnCurry && tok == '}'.toInt()) break
      addAnnotated()
      if (tok == QuickjsJni.TOK_TEMPLATE || tok == '{'.toInt()) {
        parseTokenCurry(builder, strPtr, ctx, s)
        if (tok == QuickjsJni.TOK_TEMPLATE) {
          QuickjsJni.jsParseTemplatePart(s)
        } else {
          tok = '}'.toInt()
        }
        addAnnotated()
      }
    } while (tok != QuickjsJni.TOK_EOF && (!endOnCurry || tok != '}'.toInt()))
  }

  fun hightlight(input: String): AnnotatedString {
    _ensureEngine()
    val builder = AnnotatedString.Builder()
    val ctx = _ctx!!
    val strPtr = QuickjsJni.getStringPtr(input)
    val s = QuickjsJni.jsParseInit(ctx, strPtr, input.length)
    builder.append(input)
    parseTokenCurry(builder, strPtr, ctx, s, false)
    QuickjsJni.jsParseEnd(ctx, s)
    return builder.toAnnotatedString()
  }

  companion object {
    val INSTANCE by lazy { Quickjs() }
  }
}

private object QuickjsJni {
  const val TOK_NUMBER = -128
  const val TOK_STRING = -127
  const val TOK_TEMPLATE = -126
  const val TOK_EOF = -86
  const val TOK_ERROR = -88

  external fun getStringPtr(input: String): Long
  external fun jsParseInit(ctx: Long, input: Long, inputLen: Int): Long
  external fun jsParseEnd(ctx: Long, s: Long)
  external fun jsParseNextToken(ctx: Long, s: Long): Int
  external fun jsGetParseStateTokenPtr(s: Long): Long
  external fun jsGetParseStateBufPtr(s: Long): Long
  external fun jsSetParseStateBufPtr(s: Long, ptr: Long)
  external fun jsParseTemplatePart(s: Long): Int

  external fun jsNewRuntime(): Long
  external fun jsNewContext(rt: Long): Long
  // private external fun jsNewClass(ctx: Long, name: String): Long
  external fun jsEval(ctx: Long, input: String, filename: String, evalFlags: Int): Long
  external fun jsFreeValue(ctx: Long, v: Long, free: Boolean = true)
  external fun jsToString(ctx: Long, v: Long): String
  external fun jsFreeContext(ctx: Long)
  external fun jsFreeRuntime(rt: Long)

  init {
    jniLoadLibrary("quickjs")
  }
}
