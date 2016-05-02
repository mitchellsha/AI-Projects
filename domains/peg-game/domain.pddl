;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Peg Game
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (domain PEGS)
  (:requirements :strips)
  (:predicates (empty ?h)
	       (jump ?x ?y ?z)
	       )

  (:action makejump
	     :parameters (?x ?y ?z)
	     :precondition (and (jump ?x ?y ?z) (not (empty ?x)) (not (empty ?y)) (empty ?z))
	     :effect
	     (and (not (empty ?z))
		   (empty ?y)
		   (empty ?x)))

  (:action makejump
	     :parameters (?x ?y ?z)
	     :precondition (and (jump ?z ?y ?x) (not (empty ?x)) (not (empty ?y)) (empty ?z))
	     :effect
	     (and (not (empty ?z))
		   (empty ?y)
		   (empty ?x)))
		   )
