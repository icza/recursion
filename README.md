# Recursion #

> _Simplicity is (almost) everything._

This project contains recursive algorithms which demonstrate the simplicity and the power potential of recursion and an algorithm visualizer. This visualizer application has an algorithm interface, new algorithms can be plugged into it, and it provides an interface to visualize and parametrize the algorithms.

## Screenshot of the Visualizer ##
![http://lh6.ggpht.com/_jDMClHrENz8/SX8d4fAdELI/AAAAAAAAK9I/jWkssNBIA48/s800/algvisualizer2.PNG.jpg](http://lh6.ggpht.com/_jDMClHrENz8/SX8d4fAdELI/AAAAAAAAK9I/jWkssNBIA48/s800/algvisualizer2.PNG.jpg)

[More screenshots...](../wiki/Screenshots.md)

## What is recursion? ##
Who wouldn't have seen the simple recursive factorial algorithm?
```
int fact( int n ) {
    if ( n < 2 )
        return 1;
    else
        return n * fact( n - 1 );
}
```

I don't think there's a need to explain recursion in a new way, you can find numerous explanation for that ([wikipedia](http://en.wikipedia.org/wiki/Recursion) or just [google it](http://www.google.com/search?q=recursion)).

Recursion is often said to be slow and ineffective, which is true. But those are not the only aspects of writing programs and developing softwares.

There are many cases where the recursive solution is way more simpler and clearer, and it takes much less time and effort to develop it, maintain it and understand it. This project is here to give you examples how simple, clear and understandable it can be.

The best advice you can give and take:

>  **Don't be afraid of using recursion, but use it wisely!**

## The basics of recursion ##
The 2 main things you have to know about recursive algorithms are:
  1. They (the functions/methods) **call themselves** before they would return, before they would actually finish their job (maybe not themselves directly, but a function/method that called them before, and not yet returned).
  1. There has to be some kind of **condition** (so called _termination condition_) in the recursive calls in order so they will stop sometime and return eventually.

### How can we recognize the situations and possibilities to use recursion? ###

**Self similarity.** If we can identify self similarity in the problem, it's almost sure we can use recursion to solve it. If we can break down the original task into smaller pieces which are similar to the bigger problem in a way that the solution of the bigger problem can be assembled from the solutions of the smaller ones, we're almost winners. The recursive algorithm's job is to define how to assemble the whole solution from the smaller ones.

For example in the factorial algorithm, we break down of calculating `n!` into calculating `(n-1)!`, and then we get the solution of `n!` by multiplying `(n-1)!` by `n`. Note that the recursive factorial algorithm stands here only by a demostration as the iterative solution for calculating the factorial is much more effective.

### So when should we use recursion? ###

Whenever we identify self similarity, we should not always turn to recursion automatically. Profit becomes noticeably bigger when the small pieces are not independent, when the solution of a small piece _depends on_ or _requires_ the solution(s) of another piece(s) (usually the neighbors').

For example if we want to generate a terrain, we're more than welcome to use recursion. Because if we look at a slice of the terrain, what does it look like? Yes, like a terrain. Like the whole. So we can use recursion here, but for the whole terrain to be smooth, we want the slices to connect to each other without break lines, without edges, so in order to generate a slice of a terrain we need to know what its neighbors look like, we need the generated data (the terrain heights for example) on the borders of the slices at least (or the derivate of the surface on the borders for smoother terrain generation).
