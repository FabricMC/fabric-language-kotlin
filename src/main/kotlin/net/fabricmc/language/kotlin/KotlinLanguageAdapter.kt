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

import net.fabricmc.base.loader.Init
import net.fabricmc.base.loader.language.ILanguageAdapter
import org.apache.logging.log4j.LogManager

class KotlinLanguageAdapter : ILanguageAdapter {

    private val logger = LogManager.getFormatterLogger("KotlinLanguageAdapter")

    override fun createModInstance(clazz: Class<*>): Any {
        try {
            val instanceField = clazz.getField("INSTANCE")
            val instance = instanceField.get(null) ?: throw NullPointerException()
            logger.debug("Found INSTANCE field for ${clazz.name}")
            return instance
        } catch (e: Exception) {
            logger.debug("Unable to find INSTANCE field for ${clazz.name}, constructing new instance")
            return clazz.newInstance()
        }
    }

    override fun callInitializationMethods(instance: Any) {
        instance.javaClass.declaredMethods.forEach {
            if (it.isAnnotationPresent(Init::class.java) && it.parameterCount == 0) {
                it.isAccessible = true
                it.invoke(instance)
            }
        }
    }

}