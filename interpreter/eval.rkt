#lang racket
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; CSCI 301, Fall 2022
;;
;; Lab #7
;;
;; Ryan Harang
;; W01509971
;;
;; The goal of this program is to evaluate functions and procedures in the 
;; procedure first style used by racket.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(provide lookup
         evaluate
         special-form?
         evaluate-special-form
         )

(define
  ; lookup takes a symbol to search for and en environment to search in
  (lookup sym env)
  (cond
    ; check to see if first argument passed is a symbol
    [(symbol? sym)
    (cond

      ; check the first element with sym
      ; if equal, return the second item in the pair from env
      [(equal? sym (caar env)) (cadar env)]

      ; check to see if we have made it to the last element yet
      ; if not, moves to next element by recursively calling lookup
      ; 'deletes' first element using cdr with each call
      [(> (length env) 1) (lookup sym (cdr env))]

      ; else for returning an error when env does not contain sym
      [else (error "Environment does not contain the given symbol:" sym)]
      )
    ]
    ; if sym is not a symbol this else will return an error
    [else (error "FIrst argument was not a symbol.")]
    )
  )

(define
  ; evaluate takes an expression and an environment
  (evaluate exp env)
  (cond
    ; if exp is a number it gets returned
    [(number? exp) exp]

    ; if exp is a symbol lookup is called with exp and env
    [(symbol? exp) (lookup exp env)]

    ; check for special form using special-form?
    [(special-form? exp) (evaluate-special-form exp env)]

    ; if exp is a list, each element is evaluated recursively
    [(list? exp)

        ; apply-function is used to put items through evaluate one at a time
        ; map calls evaluate on each item in x (exp)
        (apply-function (evaluate (car exp) env)
               (cdr (map (lambda (x) (evaluate x env)) exp)))
     ]

    ; returns an error if exp is not a number, symbol or list
    [else (error "Given expression was neither a number, symbol, nor a list: " exp)]
    )
  )

(define
  ; special-form? takes a list and determines if the first element is if or cond
  (special-form? ls)

  ; initial check to ensure special-form? was called with a list
  (cond
    [(list? ls)

     ; if given a list will then check if first item in list is special-form
     (cond
      [(or (equal? (car ls) 'if) (equal? (car ls) 'cond) (equal? (car ls) 'let) (equal? (car ls) 'lambda) (equal? (car ls) 'letrec))]

      ; if given a list with first item not a special form returns #f
      [else #f]
      )
    ]

    ; if not given a list an error is returned
    [else (error "special-form? called with a non-list: " ls)]
    )
  )

(define
  ; evaluate-special-form identifies the special-form
  ; and executes whichever one if finds
  (evaluate-special-form ls env)
  (cond
    [(equal? (car ls) 'if)

     ; if the first item in ls is an if the second item in ls must be a list.
     ; this list will either evaluate to #t or #f
     ; and the final two items are the return values first is returned if evaluate
     ; returns true, and the second if false
     (cond
       [(evaluate (cadr ls) env) (evaluate (caddr ls) env)]
       [else (evaluate (last ls) env)]
       )
     ]

    ; if the first item in ls is cond than cdr ls is a list of lists
    ; uses my cond-recursion method to check these lists
    [(equal? (car ls) 'cond)
     (cond-recursion (cdr ls) env)
     ]

    ; if the first item in ls is let than (caddr ls) is the expresion to
    ; send to evaluate. (cadr ls) is the list of lists containing symbol
    ; value pairs.
    [(equal? (car ls) 'let)
     (evaluate (caddr ls) (add-to-env (cadr ls) env))   
     ]
    [(equal? (car ls) 'lambda)
     (closure (cadr ls) (caddr ls) env)
     ]
    [(equal? (car ls) 'letrec)
     (evaluate (caddr ls) (replace-env (cadr ls) env))
     ]
    
    [else (error "First item in list given to evaluate-special-form is not a special-form: " ls)]
    )
  )

; apply-function is our way of choosing between using racket's
; built in apply method and our own apply method(s)
(define
  (apply-function funct arg)
  (cond

    ; checks if given a procedure that racket's built in apply
    ; method can handke
    [(procedure? funct) (apply funct arg)]

    ; checks if givena closure and calls apply-closure if so
    [(closure? funct) (apply-closure funct arg)]

    ; throws an error if all above checks fail
    [else (error "Unknown function type: " funct)]
  )
  )

; cond-recursion is used to recursively evaluate each expression in the
; conditional and return the value paired with the true expression
(define
  (cond-recursion ls env)
  (cond

   ; check to ensure not at last element
   [(> (length ls) 1)
    (cond

     ; checks to see if current expression evaluates to true
     ; if it does, return the item paired with the expression
     [(evaluate (caar ls) env) (evaluate (cadar ls) env)]

     ; if false recursion is used to move to the next element
     [else (cond-recursion (cdr ls) env)]
     )
   ]
    ; if at last element and it is else, return the last paired value
    [(equal? (caar ls) 'else) (evaluate (cadar ls) env)]

    ; if last element isn't an else and all previous conditions were false
    ; return an error
    [else (error "cond did not contain either a true condition or an else: " ls)]
  )
  )

; add-to-env takes a list of symbol-value pair lists and the updating environement
; uses map and a lambda function to add all the pairs to a mini-environment, deckares
; given environment as old. and appends them for the new environment
; used for let and letrec
(define
  (add-to-env pairs env)
  (append (make-mini pairs env) env)
  )

; method to replace environments within closures for letrec
(define
  (replace-env pairs old)

  ; a let that binds new to mini+old and mini to the result of make-mini
  (let* ((mini (make-mini pairs old)) (new (append mini old)))
    ; appending the old environment with an updated mini-environment
    ; each item in mini is checked to see if the second part is a closure
    ; if it is the closure's enironment is updated to new
    (append (map (lambda (ls)
                 (cond
                   [(closure? (cadr ls)) (set-closure-env! (cadr ls) new) (append (list (car ls)) (list (cadr ls)))]
                   [else ls]
                   )
                   ) mini) old)
    )
  )

; a helper method that makes mini-environments
(define
  (make-mini pairs env)
  (map (lambda (ls) (append (list (car ls)) (list (evaluate (cadr ls) env)))) pairs)
  )

; evalaute will be called with the body of the given closure and a built upon
; environment. This updated environment will have whatever variables were given
; to said closure as well as the vals list, paired front to back
(define apply-closure
  (lambda (closure-exp 1st) (evaluate (closure-body closure-exp) (append (map list (closure-vars closure-exp) 1st) (closure-env closure-exp))))
  )

; method for creating closures
(define closure
  (lambda (vars body env)
  (mcons 'closure (mcons env (mcons vars body)))))

; check if something is a closure
(define closure?
  (lambda (clos) (and (mpair? clos) (eq? (mcar clos) 'closure))))

; accessor for variables of a closure
(define closure-vars
  (lambda (clos) (mcar (mcdr (mcdr clos)))))

; accessor for the body of a closure
(define closure-body
  (lambda (clos) (mcdr (mcdr (mcdr clos)))))

; accessor for the environment of a closure
(define closure-env
  (lambda (clos) (mcar (mcdr clos))))

; setter for closure environments
(define set-closure-env!
  (lambda (clos new-env) (set-mcar! (mcdr clos) new-env)))