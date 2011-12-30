========
couch25k
========

A Java MIDlet which tracks workout intervals for the `Couch-to-5k`_
running plan.

My initial attempts at searching for something which implemented the
above for my bog-standard mobile phone indicated that it would likely be
easier to write it than find it, so I started doing just that.

Display and audio may be tweaked specifically for my Samsung S3100,
depending on what it does when I load this thing up.

Implemented
===========

*Features which are implemented, in implementation order.*

* Selection of Week and Workout.
* Display of current workout step (Walk or Jog) with status and timing
  for the step and overall workout.
* Workouts can be paused and resumed.
* Plays a sound and vibrates when changing interval.
* Stores completion status and date for each workout.
* Quick Start jumps to first incomplete workout.
* Tweet completion of workouts via SMS - SMS number and message are
  configurable. You must register your phone number with your Twitter
  account if you want to make use of this feature.
* Optionally plays a minute marker sound - defaults to on.

Planned
=======

*(In order of usefulness descending/superfluousness ascending)*

* Animate a sprite indicating what the current step is.
* Some sort of victory animation on the finish screen.

In Limbo
========

* Tweeting via xAuth, identifying as a Twitter app.

  * This is currently implemented in the `twitter-oauth branch`_ but to
    make it publicly usable, I would need to beg permission for my Twitter
    app to use xAuth authentication via email O_o

  * If you want to use this feature, you need to register your own Twitter
    app, create your own ``TwitterKeysImpl`` class which implements the
    `TwitterKeys interface`_ and build your own .jad/.jar files.

.. _`twitter-oauth branch`: https://github.com/insin/couch25k/tree/twitter-oauth
.. _`TwitterKeys interface`: https://github.com/insin/couch25k/blob/twitter-oauth/src/couch25k/TwitterKeys.java

Building
========

Built using `Eclipse`_, the `MTJ plugin`_ and the `Java ME SDK 3.0`_ (or the
`Sun Java Wireless Toolkit 2.5.2_01 for CLDC`_ on Linux).

I had to manually remove midp_1.0.jar and cldc_1.0.jar from the phone
configuration MTJ picked up from the Java ME SDK in order to successfully
compile using more recent features, but your mileage may vary.

.. _`Couch-to-5k`: http://www.coolrunning.com/engine/2/2_3/181.shtml
.. _`Eclipse`: http://www.eclipse.org
.. _`MTJ plugin`: http://www.eclipse.org/mtj/
.. _`Java ME SDK 3.0`: http://www.oracle.com/technetwork/java/javame/javamobile/download/overview/index.html
.. _`Sun Java Wireless Toolkit 2.5.2_01 for CLDC`: http://www.oracle.com/technetwork/java/index-jsp-137162.html


MIT License
===========

Copyright (c) 2011, Jonathan Buchanan

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.