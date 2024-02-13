[![Build](https://github.com/OneCodeDevs/navigator/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/OneCodeDevs/navigator/actions/workflows/build.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.onecode/compass-api/badge.svg)](https://mvnrepository.com/search?q=de.onecode)

# Compass

This library offers a KSP API to generate boiler plate code for Jetpack Compose Navigation.
It makes allows for a type safe usage of the navigation between Composables and moves potential errors to the compile time.
This enables are more robust way of implementing an Android App using Jetpack Compose

## Setup

You can get Compass from Maven Central

Latest version: **1.1.0-SNAPSHOT**

```Gradle.kts
implementation("de.onecode:compass-api:$compass_version")
implementation("de.onecode:compass-runtime:$compass_version")
ksp("de.onecode:compass-ksp:$compass_version")
```

Used Kotlin and KSP versions

- 1.0.0: **1.8.21-1.0.11**
- 1.0.1: **1.8.21-1.0.11**
- 1.1.0: **1.9.22-1.0.16**

## Usage

1. Create either Objects or Classes that are annotated with *Destination*. They can either be bundled in one file or spread across multiple files
2. Make sure that one *Destination* is also annotated with *Home*. This Destination will be the entry point when you start the app.
3. Compile the app to let KSP generate/update all necessary code from the destination definitions.
4. A **Compass** is generated, which you can add to your main Activity/Composable
5. Check out all provided annotations under `API description`

```Kotlin
@Home
@Destination(name = "Overview")
@Navigation(to = EditorDescription::class)
object OverviewDescription

@Destination(name = "Editor")
@Parameter(name = "clickedItemId", type = Int::class)
object EditorDescription

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			AppNavGraph()
		}
	}
}

@Composable
fun AppNavGraph() {
	Compass {
		overviewScreen { // generated by KSP
			OverviewScreen( // your content composable for that destination
				onEdit = { clickedItemId ->
					navigateToEditor(clickedItemId) // 'navigateToEditor' is generated by KSP
				}
			)
		}

		editorScreen { // generated by KSP
			EditorScreen() // your content composable for that destination
		}
	}
}
```

## API description

The API of Compass consists of multiple annotations you can use to describe your apps navigation.
Annotated destinations can be separate across the app or put together in a single file.
Valid targets of all annotations are `object`, `class` or `interface`.

### @Destination

The `@Destination` annotation defines a screen added to a *Compose NavHost*.
You can use the annotation without parameters, in which case the generated code will use the name of the Object or class or you can provide a name in the `@Destination` annotation

**Definition**

```Kotlin
@Home
@Destination
object SimpleUi
```

**Generates**

```Kotlin
Compass {
	simpleUiScreen {
		// add your Composables here
	}
}
```

### @Navigation

`@Navigation` allows you to add routes to another destination. A destination can define as many navigation routes as it wants by simple adding multiple `@Navigation` annotations.
The target of a navigation has to be an Object or Class annotated with `@Desination` as well.

**Definition**

```Kotlin
@Home
@Destination
@Navigation(to = Second::class)
object First

@Destination
object Second
```

**Generates**

```Kotlin
Compass {
	firstScreen {
		navigateToSecond()  // generated
	}
	secondScreen {
		// add your Composables here
	}
}
```

### @Parameter

A destination can define parameters it wants to receive when navigated to. The defined parameters will be added to any generated navigation function, targeting the destination.

Supported parameter types are:

- Numbers, like `Int`, `Long`, `Double`, `Float`, etc.
- `String`

**Definition**

```Kotlin
@Home
@Destination
@Navigation(to = Second::class)
object First

@Destination
@Parameter(name = "someNumber", type = Int::class)
object Second
```

**Generates**

```Kotlin
Compass {
	firstScreen {
		navigateToSecond(someNumber = 10)  // generated with the specified parameter of destination Second
	}
	secondScreen {
		// add your Composables here
	}
}
```

### @Home

`@Home` marks a destination as the start destination of the navigation graph or the subgraph. It will be the first destination opened on start up.

### @Top

`@Top` marks a destination to be navigable from the root. This can be useful if your app has a bottom navigation in its root screen.
Navigation methods for `@Top` destinations are generated in the `CompoassController`. To access the `CompassController` use `rememberCompassController()`.
`@Top` annotations are only supported in the main graph, they are disallowed in a sub graph.
**Important**: Don't forget to pass it to the `Compass` again, to make sure the same instance is used by you and the `Compass`

### @SubGraph

In addition to a main graph which will generate the `Compass`, destinations can also be grouped together into a subgraph. The subgraph can be defined independently and later included into an
existing `Compass`. This is e.g. useful in multi module projects or for defining something like a wizard.

**Definition**

```Kotlin
@Home
@Destination
@Navigation(to = Second::class)
object FirstSub

@Destination
@Parameter(name = "someNumber", type = Int::class)
object SecondSub

@SubGraph(
	name = "Wizard",
	FirstSub::class,
	SecondSub::class
)
object Module

```

**Generates**

```Kotlin
wizardSubGraph {
	firstSubScreen {
		navigateToSecond(someNumber = 10)  // generated with the specified parameter of destination SecondSub
	}
	secondSubScreen {
		// add your Composables here
	}
}
```

`wizardSubGraph` is an extension function of `NavGraphBuilder` and can therefore be directly embedded into the `Compass` lambda.

## Home or no home

You can use Compass in two modes for code generation of `@Destination` (without sub graphs).
It is possible to use destinations in a module with and without a `@Home` navigation.
The API described so far was assuming the mode that always one destination is annotated with `@Home`.
This leads to generation of a **Compass** adding all the destinations the module to it.

You can also use a mode where no `@Home` destination is defined. In this case no Compass will be generated for you.
Instead an extension function to a *NavGraphBuilder* will be created, that can be used to manually add the destination to either a **Compass** or a **NavHost**.
This can be useful if you want to structure your destinations in modules without using a sub graph.

**Definition**

```Kotlin
@Destination
object NoHome
```

**Generates**

```Koltin
public fun NavGraphBuilder.noHomeScreen(composable: @Composable noHomeContext.() -> Unit){
    ...
}
```

**Usage in e.g. app**

````Kotlin
Compass { navGraphBuilder ->
	navGraphBuilder.noHomeScreen {
		// Composable
	}
}
````

You can see an example of this in the [destination-feature](demo/destination-feature/src/main/kotlin/de/onecode/navigator/demo/destinations) module

## Example

A detailed example can be found in the [demo app](demo/app/src/main/kotlin/de/onecode/navigator/demo) for putting a `Navigator` together
and [demo library](demo/wizard/src/main/kotlin/de/onecode/navigator/demo/wizard) for configuring a sub graph
