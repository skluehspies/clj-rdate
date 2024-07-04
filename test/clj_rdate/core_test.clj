(ns clj-rdate.core-test
  (:require [clojure.test :refer :all]
            [clj-rdate.core :refer :all]
            [java-time.api :as jt]))

(deftest test-rdate-add-days
  (are [exp args] (= exp (rdate-add (rdate (:rd args)) (:dt args)))
    ; First check basic cases up add and subtract work
    (jt/local-date 2017 10 26) {:rd "1d" :dt (jt/local-date 2017 10 25)}
    (jt/local-date 2017 10 24) {:rd "-1d" :dt (jt/local-date 2017 10 25)}
    ; Expect no change with 0d
    (jt/local-date 2017 10 25) {:rd "0d" :dt (jt/local-date 2017 10 25)}
    ; Check that we correctly increment months
    (jt/local-date 2017 11 1) {:rd "7d" :dt (jt/local-date 2017 10 25)}
    ; Check that we don't have any weekend handling
    (jt/local-date 2017 10 28) {:rd "1d" :dt (jt/local-date 2017 10 27)}

    (jt/local-date-time 2017 10 26) {:rd "1d" :dt (jt/local-date-time 2017 10 25)}
    (jt/local-date-time 2017 10 24) {:rd "-1d" :dt (jt/local-date-time 2017 10 25)}
    ; Expect no change with 0d
    (jt/local-date-time 2017 10 25) {:rd "0d" :dt (jt/local-date-time 2017 10 25)}
    ; Check that we correctly increment months
    (jt/local-date-time 2017 11 1) {:rd "7d" :dt (jt/local-date-time 2017 10 25)}
    ; Check that we don't have any weekend handling
    (jt/local-date-time 2017 10 28) {:rd "1d" :dt (jt/local-date-time 2017 10 27)}))

(deftest test-rdate-add-ordering
  (are [exp args] (= exp (rdate-add (:arg1 args) (:arg2 args)))
    ; Check through various operations
    (jt/local-date 2017 10 26) {:arg1 (rdate "1d") :arg2 (jt/local-date 2017 10 25)}
    (jt/local-date 2017 10 26) {:arg1 (jt/local-date 2017 10 25) :arg2 (rdate "1d")}
    (rdate "2d+1d") {:arg1 (rdate "2d") :arg2 (rdate "1d")}))

(deftest test-rdate-whitespace
  (are [exp act] (= exp act)
    (rdate "2d+1d") (rdate "2d + 1d")
    ; It's even possible to put whitespace between the names
    (rdate "1d") (rdate "1 d")
    (rdate "3*(2d+1d)") (rdate "3 * ( 2d +    1d   )")))

(deftest test-rdate-add-weeks
  (are [exp args] (= exp (rdate-add (rdate (:rd args)) (:dt args)))
    ; First check basic cases up add and subtract work
    (jt/local-date 2017 11 1) {:rd "1w" :dt (jt/local-date 2017 10 25)}
    (jt/local-date 2017 10 18) {:rd "-1w" :dt (jt/local-date 2017 10 25)}
    ; Expect no change with 0w
    (jt/local-date 2017 10 25) {:rd "0w" :dt (jt/local-date 2017 10 25)}
    ; Check that we correctly increment months
    (jt/local-date 2017 12 13) {:rd "7w" :dt (jt/local-date 2017 10 25)}

    (jt/local-date-time 2017 11 1) {:rd "1w" :dt (jt/local-date-time 2017 10 25)}
    (jt/local-date-time 2017 10 18) {:rd "-1w" :dt (jt/local-date-time 2017 10 25)}
    ; Expect no change with 0w
    (jt/local-date-time 2017 10 25) {:rd "0w" :dt (jt/local-date-time 2017 10 25)}
    ; Check that we correctly increment months
    (jt/local-date-time 2017 12 13) {:rd "7w" :dt (jt/local-date-time 2017 10 25)}))


