# Scripted Models

an advanced model editing library for Minecraft
using scripts to render vertices, particles, and visually move the player model

right now Scripted Models is really only used for it's features and speed (compared to Pixel's Character Models)  
eventually Pixel's Character Models will use Scripted Models for model editing  
but for now Pixel's Character Models is used for simplicity and Scripted Models is used for features and speed

if you would like to test out the Scripted Models version of Pixel's Character Models
you can find it in the ["dev" branch](https://github.com/PixelDoted/PixelsCharacterModels/tree/dev/PixelsCharacterModels-Fabric)

Scripted Models has examples on how to use it
[here](https://github.com/PixelDoted/Scripted-Models/tree/master/examples)

download the ".txt" files to ".minecraft/ScriptedModels"  
then select them in the GUI by pressing "R"

## Source

path: src/main/java/me/pixeldots/scriptedmodels  
the folder "platform" is modloader and/or version specific code  
the folder "script" compiles and runs the inputted script

## Script Commands

```
vertex <x> <y> <z> <normal x> <normal y> <normal z> <u> <v> <red> <green> <blue> <alpha> // creates a vertex
translate <x> <y> <z> // visualy moves an entity/modelpart
scale <x> <y> <z> // visualy scales an entity/modelpart
rotate <x> <y> <z> // visualy rotates an entity/modelpart
angle <pitch> <yaw> <roll> // rotates a modelpart used for animations (doesn't go back to the default angle after used)
particle <particle type> <x> <y> <z> <velocity x> <velocity y> <velocity z> // creates a particle
define // think of this as a comment but compiled with the script
cancel // cancels the script (hiding the modelpart/entity running this script)

v -> vertex
p -> particle
```

## Developers

add this to your build.gradle file to use Scripted Models
```
repositories {
    maven {
		name = "Modrinth"
		url = "https://api.modrinth.com/maven"
		content {
			includeGroup "maven.modrinth"
		}
	}
}

dependencies {
	modImplementation "maven.modrinth:scripted-models:${project.scripedmodels_version}"
}
```

"ClientHelper" has some useful functions for modifying an entity's scripts  
you can see the [GUI](https://github.com/PixelDoted/Scripted-Models/blob/master/examples/Example_Mod/src/main/java/me/pixeldots/scriptedmodelsgui/gui/ScriptedModelsGUI.java) for examples