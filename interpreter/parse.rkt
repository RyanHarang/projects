#lang racket
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; CSCI 301, Fall 2022
;;
;; Lab #8
;;
;; Ryan Harang
;; W01509971
;;
;; The goal of this program is to work with eval.rkt to parse and evalaute 
;; strings of characters by first turning them into lists using the provided
;; grammar and then evaluating the resulting lists.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(provide parse)

(define parse
  (lambda (str)
    (first (L (string->list str)))
    )
  )

; L is the base of the grammar and either calls _L, EL, or epsilon
; Uses helper method E?
(define L
  (lambda (input)
    (cond
      [(null? input) (cons null null)]
     
      [(char-whitespace? (first input)) (L (rest input))]

      [(E? (first input)) (let* ((e-rest (E input)) (l-rest (L (rest e-rest)))) (cons (cons (first e-rest) (first l-rest)) (rest l-rest)))]
     
      [else (cons null input)]
     )
    )
  )

; E either calls DN or AS or (L). E is used to choose between putting
; symbols and numbers together and nesting a call to L.
(define E
  (lambda (input)
    (cond
      [(null? input) (cons '() null)]

     
      [(D? (first input)) (let* ((d-rest (D input))
                                (n-rest (N (rest d-rest) (first d-rest))))
                                n-rest)]

      [(A? (first input)) (let* ((a-rest (A input))
                                (s-rest (S (rest a-rest) (first a-rest))))
                                s-rest)]
     
      [(equal? #\( (first input)) (let* ((l-rest (L (rest input)))) (cons (first l-rest) (rest (rest l-rest))))]
     
      [else (error (string-append "syntax error.  Rest:" (list->string input)))]
     )
    )
  )

; N adds a given number to the start of a list using DN
(define N
  (lambda (input num)
    (cond
      [(or (null? input)(not (D? (first input)))) (cons num input)]
      [else (let* ((d-rest (D input))
                  (n-rest (N (rest d-rest)
                             (+ (* num 10)
                                (first d-rest))
                             )
                          )
                  )
             n-rest)]
     )
    )
  )

; S adds a given symbol to the start of a list using AS
(define S
  (lambda (input sym)
    (cond
      [(or (null? input) (not (A? (first input)))) (cons (string->symbol (list->string sym)) input)]
      [else (let* ((a-rest (A input)) (s-rest (S (rest a-rest) (append sym (first a-rest))))) s-rest)]
     )
    )
  )

; Handles single digits
; Uses helper method D? to check if input is a digit
(define D
  (lambda (input)
    (cond
      [(null? input) (error (string-append "Not a digit:" (list->string input)))]
     
      [(D? (first input)) (cons (char->number (first input)) (rest input))]
           
      [else (error (string-append "Not a digit:" (list->string input)))]
      )
    )
  )

; Handles single symbols similar to how D handles single digits
; Uses helper method A? to check if input is symbolic
(define A
  (lambda (input)
    (cond
      [(null? input) (error (string-append "Not a digit:" (list->string input)))]
      
      [(A? (first input)) (cons (cons (first input) null) (rest input))]
           
      [else (error (string-append "Not a symbol:" (list->string input)))]
      )
    )
  )

; char->number converts a char containing a digit to the integer value
(define char->number
  (lambda (char)
    (- (char->integer char)
       (char->integer #\0))
    )
  )

; char-synbolic is the given method that checls of a character is a symbol
; for our definitions
(define char-symbolic?
  (lambda (char) (and (not (char-whitespace? char)) (not (eq? char #\()) (not (eq? char #\)))))
  )

; Predicate for symbolic values.
(define A? char-symbolic?)

; Predicate for decimal values.
(define D? char-numeric?)

; Predicate for decimal or symbolic values. 
(define
  (E? input)
  (cond
    [(D? input) #t]
    [(A? input) #t]
    [(equal? input #\()]
    [else #f]
    )
  )