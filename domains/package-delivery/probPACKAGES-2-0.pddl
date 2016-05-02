;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; 
;;; oA -- oB
;;; |      |
;;; |      |
;;; mB -- mA
;;; 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (problem PACKAGES-2-0)
(:domain PACKAGES)
(:objects packageA packageB officeA officeB mailRoomA mailRoomB)
(:init (package packageA) (package packageB)
	(room officeA) (room officeB) (room mailRoomA) (room mailRoomB)
	(connected officeA officeB) (connected officeA mailRoomB)
	(connected officeB officeA) (connected officeB mailRoomA)
	(connected mailRoomA officeB) (connected mailRoomA mailRoomB)
	(connected mailRoomB officeA) (connected mailRoomB mailRoomA)
	(in packageA mailRoomA) (in packageB mailRoomB) (handempty) (at officeA))
(:goal (AND (in packageA officeA) (in packageB officeB))))