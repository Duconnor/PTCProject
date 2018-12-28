; set of states
#Q = {s1,s11,s12,s13,s14,s1a,s1r,s2,s21,s22,s23,s24,s25,s26,s27,s28,s29,s2x,s2r,accept,reject,agol,acla,rgol,rcla,acceptend,rejectend,wt1,wt2,wt3,wt4,wf1,wf2,wf3,wf4,wf5}

; the set of input symbols
#S = {a,b}

; the set of tape symbols
#T = {0,1,a,b,#,_,T,r,u,e,F,a,l,s}

; the start state
#q0 = s1

; the blank symbol
#B = _

; the set of final states
#F = {acceptend}

; the transition functions

; from start state to TM1
s1 * * * s11

; in TM1
; change the left a to 0 and b to 1
s11 a 0 r s12
s11 b 1 r s12
; go all the way right
s12 a a r s12
s12 b b r s12
; stop when reach the rightmost a/b
s12 _ _ l s13
s12 0 0 l s13
s12 1 1 l s13
; change the right a to 0 and b to 1
s13 a 0 l s14
s13 b 1 l s14
; reject when we can't find a and b (because at such case the length is odd)
s13 0 0 * s1r
s13 1 1 * s1r
; go all the way left
s14 a a l s14
s14 b b l s14
; stop when we reach the leftmost a or b
s14 0 0 r s11
s14 1 1 r s11
s11 0 0 * s1a
s11 1 1 * s1a
s11 _ _ * s1a

; out TM1 and enter TM2
s1a * * * s21
s21 0 # l s29 ; the symbol we read is 0
; go all the way left till the leftmost non # element
; and test if it is 0
s21 _ _ * s27 ; accept empty string
s29 # # l s29
s29 0 0 l s22
s29 1 1 l s22
s22 0 0 l s22
s22 1 1 l s22
s22 _ _ r s24
s22 # # r s24
s24 0 # r s26
s24 1 1 * s2r ; enter reject state because it is not 0
s26 0 0 r s26
s26 1 1 r s26
s26 # # r s28
s28 # # r s28
s28 _ _ * s27 ; accept here
s28 0 0 * s21
s28 1 1 * s21
s21 1 # l s2x ; the symbol we read is 1
; go all the way left till the leftmost non # element
; and test if it is 1
s2x # # l s2x
s2x 0 0 l s23
s2x 1 1 l s23
s23 0 0 l s23
s23 1 1 l s23
s23 _ _ r s25
s23 # # r s25
s25 1 # r s26
s25 0 0 * s2r ; enter reject state because it is not 1

s27 * * * accept
s1r * * * reject
s2r * * * reject

; write True
accept * * l agol ; go to the left end
agol * * l agol
agol _ _ r acla ; go to cla(clear all) state
acla * _ r acla
acla _ _ l wt1
wt1 _ T r wt2
wt2 _ r r wt3
wt3 _ u r wt4
wt4 _ e r acceptend

; write False
reject * * l rgol
rgol * * l rgol
rgol _ _ r rcla
rcla * _ r rcla
rcla _ _ l wf1
wf1 _ F r wf2
wf2 _ a r wf3
wf3 _ l r wf4
wf4 _ s r wf5
wf5 _ e r rejectend
