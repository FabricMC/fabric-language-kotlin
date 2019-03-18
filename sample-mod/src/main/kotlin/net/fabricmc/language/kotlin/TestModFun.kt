/*
 * Copyright 2016 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.language.kotlin

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.logging.log4j.LogManager

object TestModFun {

    val logger = LogManager.getFormatterLogger("KotlinLanguageTest")

    fun init() = runBlocking {
        logger.info("**************************")
        logger.info("Hello from Kotlin TestModFun")
        logger.info("**************************")

        // TODO: figure out how to make the logger actually display the coroutine debug info
        val prev = System.setProperty("kotlinx.coroutines.debug", "")
        logger.debug("'kotlinx.coroutines.debug' prev: $prev")

        // look we can do coroutines
        val channel = Channel<Int>()
        launch(CoroutineName("printer")) {
            for (k in channel) {
                logger.info("received: $k")
            }
        }
        launch(CoroutineName("sender")) {
            for (i in (0 until 10)) {
                delay(100)
                channel.send(i)
            }
            channel.close()
        }
        logger.info("done")
    }
}