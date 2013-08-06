#### [Bootstrap themes from Bootswatch][themes]

[![Bootswatch themes](images/3d-cube-hd-wallpaper.jpg)][themes]

Select a free theme for your website from an excellent gallery at [Bootswatch][bootswatch].
Out of the box support for these and other custom [Bootstrap][bootstrap] themes.


#### [Bootstrap themes from Bootswatch][themes]

[![Bootswatch themes](images/3d-cube-hd-wallpaper.jpg)][themes]

Select a free theme for your website from an excellent gallery at [Bootswatch][bootswatch].
Out of the box support for these and other custom [Bootstrap][bootstrap] themes.


[themes]: skin/themes/


---


## Regions
### What is Regions
Regions is a simple way for developers to create Region/Areas/Zones for there plugin without the need to write
their own code to handle regions or area themselves.

Regions uses Annotations and basic classes to setup and control regions, each plugin can control their features with
ease. As a matter of fact Regions is ment to be control 100% by the plugins that extend it, from commands to automatons
everything is hidden from the player (the player thinks you wrote it!)

### What Regions is NOT
Regions is not WorldGuard, Regions was not intended to be a full world protection plugin. While it can be
emulated and could do everything WorldGuard does, it was ment to be a way to make `FULL CUSTOM` features to add to
regions, making each region a container for custom code.

### Some Feature/Region idea
A Healing region for characters that belong to a specific faction (heals every 5 seconds)

Prevent damage to players from other players based completely on what Components/level difference/residency

Force specific mobs to spawn (Bosses) that can only stay within that Region, while limiting the amount that
spawn in that region.

### What can be done
Features can have in-game commands that can work based of permissions. This was added mainly so that admins could set
settings on features without editing yml files or code.

Save settings, in the example of InRegion, the number of times people have chatted in the region is counted, and then saved
to be loaded next time.

Detectors, these are small pre-processing code to be run before methods (such as OnTick methods) to help gather dynamic
information to be used with in those methods (such as: what players are in the region, does this player have this skill, etc)

You can make you Regions come alive, any attribute that you can give a player can (with alittle code) be added to a region
(You could even give a region health! By writing a Health feature)