(deftest test-rdate-add-months
    (are [exp args] (= exp (rdate-add (rdate (:rd args)) (:dt args)))
      ; First check basic cases up add and subtract work
      (jt/local-date 2017 11 25) {:rd "1m" :dt (jt/local-date 2017 10 25)}
      (jt/local-date 2017 9 25) {:rd "-1m" :dt (jt/local-date 2017 10 25)}
      ; Expect no change with 0m
      (jt/local-date 2017 10 25) {:rd "0m" :dt (jt/local-date 2017 10 25)}
      ; Cbeck that we preserve the month and maintain last day in conflict
      (jt/local-date 2017 11 30) {:rd "1m" :dt (jt/local-date 2017 10 31)}
      (jt/local-date 2017 9 30) {:rd "-1m" :dt (jt/local-date 2017 10 31)}
      ; Check that holds true on leap years as well
      (jt/local-date 2013 2 28) {:rd "12m" :dt (jt/local-date 2012 2 29)}
      ; Cbeck that we preserve the month and maintain last day in conflict
      (jt/local-date 2011 2 28) {:rd "-12m" :dt (jt/local-date 2012 2 29)}

      (jt/local-date-time 2017 11 25) {:rd "1m" :dt (jt/local-date-time 2017 10 25)}
      (jt/local-date-time 2017 9 25) {:rd "-1m" :dt (jt/local-date-time 2017 10 25)}
      ; Expect no change with 0m
      (jt/local-date-time 2017 10 25) {:rd "0m" :dt (jt/local-date-time 2017 10 25)}
      ; Cbeck that we preserve the month and maintain last day in conflict
      (jt/local-date-time 2017 11 30) {:rd "1m" :dt (jt/local-date-time 2017 10 31)}
      (jt/local-date-time 2017 9 30) {:rd "-1m" :dt (jt/local-date-time 2017 10 31)}
      ; Check that holds true on leap years as well
      (jt/local-date-time 2013 2 28) {:rd "12m" :dt (jt/local-date-time 2012 2 29)}
      ; Cbeck that we preserve the month and maintain last day in conflict
      (jt/local-date-time 2011 2 28) {:rd "-12m" :dt (jt/local-date-time 2012 2 29)}))

(deftest test-rdate-add-years
    (are [exp args] (= exp (rdate-add (rdate (:rd args)) (:dt args)))
      ; First check basic cases up add and subtract work
      (jt/local-date 2018 10 25) {:rd "1y" :dt (jt/local-date 2017 10 25)}
      (jt/local-date 2016 10 25) {:rd "-1y" :dt (jt/local-date 2017 10 25)}
      (jt/local-date 2029 10 25) {:rd "12y" :dt (jt/local-date 2017 10 25)}
      (jt/local-date 2005 10 25) {:rd "-12y" :dt (jt/local-date 2017 10 25)}
      ; Expect no change with 0y
      (jt/local-date 2017 10 25) {:rd "0y" :dt (jt/local-date 2017 10 25)}
      ; Check that we preserve the month and use last business day on leap years in conflict
      (jt/local-date 2013 2 28) {:rd "1y" :dt (jt/local-date 2012 2 29)}
      (jt/local-date 2011 2 28) {:rd "-1y" :dt (jt/local-date 2012 2 29)}
      (jt/local-date-time 2018 10 25) {:rd "1y" :dt (jt/local-date-time 2017 10 25)}
      (jt/local-date-time 2016 10 25) {:rd "-1y" :dt (jt/local-date-time 2017 10 25)}
      (jt/local-date-time 2029 10 25) {:rd "12y" :dt (jt/local-date-time 2017 10 25)}
      (jt/local-date-time 2005 10 25) {:rd "-12y" :dt (jt/local-date-time 2017 10 25)}
      ; Expect no change with 0y
      (jt/local-date-time 2017 10 25) {:rd "0y" :dt (jt/local-date-time 2017 10 25)}
      ; Check that we preserve the month and use last business day on leap years in conflict
      (jt/local-date-time 2013 2 28) {:rd "1y" :dt (jt/local-date-time 2012 2 29)}
      (jt/local-date-time 2011 2 28) {:rd "-1y" :dt (jt/local-date-time 2012 2 29)}))

