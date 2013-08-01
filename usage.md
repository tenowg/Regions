---
layout: default
title: Regions - Usage
---

### Plugin Development
Regions is designed from the bottom up to be developer friendly.

Plugins create their features by creating a class that extends Feature

{% highlight java %}
public class InRegion extends Feature implements Tickable {
    // Tickable is an optional Decorator that tells Regions to check
    // this class on every tick to see if something needs to be done.
}
{% endhighlight %}

Then each method and field of the class can be annotated with any Region Annotation

{% highlight java %}
@Some
public String somethingtosave;

@RegionEvent
@EventOrder(Order.EARLY)
public void execute(PlayerQuitEvent event, EventRegion region) {
    // Do something when a player quits while in region.
    // EventOrder tells the Spout EventManager to run the event early.
}
{% endhighlight %}
