========
couch25k
========

A Java MIDlet which tracks workout intervals for the `Couch-to-5k`_
running plan.

My initial attempts at searching for something which implemented the
above for my bog-standard mobile phone indicated that it would likely be
easier to write it than find it, so I started doing just that.

Built using `Eclipse`_, the `MTJ plugin`_ and the `Java ME SDK 3.0`_.

Display will be tweaked for my Samsung S3100, depending on what it does
when I load this thing up.

Implemented
===========

* Selection of Week and Workout.
* Display of current workout step (Walk or Jog) with status for the step,
  time and status for the overall workout.
* Workouts can be paused and resumed.
* Plays a sound when changing interval.

Planned
=======

*(In order of usefulness descending/superfluousness ascending)*

* Tracking of workout completion data.
* Animate a sprite indicating what the current step is.

.. _`Couch-to-5k`: http://www.coolrunning.com/engine/2/2_3/181.shtml
.. _`Eclipse`: http://www.eclipse.org
.. _`MTJ plugin`: http://www.eclipse.org/mtj/
.. _`Java ME SDK 3.0`: http://www.oracle.com/technetwork/java/javame/javamobile/download/overview/index.html