(deftest test-rdate-add-weekdays
  (are [exp args] (= exp (rdate-add (rdate (:rd args)) (:dt args)))
    ; First check basic cases up add and subtract work
    (jt/local-date 2017 10 30) {:rd "1MON" :dt (jt/local-date 2017 10 25)}
    (jt/local-date 2017 12 30) {:rd "10SAT" :dt (jt/local-date 2017 10 25)}
    (jt/local-date 2017 10 18) {:rd "-1WED" :dt (jt/local-date 2017 10 25)}
    (jt/local-date 2017 8 18) {:rd "-10FRI" :dt (jt/local-date 2017 10 25)}

    (jt/local-date-time 2017 10 30) {:rd "1MON" :dt (jt/local-date-time 2017 10 25)}
    (jt/local-date-time 2017 12 30) {:rd "10SAT" :dt (jt/local-date-time 2017 10 25)}
    (jt/local-date-time 2017 10 18) {:rd "-1WED" :dt (jt/local-date-time 2017 10 25)}
    (jt/local-date-time 2017 8 18) {:rd "-10FRI" :dt (jt/local-date-time 2017 10 25)}))

(deftest test-rdate-add-nth-weekdays
  (are [exp args] (= exp (rdate-add (rdate (:rd args)) (:dt args)))
    ; First check basic cases up add and subtract work
    (jt/local-date 2017 10 2) {:rd "1st MON" :dt (jt/local-date 2017 10 25)}
    (jt/local-date 2017 10 13) {:rd "2nd FRI" :dt (jt/local-date 2017 10 25)}
    (jt/local-date 2017 11 25) {:rd "4th SAT" :dt (jt/local-date 2017 11 25)}
    (jt/local-date 2017 12 31) {:rd "5th SUN" :dt (jt/local-date 2017 12 25)}

    (jt/local-date-time 2017 10 2) {:rd "1st MON" :dt (jt/local-date-time 2017 10 25)}
    (jt/local-date-time 2017 10 13) {:rd "2nd FRI" :dt (jt/local-date-time 2017 10 25)}
    (jt/local-date-time 2017 11 25) {:rd "4th SAT" :dt (jt/local-date-time 2017 11 25)}
    (jt/local-date-time 2017 12 31) {:rd "5th SUN" :dt (jt/local-date-time 2017 12 25)}))

(deftest test-rdate-add-nth-last-weekdays
  (are [exp args] (= exp (rdate-add (rdate (:rd args)) (:dt args)))
    ; First check basic cases up add and subtract work
    (jt/local-date 2017 10 30) {:rd "Last MON" :dt (jt/local-date 2017 10 24)}
    (jt/local-date 2017 10 20) {:rd "2nd Last FRI" :dt (jt/local-date 2017 10 24)}
    (jt/local-date 2017 12 03) {:rd "5th Last SUN" :dt (jt/local-date 2017 12 24)}

    (jt/local-date-time 2017 10 30) {:rd "Last MON" :dt (jt/local-date-time 2017 10 24)}
    (jt/local-date-time 2017 10 20) {:rd "2nd Last FRI" :dt (jt/local-date-time 2017 10 24)}
    (jt/local-date-time 2017 12 03) {:rd "5th Last SUN" :dt (jt/local-date-time 2017 12 24)}))

(deftest test-rdate-add-nth-weekdays-bad
  (are [args] (thrown? Exception (rdate-add (rdate (:rd args)) (:dt args)))
    ;  Run through cases where there is no 5th weekday in a given month
    {:rd "5th WED" :dt (jt/local-date 2017 10 25)}
    {:rd "5th MON" :dt (jt/local-date 2017 6 1)}

    {:rd "5th WED" :dt (jt/local-date-time 2017 10 25)}
    {:rd "5th MON" :dt (jt/local-date-time 2017 6 1)}))

