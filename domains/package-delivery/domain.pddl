;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; a robot delivering packages in an office building
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (domain PACKAGES)
  (:requirements :strips)
  (:predicates (in ?p ?r)
	       (at ?r)
	       (handempty)
	       (holding ?p)
	       (room ?r)
	       (package ?p)
	       (connected ?x ?y)
	       )

  (:action move
	     :parameters (?x ?y)
	     :precondition (and (room ?x) (room ?y) (connected ?x ?y) (at ?x))
	     :effect
	     (and (at ?y)
		   (not (at ?x))))

  (:action pick-up
	     :parameters (?p ?r)
	     :precondition (and (package ?p) (room ?r) (handempty) (in ?p ?r) (at ?r))
	     :effect
	     (and (not (handempty))
		   (not (in ?p ?r))
		   (holding ?p)))

  (:action put-down
	     :parameters (?p ?r)
	     :precondition (and (package ?p) (room ?r) (holding ?p) (at ?r))
	     :effect
	     (and (not (holding ?p))
		   (handempty)
		   (in ?p ?r)))
		   )
