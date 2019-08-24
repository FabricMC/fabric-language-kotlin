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

import net.fabricmc.loader.language.LanguageAdapter
import org.apache.logging.log4j.LogManager

private val Logger = LogManager.getLogger("Fabric-Language-Kotlin")

class KotlinLanguageAdapter : LanguageAdapter {
    override fun createInstance(clazz: Class<*>, options: LanguageAdapter.Options): Any {
        Logger.warn("$clazz is using a deprecated language adapter, support for this will be dropped in a future update. Update $clazz.")
        return clazz.kotlin.objectInstance ?: run {
            System.err.println("Unable to find Kotlin object instance for ${clazz.name}, constructing new instance")
            clazz.newInstance()
        }
    }
}