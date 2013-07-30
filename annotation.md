---
layout: default
title: Regions - Annotations
---
# Annotations
Each feature is built up by a set of annotations on Methods and fields. Method annotations will determine what each methods does, either firing on ticks, how often they fire, or if they get fired on Events.

### @RegionEvent
* Type: Method Annotation
* Description: Used to mark a method to be fired on an Event

RegionEvents are automatically registered with Spout at start up.

To use a RegionEvent you just need to use the following format:

{% highlight java %}
    @RegionEvent
    public execute(PlayerChatEvent event, EventRegion region) {
        // Do something on Player Chat
    }
{% endhighlight %}

If a player chats while in the region, this code will get executed.

### @OnTick
* Type: Method Annotation
* Description: Used to mark a method to be fired on every Server Tick (20 times a second)

OnTick is used in conjuntion with the Tickable interface (not spout server, but Regions interface). Without the Tickable interface `implements Tickable` the class will not even be looked at by the tick processor.

OnTick can also be used with the optional Intensity Enum `@OnTick(load = Intensity.HIGHEST)` this will limit the method to how overloaded the server is. The intensities from from lowest to highest and rate the intensity of the method, so if the method takes alot of processing power rate it higher.
* Lowest: Will not run if server TPS is 8 or lower
* Highest: Will not run if server TPS is 16 or lower

{% highlight java %}
    @OnTick(load = Intensity.LOW)
    public void someTask(float dt) {
        // Do Something on Tick, will not run if TPS is 8 or lower
    }
{% endhighlight %}

### @FeatureCommand
* Type: Method Annotation
* Description: Used to mark a method as a Feature Command
* Requires: alias (@FeatureCommand(alias = "name"))
* Can be used with @FearureCommandPermission

A Feature command is a method that can be called from within game.

{% highlight java %}
    @FeatureCommand(alias = "test")
    @FearureCommandPermission("test")
    public void commandTest(FeatureCommandArgs args) {
        // Do some command based on permission "raz.feature.{regionname}.test"
    }
{% endhighlight %}

### @FearureCommandPermission
* Type: Method Annotation
* Description: Used to mark a method as a Feature Command
* Requires: 
** Permission String (@FeatureCommandPermission("name"))
** @FeatureCommand annotation

FeatureCommandPermission adds permissions to feature commands, the Permission String by default is appended with `raz.feature.` so a player will need the full permission of `raz.feature.name` to use a feature command and will also need to be within the region to use it.

FeatureCommandPermission uses a Overridable method of hasPermission method, so you can change how permissions are handled on a Feature by Feature basis.

{% highlight java %}
    @FeatureCommand(alias = "test")
    @FearureCommandPermission("test")
    public void commandTest(FeatureCommandArgs args) {
        // Do some command based on permission "raz.feature.{regionname}.test"
    }
{% endhighlight %}