(deftest test-rdate-add-nth-last-weekdays-bad
  (are [args] (thrown? Exception (rdate-add (rdate (:rd args)) (:dt args)))
    ;  Run through cases where there is no 5th weekday in a given month
    {:rd "5th Last WED" :dt (jt/local-date 2017 10 25)}
    {:rd "5th Last MON" :dt (jt/local-date 2017 6 1)}

    {:rd "5th Last WED" :dt (jt/local-date-time 2017 10 25)}
    {:rd "5th Last MON" :dt (jt/local-date-time 2017 6 1)}))

(deftest test-rdate-add-first-day-of-month
  (are [exp args] (= exp (rdate-add (rdate (:rd args)) (:dt args)))
    ; First check basic cases up add and subtract work
    (jt/local-date 2017 10 1) {:rd "FDOM" :dt (jt/local-date 2017 10 25)}
    (jt/local-date-time 2017 10 1) {:rd "FDOM" :dt (jt/local-date-time 2017 10 25)}))

(deftest test-rdate-add-last-day-of-month
  (are [exp args] (= exp (rdate-add (rdate (:rd args)) (:dt args)))
    ; First check basic cases up add and subtract work
    (jt/local-date 2017 10 31) {:rd "LDOM" :dt (jt/local-date 2017 10 25)}
    (jt/local-date-time 2017 10 31) {:rd "LDOM" :dt (jt/local-date-time 2017 10 25)}))

(deftest test-rdate-add-basic-addition-compounds
  (are [exp args] (= exp (rdate-add (rdate (:rd args)) (:dt args)))
    ; Start with some simple models
    (jt/local-date 2017 10 28) {:rd "1d+1d" :dt (jt/local-date 2017 10 26)}
    (jt/local-date 2017 10 30) {:rd "1d+1d+1d+1d" :dt (jt/local-date 2017 10 26)}
    ; Some trivial no-op scenarios
    (jt/local-date 2017 10 26) {:rd "1d-1d" :dt (jt/local-date 2017 10 26)}
    (jt/local-date 2017 10 26) {:rd "1d-1d-1d+1d" :dt (jt/local-date 2017 10 26)}
    ; And check more complex no-ops with unary operators
    (jt/local-date 2017 10 26) {:rd "-1d+3d-2d" :dt (jt/local-date 2017 10 26)}
    ; Check that mixing methods and ordering works as expected (left to right)
    (jt/local-date 2017 10 19) {:rd "3rd WED+1d" :dt (jt/local-date 2017 10 26)}
    (jt/local-date 2017 10 18) {:rd "1d+3rd WED" :dt (jt/local-date 2017 10 26)}
    ; Now check some more obscure examples where they're not 'no-op'
    (jt/local-date 2017 10 26) {:rd "1m-1m" :dt (jt/local-date 2017 10 26)}
    (jt/local-date 2017 10 30) {:rd "1m-1m" :dt (jt/local-date 2017 10 31)}
    ; Start with some simple models
    (jt/local-date-time 2017 10 28) {:rd "1d+1d" :dt (jt/local-date-time 2017 10 26)}
    (jt/local-date-time 2017 10 30) {:rd "1d+1d+1d+1d" :dt (jt/local-date-time 2017 10 26)}
    ; Some trivial no-op scenarios
    (jt/local-date-time 2017 10 26) {:rd "1d-1d" :dt (jt/local-date-time 2017 10 26)}
    (jt/local-date-time 2017 10 26) {:rd "1d-1d-1d+1d" :dt (jt/local-date-time 2017 10 26)}
    ; And check more complex no-ops with unary operators
    (jt/local-date-time 2017 10 26) {:rd "-1d+3d-2d" :dt (jt/local-date-time 2017 10 26)}
    ; Check that mixing methods and ordering works as expected (left to right)
    (jt/local-date-time 2017 10 19) {:rd "3rd WED+1d" :dt (jt/local-date-time 2017 10 26)}
    (jt/local-date-time 2017 10 18) {:rd "1d+3rd WED" :dt (jt/local-date-time 2017 10 26)}
    (jt/local-date-time 2017 10 26) {:rd "1m-1m" :dt (jt/local-date-time 2017 10 26)}
    (jt/local-date-time 2017 10 30) {:rd "1m-1m" :dt (jt/local-date-time 2017 10 31)}))


