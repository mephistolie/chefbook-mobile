package io.chefbook.libs.crypto.digest

import kotlin.test.Test
import kotlin.test.assertEquals

class DigestTest {

  @Test
  fun digestSHA1() {
    val input = "test"
    val expectedOutput = "a94a8fe5ccb19ba61c4c0873d391e987982fbbd3"

    assertEquals(expectedOutput, input.sha1String)
  }

  @Test
  fun digestSHA256() {
    val input = "test"
    val expectedOutput = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"

    assertEquals(expectedOutput, input.sha256String)
  }
}
