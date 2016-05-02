;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; 
;;; oA <- hw -> oB
;;;  \    ^	   /
;;;   \   |   /
;;; 	> mr <
;;; 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (problem PACKAGES-2-1)
(:domain PACKAGES)
(:objects package1 package2 office1 office2 mailroom hallway)
(:init (package package1) (package package2)
	(room office1) (room office2) (room mailroom) (room hallway)
	(connected office1 mailroom)
	(connected office2 mailroom)
	(connected mailroom hallway)
	(connected hallway office1) (connected hallway office2)
	(in package1 mailroom) (in package2 mailroom) (handempty) (at hallway))
(:goal (AND (in package1 office1) (in package2 office2))))