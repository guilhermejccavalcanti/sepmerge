def __call__(self, ax, renderer):
    # Subtracting transSubfigure will tipically rely on inverted(),
    # freezing the transform; thus, this needs to be delayed until draw
    # time as transSubfigure may otherwise change after this is evaluated.
    return mtransforms.TransformedBbox(
        mtransforms.Bbox.from_bounds(*self._bounds),
<<<<<<< /Users/felipearaujo/Projects/tcc/sepmerge/testcase/left.py
        a
||||||| /Users/felipearaujo/Projects/tcc/sepmerge/testcase/base.py
        self._transform - ax.figure.transSubfigure)
=======
        b
>>>>>>> /Users/felipearaujo/Projects/tcc/sepmerge/testcase/right.py
        self._transform - ax.figure.transSubfigure)
