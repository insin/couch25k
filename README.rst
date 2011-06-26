========
couch25k
========

A Java MIDlet which tracks workout intervals for the `Couch-to-5k`_
running plan.

My initial attempts at searching for something which implemented the
above for my bog-standard mobile phone indicated that it would likely be
easier to write it than find it, so I started doing just that.

Display will be tweaked for my Samsung S3100, depending on what it does
when I load this thing up.

Implemented
===========

* Selection of Week and Workout.
* Display of current workout step (Walk or Jog) with status and timing
  for the step and workout.
* Workouts can be paused and resumed.
* Plays a sound and vibrates when changing interval.

Planned
=======

*(In order of usefulness descending/superfluousness ascending)*

* Tracking of workout completion data.
* Animate a sprite indicating what the current step is.

Building
========

Built using `Eclipse`_, the `MTJ plugin`_ and the `Java ME SDK 3.0`_.

I had to manually remove midp_1.0.jar and cldc_1.0.jar from the phone
configuration MTJ picked up from the Java ME SDK in order to successfully
compile using more recent features, but your mileage may vary.

.. _`Couch-to-5k`: http://www.coolrunning.com/engine/2/2_3/181.shtml
.. _`Eclipse`: http://www.eclipse.org
.. _`MTJ plugin`: http://www.eclipse.org/mtj/
.. _`Java ME SDK 3.0`: http://www.oracle.com/technetwork/java/javame/javamobile/download/overview/index.html
