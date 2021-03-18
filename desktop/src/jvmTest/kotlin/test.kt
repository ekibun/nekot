import kotlin.test.Test

class Test {
    @Test
    fun testHightlight() {
        print(Hightlight.hightlight("hello (`你好 \${ 434} world` + `233`) { const a =1; //comment \n }"))
    }
}
