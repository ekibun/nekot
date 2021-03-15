import kotlin.test.Test

class Test {
    @Test
    fun testQuickjs() {
        val qjs = Quickjs()
        print(qjs.hightlight("hello (`hello \${ 434} world` + `233`) { const a =1; //comment \n }"))
        qjs.close();
    }
}