(deftest test-rdate-add-basic-multiplication-compounds
  (are [exp args] (= exp (rdate-add (rdate (:rd args)) (:dt args)))
    ; Start with some simple left multiplier cases
    (jt/local-date 2017 10 27) {:rd "1*1d" :dt (jt/local-date 2017 10 26)}
    (jt/local-date 2017 10 28) {:rd "2*1d" :dt (jt/local-date 2017 10 26)}
    (jt/local-date 2017 10 30) {:rd "4*1d" :dt (jt/local-date 2017 10 26)}
    (jt/local-date 2017 10 22) {:rd "4*-1d" :dt (jt/local-date 2017 10 26)}
    ; And now check the right multiplier
    (jt/local-date 2017 10 27) {:rd "1d*1" :dt (jt/local-date 2017 10 26)}
    (jt/local-date 2017 10 28) {:rd "1d*2" :dt (jt/local-date 2017 10 26)}
    (jt/local-date 2017 10 30) {:rd "1d*4" :dt (jt/local-date 2017 10 26)}
    (jt/local-date 2017 10 22) {:rd "-1d*4" :dt (jt/local-date 2017 10 26)}
    ; Repeated multiplications work as expected? 3*2*1d == 6d and check
    ; various iterations work as expected
    (jt/local-date 2017 11 01) {:rd "3*2*1d" :dt (jt/local-date 2017 10 26)}
    (jt/local-date 2017 11 01) {:rd "1d*2*3" :dt (jt/local-date 2017 10 26)}
    (jt/local-date 2017 11 01) {:rd "2*1d*3" :dt (jt/local-date 2017 10 26)}

    ; Check that it takes prescedence over addition
    (jt/local-date 2017 11 02) {:rd "2*3d+1d" :dt (jt/local-date 2017 10 26)}
    ; But bracketing will overrule this
    (jt/local-date 2017 11 03) {:rd "2*(3d+1d)" :dt (jt/local-date 2017 10 26)}
    ; Start with some simple left multiplier cases
    (jt/local-date-time 2017 10 27) {:rd "1*1d" :dt (jt/local-date-time 2017 10 26)}
    (jt/local-date-time 2017 10 28) {:rd "2*1d" :dt (jt/local-date-time 2017 10 26)}
    (jt/local-date-time 2017 10 30) {:rd "4*1d" :dt (jt/local-date-time 2017 10 26)}
    (jt/local-date-time 2017 10 22) {:rd "4*-1d" :dt (jt/local-date-time 2017 10 26)}
    ; And now check the right multiplier
    (jt/local-date-time 2017 10 27) {:rd "1d*1" :dt (jt/local-date-time 2017 10 26)}
    (jt/local-date-time 2017 10 28) {:rd "1d*2" :dt (jt/local-date-time 2017 10 26)}
    (jt/local-date-time 2017 10 30) {:rd "1d*4" :dt (jt/local-date-time 2017 10 26)}
    (jt/local-date-time 2017 10 22) {:rd "-1d*4" :dt (jt/local-date-time 2017 10 26)}
    ; Repeated multiplications work as expected? 3*2*1d == 6d and check
    ; various iterations work as expected
    (jt/local-date-time 2017 11 01) {:rd "3*2*1d" :dt (jt/local-date-time 2017 10 26)}
    (jt/local-date-time 2017 11 01) {:rd "1d*2*3" :dt (jt/local-date-time 2017 10 26)}
    (jt/local-date-time 2017 11 01) {:rd "2*1d*3" :dt (jt/local-date-time 2017 10 26)}

    ; Check that it takes prescedence over addition
    (jt/local-date-time 2017 11 02) {:rd "2*3d+1d" :dt (jt/local-date-time 2017 10 26)}
    ; But bracketing will overrule this
    (jt/local-date-time 2017 11 03) {:rd "2*(3d+1d)" :dt (jt/local-date-time 2017 10 26)}))

