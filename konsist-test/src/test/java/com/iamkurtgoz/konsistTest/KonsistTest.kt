/*
 * Copyright 2024 Sporthor Android
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
package com.iamkurtgoz.konsistTest

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import com.iamkurtgoz.core.common.common.useCase.IUseCase
import com.iamkurtgoz.core.common.common.useCase.IUseCaseWithoutParams
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.core.useCase.CoreUseCase
import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.declaration.KoClassDeclaration
import com.lemonappdev.konsist.api.declaration.KoFileDeclaration
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.ext.list.withParent
import com.lemonappdev.konsist.api.verify.assertTrue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Qualifier
import kotlin.test.Test
import kotlin.test.assertTrue

class KonsistTest {

    // ViewModel
    @Test
    fun `classes extending 'ViewModel' should have 'ViewModel' suffix`() {
        Konsist.scopeFromProject()
            .classes()
            .filter { koInterface ->
                koInterface.hasParentOf(ViewModel::class) || koInterface.hasParentOf(CoreViewModel::class)
            }
            .assertTrue {
                it.name.endsWith("ViewModel")
            }
    }

    // UseCase
    @Test
    fun `interfaces extending IUseCase or IUseCaseWithoutParams have UseCase suffix`() {
        Konsist.scopeFromProject()
            .interfaces()
            .filter { koInterface ->
                koInterface.hasParentOf(IUseCase::class) || koInterface.hasParentOf(IUseCaseWithoutParams::class)
            }
            .assertTrue {
                it.name.endsWith("UseCase")
            }
    }

    @Test
    fun `classes with 'UseCase' or 'UseCaseImpl' suffix should reside in 'useCase' package`() {
        Konsist.scopeFromProject()
            .classes()
            .withNameEndingWith("UseCase", "UseCaseImpl")
            .assertTrue {
                it.resideInPackage("..useCase..")
            }
    }

    @Test
    fun `classes extending 'CoreUseCase' should have 'UseCaseImpl' suffix`() {
        Konsist.scopeFromProject()
            .classes()
            .filter { koInterface ->
                koInterface.hasParentOf(CoreUseCase::class) || koInterface.hasParentOf(IUseCase::class) || koInterface.hasParentOf(IUseCaseWithoutParams::class)
            }
            .assertTrue {
                if (it.name == "UserActionControllerImpl") return@assertTrue true
                it.name.endsWith("UseCaseImpl")
            }
    }

    // Repository
    @Test
    fun `classes implementing interfaces ending with 'Repository' should end with 'RepositoryImpl'`() {
        Konsist.scopeFromProject()
            .classes()
            .withParent {
                it.name.endsWith("Repository")
            }
            .assertTrue {
                it.name.endsWith("RepositoryImpl")
            }
    }

    @Test
    fun `classes with 'Repository' or 'RepositoryImpl' suffix should reside in 'repository' package`() {
        Konsist.scopeFromProject()
            .classes()
            .withNameEndingWith("Repository", "RepositoryImpl")
            .assertTrue {
                it.resideInPackage("..repository..") || it.resideInPackage("..impl..") || it.resideInPackage("..core..")
            }
    }

    // Data Source
    @Test
    fun `classes implementing interfaces ending with 'DataSource' should end with 'DataSourceImpl'`() {
        Konsist.scopeFromProject()
            .classes()
            .withParent {
                it.name.endsWith("DataSource")
            }
            .assertTrue {
                it.name.endsWith("DataSourceImpl")
            }
    }

    @Test
    fun `classes implementing interfaces ending with 'DataSource' have only suspend functions`() {
        Konsist.scopeFromProject()
            .classes()
            .withParent {
                it.name.endsWith("DataSource")
            }
            .assertTrue { koInterface ->
                koInterface
                    .functions()
                    .all { it.hasSuspendModifier }
            }
    }

    @Test
    fun `classes with 'DataSource' or 'DataSourceImpl' suffix should reside in 'dataSource' package`() {
        Konsist.scopeFromProject()
            .classes()
            .withNameEndingWith("DataSource", "DataSourceImpl")
            .filterNot { it.hasAnnotationOf(Qualifier::class) }
            .assertTrue {
                it.resideInPackage("..dataSource..") || it.resideInPackage("..core..")
            }
    }

    // Api Service
    @Test
    fun `retrofit services must be interface, annotated with @Keep, and have only suspend functions`() {
        Konsist.scopeFromProject()
            .interfaces()
            .withNameEndingWith("Service")
            .assertTrue { koInterface ->
                val hasKeepAnnotation = koInterface.hasAnnotationOf(Keep::class)
                val allFunctionsAreSuspend = koInterface
                    .functions()
                    .all { it.hasSuspendModifier }

                hasKeepAnnotation && allFunctionsAreSuspend
            }
    }

    // Response Model
    @Test
    fun `response models must have Keep, Serializable annotations and all properties must have SerialName, val, and nullable`() {
        Konsist.scopeFromProject()
            .classes()
            .filter { it.hasDataModifier && it.name.endsWith("ResponseModel") }
            .assertTrue { koClass ->
                val hasKeepAnnotation = koClass.hasAnnotationOf(Keep::class)
                val hasSerializableAnnotation = koClass.hasAnnotationOf(Serializable::class)
                val allPropertiesValid = koClass.properties()
                    .all { property ->
                        val hasSerializedName = property.hasAnnotationOf(SerialName::class)
                        val isVal = property.isVal
                        val isNullable = property.type?.isNullable == true
                        hasSerializedName && isVal && isNullable
                    }

                hasKeepAnnotation && hasSerializableAnnotation && allPropertiesValid
            }
    }

    // Domain Model
    @Test
    fun `domain models all properties must have val, and nullable`() {
        Konsist.scopeFromProject()
            .classes()
            .filter { it.hasDataModifier && it.name.endsWith("DomainModel") }
            .assertTrue { koClass ->
                val allPropertiesValid = koClass.properties()
                    .all { property ->
                        val isVal = property.isVal
                        val isNullable = property.type?.isNullable == true
                        isVal && isNullable
                    }

                allPropertiesValid
            }
    }

    // UI Model
    /*
    @Test
    fun `ui models all properties must have val, and nullable`() {
        Konsist.scopeFromProject()
            .classes()
            .filter { it.hasDataModifier && it.name.endsWith("UIModel") }
            .assertTrue { koClass ->
                val allPropertiesValid = koClass
                    .properties()
                    .all { property ->
                        val isVal = property.isVal
                        val isNullable = property.type?.isNullable == true

                        // If variable not in constructor, it can be null safe.
                        if (!property.isConstructorDefined) {
                            isVal
                        } else {
                            isVal && isNullable
                        }
                    }

                allPropertiesValid
            }
    }
     */

    // Navigation Route Model Test
    @Test
    fun `navigation module must contain only allowed file names`() {
        val navigationFiles = Konsist
            .scopeFromModule("core/navigation")
            .files
            .filter { !it.name.endsWith("Test") }
            .filter { !it.name.endsWith("NavExtensions") }

        navigationFiles.assertTrue { file: KoFileDeclaration ->
            println(file.name)
            val name = file.name
            name.endsWith("ScreenRoute") ||
                name.endsWith("NavRoute") ||
                name.endsWith("ScreenNavigateModel") ||
                name.endsWith("NavigateBackWithResultModel") ||
                name == "CustomNavType"
        }
    }

    @Test
    fun `classes ending with ScreenRoute must follow the rules`() {
        val classes = Konsist
            .scopeFromModule("core/navigation")
            .classes()
            .filter { it.name.endsWith("ScreenRoute") }

        classes.assertTrue { koClass: KoClassDeclaration ->
            println("Girdi -> ${koClass.name}")
            val isDataClass = koClass.hasDataModifier
            val isEnum = koClass.hasEnumModifier

            if (!isDataClass && !isEnum) return@assertTrue false

            val hasKeepAnnotation = koClass.hasAnnotationOf(Keep::class)
            val hasSerializableAnnotation = koClass.hasAnnotationOf(Serializable::class)
            if (!hasKeepAnnotation || !hasSerializableAnnotation) return@assertTrue false

            if (isDataClass) {
                val primaryConstructor = koClass.primaryConstructor
                val parameters = primaryConstructor?.parameters

                if (parameters != null) {
                    val modelParam = parameters.first()
                    if (modelParam.type.isNullable) return@assertTrue false
                }
            }

            println("Çıktı -> ${koClass.name}")
            true
        }
    }

    @Test
    fun `classes ending with ScreenNavigateModel must follow the rules`() {
        val classes = Konsist
            .scopeFromModule("core/navigation")
            .classes()
            .filter { it.name.endsWith("ScreenNavigateModel") }

        classes.assertTrue { koClass: KoClassDeclaration ->
            val isSealedClass = koClass.hasSealedModifier
            val isDataClass = koClass.hasDataModifier
            val isEnum = koClass.hasEnumModifier
            val name = koClass.name
            println(name)

            if (!isSealedClass && !isDataClass && !isEnum) return@assertTrue false

            val hasKeepAnnotation = koClass.hasAnnotationOf(Keep::class)
            val hasSerializableAnnotation = koClass.hasAnnotationOf(Serializable::class)
            if (!hasKeepAnnotation || !hasSerializableAnnotation) return@assertTrue false

            if (isDataClass) {
                val primaryConstructor = koClass.primaryConstructor
                val parameters = primaryConstructor?.parameters
                if (parameters.isNullOrEmpty()) {
                    return@assertTrue false
                }
            }
            true
        }
    }

    @Test
    fun `classes ending with NavRoute must be a normal class with Keep and Serializable`() {
        val classes = Konsist
            .scopeFromModule("core/navigation")
            .classes()
            .filter { it.name.endsWith("NavRoute") }

        classes.assertTrue { koClass: KoClassDeclaration ->
            if (koClass.hasDataModifier) return@assertTrue false
            if (koClass.hasEnumModifier) return@assertTrue false
            if (koClass.hasInterfaces()) return@assertTrue false

            val hasKeepAnnotation = koClass.hasAnnotationOf(Keep::class)
            val hasSerializableAnnotation = koClass.hasAnnotationOf(Serializable::class)

            if (!hasKeepAnnotation || !hasSerializableAnnotation) return@assertTrue false

            true
        }
    }

    @Test
    fun `CustomNavType file should exist and follow rules`() {
        val customNavTypeClasses = Konsist
            .scopeFromModule("core/navigation")
            .classes()
            .filter { it.name == "CustomNavType" }

        assertTrue("CustomNavType is not found!") {
            customNavTypeClasses.isNotEmpty()
        }
    }
}
