# Scripted Models

Scripted Models is currently in alpha  

## Source

path: src/main/java/me/pixeldots/scriptedmodels
the folder "platform" is modloader and/or version(forge) specific code
the folder "script" compiles and runs the inputted script

## Script Commands

```
vertex <x> <y> <z> <normal x> <normal y> <normal z> <u> <v> <red> <green> <blue> <alpha>
v <x> <y> <z> <normal x> <normal y> <normal z> <u> <v> <red> <green> <blue> <alpha>
translate <x> <y> <z>
scale <x> <y> <z>
rotate <x> <y> <z>
particle <particle type> <x> <y> <z> <velocity x> <velocity y> <velocity z>
p <particle type> <x> <y> <z> <velocity x> <velocity y> <velocity z>
```