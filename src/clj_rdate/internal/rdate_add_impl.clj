(ns clj-rdate.internal.rdate-add-impl
  "Implementation details for rd/rdate-add"
  (:require [java-time.api :as jt]
            [clj-rdate.internal.fn :as intfn]))
(refer 'clj-rdate.core)

(defmethod rdate-add [:clj-rdate.core/date-obj :clj-rdate.core/rdate] [dt rd] (rdate-add rd dt))
(defmethod rdate-add [:clj-rdate.core/rdate :clj-rdate.core/rdate] [left right]
  {:type :clj-rdate.core/compound :parts [left right]})
(defmethod rdate-add [:clj-rdate.core/string-obj :clj-rdate.core/date-obj] [rd dt] (rdate-add (rdate rd) dt))
(defmethod rdate-add [:clj-rdate.core/date-obj :clj-rdate.core/string-obj] [dt rd] (rdate-add (rdate rd) dt))
(defmethod rdate-add [:clj-rdate.core/string-obj :clj-rdate.core/string-obj] [l r] (rdate-add (rdate l) (rdate r)))
(defmethod rdate-add [:clj-rdate.core/days :clj-rdate.core/date-obj] [rd dt]
  (jt/plus dt (jt/days (:period rd))))
(defmethod rdate-add [:clj-rdate.core/weeks :clj-rdate.core/date-obj] [rd dt]
  (jt/plus dt (jt/weeks (:period rd))))
(defmethod rdate-add [:clj-rdate.core/months :clj-rdate.core/date-obj] [rd dt]
  (jt/plus dt (jt/months (:period rd))))
(defmethod rdate-add [:clj-rdate.core/years :clj-rdate.core/date-obj] [rd dt]
  (jt/plus dt (jt/years (:period rd))))
(defmethod rdate-add [:clj-rdate.core/weekdays :clj-rdate.core/date-obj] [rd dt]
  (let [weekday-diff (- (jt/as dt :day-of-week) (:weekday rd))
        period-chg (cond (and (< (:period rd) 0) (> weekday-diff 0)) 1 (and (> (:period rd) 0) (< weekday-diff 0)) -1 :else 0)
        period (+ (:period rd) period-chg)]
      (jt/plus dt (jt/days (- (* period 7) weekday-diff)))))
(defmethod rdate-add [:clj-rdate.core/nth-weekdays :clj-rdate.core/date-obj] [rd dt]
  "Get the nth weekday in the given month. Exception if out of bounds"
  (let [wkd (jt/as dt :day-of-week)
        wkd-1st (inc (mod (- (dec wkd) (dec (mod (jt/as dt :day-of-month) 7))) 7))
        wkd-1st-diff (- wkd-1st (:weekday rd))
        period (if (> wkd-1st-diff 0) (:period rd) (dec (:period rd)))
        days (inc (- (* 7 period) wkd-1st-diff))]
    (date-constructor dt (jt/as dt :year) (jt/as dt :month-of-year) days)))
(defmethod rdate-add [:clj-rdate.core/nth-last-weekdays :clj-rdate.core/date-obj] [rd dt]
  "Get the nth last weekday in the given month. Exception if out of bounds"
  (let [ldom (jt/adjust dt :last-day-of-month)
        ldom-dow (jt/as ldom :day-of-week)
        ldom-dow-diff (- ldom-dow (:weekday rd))
        period (if (>= ldom-dow-diff 0) (dec (:period rd)) (:period rd))
        days-to-sub (+ (* period 7) ldom-dow-diff)
        days (- (jt/as ldom :day-of-month) days-to-sub)]
    (date-constructor dt (jt/as dt :year) (jt/as dt :month-of-year) days)))
(defmethod rdate-add [:clj-rdate.core/first-day-of-month :clj-rdate.core/date-obj] [rd dt]
  (jt/adjust dt :first-day-of-month))
(defmethod rdate-add [:clj-rdate.core/last-day-of-month :clj-rdate.core/date-obj] [rd dt]
  (jt/adjust dt :last-day-of-month))
(defmethod rdate-add [:clj-rdate.core/easter-sunday :clj-rdate.core/date-obj] [rd dt]
  (intfn/easter-sunday date-constructor dt (:period rd)))
(defmethod rdate-add [:clj-rdate.core/day-month :clj-rdate.core/date-obj] [rd dt]
  (date-constructor dt (jt/as dt :year) (:month rd) (:day rd)))
(defmethod rdate-add [:clj-rdate.core/compound :clj-rdate.core/date-obj] [rd dt]
  (reduce #(rdate-add %2 %1) dt (:parts rd)))
(defmethod rdate-add [:clj-rdate.core/repeat :clj-rdate.core/date-obj] [rd dt]
  (reduce #(rdate-add %2 %1) dt (repeat (:times rd) (:part rd))))
(defmethod rdate-add [:clj-rdate.core/calendar :clj-rdate.core/date-obj] [rd dt]
  (let [bdc-op (get {:clj-rdate.core/nbd jt/plus :clj-rdate.core/pbd jt/minus} (:bdc rd))
        cal (:cal rd)
        one-day (jt/days 1)]
    (loop [result (rdate-add (:rdate rd) dt)]
      (if (is-not-holiday? cal result) result (recur (bdc-op result one-day))))))
