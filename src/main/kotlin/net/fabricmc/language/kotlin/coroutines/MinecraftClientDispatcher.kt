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

package net.fabricmc.language.kotlin.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.Runnable
import net.minecraft.client.MinecraftClient
import kotlin.coroutines.CoroutineContext

private inline val mc get() = MinecraftClient.getInstance()

@Suppress("unused")
val Dispatchers.Minecraft: MinecraftClientDispatcher
    get() = MinecraftClientDispatcher

@OptIn(InternalCoroutinesApi::class)
object MinecraftClientDispatcher : MainCoroutineDispatcher() {
    override val immediate: MainCoroutineDispatcher get() = this

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        mc.execute(block)
    }

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return !mc.isOnThread
    }

    override fun toString(): String = toStringInternalImpl() ?: "MinecraftClient"
}
