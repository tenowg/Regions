---
layout: default
title: Regions - Annotations
---
# Annotations
Each feature is built up by a set of annotations on Methods and fields. Method annotations will determine what each methods does, either firing on ticks, how often they fire, or if they get fired on Events.

## Event Annotations
These will likely give your regions the most life, as any Event (Event custom events, with come extra code) can be used within any Feature.

### @RegionEvent
* Type: Method Annotation
* Description: Used to mark a method to be fired on an Event

RegionEvents are automatically registered with Spout at start up.

To use a RegionEvent you just need to use the following format:

    @RegionEvent
    public execute(PlayerChatEvent event, EventRegion region) {
        // Do something on Player Chat
    }

If a player chats while in the region, this code will get executed.