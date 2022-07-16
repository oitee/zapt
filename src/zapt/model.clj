(ns zapt.model
  (:require [zapt.core :as zc]
            [taoensso.carmine :as car :refer (wcar)]))

(defn convert-to-millis
  [sec]
  (* sec 1000))

(defn get-curr-ts-in-millis
  []
  (System/currentTimeMillis))

(defn create-store
  "Initialise a store. Accepts an `id` and `window-size`"
  ([]
   (create-store (rand-int 100000)))
  ([id]
   (let [curr-ts (get-curr-ts-in-millis)
         res (zc/wcar*
              (car/zadd id curr-ts curr-ts))]
     (when (= res 1)
       id))))

(defn find-count-after-rem
  "Returns the size of the store after removing
  stale elements"
  [id cut-off-ts]
  (last (zc/wcar*
            (car/zremrangebyscore id 0 cut-off-ts)
            (car/zcount id 0 "inf"))))

(comment (defn show-curr-store*
           "Helper fn for testing: Shows all
  elements of given store"
           [id]
           (zc/wcar*
            (car/zrange id 0 100000000000000))))

(comment (defn clear-store*
           "Helper Function for testing: Clears
  a given store"
           [id]
           (zc/wcar*
            (car/zremrangebyscore id 0 "inf"))))

(defn check-size-of-window
  "After calling `find-count-after-rem`, it
  returns `true`, if the count of the store is less than
  `window-limit`. Else, it returns `false`.
  Also, when the no of elements in the store is within bounds, it
  asynchronously adds a new element to the store."
  ([id]
   (check-size-of-window id 5 50))
  ([id window-duration-in-sec window-limit]
   (let [curr-ts (get-curr-ts-in-millis)
         w-duration-in-millis (convert-to-millis window-duration-in-sec)
         cut-off-ts (- curr-ts w-duration-in-millis)
         curr-window-size (find-count-after-rem id cut-off-ts)]
     (or (when (< curr-window-size window-limit)
           (future (zc/wcar*
                    (car/zadd id curr-ts curr-ts)))
           true)
         false))))
