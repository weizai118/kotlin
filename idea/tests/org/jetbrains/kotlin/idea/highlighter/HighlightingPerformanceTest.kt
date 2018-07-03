/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.highlighter

import com.intellij.psi.PsiManager
import com.intellij.psi.impl.PsiModificationTrackerImpl
import com.intellij.testFramework.LightProjectDescriptor
import com.intellij.testFramework.PlatformTestUtil
import org.jetbrains.kotlin.idea.test.KotlinLightCodeInsightFixtureTestCase
import org.jetbrains.kotlin.idea.test.KotlinWithJdkAndRuntimeLightProjectDescriptor

class HighlightingPerformanceTest : KotlinLightCodeInsightFixtureTestCase() {
    private val text = """
        fun test(a: String, b: ArrayList<java.util.Date>) {
            println(a + b)
        }
    """.trimIndent()

    fun testModificationCountInc() {
        myFixture.configureByText("temp.kt", text)
        myFixture.checkHighlighting(true, false, true)

        val tracker = PsiManager.getInstance(myFixture.project).modificationTracker as PsiModificationTrackerImpl

        PlatformTestUtil.assertTiming("Update local cache", 55) {
            tracker.modificationCount.inc()
            myFixture.checkHighlighting(true, false, true)
        }
    }

    fun testOutOfBlockModificationCountInc() {
        myFixture.configureByText("temp.kt", text)
        myFixture.checkHighlighting(true, false, true)

        val tracker = PsiManager.getInstance(myFixture.project).modificationTracker as PsiModificationTrackerImpl

        PlatformTestUtil.assertTiming("Update all trackers", 165) {
            tracker.incOutOfCodeBlockModificationCounter()
            myFixture.checkHighlighting(true, false, true)
        }
    }

    override fun getProjectDescriptor(): LightProjectDescriptor = KotlinWithJdkAndRuntimeLightProjectDescriptor.INSTANCE_FULL_JDK
}