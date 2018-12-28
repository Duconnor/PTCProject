; the finite set of states
#Q = {s1,s11,s12,s13,s1r,s1a,s2,s21,s22,s23,s24,s25,s26,s27,s28,s29,s2a,s2r,s3,s4,accept,reject}

; the finite set of input symbols
#S = {1,x,=}

; the complete set of tape symbols
#T = {1,x,=,0,#,_}

; the start state
#q0 = s1

; the blank symbol
#B = _

; the set of final states
#F = {accept}

; the transition fuctions

; start state, enter TM1
s1 * * * s11

; in TM1
s11 1 1 r s11
s11 x x r s12
s11 _ _ * s1r ; enter reject state
s11 = = * s1r
s12 1 1 r s12
s12 _ _ * s1r
s12 x x * s1r
s12 = = r s13
s13 1 1 r s13
s13 = = * s1r
s13 x x * s1r
s13 _ _ l s1a

; TM1 finish
; switch head back to the begining
s1a * * * s2
s2 1 1 l s2
s2 x x l s2
s2 = = l s2

; enter TM2
s2 _ _ r s21

; in TM2
s21 x x * s2a ; enter accept state
s21 1 # r s22
s22 1 1 r s22
s22 x x r s23
s23 0 0 r s23
s23 = = l s28
s23 1 0 r s24
s24 1 1 r s24
s24 = = r s25
s25 1 1 r s25
s25 _ _ l s26
s25 # # l s26
s26 = = * s2r ; reject
s26 1 # l s27
s27 1 1 l s27
s27 0 0 l s27
s27 = = l s27
s27 x x r s23
s28 0 1 l s28
s28 x x l s29
s29 1 1 l s29
s29 # # r s21

; out TM2
; check for acceptance
s2a * * * s3
s3 # # r s3
s3 x x r s3
s3 1 1 r s3
s3 = = r s4
s4 # # * accept
s4 _ _ * accept
s4 1 1 * reject

