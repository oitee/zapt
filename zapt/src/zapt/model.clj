(ns zapt.model
  (:require [zapt.core :as zc]
            [taoensso.carmine :as car :refer (wcar)]))

(defn convert-to-micros
  [sec]
  (* sec 1000000))

(defn get-curr-ts
  []
  (Integer/parseInt (first (zc/wcar*
           (car/time)))))

(defn create-store
  "Initialise a store. Accepts an `id` and `window-size`"
  ([]
   (create-store (rand-int 100000)))
  ([id]
   (let [curr-ts (get-curr-ts)
         res (zc/wcar*
              (car/zadd id curr-ts curr-ts))]
     (when (= res 1)
       id))))



(defn add-rem-count
  ([id]
   (add-rem-count id 5))
  ([id window-size]
   "Accepts the `id` of the store and the `window-size` in seconds.
Adds a new element to the set, removes all elements
that's outside the window-size and returns the current count
the store. window-size in seconds"
   (let [curr-ts (get-curr-ts)
         w-size-in-micro (convert-to-micros window-size)
         cut-off-ts (- curr-ts w-size-in-micro)]
     (last (zc/wcar*
            (car/zadd id curr-ts curr-ts)
            (car/zremrangebyscore id 0 cut-off-ts)
            (car/zcount id 0 "inf"))))))



