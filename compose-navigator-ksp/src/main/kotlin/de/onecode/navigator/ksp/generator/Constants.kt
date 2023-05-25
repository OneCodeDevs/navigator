package de.onecode.navigator.ksp.generator

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName

internal const val PACKAGE = "io.redandroid.navigator"
internal const val NAVIGATOR_COMPOSABLE_NAME = "Navigator"
internal const val NAVIGATOR_CONTROLLER_COMPOSABLE_NAME = "NavigatorController"
internal const val REMEMBER_NAVIGATOR_CONTROLLER_NAME ="rememberNavigatorController"
internal const val SCREEN_BUILDER = "ScreenBuilder"
internal const val COMMON_CONTEXT = "CommonContext"
internal const val LOCAL_NAV_HOST_CONTROLLER = "LocalNavHostController"

internal val screenBuilderClass = ClassName(PACKAGE, SCREEN_BUILDER)
internal val navigatorControllerClass = ClassName(PACKAGE, NAVIGATOR_CONTROLLER_COMPOSABLE_NAME)
internal val composableClass = ClassName("androidx.compose.runtime", "Composable")
internal val composeAnnotation = AnnotationSpec.builder(composableClass).build()
internal val composeModifierClass = ClassName("androidx.compose.ui", "Modifier")
internal val navHostControllerClass = ClassName("androidx.navigation", "NavHostController")
internal val navBackStackEntryClass = ClassName("androidx.navigation", "NavBackStackEntry")
internal val navGraphBuilderClass = ClassName("androidx.navigation", "NavGraphBuilder")
internal val rememberName = MemberName("androidx.compose.runtime", "remember")
internal val rememberNavControllerName = MemberName("androidx.navigation.compose", "rememberNavController")
internal val mutableStateClass = ClassName("androidx.compose.runtime", "MutableState")
internal val stateClass = ClassName("androidx.compose.runtime", "State")
internal val mutableStateOfName = MemberName("androidx.compose.runtime", "mutableStateOf")
