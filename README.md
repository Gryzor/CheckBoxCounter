# CheckBoxCounter
Simple Demo To Count Items Checked in a RecyclerView

The idea is to demonstrate one relatively simple way to pass "click" events to higher levels. 

After years of StackOverdlow, I realized that most people (myself included!) tend to put their logic in the wrong places.
When dealing with a Recyclerview, there's a lot going on (as better as it is compared to the old ListView, it's still very _boilerplatty_).

The architecture is simple: Let the Adapter in peace; let the viewHolders alone, these two objects have a lot to deal with already, don't make them think.


`Activity -> Adapter -> View Holder` 

Let the Activity (through a ViewModel -not included in the demo-) deal with whatever you want to happen. Have the Lower level components (viewholders, adapters) simply inform what happened, *not make decisions* on behalf of anybody.

This project simply shows a list of "Things" and a checkbox for each. There's a counter at the top that ... err... _counts_ the number of checked items at any given time. 

