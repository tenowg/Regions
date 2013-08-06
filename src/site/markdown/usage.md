## Usage
### Plugin Development
Regions is designed from the bottom up to be developer friendly.

Plugins create their features by creating a class that extends Feature

<div class="source">
<pre class="java"><code>public class InRegion extends Feature implements Tickable {
    // Tickable is an optional Decorator that tells Regions to check
    // this class on every tick to see if something needs to be done.
}</code></pre>
</div>

Then each method and field of the class can be annotated with any Region Annotation

<div class="source">
<pre class="java"><code>// Field and Method Annotations
@Data
public String somethingtosave;

@RegionEvent
@EventOrder(Order.EARLY)
public void execute(PlayerQuitEvent event, EventRegion region) {
    // Do something when a player quits while in region.
    // EventOrder tells the Spout EventManager to run the event early.
}</code></pre>
</div>

Then simply register the feature with Regions.

<div class="source"><pre class="java"><code>RegionAPI.registerFeature(this, InRegion.class);</code></pre></div>

Each plugin should control adding and removing features to regions, this can easily be done.

<div class="source">
<pre class="java"><code>Region region = world.get(WorldRegionComponent.class).get(region);
region.add(Feature.class);</code></pre>
</div>

Which should be controlled with commands or actions from each plugin with help from the RegionAPI static methods.

<div class="source">
<pre class="java"><code>RegionAPI to be expanded on.</code></pre>
</div>

### Special Events
Most events will be pre-configured to work with Regions, and will be automatically registered when a feature is registered with Regions.

Sometimes, however, some developers like to create their own Custom Events. And because it is nearly impossible to guess what every developer is going to need, I have made it possible to use Custom Events with Regions with only a minor amount of extra code.

All that needs to done is to `extends EventParser` and add a parser for your Custom Event that returns either `WorldPoint` or `WorldUUID`. Once this is done and returns the right information, everything is ready to be registered with Regions.

<div class="source">
<pre class="java"><code>public class CustomEventParser extends EventParser {
    // Custom Events
    public WorldPoint parse(EnterRegionEvent event) {
        WorldPoint wp = new WorldPoint();
        wp.setWorld(event.getPlayer().getWorld());
        wp.setLoc(event.getPlayer().getPhysics().getPosition());
        return wp;
    }
	
    public WorldUUID parse(LeaveRegionEvent event) {
        WorldUUID wp = new WorldUUID();
        wp.setWorld(event.getPlayer().getWorld());
        wp.setUUID(event.getRegion().getUUID());
        return wp;
    }
}</code></pre>
</div>

After you create the EventParser for your Custom Event you will just need to pass it when you register a Feature that uses that Event.

<div class="source">
<pre class="java"><code>RegionAPI.registerFeature(this, InRegion.class, new CustomEventParser());</code></pre>
</div>

Currently Regions supports 90% of all Spout Server events, and will have support to handle Vanilla Events in the future.
