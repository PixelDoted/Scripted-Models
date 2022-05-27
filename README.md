# Scripted Models

an advanced model editing mod for Minecraft
using scripts to render vertices, particles, and visually move the player model

right now Scripted Models is really only used for it's features and speed (compared to Pixel's Character Models)  
eventually Pixel's Character Models will use Scripted Models for model editing  
but for now Pixel's Character Models is used for simplicity and Scripted Models is used for features and speed

Scripted Models has examples on how to use it
[here](https://github.com/PixelDoted/Scripted-Models/tree/master/examples)

download the ".txt" files to ".minecraft/ScriptedModels"  
then select them in the GUI by pressing "R"

## Source

path: src/main/java/me/pixeldots/scriptedmodels  
the folder "platform" is modloader and/or version(forge) specific code  
the folder "script" compiles and runs the inputted script

## Script Commands

```
vertex <x> <y> <z> <normal x> <normal y> <normal z> <u> <v> <red> <green> <blue> <alpha> // creates a vertex
translate <x> <y> <z> // visualy moves an entity/modelpart
scale <x> <y> <z> // visualy scales an entity/modelpart
rotate <x> <y> <z> // visualy rotates an entity/modelpart
particle <particle type> <x> <y> <z> <velocity x> <velocity y> <velocity z> // creates a particle
cancel // cancels the script (hiding the modelpart/entity running this script)

v -> vertex
p -> particle
```