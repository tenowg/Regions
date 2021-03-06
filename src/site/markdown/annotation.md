# Annotations
Each feature is built up by a set of annotations on Methods and fields. Method annotations will determine what each methods does, either firing on ticks, how often they fire, or if they get fired on Events.

### @RegionEvent
* Type: Method Annotation
* Description: Used to mark a method to be fired on an Event

RegionEvents are automatically registered with Spout at start up.

To use a RegionEvent you just need to use the following format:

<div class="source"><pre class="java"><code>@RegionEvent
public execute(PlayerChatEvent event, EventRegion region) {
    // Do something on Player Chat
}
</code></pre></div>

If a player chats while in the region, this code will get executed.

### @OnTick
* Type: Method Annotation
* Description: Used to mark a method to be fired on every Server Tick (20 times a second)

OnTick is used in conjuntion with the Tickable interface (not spout server, but Regions interface). Without the Tickable interface `implements Tickable` the class will not even be looked at by the tick processor.

OnTick can also be used with the optional Intensity Enum `@OnTick(load = Intensity.HIGHEST)` this will limit the method to how overloaded the server is. The intensities from from lowest to highest and rate the intensity of the method, so if the method takes alot of processing power rate it higher.
* Lowest: Will not run if server TPS is 8 or lower
* Highest: Will not run if server TPS is 16 or lower

<div class="source"><pre class="java"><code>@OnTick(load = Intensity.LOW)
public void someTask(float dt) {
    // Do Something on Tick, will not run if TPS is 8 or lower
}
</code></pre></div>

### @FeatureCommand
* Type: Method Annotation
* Description: Used to mark a method as a Feature Command
* Requires: alias (@FeatureCommand(alias = "name"))
* Can be used with @FearureCommandPermission

A Feature command is a method that can be called from within game with `/region set featurename args...`. The region must first be selected with `/region select` command.

<div class="source"><pre class="java"><code>@FeatureCommand(alias = "test")
@FearureCommandPermission("test")
public void commandTest(FeatureCommandArgs args) {
    // Do some command based on permission "raz.feature.{regionname}.test"
}
</code></pre></div>

### @FeatureCommandPermission
* Type: Method Annotation
* Description: Used to mark a method as a Feature Command
* Requires: 
    * Permission String (@FeatureCommandPermission("name"))
    * @FeatureCommand annotation

FeatureCommandPermission adds permissions to feature commands, the Permission String by default is appended with `raz.feature.` so a player will need the full permission of `raz.feature.regionname.name` to use a feature command.

FeatureCommandPermission uses a Overridable method of hasPermission method, so you can change how permissions are handled on a Feature by Feature basis.

<div class="source"><pre class="java"><code>@FeatureCommand(alias = "test")
@FearureCommandPermission("test")
public void commandTest(FeatureCommandArgs args) {
    // Do some command based on permission "raz.feature.{regionname}.test"
}
</code></pre></div>

### @RegionDetector
* Type: Method Annotation
* Description: Used to mark a method that should pre-process information before execution.
* Requires: List of Detector classes to process.

This annotation will add and then process each Detector class that is attached to the @RegionDetector annoation.

<div class="source"><pre class="java"><code>@RegionDetector({PlayerInRegion.class, PlayerHasSkill.class})
@FeatureCommand(alias = "test")
public void commandTest(FeatureCommandArgs args) {
    // Do the command.
}
</code></pre></div>

In that example, it would generate two list which could then be called from the code in the feature with

<div class="source"><pre class="java"><code>get(PlayerInRegion.class).getPlayers();
get(PlayerHasSkill.class).getPlayers();
</code></pre></div>

The first being all the players in the Region, the second being a list of all the Players in the region that have a specific skill (at this point that would be coded into the detector). Configurations can be set within the code. As you can add a Detector manually in the onAttached() and the annotation will use that detector when it processes the annotation. If no Detector is added manually, the Annotation will add it for you.

### @Data
* Type: Field Annotation
* Description: Used to mark a public field in a feature to be saved.

<div class="source"><pre class="java"><code>// Standard (uses name of field in data files)
@Data
public String valuetosave;

// Named (uses specified name in data files to help with readability
@Data("name")
public String datatosave; // Will be saved as "name" in data files.
</code></pre></div>

## Not Implemented Yet Annotations

### @FeatureName("name")
* Type: Class Annotation
* Description: If you wish to use a different name for this feature in data files.

<div class="source"><pre class="java"><code>@FeatureName("name")
public class SomeFeature implements Feature { // will use "name" instead of "somefeature" in data files.
}
</code></pre></div>
