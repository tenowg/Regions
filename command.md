## Direct Region Commands
There are only a few basic commands available to Regions, Regions was designed to be extended and used by other plugins. With that in mind most commands either focus on Features, or manual Admin activitiy, most of these commands should not be give to regular users.

### ADDFEATURE
> Permission: **regions.command.addfeature** <br />
> Usage: **/region addfeature plugin feature**
>
> This will attempt to add a feature to a region you have selected.

### CREATE
> Permission: **regions.command.create** <br />
> Usage: **/region create (name)**
>
> This command will be used after you create a new region `/region new`. If a region is new and not saved yet, use this command to save it.

### LIST
> Permission: **regions.command.list** <br />
> Usage: **/region list**
>
> This will list all the regions in the current world.

### LISTFEATURES
> Permission: **regions.command.listfeatures** <br />
> Usage: **/region listfeatures**
>
> This will list all available features in the game.

### NEW
> Permission: **regions.command.new** <br />
> Usage: **/region new**
>
> This will create a new region and place it into the selected region in player memory, this region is not yet saved and has no impact on the world yet, use `/region create (name)` to save the region.

### POINTS
> Permission: **regions.command.points** <br />
> Usage: **/region points**
>
> This will list all the points that need to be set in a Region while editing.

### POS
> Permission: **regions.command.position** <br />
> Usage: **/region pos ONE**
>
> Select the Position point of a volume.
>
> Every Volume type can come with several different positions available to be set, and likely each one will be required to be set before the region can be saved/updated.

### UPDATE
> Permission: **regions.command.update** <br />
> Usage: **/region update**
>
> After a region has been selected `/region select` and edited this command will force the update of the region.

### SELECT
> Permission: **regions.command.select** <br />
> Usage: **/region select (name)**
> Todo: Allow a select to be used with the name argument, if player is in one region, it is selected.
>
> This will select a region to be edited or commands to be used against.

### SET
> Permission: **regions.command.set** <br />
> Usage: **/region set (feature) (commands) [args]**
>
> This is used to execute a command on a feature that you have selected. It is intended to set a variable or config, but can be used for anything that can be coded into a @FeatureCommand

### SETTYPE
> Permission: **regions.command.settype** <br />
> Usage: **/region settype (type)**
>
> This will set the type of a new/selected region that is being edited, once this is set you will need to reset all positions using `/regions pos POS`, you can get a list of all types with `/region types`

### TYPES
> Permission: **regions.command.types** <br />
> Usage: **/region types**
>
> This will list the available Volume types that can be used by Regions.

### REGION
> Permission: **regions.command.remove** <br />
> Usage: **/region remove (name)**
> Todo: Remove selected Region from game.
>
> This will remove a region from the game.

## Managing Features

As Admin you should be able to have complete control over the features and effects that each region has, so there are some basic commands that can remove/add and modify all features on all regions.
