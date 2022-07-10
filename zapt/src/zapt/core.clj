(ns zapt.core
  (:gen-class)
  (:require [taoensso.carmine :as car :refer (wcar)]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def server1-conn {:pool {}
                   :spec {:host "127.0.0.1" :port 9001}})

(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Trying out some basic Redis Comamnds
(wcar* (car/ping))
;;=> "PONG"

(wcar*
 (car/set "my-first-key" "Hello Redis!"))
;;=> "OK"

(wcar*
 (car/get "my-first-key"))
;;=> "Hello Redis!"

(wcar*
 (car/mset "second-k" "R.E." "third-k" "D.I." "fourth-k" "S!"))
;;=> "OK"

(wcar*
 (car/mget "second-k" "third-k" "fourth-k"))
;;=> ["R.E." "D.I." "S!"]
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
