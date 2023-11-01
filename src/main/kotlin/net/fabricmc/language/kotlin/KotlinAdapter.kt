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

import net.fabricmc.loader.api.LanguageAdapter
import net.fabricmc.loader.api.LanguageAdapterException
import net.fabricmc.loader.api.ModContainer
import java.lang.invoke.MethodHandleProxies
import java.lang.invoke.MethodHandles
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.jvmErasure

open class KotlinAdapter : LanguageAdapter {
    override fun <T : Any> create(mod: ModContainer, value: String, type: Class<T>): T {
        val methodSplit = value.split("::").dropLastWhile { it.isEmpty() }.toTypedArray()
        val methodSplitSize = methodSplit.size
        if (methodSplitSize >= 3) {
            throw LanguageAdapterException("Invalid handle format: $value")
        }

        val c: Class<Any> = try {
            Class.forName(methodSplit[0]) as Class<Any>
        } catch (e: ClassNotFoundException) {
            throw LanguageAdapterException(e)
        }
        val k = c.kotlin

        when (methodSplit.size) {
            1 -> {
                return if (type.isAssignableFrom(c)) {
                    // try to return the objectInstance first
                    @Suppress("UNCHECKED_CAST")
                    k.objectInstance as? T
                        ?: try {
                            k.createInstance() as T
                        } catch (e: Exception) {
                            throw LanguageAdapterException(e)
                        }
                } else {
                    throw LanguageAdapterException("Class " + c.name + " cannot be cast to " + type.name + "!")
                }
            }
            2 -> {
                val instance = k.objectInstance ?: run {
                    // In Kotlin 1.9.20 objectInstance can return null.
                    // See: https://github.com/FabricMC/fabric-language-kotlin/issues/120
                    return LanguageAdapter.getDefault().create(mod, value, type)
                }

                val methodList = instance::class.memberFunctions.filter { m ->
                    m.name == methodSplit[1]
                }

                k.declaredMemberProperties.find {
                    it.name == methodSplit[1]
                }?.let { field ->
                    try {
                        val fType = field.returnType

                        if (methodList.isNotEmpty()) {
                            throw LanguageAdapterException("Ambiguous $value - refers to both field and method!")
                        }

                        if (!type.kotlin.isSuperclassOf(fType.jvmErasure)) {
                            throw LanguageAdapterException("Field " + value + " cannot be cast to " + type.name + "!")
                        }

                        return field.get(instance) as T
                    } catch (e: NoSuchFieldException) {
                        // ignore
                    } catch (e: IllegalAccessException) {
                        throw LanguageAdapterException("Field $value cannot be accessed!", e)
                    }
                }

                if (!type.isInterface) {
                    throw LanguageAdapterException("Cannot proxy method " + value + " to non-interface type " + type.name + "!")
                }

                if (methodList.isEmpty()) {
                    throw LanguageAdapterException("Could not find $value!")
                } else if (methodList.size >= 2) {
                    throw LanguageAdapterException("Found multiple method entries of name $value!")
                }

                val handle = try {
                    MethodHandles.lookup()
                        .unreflect(methodList[0].javaMethod)
                        .bindTo(instance)
                } catch (ex: Exception) {
                    throw LanguageAdapterException("Failed to create method handle for $value!", ex)
                }

                try {
                    return MethodHandleProxies.asInterfaceInstance(type, handle)
                } catch (ex: Exception) {
                    throw LanguageAdapterException(ex)
                }
            }
            else -> throw LanguageAdapterException("Invalid handle format: $value")
        }
    }
}