### K-d Trees

Status: finished.

A data type to represent a set of points in the unit square using a 2d-tree to support efficient range search (find all of the points contained in a query rectangle) and nearest-neighbour search (find a closest point to a query point).

#### PointSET.java
<ul>
<li>Brute force implementation</li>
<li>Nearest-neighbour and range-search each O(n)</li>
</ul>

#### KdTree.java
<ul>
<li>Implementation using custom-built 2d-tree</li>
<li>Nearest-neighbour and range-search each average ~logN</li>
</ul>

Dependencies: TODO
Full specification: TODO
