---
layout: default
title: Regions - Usage
---

### Plugin Development
Regions is designed from the bottom up to be developer friendly.

Plugins create their features by creating a class that extends Feature

codehere

Then each method/field of the class can be annotated with any Region Annotation

codehere

Then simply register the feature with Regions.

codehere

Each plugin should control adding and removing features to regions, this can easily be done.

codehere

Which should be controlled with commands/actions from each plugin with help from the RegionAPI static methods.

codehere

### Special Events
Most events will be pre-configured to work with Regions, and will be automatically registered when a feature is registered with Regions.

Sometimes, however, some developers like to create their own Custom Events. And because it is nearly impossible to guess what every developer is going to need, I have made it possible to use Custom Events with Regions with only a minor amount of extra code.

All that needs to done is to `extends EventParser` and add a parser for your Custom Event that returns either `oneclass` or `anotherclass`. Once this is done and returns the right information, everything is ready to be registered with Regions.

codehere

After you create the EventParser for your Custom Event you will just need to pass it when you register a Feature that uses that Event.

codehere

Currently Regions supports 90% of all Spout Server events, and will have support to handle Vanilla Events in the future