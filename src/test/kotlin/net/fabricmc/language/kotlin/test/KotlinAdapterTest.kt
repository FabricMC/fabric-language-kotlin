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

package net.fabricmc.language.kotlin.test

import net.fabricmc.api.ModInitializer
import net.fabricmc.language.kotlin.KotlinAdapter
import net.fabricmc.loader.api.FabricLoader
import kotlin.test.Test

class KotlinAdapterTest {
    @Test
    fun classEntrypoint() {
        testEntrypoint("net.fabricmc.language.kotlin.test.entrypoints.ClassEntrypoint")
    }

    @Test
    fun objectClassEntrypoint() {
        testEntrypoint("net.fabricmc.language.kotlin.test.entrypoints.ObjectClassEntrypoint")
    }

    @Test
    fun objectFunctionEntrypoint() {
        testEntrypoint("net.fabricmc.language.kotlin.test.entrypoints.ObjectFunctionEntrypoint::init")
    }

    @Test
    fun objectFieldEntrypoint() {
        testEntrypoint("net.fabricmc.language.kotlin.test.entrypoints.ObjectFieldEntrypoint::initializer")
    }

    @Test
    fun companionClassEntrypoint() {
        testEntrypoint("net.fabricmc.language.kotlin.test.entrypoints.CompanionClassEntrypoint\$Companion")
    }

    @Test
    fun companionFunctionEntrypoint() {
        testEntrypoint("net.fabricmc.language.kotlin.test.entrypoints.CompanionFunctionEntrypoint\$Companion::init")
    }

    @Test
    fun companionFieldEntrypoint() {
        testEntrypoint("net.fabricmc.language.kotlin.test.entrypoints.CompanionFieldEntrypoint\$Companion::initializer")
    }

    @Test
    fun topLevelEntrypoint() {
        testEntrypoint("net.fabricmc.language.kotlin.test.entrypoints.TopLevelEntrypointKt::init")
    }

    private fun testEntrypoint(value: String) {
        FabricLoader.getInstance().objectShare.remove("fabric-language-kotlin:test")

        val modContainer = FabricLoader.getInstance().getModContainer("fabric-language-kotlin").get()
        val entrypoint = KotlinAdapter().create(modContainer, value, ModInitializer::class.java)
        entrypoint.onInitialize()

        assert(FabricLoader.getInstance().objectShare.get("fabric-language-kotlin:test") == "true")
    }
}