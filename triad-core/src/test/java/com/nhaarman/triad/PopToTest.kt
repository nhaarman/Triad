package com.nhaarman.triad

import com.nhaarman.expect.expect
import org.junit.Test

class PopToTest {

    lateinit var triad: Triad

    @Test
    fun A_popToA_isA() {
        /* Given */
        triad = triad(a)

        /* When */
        triad.popTo(A())

        /* Then */
        expectBackstackToBe(a)
        expectLastScreenToBe(a)
    }

    @Test
    fun AB_popToA_isA() {
        /* Given */
        triad = triad(a, b)

        /* When */
        triad.popTo(A())

        /* Then */
        expectBackstackToBe(a)
        expectLastScreenToBe(a)
    }

    @Test
    fun A_popToB_isB() {
        /* Given */
        triad = triad(a)

        /* When */
        triad.popTo(b)

        /* Then */
        expectBackstackToBe( b)
        expectLastScreenToBe(b)
    }

    @Test
    fun ABC_popToB_isAB() {
        /* Given */
        triad = triad(a, b, c)

        /* When */
        triad.popTo(B())

        /* Then */
        expectBackstackToBe(a, b)
        expectLastScreenToBe(b)
    }

    @Test
    fun ABC_popToC_isABC() {
        /* Given */
        triad = triad(a, b, c)

        /* When */
        triad.popTo(C())

        /* Then */
        expectBackstackToBe(a, b, c)
        expectLastScreenToBe(c)
    }

    @Test
    fun ABC_popToBC_isAB() {
        /* Given */
        triad = triad(a, b, c)

        /* When */
        triad.popTo(B(), C())

        /* Then */
        expectBackstackToBe(a, b)
        expectLastScreenToBe(b)
    }

    @Test
    fun AB_popToBC_isAB() {
        /* Given */
        triad = triad(a, b)

        /* When */
        triad.popTo(B(), C())

        /* Then */
        expectBackstackToBe(a, b)
        expectLastScreenToBe(b)
    }

    @Test
    fun A_popToBC_isB() {
        /* Given */
        triad = triad(a)

        /* When */
        triad.popTo(b, C())

        /* Then */
        expectBackstackToBe(b)
        expectLastScreenToBe(b)
    }

    @Test
    fun ABC_popToCB_isABC() {
        /* Given */
        triad = triad(a, b, c)

        /* When */
        triad.popTo(C(), B())

        /* Then */
        expectBackstackToBe(a, b, c)
        expectLastScreenToBe(c)
    }

    @Test
    fun AB_popToCB_isAC() {
        /* Given */
        triad = triad(a, b)

        /* When */
        triad.popTo(c, B())

        /* Then */
        expectBackstackToBe(a, c)
        expectLastScreenToBe(c)
    }

    @Test
    fun A_popToCB_isC() {
        /* Given */
        triad = triad(a)

        /* When */
        triad.popTo(c, B())

        /* Then */
        expectBackstackToBe(c)
        expectLastScreenToBe(c)
    }

    @Test
    fun ABCDE_popToBC_isABC() {
        /* Given */
        triad = triad(a, b, c, d, e)

        /* When */
        triad.popTo(B(), C())

        /* Then */
        expectBackstackToBe(a, b, c)
        expectLastScreenToBe(c)
    }

    @Test
    fun ABCDE_popToCB_isABC() {
        /* Given */
        triad = triad(a, b, c, d, e)

        /* When */
        triad.popTo(C(), B())

        /* Then */
        expectBackstackToBe(a, b, c)
        expectLastScreenToBe(c)
    }

    @Test
    fun ADE_popToBC_isADB() {
        /* Given */
        triad = triad(a, d, e)

        /* When */
        triad.popTo(b, C())

        /* Then */
        expectBackstackToBe(a, d, b)
        expectLastScreenToBe(b)
    }

    @Test
    fun ADE_popToCB_isADC() {
        /* Given */
        triad = triad(a, d, e)

        /* When */
        triad.popTo(c, B())

        /* Then */
        expectBackstackToBe(a, d, c)
        expectLastScreenToBe(c)
    }

    @Test
    fun ADCBE_popToCB_isADCB() {
        /* Given */
        triad = triad(a, d, c, b, e)

        /* When */
        triad.popTo(C(), B())

        /* Then */
        expectBackstackToBe(a, d, c, b)
        expectLastScreenToBe(b)
    }

    private fun expectLastScreenToBe(screen: SimpleScreen) {
        expect(triad.backstack.current<Any>()?.screen).toBe(screen)
    }

    private fun expectBackstackToBe(vararg screen: SimpleScreen) {
        val backstack = triad.backstack
        val screens = screen.asList()

        expect(backstack.size()).toBe(screens.size)

        var i = 0
        backstack.reverseIterator().forEach { screen ->
            expect(screen.javaClass).toBe(screens[i].javaClass)
            i++
        }
    }
}


private fun triad(vararg screen: SimpleScreen) = TriadFactory.newInstance(Backstack.of(*screen), object : Triad.Listener<Any> {
    override fun screenPushed(pushedScreen: Screen<Any>) {
    }

    override fun screenPopped(poppedScreen: Screen<Any>) {
    }

    override fun forward(newScreen: Screen<Any>, animator: TransitionAnimator?, onComplete: () -> Unit) {
        onComplete()
    }

    override fun backward(newScreen: Screen<Any>, animator: TransitionAnimator?, onComplete: () -> Unit) {
        onComplete()
    }

    override fun replace(newScreen: Screen<Any>, animator: TransitionAnimator?, onComplete: () -> Unit) {
        onComplete()
    }

})

private val a = A()
private val b = B()
private val c = C()
private val d = D()
private val e = E()

private class A : SimpleScreen() { override fun toString() = "A"}
private class B : SimpleScreen() { override fun toString() = "B"}
private class C : SimpleScreen() { override fun toString() = "C"}
private class D : SimpleScreen() { override fun toString() = "D"}
private class E : SimpleScreen() { override fun toString() = "E"}

private abstract class SimpleScreen : Screen<Any>() {
    override val layoutResId: Int
        get() = throw UnsupportedOperationException()

    override fun createPresenter(viewId: Int): Presenter<*, *> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}