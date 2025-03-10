/*
 * Copyright (C) 2021 Zac Sweers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.zacsweers.moshix.ir.gradle

import dev.zacsweers.moshi.ir.gradle.VERSION
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

class MoshiGradleSubplugin : KotlinCompilerPluginSupportPlugin {

  override fun apply(target: Project) {
    target.extensions.create("moshi", MoshiPluginExtension::class.java)
  }

  override fun getCompilerPluginId(): String = "moshi-compiler-plugin"

  override fun getPluginArtifact(): SubpluginArtifact =
      SubpluginArtifact(
          groupId = "dev.zacsweers.moshix", artifactId = "moshi-compiler-plugin", version = VERSION)

  override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

  override fun applyToCompilation(
      kotlinCompilation: KotlinCompilation<*>
  ): Provider<List<SubpluginOption>> {
    val project = kotlinCompilation.target.project
    val extension = project.extensions.getByType(MoshiPluginExtension::class.java)
    val generatedAnnotation = extension.generatedAnnotation.orNull

    // Minimum Moshi version
    project.dependencies.add("api", "com.squareup.moshi:moshi:1.13.0")

    val enableSealed = extension.enableSealed.get()
    if (enableSealed) {
      project.dependencies.add("api", "dev.zacsweers.moshix:moshi-sealed-runtime:$VERSION")
    }

    return project.provider {
      buildList {
        add(SubpluginOption(key = "enabled", value = extension.enabled.get().toString()))
        add(SubpluginOption(key = "enableSealed", value = enableSealed.toString()))
        if (generatedAnnotation != null) {
          add(SubpluginOption(key = "generatedAnnotation", value = generatedAnnotation))
        }
      }
    }
  }
}
