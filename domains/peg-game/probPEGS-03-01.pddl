;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; 
;;;   0 1 1
;;; 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (problem PEGS-03-01)
(:domain PEGS)
(:objects hole1 hole2 hole3)
(:init (empty hole1)
	(not (empty hole2))
	(not (empty hole3))
	(jump hole1 hole2 hole3)
	(jump hole3 hole2 hole1)
	)
(:goal (AND
		(not (empty hole1))
		(empty hole2)
		(empty hole3)
		)))