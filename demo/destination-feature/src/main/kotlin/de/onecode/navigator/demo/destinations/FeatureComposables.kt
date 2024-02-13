package de.onecode.navigator.demo.destinations

import androidx.navigation.NavGraphBuilder
import de.onecode.compass.featureComposableScreen

fun NavGraphBuilder.attachFeatureComposable() {
	featureComposableScreen {
		FeatureComposable(fromApp)
	}
}