(deftest test-rdate-add-easter-sunday
  (are [exp args] (= exp (rdate-add (rdate (:rd args)) (:dt args)))
    ; Check some particular cases out
    (jt/local-date 2017 04 16) {:rd "0E" :dt (jt/local-date 2017 10 26)}
    (jt/local-date 2027 03 28) {:rd "10E" :dt (jt/local-date 2017 10 26)}
    (jt/local-date 2016 03 27) {:rd "-1E" :dt (jt/local-date 2017 10 26)}))

(deftest test-rdate-add-day-month
  (are [exp args] (= exp (rdate-add (rdate (:rd args)) (:dt args)))
    ; Check some particular cases out
    (jt/local-date 2017 01 01) {:rd "1JAN" :dt (jt/local-date 2017 10 26)}
    (jt/local-date 2017 02 23) {:rd "23FEB" :dt (jt/local-date 2017 10 26)}
    (jt/local-date 2017 12 31) {:rd "31DEC" :dt (jt/local-date 2017 10 26)}))

(deftest test-rdate-add-days-with-cal
  (are [exp args] (= exp (rdate-add (:rd args) (:dt args)))
    (jt/local-date 2017 10 27) {:rd "1d@Weekdays" :dt (jt/local-date 2017 10 26)}
    (jt/local-date 2017 10 30) {:rd "1d@Weekdays" :dt (jt/local-date 2017 10 27)}
    (jt/local-date 2017 10 30) {:rd "1d@Weekdays" :dt (jt/local-date 2017 10 28)}
    (jt/local-date 2017 10 30) {:rd "2d@Weekdays" :dt (jt/local-date 2017 10 27)}
    ;; (jt/local-date 2017 10 31) {:rd "2*(1d@Weekdays)" :dt (jt/local-date 2017 10 27)}

    (jt/local-date 2017 10 26) {:rd "-1d@Weekdays" :dt (jt/local-date 2017 10 27)}
    (jt/local-date 2017 10 27) {:rd "-1d@Weekdays" :dt (jt/local-date 2017 10 30)}
    (jt/local-date 2017 10 27) {:rd "-1d@Weekdays" :dt (jt/local-date 2017 10 29)}
    (jt/local-date 2017 10 27) {:rd "-2d@Weekdays" :dt (jt/local-date 2017 10 30)}
    ;; (jt/local-date 2017 10 27) {:rd "2*(-1d@Weekdays)" :dt (jt/local-date 2017 10 31)}
    ))

(deftest test-rdate-add-biz-days
  (are [exp args] (= exp (rdate-add (:rd args) (:dt args)))
    (jt/local-date 2017 10 27) {:rd "1b" :dt (jt/local-date 2017 10 26)}
    (jt/local-date 2017 10 30) {:rd "2b" :dt (jt/local-date 2017 10 26)}
    ; We can see that 2b is equivalent to 2*(1d@Weekdays)
    (jt/local-date 2017 10 31) {:rd "2b" :dt (jt/local-date 2017 10 27)}
    ; We see that -2b is equivalent to 2*(-1d@weekdays)
    (jt/local-date 2017 10 27) {:rd "-2b" :dt (jt/local-date 2017 10 31)}
    (jt/local-date 2017 10 26) {:rd "-2b" :dt (jt/local-date 2017 10 30)}
    ; 0b is actually 1*(0d@Weekdays)
    (jt/local-date 2017 10 26) {:rd "0b" :dt (jt/local-date 2017 10 26)}
    (jt/local-date 2017 10 30) {:rd "0b" :dt (jt/local-date 2017 10 28)}))
