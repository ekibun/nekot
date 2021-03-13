import kotlin.test.Test

class Test {
    @Test
    fun testQuickjs() {
        val qjs = Quickjs()
        print(qjs.evaluate("1+1", "<test>", 0))
        qjs.close();
    }
}
