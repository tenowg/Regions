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
* Description: User to mark a method to be fired on every Server Tick (20 times a second)

OnTick is used in conjuntion with the Tickable interface (not spout server, but Regions interface). Without the Tickable interface `implements Tickable` the class will not even be looked at by the tick processor.

OnTick can also be used with the optional Intensity Enum `@OnTick(load = Intensity.HIGHEST)` this will limit the method to how overloaded the server is. The intensities from from lowest to highest and rate the intensity of the method, so if the method takes alot of processing power rate it higher.
* Lowest: Will not run if server TPS is 8 or lower
* Highest: Will not run if server TPS is 16 or lower