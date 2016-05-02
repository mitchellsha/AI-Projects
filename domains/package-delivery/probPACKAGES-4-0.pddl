;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; 
;;; o1 -> H1 <- H4 -> o4
;;; ^	  |		^	  |
;;; |	  v	    |	  v
;;; o2 <- H2 -> H3 <- o3
;;; 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (problem PACKAGES-2-1)
(:domain PACKAGES)
(:objects package1 package2 package3 package4 office1 office2 office3 office4 hallway1 hallway2 hallway3 hallway4)
(:init (package package1) (package package2) (package package3) (package package4)
	(room office1) (room office2) (room office3) (room office4)
	(room hallway1) (room hallway2) (room hallway3) (room hallway4)
	(connected office1 hallway1)
	(connected office2 office1)
	(connected office3 hallway3)
	(connected office4 office3)
	(connected hallway1 hallway2)
	(connected hallway2 office2) (connected hallway2 hallway3)
	(connected hallway3 hallway4)
	(connected hallway4 office4) (connected hallway4 hallway1)
	(in package1 office3) (in package2 office4) (in package3 office1) (in package4 office2)
	(handempty) (at hallway1))
(:goal (AND (in package1 office1) (in package2 office2) (in package3 office3) (in package4 office4))))